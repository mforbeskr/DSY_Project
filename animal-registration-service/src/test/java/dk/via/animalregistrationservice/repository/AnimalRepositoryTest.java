package dk.via.animalregistrationservice.repository;

import dk.via.animalregistrationservice.model.Animal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Runs the derived queries against the real PostgreSQL database from application.properties.
 * Every test runs in a transaction that is rolled back afterwards, so nothing is left in the database.
 * The test animals use their own farm and year, so the existing test data doesn't affect the results.
 */
@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=validate")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AnimalRepositoryTest {

    private static final String FARM = "TEST-FARM";

    @Autowired
    private AnimalRepository repository;

    private Animal save(String registrationNumber, LocalDateTime arrivalDate) {
        return repository.save(new Animal(registrationNumber, arrivalDate, FARM, 100));
    }

    private static List<String> registrationNumbers(List<Animal> animals) {
        return animals.stream().map(Animal::getRegistrationNumber).sorted().toList();
    }

    @Test
    void save_givesTheAnimalAnIdFromTheDatabase() {
        Animal saved = save("TEST-001", LocalDateTime.of(2030, 1, 1, 12, 0));

        assertTrue(saved.getAnimalId() > 0);
    }

    @Test
    void findAnimalByRegistrationNumber_findsTheAnimal() {
        save("TEST-001", LocalDateTime.of(2030, 1, 1, 12, 0));

        assertEquals(FARM, repository.findAnimalByRegistrationNumber("TEST-001").orElseThrow().getOrigin());
    }

    @Test
    void findAnimalByRegistrationNumber_returnsEmptyForUnknownNumber() {
        assertTrue(repository.findAnimalByRegistrationNumber("TEST-UNKNOWN").isEmpty());
    }

    @Test
    void findAnimalsByOrigin_returnsOnlyAnimalsFromThatFarm() {
        save("TEST-001", LocalDateTime.of(2030, 1, 1, 12, 0));
        save("TEST-002", LocalDateTime.of(2030, 1, 2, 12, 0));
        repository.save(new Animal("TEST-003", LocalDateTime.of(2030, 1, 1, 12, 0), "OTHER-TEST-FARM", 100));

        assertEquals(List.of("TEST-001", "TEST-002"), registrationNumbers(repository.findAnimalsByOrigin(FARM)));
    }

    @Test
    void findAnimalsByArrivalDate_includesStartAndExcludesEnd() {
        save("TEST-START", LocalDateTime.of(2030, 1, 1, 0, 0));
        save("TEST-LATE", LocalDateTime.of(2030, 1, 1, 23, 59, 59));
        save("TEST-NEXT-DAY", LocalDateTime.of(2030, 1, 2, 0, 0));
        save("TEST-DAY-BEFORE", LocalDateTime.of(2029, 12, 31, 23, 59, 59));

        List<Animal> found = repository.findAnimalsByArrivalDateGreaterThanEqualAndArrivalDateLessThan(
                LocalDateTime.of(2030, 1, 1, 0, 0),
                LocalDateTime.of(2030, 1, 2, 0, 0));

        assertEquals(List.of("TEST-LATE", "TEST-START"), registrationNumbers(found));
    }

    @Test
    void findAnimalsBySyncedFalse_returnsOnlyUnsyncedAnimals() {
        save("TEST-UNSYNCED", LocalDateTime.of(2030, 1, 1, 12, 0));
        Animal synced = save("TEST-SYNCED", LocalDateTime.of(2030, 1, 1, 12, 0));
        synced.markSynced();
        repository.save(synced);

        List<Animal> unsynced = repository.findAnimalsBySyncedFalse().stream()
                .filter(animal -> animal.getOrigin().equals(FARM))
                .toList();

        assertEquals(List.of("TEST-UNSYNCED"), registrationNumbers(unsynced));
    }
}
