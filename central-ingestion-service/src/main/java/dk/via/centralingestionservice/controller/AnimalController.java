package dk.via.centralingestionservice.controller;

import dk.via.centralingestionservice.repository.AnimalRepository;
import dk.via.centralingestionservice.repository.CentralAnimal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/animals")
public class AnimalController {

    private final AnimalRepository repository;

    public AnimalController(AnimalRepository repository) {
        this.repository = repository;
    }

    // GET /animals/REG001  ->  200 if the animal has reached the central database, otherwise 404
    @GetMapping("/{registrationNumber}")
    public ResponseEntity<CentralAnimal> getByRegistrationNumber(@PathVariable String registrationNumber) {
        return ResponseEntity.of(repository.findByRegistrationNumber(registrationNumber));
    }
}
