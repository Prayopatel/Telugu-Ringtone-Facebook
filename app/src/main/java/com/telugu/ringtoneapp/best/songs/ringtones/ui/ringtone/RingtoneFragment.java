package com.telugu.ringtoneapp.best.songs.ringtones.ui.ringtone;

import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.telugu.ringtoneapp.best.songs.ringtones.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;


public class RingtoneFragment extends Fragment {
    public static final int CONTACT_CHOOSER_ACTIVITY_CODE = 1001;
    String TAG = "RingtoneFragment";
    private String folderName = "Islamic Ringtone";
    private MediaPlayer mediaPlayer;
    int position;
    private String rintoneTitle;
    private int song;
    private AdView mAdView;





    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        setHasOptionsMenu(true);
        View inflate = layoutInflater.inflate(R.layout.fragment_ringtone, viewGroup, false);
        inflate.setClickable(true);
        inflate.setFocusable(true);

        mAdView = inflate.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        this.position = getArguments().getInt("position");
        this.rintoneTitle = getArguments().getString("name");
        this.song = getArguments().getInt("song");
        ((ImageButton) inflate.findViewById(R.id.myRingtone)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ringtoneMethod();
            }
        });
        ((ImageButton) inflate.findViewById(R.id.myAlarm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmMethod();
            }
        });
        ((ImageButton) inflate.findViewById(R.id.myContact)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               contactMethod();
            }
        });
        ((ImageButton) inflate.findViewById(R.id.myNotification)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               notificationMethod();
            }
        });
        this.mediaPlayer = new MediaPlayer();
        final ImageButton imageButton = (ImageButton) inflate.findViewById(R.id.myPlayPause);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RingtoneFragment.this.lambda$onCreateView$4$RingtoneFragment(imageButton, view);
            }
        });
        return inflate;
    }



    public void lambda$onCreateView$4$RingtoneFragment(final ImageButton imageButton, View view) {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            MediaPlayer create = MediaPlayer.create(getContext(), this.song);
            this.mediaPlayer = create;
            create.start();
            imageButton.setImageResource(R.drawable.ic_pause);
        } else {
            MediaPlayer mediaPlayer2 = this.mediaPlayer;
            if (mediaPlayer2 != null && mediaPlayer2.isPlaying()) {
                this.mediaPlayer.stop();
                imageButton.setImageResource(R.drawable.ic_play);
            }
        }

        this.mediaPlayer.setOnCompletionListener(mediaPlayer3 -> {
            mediaPlayer3.stop();
            imageButton.setImageResource(R.drawable.ic_play);
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        TextView textView = getActivity().findViewById(R.id.titletext);
        textView.setText(this.rintoneTitle);
        textView.setTextColor(getResources().getColor(R.color.white));
    }

    public void ringtoneMethod() {
        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        if ((Build.VERSION.SDK_INT >= 23 && audioManager.getRingerMode() == 0) || (Build.VERSION.SDK_INT >= 23 && notificationManager.getCurrentInterruptionFilter() == NotificationManager.INTERRUPTION_FILTER_PRIORITY)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && !notificationManager.isNotificationPolicyAccessGranted()) {
                try {
                    Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                    startActivity(intent);
                } catch (Exception et) {
                    Toast.makeText(getContext(), "Something Went Wrong! ", Toast.LENGTH_SHORT).show();
                }
            } else if (Build.VERSION.SDK_INT >= 23) {
                if (Settings.System.canWrite(getContext())) {
                    Dexter.withContext(getContext()).withPermissions("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                            if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                RingtoneFragment.this.SetAsRingtone();
                            }
                            if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                                SettingDialog.showSettingDialog(RingtoneFragment.this.getActivity());
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).withErrorListener(dexterError -> RingtoneFragment.this.lambda$ringtoneMethod$5$RingtoneFragment()).onSameThread().check();
                    return;
                }
                Intent intent = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS");
                intent.setData(Uri.parse("package:" + getContext().getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              startActivityForResult(intent,0);
            } else {
                SetAsRingtone();
            }
        } else if (Build.VERSION.SDK_INT >= 23 && audioManager.getRingerMode() != 0 && notificationManager.getCurrentInterruptionFilter() != NotificationManager.INTERRUPTION_FILTER_PRIORITY) {
            if (Settings.System.canWrite(getContext())) {

                Dexter.withContext(getActivity()).withPermissions("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            RingtoneFragment.this.SetAsRingtone();
                        } else if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            SettingDialog.showSettingDialog(RingtoneFragment.this.getActivity());
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(dexterError -> RingtoneFragment.this.lambda$ringtoneMethod$6$RingtoneFragment()).onSameThread().check();
                return;
            }
            Intent intent2 = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS");
            intent2.setData(Uri.parse("package:" + getContext().getPackageName()));
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(intent2,0);
        } else {
            SetAsRingtone();
        }
    }

    public void lambda$ringtoneMethod$5$RingtoneFragment() {
        Toast.makeText(getActivity(), "Error occurred! ", Toast.LENGTH_SHORT).show();
    }

    public void lambda$ringtoneMethod$6$RingtoneFragment() {
        Toast.makeText(getActivity(), "Error occurred! ", Toast.LENGTH_SHORT).show();
    }

    public void notificationMethod() {
        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        if ((Build.VERSION.SDK_INT >= 23 && audioManager.getRingerMode() == 0) || (Build.VERSION.SDK_INT >= 23 && notificationManager.getCurrentInterruptionFilter() == NotificationManager.INTERRUPTION_FILTER_PRIORITY)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && !notificationManager.isNotificationPolicyAccessGranted()) {
                try {
                    Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                    startActivity(intent);
                }catch (Exception et){
                    Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }else if (Build.VERSION.SDK_INT >= 23) {
                if (Settings.System.canWrite(getActivity())) {

                    Dexter.withContext(getActivity()).withPermissions("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                            if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                RingtoneFragment.this.setMesageTOne();
                            } else if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                                SettingDialog.showSettingDialog(RingtoneFragment.this.getActivity());
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).withErrorListener(dexterError -> RingtoneFragment.this.lambda$notificationMethod$7$RingtoneFragment()).onSameThread().check();
                    return;
                }
                Intent intent = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS");
                intent.setData(Uri.parse("package:" + getContext().getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent,0);
            } else {
                setMesageTOne();
            }
        } else if (Build.VERSION.SDK_INT >= 23 && audioManager.getRingerMode() != 0 && notificationManager.getCurrentInterruptionFilter() != NotificationManager.INTERRUPTION_FILTER_PRIORITY) {
            if (Settings.System.canWrite(getActivity())) {
                Dexter.withContext(getActivity()).withPermissions("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            RingtoneFragment.this.setMesageTOne();
                        } else if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            SettingDialog.showSettingDialog(RingtoneFragment.this.getActivity());
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(dexterError -> RingtoneFragment.this.lambda$notificationMethod$8$RingtoneFragment()).onSameThread().check();
                return;
            }
            Intent intent2 = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS");
            intent2.setData(Uri.parse("package:" + getContext().getPackageName()));
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(intent2,0);
        } else {
            setMesageTOne();
        }
    }

    public void lambda$notificationMethod$7$RingtoneFragment() {
        Toast.makeText(getActivity(), "Error occurred! ", Toast.LENGTH_SHORT).show();
    }

    public void lambda$notificationMethod$8$RingtoneFragment() {
        Toast.makeText(getContext().getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
    }

    public void alarmMethod() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.System.canWrite(getActivity())) {
                Dexter.withContext(getActivity()).withPermissions("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            RingtoneFragment.this.setMyAlarm();
                        } else if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            SettingDialog.showSettingDialog(RingtoneFragment.this.getActivity());
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(dexterError -> RingtoneFragment.this.lambda$alarmMethod$9$RingtoneFragment()).onSameThread().check();
                return;
            }
            Intent intent = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS");
            intent.setData(Uri.parse("package:" + getContext().getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         startActivityForResult(intent,0);
            return;
        }
        setMyAlarm();
    }

    public void lambda$alarmMethod$9$RingtoneFragment() {
        Toast.makeText(getContext().getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
    }

    public void contactMethod() {
        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        if ((Build.VERSION.SDK_INT >= 23 && audioManager.getRingerMode() == 0) || (Build.VERSION.SDK_INT >= 23 && notificationManager.getCurrentInterruptionFilter() == NotificationManager.INTERRUPTION_FILTER_PRIORITY)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && !notificationManager.isNotificationPolicyAccessGranted()) {
                try {
                    Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                    startActivity(intent);
                }catch (Exception et){
                    Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            } else if (Build.VERSION.SDK_INT >= 23) {
                Dexter.withContext(getActivity()).withPermissions("android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            RingtoneFragment.this.contactRingtone();
                        } else if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            SettingDialog.showSettingDialog(RingtoneFragment.this.getActivity());
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(dexterError -> RingtoneFragment.this.lambda$contactMethod$10$RingtoneFragment()).onSameThread().check();
            } else {
                contactRingtone();
            }
        } else if (Build.VERSION.SDK_INT >= 23 && audioManager.getRingerMode() != 0 && notificationManager.getCurrentInterruptionFilter() != NotificationManager.INTERRUPTION_FILTER_PRIORITY) {
            Dexter.withContext(getActivity()).withPermissions("android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        RingtoneFragment.this.contactRingtone();
                    } else if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                        SettingDialog.showSettingDialog(RingtoneFragment.this.getActivity());
                    }
                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).withErrorListener(dexterError -> RingtoneFragment.this.lambda$contactMethod$11$RingtoneFragment(dexterError)).onSameThread().check();
        } else {
            contactRingtone();
        }
    }

    public void lambda$contactMethod$10$RingtoneFragment() {
        Toast.makeText(getActivity(), "Error occurred! ", Toast.LENGTH_SHORT).show();
    }

    public void lambda$contactMethod$11$RingtoneFragment(DexterError dexterError) {
        Toast.makeText(getActivity(), "Error occurred! ", Toast.LENGTH_SHORT).show();
    }

    public void contactRingtone() {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("vnd.android.cursor.dir/contact");
       startActivityForResult(intent,0);
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        AssetFileDescriptor assetFileDescriptor;
        Cursor query;
        AssetFileDescriptor assetFileDescriptor2;
        super.onActivityResult(i, i2, intent);
        if (i == 1001 && i2 == -1 && intent.getData() != null) {
            String str = this.rintoneTitle + "_" + System.currentTimeMillis();
            if (Build.VERSION.SDK_INT >= 29) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("_display_name", str + ".mp3");
                contentValues.put("mime_type", "audio/mp3");
                contentValues.put("is_ringtone", (Boolean) true);
                contentValues.put("relative_path", Environment.DIRECTORY_ALARMS + "/" + this.folderName);
                Uri parse = Uri.parse("android.resource://" + getActivity().getPackageName() + "/raw/" + this.song);
                new File(parse.getPath());
                Uri insert = getActivity().getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues);
                Log.v(this.TAG, "URI" + insert);
                try {
                    assetFileDescriptor2 = getActivity().getContentResolver().openAssetFileDescriptor(parse, "r");
                } catch (FileNotFoundException e) {
                    Log.v(this.TAG, "File Not Found Exception" + e.getMessage());
                    assetFileDescriptor2 = null;
                }
                try {
                    OutputStream openOutputStream = getActivity().getContentResolver().openOutputStream(insert);
                    FileInputStream createInputStream = assetFileDescriptor2.createInputStream();
                    byte[] bArr = new byte[1024];
                    while (createInputStream.read(bArr) != -1) {
                        try {
                            openOutputStream.write(bArr);
                        } catch (IOException e2) {
                            Log.v(this.TAG, "IOE " + e2.getMessage());
                        }
                    }
                    openOutputStream.close();
                    openOutputStream.flush();
                    if (openOutputStream != null) {
                        openOutputStream.close();
                    }
                } catch (Exception e3) {
                    Log.v(this.TAG, "Exception " + e3.getMessage());
                }
                query = getActivity().getContentResolver().query(intent.getData(), new String[]{"_id", "lookup", "display_name"}, null, null, null);
                query.moveToFirst();
                try {
                    long j = query.getLong(0);
                    String string = query.getString(1);
                    String string2 = query.getString(2);
                    Uri lookupUri = ContactsContract.Contacts.getLookupUri(j, string);
                    Uri withAppendedPath = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, query.getString(query.getColumnIndexOrThrow("_id")));
                    if (lookupUri == null) {
                        return;
                    }
                    ContentValues contentValues2 = new ContentValues();
                    contentValues2.put("raw_contact_id", Long.valueOf(j));
                    contentValues2.put("custom_ringtone", insert.toString());
                    try {
                        getActivity().getContentResolver().update(withAppendedPath, contentValues2, null, null);
                        Toast.makeText(getContext(), "Ringtone " + this.rintoneTitle + " assigned to " + string2, Toast.LENGTH_LONG).show();
                    } catch (NullPointerException e4) {
                        Log.v("contact_exception", e4.toString());
                    }
                    return;
                } finally {
                }
            }
            ((AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE)).setRingerMode(2);
            File file = new File(Environment.getExternalStorageDirectory() + "/" + this.folderName, str + ".mp3");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e5) {
                    e5.printStackTrace();
                }
            }
            try {
                assetFileDescriptor = getActivity().getContentResolver().openAssetFileDescriptor(Uri.parse("android.resource://" + getActivity().getPackageName() + "/raw/" + this.song), "r");
            } catch (FileNotFoundException unused) {
                assetFileDescriptor = null;
            }
            try {
                byte[] bArr2 = new byte[1024];
                FileInputStream createInputStream2 = assetFileDescriptor.createInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                for (int read = createInputStream2.read(bArr2); read != -1; read = createInputStream2.read(bArr2)) {
                    fileOutputStream.write(bArr2, 0, read);
                }
                fileOutputStream.close();
            } catch (IOException e6) {
                e6.printStackTrace();
            }
            ContentValues contentValues3 = new ContentValues();
            contentValues3.put("_data", file.getAbsolutePath());
            contentValues3.put("title", this.rintoneTitle);
            contentValues3.put("_size", Long.valueOf(file.length()));
            contentValues3.put("mime_type", "audio/mp3");
            contentValues3.put("artist", getString(R.string.app_name));
            contentValues3.put("is_alarm", (Boolean) false);
            contentValues3.put("is_music", (Boolean) false);
            contentValues3.put("is_notification", (Boolean) false);
            contentValues3.put("is_ringtone", (Boolean) true);
            Uri contentUriForPath = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());
            getActivity().getContentResolver().delete(contentUriForPath, "_data=\"" + file.getAbsolutePath() + "\"", null);
            Uri insert2 = getActivity().getContentResolver().insert(contentUriForPath, contentValues3);
            query = getActivity().getContentResolver().query(intent.getData(), new String[]{"_id", "lookup", "display_name"}, null, null, null);
            query.moveToFirst();
            try {
                long j2 = query.getLong(0);
                String string3 = query.getString(1);
                String string4 = query.getString(2);
                Uri lookupUri2 = ContactsContract.Contacts.getLookupUri(j2, string3);
                Uri withAppendedPath2 = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, query.getString(query.getColumnIndexOrThrow("_id")));
                if (lookupUri2 == null) {
                    return;
                }
                ContentValues contentValues4 = new ContentValues();
                contentValues4.put("raw_contact_id", Long.valueOf(j2));
                contentValues4.put("custom_ringtone", insert2.toString());
                try {
                    getActivity().getContentResolver().update(withAppendedPath2, contentValues4, null, null);
                    Toast.makeText(getContext(), "Ringtone assigned to : " + string4, Toast.LENGTH_LONG).show();
                } catch (NullPointerException e7) {
                    Log.e("contact_exception", e7.toString());
                }
            } finally {
            }
        }
    }


    public void setMesageTOne() {
        AssetFileDescriptor assetFileDescriptor;
        AssetFileDescriptor assetFileDescriptor2;
        String str = this.rintoneTitle + "_" + System.currentTimeMillis();
        if (Build.VERSION.SDK_INT >= 29) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("_display_name", str + ".mp3");
            contentValues.put("mime_type", "audio/mp3");
            contentValues.put("is_notification", (Boolean) true);
            contentValues.put("relative_path", Environment.DIRECTORY_RINGTONES + "/" + this.folderName);
            Uri parse = Uri.parse("android.resource://" + getActivity().getPackageName() + "/raw/" + this.song);
            new File(parse.getPath());
            Uri insert = getActivity().getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues);
            Log.v(this.TAG, "URI" + insert);
            try {
                assetFileDescriptor2 = getActivity().getContentResolver().openAssetFileDescriptor(parse, "r");
            } catch (FileNotFoundException e) {
                Log.v(this.TAG, "File Not Found Exception" + e.getMessage());
                assetFileDescriptor2 = null;
            }
            try {
                OutputStream openOutputStream = getActivity().getContentResolver().openOutputStream(insert);
                FileInputStream createInputStream = assetFileDescriptor2.createInputStream();
                byte[] bArr = new byte[1024];
                while (createInputStream.read(bArr) != -1) {
                    try {
                        openOutputStream.write(bArr);
                    } catch (IOException e2) {
                        Log.v(this.TAG, "IOE" + e2.getMessage());
                    }
                }
                openOutputStream.close();
                openOutputStream.flush();
                if (openOutputStream != null) {
                    openOutputStream.close();
                }
            } catch (Exception e3) {
                Log.v(this.TAG, "open Asset File Descriptor " + e3.getMessage());
            }
            try {
                RingtoneManager.setActualDefaultRingtoneUri(getContext(), 2, insert);
                Context context = getContext();
                StringBuilder sb = new StringBuilder();
                sb.append("Notification tone set :" + this.rintoneTitle);
                Toast.makeText(context, sb, Toast.LENGTH_LONG).show();
                return;
            } catch (Exception unused) {
                Context context2 = getContext();
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Feature is not working on your phone !!");
                Toast.makeText(context2, sb2, Toast.LENGTH_LONG).show();
                return;
            }
        }
        ((AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE)).setRingerMode(2);
        File file = new File(Environment.getExternalStorageDirectory() + "/" + this.folderName, str + ".mp3");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
        }
        Uri parse2 = Uri.parse("android.resource://" + getActivity().getPackageName() + "/raw/" + this.song);
        ContentResolver contentResolver = getActivity().getContentResolver();
        try {
            assetFileDescriptor = contentResolver.openAssetFileDescriptor(parse2, "r");
        } catch (FileNotFoundException unused2) {
            assetFileDescriptor = null;
        }
        try {
            byte[] bArr2 = new byte[1024];
            FileInputStream createInputStream2 = assetFileDescriptor.createInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            while (true) {
                int read = createInputStream2.read(bArr2);
                if (read == -1) {
                    break;
                }
                fileOutputStream.write(bArr2, 0, read);
            }
            fileOutputStream.close();
        } catch (IOException e5) {
            e5.printStackTrace();
        }
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("_data", file.getAbsolutePath());
        contentValues2.put("title", this.rintoneTitle);
        contentValues2.put("mime_type", "audio/mp3");
        contentValues2.put("_size", Long.valueOf(file.length()));
        contentValues2.put("artist", Integer.valueOf((int) R.string.app_name));
        contentValues2.put("is_notification", true);
        contentValues2.put("is_music", false);
        try {
            Context context3 = getContext();
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Notification set successfully !");
            Toast.makeText(context3, sb3, Toast.LENGTH_LONG).show();
            RingtoneManager.setActualDefaultRingtoneUri(getContext(), 2, contentResolver.insert(MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath()), contentValues2));
        } catch (Throwable unused3) {
            Context context4 = getContext();
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Feature is not working");
            Toast.makeText(context4, sb4, Toast.LENGTH_LONG).show();
        }
    }

    public void SetAsRingtone() {
        AssetFileDescriptor assetFileDescriptor;
        AssetFileDescriptor assetFileDescriptor2;
        String str = this.rintoneTitle + "_" + System.currentTimeMillis();
        if (Build.VERSION.SDK_INT >= 29) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("_display_name", str + ".mp3");
            contentValues.put("mime_type", "audio/mp3");
            contentValues.put("is_ringtone", (Boolean) true);
            contentValues.put("relative_path", Environment.DIRECTORY_RINGTONES + "/" + this.folderName);
            Uri parse = Uri.parse("android.resource://" + getActivity().getPackageName() + "/raw/" + this.song);
            new File(parse.getPath());
            Uri insert = getActivity().getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues);
            Log.v(this.TAG, "URI" + insert);
            try {
                assetFileDescriptor2 = getActivity().getContentResolver().openAssetFileDescriptor(parse, "r");
            } catch (FileNotFoundException e) {
                Log.v(this.TAG, "open Asset File Descriptor" + e.getMessage());
                assetFileDescriptor2 = null;
            }
            try {
                OutputStream openOutputStream = getActivity().getContentResolver().openOutputStream(insert);
                FileInputStream createInputStream = assetFileDescriptor2.createInputStream();
                byte[] bArr = new byte[1024];
                while (createInputStream.read(bArr) != -1) {
                    try {
                        openOutputStream.write(bArr);
                    } catch (IOException e2) {
                        Log.v(this.TAG, "IOE" + e2.getMessage());
                    }
                }
                openOutputStream.close();
                openOutputStream.flush();
                if (openOutputStream != null) {
                    openOutputStream.close();
                }
            } catch (Exception e3) {
                Log.v(this.TAG, "Ex " + e3.getMessage());
            }
            try {
                RingtoneManager.setActualDefaultRingtoneUri(getContext(), 1, insert);
                Toast.makeText(getContext(), "Ringtone set : " + this.rintoneTitle, Toast.LENGTH_LONG).show();
                return;
            } catch (Exception unused) {
                Context context = getContext();
                StringBuilder sb = new StringBuilder();
                sb.append("Feature is not working on your phone !!");
                Toast.makeText(context, sb, Toast.LENGTH_LONG).show();
                return;
            }
        }
        ((AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE)).setRingerMode(2);
        File file = new File(Environment.getExternalStorageDirectory() + "/" + this.folderName, str + ".mp3");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
        }
        Uri parse2 = Uri.parse("android.resource://" + getActivity().getPackageName() + "/raw/" + this.song);
        ContentResolver contentResolver = getActivity().getContentResolver();
        try {
            assetFileDescriptor = contentResolver.openAssetFileDescriptor(parse2, "r");
        } catch (FileNotFoundException unused2) {
            assetFileDescriptor = null;
        }
        try {
            byte[] bArr2 = new byte[1024];
            FileInputStream createInputStream2 = assetFileDescriptor.createInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            while (true) {
                int read = createInputStream2.read(bArr2);
                if (read == -1) {
                    break;
                }
                fileOutputStream.write(bArr2, 0, read);
            }
            fileOutputStream.close();
        } catch (IOException e5) {
            e5.printStackTrace();
        }
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("_data", file.getAbsolutePath());
        contentValues2.put("title", this.rintoneTitle);
        contentValues2.put("mime_type", "audio/mp3");
        contentValues2.put("_size", Long.valueOf(file.length()));
        contentValues2.put("artist", Integer.valueOf((int) R.string.app_name));
        contentValues2.put("is_ringtone", (Boolean) true);
        contentValues2.put("is_notification", (Boolean) false);
        contentValues2.put("is_alarm", (Boolean) false);
        contentValues2.put("is_music", (Boolean) false);
        System.out.println("file.getAbsolutePath() " + file.getAbsolutePath());
        try {
            Context context2 = getContext();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Ringtone set successfully !");
            Toast.makeText(context2, sb2, Toast.LENGTH_LONG).show();
            RingtoneManager.setActualDefaultRingtoneUri(getContext(), 1, contentResolver.insert(MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath()), contentValues2));
        } catch (Throwable unused3) {
            Context context3 = getContext();
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Feature is not working");
            Toast.makeText(context3, sb3, Toast.LENGTH_LONG).show();
        }
    }


    public void setMyAlarm() {
        AssetFileDescriptor assetFileDescriptor;
        AssetFileDescriptor assetFileDescriptor2;
        if (Build.VERSION.SDK_INT >= 29) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("_display_name", (this.rintoneTitle + "_" + System.currentTimeMillis()) + ".mp3");
            contentValues.put("mime_type", "audio/mp3");
            contentValues.put("is_alarm", (Boolean) true);
            contentValues.put("relative_path", Environment.DIRECTORY_ALARMS + "/" + this.folderName);
            Uri parse = Uri.parse("android.resource://" + getActivity().getPackageName() + "/raw/" + this.song);
            new File(parse.getPath());
            Uri insert = getActivity().getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues);
            Log.v(this.TAG, "Uri" + insert);
            ContentResolver contentResolver = getActivity().getContentResolver();
            try {
                assetFileDescriptor2 = contentResolver.openAssetFileDescriptor(parse, "r");
            } catch (FileNotFoundException e) {
                Log.v(this.TAG, "File Not Found Exception" + e.getMessage());
                assetFileDescriptor2 = null;
            }
            try {
                OutputStream openOutputStream = getActivity().getContentResolver().openOutputStream(insert);
                FileInputStream createInputStream = assetFileDescriptor2.createInputStream();
                byte[] bArr = new byte[1024];
                while (createInputStream.read(bArr) != -1) {
                    try {
                        openOutputStream.write(bArr);
                    } catch (IOException e2) {
                        Log.v(this.TAG, "open Asset File Descriptor 2" + e2.getMessage());
                    }
                }
                openOutputStream.close();
                openOutputStream.flush();
                if (openOutputStream != null) {
                    openOutputStream.close();
                }
            } catch (Exception e3) {
                Log.v(this.TAG, "open Asset File Descriptor 3" + e3.getMessage());
            }
            try {
                RingtoneManager.setActualDefaultRingtoneUri(getContext(), 4, insert);
                Settings.System.putString(contentResolver, "alarm_alert", insert.toString());
                Toast.makeText(getContext(), "Alarm Set : " + this.rintoneTitle, Toast.LENGTH_SHORT).show();
                return;
            } catch (Exception unused) {
                Context context = getContext();
                StringBuilder sb = new StringBuilder();
                sb.append("Feature is not working on your phone !!");
                Toast.makeText(context, sb, Toast.LENGTH_LONG).show();
                return;
            }
        }
        File file = new File(Environment.getExternalStorageDirectory(), this.folderName);
        if (!file.exists()) {
            file.mkdirs();
        }
        String str = (Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + this.folderName) + "/";
        File file2 = new File(str, (this.rintoneTitle + System.currentTimeMillis()) + ".mp3");
        Uri parse2 = Uri.parse("android.resource://" + getActivity().getPackageName() + "/raw/" + this.song);
        PrintStream printStream = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("mUri ");
        sb2.append(parse2);
        printStream.println(sb2);
        ContentResolver contentResolver2 = getActivity().getContentResolver();
        try {
            assetFileDescriptor = contentResolver2.openAssetFileDescriptor(parse2, "r");
        } catch (FileNotFoundException unused2) {
            assetFileDescriptor = null;
        }
        try {
            byte[] bArr2 = new byte[1024];
            FileInputStream createInputStream2 = assetFileDescriptor.createInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            for (int read = createInputStream2.read(bArr2); read != -1; read = createInputStream2.read(bArr2)) {
                fileOutputStream.write(bArr2, 0, read);
            }
            fileOutputStream.close();
        } catch (IOException unused3) {
        }
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("_data", file2.getAbsolutePath());
        contentValues2.put("title", this.rintoneTitle);
        contentValues2.put("mime_type", "audio/mp3");
        contentValues2.put("_size", Long.valueOf(file2.length()));
        contentValues2.put("artist", Integer.valueOf((int) R.string.app_name));
        contentValues2.put("is_alarm", (Boolean) true);
        Uri contentUriForPath = MediaStore.Audio.Media.getContentUriForPath(file2.getAbsolutePath());
        getActivity().getContentResolver().delete(contentUriForPath, "_data=\"" + file2.getAbsolutePath() + "\"", null);
        Uri insert2 = contentResolver2.insert(contentUriForPath, contentValues2);
        try {
            RingtoneManager.setActualDefaultRingtoneUri(getContext(), 4, insert2);
            Settings.System.putString(contentResolver2, "alarm_alert", insert2.toString());
            Toast.makeText(getContext(), "Alarm set successfully !", Toast.LENGTH_SHORT).show();
        } catch (Throwable unused4) {
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
            return;
        }
        this.mediaPlayer.pause();
    }
}
