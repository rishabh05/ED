//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/29/2015
//===============================================================================
package com.eworkplaceapps.platform.commons;

/**
 * all types of constants are defined here
 */
public class Commons {
    //base url
    public static final String BASE_URL = "http://ewp-dev36.eworkplace.com:5500";

    //controller
    public static final String AUTH_CONTROLLER = "/AuthenticationController";
    public static final String SYNC_CONTROLLER = "/SyncController";

    //methods for url
    public static final String AUTH_USER = "/AuthenticateUser";
    public static final String LOGIN_USER_TOKEN = "/GetLoginUserToken";


    //methods for headers
    public static final String AUTH_USER_A = "AuthenticateUser";
    public static final String SIGNUP_USER_A = "SignUpUser";
    public static final String SET_PRIMARYUSER_PASSWORD = "SetPrimaryUserPassword";
    public static final String REQUIRED_SET_PASSWORD = "RequiredSetPassword";
    public static final String RESET_PASSWORD = "ResetPassword";
    public static final String CHANGE_PASSWORD = "ChangePassword";
    public static final String LOGIN_USER_TOKEN_A = "GetLoginUserToken";
    public static final String RESOLVE_CLIENT_SYNC_CONFLICTS_A = "ResolveClientSyncConflicts";
    public static final String REGISTER_DEVICE_A = "RegisterDevice";
    public static final String GET_INIT_FOR_TENANT_A = "GetInitDataForTenant";

    //header params
    public static final String USER_EMAIL = "UserEmail";
    public static final String USER_PASS = "UserPassword";
    public static final String REQ_DEVICE_ID = "RequesterDeviceId";
    public static final String ACTION = "action";
    public static final String TENANT_ID = "TenantId";
    public static final String DB_VERSION = "DatabaseVersion";
    public static final String APP_SUITE_VERSION = "AppSuiteVersion";
    public static final String REQ_DEVICE_NAME = "RequesterDeviceName";
    public static final String LOGIN_TOKEN = "EwpAccessToken";
    public static final String CHUNK_SIZE_IN_BYTES = "ChunkSizeInBytes";
    public static final String LAST_SENT_TRANS_ROW_NUM = "LastSentTransRowNumber";
    public static final String GET_LATITUDE_LONGITUDE = "GetAddress";


}
