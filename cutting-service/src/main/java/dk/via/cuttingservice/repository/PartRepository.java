package dk.via.cuttingservice.repository;

import dk.via.cuttingservice.model.Part;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartRepository extends JpaRepository<Part, Integer> {
    List<Part> findPartsBySyncedFalse();

}
