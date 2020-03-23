package redditreader.com.redditreader_android.models;

import android.content.SharedPreferences;

import redditreader.com.redditreader_android.MainActivity;

import static android.content.Context.MODE_PRIVATE;

public class User {
    static private String username = "Username";
    static private String profileURL = "https://www.redditstatic.com/avatars/avatar_default_03_FFB000.png";
    static private int karma = 0;
    static private int accountAge = 1;
    static private String accountAgePostfix = " day";
    static private boolean updated = false;

    public static boolean isUpdated() {
        return updated;
    }

    public static void setUpdated(boolean updated) {
        User.updated = updated;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        User.username = username;
    }

    public static String getProfileURL() {
        return profileURL;
    }

    public static void setProfileURL(String profileURL) {
        User.profileURL = profileURL;
    }

    public static int getKarma() {
        return karma;
    }

    public static void setKarma(int karma) {
        User.karma = karma;
    }

    public static int getAccountAge() {
        return accountAge;
    }

    public static void setAccountAge(int accountAge) {
        User.accountAge = accountAge;
    }

    public static String getAccountAgePostfix() {
        return accountAgePostfix;
    }

    public static void setAccountAgePostfix(String accountAgePostfix) {
        User.accountAgePostfix = accountAgePostfix;
    }

    public static void retrieveUser(){
        final SharedPreferences sharedPreferences = MainActivity.getContext().getSharedPreferences(MainActivity.SHARED_PREFS, MODE_PRIVATE);
        username = sharedPreferences.getString("username","Username");
        karma = sharedPreferences.getInt("karma",0);
        accountAge = sharedPreferences.getInt("accountAge",1);
        accountAgePostfix = sharedPreferences.getString("accountAgePostfix"," day old");
        profileURL = sharedPreferences.getString("profileURL","https://www.redditstatic.com/avatars/avatar_default_03_FFB000.png");
        updated = true;
    }

    public static void storeUser(){
        final SharedPreferences sharedPreferences = MainActivity.getContext().getSharedPreferences(MainActivity.SHARED_PREFS, MODE_PRIVATE);
        sharedPreferences.edit().putString("username",username).apply();
        sharedPreferences.edit().putInt("karma",karma).apply();
        sharedPreferences.edit().putInt("accountAge",accountAge).apply();
        sharedPreferences.edit().putString("accountAgePostfix",accountAgePostfix).apply();
        sharedPreferences.edit().putString("profileURL", profileURL).apply();
    }
}
