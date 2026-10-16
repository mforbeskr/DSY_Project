DROP TABLE IF EXISTS producttray;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS producttype;
DROP TABLE IF EXISTS part;
DROP TABLE IF EXISTS tray;
DROP TABLE IF EXISTS parttype;
DROP TABLE IF EXISTS animal;

CREATE TABLE animal
(
    animal_id           INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    arrival_date        TIMESTAMP NOT NULL,
    weight              DOUBLE PRECISION NOT NULL,
    origin              VARCHAR(50) NOT NULL,
    registration_number VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE parttype
(
    part_type_id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name         VARCHAR(100) NOT NULL
);

CREATE TABLE tray
(
    tray_id             INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    max_weight_capacity DOUBLE PRECISION NOT NULL,
    current_weight      DOUBLE PRECISION NOT NULL,
    part_type_id        INTEGER          NOT NULL REFERENCES parttype (part_type_id)
);

CREATE TABLE part
(
    part_id      INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    weight       DOUBLE PRECISION NOT NULL,
    animal_id    INTEGER          NOT NULL REFERENCES animal (animal_id),
    tray_id      INTEGER          NOT NULL REFERENCES tray (tray_id),
    part_type_id INTEGER          NOT NULL REFERENCES parttype (part_type_id)
);

CREATE TABLE producttype
(
    product_type_id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name            VARCHAR(100) NOT NULL
);

CREATE TABLE product
(
    product_id      INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    packing_date    TIMESTAMP,
    product_type_id INTEGER NOT NULL REFERENCES producttype (product_type_id)
);

-- A product can contain parts from several trays, and a tray can feed several products.
CREATE TABLE producttray
(
    product_id INTEGER NOT NULL REFERENCES product (product_id),
    tray_id    INTEGER NOT NULL REFERENCES tray (tray_id),
    PRIMARY KEY (product_id, tray_id)
);
