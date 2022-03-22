package com.iti.aurora.utils.spfactory;

import android.content.Context;
import android.content.SharedPreferences;

import com.iti.aurora.utils.Constants;

public class SharedPreferencesFactory {

    private static final String SHARED_PREFERENCES_FILE_NAME = "SHARED_PREFERENCES_FILE_NAME";
    private static SharedPreferencesFactory instance = null;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private SharedPreferencesFactory(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SharedPreferencesFactory getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesFactory(context);
        }
        return instance;
    }

    public void putBoolean(boolean isValue){
        editor.putBoolean(Constants.SharedPreferencesConstants.USER_SIGN_IN,isValue);
        editor.commit();
    }

    public boolean getBoolean() {
        return sharedPreferences.getBoolean(Constants.SharedPreferencesConstants.USER_SIGN_IN,false);
    }
}
