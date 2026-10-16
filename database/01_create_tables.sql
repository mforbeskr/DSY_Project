DROP TABLE IF EXISTS producttray;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS producttype;
DROP TABLE IF EXISTS part;
DROP TABLE IF EXISTS tray;
DROP TABLE IF EXISTS parttype;
DROP TABLE IF EXISTS animal;

CREATE TABLE animal
(
    animal_id           INTEGER PRIMARY KEY,
    arrival_date        DATE,
    weight              DOUBLE PRECISION,
    origin              VARCHAR(50),
    registration_number VARCHAR(50)
);

CREATE TABLE parttype
(
    part_type_id INTEGER PRIMARY KEY,
    name         VARCHAR(100) NOT NULL
);

CREATE TABLE tray
(
    tray_id             INTEGER PRIMARY KEY,
    max_weight_capacity DOUBLE PRECISION NOT NULL,
    current_weight      DOUBLE PRECISION NOT NULL,
    part_type_id        INTEGER          NOT NULL REFERENCES parttype (part_type_id)
);

CREATE TABLE part
(
    part_id      INTEGER PRIMARY KEY,
    weight       DOUBLE PRECISION NOT NULL,
    animal_id    INTEGER          NOT NULL REFERENCES animal (animal_id),
    tray_id      INTEGER          NOT NULL REFERENCES tray (tray_id),
    part_type_id INTEGER          NOT NULL REFERENCES parttype (part_type_id)
);

CREATE TABLE producttype
(
    product_type_id INTEGER PRIMARY KEY,
    name            VARCHAR(100) NOT NULL
);

CREATE TABLE product
(
    product_id      INTEGER PRIMARY KEY,
    packing_date    DATE,
    product_type_id INTEGER NOT NULL REFERENCES producttype (product_type_id)
);

-- A product can contain parts from several trays, and a tray can feed several products.
CREATE TABLE producttray
(
    product_id INTEGER NOT NULL REFERENCES product (product_id),
    tray_id    INTEGER NOT NULL REFERENCES tray (tray_id),
    PRIMARY KEY (product_id, tray_id)
);
