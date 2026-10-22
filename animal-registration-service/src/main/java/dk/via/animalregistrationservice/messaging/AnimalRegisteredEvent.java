package dk.via.animalregistrationservice.messaging;

import java.time.LocalDateTime;

public record AnimalRegisteredEvent(String registrationNumber, LocalDateTime arrivalDate, String origin, double weight) {
}
