package dk.via.centralingestionservice.controller;

import dk.via.centralingestionservice.repository.AnimalRepository;
import dk.via.centralingestionservice.repository.CentralAnimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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
        when(repository.findByRegistrationNumber("REG001")).thenReturn(Optional.of(new CentralAnimal(1, "REG001")));

        mockMvc.perform(get("/animals/REG001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.animalId").value(1))
                .andExpect(jsonPath("$.registrationNumber").value("REG001"));
    }

    @Test
    void getByRegistrationNumber_returns404WhenTheAnimalHasNotArrivedYet() throws Exception {
        when(repository.findByRegistrationNumber("REG999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/animals/REG999"))
                .andExpect(status().isNotFound());
    }
}
