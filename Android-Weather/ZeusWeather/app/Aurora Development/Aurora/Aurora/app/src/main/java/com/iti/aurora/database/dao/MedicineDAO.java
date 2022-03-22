package com.iti.aurora.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.iti.aurora.model.medicine.Medicine;

import java.util.List;

@Dao
public interface MedicineDAO {
    @Query("SELECT * FROM medicine")
    LiveData<List<Medicine>> getAllMedicines();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMedicine(Medicine medicine);

    @Update
    void updateMedicine(Medicine medicine);

    @Query("SELECT * FROM medicine WHERE medId = :medId")
    Medicine getSpecificMedicine(long medId);

    @Delete
    void deleteMedicine(Medicine medicine);
}