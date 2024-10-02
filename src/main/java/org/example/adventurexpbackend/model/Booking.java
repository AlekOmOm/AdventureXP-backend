package org.example.adventurexpbackend.model;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.List;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime startTime;
    private LocalTime endTime;
    private int personsAmount;

    @ManyToOne
    @JoinColumn(name = "activity", referencedColumnName = "id")
    Activity activity;

    //Default constructor
    public Booking() {}

    public Booking(LocalTime startTime, LocalTime endTime, int personsAmount, Activity activity) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.personsAmount = personsAmount;
        this.activity = activity;
    }


    //----------------------getters and setters------------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public int getPersonsAmount() {
        return personsAmount;
    }

    public void setPersonsAmount(int personsAmount) {
        this.personsAmount = personsAmount;
    }
}
