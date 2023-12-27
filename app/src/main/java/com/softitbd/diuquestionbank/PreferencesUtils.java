package com.softitbd.diuquestionbank;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtils {

    private static final String PREFS_NAME = "MyPrefs";

    // Keys for storing data in SharedPreferences
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_STATUS = "user_status";
    private static final String KEY_USER_REMAINING_DAY = "user_remaining_days";
    private static final String KEY_USER_END_DATE = "user_end_date";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // Save access token to SharedPreferences
    public static void saveAccessToken(Context context, String accessToken) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.apply();
    }

    // Retrieve access token from SharedPreferences
    public static String getAccessToken(Context context) {
        return getSharedPreferences(context).getString(KEY_ACCESS_TOKEN, "");
    }

    // Save user ID to SharedPreferences
    public static void saveUserId(Context context, String userId) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    // Retrieve user ID from SharedPreferences
    public static String getUserId(Context context) {
        return getSharedPreferences(context).getString(KEY_USER_ID, "");
    }

    // Save user name to SharedPreferences
    public static void saveUserName(Context context, String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_USER_NAME, userName);
        editor.apply();
    }

    // Retrieve user name from SharedPreferences
    public static String getUserName(Context context) {
        return getSharedPreferences(context).getString(KEY_USER_NAME, "");
    }

    public static void saveUserStatus(Context context, String status) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_USER_STATUS, status);
        editor.apply();
    }
    public static String getUserStatus(Context context) {
        return getSharedPreferences(context).getString(KEY_USER_STATUS, "");
    }
    public static void saveUserRemainingDay(Context context, int day) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(KEY_USER_END_DATE, day);
        editor.apply();
    }
    public static int getUserRemainingDay(Context context) {
        return getSharedPreferences(context).getInt(KEY_USER_REMAINING_DAY, 0);
    }
    public static void saveUserEndDate(Context context, String end_date) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_USER_END_DATE, end_date);
        editor.apply();
    }
    public static String getUserEndDate(Context context) {
        return getSharedPreferences(context).getString(KEY_USER_END_DATE, "");
    }
}
