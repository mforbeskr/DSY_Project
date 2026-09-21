package model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ModelTest
{
    @Test
    void animal_exposesItsValues()
    {
        Animal animal = new Animal(1, "DK-001", LocalDate.of(2026, 9, 1), 250.5);

        assertEquals(1, animal.getAnimalId());
        assertEquals("DK-001", animal.getRegistrationNumber());
        assertEquals(LocalDate.of(2026, 9, 1), animal.getArrivalDate());
        assertEquals(250.5, animal.getWeight());
    }

    @Test
    void part_exposesItsValues()
    {
        Part part = new Part(5, 1, 3, 12.5, "Ribs");

        assertEquals(5, part.getPartId());
        assertEquals(1, part.getAnimalId());
        assertEquals(3, part.getTrayId());
        assertEquals(12.5, part.getWeight());
        assertEquals("Ribs", part.getType());
    }

    @Test
    void part_hasNoTrayUntilItIsPlacedInOne()
    {
        Part part = new Part(5, 1, null, 12.5, "Ribs");

        assertNull(part.getTrayId());
    }

    @Test
    void tray_exposesItsValues()
    {
        Tray tray = new Tray(3, 50.0, 12.5, "Ribs");

        assertEquals(3, tray.getTrayId());
        assertEquals(50.0, tray.getMaxWeightCapacity());
        assertEquals(12.5, tray.getCurrentWeight());
        assertEquals("Ribs", tray.getPartType());
    }

    @Test
    void product_exposesItsValues()
    {
        Product product = new Product(9, LocalDate.of(2026, 9, 5), "Half animal", List.of(3, 4));

        assertEquals(9, product.getProductId());
        assertEquals(LocalDate.of(2026, 9, 5), product.getPackingDate());
        assertEquals("Half animal", product.getType());
        assertEquals(List.of(3, 4), product.getTrayIds());
    }

    @Test
    void product_isNotAffectedByLaterChangesToTheListItWasGiven()
    {
        List<Integer> trayIds = new ArrayList<>(List.of(3, 4));
        Product product = new Product(9, LocalDate.of(2026, 9, 5), "Half animal", trayIds);

        trayIds.add(5);

        assertEquals(List.of(3, 4), product.getTrayIds());
    }

    @Test
    void product_trayIdsCannotBeModifiedFromOutside()
    {
        Product product = new Product(9, LocalDate.of(2026, 9, 5), "Half animal", List.of(3, 4));

        assertThrows(UnsupportedOperationException.class, () -> product.getTrayIds().add(5));
    }
}
