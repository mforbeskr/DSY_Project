import {useEffect, useState} from 'react'
import {BaseEdge, getBezierPath, MarkerType, Position, ReactFlow} from '@xyflow/react'
import type {Edge, EdgeProps, Node} from '@xyflow/react'
import '@xyflow/react/dist/style.css'
import {getAnimalById, getCentralAnimal} from './api'
import type {Animal, CentralAnimal} from './types'

type Stage = 'idle' | 'drafting' | 'local' | 'queued' | 'central'

// left column goes down, right column goes up
const steps = [
    {id: 'slaughterer', title: 'Slaughterer', tech: 'Person', x: 0, y: 0, from: Position.Top, to: Position.Bottom},
    {id: 'registration', title: 'Registration Service', tech: 'Spring Boot · REST', x: 0, y: 120, from: Position.Top, to: Position.Bottom},
    {id: 'station1', title: 'Station 1 DB', tech: 'PostgreSQL · local', x: 0, y: 240, from: Position.Top, to: Position.Right},
    {id: 'queue', title: 'RabbitMQ', tech: 'Queue · AMQP', x: 230, y: 240, from: Position.Left, to: Position.Top},
    {id: 'ingestion', title: 'Central Ingestion', tech: 'Spring Boot', x: 230, y: 120, from: Position.Bottom, to: Position.Top},
    {id: 'central', title: 'Central DB', tech: 'PostgreSQL', x: 230, y: 0, from: Position.Bottom, to: Position.Top},
]

// which step the animal is at for each stage
const stepOf: Record<Stage, number> = {idle: -1, drafting: 0, local: 2, queued: 3, central: 5}

const activeTexts = ['Registering…', 'Validating…', 'Waiting for RabbitMQ', 'Waiting for ingestion', 'Writing…', 'Arrived']

const statusTexts: Record<Stage, string> = {
    idle: 'Start a registration to follow the animal through the system.',
    drafting: 'Filling in the registration.',
    local: 'Stored in Station 1 DB. Waiting to be sent to RabbitMQ. If it stays here, RabbitMQ is down.',
    queued: 'Sent to RabbitMQ. Waiting for Central Ingestion to pick it up.',
    central: 'Arrived in the central database.',
}

const arrowColors = {done: '#03b26c', waiting: '#f59e0b'}

const STEP_DELAY = 800

// an arrow with a small dot (the data) travelling along it while it is in use
function PacketEdge({id, sourceX, sourceY, targetX, targetY, sourcePosition, targetPosition, markerEnd, data}: EdgeProps) {
    const [path] = getBezierPath({sourceX, sourceY, targetX, targetY, sourcePosition, targetPosition})
    return (
        <>
            <BaseEdge id={id} path={path} markerEnd={markerEnd}/>
            {data?.moving === true && (
                <circle r="4" className="packet">
                    <animateMotion dur="1.4s" begin="0.6s" repeatCount="indefinite" path={path}/>
                </circle>
            )}
        </>
    )
}

const edgeTypes = {packet: PacketEdge}

// asks both services every second where the animal is, until it has reached the central database
function useStage(animal: Animal | null, drafting: boolean) {
    const [polled, setPolled] = useState<{ id: number, stage: Stage, central: CentralAnimal | null } | null>(null)

    useEffect(() => {
        if (!animal) return
        const tracked = animal
        let stopped = false

        // a service that is down just means the animal hasn't got that far yet
        async function check() {
            const local = await getAnimalById(String(tracked.animalId)).catch(() => null)
            const central = await getCentralAnimal(tracked.registrationNumber).catch(() => null)
            if (stopped) return
            const stage: Stage = central ? 'central' : local?.synced ? 'queued' : 'local'
            setPolled({id: tracked.animalId, stage, central})
            if (central) clearInterval(timer)
        }

        const timer = setInterval(check, 1000)
        check()
        return () => {
            stopped = true
            clearInterval(timer)
        }
    }, [animal])

    if (!animal) return {stage: (drafting ? 'drafting' : 'idle') as Stage, central: null}
    if (polled?.id === animal.animalId) return polled
    return {stage: (animal.synced ? 'queued' : 'local') as Stage, central: null}
}

interface Props {
    animal: Animal | null
    drafting: boolean
}

function FlowView({animal, drafting}: Props) {
    const {stage, central} = useStage(animal, drafting)
    const key = animal ? animal.animalId : 'draft'
    const target = stepOf[stage]

    // how far the animation has come; it moves one step at a time towards the target
    const [progress, setProgress] = useState({key, shown: 0})
    const shown = Math.min(progress.key === key ? progress.shown : 0, target)

    useEffect(() => {
        if (shown >= target) return
        const timer = setTimeout(() => setProgress({key, shown: shown + 1}), STEP_DELAY)
        return () => clearTimeout(timer)
    }, [key, shown, target])

    function stateOf(index: number) {
        if (index > shown) return 'hidden'
        if (index === 0 && stage !== 'drafting') return 'done'
        if (index < shown || (index === 5 && stage === 'central')) return 'done'
        return 'waiting'
    }

    const nodes: Node[] = steps.map((step, index) => ({
        id: step.id,
        position: {x: step.x, y: step.y},
        targetPosition: step.from,
        sourcePosition: step.to,
        className: `flow-node ${stateOf(index)}`,
        data: {
            label: (
                <>
                    <strong>{step.title}</strong>
                    <span>{stateOf(index) === 'waiting' || index === 5 ? activeTexts[index] : step.tech}</span>
                </>
            ),
        },
    }))

    const edges: Edge[] = steps.slice(1).map((step, index) => {
        const state = stateOf(index + 1)
        return {
            id: `${steps[index].id}-${step.id}`,
            type: 'packet',
            source: steps[index].id,
            target: step.id,
            className: state,
            data: {moving: state === 'waiting'},
            markerEnd: {type: MarkerType.ArrowClosed, color: state === 'done' ? arrowColors.done : arrowColors.waiting},
        }
    })

    const status = shown < target ? 'On its way…' : statusTexts[stage]
    const detail = central && shown === 5 ? `Local id ${animal?.animalId} · central id ${central.animalId}` : ''

    return (
        <>
            <div className="flow">
                <ReactFlow nodes={nodes} edges={edges} edgeTypes={edgeTypes}
                           fitView fitViewOptions={{padding: 0.08}}
                           nodesDraggable={false} nodesConnectable={false} elementsSelectable={false}
                           panOnDrag={false} zoomOnScroll={false} zoomOnPinch={false} zoomOnDoubleClick={false}
                           preventScrolling={false}/>
            </div>
            {stage !== 'idle' && (
                <p className="flow-status">
                    {animal && <strong>{animal.registrationNumber} · </strong>}{status}{detail && <><br/>{detail}</>}
                </p>
            )}
        </>
    )
}

export default FlowView
