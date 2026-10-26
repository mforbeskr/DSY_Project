package dk.via.centralingestionservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

// A piece cut from one animal and placed in one tray
@Entity
public class Part {
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id
    private int partId;
    @Column(nullable = false)
    private double weight;
    @ManyToOne(optional = false) @JoinColumn(name = "animal_id")
    private Animal animal;
    @ManyToOne(optional = false) @JoinColumn(name = "tray_id")
    private Tray tray;
    @ManyToOne(optional = false) @JoinColumn(name = "part_type_id")
    private PartType partType;

    public Part() {
    }

    public Part(double weight, Animal animal, Tray tray, PartType partType) {
        this.weight = weight;
        this.animal = animal;
        this.tray = tray;
        this.partType = partType;
    }

    public int getPartId() {
        return partId;
    }

    public double getWeight() {
        return weight;
    }

    public Animal getAnimal() {
        return animal;
    }

    public Tray getTray() {
        return tray;
    }

    public PartType getPartType() {
        return partType;
    }
}
