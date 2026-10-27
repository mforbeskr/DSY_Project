package dk.via.centralingestionservice.repository;

import dk.via.centralingestionservice.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface AnimalRepository extends JpaRepository<Animal, Integer> {

    Optional<Animal> findAnimalByRegistrationNumberIgnoreCase(String registrationNumber);

    boolean existsByRegistrationNumberIgnoreCase(String registrationNumber);
}



