package dk.via.centralingestionservice.messaging;

import dk.via.centralingestionservice.model.Animal;
import dk.via.centralingestionservice.repository.AnimalRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AnimalListener {

    private final AnimalRepository repository;

    public AnimalListener(AnimalRepository repository) {
        this.repository = repository;
    }

    // dupletter ignoreres fordi man checker på regNo ligegyldigt om upper/lowercase
    @RabbitListener(queues = RabbitConfig.ANIMAL_QUEUE)
    public void onAnimalRegistered(AnimalRegisteredEvent event) {
        if (!repository.existsByRegistrationNumberIgnoreCase(event.registrationNumber())) {
            Animal newAnimal = new Animal(
                    event.registrationNumber(),
                    event.arrivalDate(),
                    event.origin(),
                    event.weight());

            repository.save(newAnimal);
        }
    }
}
