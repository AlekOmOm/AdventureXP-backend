
package org.example.adventurexpbackend.config;

import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.Booking;
import org.example.adventurexpbackend.model.Equipment;
import org.example.adventurexpbackend.model.EquipmentType;
import org.example.adventurexpbackend.repository.ActivityRepository;
import org.example.adventurexpbackend.repository.BookingRepository;
import org.example.adventurexpbackend.repository.EquipmentRepository;
import org.example.adventurexpbackend.repository.EquipmentTypeRepository;
import org.example.adventurexpbackend.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class InitData implements CommandLineRunner {

    private final ActivityRepository activityRepository;
    private final EquipmentRepository equipmentRepository;
    private final EquipmentTypeRepository equipmentTypeRepository;
    private final BookingRepository bookingRepository;
    private final SequenceResetter sequenceResetter;
    private final ActivityService activityService;

    @Autowired
    public InitData(ActivityRepository activityRepository, EquipmentRepository equipmentRepository, EquipmentTypeRepository equipmentTypeRepository, BookingRepository bookingRepository, SequenceResetter sequenceResetter, ActivityService activityService) {
        this.activityRepository = activityRepository;
        this.equipmentRepository = equipmentRepository;
        this.equipmentTypeRepository = equipmentTypeRepository;
        this.bookingRepository = bookingRepository;
        this.sequenceResetter = sequenceResetter;
        this.activityService = activityService;
    }

    @Override
    public void run(String... args) throws Exception {
        sequenceResetter.resetSequences();

        Set<EquipmentType> paintballEquipmentTypes = createPaintballEquipmentTypes();
        Set<EquipmentType> climbingEquipmentTypes = createClimbingEquipmentTypes();
        Set<EquipmentType> goKartEquipmentTypes = createGoKartEquipmentTypes();

        List<Equipment> paintballEquipmentList = createPaintballEquipment();
        List<Equipment> climbingEquipmentList = createClimbingEquipment();
        List<Equipment> goKartEquipmentList = createGoKartEquipment();

        List<Activity> activities = createActivities(paintballEquipmentList, climbingEquipmentList, goKartEquipmentList, paintballEquipmentTypes, climbingEquipmentTypes, goKartEquipmentTypes);
        createBookings(activities);
    }

    private Set<EquipmentType> createPaintballEquipmentTypes() {
        Set<EquipmentType> equipmentTypes = new HashSet<>(List.of(
                new EquipmentType("Paintball gun"),
                new EquipmentType("Paintball mask"),
                new EquipmentType("Paintball suit")
        ));
        return new HashSet<>((equipmentTypes));
    }

    private Set<EquipmentType> createClimbingEquipmentTypes() {
        Set<EquipmentType> equipmentTypes = new HashSet<>(List.of(
                new EquipmentType("Climbing shoes"),
                new EquipmentType("Climbing harness"),
                new EquipmentType("Climbing chalk")
        ));
        return new HashSet<>((equipmentTypes));
    }

    private Set<EquipmentType> createGoKartEquipmentTypes() {
        Set<EquipmentType> equipmentTypes = new HashSet<>(List.of(
                new EquipmentType("Go-kart car"),
                new EquipmentType("Go-kart helmet"),
                new EquipmentType("Go-kart suit"),
                new EquipmentType("Go-kart gloves")
        ));
        return new HashSet<>((equipmentTypes));
    }

    private List<Equipment> createPaintballEquipment() {
        List<Equipment> equipmentList = new ArrayList<>(List.of(
                new Equipment("Paintball gun", true, false),
                new Equipment("Paintball mask", true, false),
                new Equipment("Paintball suit", true, false)
        ));
        return equipmentList;
    }

    private List<Equipment> createClimbingEquipment() {
        List<Equipment> equipmentList = new ArrayList<>(List.of(
                new Equipment("Climbing shoes", true, false),
                new Equipment("Climbing harness", true, false),
                new Equipment("Climbing chalk", true, false)
        ));
        return equipmentList;
    }

    private List<Equipment> createGoKartEquipment() {
        return new ArrayList<>(List.of(
                new Equipment("Go-kart car", true, false),
                new Equipment("Go-kart helmet", true, false),
                new Equipment("Go-kart suit", true, false),
                new Equipment("Go-kart gloves", true, false)
        ));
    }

    private List<Activity> createActivities(List<Equipment> paintballEquipmentList, List<Equipment> climbingEquipmentList, List<Equipment> goKartEquipmentList, Set<EquipmentType> paintballEquipmentTypes, Set<EquipmentType> climbingEquipmentTypes, Set<EquipmentType> goKartEquipmentTypes) {
        List<Activity> activities = new ArrayList<>(List.of(
                new Activity("Paintball", "Paintball is a fun activity for everyone", 100, 120, 10, 100, 2, 20, LocalTime.of(10, 0), LocalTime.of(18, 0), 2, paintballEquipmentList, paintballEquipmentTypes),
                new Activity("Climbing", "Climbing is a fun activity for everyone", 100, 120, 10, 100, 2, 20, LocalTime.of(10, 0), LocalTime.of(18, 0), 2, climbingEquipmentList, climbingEquipmentTypes),
                new Activity("Go-kart", "Go-kart is a fun activity for everyone", 100, 120, 10, 100, 2, 20, LocalTime.of(10, 0), LocalTime.of(18, 0), 2, goKartEquipmentList, goKartEquipmentTypes)
        ));
        return activityService.saveAllActivities(activities);
    }

    private void createBookings(List<Activity> activities) {
        List<Booking> bookings = new ArrayList<>(List.of(
                new Booking("John the Baptist", LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(12, 0), activities.get(0).getPersonsMax(), activities.get(0)), // Paintball
                new Booking("Francis of Assisi", LocalDate.now(), LocalTime.of(12, 0), LocalTime.of(14, 0), 6, activities.get(0)), // Paintball 2 booking
                new Booking("Soeren Pind", LocalDate.now(), LocalTime.of(14, 0), LocalTime.of(16, 0), 8, activities.get(0)), // Paintball 3 booking
                new Booking("Scooby-Doo", LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(12, 0), activities.get(1).getPersonsMax(), activities.get(1)), // Climbing
                new Booking("Mickey Mouse", LocalDate.now(), LocalTime.of(12, 0), LocalTime.of(14, 0), 6, activities.get(1)), // Climbing 2 booking
                new Booking("John Lennon", LocalDate.now(), LocalTime.of(14, 0), LocalTime.of(16, 0), 8, activities.get(1)), // Climbing 3 booking
                new Booking("George Harrison", LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(12, 0), activities.get(2).getPersonsMax(), activities.get(2)), // Go-kart
                new Booking("Margrethe den Foerste", LocalDate.now(), LocalTime.of(12, 0), LocalTime.of(14, 0), 6, activities.get(2)), // Go-kart 2 booking
                new Booking("Churchill", LocalDate.now(), LocalTime.of(14, 0), LocalTime.of(16, 0), 8, activities.get(2)) // Go-kart 3 booking
        ));
        bookingRepository.saveAll(bookings);
    }
}
