package dk.via.animalregistrationservice.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnimalTest {

    @Test
    void animal_exposesItsValues() {
        Animal animal = new Animal("REG001", LocalDateTime.of(2026, 9, 18, 7, 30), "FARM001", 120.5);

        assertEquals("REG001", animal.getRegistrationNumber());
        assertEquals(LocalDateTime.of(2026, 9, 18, 7, 30), animal.getArrivalDate());
        assertEquals("FARM001", animal.getOrigin());
        assertEquals(120.5, animal.getWeight());
    }

    @Test
    void animal_hasNoIdUntilTheDatabaseGivesItOne() {
        Animal animal = new Animal("REG001", LocalDateTime.of(2026, 9, 18, 7, 30), "FARM001", 120.5);

        assertEquals(0, animal.getAnimalId());
    }
}
