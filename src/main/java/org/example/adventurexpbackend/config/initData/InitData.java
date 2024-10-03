package org.example.adventurexpbackend.config.initData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitData implements CommandLineRunner {

    private final InitDataActivity initDataActivity;
    private final InitDataEquipment initDataEquipment;
    private final InitDataBooking initDataBooking;

    @Autowired
    public InitData(InitDataActivity initDataActivity, InitDataEquipment initDataEquipment, InitDataBooking initDataBooking) {
        this.initDataActivity = initDataActivity;
        this.initDataEquipment = initDataEquipment;
        this.initDataBooking = initDataBooking;
    }

    @Override
    public void run(String... args) throws Exception {
        initDataActivity.saveData();
        initDataEquipment.saveData();
        initDataBooking.setActivities(initDataActivity.getActivities());
        initDataBooking.saveData();
    }
}