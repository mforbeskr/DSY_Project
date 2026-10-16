-- DSY Slaughterhouse - test data (PostgreSQL)
-- Run after 01_create_tables.sql.
--
-- Example traceability with this data:
--   product 1 -> tray 1          -> animals REG001, REG002
--   product 2 -> trays 1 and 2   -> animals REG001, REG002
--   animal 1  -> products 1, 2

INSERT INTO animal (animal_id, arrival_date, weight, registration_number)
VALUES (1, '2026-09-18', 120.5, 'REG001'),
       (2, '2026-09-18', 132, 'REG002');

INSERT INTO parttype (part_type_id, name)
VALUES (1, 'Ham'),
       (2, 'Rib');

INSERT INTO tray (tray_id, max_weight_capacity, current_weight, part_type_id)
VALUES (1, 50, 40, 1),
       (2, 50, 35, 2);

INSERT INTO part (part_id, weight, animal_id, tray_id, part_type_id)
VALUES (1, 20, 1, 1, 1),
       (2, 20, 2, 1, 1),
       (3, 15, 1, 2, 2),
       (4, 20, 2, 2, 2);

INSERT INTO producttype (product_type_id, name)
VALUES (1, 'Ham Package'),
       (2, 'Half Animal');

INSERT INTO product (product_id, packing_date, product_type_id)
VALUES (1, '2026-09-18', 1),
       (2, '2026-09-18', 2);

INSERT INTO producttray (product_id, tray_id)
VALUES (1, 1),
       (2, 1),
       (2, 2);
