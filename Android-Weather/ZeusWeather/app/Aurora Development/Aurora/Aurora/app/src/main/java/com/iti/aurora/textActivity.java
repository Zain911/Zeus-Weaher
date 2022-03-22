package com.iti.aurora;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

public class textActivity extends AppCompatActivity {
    private CardView medicationNameCardView;
    View medicationNameView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private void makeTransition(View view,  CardView cardView) {
        if (view.getVisibility() == View.VISIBLE) {
            TransitionManager.beginDelayedTransition(cardView,
                    new AutoTransition());
            view.setVisibility(View.GONE);
           // image.setImageResource(com.hbb20.R.drawable.ccp_ic_arrow_drop_down);

        } else {
            TransitionManager.beginDelayedTransition(cardView,
                    new AutoTransition());
            view.setVisibility(View.VISIBLE);
           // image.setImageResource(com.hbb20.R.drawable.ccp_ic_arrow_drop_down);
        }
    }
}
