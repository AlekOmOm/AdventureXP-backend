package org.example.adventurexpbackend.config.initData;

import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.repository.ActivityRepository;
import org.example.adventurexpbackend.repository.BookingRepository;
import org.example.adventurexpbackend.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InitData implements CommandLineRunner {

    private final InitDataActivity initDataActivity;
    private final InitDataEquipment initDataEquipment;
    private final InitDataBooking initDataBooking;

    private final ActivityRepository activityRepository;
    private final EquipmentRepository equipmentRepository;
    private final BookingRepository bookingRepository;
    private final SequenceResetter sequenceResetter;

    @Autowired
    public InitData(InitDataActivity initDataActivity, InitDataEquipment initDataEquipment, InitDataBooking initDataBooking, ActivityRepository activityRepository, EquipmentRepository equipmentRepository, BookingRepository bookingRepository, SequenceResetter sequenceResetter) {
        this.initDataActivity = initDataActivity;
        this.initDataEquipment = initDataEquipment;
        this.initDataBooking = initDataBooking;
        this.activityRepository = activityRepository;
        this.equipmentRepository = equipmentRepository;
        this.bookingRepository = bookingRepository;
        this.sequenceResetter = sequenceResetter;
    }

    @Override
    public void run(String... args) throws Exception {

        initData();
    }

    private void initData() {

        clearDB();

        sequenceResetter.resetAutoIncrement("activity");
        sequenceResetter.resetAutoIncrement("equipment");
        sequenceResetter.resetAutoIncrement("booking");
        sequenceResetter.resetAutoIncrement("equipment_type");

        System.out.println("Initializing data...");
        if (isDBEmpty()) {
            List<Activity> activities = initDataActivity.saveData();

            initDataBooking.setActivities(activities);
            initDataBooking.saveData();
            System.out.println("Data initialized");
        }
    }

    private void clearDB() {
        bookingRepository.deleteAll();
        activityRepository.deleteAll();
        equipmentRepository.deleteAll();
        System.out.println("Database cleared");
    }

    private boolean isDBEmpty() {
        return activityRepository.findAll().isEmpty() && equipmentRepository.findAll().isEmpty() && bookingRepository.findAll().isEmpty();
    }
}