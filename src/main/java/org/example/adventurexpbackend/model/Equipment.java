package org.example.adventurexpbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private boolean functional;
    private boolean underService;


    // ------------------- Constructors -------------------
    public Equipment() {
    }

    public Equipment(String name, boolean functional, boolean underService) {
        this.name = name;
        this.functional = functional;
        this.underService = underService;
    }

    // ------------------- Getter & Setters -------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFunctional() {
        return functional;
    }

    public void setFunctional(boolean functional) {
        this.functional = functional;
    }

    public boolean isUnderService() {
        return underService;
    }

    public void setUnderService(boolean underService) {
        this.underService = underService;
    }


    @Override
    public String toString() {
        return "Equipment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", functional=" + functional +
                ", underService=" + underService +
                '}';
    }
}