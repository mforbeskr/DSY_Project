package dk.via.centralingestionservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// e.g. Ham, Rib. A tray only holds parts of one type.
@Entity
@Table(name = "parttype")
public class PartType {
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id
    private int partTypeId;
    @Column(nullable = false, length = 100)
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
