


    // convert types to Equipment objects

    public List<Equipment> getPaintballEquipmentTypeList() {

        List<Equipment> equipmentList = new ArrayList<>();
        for (EquipmentType equipmentType : getPaintballEquipmentTypes()) {
            equipmentList.add(new Equipment(equipmentType.getName(), true, false, null));
        }
        return equipmentList;
    }

    public List<Equipment> getClimbingEquipmentTypeList() {

        List<Equipment> equipmentList = new ArrayList<>();
        for (EquipmentType equipmentType : getClimbingEquipmentTypes()) {
            equipmentList.add(new Equipment(equipmentType.getName(), true, false, null));
        }
        return equipmentList;
    }

    public List<Equipment> getGoKartEquipmentTypeList() {

        List<Equipment> equipmentList = new ArrayList<>();
        for (EquipmentType equipmentType : getGoKartEquipmentTypes()) {
            equipmentList.add(new Equipment(equipmentType.getName(), true, false, null));
        }
        return equipmentList;
    }


}