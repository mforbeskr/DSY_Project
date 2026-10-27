package dk.via.centralingestionservice.controller;

import dk.via.centralingestionservice.model.Animal;
import dk.via.centralingestionservice.repository.AnimalRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnimalController.class)
class AnimalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AnimalRepository repository;

    @Test
    void getByRegistrationNumber_returns200WhenTheAnimalIsInTheCentralDatabase() throws Exception {
        Animal animal = new Animal("REG001", LocalDateTime.of(2026, 9, 18, 7, 30), "FARM001", 120.5);
        when(repository.findAnimalByRegistrationNumberIgnoreCase("REG001")).thenReturn(Optional.of(animal));

        mockMvc.perform(get("/animals/REG001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.animalId").exists())
                .andExpect(jsonPath("$.registrationNumber").value("REG001"));
    }

    @Test
    void getByRegistrationNumber_returns404WhenTheAnimalHasNotArrivedYet() throws Exception {
        when(repository.findAnimalByRegistrationNumberIgnoreCase("REG999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/animals/REG999"))
                .andExpect(status().isNotFound());
    }
}
