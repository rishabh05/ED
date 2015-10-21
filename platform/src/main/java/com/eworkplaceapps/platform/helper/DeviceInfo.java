//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/18/2015
//===============================================================================
package com.eworkplaceapps.platform.helper;

import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.UUID;

public class DeviceInfo extends BaseEntity {

    private static String DeviceInfo_Entity_Name = "DeviceInfo";

    public DeviceInfo() {
        super(DeviceInfo_Entity_Name);
    }

    /**
     * Create AppConfig object and return created object.
     *
     * @return
     */
    public static DeviceInfo createEntity() {
        return new DeviceInfo();
    }

    private String deviceId = "";

    private String appName = "";

    private UUID tenantId = Utils.emptyUUID();

    private UUID userId = Utils.emptyUUID();

    private boolean isInitialized = false;

    private int lastSyncCount = 0;

    public int getLastSyncCount() {
        return lastSyncCount;
    }

    public void setLastSyncCount(int lastSyncCount) {
        setPropertyChanged(this.lastSyncCount, lastSyncCount);
        this.lastSyncCount = lastSyncCount;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public void setInitialized(boolean isInitialized) {
        setPropertyChanged(this.isInitialized, isInitialized);
        this.isInitialized = isInitialized;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        setPropertyChanged(this.userId, userId);
        this.userId = userId;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        setPropertyChanged(this.tenantId, tenantId);
        this.tenantId = tenantId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        setPropertyChanged(this.appName, appName);
        this.appName = appName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        setPropertyChanged(this.deviceId, deviceId);
        this.deviceId = deviceId;
    }

    /**
     * Create the copy of an existing Task object.
     *
     * @param entity
     * @return
     */
    public BaseEntity copyTo(BaseEntity entity) {
        // If both entities are not same then return the entity.
        if (entity.getEntityName() != this.entityName) {
            return null;
        }
        DeviceInfo deviceInfo = (DeviceInfo) entity;
        deviceInfo.setDeviceId(this.deviceId);
        deviceInfo.setTenantId(this.tenantId);
        deviceInfo.setUserId(this.userId);
        deviceInfo.setLastSyncCount(this.lastSyncCount);
        deviceInfo.setInitialized(this.isInitialized);
        return deviceInfo;
    }
}
