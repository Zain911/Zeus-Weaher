package com.iti.aurora.utils.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.iti.aurora.R;
import com.iti.aurora.model.medicine.Medicine;
import com.iti.aurora.utils.Constants;

import java.text.MessageFormat;
import java.util.Random;

public class NotificationCenter {
    public static void showRefill(Context context, Medicine medicine) {
        Uri alarmsound = Uri.parse("android.resource://" + context.getApplicationContext().getPackageName() + "/" + R.raw.messagetone);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
       int medcnieIcon = Constants.getMedicnieIcon(medicine.getName());
     //   int medcnieIcon = R.drawable.ic_notification_reminder;

        if (medicine.getDosagesLeft() <= medicine.getRemindMeOn()) {
            NotificationChannel channel = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                channel = new NotificationChannel("my_channel_02", "Medication Refill Reminder", NotificationManager.IMPORTANCE_HIGH);
                channel.setSound(alarmsound, Constants.NotificationUtil.audioAttributesFactory);
                channel.enableLights(true);
                channel.enableVibration(true);
            }
//todo add icon of medication
            Notification notification = builder

                    .setContentTitle(MessageFormat.format("Need to Refill: {0} {1}", medicine.getName(), medicine.getMedicineForm()))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(MessageFormat.format(context.getString(R.string.refilMsgVofification), medicine.getName(), medicine.getMedicineForm(), medicine.getRemindMeOn(), medicine.getDosagesLeft())))
                    .setAutoCancel(true)
                    .setColor(Color.RED)

                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            medcnieIcon))
                    .setChannelId("my_channel_02")
                    .build();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(new Random(System.currentTimeMillis()).nextInt(120), notification);


        }
    }

    public static void showMedicationReminder(Context context, PendingIntent intent, Medicine medicine, String time) {
        Uri alarmsound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getApplicationContext().getPackageName() + "/" + R.raw.messagetone);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        int medcnieIcon = Constants.getMedicnieIcon(medicine.getName());
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel("my_channel_01", "Medicine", NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(alarmsound, Constants.NotificationUtil.audioAttributesFactory);
            channel.enableLights(true);
            channel.enableVibration(true);
        }
//todo add icon of medication
        Notification notification = builder
                .setContentTitle("Time to Take : " + medicine.getName() + " " + medicine.getMedicineForm())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.youshould_take) + medicine.getName()
                        + " at " + time + "\n" + "don't forget " + medicine.getInstruction()))
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.reminder))
                .setContentIntent(intent)
                .setSound(alarmsound)
                .setChannelId("my_channel_01")
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(new Random(System.currentTimeMillis()).nextInt(120), notification);
    }


}
