package org.example.adventurexpbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String Name;
    private int personsMin;
    private int personsMax;



//---------------Getters and Setters-----------------------


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
}
