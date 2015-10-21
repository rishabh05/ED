//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/24/2015
//===============================================================================
package com.eworkplaceapps.platform.tenant;

import android.database.Cursor;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.eworkplaceapps.platform.authentication.AuthenticationReply;
import com.eworkplaceapps.platform.commons.Commons;
import com.eworkplaceapps.platform.context.ContextHelper;
import com.eworkplaceapps.platform.dbsync.RestService;
import com.eworkplaceapps.platform.dbsync.SyncHistory;
import com.eworkplaceapps.platform.dbsync.SyncNetworkService;
import com.eworkplaceapps.platform.dbsync.SyncReply;
import com.eworkplaceapps.platform.dbsync.SyncRequest;
import com.eworkplaceapps.platform.dbsync.SyncService;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.ExceptionBean;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.helper.AppConfig;
import com.eworkplaceapps.platform.helper.AppConfigData;
import com.eworkplaceapps.platform.helper.DeviceInfo;
import com.eworkplaceapps.platform.helper.DeviceInfoData;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.requesthandler.RequestCallback;
import com.eworkplaceapps.platform.utils.AppMessage;
import com.eworkplaceapps.platform.utils.Utils;
import com.eworkplaceapps.platform.utils.enums.AuthenticationType;
import com.eworkplaceapps.platform.utils.enums.RequesterType;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

/**
 * This is a TenantRegistrationService class for tenant initialization. Points to note about the class:
 * This class provides tenant registration through rest service.
 * It implements complete control flow to tenant registration with the server.
 * There are three main control flows:
 * (1) It authenticate the user and send authentication reply to UI.
 * (2) Client sends its data to tenant registration if Client is member of Single tenant
 * (3) Client sends its data to tenant registration if Client is member of Multiple tenant
 * Note that all 3 control flows are initiated from the client end.
 * Server needs to just respond correctly.
 * The control flow in each case is broken down in to several unit steps.
 * The caller must call the steps in the right sequence.
 * The steps are designed to manage the asynchronous calls to the server. ReceiveSyncTime
 */
public class TenantRegistrationService {
    private final Logger log = Logger.getLogger(TenantRegistrationService.class);
    private static final String TAG = "TenantRegistration";
    final String APP_ID = "A8920FC7-4874-4854-BCE1-4C4909C6C824";
    private AuthenticationReply authenticationReply;
    // Setter for UserEmail.
    private String userEmail = "";
    // Getter/setter for tenant Id.
    private String tenantId = Utils.emptyUUID().toString();
    private TenantRegister selectedTenant;
    private int initialChuckSizeByte = 100000;
    private UUID initHistoryId;
    private int chunkByteSizeAddedBy = 50000;
    private int lastPercent = 0;

    public TenantRegister getSelectedTenant() {
        return selectedTenant;
    }

    /**
     * Method is used to authenticate user.
     *
     * @param userEmail
     * @param password
     * @throws EwpException
     */
    public void authenticateUser(final String userEmail, String password, double lat, double lng,String locale,
                                 final RequestCallback requestCallback) throws EwpException, JSONException, UnsupportedEncodingException {
        RestService restService = new RestService();
        String url = restService.getAuthenticateServerUrlString() + "tenantusers/authenticateuser";
        String clientDeviceId = restService.getAppConfig().getClientDeviceId();
        AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                super.callback(url, object, status);
                try {
                    if (status.getCode() == 200 && object != null) {
                        authenticationReply = parseAuthenticationReply(object);
                        if (authenticationReply != null && authenticationReply.getTenantList().size() == 1) {
                            authenticationReply.getTenantList().get(0).setLoginToken(authenticationReply.getLoginToken());
                        }
                        requestCallback.onSuccess(Commons.AUTH_USER_A, authenticationReply);
                    } else {
                        String error = "";
                        EwpException ex = EwpException.handleError(null, status);
                        if (ex != null && ex.message != null && ex.message.size() > 0)
                            error = ex.message.get(0);
                        requestCallback.onFailure(Commons.AUTH_USER_A, error);
                        Log.e(TAG, error);
                    }
                } catch (Exception e) {
                    requestCallback.onFailure(Commons.AUTH_USER_A, e.getMessage());
                    Log.e(TAG, e.getMessage());
                }
            }
        };
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("OAuthAccessToken", "");
        params.put("ApplicationId", APP_ID);
