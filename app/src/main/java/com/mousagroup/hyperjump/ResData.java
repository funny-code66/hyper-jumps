package com.mousagroup.hyperjump;

import android.content.Context;

import static com.mousagroup.hyperjump.BuildConfig.*;

public class ResData {

    private ResData() {
    }

    private static String _app_name;
    private static String _app_id;
    private static String _admob_app_id;
    private static String _admob_banner_id;
    private static String _admob_interstitial_id;
    private static String _admob_reward_video_id;
    private static String _more_games_url;
    private static String _rate_game_url;
    private static String _share_message;

    public static String getAppName() {
        return _app_name;
    }

    public static String getAppId() {
        return _app_id;
    }

    public static String getAdmobAppId() {
        return _admob_app_id;
    }

    public static String getAdmobBannerId() {
        return _admob_banner_id;
    }

    public static String getAdmobInterstitialId() {
        return _admob_interstitial_id;
    }

    public static String getAdmobRewardId() {
        return _admob_reward_video_id;
    }

    public static String getMoreGamesUrl() {
        return _more_games_url;
    }

    public static String getRateGamesUrl() {
        return _rate_game_url;
    }

    public static String getShareMessage() {
        return _share_message;
    }

    public static void init(Context context) {
        _app_name = context.getResources().getString(R.string.app_name);
        _app_id = context.getResources().getString(R.string.app_id);
        _admob_app_id = context.getResources().getString(R.string.admob_app_id);
        _admob_banner_id = context.getResources().getString(R.string.admob_banner_id);
        _admob_interstitial_id = context.getResources().getString(R.string.admob_interstitial_id);
        _admob_reward_video_id = context.getResources().getString(R.string.admob_reward_video_id);
        _more_games_url = context.getResources().getString(R.string.more_games_url);
        _rate_game_url = "http://play.google.com/store/apps/details?id=" + APPLICATION_ID;
        _share_message = context.getResources().getString(R.string.share_message);
    }

}