package org.example.adventurexpbackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SequenceResetter {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SequenceResetter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void resetSequence(String sequenceName) {
        jdbcTemplate.execute("ALTER SEQUENCE " + sequenceName + " RESTART WITH 1");
    }

    public void resetAutoIncrement(String tableName) {
        jdbcTemplate.execute("ALTER TABLE " + tableName + " AUTO_INCREMENT = 1");
    }

    public void resetAutoIncrement(String tableName, long startValue) {
        jdbcTemplate.execute("ALTER TABLE " + tableName + " AUTO_INCREMENT = " + startValue);
    }


    public void resetSequences(long startValueActivity, long startValueEquipment, long startValueEquipmentType, long startValueBooking) {

        resetAutoIncrement("activity");
        resetAutoIncrement("equipment");
        resetAutoIncrement("equipment_type");
        resetAutoIncrement("booking");

        resetAutoIncrement("activity", startValueActivity);
        resetAutoIncrement("equipment", startValueEquipment);
        resetAutoIncrement("equipment_type", startValueEquipmentType);
        resetAutoIncrement("booking", startValueBooking);
    }


}