package model;

public class Tray
{
    private final int trayId;
    private final double maxWeightCapacity;
    private final double currentWeight;
    private final String partType;

    public Tray(int trayId, double maxWeightCapacity, double currentWeight, String partType)
    {
        this.trayId = trayId;
        this.maxWeightCapacity = maxWeightCapacity;
        this.currentWeight = currentWeight;
        this.partType = partType;
    }

    public int getTrayId()
    {
        return trayId;
    }

    public double getMaxWeightCapacity()
    {
        return maxWeightCapacity;
    }

    public double getCurrentWeight()
    {
        return currentWeight;
    }

    public String getPartType()
    {
        return partType;
    }
}
