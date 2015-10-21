package com.eworkplaceapps.employeedirectory.common;

import com.eworkplaceapps.platform.utils.DevicePreferences;

/**
 * Created by eWorkplace on 9/30/2015.
 */

public class EDPreferences extends DevicePreferences {
    /// Employee Preference unique key. All are unique keys, It used to save employee preference settings in device.
    private String appVibrate = "Connect-InAppVibrate";
    private String appSound = "Connect-InAppSound";
    private String tone = "Connect-Tone";
    private String useCellularDataKey = "Connect-UseCellularData";
    private String timeToSyncKey = "Connect-TimeToSync";
    private int timeToSync = 2;
    private boolean useCellularData = true;
    private String ringTone = "";
    private boolean inAppSound = true;
    private boolean inAppVibrate;

    private EDPreferences() {
        timeToSync = super.getIntValueFromKey(timeToSyncKey);
        useCellularData = super.getBooleanlValueFromKey(useCellularDataKey);
        inAppSound = super.getBooleanlValueFromKey(appSound);
        inAppVibrate = super.getBooleanlValueFromKey(appVibrate);
        ringTone = super.getStringValueFromKey(ringTone);
    }

    public int getTimeToSync() {
        return timeToSync;
    }

    public void setTimeToSync(int timeToSync) {
        super.setIntValue(timeToSyncKey, timeToSync);
        this.timeToSync = timeToSync;
    }


    public boolean isUseCellularData() {
        return useCellularData;
    }

    public String getRingTone() {
        return ringTone;
    }

    public void setRingTone(String ringTone) {
        super.setStringValue(tone, ringTone);
        this.ringTone = ringTone;
    }

    public void setUseCellularData(boolean useCellularData) {

        super.setBooleanValue(useCellularDataKey, useCellularData);
        this.useCellularData = useCellularData;
    }

    public boolean isInAppSound() {
        return inAppSound;
    }

    public void setInAppSound(boolean inAppSound) {
        super.setBooleanValue(appSound, inAppSound);
        this.inAppSound = inAppSound;
    }

    public boolean isInAppVibrate() {
        return inAppVibrate;
    }

    public void setInAppVibrate(boolean inAppVibrate) {
        super.setBooleanValue(appVibrate, inAppSound);
        this.inAppVibrate = inAppVibrate;
    }

}
