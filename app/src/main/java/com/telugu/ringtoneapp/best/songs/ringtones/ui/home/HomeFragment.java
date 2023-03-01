package com.telugu.ringtoneapp.best.songs.ringtones.ui.home;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.karumi.dexter.BuildConfig;
import com.telugu.ringtoneapp.best.songs.ringtones.Adconfig;
import com.telugu.ringtoneapp.best.songs.ringtones.R;
import com.telugu.ringtoneapp.best.songs.ringtones.ui.list.RingtoneListFragment;


public class HomeFragment extends Fragment {
    private String privacyUrl = "https://teluguringtone99.blogspot.com/2023/02/privacy-policy-celular-ringtone-built.html";
    private AdView adViewfacebook;
    private InterstitialAd interstitialAd;

    boolean isloaded = false;
    boolean noloaded = false;
    ProgressDialog progressDialog;

    ImageButton share, rate, privacy;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        setHasOptionsMenu(true);
        View inflate = layoutInflater.inflate(R.layout.fragment_home, viewGroup, false);
        inflate.setClickable(true);
        inflate.setFocusable(true);
        adViewfacebook = new com.facebook.ads.AdView(getContext(), Adconfig.BANNER, AdSize.BANNER_HEIGHT_50);
        interstitialAd = new InterstitialAd(getContext(), Adconfig.INTERSTITIAL);

        LinearLayout adContainer = (LinearLayout) inflate.findViewById(R.id.banner_container);
        adContainer.addView(adViewfacebook);
        adViewfacebook.loadAd();

        ShowFbInter();

        share=inflate.findViewById(R.id.share);
        rate=inflate.findViewById(R.id.idrate);
        privacy=inflate.findViewById(R.id.privacy);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    Log.e("fail","Fail to share");
                }
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id=" + getContext().getPackageName());
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getContext(), " unable to find market app", Toast.LENGTH_LONG).show();
                }
            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(privacyUrl));
                startActivity(i);
            }
        });




        ((ImageButton) inflate.findViewById(R.id.nextButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View view) {
                ShowFbInter();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homeFragment, new RingtoneListFragment()).addToBackStack(null).commitAllowingStateLoss();
            }
        });
        return inflate;
    }



    public void ShowFbInter(){
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
    }






    @Override 
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        TextView textView = (TextView) getActivity().findViewById(R.id.titletext);
        textView.setText(getString(R.string.app_name));
        textView.setTextColor(getResources().getColor(R.color.white));
    }


}
