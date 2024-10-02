package org.example.adventurexpbackend.config.initData;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InitData implements CommandLineRunner {

    // services for saving data
    // private final

    @Override
    public void run(String... args) throws Exception {
        // save data
        List<Object> dataClass = new ArrayList<>(List.of(
                new InitDataActivity(),
                new InitDataEquipment(),
                new InitDataBooking()
        ));

        for (Object data : dataClass) {
            data.saveData();
        }
    }




}
