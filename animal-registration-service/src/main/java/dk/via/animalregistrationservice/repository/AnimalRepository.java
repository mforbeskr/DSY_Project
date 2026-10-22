package dk.via.animalregistrationservice.repository;

import dk.via.animalregistrationservice.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AnimalRepository extends JpaRepository<Animal, Integer> {

    // from is included, to is excluded: arrivalDate >= from AND arrivalDate < to
    List<Animal> findAnimalsByArrivalDateGreaterThanEqualAndArrivalDateLessThan(LocalDateTime from, LocalDateTime to);

    List<Animal> findAnimalsByOrigin(String origin);

    Optional<Animal> findAnimalByRegistrationNumber(String registrationNumber);

    List<Animal> findAnimalsBySyncedFalse();

}
