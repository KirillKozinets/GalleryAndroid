package com.journaldev.mvpdagger2.application;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.journaldev.mvpdagger2.application.App;

public class AppPreference {

    private static Boolean isAnim = null;
    private static Boolean isCache = null;
    private static Boolean isDarkTheme = null;

    public void changeTheme(Activity activity, int darkTheme, int lightTheme) {
        if (isDarkTheme == null) {
            activity.setTheme(lightTheme);
        }

        if (isDarkTheme) {
            activity.setTheme(darkTheme);
        } else
            activity.setTheme(lightTheme);
    }

    public Boolean getIsAnim() {
        return isAnim;
    }

    public Boolean getIsCache() {
        return isCache;
    }

    public void setIsAnim(Boolean isAnim) {
        AppPreference.isAnim = isAnim;
    }

    public void setIsDarkTheme(Boolean isDarkTheme) {
        AppPreference.isDarkTheme = isDarkTheme;
    }

    public void setIsCache(Boolean isCache) {
        AppPreference.isCache = isCache;
    }

    public AppPreference(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        loadIsAnim(prefs);
        loadIsDarkTheme(prefs);
        loadIsCache(prefs);
    }

    private void loadIsAnim(SharedPreferences prefs) {
        isAnim = prefs.getBoolean("isAnim", true);
    }

    private void loadIsDarkTheme(SharedPreferences prefs) {
        isDarkTheme = prefs.getBoolean("isDarkTheme", true);
    }

    private void loadIsCache(SharedPreferences prefs) {
        isCache = prefs.getBoolean("isCache", true);
    }

}
