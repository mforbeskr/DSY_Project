package dk.via.animalregistrationservice.controller;

import dk.via.animalregistrationservice.model.Animal;
import dk.via.animalregistrationservice.service.AnimalRegService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests the HTTP layer: URLs, status codes and JSON. Only the controller is started;
 * the service is a Mockito mock, so no database is needed.
 */
@WebMvcTest(AnimalController.class)
class AnimalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AnimalRegService service;

    private static final Animal REG001 =
            new Animal("REG001", LocalDateTime.of(2026, 9, 18, 7, 30), "FARM001", 120.5);

    @Test
    void register_returns201WithLocationAndTheStoredAnimal() throws Exception {
        when(service.register(any(Animal.class))).thenReturn(REG001);

        mockMvc.perform(post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"registrationNumber": "REG001", "arrivalDate": "2026-09-18T07:30:00",
                                 "origin": "FARM001", "weight": 120.5}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/animals/0"))
                .andExpect(jsonPath("$.registrationNumber").value("REG001"))
                .andExpect(jsonPath("$.arrivalDate").value("2026-09-18T07:30:00"))
                .andExpect(jsonPath("$.origin").value("FARM001"))
                .andExpect(jsonPath("$.weight").value(120.5));
    }

    @Test
    void register_returns400WhenTheServiceRejectsTheAnimal() throws Exception {
        when(service.register(any(Animal.class)))
                .thenThrow(new IllegalArgumentException("Weight must be greater than 0"));

        mockMvc.perform(post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"registrationNumber": "REG001", "arrivalDate": "2026-09-18T07:30:00",
                                 "origin": "FARM001", "weight": 0}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Weight must be greater than 0"));
    }

    @Test
    void getById_returns200WithTheAnimal() throws Exception {
        when(service.findById(1)).thenReturn(Optional.of(REG001));

        mockMvc.perform(get("/animals/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registrationNumber").value("REG001"));
    }

    @Test
    void getById_returns404ForUnknownAnimal() throws Exception {
        when(service.findById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/animals/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getByRegistrationNumber_returns200WithTheAnimal() throws Exception {
        when(service.findByRegistrationNumber("REG001")).thenReturn(Optional.of(REG001));

        mockMvc.perform(get("/animals").param("registrationNumber", "REG001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.origin").value("FARM001"));
    }

    @Test
    void getByRegistrationNumber_returns404ForUnknownNumber() throws Exception {
        when(service.findByRegistrationNumber("NOPE")).thenReturn(Optional.empty());

        mockMvc.perform(get("/animals").param("registrationNumber", "NOPE"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getByArrivalDate_passesTheDateOnAndReturnsTheList() throws Exception {
        when(service.findByArrivalDate(LocalDate.of(2026, 9, 18))).thenReturn(List.of(REG001));

        mockMvc.perform(get("/animals").param("date", "2026-09-18"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].registrationNumber").value("REG001"));

        verify(service).findByArrivalDate(LocalDate.of(2026, 9, 18));
    }

    @Test
    void getByArrivalDate_returnsEmptyListWhenNoAnimalsArrived() throws Exception {
        when(service.findByArrivalDate(LocalDate.of(2026, 9, 17))).thenReturn(List.of());

        mockMvc.perform(get("/animals").param("date", "2026-09-17"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getByArrivalDate_returns400ForInvalidDate() throws Exception {
        mockMvc.perform(get("/animals").param("date", "18-09-2026"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getByOrigin_returns200WithTheList() throws Exception {
        when(service.findByOrigin("FARM001")).thenReturn(List.of(REG001));

        mockMvc.perform(get("/animals").param("origin", "FARM001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].origin").value("FARM001"));
    }

    @Test
    void getAnimals_returns400WithoutAnySearchParameter() throws Exception {
        mockMvc.perform(get("/animals"))
                .andExpect(status().isBadRequest());
    }
}
