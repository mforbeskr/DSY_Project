package dk.via.animalregistrationservice.messaging;

import dk.via.animalregistrationservice.model.Animal;
import dk.via.animalregistrationservice.repository.AnimalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.net.ConnectException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The repository and RabbitTemplate are Mockito mocks, so neither the database nor RabbitMQ is needed.
 */
class AnimalPublisherTest {

    private AnimalRepository repository;
    private RabbitTemplate rabbitTemplate;
    private AnimalPublisher publisher;

    @BeforeEach
    void setUp() {
        repository = mock(AnimalRepository.class);
        rabbitTemplate = mock(RabbitTemplate.class);
        publisher = new AnimalPublisher(repository, rabbitTemplate);
    }

    private static Animal animal(String registrationNumber) {
        return new Animal(registrationNumber, LocalDateTime.of(2026, 9, 19, 7, 30), "FARM001", 110);
    }

    @Test
    void publishUnsyncedAnimals_sendsTheAnimalAndMarksItSynced() {
        Animal animal = animal("REG003");
        when(repository.findAnimalsBySyncedFalse()).thenReturn(List.of(animal));

        publisher.publishUnsyncedAnimals();

        verify(rabbitTemplate).convertAndSend(RabbitConfig.ANIMAL_QUEUE,
                new AnimalRegisteredEvent("REG003", LocalDateTime.of(2026, 9, 19, 7, 30), "FARM001", 110));
        assertTrue(animal.isSynced());
        verify(repository).save(animal);
    }

    @Test
    void publishUnsyncedAnimals_keepsTheAnimalUnsyncedWhenRabbitMqIsDown() {
        Animal animal = animal("REG003");
        when(repository.findAnimalsBySyncedFalse()).thenReturn(List.of(animal));
        doThrow(new AmqpConnectException(new ConnectException()))
                .when(rabbitTemplate).convertAndSend(eq(RabbitConfig.ANIMAL_QUEUE), any(Object.class));

        publisher.publishUnsyncedAnimals();

        assertFalse(animal.isSynced());
        verify(repository, never()).save(any());
    }

    @Test
    void publishUnsyncedAnimals_doesNothingWhenEverythingIsSynced() {
        when(repository.findAnimalsBySyncedFalse()).thenReturn(List.of());

        publisher.publishUnsyncedAnimals();

        verify(rabbitTemplate, never()).convertAndSend(any(String.class), any(Object.class));
    }
}
