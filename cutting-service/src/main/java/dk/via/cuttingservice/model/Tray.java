package dk.via.cuttingservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Tray {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int trayId;
    @Column(nullable = false, unique = true, length = 50)
    private String trayCode;
    @ManyToOne(optional = false) @JoinColumn(name = "part_type_id")
    private PartType partType;
    @Column(nullable = false)
    private double maxWeightCapacity;
    @Column(nullable = false)
    private double currentWeight;

    public Tray() {
    }

    public Tray(String trayCode, PartType partType, double maxWeightCapacity) {
        this.trayCode = trayCode;
        this.maxWeightCapacity = maxWeightCapacity;
        this.partType = partType;
    }

    public int getTrayId() {
        return trayId;
    }

    public String getTrayCode() {
        return trayCode;
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
