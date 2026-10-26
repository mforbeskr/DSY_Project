package dk.via.animalregistrationservice.service;

import dk.via.animalregistrationservice.model.Animal;
import dk.via.animalregistrationservice.repository.AnimalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AnimalRegService {

    private final AnimalRepository repository;

    // Spring passes in its own implementation of AnimalRepository (constructor injection)
    public AnimalRegService(AnimalRepository repository) {
        this.repository = repository;
    }

    public Animal register(Animal animal) {
        if (animal.getWeight() <= 0) {
            throw new IllegalArgumentException("Weight must be greater than 0");
        }
        if (repository.findAnimalByRegistrationNumber(animal.getRegistrationNumber()).isPresent()) {
            throw new IllegalArgumentException(
                    "Animal with registration number " + animal.getRegistrationNumber() + " already exists");
        }

        // save() returns the stored animal, now with the id the database generated
        return repository.save(animal);
    }

    public Optional<Animal> findByRegistrationNumber(String registrationNumber) {
        return repository.findAnimalByRegistrationNumber(registrationNumber);
    }

    // arrival_date holds a time as well, so one date becomes the range [that day 00:00, next day 00:00)
    public List<Animal> findByArrivalDate(LocalDate date) {
        LocalDateTime from = date.atStartOfDay();
        LocalDateTime to = date.plusDays(1).atStartOfDay();

        return repository.findAnimalsByArrivalDateGreaterThanEqualAndArrivalDateLessThan(from, to);
    }

    public List<Animal> findByOrigin(String origin) {
        return repository.findAnimalsByOrigin(origin);
    }
}
