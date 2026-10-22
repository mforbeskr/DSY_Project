package dk.via.centralingestionservice.messaging;

import dk.via.centralingestionservice.repository.AnimalRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AnimalListener {

    private final AnimalRepository repository;

    public AnimalListener(AnimalRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = RabbitConfig.ANIMAL_QUEUE)
    public void onAnimalRegistered(AnimalRegisteredEvent event) {
        repository.insert(event);
    }
}
