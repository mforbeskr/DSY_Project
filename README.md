# DSY Project - Slaughterhouse

PRO3/DSY kursusopgave. Lavet alene.

## Services

| Service | Hvad | Port |
|---|---|---|
| traceability-service | gRPC, finder dyr i et produkt og produkter et dyr er i (Part 2) | 9090 |
| animal-registration-service | REST, station 1, registrerer dyr i sin egen lokale DB (Part 3) | 8080 |
| central-ingestion-service | Henter dyr fra RabbitMQ og skriver dem i den centrale DB | 8081 |
| frontend | React, registrer og søg dyr, viser hvordan et dyr flyder gennem systemet (ekstra, ikke krav) | 5173 |

Station 1 gemmer lokalt, så den virker selvom nettet er nede. Dyrene sendes via RabbitMQ til den centrale DB når der er forbindelse.

## Diagrammer

Ligger i `documentation/`. Begge domænemodeller (Ass1 og Ass3) er med, så man kan se udviklingen.

## Domænemodel og tabeller

Domænemodellen findes som JPA-entities (`Animal`, `Part`, `PartType`, `Tray`, `Product`, `ProductType`) i `central-ingestion-service/src/main/java/dk/via/centralingestionservice/model`.
Tabellerne bliver genereret af Hibernate ud fra entities (`spring.jpa.hibernate.ddl-auto=update`), så der er ingen `CREATE TABLE`-scripts.

## Kør det

1. Opret databaserne, start services én gang så Hibernate laver tabellerne, og indsæt testdata. Se `database/README.md`
2. Start RabbitMQ (kræver Docker): `docker compose up -d`
3. Start de tre services: `MainRegistration` i animal-registration-service, `MainIngestion` i de to andre
4. Frontend (kræver Node): `cd frontend`, `npm install`, `npm run dev` og åbn http://localhost:5173
5. Tests: `mvn test` i roden (kræver PostgreSQL)

## API-dokumentation (Swagger UI)

Når services kører:
- Station 1 (registrering): http://localhost:8080/swagger-ui.html
- Central ingestion: http://localhost:8081/swagger-ui.html

Her kan man se alle endpoints, parametre og svar, og prøve dem med **Try it out**.

## Postman eksempler

Registrer et dyr:
```
POST http://localhost:8080/animals
Content-Type: application/json

{ "registrationNumber": "REG003", "arrivalDate": "2026-09-19T07:30:00", "origin": "FARM001", "weight": 110 }
```

Hent et dyr (på registreringsnummer):
```
GET http://localhost:8080/animals/REG001
```

Alle dyr der ankom på en dato:
```
GET http://localhost:8080/animals?date=2026-09-18
```

Alle dyr fra en gård:
```
GET http://localhost:8080/animals?origin=FARM001
```
