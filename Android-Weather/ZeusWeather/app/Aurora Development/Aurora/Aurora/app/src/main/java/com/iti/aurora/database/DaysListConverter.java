package com.iti.aurora.database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iti.aurora.model.medicine.Dose;
import com.iti.aurora.utils.selectdays.DaysOfWeek;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DaysListConverter {

    @TypeConverter
    public static List<DaysOfWeek> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<DaysOfWeek>>() {}.getType();

        Gson gson = new Gson();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<DaysOfWeek> someObjects) {
        Gson gson = new Gson();
        return gson.toJson(someObjects);
    }
}
