package com.telugu.ringtoneapp.best.songs.ringtones.ui.ringtone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;


public class SettingDialog {

    static Context context;

    public static void showSettingDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SettingDialog.lambda$showSettingDialog$0(activity, dialogInterface, i);
            }
        });
        builder.setNegativeButton("Cancel", SettingDialogInterface.INSTANCE);
        builder.show();
    }


    public static void lambda$showSettingDialog$0(Activity activity, DialogInterface dialogInterface, int i) {
        dialogInterface.cancel();
        openSettings();
    }

    public static void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);

    }
}
