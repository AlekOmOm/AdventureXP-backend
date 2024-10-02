package org.example.adventurexpbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private boolean functional;
    private boolean underService;

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


}
