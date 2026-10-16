package dk.via.animalregistrationservice.controller;

import java.time.LocalDateTime;

public record RegisterAnimalRequest(String registrationNumber, LocalDateTime arrivalDate, String origin, double weight) {
}
