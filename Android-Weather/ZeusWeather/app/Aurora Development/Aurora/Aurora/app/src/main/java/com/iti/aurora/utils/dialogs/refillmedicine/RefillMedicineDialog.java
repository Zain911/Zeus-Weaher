package com.iti.aurora.utils.dialogs.refillmedicine;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.iti.aurora.R;
import com.iti.aurora.model.medicine.Medicine;

import java.text.MessageFormat;

public class RefillMedicineDialog extends Dialog {

    TextView youCurrentlyHaveTextView;
    TextInputLayout noOfDosagesToAddTextInputLayout;
    TextInputEditText noOfDosagesToAddTextInputEditText;
    Button refillButton;

    public RefillMedicineDialog(@NonNull Context context, Medicine medicine, RefillMedicineClickHandler refillMedicineClickHandler) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_refill_medicine);

        youCurrentlyHaveTextView = findViewById(R.id.youCurrentlyHaveTextView);
        noOfDosagesToAddTextInputLayout = findViewById(R.id.noOfDosagesToAddTextInputLayout);
        noOfDosagesToAddTextInputEditText = findViewById(R.id.noOfDosagesToAddTextInputEditText);
        refillButton = findViewById(R.id.refillButton);

        youCurrentlyHaveTextView.setText(MessageFormat.format("{0} {1} for {2}", getContext().getResources().getString(R.string.you_currently_have), medicine.getDosagesLeft(), medicine.getName()));

        refillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noOfDosagesToAddTextInputLayout.setError("");
                if (!noOfDosagesToAddTextInputEditText.getText().toString().isEmpty()) {
                    refillMedicineClickHandler.refillMedicine(medicine, Integer.parseInt(noOfDosagesToAddTextInputEditText.getText().toString()));
                    RefillMedicineDialog.this.dismiss();
                } else {
                    noOfDosagesToAddTextInputLayout.setError(getContext().getResources().getString(R.string.pleaseEnterNumberToAdd));
                }
            }
        });

    }


}