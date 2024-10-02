package org.example.adventurexpbackend.config.initData;

import org.example.adventurexpbackend.model.Activity;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class InitDataActivity {
    // Activity is for the activities: Paintball, Climbing, Go-kart

    // constructor parameters: String name, String description, int pricePrPerson, int timeMaxLimit, int ageMin, int ageMax, int personsMin, int personsMax, LocalTime openingTime, LocalTime closingTime, int timeSlotInterval, List<Equipment> equipmentList, Set<Equipment> equipmentRequiredPerPerson) {

    protected List<Activity> getActivities() {
        return new ArrayList<>(List.of(
                new Activity("Paintball", "Paintball is a fun activity for everyone", 100, 120, 10, 100, 2, 20, 10, 18, 2, null, null),
        ));
    }

    public void saveData() {
        // activityRepository.saveAll(getActivities());
    }
}
