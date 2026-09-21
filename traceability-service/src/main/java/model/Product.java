package model;

import java.time.LocalDate;
import java.util.List;

public class Product
{
    private final int productId;
    private final LocalDate packingDate;
    private final String type;
    private final List<Integer> trayIds;

    public Product(int productId, LocalDate packingDate, String type, List<Integer> trayIds)
    {
        this.productId = productId;
        this.packingDate = packingDate;
        this.type = type;
        this.trayIds = List.copyOf(trayIds);
    }

    public int getProductId()
    {
        return productId;
    }

    public LocalDate getPackingDate()
    {
        return packingDate;
    }

    public String getType()
    {
        return type;
    }

    public List<Integer> getTrayIds()
    {
        return trayIds;
    }
}
