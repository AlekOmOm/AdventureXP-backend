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

    @ManyToOne
    @JoinColumn(name = "activity_id")
    @JsonBackReference
    private Activity activity;

    @ManyToOne
    @JoinColumn(name = "equipment_type_id")
    private EquipmentType equipmentType;

    // Constructors, getters, and setters
    public Equipment() {
    }

    public Equipment(String name, boolean functional, boolean underService, Activity activity, EquipmentType equipmentType) {
        this.name = name;
        this.functional = functional;
        this.underService = underService;
        this.activity = activity;
        this.equipmentType = equipmentType;
    }

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

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public EquipmentType getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(EquipmentType equipmentType) {
        this.equipmentType = equipmentType;
    }
}