//        params.put("TenantUrl", "");
        params.put("Region", "US");
        params.put("IANATimeZoneId", TimeZone.getDefault().getID());
        params.put("RequesterId", restService.getAppConfig().getClientDeviceId());
        params.put("RequesterType", RequesterType.ANDROID.getId());
        params.put("Password", password);
        params.put("LoginEmail", userEmail);
        params.put("AuthenticationType", AuthenticationType.EW_APPS.getId());
        params.put("Latitude", String.valueOf(lat));
        params.put("Longitude",String.valueOf(lng));
        log.info("login params " + params.toString());
        AQuery aQuery = new AQuery(ContextHelper.getContext());
        aQuery.ajax(url, params, JSONObject.class, cb);
    }

    /**
     * get user login token based on tenant ID (in case of multiple tenants)
     *
     * @param userEmail
     * @param tenantRegister
     * @throws EwpException
     */
    public void getUserToken(String userEmail, TenantRegister tenantRegister, final RequestCallback requestCallback) throws EwpException {
        String url = Commons.BASE_URL + Commons.AUTH_CONTROLLER + Commons.LOGIN_USER_TOKEN;
        AjaxCallback<XmlPullParser> cb = new AjaxCallback<XmlPullParser>() {
            @Override
            public void callback(String url, XmlPullParser object, AjaxStatus status) {
                super.callback(url, object, status);
                if (status.getCode() == 200 && object != null) {
                    int event = 0;
                    String text = "", token = "";
                    try {
                        event = object.getEventType();
                        while (event != XmlPullParser.END_DOCUMENT) {
                            String name = object.getName();
                            switch (event) {
                                case XmlPullParser.START_TAG:
                                    break;
                                case XmlPullParser.TEXT:
                                    text = object.getText();
                                    break;
                                case XmlPullParser.END_TAG:
                                    if (name != null &&
                                            "string".equals(name)) {
                                        token = text;
                                    }
                                    break;
                                default:
                                    break;
                            }
                            event = object.next();
                        }
                        selectedTenant.setLoginToken(token);
                        requestCallback.onSuccess(Commons.LOGIN_USER_TOKEN_A, token);
                    } catch (XmlPullParserException e) {
                        requestCallback.onFailure(Commons.LOGIN_USER_TOKEN_A, null);
                        Log.d(TAG, "" + e);
                    } catch (IOException e) {
                        requestCallback.onFailure(Commons.LOGIN_USER_TOKEN_A, null);
                        Log.d(TAG, "" + e);
                    }
                } else if (status.getCode() == 500 && object != null) {
                    ExceptionBean exceptionBean = null;
                    try {
                        exceptionBean = ExceptionBean.parseErrorXml(object, EnumsForExceptions.ErrorModule.DATA_SERVICE, status.getCode());
                    } catch (EwpException e) {
                        e.printStackTrace();
                        requestCallback.onFailure(Commons.LOGIN_USER_TOKEN_A, exceptionBean);
                    }
                }
            }
        };
        cb.header(Commons.USER_EMAIL, userEmail);
        cb.header(Commons.TENANT_ID, tenantRegister.getTenantId().toString());
        cb.header(Commons.ACTION, Commons.GET_INIT_FOR_TENANT_A);
        cb.header(Commons.REQ_DEVICE_ID, "A8920FC7-4874-4854-BCE1-4C4909C6C824");
        cb.url(url).type(XmlPullParser.class);
        cb.method(AQuery.METHOD_GET);
        AQuery aQuery = new AQuery(ContextHelper.getContext());
        aQuery.ajax(cb);
    }

    /**
     * parse authentication reply
     *
     * @param object
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    public AuthenticationReply parseAuthenticationReply(XmlPullParser object) throws XmlPullParserException, IOException {
        int event;
        String text = null;
        AuthenticationReply authReply = new AuthenticationReply();
        TenantRegister teaTenantRegister = null;
        event = object.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            String name = object.getName();
            switch (event) {
                case XmlPullParser.START_TAG:
                    if ("TENANT".equals(name)) {
                        teaTenantRegister = new TenantRegister();
                        authReply.getTenantList().add(teaTenantRegister);
                    }
                    break;
                case XmlPullParser.TEXT:
                    text = object.getText();
                    break;
                case XmlPullParser.END_TAG:
                    if ("Valid".equals(name)) {
                        authReply.setValid("True".equals(text));
                    } else if (name.equals(Commons.LOGIN_TOKEN)) {
                        authReply.setLoginToken(text);
                    } else if ("SYSTEM_ADMIN".equals(name)) {
                        authReply.setSystemAdmin("True".equals(text));
                    } else if ("MultiTenantUser".equals(name)) {
                        authReply.setMultiTenantUser("True".equals(text));
                    } else if ("TENANT".equals(name)) {
                        //Parse each tenant element and add to list and add to tenantlist.
                        teaTenantRegister.setTenantId(UUID.fromString(object.getAttributeValue(null, "Id")));
                        teaTenantRegister.setName(object.getAttributeValue(null, "Name"));
                    }
                    break;
                default:
                    break;
            }
            event = object.next();
        }
        authenticationReply = authReply;
        return authenticationReply;
    }

    /**
     * parse authentication reply from JSON
     *
     * @param object
     * @return AuthenticationReply
     * @throws JSONException
     */
    public AuthenticationReply parseAuthenticationReply(JSONObject object) throws JSONException {
        AuthenticationReply authReply = new AuthenticationReply();
        authReply.setValid("true".equals(object.getString("Success")));
//        authReply.setSystemAdmin("true".equals(object.getString("SystemAdmin")));
        authReply.setMultiTenantUser("true".equals(object.getString("MultiTenant")));
//        authReply.setUrlString(object.getString("TenantUrl"));
        authReply.setLoginToken(object.getString("AccessToken"));
        authReply.setUserID(UUID.fromString(object.getString("UserId")));
        JSONArray tenantArray = object.getJSONArray("TenantList");
        for (int i = 0; i < tenantArray.length(); i++) {
            JSONObject tenantObject = tenantArray.getJSONObject(i);
            TenantRegister tenantRegister = new TenantRegister();
            tenantRegister.setTenantId(UUID.fromString(tenantObject.getString("TenantId")));
            tenantRegister.setName(tenantObject.getString("Name"));
            if (authReply.getMultiTenantUser()) {
                tenantRegister.setLoginToken("");
            }
            authReply.getTenantList().add(tenantRegister);
        }
        return authReply;
    }

    /**
     * Method take 3 parameters.
     * 1) It ask for logged in user email
     * 2) TenantRegister item: Need to register user from given tenant object.
     * 3) Request callback
     *
     * @param loginEmail
     * @param tenantRegister
     * @param requestCallback
     * @throws EwpException
     */
    public void registerDevice(String loginEmail, TenantRegister tenantRegister, RequestCallback requestCallback) throws EwpException {
        DeviceInfoData deviceInfoData = new DeviceInfoData();
        DeviceInfo deviceInfo = deviceInfoData.getUserInfoAsEntity();
        userEmail = loginEmail;
        this.selectedTenant = tenantRegister;
        this.tenantId = tenantRegister.getTenantId().toString();
        // If no user is registered before then register the device.
        // or logged in user changed then also call register device() method to update user.
        if (deviceInfo == null || deviceInfo.getEntityId().toString() != tenantRegister.getUserId().toString()) {
            if (tenantId.equals(Utils.emptyUUID().toString())) {
                registerDevice(tenantRegister, requestCallback);
            } else {
                // USER is member of multiple tenant
                registerDeviceForTenant(requestCallback);
            }
        }
    }

    /**
     * register device callback
     *
     * @param requestCallback
     * @throws EwpException
     */
    public void registerDevice(TenantRegister tenantRegister, final RequestCallback requestCallback) throws EwpException {
        RestService restService = new RestService();
        SyncService service = new SyncService();
        selectedTenant = tenantRegister;
        // get the device id
        String myDeviceId = service.getDataProvider().getMyDeviceId();
        AjaxCallback<XmlPullParser> callback = new AjaxCallback<XmlPullParser>() {
            @Override
            public void callback(String url, XmlPullParser object, AjaxStatus status) {
                super.callback(url, object, status);
                try {
                    if (status.getCode() == 200 && object != null) {
                        List<TenantRegister> tenantRegisterList = TenantRegister.parseXmlList(object);
                        if (tenantRegisterList != null && !tenantRegisterList.isEmpty()) {
                            tenantRegisterList.get(0).setLoginToken(selectedTenant.getLoginToken());
                            selectedTenant = tenantRegisterList.get(0);
                            requestCallback.onSuccess(Commons.REGISTER_DEVICE_A, tenantRegisterList);
                        } else {
                            requestCallback.onFailure(Commons.REGISTER_DEVICE_A, "Device Registration Failure");
                        }
                    } else {
                        String error = "";
                        EwpException ex = EwpException.handleError(null, status);
                        if (ex != null && ex.message != null && ex.message.size() > 0)
                            error = ex.message.get(0);
                        requestCallback.onFailure(Commons.REGISTER_DEVICE_A, error);
                    }
                } catch (Exception e) {
                    requestCallback.onFailure(Commons.REGISTER_DEVICE_A, "" + e);
                    Log.d(TAG, "Exception Occurred" + e);
                }
            }
        };
        String urlString = restService.serverUrlString() + "registerdevice";
        callback.url(urlString);
        callback.type(XmlPullParser.class);
        callback.header(Commons.ACTION, Commons.REGISTER_DEVICE_A);
        callback.header(Commons.REQ_DEVICE_ID, myDeviceId);
        callback.header(Commons.LOGIN_TOKEN, selectedTenant.getLoginToken());
        callback.header(Commons.USER_EMAIL, userEmail);
        callback.method(AQuery.METHOD_GET);
        AQuery aQuery = new AQuery(ContextHelper.getContext());
        aQuery.ajax(callback);
        SyncHistory.addHistory(Commons.REGISTER_DEVICE_A, null, null, Utils.getUTCDateTimeAsDate(), Utils.getUTCDateTimeAsDate(), restService.getAppConfig().getServerDeviceId(), "OK", 0);
        Log.d(this.getClass().getName(), "Sending data to Register Device");
    }

    /**
     * register device for tenant callback
     *
     * @param requestCallback
     * @throws EwpException
     */
    public void registerDeviceForTenant(final RequestCallback requestCallback) throws EwpException {
        RestService restService = new RestService();
        SyncService service = new SyncService();
        AppConfigData appConfigData = new AppConfigData();
        AppConfig appConfig = appConfigData.getAppConfig();
        String appVersion = appConfig.getApplicationSuiteVersion();
        String dbVersion = appConfig.getDatabaseVersion();
        // get the device id
        String myDeviceId = service.getDataProvider().getMyDeviceId();
        AjaxCallback<XmlPullParser> callback = new AjaxCallback<XmlPullParser>() {
            @Override
            public void callback(String url, XmlPullParser object, AjaxStatus status) {
                super.callback(url, object, status);
                try {
                    if (status.getCode() == 200 && object != null) {
                        List<TenantRegister> tenantRegisterList = TenantRegister.parseXmlList(object);
                        if (!tenantRegisterList.isEmpty()) {
                            tenantRegisterList.get(0).setLoginToken(selectedTenant.getLoginToken());
                            selectedTenant = tenantRegisterList.get(0);
                           /* //TODO//added by shrey on 24/07/2015 for stopping initialization process
                            EwpSession.getSharedInstance().setSession(selectedTenant.getTenantId(), selectedTenant.getUserId(), "", selectedTenant.getLoginToken());*/
                        }
                        requestCallback.onSuccess("RegisterDevice", tenantRegisterList);
                    } else {
                        String error = "";
                        EwpException ex = EwpException.handleError(null, status);
                        if (ex != null && ex.message != null && ex.message.size() > 0)
                            error = ex.message.get(0);
                        requestCallback.onFailure("RegisterDevice", error);
                    }
                } catch (Exception e) {
                    requestCallback.onFailure("RegisterDevice", "" + e);
                    e.printStackTrace();
                    Log.d(TAG, "Exception Occurred" + e);
                }
            }
        };
        String urlString = restService.serverUrlString() + "registerdevice";
        callback.url(urlString);
        callback.type(XmlPullParser.class);
        callback.header(Commons.ACTION, "RegisterDevice");
        callback.header(Commons.REQ_DEVICE_ID, myDeviceId);
        callback.header(Commons.TENANT_ID, selectedTenant.getTenantId().toString());
        callback.header(Commons.LOGIN_TOKEN, selectedTenant.getLoginToken());
        callback.header(Commons.APP_SUITE_VERSION, appVersion);
        callback.header(Commons.DB_VERSION, dbVersion);
        callback.header(Commons.REQ_DEVICE_NAME, Utils.getDeviceIMEI(ContextHelper.getContext()));
        callback.header(Commons.USER_EMAIL, userEmail);
        callback.method(AQuery.METHOD_POST);
        log.info("register device params " + callback.toString());
        AQuery aQuery = new AQuery(ContextHelper.getContext());
        aQuery.ajax(callback);
        SyncHistory.addHistory("RegisterDevice", null, null, Utils.getUTCDateTimeAsDate(), Utils.getUTCDateTimeAsDate(), restService.getAppConfig().getServerDeviceId(), "OK", 0);
        Log.d(this.getClass().getName(), "Sending data to RegisterDeviceForTenant Device");
    }


    /**
     * If data base initialization process has been completed then this method return true.
     * If initialization process is incomplete then return false as well as return last sync transaction number.
     *
     * @return
     */
    public boolean isDatabaseInitializationProcessComplete() throws EwpException {
        DeviceInfoData deviceInfo = new DeviceInfoData();
        Cursor resultSet = deviceInfo.getUserInfo();
        if (resultSet != null && resultSet.moveToFirst()) {
            //STRING lastSyncCount = resultSet.getColumnName(resultSet.getColumnIndex("LastSyncCount"));
            String isInitialized = resultSet.getColumnName(resultSet.getColumnIndex("IsInitialized"));
            if (!"true".equals(isInitialized)) {
                return false;
            }
        }
        return false;
    }

    /**
     * It is used to add/update in DeviceInfo table.
     * It is also used to initialize the data with selected tenant id. If selected tenant data already exist then don't need to re-initialize the data.
     *
     * @throws EwpException
     */
    public void initializeDataForTenant(RequestCallback requestCallback) throws EwpException {
        DeviceInfoData deviceInfoData = new DeviceInfoData();
        TenantRegister obj = this.selectedTenant;
        boolean resultInfoTuple = deviceInfoData.isTenantExist(obj.getTenantId());

        // If tenant data is exist then we don't need to re-initialize the data.
        if (resultInfoTuple) {
            List<DeviceInfo> deviceInfoList = deviceInfoData.getList();
            if (deviceInfoList != null && !deviceInfoList.isEmpty()) {
                DeviceInfo deviceInfo = deviceInfoList.get(0);
                // If tenant data is exist with different user id then update user id in DeviceInfo table.
                if (!obj.getUserId().equals(deviceInfo.getUserId())) {
                    deviceInfo.setUserId(obj.getUserId());
                    deviceInfoData.update(deviceInfo);
                    // UPDATE the session variables.
                    EwpSession.getSharedInstance().setSession(UUID.fromString(tenantId), obj.getUserId(), obj.getName(), obj.getLoginToken());
                    // Resetting the session.
                    EwpSession.getSharedInstance().refresh();
                } else {
                    EwpSession.getSharedInstance().setSession(UUID.fromString(tenantId), obj.getUserId(), obj.getName(), obj.getLoginToken());
                }
            }
        } else {
            boolean isCleaned = deviceInfoData.cleanDeviceDatabase();
            Object resultTuple = deviceInfoData.addDeviceInfo(UUID.fromString(tenantId), obj.getUserId());
            if (resultTuple == null) {
                throw new EwpException("");
            }
            initHistoryId = null;
            // Setting the session variables.
            EwpSession.getSharedInstance().setSession(UUID.fromString(tenantId), obj.getUserId(), obj.getName(), obj.getLoginToken());
            // loginViewController?.showActivityIndicator()
            initializeDataForTenant(0, initialChuckSizeByte, requestCallback, true);
        }
    }

    /**
     * initialize Database For Given USER
     *
     * @param loginEmail
     * @param tenant
     * @throws EwpException
     */
    public void initializeDatabaseForGivenUser(String loginEmail, TenantRegister tenant, RequestCallback requestCallback) throws EwpException {
        DeviceInfoData deviceInfoData = new DeviceInfoData();
        DeviceInfo deviceInfo = deviceInfoData.getUserInfoAsEntity();
        if (deviceInfo == null) {
            throw new EwpException("");
        }
        // update the user token.
        TenantUserDataService tenantUserService = new TenantUserDataService();
        tenantUserService.updateUserToken(tenant.getUserId(), tenant.getLoginToken());
        // If tenant ids are different then re init data.
        if (deviceInfo.getTenantId().toString() != tenant.getUserId().toString()) {
            initializeDataForTenant(requestCallback);
        } else {
            // Need to change token.
            updateDeviceInfoAndSessionVariables(tenant);
        }
        // If tenant ids are equal but initialization process is in-complete then complete it.
        if ((!deviceInfo.isInitialized()) && deviceInfo.getTenantId().equals(tenant.getTenantId())) {
            initializeDataForTenant(deviceInfo.getLastSyncCount(), initialChuckSizeByte, requestCallback, true);
            //   endProcess = false;
        }
        // Updating the user.
        deviceInfo.setUserId(tenant.getUserId());
        deviceInfoData.update(deviceInfo);
        EwpSession.getSharedInstance().setSession(UUID.fromString(tenantId), tenant.getUserId(), "", tenant.getLoginToken());
        EwpSession.getSharedInstance().refresh();
    }

    /**
     * update device info and session variables
     *
     * @param obj
     * @throws EwpException
     */
    private void updateDeviceInfoAndSessionVariables(TenantRegister obj) throws EwpException {
        DeviceInfoData deviceInfoData = new DeviceInfoData();
        List<DeviceInfo> entityList = deviceInfoData.getList();
        if (!entityList.isEmpty()) {
            DeviceInfo deviceInfo = entityList.get(0);
            // If tenant data is exist with different user id then update user id in DeviceInfo table.
            if (!obj.getUserId().equals(deviceInfo.getUserId())) {
                deviceInfo.setUserId(obj.getUserId());
                deviceInfoData.update(deviceInfo);
                // UPDATE the session variables.
                EwpSession.getSharedInstance().setSession(obj.getTenantId(), obj.getUserId(), obj.getName(), obj.getLoginToken());
                // Resetting the session.
                EwpSession.getSharedInstance().refresh();
            }
        }
    }

    /**
     * initialize data for tenant
     *
     * @param lastSentTransRowNumber
     * @param chunkSizeInBytes
     * @param requestCallback
     * @param initHandler
     * @throws EwpException
     */
    public void initializeDataForTenant(int lastSentTransRowNumber, int chunkSizeInBytes, final RequestCallback requestCallback, boolean initHandler) throws EwpException {
        RestService restService = new RestService();
        SyncService service = new SyncService();
        // get the device id
        String myDeviceId = service.getDataProvider().getMyDeviceId();
        AppConfigData appConfigData = new AppConfigData();
        AppConfig appConfig = appConfigData.getAppConfig();
        String appVersion = appConfig.getApplicationSuiteVersion();
        String dbVersion = appConfig.getDatabaseVersion();
        // USER is member of multiple tenant
        String urlString = restService.serverUrlString() + "inittenantappdata";
        AjaxCallback<XmlPullParser> callback = new AjaxCallback<XmlPullParser>() {
            @Override
            public void callback(String url, XmlPullParser object, AjaxStatus status) {
                super.callback(url, object, status);
                try {
                    if (status.getCode() == 200 && object != null) {
                        SyncRequest syncRequest = SyncRequest.parseXml(object);
                        processChunkData(syncRequest, requestCallback);
                    } else {
                        String error = "";
                        EwpException ex = EwpException.handleError(null, status);
                        if (ex != null && ex.message != null && ex.message.size() > 0)
                            error = ex.message.get(0);
                        requestCallback.onFailure("InitTenantAppData", error);
                    }
                } catch (Exception e) {
                    requestCallback.onFailure("InitTenantAppData", "" + e);
                    Log.d(TAG, "Exception Occurred" + e);
                }
            }
        };
        callback.url(urlString);
        // ADD the tenantId in HttpHeader field
        callback.type(XmlPullParser.class);
        callback.header(Commons.ACTION, "InitTenantAppData");
        callback.header(Commons.REQ_DEVICE_ID, myDeviceId);
        callback.header(Commons.CHUNK_SIZE_IN_BYTES, String.valueOf(chunkSizeInBytes));
        callback.header(Commons.LAST_SENT_TRANS_ROW_NUM, String.valueOf(lastSentTransRowNumber));
        callback.header(Commons.LOGIN_TOKEN, selectedTenant.getLoginToken());
        callback.header(Commons.TENANT_ID, selectedTenant.getTenantId().toString());
        callback.header("AppId", EwpSession.EMPLOYEE_APPLICATION_ID);
        callback.header("UserId", EwpSession.getSharedInstance().getUserId().toString());
        callback.header(Commons.APP_SUITE_VERSION, appVersion);
        callback.header(Commons.DB_VERSION, dbVersion);
        callback.method(AQuery.METHOD_GET);
        callback.timeout(600000);
        log.info("initialization params----" + callback.toString());
        AQuery aQuery = new AQuery(ContextHelper.getContext());
        aQuery.ajax(callback);
        Log.d(this.getClass().getName(), "Process to get chunk data");
    }

    /**
     * process data after getting sync request data
     *
     * @param syncRequest
     * @param requestCallback
     */
    private void processChunkData(SyncRequest syncRequest, RequestCallback requestCallback) {
        try {
            SyncService syncService = new SyncService();
            SyncReply syncReply = syncService.processRemoteRequest(syncRequest);
            if (syncReply == null) {
                requestCallback.onFailure("InitTenantAppData", AppMessage.INITIALIZATION_FAILED);
                return;
            }
            if (syncRequest.isInitCompleted()) {
                // initialization completed set progress to 100
                requestCallback.updateProgress("InitTenantAppData", "Downloading Database Completed", 100);
                DeviceInfoData deviceInfoData = new DeviceInfoData();
                RestService restService = new RestService();
                // Method is used to set the initialization process is completed.
                deviceInfoData.updateDeviceInitilizationCompleted(restService.getAppConfig().getClientDeviceId());
                requestCallback.onSuccess("InitTenantAppData", null);
                new SyncNetworkService().addUpdateHistory(false, "InitTenantAppData", syncRequest, syncReply, "", null);
                EwpSession.getSharedInstance().setSession(selectedTenant.getTenantId(), selectedTenant.getUserId(), "", selectedTenant.getLoginToken());
                EwpSession.getSharedInstance().refresh();
            } else if (syncReply.getStatus() == EnumsForExceptions.ErrorType.SUCCESS.getId()) {
                if (lastPercent == 0) {
                    lastPercent += 10;
                    requestCallback.updateProgress("InitTenantAppData", "Downloading Database", 10);
                } else {
                    if (lastPercent > 90 && lastPercent < 98) {
                        lastPercent = lastPercent + 1;
                        requestCallback.updateProgress("InitTenantAppData", "Downloading Database", lastPercent);
                    } else if (lastPercent < 98) {
                        lastPercent = lastPercent + 3;
                        requestCallback.updateProgress("InitTenantAppData", "Downloading Database", lastPercent);
                    }
                }
                int checkSizeInByte = initialChuckSizeByte;
                DeviceInfoData deviceInfoData = new DeviceInfoData();
                int lastSyncItemNumber = 0;
                if (syncRequest.getSyncTransactionList().isEmpty()) {
                    DeviceInfoData deviceInfoData1 = new DeviceInfoData();
                    DeviceInfo deviceInfo = deviceInfoData1.getUserInfoAsEntity();
                    if (deviceInfo != null) {
                        lastSyncItemNumber = deviceInfo.getLastSyncCount();
                        checkSizeInByte = initialChuckSizeByte + chunkByteSizeAddedBy;
                        chunkByteSizeAddedBy += 100000;
                        if (chunkByteSizeAddedBy == 50000000) {
                            requestCallback.onFailure("InitTenantAppData", AppMessage.INITIALIZATION_FAILED);
                        }
                    }
                } else {
                    lastSyncItemNumber = syncRequest.getSyncTransactionList().get(syncRequest.getSyncTransactionList().size() - 1).getSyncRowNumber();
                    chunkByteSizeAddedBy = 100000;
                }
                RestService restService = new RestService();
                // Method is used to update the last sync transaction number.
                boolean isUpdated = deviceInfoData.updateDeviceLastSyncCount(restService.getAppConfig().getClientDeviceId(), lastSyncItemNumber);
                if (isUpdated) {
                    // Getting the data from next lastSyncItemNumber
                    initializeDataForTenant(lastSyncItemNumber, checkSizeInByte, requestCallback, isUpdated);
                    new SyncNetworkService().addUpdateHistory(false, "InitTenantAppData", syncRequest, syncReply, "", null);
                } else {
                    // Method is used to update the last sync transaction number.
                    // boolean lastSyncCount = deviceInfoData.updateDeviceLastSyncCount(restService.appConfig.getClientDeviceId(), syncReply.getLastSyncRowNumber());
                    new SyncNetworkService().addUpdateHistory(false, "InitTenantAppData", syncRequest, syncReply, "", null);
                    Log.d(this.getClass().getName(), "Last SYNC Count not updated from processChunkData()");
                    requestCallback.onFailure("InitTenantAppData", AppMessage.INITIALIZATION_FAILED);
                }
            } else {
                requestCallback.onFailure("InitTenantAppData", AppMessage.INITIALIZATION_FAILED);
                Log.d(this.getClass().getName(), "Last SYNC Count not updated from processChunkData()");
            }
        } catch (EwpException ex) {
            requestCallback.onFailure("InitTenantAppData", "" + ex);
        }
    }

    public void setSelectedTenant(TenantRegister selectedTenant) {
        this.selectedTenant = selectedTenant;
    }
}
