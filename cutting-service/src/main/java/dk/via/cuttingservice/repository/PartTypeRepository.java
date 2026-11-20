package dk.via.cuttingservice.repository;

import dk.via.cuttingservice.model.PartType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartTypeRepository extends JpaRepository<PartType, Integer> {
    Optional<PartType> findPartTypeByNameIgnoreCase(String name);

}
