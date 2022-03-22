package com.iti.aurora.firestore.firestoremirror;

import com.iti.aurora.model.User;
import com.iti.aurora.model.medicine.Dose;
import com.iti.aurora.model.medicine.Medicine;
import com.iti.aurora.model.medicine.Treatment;

import java.util.List;

public interface RemoteSourceFireStoreMirror {
    void putUser(User user);
    void putMedicine(Medicine medicine);
    void putTreatment(Treatment treatment);
    void putDoses(List<Dose> dosesList);
    void putDose(Dose dose);

    void getUser();
    void getMedicine(String medId);
    void getTreatment(String medId);
    void getDoseById(String doseId);


    void updateUserData(User user);
    void updateMedicine(Medicine medicine);
    void updateTreatment(Treatment treatment);
    void updateDoses(List<Dose> doseList);
    void updateDose(Dose dose);

    void removeUser(User user);
    void removeMedicine(Medicine medicine);
    void removeTreatment(Treatment treatment);
    void removeDoses(List<Dose> doseLists);

}
