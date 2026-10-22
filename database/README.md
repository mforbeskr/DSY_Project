# Database (PostgreSQL)

Der er to databaser:

| Database | Scripts | Bruges af |
|---|---|---|
| `dsy_slaughterhouse` (central) | `01_create_tables.sql`, `02_insert_data.sql` | traceability-service, central-ingestion-service |
| `station1_registration` (lokal for station 1) | `station1/01_create_tables.sql`, `station1/02_insert_data.sql` | animal-registration-service |

Brugernavn og kode til din egen PostgreSQL skal stå i `traceability-service/src/main/java/database/DatabaseConfig.java`
og i `application.properties` i animal-registration-service og central-ingestion-service.

## Opsætning i IntelliJ

1. **Opret forbindelse:** Åbn *View → Tool Windows → Database*, og vælg **+ → Data Source → PostgreSQL**.
   Udfyld Host `localhost`, Port `5432`, din egen PostgreSQL-bruger og kode, og Database `postgres`.
   Klik **Test Connection** og derefter **OK**.
2. **Opret databaserne:** Højreklik på forbindelsen, vælg **New → Query Console**, og kør:
   ```sql
   CREATE DATABASE dsy_slaughterhouse;
   CREATE DATABASE station1_registration;
   ```
3. **Skift til den centrale database:** Højreklik på forbindelsen, vælg **Properties**, og ret Database til `dsy_slaughterhouse`.
4. **Kør scripts:** Højreklik på `01_create_tables.sql`, vælg **Run**, og vælg forbindelsen som target.
   Gør derefter det samme med `02_insert_data.sql`.
5. **Gentag for station 1:** Ret Database til `station1_registration`, og kør `station1/01_create_tables.sql` og `station1/02_insert_data.sql`.
6. **Tjek:** Opdatér forbindelsen. `dsy_slaughterhouse` skal have 7 tabeller, og `station1_registration` skal have 1.

> Springer du trin 3 eller 5 over, bliver tabellerne oprettet i den forkerte database, og så finder servicen dem ikke.

## RabbitMQ

Station 1 sender registrerede dyr til den centrale database via RabbitMQ. Start den med Docker fra projektets rod:

```
docker compose up -d
```

Administrationssiden ligger på http://localhost:15672 (bruger `guest`, kode `guest`).

## Testdata

| Produkt | Bakker | Dyr |
|---|---|---|
| 1 (Ham Package) | 1 | REG001, REG002 |
| 2 (Half Animal) | 1, 2 | REG001, REG002 |

Altså: dyr 1 (REG001) kan spores til produkt 1 og 2.
REG001 og REG002 ligger også i station 1's database, markeret som `synced`.
