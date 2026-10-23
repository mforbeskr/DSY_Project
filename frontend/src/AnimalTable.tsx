import type {Animal} from './types'

interface Props {
    animals: Animal[]
    onSelect: (animal: Animal) => void
}

function formatDate(arrivalDate: string) {
    return arrivalDate.replace('T', ' ').slice(0, 16)
}

function AnimalTable({animals, onSelect}: Props) {
    return (
        <>
            <div className="results-header">
                <h2>Results</h2>
                <span className="total">Total results<strong>{animals.length}</strong></span>
            </div>

            {animals.length === 0 ? (
                <p className="message">No animals found</p>
            ) : (
                <table>
                    <thead>
                    <tr>
                        <th>Id</th>
                        <th>Registration no.</th>
                        <th>Arrival</th>
                        <th>Origin</th>
                        <th className="number">Weight</th>
                        <th>Status</th>
                    </tr>
                    </thead>
                    <tbody>
                    {animals.map((animal) => (
                        <tr key={animal.animalId} className="clickable" title="Show data flow"
                            onClick={() => onSelect(animal)}>
                            <td className="muted">{animal.animalId}</td>
                            <td>{animal.registrationNumber}</td>
                            <td>{formatDate(animal.arrivalDate)}</td>
                            <td>{animal.origin}</td>
                            <td className="number">{animal.weight} kg</td>
                            <td>
                                <span className={animal.synced ? 'badge synced' : 'badge pending'}>
                                    {animal.synced ? 'Synced' : 'Pending'}
                                </span>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </>
    )
}

export default AnimalTable
