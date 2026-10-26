package dk.via.centralingestionservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Product {
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id
    private int productId;
    private LocalDateTime packingDate;
    @ManyToOne(optional = false) @JoinColumn(name = "product_type_id")
    private ProductType productType;

    // A product can contain parts from several trays, and a tray can feed several products
    @ManyToMany
    @JoinTable(name = "producttray",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "tray_id"))
    private Set<Tray> trays = new HashSet<>();

    public Product() {
    }

    public Product(LocalDateTime packingDate, ProductType productType) {
        this.packingDate = packingDate;
        this.productType = productType;
    }

    public int getProductId() {
        return productId;
    }

    public LocalDateTime getPackingDate() {
        return packingDate;
    }

    public ProductType getProductType() {
        return productType;
    }

    public Set<Tray> getTrays() {
        return trays;
    }

    public void addTray(Tray tray) {
        trays.add(tray);
    }
}
