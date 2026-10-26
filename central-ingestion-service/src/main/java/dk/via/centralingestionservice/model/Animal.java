package dk.via.centralingestionservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Animal {
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id
    private int animalId;
    @Column(nullable = false, unique = true, length = 50)
    private String registrationNumber;
    @Column(nullable = false)
    private LocalDateTime arrivalDate;
    @Column(nullable = false, length = 50)
    private String origin;
    @Column(nullable = false)
    private double weight;

    public Animal() {
    }

    public Animal(String registrationNumber, LocalDateTime arrivalDate, String origin, double weight) {
        this.registrationNumber = registrationNumber;
        this.arrivalDate = arrivalDate;
        this.origin = origin;
        this.weight = weight;
    }

    public int getAnimalId() {
        return animalId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public LocalDateTime getArrivalDate() {
        return arrivalDate;
    }

    public String getOrigin() {
        return origin;
    }

    public double getWeight() {
        return weight;
    }
}
