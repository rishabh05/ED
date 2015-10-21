//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/18/2015
//===============================================================================
package com.eworkplaceapps.platform.helper;

import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.exception.EwpException;

import java.util.UUID;

/**
 * 1) Build sql statement for getting AppConfig instance.
 */
public class AppConfigData extends BaseData<AppConfig> {

    /**
     * Searches for Task Entity that matches the id value.
     * returns Task an Entity object.
     *
     * @return
     */
    public AppConfig getAppConfig() throws EwpException {
        String sql = "SELECT * From AppConfig";
        return executeSqlAndGetEntity(sql);
    }

    @Override
    public AppConfig createEntity() {
        return AppConfig.createEntity();
    }


    @Override
    public void update(AppConfig entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("ClientDeviceId", entity.getClientDeviceId());
        super.update("AppConfig", values, null, null);
    }

    /**
     * @param entity
     * @return
     * @throws EwpException
     */
    public int updateClientDeviceId(AppConfig entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("ClientDeviceId", entity.getClientDeviceId());
        int recordsUpdated = super.update("AppConfig", values, null, null);
        return recordsUpdated;
    }

    /**
     * This method will generate the new clientDeviceId if app config has empty clientDeviceId.
     */
    public void generateMyDeviceId() throws EwpException {
        AppConfig response = getAppConfig();
        if (response.getClientDeviceId() == null || "".equals(response.getClientDeviceId())) {
            UUID deviceId = UUID.randomUUID();
            response.setClientDeviceId(deviceId.toString());
            update(response);
            // Updating the SyncTriggerData as well as
            String sql = "UPDATE SyncTriggerData SET DeviceId= '" + deviceId.toString() + "'";
            super.executeNonQuery(sql);
        }
    }

    /**
     * Set property value from  database ResultSet.
     *
     * @param resultSet
     */
    @Override
    public void setPropertiesFromResultSet(AppConfig entity, Cursor resultSet) {

        String id = resultSet.getString(resultSet.getColumnIndex("ServerDeviceId"));
        if (id != null) {
            entity.setServerDeviceId(id);
        }

        String clientDeviceId = resultSet.getString(resultSet.getColumnIndex("ClientDeviceId"));
        if (clientDeviceId != null) {
            entity.setClientDeviceId(clientDeviceId);
        }

        String version = resultSet.getString(resultSet.getColumnIndex("AppSuiteVersion"));
        if (version != null) {
            entity.setApplicationSuiteVersion(version);
        }

        String databaseVersion = resultSet.getString(resultSet.getColumnIndex("DatabaseVersion"));
        if (databaseVersion != null) {
            entity.setDatabaseVersion(databaseVersion);
        }

        String serverUrl = resultSet.getString(resultSet.getColumnIndex("ServerUrl"));
        if (serverUrl != null) {
            entity.setServerUrl(serverUrl);
        }

        String authenticateUrl = resultSet.getString(resultSet.getColumnIndex("AuthenticateUrl"));
        if (authenticateUrl != null) {
            entity.setAuthenticateUrl(authenticateUrl);
        }

        Boolean allowCellularAccess = Boolean.parseBoolean(resultSet.getString(resultSet.getColumnIndex("AllowCellularAccess")));
        entity.setAllowCellularAccess(allowCellularAccess);

        String timeoutIntervalForHTTPRequest = resultSet.getString(resultSet.getColumnIndex("TimeoutIntervalForHTTPRequest"));
        if (timeoutIntervalForHTTPRequest != null && !"".equals(timeoutIntervalForHTTPRequest)) {
            entity.setTimeoutIntervalForHTTPRequest(Integer.valueOf(timeoutIntervalForHTTPRequest));
        }
        entity.setDirty(false);
    }
}
