package dk.via.centralingestionservice.messaging;

import dk.via.centralingestionservice.model.Animal;
import dk.via.centralingestionservice.repository.AnimalRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AnimalListenerTest {

    private final AnimalRepository repository = mock(AnimalRepository.class);
    private final AnimalListener listener = new AnimalListener(repository);
    private final AnimalRegisteredEvent event =
            new AnimalRegisteredEvent("REG003", LocalDateTime.of(2026, 9, 19, 7, 30), "FARM001", 110);

    @Test
    void onAnimalRegistered_savesANewAnimal() {
        when(repository.existsByRegistrationNumberIgnoreCase("REG003")).thenReturn(false);

        listener.onAnimalRegistered(event);

        verify(repository).save(any(Animal.class));
    }

    @Test
    void onAnimalRegistered_ignoresADuplicate() {
        when(repository.existsByRegistrationNumberIgnoreCase("REG003")).thenReturn(true);

        listener.onAnimalRegistered(event);

        verify(repository, never()).save(any());
    }
}
