import {useState} from 'react'
import type {FormEvent} from 'react'
import {getAnimalByRegistrationNumber, getAnimalsByDate, getAnimalsByOrigin} from './api'
import type {Animal} from './types'

type SearchType = 'registrationNumber' | 'date' | 'origin'

const searchTypes: Record<SearchType, { label: string, placeholder: string, inputType: string }> = {
    registrationNumber: {label: 'Registration no.', placeholder: 'e.g. REG001', inputType: 'text'},
    date: {label: 'Arrival date', placeholder: '', inputType: 'date'},
    origin: {label: 'Origin', placeholder: 'e.g. FARM001', inputType: 'text'},
}

interface Props {
    onResult: (animals: Animal[]) => void
    onError: (message: string) => void
}

async function find(type: SearchType, value: string): Promise<Animal[]> {
    if (type === 'date') return getAnimalsByDate(value)
    if (type === 'origin') return getAnimalsByOrigin(value)

    const animal = await getAnimalByRegistrationNumber(value)
    return animal ? [animal] : []
}

function SearchBar({onResult, onError}: Props) {
    const [type, setType] = useState<SearchType>('origin')
    const [value, setValue] = useState('')

    async function submit(e: FormEvent) {
        e.preventDefault()
        if (!value) return
        try {
            onResult(await find(type, value))
        } catch (e) {
            onError(String(e))
        }
    }

    function changeType(newType: SearchType) {
        setType(newType)
        setValue('')
    }

    return (
        <form className="search-bar" onSubmit={submit}>
            <select value={type} onChange={(e) => changeType(e.target.value as SearchType)}>
                {Object.entries(searchTypes).map(([key, searchType]) => (
                    <option key={key} value={key}>{searchType.label}</option>
                ))}
            </select>
            <input type={searchTypes[type].inputType} placeholder={searchTypes[type].placeholder}
                   value={value} onChange={(e) => setValue(e.target.value)}/>
            <button type="submit">Search</button>
        </form>
    )
}

export default SearchBar
