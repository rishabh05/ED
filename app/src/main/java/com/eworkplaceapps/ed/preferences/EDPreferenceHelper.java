//===============================================================================
// (c) 2015 eWorkplace Apps.  All rights reserved.
// Original Author: Dheeraj Nagar
// Original Date: 12 June 2015
//===============================================================================
package com.eworkplaceapps.ed.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.eworkplaceapps.ed.utils.Utils;
import com.eworkplaceapps.platform.context.ContextHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by admin on 6/12/2015.
 */
public class EDPreferenceHelper {

    private static EDPreferenceHelper preferences;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static final String IS_SYNC_STARTED = "isSyncStarted";

    private EDPreferenceHelper() {
    }

    public static EDPreferenceHelper getPreferences() {
        if (preferences == null) {
            preferences = new EDPreferenceHelper();
            sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(ContextHelper.getContext());
            editor = sharedPreferences.edit();
        }
        return preferences;
    }

    /**
     * @param context - Android context
     * @return - The app shared preferences
     */
    private static SharedPreferences getSharedPrefs(Context context) {
        return context.getSharedPreferences(Preferences.PrefNames.PREFS_NAME_TAG, Context.MODE_PRIVATE);
    }

    /**
     * @param key
     * @param value
     */
    public void setStringValue(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * @param key
     * @param value
     */
    public void setIntValue(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * @param key
     */
    public int getIntValue(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public void setMapValue(Map<String, String> socialProfSelectedValue) {
        Set<String> targetSet = new HashSet<String>(socialProfSelectedValue.values());

        editor.remove(Utils.STORED_SET_VALUE);
        editor.commit();
        editor.putStringSet(Utils.STORED_SET_VALUE, targetSet);
        editor.commit();
    }

    public Map<String, String> getMapValue() {
        Map<String, String> socialMap = new HashMap<String, String>();
        Set<String> mapSet = sharedPreferences.getStringSet(Utils.STORED_SET_VALUE, null);
        if (mapSet != null) {
            for (String s : mapSet) {
                socialMap.put(s, s);
            }
        }
        return socialMap;
    }

    /**
     * @param key
     * @return
     */
    public Object getValueFromPreferences(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void clearPreferences() {
        editor.clear();
    }

    public static void setSyncStarted(boolean flag) {
        editor.putBoolean(IS_SYNC_STARTED, flag);
        editor.commit();
    }

    public static boolean isSyncStarted() {
        return sharedPreferences.getBoolean(IS_SYNC_STARTED, false);
    }

    public static void setSyncWorking(boolean flag) {
        editor.putBoolean("isSyncWorking", flag);
        editor.commit();
    }

    public static boolean isSyncWorking() {
        return sharedPreferences.getBoolean("isSyncWorking", false);
    }


    /**
     * Set accessToken on shared preferences
     */
    public static void resetAccessToken(Context context) {
        SharedPreferences prefs = getSharedPrefs(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(Preferences.PrefKeys.PREF_KEY_ACCESS_TOKEN);
        editor.apply();
        editor.commit();
    }


    public static void clearCredentials(Context context) {
        SharedPreferences prefs = getSharedPrefs(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(Preferences.PrefKeys.PREF_KEY_ACCESS_TOKEN);
        editor.remove(Preferences.PrefKeys.PREF_KEY_ACCESS_SECRET);
        editor.remove(Preferences.PrefKeys.PREF_KEY_USER_NAME);
        editor.putBoolean(Preferences.PrefKeys.PREF_KEY_TWITTER_LOGIN, false);
        editor.commit();
    }

}
