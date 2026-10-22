package dk.via.animalregistrationservice.messaging;

import dk.via.animalregistrationservice.model.Animal;
import dk.via.animalregistrationservice.repository.AnimalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AnimalPublisher {

    private static final Logger log = LoggerFactory.getLogger(AnimalPublisher.class);

    private final AnimalRepository repository;
    private final RabbitTemplate rabbitTemplate;

    public AnimalPublisher(AnimalRepository repository, RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
    }

    // animals stay unsynced while RabbitMQ can't be reached, and are tried again on the next run
    @Scheduled(fixedDelay = 5000)
    public void publishUnsyncedAnimals() {
        for (Animal animal : repository.findAnimalsBySyncedFalse()) {
            try {
                rabbitTemplate.convertAndSend(RabbitConfig.ANIMAL_QUEUE, new AnimalRegisteredEvent(
                        animal.getRegistrationNumber(), animal.getArrivalDate(), animal.getOrigin(), animal.getWeight()));
            } catch (AmqpException e) {
                log.warn("Could not reach RabbitMQ, trying again later");
                return;
            }
            animal.markSynced();
            repository.save(animal);
        }
    }
}
