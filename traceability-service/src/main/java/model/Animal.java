package model;

import java.time.LocalDate;

public class Animal
{
    private final int animalId;
    private final String registrationNumber;
    private final LocalDate arrivalDate;
    private final double weight;

    public Animal(int animalId, String registrationNumber, LocalDate arrivalDate, double weight)
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

    public LocalDate getArrivalDate()
    {
        return arrivalDate;
    }

    public double getWeight()
    {
        return weight;
    }
}
