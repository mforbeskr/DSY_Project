# Database (PostgreSQL)

| Fil | Indhold |
|---|---|
| `01_create_tables.sql` | Opretter tabellerne med primær- og fremmednøgler |
| `02_insert_data.sql` | Indsætter testdata |

Servicen forbinder til `jdbc:postgresql://localhost:5432/dsy_slaughterhouse`.
Brugernavn og kode til din egen PostgreSQL skal stå i `traceability-service/src/main/java/database/DatabaseConfig.java`.

## Opsætning i IntelliJ

1. **Opret forbindelse:** Åbn *View → Tool Windows → Database*, og vælg **+ → Data Source → PostgreSQL**.
   Udfyld Host `localhost`, Port `5432`, din egen PostgreSQL-bruger og kode, og Database `postgres`.
   Klik **Test Connection** og derefter **OK**.
2. **Opret databasen:** Højreklik på forbindelsen, vælg **New → Query Console**, og kør:
   ```sql
   CREATE DATABASE dsy_slaughterhouse;
   ```
3. **Skift til den nye database:** Højreklik på forbindelsen, vælg **Properties**, og ret Database til `dsy_slaughterhouse`.
4. **Kør scripts:** Højreklik på `01_create_tables.sql`, vælg **Run**, og vælg forbindelsen som target.
   Gør derefter det samme med `02_insert_data.sql`.
5. **Tjek:** Opdatér forbindelsen. Under `dsy_slaughterhouse → public → tables` skal der nu være 7 tabeller.

> Springer du trin 3 over, bliver tabellerne oprettet i `postgres`-databasen, og så finder servicen dem ikke.

## Testdata

| Produkt | Bakker | Dyr |
|---|---|---|
| 1 (Ham Package) | 1 | REG001, REG002 |
| 2 (Half Animal) | 1, 2 | REG001, REG002 |

Altså: dyr 1 (REG001) kan spores til produkt 1 og 2.
