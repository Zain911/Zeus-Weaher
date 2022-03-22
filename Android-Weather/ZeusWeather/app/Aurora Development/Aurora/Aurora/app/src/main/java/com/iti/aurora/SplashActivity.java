package com.iti.aurora;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.iti.aurora.intro.IntroActivity;

public class SplashActivity extends AppCompatActivity {
    CardView cardView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar()!=null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        // todo implement shared preferences here
      new Handler().postDelayed(()-> startActivity(new Intent(this, IntroActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)), 3200);
    }
}
