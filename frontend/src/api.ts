import type {Animal, CentralAnimal, RegisterAnimalRequest} from './types'

// asks the Central Ingestion Service (port 8081) whether the animal has reached the central database
export async function getCentralAnimal(registrationNumber: string): Promise<CentralAnimal | null> {
    const response = await fetch(`/central/animals/${encodeURIComponent(registrationNumber)}`)
    if (response.status === 404) {
        return null
    }
    if (!response.ok) {
        throw new Error(`Central lookup failed: ${response.status}`)
    }
    return response.json()
}

async function getAnimals(query: string): Promise<Animal[]> {
    const response = await fetch(`/api/animals?${query}`)
    if (!response.ok) {
        throw new Error(`Search failed: ${response.status}`)
    }
    return response.json()
}

// 404 means no animal was found, so it returns null instead of throwing
async function getAnimal(url: string): Promise<Animal | null> {
    const response = await fetch(url)
    if (response.status === 404) {
        return null
    }
    if (!response.ok) {
        throw new Error(`Search failed: ${response.status}`)
    }
    return response.json()
}

export function getAnimalByRegistrationNumber(registrationNumber: string): Promise<Animal | null> {
    return getAnimal(`/api/animals/${encodeURIComponent(registrationNumber)}`)
}

export function getAnimalsByDate(date: string): Promise<Animal[]> {
    return getAnimals(`date=${encodeURIComponent(date)}`)
}

export function getAnimalsByOrigin(origin: string): Promise<Animal[]> {
    return getAnimals(`origin=${encodeURIComponent(origin)}`)
}

export async function registerAnimal(request: RegisterAnimalRequest): Promise<Animal> {
    const response = await fetch('/api/animals', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(request),
    })
    if (!response.ok) {
        throw new Error(await response.text())
    }
    return response.json()
}
