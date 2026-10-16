package model;

import java.time.LocalDateTime;
import java.util.List;

public class Product
{
    private final int productId;
    private final LocalDateTime packingDate;
    private final String type;
    private final List<Integer> trayIds;

    public Product(int productId, LocalDateTime packingDate, String type, List<Integer> trayIds)
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

    public LocalDateTime getPackingDate()
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
