package com.iti.aurora.utils.workmanager;

import static com.iti.aurora.utils.notification.NotificationCenter.showMedicationReminder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.iti.aurora.MainActivity;
import com.iti.aurora.R;
import com.iti.aurora.NotificationDialogOverApp;
import com.iti.aurora.model.medicine.Dose;
import com.iti.aurora.model.medicine.Medicine;
import com.iti.aurora.utils.Constants;
import com.iti.aurora.utils.notification.NotificationCenter;

import java.text.MessageFormat;
import java.util.Random;

public class NotifierAlarm extends BroadcastReceiver {
    String time;


    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getBundleExtra("MED_DATA");
        Medicine medicine = (Medicine) bundle.getSerializable(Constants.NotificationUtil.MEDICNIE_SPECS);
        Dose dose = (Dose) bundle.getSerializable(Constants.NotificationUtil.DOSE_SPECS);
        time = intent.getStringExtra(Constants.NotificationUtil.MEDICINE_TIME);
        int NOTIFICATION_ID = new Random(System.currentTimeMillis()).nextInt(120);
        Uri alarmsound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getApplicationContext().getPackageName() + "/" + R.raw.messagetone);

        //todo here luncher activity name form notification click
        Intent intent1 = new Intent(context, MainActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addParentStack(MainActivity.class);
        taskStackBuilder.addNextIntent(intent1);
        PendingIntent intent2 = taskStackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCenter.showMedicationReminder(context, intent2, medicine, time);

        if (medicine.getDosagesLeft() <= medicine.getRemindMeOn())
            NotificationCenter.showRefill(context, medicine);
        showDialog(context, medicine, time, dose);
    }


    private void showDialog(Context context, Medicine medicine, String time, Dose dose) {
        NotificationDialogOverApp notificationDialogOverApp = new NotificationDialogOverApp(context, medicine, time, dose);
        notificationDialogOverApp.open();

    }

}