package com.iti.aurora.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.iti.aurora.database.dao.DoseDAO;
import com.iti.aurora.database.dao.MedicineDAO;
import com.iti.aurora.database.dao.TreatmentDAO;
import com.iti.aurora.model.medicine.Dose;
import com.iti.aurora.model.medicine.Medicine;
import com.iti.aurora.model.medicine.Treatment;


@Database(entities = {Medicine.class, Dose.class, Treatment.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase instance = null;
    public abstract MedicineDAO medicineDAO();
    public abstract DoseDAO doseDAO();
    public abstract TreatmentDAO treatmentDAO();

    public static synchronized AppDataBase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),AppDataBase.class,"auroradatabase").build();
        }
        return instance;
    }


}



