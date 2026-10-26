package dk.via.centralingestionservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// e.g. Ham Package, Half Animal
@Entity
@Table(name = "producttype")
public class ProductType {
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id
    private int productTypeId;
    @Column(nullable = false, length = 100)
    private String name;

    public ProductType() {
    }

    public ProductType(String name) {
        this.name = name;
    }

    public int getProductTypeId() {
        return productTypeId;
    }

    public String getName() {
        return name;
    }
}
