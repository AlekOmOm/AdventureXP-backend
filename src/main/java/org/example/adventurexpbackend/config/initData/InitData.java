package org.example.adventurexpbackend.config.initData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InitData implements CommandLineRunner {

    // services for saving data
    // private final

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
        // save data

        initDataActivity.saveData();
        initDataEquipment.saveData(initDataActivity.getActivities());
        initDataBooking.saveData();
    }




}
