package dk.via.centralingestionservice.controller;

import dk.via.centralingestionservice.repository.AnimalRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/animals")
@Tag(name = "Central animals", description = "Check whether an animal from a station has reached the central database")
public class AnimalController {

    private final AnimalRepository repository;

    public AnimalController(AnimalRepository repository) {
        this.repository = repository;
    }

    // GET /animals/REG001  ->  200 if the animal has reached the central database, otherwise 404
    @Operation(summary = "Get an animal from the central database by its registration number")
    @ApiResponse(responseCode = "200", description = "The animal has reached the central database")
    @ApiResponse(responseCode = "404", description = "Not there (yet)", content = @Content)
    @GetMapping("/{registrationNumber}")
    public ResponseEntity<CentralAnimal> getByRegistrationNumber(
            @Parameter(description = "The animal's ear tag", example = "REG001")
            @PathVariable String registrationNumber) {
        Optional<CentralAnimal> found = repository.findAnimalByRegistrationNumberIgnoreCase(registrationNumber)
                .map(animal -> new CentralAnimal(animal.getAnimalId(), animal.getRegistrationNumber()));
        return ResponseEntity.of(found);
    }
}
