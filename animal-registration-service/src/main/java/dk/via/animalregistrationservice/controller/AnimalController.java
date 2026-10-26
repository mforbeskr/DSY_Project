package dk.via.animalregistrationservice.controller;

import dk.via.animalregistrationservice.model.Animal;
import dk.via.animalregistrationservice.service.AnimalRegService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/animals")
@Tag(name = "Animals", description = "Station 1: register animals and look them up")
public class AnimalController {

    private final AnimalRegService service;

    public AnimalController(AnimalRegService service) {
        this.service = service;
    }

    // POST /animals  (JSON body)  ->  201 Created + the stored animal, Location: /animals/{registrationNumber}
    @Operation(summary = "Register an animal",
            description = "Stores the animal locally at station 1. It is sent to the central database via RabbitMQ afterwards.")
    @ApiResponse(responseCode = "201", description = "Animal registered")
    @ApiResponse(responseCode = "400", description = "Weight is not above 0, or the registration number already exists")
    @PostMapping
    public ResponseEntity<Animal> register(@RequestBody RegisterAnimalRequest request) {
        Animal animal = new Animal(request.registrationNumber(), request.arrivalDate(), request.origin(), request.weight());
        Animal saved = service.register(animal);

        return ResponseEntity.created(URI.create("/animals/" + saved.getRegistrationNumber())).body(saved);
    }

    // GET /animals/REG001  ->  200 + the animal, or 404 if it doesn't exist
    // The registration number (ear tag) is the animal's identity. The id is only a database key.
    @Operation(summary = "Get an animal by its registration number")
    @ApiResponse(responseCode = "200", description = "Animal found")
    @ApiResponse(responseCode = "404", description = "No animal with that registration number", content = @Content)
    @GetMapping("/{registrationNumber}")
    public ResponseEntity<Animal> getByRegistrationNumber(
            @Parameter(description = "The animal's ear tag", example = "REG001") @PathVariable String registrationNumber) {
        return ResponseEntity.of(service.findByRegistrationNumber(registrationNumber));
    }

    // GET /animals?date=2026-09-18  ->  200 + list (empty if none arrived that day)
    // required = false is only for Swagger, so it doesn't demand both filters. params = "date" still requires date.
    @Operation(summary = "Find animals by arrival date or origin",
            description = "Use one of the two filters. Returns an empty list if nothing matches.")
    @GetMapping(params = "date")
    public List<Animal> getByArrivalDate(
            @Parameter(description = "Arrival date (yyyy-MM-dd)", example = "2026-09-18") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.findByArrivalDate(date);
    }

    // GET /animals?origin=FARM001  ->  200 + list (empty if none from that farm)
    @GetMapping(params = "origin")
    public List<Animal> getByOrigin(
            @Parameter(description = "The farm the animal came from", example = "FARM001") @RequestParam(required = false) String origin) {
        return service.findByOrigin(origin);
    }

    // Rule broken in the service (e.g. weight <= 0, duplicate registration number)  ->  400 Bad Request
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgument(IllegalArgumentException e) {
        return e.getMessage();
    }
}
