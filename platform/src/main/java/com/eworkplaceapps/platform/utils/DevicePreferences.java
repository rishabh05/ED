package com.eworkplaceapps.platform.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.eworkplaceapps.platform.context.ContextHelper;

/**
 * Created by eWorkplace on 9/30/2015.
 */
public class DevicePreferences {
    private static DevicePreferences preferences;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static final String IS_SYNC_STARTED = "isSyncStarted";

    public DevicePreferences() {
    }

    public static DevicePreferences getPreferences() {
        if (preferences == null) {
            preferences = new DevicePreferences();
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ContextHelper.getContext());
            editor = sharedPreferences.edit();
        }
        return preferences;
    }
    /// It is used to get preference value from key. If value not found then return nil.
    /// :param: key, It is a unique key. user can get value by passing this key.
    public String getStringValueFromKey(String key)  {
        return sharedPreferences.getString(key,"");
    }

    /// It is used to set preference value by passing key and value. Key must be unique if it is same as other key used before then it will override the previoud value.
    /// :param: key, It must be a unique key. user can set value by passing this key.
    /// :param: value, It is a value saved with respect to key.
    public void setStringValue(String key,String value ) {
        editor.putString(key, value);
        editor.commit();
    }
    /// It is used to get preference value from key. If value not found then return nil.
    /// :param: key, It is a unique key. user can get value by passing this key.
    public int getIntValueFromKey(String key)  {
        return sharedPreferences.getInt(key,0);
    }

    /// It is used to set preference value by passing key and value. Key must be unique if it is same as other key used before then it will override the previoud value.
    /// :param: key, It must be a unique key. user can set value by passing this key.
    /// :param: value, It is a value saved with respect to key.
    public void setIntValue(String key,int value ) {
        editor.putInt(key, value);
        editor.commit();
    }
    /// It is used to get preference value from key. If value not found then return nil.
    /// :param: key, It is a unique key. user can get value by passing this key.
    public boolean getBooleanlValueFromKey(String key)  {
        return sharedPreferences.getBoolean(key, false);
    }

    /// It is used to set preference value by passing key and value. Key must be unique if it is same as other key used before then it will override the previoud value.
    /// :param: key, It must be a unique key. user can set value by passing this key.
    /// :param: value, It is a value saved with respect to key.
    public void setBooleanValue(String key,boolean value ) {
        editor.putBoolean(key, value);
        editor.commit();
    }

}
