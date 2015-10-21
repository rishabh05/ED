//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/18/2015
//===============================================================================
package com.eworkplaceapps.platform.helper;

import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.utils.SqlUtils;

import java.util.List;
import java.util.UUID;

/**
 * It expand the BaseData to provide services to DeviceInfo entities.
 * It is used to check, A tenant is already register with a device or not.
 */
public class DeviceInfoData extends BaseData<DeviceInfo> {

    /**
     * GET the  DeviceInfo entity
     *
     * @return
     */
    public DeviceInfo createEntity() {
        return DeviceInfo.createEntity();
    }


    /**
     * Method is used to update the last sync item. Build UPDATE SQL statement and updating the last syncCount.
     *
     * @return
     */
    public boolean updateDeviceLastSyncCount(String deviceId, int lastSyncCount) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("LastSyncCount", lastSyncCount);
        return super.update("DeviceInfo", values, "LOWER(DeviceId)=?", new String[]{deviceId.toLowerCase()}) > 0;
    }

    /**
     * Method is used to update sync process has completed for requested deviceid. Build UPDATE SQL statement and set device initialization is completed.
     *
     * @param deviceId
     * @return
     */
    public boolean updateDeviceInitilizationCompleted(String deviceId) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("IsInitilized", true);
        return super.update("DeviceInfo", values, "LOWER(DeviceId)=?", new String[]{deviceId.toLowerCase()}) > 0;
    }

    public Cursor getUserInfo() throws EwpException {
        String sql = "select D.TenantId as TenantId, D.UserId as UserId,'' as FullName, D.DeviceId, D.LastSyncCount, D.IsInitilized from DeviceInfo as D";
        return executeSqlAndGetResultSet(sql);
    }

    public DeviceInfo getUserInfoAsEntity() throws EwpException {
        String sql = "select D.TenantId as TenantId, D.UserId as UserId, D.DeviceId, D.LastSyncCount, D.IsInitilized from DeviceInfo as D";
        DeviceInfo entity = executeSqlAndGetEntity(sql);
        if (entity != null) {
            return entity;
        }
        return null;
    }

    /**
     * It is used to check. A user is already been used this device.
     *
     * @return
     */
    public boolean userExist() throws EwpException {
        Cursor resultSet = getUserInfo();
        if (resultSet != null && resultSet.moveToFirst()) {
            return true;
        }
        return false;
    }

    /**
     * GET all the Task Entity record from database.
     * Returns Collection of Task Entity.
     *
     * @return
     */
    public List<DeviceInfo> getList() throws EwpException {
        String mySql = "select D.TenantId as TenantId, D.UserId as UserId, D.DeviceId, D.LastSyncCount, D.IsInitilized from DeviceInfo as D";
        return executeSqlAndGetEntityList(mySql);
    }

    /**
     * It is used to add register device info entry with a user and tenant in DeviceInfo table.
     *
     * @param tenantId
     * @param userId
     * @return
     */
    public Object addDeviceInfo(UUID tenantId, UUID userId) throws EwpException {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setTenantId(tenantId);
        deviceInfo.setUserId(userId);
        AppConfigData appConfig = new AppConfigData();
        AppConfig result = appConfig.getAppConfig();
        if (result != null) {
            deviceInfo.setDeviceId(result.getClientDeviceId());
            return add(deviceInfo);
        }
        return null;
    }

    /**
     * It is used to check, DATA is exist with a given tenant id.
     *
     * @return
     */
    public boolean isTenantExist(UUID tenantId) throws EwpException {
        String sql = "select D.TenantId as TenantId, D.UserId as UserId, D.DeviceId, D.LastSyncCount, D.IsInitilized from DeviceInfo as D ";
        sql += "where LOWER(D.TenantId) = LOWER('" + tenantId + "')";
        return SqlUtils.recordExists(sql);
    }

    public boolean cleanDeviceDatabase() throws EwpException {
        DeviceInfoData deviceInfo = new DeviceInfoData();
        Cursor resultSet = deviceInfo.getUserInfo();
        boolean isClean = false;
        if (resultSet != null && resultSet.moveToFirst()) {
            isClean = true;
        }
        if (isClean) {
            return cleanDeviceData();
        }
        return false;
    }

    private boolean cleanDeviceData() throws EwpException {
        String sql = "select * from SyncArticles";
        Cursor resultSet = executeSqlAndGetResultSet(sql);
        if (resultSet != null) {
            while (resultSet.moveToFirst()) {
                String tableName = resultSet.getString(resultSet.getColumnIndex("TableName"));
                if (tableName != null) {
                    String sq = "DELETE FROM " + tableName;
                    boolean error = super.executeNonQuerySuccess(sq);
                    return error;
                }
            }
        }
        boolean error = super.executeNonQuerySuccess("DELETE FROM DeviceInfo");
        return error;
    }

    /**
     * It is used to check, A user is already registered with a tenant.
     *
     * @return
     */
    public static boolean isUserExist() throws EwpException {
        String sql = "select * from DeviceInfo";
        return SqlUtils.recordExists(sql);
    }

    @Override
    public long insertEntity(DeviceInfo entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("DeviceId", entity.getDeviceId());
        values.put("TenantId", entity.getTenantId().toString());
        values.put("IsInitilized", entity.isInitialized());
        values.put("LastSyncCount", entity.getLastSyncCount());
        values.put("UserId", entity.getUserId().toString());
        return super.insert("DeviceInfo", values);
    }

    @Override
    public void update(DeviceInfo entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("DeviceId", entity.getDeviceId());
        values.put("TenantId", entity.getTenantId().toString());
        values.put("IsInitilized", entity.isInitialized());
        values.put("LastSyncCount", entity.getLastSyncCount());
        super.update("DeviceInfo", values, "LOWER(DeviceId)=? and LOWER(TenantId)=?", new String[]{entity.getDeviceId().toLowerCase(), entity.getTenantId().toString().toLowerCase()});
    }

    /**
     * Set property value from  database ResultSet.
     */
    @Override
    public void setPropertiesFromResultSet(DeviceInfo entity, Cursor resultSet) {
        String clientDeviceId = resultSet.getString(resultSet.getColumnIndex("DeviceId"));
        if (clientDeviceId != null) {
            entity.setDeviceId(clientDeviceId);
        }
        String id = resultSet.getString(resultSet.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }
        String userId = resultSet.getString(resultSet.getColumnIndex("UserId"));
        if (userId != null && !"".equals(userId)) {
            entity.setUserId(UUID.fromString(userId));
        }
        String isInitialized = resultSet.getString(resultSet.getColumnIndex("IsInitilized"));
        entity.setInitialized("1".equalsIgnoreCase(isInitialized));
        String syncCount = resultSet.getString(resultSet.getColumnIndex("LastSyncCount"));
        if (syncCount != null) {
            entity.setLastSyncCount(Integer.valueOf(syncCount));
        }
        entity.setDirty(false);
    }
}
