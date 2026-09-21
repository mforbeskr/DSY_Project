package repository;

import database.DBConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Runs the real SQL against the local PostgreSQL database configured in
 * {@link database.DatabaseConfig}. When that database can't be reached the
 * tests are skipped rather than failed, so the build still works on a machine
 * without the database.
 */
class TraceabilityRepositoryImplTest
{
    private static boolean databaseAvailable;

    private final TraceabilityRepository repository = new TraceabilityRepositoryImpl();

    @BeforeAll
    static void checkDatabase()
    {
        try (Connection ignored = DBConnection.getConnection())
        {
            databaseAvailable = true;
        }
        catch (Exception e)
        {
            databaseAvailable = false;
        }
    }

    @Test
    void getRegistrationNumbersForProduct_returnsEmptyListForUnknownProduct()
    {
        assumeTrue(databaseAvailable, "PostgreSQL database not reachable");

        assertTrue(repository.getRegistrationNumbersForProduct(-1).isEmpty());
    }

    @Test
    void getProductIdsForAnimal_returnsEmptyListForUnknownAnimal()
    {
        assumeTrue(databaseAvailable, "PostgreSQL database not reachable");

        assertTrue(repository.getProductIdsForAnimal(-1).isEmpty());
    }

    @Test
    void getRegistrationNumbersForProduct_neverReturnsNull()
    {
        assumeTrue(databaseAvailable, "PostgreSQL database not reachable");

        assertNotNull(repository.getRegistrationNumbersForProduct(1));
    }

    @Test
    void getProductIdsForAnimal_neverReturnsNull()
    {
        assumeTrue(databaseAvailable, "PostgreSQL database not reachable");

        assertNotNull(repository.getProductIdsForAnimal(1));
    }
}
