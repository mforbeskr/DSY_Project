package dk.via.centralingestionservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Tray {
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id
    private int trayId;
    @Column(nullable = false)
    private double maxWeightCapacity;
    @Column(nullable = false)
    private double currentWeight;
    @ManyToOne(optional = false) @JoinColumn(name = "part_type_id")
    private PartType partType;

    public Tray() {
    }

    public Tray(double maxWeightCapacity, PartType partType) {
        this.maxWeightCapacity = maxWeightCapacity;
        this.partType = partType;
    }

    public int getTrayId() {
        return trayId;
    }

    public double getMaxWeightCapacity() {
        return maxWeightCapacity;
    }

    public double getCurrentWeight() {
        return currentWeight;
    }

    public PartType getPartType() {
        return partType;
    }
}
