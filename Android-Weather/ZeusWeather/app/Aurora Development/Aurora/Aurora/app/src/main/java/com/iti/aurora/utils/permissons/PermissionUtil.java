package com.iti.aurora.utils.permissons;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;

import com.iti.aurora.R;
import com.iti.aurora.editmeds.view.EditMedsActivity;
import com.iti.aurora.utils.dialogs.TwoButtonsDialog;

public class PermissionUtil {

    public static boolean iskBatteryOptimizePermission(Context context) {

        String permission = android.Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS;
        int res = context.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);

    }


    public static void requestIgnorePowerOptimize(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String packageName = context.getPackageName();
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if (pm.isIgnoringBatteryOptimizations(packageName))
                intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
            else {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
            }
            context.startActivity(intent);

        }
    }

    public static void checkOverlayPermission(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {
                AlertDialog.Builder builder = TwoButtonsDialog.TwoButtonsDialogBuilder(context,
                        context.getResources().getString(R.string.EnableNotiDialog),
                        context.getResources().getString(R.string.EnableNotiDialogContent),
                        context.getResources().getString(R.string.yes),
                        context.getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(myIntent);
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }

                );
                builder.show();

            }
        }
    }
}