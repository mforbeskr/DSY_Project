DROP TABLE IF EXISTS animal;

CREATE TABLE animal
(
    animal_id           INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    arrival_date        TIMESTAMP NOT NULL,
    weight              DOUBLE PRECISION NOT NULL,
    origin              VARCHAR(50) NOT NULL,
    registration_number VARCHAR(50) UNIQUE NOT NULL,
    synced              BOOLEAN NOT NULL DEFAULT FALSE
);
