

-- Create the activities table
CREATE TABLE activities (
                            id LONG AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            description TEXT,
                            price_pr_person INT NOT NULL,
                            time_max_limit INT NOT NULL,
                            age_min INT NOT NULL,
                            age_max INT NOT NULL,
                            persons_min INT NOT NULL,
                            persons_max INT NOT NULL,
                            opening_time TIME NOT NULL,
                            closing_time TIME NOT NULL,
                            time_slot_interval INT NOT NULL
);

-- Create the equipment table with a foreign key reference to activities
CREATE TABLE equipment (
                           id LONG AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           functional BOOLEAN NOT NULL,
                           under_service BOOLEAN NOT NULL,
                           activity_id BIGINT,
                           FOREIGN KEY (activity_id) REFERENCES activities(id) ON DELETE CASCADE
);

