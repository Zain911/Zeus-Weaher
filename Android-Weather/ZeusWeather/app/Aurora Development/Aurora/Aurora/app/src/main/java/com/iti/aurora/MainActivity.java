package com.iti.aurora;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.iti.aurora.utils.permissons.PermissionUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null)
            this.getSupportActionBar().hide();
        setContentView(R.layout.activity_main_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNavigationView, Navigation.findNavController(this, R.id.nav_host_fragment));
        bottomNavigationView.setOnNavigationItemReselectedListener(menuItem -> {
        });
       if (!PermissionUtil.iskBatteryOptimizePermission(getApplicationContext()))
            PermissionUtil.requestIgnorePowerOptimize(getApplicationContext());
        PermissionUtil.checkOverlayPermission(this);
    }

}