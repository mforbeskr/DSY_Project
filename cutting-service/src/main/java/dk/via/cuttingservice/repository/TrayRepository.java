package dk.via.cuttingservice.repository;

import dk.via.cuttingservice.model.Tray;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrayRepository extends JpaRepository<Tray, Integer> {
    Optional<Tray> findTrayByTrayCodeIgnoreCase(String trayCode);

}
