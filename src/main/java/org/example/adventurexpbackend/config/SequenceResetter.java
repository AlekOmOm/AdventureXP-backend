package org.example.adventurexpbackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

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
}