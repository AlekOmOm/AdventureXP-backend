package org.example.adventurexpbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private int pricePrPerson;
    private int timeMaxLimit;
    private int ageMin;
    private int ageMax;
    private int personsMin;
    private int personsMax;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private int timeSlotInterval;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL)
    private List<Equipment> equipmentList;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "activity_equipment_type",
            joinColumns = @JoinColumn(name = "activity_id"),
            inverseJoinColumns = @JoinColumn(name = "equipment_type_id")
    )
    private Set<EquipmentType> equipmentTypes;

    // Constructors, getters, and setters
    public Activity() {
    }

    public Activity(String name, String description, int pricePrPerson, int timeMaxLimit, int ageMin, int ageMax, int personsMin, int personsMax, LocalTime openingTime, LocalTime closingTime, int timeSlotInterval, List<Equipment> equipmentList, Set<EquipmentType> equipmentRequiredPerPerson) {
        this.name = name;
        this.description = description;
        this.pricePrPerson = pricePrPerson;
        this.timeMaxLimit = timeMaxLimit;
        this.ageMin = ageMin;
        this.ageMax = ageMax;
        this.personsMin = personsMin;
        this.personsMax = personsMax;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.timeSlotInterval = timeSlotInterval;
        this.equipmentList = equipmentList;
        this.equipmentTypes = equipmentRequiredPerPerson;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPricePrPerson() {
        return pricePrPerson;
    }

    public void setPricePrPerson(int pricePrPerson) {
        this.pricePrPerson = pricePrPerson;
    }

//---------------Getters and Setters-----------------------
    public int getTimeMaxLimit() {
        return timeMaxLimit;
    }

    public void setTimeMaxLimit(int timeMaxLimit) {
        this.timeMaxLimit = timeMaxLimit;
    }

    public int getAgeMin() {
        return ageMin;
    }

    public void setAgeMin(int ageMin) {
        this.ageMin = ageMin;
    }

    public int getAgeMax() {
        return ageMax;
    }

    public void setAgeMax(int ageMax) {
        this.ageMax = ageMax;
    }

    public int getPersonsMin() {
        return personsMin;
    }

    public void setPersonsMin(int personsMin) {
        this.personsMin = personsMin;
    }

    public int getPersonsMax() {
        return personsMax;
    }

    public void setPersonsMax(int personsMax) {
        this.personsMax = personsMax;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public int getTimeSlotInterval() {
        return timeSlotInterval;
    }

    public void setTimeSlotInterval(int timeSlotInterval) {
        this.timeSlotInterval = timeSlotInterval;
    }

    public List<Equipment> getEquipmentList() {
        return equipmentList;
    }

    public void setEquipmentList(List<Equipment> equipmentList) {
        this.equipmentList = equipmentList;
    }

    public Set<EquipmentType> getEquipmentTypes() {
        return equipmentTypes;
    }

    public void setEquipmentTypes(Set<EquipmentType> equipmentTypes) {
        this.equipmentTypes = equipmentTypes;
    }
}