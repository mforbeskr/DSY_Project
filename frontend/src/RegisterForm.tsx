import {useState} from 'react'
import type {FormEvent} from 'react'
import {registerAnimal} from './api'
import type {Animal} from './types'

interface Props {
    onRegistered: (animal: Animal) => void
}

function RegisterForm({onRegistered}: Props) {
    const [registrationNumber, setRegistrationNumber] = useState('')
    const [arrivalDate, setArrivalDate] = useState('')
    const [origin, setOrigin] = useState('')
    const [weight, setWeight] = useState('')
    const [error, setError] = useState('')

    async function submit(e: FormEvent) {
        e.preventDefault()
        try {
            const animal = await registerAnimal({registrationNumber, arrivalDate, origin, weight: Number(weight)})
            setError('')
            onRegistered(animal)
        } catch (e) {
            setError(String(e))
        }
    }

    return (
        <form className="register-form" onSubmit={submit}>
            <input placeholder="Registration number" value={registrationNumber}
                   onChange={(e) => setRegistrationNumber(e.target.value)}/>
            <input type="datetime-local" value={arrivalDate} onChange={(e) => setArrivalDate(e.target.value)}/>
            <input placeholder="Origin (farm)" value={origin} onChange={(e) => setOrigin(e.target.value)}/>
            <input type="number" placeholder="Weight (kg)" value={weight} onChange={(e) => setWeight(e.target.value)}/>
            <button type="submit">Register</button>
            {error && <p className="error">{error}</p>}
        </form>
    )
}

export default RegisterForm
