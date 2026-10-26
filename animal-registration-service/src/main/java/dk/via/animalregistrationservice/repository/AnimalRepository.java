package dk.via.animalregistrationservice.repository;

import dk.via.animalregistrationservice.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AnimalRepository extends JpaRepository<Animal, Integer> {

    // from is included, to is excluded: arrivalDate >= from AND arrivalDate < to
    List<Animal> findAnimalsByArrivalDateGreaterThanEqualAndArrivalDateLessThan(LocalDateTime from, LocalDateTime to);

    // IgnoreCase: "hedehusene farm" also finds "Hedehusene Farm" (SQL: UPPER(origin) = UPPER(?))
    List<Animal> findAnimalsByOriginIgnoreCase(String origin);

    Optional<Animal> findAnimalByRegistrationNumberIgnoreCase(String registrationNumber);

    List<Animal> findAnimalsBySyncedFalse();

}
