
-- Clear existing data
DELETE FROM equipment;
DELETE FROM activity;

-- Reset auto-increment counters
ALTER TABLE activities AUTO_INCREMENT = 1;
ALTER TABLE equipment AUTO_INCREMENT = 1;

-- Insert test data for Equipment
INSERT INTO equipment (name, functional, under_service) VALUES
('Paintball Gun', true, false),
('Go-Kart', true, false),
('Sumo Suit', true, false),
('Sumo Suit', true, false);

-- Insert test data for Activity
INSERT INTO activity (name, description, price_pr_person, time_max_limit, age_min, age_max, persons_min, persons_max, opening_time, closing_time, time_slot_interval) VALUES
('Paintball', 'Exciting paintball activity', 200, 120, 12, 50, 4, 20, '09:00', '18:00', 30),
('Go-Kart Racing', 'Fast-paced go-kart racing', 300, 60, 10, 60, 2, 10, '10:00', '20:00', 15),
('Sumo Wrestling', 'Fun sumo wrestling activity', 150, 30, 8, 40, 2, 4, '11:00', '17:00', 10);

-- Update Equipment with Activity IDs
UPDATE equipment SET activity_id = (SELECT id FROM activities WHERE name = 'Paintball' LIMIT 1) WHERE name = 'Paintball Gun';
UPDATE equipment SET activity_id = (SELECT id FROM activities WHERE name = 'Go-Kart Racing' LIMIT 1) WHERE name = 'Go-Kart';
UPDATE equipment SET activity_id = (SELECT id FROM activities WHERE name = 'Sumo Wrestling' LIMIT 1) WHERE name = 'Sumo Suit';