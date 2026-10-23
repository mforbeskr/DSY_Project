import {useState} from 'react'
import type {FormEvent} from 'react'
import {registerAnimal} from './api'
import type {Animal} from './types'

interface Props {
    onRegistered: (animal: Animal) => void
    onDraftChange: (drafting: boolean) => void
}

const emptyForm = {registrationNumber: '', arrivalDate: '', origin: '', weight: ''}

function RegisterForm({onRegistered, onDraftChange}: Props) {
    const [form, setForm] = useState(emptyForm)
    const [error, setError] = useState('')

    function change(field: keyof typeof emptyForm, value: string) {
        const next = {...form, [field]: value}
        setForm(next)
        onDraftChange(Object.values(next).some((v) => v !== ''))
    }

    async function submit(e: FormEvent) {
        e.preventDefault()
        try {
            const animal = await registerAnimal({...form, weight: Number(form.weight)})
            setError('')
            setForm(emptyForm)
            onRegistered(animal)
        } catch (e) {
            setError(String(e))
        }
    }

    return (
        <form className="register-form" onSubmit={submit}>
            <input placeholder="Registration number" value={form.registrationNumber}
                   onChange={(e) => change('registrationNumber', e.target.value)}/>
            <input type="datetime-local" value={form.arrivalDate}
                   onChange={(e) => change('arrivalDate', e.target.value)}/>
            <input placeholder="Origin (farm)" value={form.origin}
                   onChange={(e) => change('origin', e.target.value)}/>
            <input type="number" placeholder="Weight (kg)" value={form.weight}
                   onChange={(e) => change('weight', e.target.value)}/>
            <button type="submit">Register</button>
            {error && <p className="error">{error}</p>}
        </form>
    )
}

export default RegisterForm
