package dk.via.animalregistrationservice.service;

import dk.via.animalregistrationservice.model.Animal;
import dk.via.animalregistrationservice.repository.AnimalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the rules in the service on their own. The repository is a Mockito mock,
 * so no database is needed.
 */
class AnimalRegServiceTest {

    private AnimalRepository repository;
    private AnimalRegService service;

    @BeforeEach
    void setUp() {
        repository = mock(AnimalRepository.class);
        service = new AnimalRegService(repository);
    }

    private static Animal animal(String registrationNumber, double weight) {
        return new Animal(registrationNumber, LocalDateTime.of(2026, 9, 18, 7, 30), "FARM001", weight);
    }

    @Test
    void register_savesAndReturnsTheStoredAnimal() {
        Animal animal = animal("REG001", 120.5);
        Animal stored = animal("REG001", 120.5);
        when(repository.findAnimalByRegistrationNumber("REG001")).thenReturn(Optional.empty());
        when(repository.save(animal)).thenReturn(stored);

        assertSame(stored, service.register(animal));
    }

    @Test
    void register_rejectsZeroWeight() {
        assertThrows(IllegalArgumentException.class, () -> service.register(animal("REG001", 0)));

        verify(repository, never()).save(any());
    }

    @Test
    void register_rejectsNegativeWeight() {
        assertThrows(IllegalArgumentException.class, () -> service.register(animal("REG001", -5)));

        verify(repository, never()).save(any());
    }

    @Test
    void register_rejectsDuplicateRegistrationNumber() {
        when(repository.findAnimalByRegistrationNumber("REG001")).thenReturn(Optional.of(animal("REG001", 100)));

        assertThrows(IllegalArgumentException.class, () -> service.register(animal("REG001", 120.5)));

        verify(repository, never()).save(any());
    }

    @Test
    void findById_returnsWhatTheRepositoryFinds() {
        Animal animal = animal("REG001", 120.5);
        when(repository.findById(1)).thenReturn(Optional.of(animal));

        assertEquals(Optional.of(animal), service.findById(1));
    }

    @Test
    void findById_returnsEmptyForUnknownAnimal() {
        when(repository.findById(999)).thenReturn(Optional.empty());

        assertEquals(Optional.empty(), service.findById(999));
    }

    @Test
    void findByRegistrationNumber_returnsWhatTheRepositoryFinds() {
        Animal animal = animal("REG001", 120.5);
        when(repository.findAnimalByRegistrationNumber("REG001")).thenReturn(Optional.of(animal));

        assertEquals(Optional.of(animal), service.findByRegistrationNumber("REG001"));
    }

    @Test
    void findByArrivalDate_searchesFromStartOfDayUntilStartOfNextDay() {
        service.findByArrivalDate(LocalDate.of(2026, 9, 18));

        verify(repository).findAnimalsByArrivalDateGreaterThanEqualAndArrivalDateLessThan(
                LocalDateTime.of(2026, 9, 18, 0, 0),
                LocalDateTime.of(2026, 9, 19, 0, 0));
    }

    @Test
    void findByArrivalDate_handlesTheLastDayOfTheYear() {
        service.findByArrivalDate(LocalDate.of(2026, 12, 31));

        verify(repository).findAnimalsByArrivalDateGreaterThanEqualAndArrivalDateLessThan(
                LocalDateTime.of(2026, 12, 31, 0, 0),
                LocalDateTime.of(2027, 1, 1, 0, 0));
    }

    @Test
    void findByOrigin_returnsWhatTheRepositoryFinds() {
        List<Animal> animals = List.of(animal("REG001", 120.5));
        when(repository.findAnimalsByOrigin("FARM001")).thenReturn(animals);

        assertEquals(animals, service.findByOrigin("FARM001"));
    }
}
