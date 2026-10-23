import {useState} from 'react'
import AnimalTable from './AnimalTable'
import FlowView from './FlowView'
import RegisterForm from './RegisterForm'
import SearchBar from './SearchBar'
import type {Animal} from './types'

function App() {
    const [animals, setAnimals] = useState<Animal[] | null>(null)
    const [error, setError] = useState('')
    const [lastRegistered, setLastRegistered] = useState<Animal | null>(null)
    const [tracked, setTracked] = useState<Animal | null>(null)

    function showResult(found: Animal[]) {
        setAnimals(found)
        setError('')
    }

    function registered(animal: Animal) {
        setLastRegistered(animal)
        setTracked(animal)
    }

    return (
        <main>
            <h1>Slaughterhouse</h1>

            <section className="card">
                <h2>Register animal</h2>
                <RegisterForm onRegistered={registered}/>
                {lastRegistered && (
                    <p className="success">
                        Registered {lastRegistered.registrationNumber} with id {lastRegistered.animalId}
                    </p>
                )}
            </section>

            {tracked && (
                <section className="card">
                    <FlowView key={tracked.animalId} animal={tracked}/>
                </section>
            )}

            <section className="card">
                <h2>Search animals</h2>
                <SearchBar onResult={showResult} onError={setError}/>
                {error && <p className="error">{error}</p>}
                {animals && <AnimalTable animals={animals} onSelect={setTracked}/>}
            </section>
        </main>
    )
}

export default App
