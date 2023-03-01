package com.telugu.ringtoneapp.best.songs.ringtones;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;



public class MainActivity extends AppCompatActivity {




    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(com.telugu.ringtoneapp.best.songs.ringtones.R.id.toolbar));
        requestForPermission();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    private void requestForPermission() {

            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_SETTINGS", "android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS"}, 1);

    }





}
