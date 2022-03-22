package com.iti.aurora.model.medicine;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.iti.aurora.database.DateConverter;
import com.iti.aurora.database.DaysListConverter;
import com.iti.aurora.utils.selectdays.DaysOfWeek;

import java.util.Date;
import java.util.List;

@Entity(tableName = "treatment",
        foreignKeys = @ForeignKey(entity = Medicine.class,
                parentColumns = "medId",
                childColumns = "medId",
                onDelete = ForeignKey.CASCADE
        )
)
public class Treatment {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "treatmentId")
    @NonNull
    long treatmentId;

    @ColumnInfo(name = "medId")
    @NonNull
    private long medId;

    @TypeConverters({DateConverter.class})
    private Date startDate;

    @TypeConverters({DateConverter.class})
    private Date endDate;

    @TypeConverters({DaysListConverter.class})
    private List<DaysOfWeek> daysList;

    @ColumnInfo(name = "recurrencyModel")
    private String recurrency;

    public Treatment() {
    }

    public Treatment(long medId, Date startDate, Date endDate) {
        this.medId = medId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public long getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(long treatmentId) {
        this.treatmentId = treatmentId;
    }

    public long getMedId() {
        return medId;
    }

    public void setMedId(long medId) {
        this.medId = medId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<DaysOfWeek> getDaysList() {
        return daysList;
    }

    public void setDaysList(List<DaysOfWeek> daysList) {
        this.daysList = daysList;
    }

    public String getRecurrency() {
        return recurrency;
    }

    public void setRecurrency(String recurrency) {
        this.recurrency = recurrency;
    }
}