package model;

public class Part
{
    private final int partId;
    private final int animalId;
    private final Integer trayId;
    private final double weight;
    private final String type;

    public Part(int partId, int animalId, Integer trayId, double weight, String type)
    {
        this.partId = partId;
        this.animalId = animalId;
        this.trayId = trayId;
        this.weight = weight;
        this.type = type;
    }

    public int getPartId()
    {
        return partId;
    }

    public int getAnimalId()
    {
        return animalId;
    }

    public Integer getTrayId()
    {
        return trayId;
    }

    public double getWeight()
    {
        return weight;
    }

    public String getType()
    {
        return type;
    }
}
