package dk.via.centralingestionservice.messaging;

import dk.via.centralingestionservice.repository.AnimalRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AnimalListenerTest {

    @Test
    void onAnimalRegistered_insertsTheAnimal() {
        AnimalRepository repository = mock(AnimalRepository.class);
        AnimalListener listener = new AnimalListener(repository);
        AnimalRegisteredEvent event =
                new AnimalRegisteredEvent("REG003", LocalDateTime.of(2026, 9, 19, 7, 30), "FARM001", 110);

        listener.onAnimalRegistered(event);

        verify(repository).insert(event);
    }
}
