package com.buildbox;

import java.lang.ref.WeakReference;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.mousagroup.hyperjump.ResData;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import org.cocos2dx.lib.Cocos2dxActivity;

public class AdIntegrator {
    public static native boolean rewardedVideoDidEnd();

    private static AdView banner;
    private static InterstitialAd interstitial;
    private static RewardedAd rewardedVideo;

    private static WeakReference<Cocos2dxActivity> activity;

    public static void initBridge(Cocos2dxActivity act){

        activity = new WeakReference<>(act);
    }

    public static void initAds(){
        activity.get().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FrameLayout frameLayout = (FrameLayout)activity.get().findViewById(android.R.id.content);
                RelativeLayout layout = new RelativeLayout(activity.get() );
                frameLayout.addView( layout );

                RelativeLayout.LayoutParams adViewParams = new RelativeLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT);
                adViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                adViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                adViewParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

                banner = new AdView(activity.get());
                String ADMOB_BANNER_ID = ResData.getAdmobBannerId();
                Log.d("ADMOB_BANNER_ID", ADMOB_BANNER_ID);
                banner.setAdUnitId(ADMOB_BANNER_ID);
                banner.setAdSize(AdSize.BANNER);

                banner.setLayoutParams(adViewParams);
                layout.addView(banner);
                banner.setVisibility(View.INVISIBLE);

                AdRequest adRequest = new AdRequest.Builder().build();
                banner.loadAd(adRequest);

                initInterstitial();

                initRewardVideo();

                /*
                if(interstitial != null){
                    interstitial = null;

                }

                if(rewardedVideo != null){
                    rewardedVideo = null;

                }
                */

            }
        });
    }

    public static void initInterstitial(){

        AdRequest adRequest = new AdRequest.Builder().build();

        String ADMOB_INTERSTITIAL_ID = ResData.getAdmobInterstitialId();
        Log.d("ADMOB_INTERSTITIAL_ID", ADMOB_INTERSTITIAL_ID);

        InterstitialAd.load(activity.get(), ADMOB_INTERSTITIAL_ID, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        interstitial = interstitialAd;
                        interstitial.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                initInterstitial();
                                Log.d("TAG", "The ad was dismissed.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                Log.d("TAG", "The ad failed to show." +adError.getMessage());
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                interstitial = null;
                                Log.d("TAG", "The ad was shown.");
                            }
                        });
                        Log.i("INTERSTITIAL", "onAdLoaded" +interstitialAd.getResponseInfo());
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i("INTERSTITIAL_ERROR", loadAdError.getMessage());
                        interstitial = null;
                    }


                });


    }

    public static void initRewardVideo(){

        AdRequest adRequest = new AdRequest.Builder().build();
        String ADMOB_REWARD_ID = ResData.getAdmobRewardId();
        Log.d("ADMOB_INTERSTITIAL_ID", ADMOB_REWARD_ID);

        RewardedAd.load(activity.get(), ADMOB_REWARD_ID,
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d("REWARD_AD", loadAdError.getMessage());
                        rewardedVideo = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        rewardedVideo = rewardedAd;
                        rewardedVideo.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d("REWARD_AD", "Ad was shown.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                Log.d("REWARD_AD", "Ad failed to show.");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                initRewardVideo();
                                // Set the ad reference to null so you don't show the ad a second time.
                                Log.d("REWARD_AD", "Ad was dismissed.");
                                rewardedVideo = null;
                            }
                        });

                        Log.d("REWARD_AD", "Ad was loaded.");
                    }
                });


    }



    public static void showBanner(){

        activity.get().runOnUiThread( new Runnable() {
            public void run() {
                banner.setVisibility(View.VISIBLE);
            }
        });

    }

    public static void hideBanner(){

        activity.get().runOnUiThread( new Runnable() {
            public void run() {
                banner.setVisibility(View.INVISIBLE);
            }
        });

    }

    public static boolean isBannerVisible(){
        return true;
    }

    public static boolean isRewardedVideoAvialable(){
        return true;
    }

    public static void showInterstitial(){

        activity.get().runOnUiThread( new Runnable() {
            public void run() {
                if (interstitial != null) {
                    Log.d("SHOW", "Interstitial Show: "+interstitial.toString());;
                    interstitial.show(activity.get());
                }
            }
        });

    }

    public static void showFullScreen(){
        activity.get().runOnUiThread( new Runnable() {
            public void run() {
                if (interstitial != null) {
                    Log.d("SHOW", "Interstitial Show: "+interstitial.toString());;
                    interstitial.show(activity.get());
                }
            }
        });
    }

    public static void showRewardedVideo(){
        activity.get().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (rewardedVideo != null) {
                    //Activity activityContext = MainActivity.this;
                    rewardedVideo.show(activity.get(), new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            // Handle the reward.
                            rewardedVideoDidEnd();
                            Log.d("REWARD_AD", "The user earned the reward.");
                            //int rewardAmount = rewardItem.getAmount();
                            //String rewardType = rewardItem.getType();
                        }
                    });
                } else {
                    Log.d("REWARD_AD", "The rewarded ad wasn't ready yet."+" the error is: ");
                }

            }
        });

    }

    public static void buttonActivated(){

    }

    public static boolean buttonVisible(){
        return true;
    }
}
