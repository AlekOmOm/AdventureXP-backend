package org.example.adventurexpbackend.config.initData;

import org.example.adventurexpbackend.repository.ActivityRepository;
import org.example.adventurexpbackend.repository.BookingRepository;
import org.example.adventurexpbackend.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitData implements CommandLineRunner {

    private final InitDataActivity initDataActivity;
    private final InitDataEquipment initDataEquipment;
    private final InitDataBooking initDataBooking;

    // repositories DI
    private final ActivityRepository activityRepository;
    private final EquipmentRepository equipmentRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public InitData(InitDataActivity initDataActivity, InitDataEquipment initDataEquipment, InitDataBooking initDataBooking, ActivityRepository activityRepository, EquipmentRepository equipmentRepository, BookingRepository bookingRepository) {
        this.initDataActivity = initDataActivity;
        this.initDataEquipment = initDataEquipment;
        this.initDataBooking = initDataBooking;
        this.activityRepository = activityRepository;
        this.equipmentRepository = equipmentRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(isDBEmpty()) {
            initDataActivity.saveData();
            initDataBooking.setActivities(initDataActivity.getActivities());
            initDataBooking.saveData();
        }
    }

    private boolean isDBEmpty() {
        if (activityRepository.findAll().isEmpty() && equipmentRepository.findAll().isEmpty() && bookingRepository.findAll().isEmpty()) {
            System.out.println("Database is empty");
            System.out.println("Initializing data...");
            return true;
        }
        return false;
    }
}