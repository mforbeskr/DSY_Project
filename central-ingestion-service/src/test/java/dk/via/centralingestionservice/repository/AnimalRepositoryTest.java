package dk.via.centralingestionservice.repository;

import dk.via.centralingestionservice.model.Animal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Runs against the real central database. Every test is rolled back afterwards.
 * ddl-auto=validate so the test never changes the tables.
 */
@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=validate")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AnimalRepositoryTest {

    @Autowired
    private AnimalRepository repository;

    private Animal save(String registrationNumber) {
        return repository.save(new Animal(registrationNumber, LocalDateTime.of(2030, 1, 1, 12, 0), "TEST-FARM", 100));
    }

    @Test
    void save_givesTheAnimalAnIdFromTheDatabase() {
        assertTrue(save("TEST-001").getAnimalId() > 0);
    }

    @Test
    void findAnimalByRegistrationNumber_findsASavedAnimal() {
        save("TEST-001");

        assertEquals("TEST-FARM", repository.findAnimalByRegistrationNumberIgnoreCase("TEST-001").orElseThrow().getOrigin());
    }

    @Test
    void findAnimalByRegistrationNumber_ignoresCase() {
        save("TEST-001");

        assertTrue(repository.findAnimalByRegistrationNumberIgnoreCase("test-001").isPresent());
    }

    @Test
    void findAnimalByRegistrationNumber_returnsEmptyForUnknownNumber() {
        assertTrue(repository.findAnimalByRegistrationNumberIgnoreCase("TEST-UNKNOWN").isEmpty());
    }

    @Test
    void existsByRegistrationNumber_isTrueOnlyForSavedAnimals() {
        save("TEST-001");

        assertTrue(repository.existsByRegistrationNumberIgnoreCase("test-001"));
        assertFalse(repository.existsByRegistrationNumberIgnoreCase("TEST-UNKNOWN"));
    }
}
