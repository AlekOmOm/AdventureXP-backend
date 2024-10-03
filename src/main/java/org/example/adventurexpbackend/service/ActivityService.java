package org.example.adventurexpbackend.service;

import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.Equipment;
import org.example.adventurexpbackend.model.EquipmentType;
import org.example.adventurexpbackend.repository.ActivityRepository;
import org.example.adventurexpbackend.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final EquipmentRepository equipmentRepository;

    @Autowired
    public ActivityService(ActivityRepository activityRepository, EquipmentRepository equipmentRepository) {
        this.activityRepository = activityRepository;
        this.equipmentRepository = equipmentRepository;
    }

    public List<Activity> saveAllActivities(List<Activity> activities) {
        List<Activity> activityList = new ArrayList<>();
        for (Activity activity : activities) {
            activity.setEquipmentList(equipmentRepository.saveAll(activity.getEquipmentList()));
            activityList.add(activityRepository.save(activity));
        }
        return activityList;
    }

    public void addEquipmentToActivity(Activity activity, Equipment equipment) {
        Set<EquipmentType> requiredTypes = activity.getEquipmentTypes();
        if (requiredTypes.contains(equipment.getEquipmentType())) {
            equipment.setActivity(activity);
            equipmentRepository.save(equipment);
        } else {
            throw new IllegalArgumentException("Equipment type not allowed for this activity");
        }
    }

    // addEquipmentTypeToctivity method
    public void addEquipmentTypeToActivity(Activity activity, EquipmentType equipmentType) {
        Set<EquipmentType> requiredTypes = activity.getEquipmentTypes();
        requiredTypes.add(equipmentType);
        activity.setEquipmentTypes(requiredTypes);
        activityRepository.save(activity);
    }

    // removeEquipmentTypeFromActivity method

    public void removeEquipmentTypeFromActivity(Activity activity, EquipmentType equipmentType) {
        Set<EquipmentType> requiredTypes = activity.getEquipmentTypes();
        requiredTypes.remove(equipmentType);
        activity.setEquipmentTypes(requiredTypes);
        activityRepository.save(activity);
    }
}

