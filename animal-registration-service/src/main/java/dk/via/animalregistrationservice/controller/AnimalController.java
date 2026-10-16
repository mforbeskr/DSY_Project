package dk.via.animalregistrationservice.controller;

import dk.via.animalregistrationservice.model.Animal;
import dk.via.animalregistrationservice.service.AnimalRegService;
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
public class AnimalController {

    private final AnimalRegService service;

    public AnimalController(AnimalRegService service) {
        this.service = service;
    }

    // POST /animals  (JSON body)  ->  201 Created + the stored animal with its new id
    @PostMapping
    public ResponseEntity<Animal> register(@RequestBody RegisterAnimalRequest request) {
        Animal animal = new Animal(request.registrationNumber(), request.arrivalDate(), request.origin(), request.weight());
        Animal saved = service.register(animal);

        return ResponseEntity
                .created(URI.create("/animals/" + saved.getAnimalId()))
                .body(saved);
    }

    // GET /animals/1  ->  200 + the animal, or 404 if it doesn't exist
    @GetMapping("/{animalId}")
    public ResponseEntity<Animal> getById(@PathVariable int animalId) {
        return ResponseEntity.of(service.findById(animalId));
    }

    // GET /animals?registrationNumber=REG001  ->  200 + the animal, or 404
    @GetMapping(params = "registrationNumber")
    public ResponseEntity<Animal> getByRegistrationNumber(@RequestParam String registrationNumber) {
        return ResponseEntity.of(service.findByRegistrationNumber(registrationNumber));
    }

    // GET /animals?date=2026-09-18  ->  200 + list (empty if none arrived that day)
    @GetMapping(params = "date")
    public List<Animal> getByArrivalDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.findByArrivalDate(date);
    }

    // GET /animals?origin=FARM001  ->  200 + list (empty if none from that farm)
    @GetMapping(params = "origin")
    public List<Animal> getByOrigin(@RequestParam String origin) {
        return service.findByOrigin(origin);
    }

    // Rule broken in the service (e.g. weight <= 0, duplicate registration number)  ->  400 Bad Request
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgument(IllegalArgumentException e) {
        return e.getMessage();
    }
}
