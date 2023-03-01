package com.telugu.ringtoneapp.best.songs.ringtones.ui.list;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.telugu.ringtoneapp.best.songs.ringtones.R;
import com.telugu.ringtoneapp.best.songs.ringtones.ui.ringtone.RingtoneFragment;
import java.util.ArrayList;

public class RingtoneListFragment extends Fragment {
    private ListCardAdapter listCardAdapter;
    private ArrayList<ListCardModel> listCardModelArrayList;
    private MediaPlayer mediaPlayer;
    private int mediaPlayerIndex = 0;
    private RecyclerView recyclerView;
    private ArrayList<Integer> ringtoneList;
    private ArrayList<String> ringtoneNameList;
    private InterstitialAd mInterstitialAd;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_ringtonelist, viewGroup, false);
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(getContext(),getString(R.string.Interstitial), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }
                });
        inflate.setClickable(true);
        inflate.setFocusable(true);
        myRecyclerView(inflate);
        return inflate;



    }

    private void myRecyclerView(View view) {
        this.mediaPlayer = new MediaPlayer();
        addValueToNameList();
        addRingtoneToLIst();
        this.listCardModelArrayList = new ArrayList<>();
        addModelToList();
        this.recyclerView = view.findViewById(R.id.idListRV);
        this.listCardAdapter = new ListCardAdapter(this.listCardModelArrayList);
        this.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        this.recyclerView.setAdapter(this.listCardAdapter);
        this.listCardAdapter.setWhenPlayClickListener(RingtoneListFragment.this::onClickRegtoneItem);
        this.listCardAdapter.setWhenClickListener(listCardModel -> fragmentTransferMethod(listCardModel.getPosition()));
    }

    public void onClickRegtoneItem(ListCardModel listCardModel) {
        playMedia(listCardModel.getPosition());
        this.listCardAdapter.notifyDataSetChanged();
    }


    public void fragmentTransferMethod(int i) {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(getActivity());
        }

        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            this.mediaPlayer.stop();
            this.listCardModelArrayList.get(i).setPlayingState(false);
            this.listCardAdapter.notifyDataSetChanged();
        }
        RingtoneFragment ringtoneFragment = new RingtoneFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", i);
        bundle.putString("name", this.ringtoneNameList.get(i));
        bundle.putInt("song", this.ringtoneList.get(i));
        ringtoneFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ListFragment, ringtoneFragment).addToBackStack("tag").commit();
    }

    private void addValueToNameList() {
        ArrayList<String> arrayList = new ArrayList<>();
        this.ringtoneNameList = arrayList;
        arrayList.add(getString(R.string.r1));
        this.ringtoneNameList.add(getString(R.string.r2));
        this.ringtoneNameList.add(getString(R.string.r3));
        this.ringtoneNameList.add(getString(R.string.r4));
        this.ringtoneNameList.add(getString(R.string.r5));
        this.ringtoneNameList.add(getString(R.string.r6));
        this.ringtoneNameList.add(getString(R.string.r7));
        this.ringtoneNameList.add(getString(R.string.r8));
        this.ringtoneNameList.add(getString(R.string.r9));
        this.ringtoneNameList.add(getString(R.string.r10));
        this.ringtoneNameList.add(getString(R.string.r11));
        this.ringtoneNameList.add(getString(R.string.r12));
        this.ringtoneNameList.add(getString(R.string.r13));
        this.ringtoneNameList.add(getString(R.string.r14));
        this.ringtoneNameList.add(getString(R.string.r15));
        this.ringtoneNameList.add(getString(R.string.r16));
        this.ringtoneNameList.add(getString(R.string.r17));
        this.ringtoneNameList.add(getString(R.string.r18));
        this.ringtoneNameList.add(getString(R.string.r19));
        this.ringtoneNameList.add(getString(R.string.r20));
        this.ringtoneNameList.add(getString(R.string.r21));
        this.ringtoneNameList.add(getString(R.string.r22));
        this.ringtoneNameList.add(getString(R.string.r23));
        this.ringtoneNameList.add(getString(R.string.r24));
        this.ringtoneNameList.add(getString(R.string.r25));
        this.ringtoneNameList.add(getString(R.string.r26));
        this.ringtoneNameList.add(getString(R.string.r27));



    }

    private void addRingtoneToLIst() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        this.ringtoneList = arrayList;
        arrayList.add(R.raw.r1);
        this.ringtoneList.add(R.raw.r2);
        this.ringtoneList.add(R.raw.r3);
        this.ringtoneList.add(R.raw.r4);
        this.ringtoneList.add(R.raw.r5);
        this.ringtoneList.add(R.raw.r6);
        this.ringtoneList.add(R.raw.r7);
        this.ringtoneList.add(R.raw.r8);
        this.ringtoneList.add(R.raw.r9);
        this.ringtoneList.add(R.raw.r10);
        this.ringtoneList.add(R.raw.r11);
        this.ringtoneList.add(R.raw.r12);
        this.ringtoneList.add(R.raw.r13);
        this.ringtoneList.add(R.raw.r14);
        this.ringtoneList.add(R.raw.r15);
        this.ringtoneList.add(R.raw.r16);
        this.ringtoneList.add(R.raw.r17);
        this.ringtoneList.add(R.raw.r18);
        this.ringtoneList.add(R.raw.r19);
        this.ringtoneList.add(R.raw.r20);
        this.ringtoneList.add(R.raw.r21);
        this.ringtoneList.add(R.raw.r22);
        this.ringtoneList.add(R.raw.r23);
        this.ringtoneList.add(R.raw.r24);
        this.ringtoneList.add(R.raw.r25);
        this.ringtoneList.add(R.raw.r26);
        this.ringtoneList.add(R.raw.r27);


    }

    private void addModelToList() {
        for (int i = 0; i < this.ringtoneNameList.size(); i++) {
            this.listCardModelArrayList.add(new ListCardModel(i, this.ringtoneNameList.get(i), false));
        }
    }

    private void playMedia(int i) {
        for (int i2 = 0; i2 < this.listCardModelArrayList.size(); i2++) {
            if (i2 != i) {
                this.listCardModelArrayList.get(i2).setPlayingState(false);
            }
        }

        if (this.mediaPlayerIndex != i && this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.stop();
            this.mediaPlayer.release();

            MediaPlayer create = MediaPlayer.create(getContext(), this.ringtoneList.get(i));
            this.mediaPlayer = create;
            create.start();
            this.mediaPlayerIndex = i;
        } else if (this.mediaPlayerIndex == i && this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.stop();
        } else if (this.mediaPlayerIndex == i && !this.mediaPlayer.isPlaying()) {
            MediaPlayer create2 = MediaPlayer.create(getContext(), this.ringtoneList.get(i));
            this.mediaPlayer = create2;
            create2.start();
        } else {
            MediaPlayer create3 = MediaPlayer.create(getContext(), this.ringtoneList.get(i));
            this.mediaPlayer = create3;
            create3.start();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        TextView textView = getActivity().findViewById(R.id.titletext);
        textView.setText("Ringtone List");
        textView.setTextColor(getResources().getColor(R.color.white));
    }

    @Override 
    public void onResume() {
        super.onResume();

        if (this.listCardModelArrayList != null) {
            for (int i = 0; i < this.listCardModelArrayList.size(); i++) {
                this.listCardModelArrayList.get(i).setPlayingState(false);
                this.listCardAdapter.notifyDataSetChanged();
            }
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
        this.listCardModelArrayList.get(this.mediaPlayerIndex).setPlayingState(false);
        this.listCardAdapter.notifyDataSetChanged();
    }
}
