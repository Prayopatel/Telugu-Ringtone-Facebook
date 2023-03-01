package com.telugu.ringtoneapp.best.songs.ringtones.ui.ringtone;

import android.content.DialogInterface;


public final class SettingDialogInterface implements DialogInterface.OnClickListener {
    public static final  SettingDialogInterface INSTANCE = new SettingDialogInterface();

    private  SettingDialogInterface() {
    }

    @Override
    public  void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.cancel();
    }
}
