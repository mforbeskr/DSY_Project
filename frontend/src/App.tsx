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
    const [drafting, setDrafting] = useState(false)

    function showResult(found: Animal[]) {
        setAnimals(found)
        setError('')
    }

    function draftChanged(isDrafting: boolean) {
        setDrafting(isDrafting)
        if (isDrafting) setTracked(null)
    }

    function registered(animal: Animal) {
        setLastRegistered(animal)
        setDrafting(false)
        setTracked(animal)
    }

    return (
        <main>
            <h1>Slaughterhouse</h1>

            <div className="layout">
                <div>
                    <section className="card">
                        <h2>Register animal</h2>
                        <RegisterForm onRegistered={registered} onDraftChange={draftChanged}/>
                        {lastRegistered && (
                            <p className="success">
                                Registered {lastRegistered.registrationNumber} with id {lastRegistered.animalId}
                            </p>
                        )}
                    </section>

                    <section className="card">
                        <h2>Search animals</h2>
                        <SearchBar onResult={showResult} onError={setError}/>
                        {error && <p className="error">{error}</p>}
                        {animals && <AnimalTable animals={animals} onSelect={setTracked}/>}
                    </section>
                </div>

                <aside className="flow-panel">
                    <FlowView animal={tracked} drafting={drafting && !tracked}/>
                </aside>
            </div>
        </main>
    )
}

export default App
