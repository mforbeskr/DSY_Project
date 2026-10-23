export interface Animal {
    animalId: number
    registrationNumber: string
    arrivalDate: string
    origin: string
    weight: number
    synced: boolean
}

export interface CentralAnimal {
    animalId: number
    registrationNumber: string
}

export interface RegisterAnimalRequest {
    registrationNumber: string
    arrivalDate: string
    origin: string
    weight: number
}
