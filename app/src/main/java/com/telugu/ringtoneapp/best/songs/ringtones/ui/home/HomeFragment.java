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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.karumi.dexter.BuildConfig;
import com.telugu.ringtoneapp.best.songs.ringtones.R;
import com.telugu.ringtoneapp.best.songs.ringtones.ui.list.RingtoneListFragment;


public class HomeFragment extends Fragment {
    private String privacyUrl = "https://teluguringtone99.blogspot.com/2023/02/privacy-policy-celular-ringtone-built.html";

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
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
        mAdView = inflate.findViewById(R.id.adView);
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        loadads();
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




        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        ((ImageButton) inflate.findViewById(R.id.nextButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homeFragment, new RingtoneListFragment()).addToBackStack(null).commitAllowingStateLoss();
                showIntAds();
            }
        });
        return inflate;
    }




    public void loadads() {

        isloaded =false;
        noloaded =false;

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        InterstitialAd.load(getContext(), getString(R.string.Interstitial), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        isloaded = true;
                        if (noloaded){
                            progressDialog.dismiss();
                            showIntAds();
                        }
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }
                });
    }


    public void showIntAds(){
        if (mInterstitialAd != null){
            mInterstitialAd.show(getActivity());
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    mInterstitialAd = null;
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homeFragment, new RingtoneListFragment()).addToBackStack(null).commitAllowingStateLoss();

                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    mInterstitialAd = null;
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homeFragment, new RingtoneListFragment()).addToBackStack(null).commitAllowingStateLoss();

                }
            });
        }
    };



    @Override 
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        TextView textView = (TextView) getActivity().findViewById(R.id.titletext);
        textView.setText(getString(R.string.app_name));
        textView.setTextColor(getResources().getColor(R.color.white));
    }


}
