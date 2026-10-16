package model;

import java.time.LocalDateTime;

public class Animal
{
    private final int animalId;
    private final String registrationNumber;
    private final LocalDateTime arrivalDate;
    private final double weight;

    public Animal(int animalId, String registrationNumber, LocalDateTime arrivalDate, double weight)
    {
        this.animalId = animalId;
        this.registrationNumber = registrationNumber;
        this.arrivalDate = arrivalDate;
        this.weight = weight;
    }

    public int getAnimalId()
    {
        return animalId;
    }

    public String getRegistrationNumber()
    {
        return registrationNumber;
    }

    public LocalDateTime getArrivalDate()
    {
        return arrivalDate;
    }

    public double getWeight()
    {
        return weight;
    }
}
