package dk.via.centralingestionservice.repository;

import dk.via.centralingestionservice.messaging.AnimalRegisteredEvent;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AnimalRepository {

    private final JdbcClient jdbcClient;

    public AnimalRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    // returns 1 if the animal was inserted, 0 if it was already there
    public int insert(AnimalRegisteredEvent event) {
        return jdbcClient.sql("""
                        INSERT INTO animal (registration_number, arrival_date, origin, weight)
                        VALUES (?, ?, ?, ?)
                        ON CONFLICT (registration_number) DO NOTHING
                        """)
                .params(event.registrationNumber(), event.arrivalDate(), event.origin(), event.weight())
                .update();
    }

    public Optional<CentralAnimal> findByRegistrationNumber(String registrationNumber) {
        return jdbcClient.sql("SELECT animal_id, registration_number FROM animal WHERE registration_number = ?")
                .param(registrationNumber)
                .query(CentralAnimal.class)
                .optional();
    }
}
