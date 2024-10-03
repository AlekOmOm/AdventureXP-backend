package org.example.adventurexpbackend.service;

import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.repository.ActivityRepository;
import org.example.adventurexpbackend.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
}
