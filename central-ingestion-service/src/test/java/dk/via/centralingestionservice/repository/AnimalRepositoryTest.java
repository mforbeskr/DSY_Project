package dk.via.centralingestionservice.repository;

import dk.via.centralingestionservice.messaging.AnimalRegisteredEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Runs against the real central database. Every test is rolled back afterwards.
 */
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(AnimalRepository.class)
class AnimalRepositoryTest {

    @Autowired
    private AnimalRepository repository;

    @Autowired
    private JdbcClient jdbcClient;

    private static final AnimalRegisteredEvent EVENT =
            new AnimalRegisteredEvent("TEST-001", LocalDateTime.of(2030, 1, 1, 12, 0), "TEST-FARM", 100);

    private int countAnimals(String registrationNumber) {
        return jdbcClient.sql("SELECT COUNT(*) FROM animal WHERE registration_number = ?")
                .param(registrationNumber)
                .query(Integer.class)
                .single();
    }

    @Test
    void insert_storesTheAnimal() {
        assertEquals(1, repository.insert(EVENT));
        assertEquals(1, countAnimals("TEST-001"));
    }

    @Test
    void insert_ignoresAnAnimalThatIsAlreadyThere() {
        repository.insert(EVENT);

        assertEquals(0, repository.insert(EVENT));
        assertEquals(1, countAnimals("TEST-001"));
    }

    @Test
    void findByRegistrationNumber_findsAnInsertedAnimal() {
        repository.insert(EVENT);

        assertEquals("TEST-001", repository.findByRegistrationNumber("TEST-001").orElseThrow().registrationNumber());
    }

    @Test
    void findByRegistrationNumber_ignoresCase() {
        repository.insert(EVENT);

        assertTrue(repository.findByRegistrationNumber("test-001").isPresent());
    }

    @Test
    void findByRegistrationNumber_returnsEmptyForUnknownNumber() {
        assertTrue(repository.findByRegistrationNumber("TEST-UNKNOWN").isEmpty());
    }
}
