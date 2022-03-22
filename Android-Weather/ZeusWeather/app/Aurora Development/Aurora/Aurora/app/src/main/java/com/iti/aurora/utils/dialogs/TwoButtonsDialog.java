package com.iti.aurora.utils.dialogs;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class TwoButtonsDialog {

    public static AlertDialog.Builder TwoButtonsDialogBuilder(Context context, String title, String message, String positiveButtonText, String negativeButtonText, DialogInterface.OnClickListener positiveClick, DialogInterface.OnClickListener negativeClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton(negativeButtonText, negativeClick);
        builder.setPositiveButton(positiveButtonText, positiveClick);
        return builder;
    }
}