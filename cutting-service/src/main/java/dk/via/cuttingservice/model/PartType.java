package dk.via.cuttingservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

// e.g. Ham, Rib. A tray only holds parts of one type.
@Entity
public class PartType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int partTypeId;
    @Column(nullable = false, length = 100, unique = true)
    private String name;

    public PartType() {
    }
    public PartType(String name) {
        this.name = name;
    }

    public int getPartTypeId() {
        return partTypeId;
    }

    public String getName() {
        return name;
    }
}
