package com.iti.aurora;

import static android.content.Context.WINDOW_SERVICE;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iti.aurora.database.ConcreteLocalSource;
import com.iti.aurora.model.Repository;
import com.iti.aurora.model.RepositoryInterface;
import com.iti.aurora.model.medicine.Dose;
import com.iti.aurora.model.medicine.Medicine;
import com.iti.aurora.utils.workmanager.DoseAlarmManager;

import java.text.MessageFormat;

public class NotificationDialogOverApp {

    private Context context;
    private View mView;
    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private LayoutInflater layoutInflater;
    private ImageView medicnieIcon_ImageView;
    TextView timerTextView, medicnieNameTextView, instructionTextView, reasonTextView;
    RepositoryInterface repositoryInterface;

    public NotificationDialogOverApp(Context context, Medicine medicine, String time, Dose dose) {
        this.context = context;
        repositoryInterface = Repository.getInstance(
                ConcreteLocalSource.getInstance(context.getApplicationContext()), context.getApplicationContext()
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

        }
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = layoutInflater.inflate(R.layout.dialog_dosage_reminder, null);
        timerTextView = mView.findViewById(R.id.timerTextView);
        medicnieNameTextView = mView.findViewById(R.id.medicnieName_textview);
        instructionTextView = mView.findViewById(R.id.instruction_TextView);
        reasonTextView = mView.findViewById(R.id.reason_TextView);
        medicnieIcon_ImageView=mView.findViewById(R.id.medicnieIcon_ImageView);

       // medicnieIcon_ImageView.setImageResource();
        if (medicine.getMedicineForm().equalsIgnoreCase("Pills"))
            medicnieIcon_ImageView.setBackgroundResource(R.drawable.ic_medicine_pill);
        else if (medicine.getMedicineForm().equalsIgnoreCase("Injection"))
            medicnieIcon_ImageView.setBackgroundResource(R.drawable.icon_vaccine);
        else if (medicine.getMedicineForm().equalsIgnoreCase("Powder"))
            medicnieIcon_ImageView.setBackgroundResource(R.drawable.icon_powder);
        else if (medicine.getMedicineForm().equalsIgnoreCase("Drops"))
            medicnieIcon_ImageView.setBackgroundResource(R.drawable.ic_dropper);
        else if (medicine.getMedicineForm().equalsIgnoreCase("Inhaler"))
            medicnieIcon_ImageView.setBackgroundResource(R.drawable.ic_inhaler);
        else
            medicnieIcon_ImageView.setBackgroundResource(R.drawable.ic_medicine_pill);



        timerTextView.setText(time);
        medicnieNameTextView.setText(medicine.getName().concat(" " + medicine.getMedicineForm()));
        instructionTextView.setText((MessageFormat.format("{0} {1} from {2} {3}",
                medicine.getNumberOfUnits(), medicine.getStrengthUnit(),
                medicine.getMedicineForm(),
                medicine.getInstruction())));

        reasonTextView.setText(medicine.getReasonOfTaking());
        mView.findViewById(R.id.skip_button).setOnClickListener(view -> close());
        mView.findViewById(R.id.snooze_button).setOnClickListener(view -> {
            DoseAlarmManager doseAlarmManager = new DoseAlarmManager(context, dose, medicine , true);
            Toast.makeText(context, R.string.back_in_five_minuts, Toast.LENGTH_SHORT).show();
            close();
        });
        mView.findViewById(R.id.take_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo add is taken in DB here
                dose.setTaken(true);
                repositoryInterface.updateDose(dose);
                close();

            }
        });
        mParams.gravity = Gravity.CENTER;
        mWindowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);

    }

    public void open() {

        try {

            if (mView.getWindowToken() == null) {
                if (mView.getParent() == null) {
                    mWindowManager.addView(mView, mParams);
                }
            }
        } catch (Exception e) {
            Log.d("Error1", e.toString());
        }

    }

    public void close() {
        try {
            ((WindowManager) context.getSystemService(WINDOW_SERVICE)).removeView(mView);
            mView.invalidate();
            ((ViewGroup) mView.getParent()).removeAllViews();
        } catch (Exception e) {
            Log.d("Error2", e.toString());
        }
    }
}