package dk.via.cuttingservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.UUID;

// A piece cut from one animal and placed in one tray
@Entity
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int partId;
    @Column(nullable = false, unique = true)
    private UUID partNumber;
    @Column(nullable = false, length = 50)
    private String animalRegistrationNumber;
    @ManyToOne(optional = false)
    @JoinColumn(name = "part_type_id")
    private PartType partType;
    @Column(nullable = false)
    private double weight;
    @ManyToOne(optional = false)
    @JoinColumn(name = "tray_id")
    private Tray tray;
    @Column(nullable = false)
    private boolean synced;

    public Part() {
    }

    public Part(String animalRegistrationNumber, PartType partType, double weight, Tray tray) {
        this.partNumber = UUID.randomUUID();
        this.animalRegistrationNumber = animalRegistrationNumber;
        this.partType = partType;
        this.weight = weight;
        this.tray = tray;
    }

    public double getWeight() {
        return weight;
    }

    public Tray getTray() {
        return tray;
    }

    public boolean isSynced() {
        return synced;
    }

    public int getPartId() {
        return partId;
    }

    public UUID getPartNumber() {
        return partNumber;
    }

    public String getAnimalRegistrationNumber() {
        return animalRegistrationNumber;
    }

    public PartType getPartType() {
        return partType;
    }

    public void markSynced() {
        synced = true;
    }
}

