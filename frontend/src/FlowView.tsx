import {useEffect, useState} from 'react'
import {Position, ReactFlow} from '@xyflow/react'
import type {Edge, Node} from '@xyflow/react'
import '@xyflow/react/dist/style.css'
import {getAnimalById, getCentralAnimal} from './api'
import type {Animal, CentralAnimal} from './types'

type Stage = 'local' | 'queued' | 'central'

const steps = [
    {id: 'slaughterer', title: 'Slaughterer', tech: 'Person', x: 0, y: 0, from: Position.Left, to: Position.Right},
    {id: 'registration', title: 'Registration Service', tech: 'Spring Boot · REST', x: 260, y: 0, from: Position.Left, to: Position.Right},
    {id: 'station1', title: 'Station 1 DB', tech: 'PostgreSQL · local', x: 520, y: 0, from: Position.Left, to: Position.Bottom},
    {id: 'queue', title: 'RabbitMQ', tech: 'Queue · AMQP', x: 520, y: 150, from: Position.Top, to: Position.Left},
    {id: 'ingestion', title: 'Central Ingestion', tech: 'Spring Boot', x: 260, y: 150, from: Position.Right, to: Position.Left},
    {id: 'central', title: 'Central DB', tech: 'PostgreSQL', x: 0, y: 150, from: Position.Right, to: Position.Left},
]

// how many steps the animal has passed, and which step it is waiting at
const progress: Record<Stage, { done: number, waiting: string }> = {
    local: {done: 3, waiting: 'queue'},
    queued: {done: 4, waiting: 'ingestion'},
    central: {done: 6, waiting: ''},
}

function stateOf(index: number, stage: Stage) {
    if (index < progress[stage].done) return 'done'
    if (steps[index].id === progress[stage].waiting) return 'waiting'
    return 'idle'
}

function statusText(stage: Stage, animal: Animal, central: CentralAnimal | null) {
    if (stage === 'local') return 'Stored in Station 1 DB. Waiting to be sent to RabbitMQ. If it stays here, RabbitMQ is down.'
    if (stage === 'queued') return 'Sent to RabbitMQ. Waiting for Central Ingestion to pick it up.'
    return `Arrived in the central database. Local id ${animal.animalId}, central id ${central?.animalId}.`
}

interface Props {
    animal: Animal
}

function FlowView({animal}: Props) {
    const [stage, setStage] = useState<Stage>(animal.synced ? 'queued' : 'local')
    const [central, setCentral] = useState<CentralAnimal | null>(null)

    useEffect(() => {
        let stopped = false

        async function check() {
            const local = await getAnimalById(String(animal.animalId))
            const found = await getCentralAnimal(animal.registrationNumber)
            if (stopped) return
            setCentral(found)
            setStage(found ? 'central' : local?.synced ? 'queued' : 'local')
            if (found) clearInterval(timer)
        }

        const timer = setInterval(check, 1000)
        check()
        return () => {
            stopped = true
            clearInterval(timer)
        }
    }, [animal])

    const nodes: Node[] = steps.map((step, index) => ({
        id: step.id,
        position: {x: step.x, y: step.y},
        targetPosition: step.from,
        sourcePosition: step.to,
        className: `flow-node ${stateOf(index, stage)}`,
        data: {
            label: (
                <>
                    <strong>{step.title}</strong>
                    <span>{step.tech}</span>
                </>
            ),
        },
    }))

    const edges: Edge[] = steps.slice(1).map((step, index) => ({
        id: `${steps[index].id}-${step.id}`,
        source: steps[index].id,
        target: step.id,
        animated: stateOf(index + 1, stage) === 'waiting',
        className: stateOf(index + 1, stage),
    }))

    return (
        <>
            <div className="results-header">
                <h2>Data flow · {animal.registrationNumber}</h2>
                <span className={`badge ${stage === 'central' ? 'synced' : 'pending'}`}>
                    {stage === 'central' ? 'Arrived' : 'In transit'}
                </span>
            </div>
            <div className="flow">
                <ReactFlow nodes={nodes} edges={edges} fitView nodesDraggable={false} nodesConnectable={false}
                           elementsSelectable={false} panOnDrag={false} zoomOnScroll={false} zoomOnDoubleClick={false}/>
            </div>
            <p className="flow-status">{statusText(stage, animal, central)}</p>
        </>
    )
}

export default FlowView
