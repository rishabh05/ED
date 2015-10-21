//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/17/2015
//===============================================================================
package com.eworkplaceapps.platform.helper;

import com.eworkplaceapps.platform.entity.BaseEntity;

/**
 * This class provides application configuration/setup related data.
 */
public class AppConfig extends BaseEntity {

    private static final String APP_CONFIG_ENTITY_NAME = "AppConfig";

    public AppConfig() {
        super(APP_CONFIG_ENTITY_NAME);
    }

    // Create AppConfig object and return created object.
    public static AppConfig createEntity() {
        return new AppConfig();
    }

    private String authenticateUrl = "";
    /// It's a url of server to sync with.
    private String serverUrl;

    // Getter/setter for server device id
    private String serverDeviceId;

    // Getter/setter for app config client device id
    private String clientDeviceId;

    // Getter/setter for app config client device id
    private String databaseVersion;

    // Getter/setter for APPLICATION Version
    private String applicationSuiteVersion;

    private Boolean allowCellularAccess = true;

    private int timeoutIntervalForHTTPRequest = 120;

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        setPropertyChanged(this.serverUrl, serverUrl);
        this.serverUrl = serverUrl;
    }

    public String getServerDeviceId() {
        return serverDeviceId;
    }

    public void setServerDeviceId(String serverDeviceId) {
        setPropertyChanged(this.serverDeviceId, serverDeviceId);
        this.serverDeviceId = serverDeviceId;
    }

    public String getClientDeviceId() {
        return clientDeviceId;
    }

    public void setClientDeviceId(String clientDeviceId) {
        setPropertyChanged(this.clientDeviceId, clientDeviceId);
        this.clientDeviceId = clientDeviceId;
    }

    public String getDatabaseVersion() {
        return databaseVersion;
    }

    public void setDatabaseVersion(String databaseVersion) {
        setPropertyChanged(this.databaseVersion, databaseVersion);
        this.databaseVersion = databaseVersion;
    }

    public String getApplicationSuiteVersion() {
        return applicationSuiteVersion;
    }

    public void setApplicationSuiteVersion(String applicationSuiteVersion) {
        setPropertyChanged(this.applicationSuiteVersion, applicationSuiteVersion);
        this.applicationSuiteVersion = applicationSuiteVersion;
    }

    public Boolean getAllowCellularAccess() {
        return allowCellularAccess;
    }

    public void setAllowCellularAccess(Boolean allowCellularAccess) {
        setPropertyChanged(this.allowCellularAccess, allowCellularAccess);
        this.allowCellularAccess = allowCellularAccess;
    }

    public int getTimeoutIntervalForHTTPRequest() {
        return timeoutIntervalForHTTPRequest;
    }

    public void setTimeoutIntervalForHTTPRequest(int timeoutIntervalForHTTPRequest) {
        setPropertyChanged(this.timeoutIntervalForHTTPRequest, timeoutIntervalForHTTPRequest);
        this.timeoutIntervalForHTTPRequest = timeoutIntervalForHTTPRequest;
    }

    public String getAuthenticateUrl() {
        return authenticateUrl;
    }

    public void setAuthenticateUrl(String authenticateUrl) {
        this.authenticateUrl = authenticateUrl;
    }
}
