//===============================================================================
// copyright 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 7/23/2015
//===============================================================================
package com.eworkplaceapps.platform.dbsync;

import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.eworkplaceapps.platform.commons.Commons;
import com.eworkplaceapps.platform.context.ContextHelper;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.helper.AppConfig;
import com.eworkplaceapps.platform.helper.AppConfigData;
import com.eworkplaceapps.platform.helper.DeviceInfo;
import com.eworkplaceapps.platform.helper.DeviceInfoData;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.requesthandler.RequestCallback;
import com.eworkplaceapps.platform.tenant.TenantRegister;
import com.eworkplaceapps.platform.utils.AppMessage;

import org.xmlpull.v1.XmlPullParser;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * This class provides higher level network service for init chat process.
 * It implements complete control flow to init with the server.
 * There are two main control flows:
 * (1) Client sends its data to the server for syncing.
 * (2) Client requests data from the server to sync with.
 * Note that both control flows are initiated from the client end.
 * Server needs to just respond correctly.
 * <p/>
 * The control flow in each case is broken down in to several unit steps.
 * The caller must call the steps in the right sequence.
 * The steps are designed to manage the asynchronus calls to the server
 * in custom handlers provided by the caller.
 * <p/>
 * The caller of this service may be either the user initiating sync, or
 * the interval timer tick initiating sync. Or some other method.
 */
public class ChatSyncNetworkService {
    private Date startDate = new Date();
    // Save server response data for use by the caller.
    // Used between various control flow steps as! described below.
    private String serverResponseData;
    private RestService restService;
    private UUID syncHistoryId;
    private TenantRegister selectedTenant;
    private SyncService service;
    private int initialChuckSizeByte = 100000;
    private int lastPercent = 0;
    private Date clientCallTime = new Date();
    private int totalClientProcessingTime = 0;
    private int totalNetworkClientProcessingTime = 0;
    private int intPacketNo = 1;
    private boolean firstTime = true;
    private int totalSyncRows = 0;
    private int chunkByteSizeAddedBy = 100000;
    // It is used to log, How many bytes we got from server at the time of initilization.
    private int totalBytes = 0;

    public ChatSyncNetworkService() {
    }

    public ChatSyncNetworkService(RestService restService, SyncService service, TenantRegister selectedTenant) {
        this.selectedTenant = selectedTenant;
        this.service = service;
        this.restService = restService;
    }

    public void initializeDataForTenant(RequestCallback requestCallback) throws EwpException {
        if (selectedTenant == null) {
            requestCallback.onFailure("ChatInit", null);
            return;
        }
        initializeDataForTenant(0, initialChuckSizeByte, requestCallback);
    }

    public void initializeDataForTenant(int lastSentTransRowNumber, int chunkSizeInBytes, final RequestCallback requestCallback) throws EwpException {
        String myDeviceId = restService.getAppConfig().getClientDeviceId();
        String urlString = restService.serverChatUrlString() + "InitTenantAppData";// "GetInitDataForTenant"
        AppConfigData appConfigData = new AppConfigData();
        AppConfig appConfig = appConfigData.getAppConfig();
        String appVersion = appConfig.getApplicationSuiteVersion();
        String dbVersion = appConfig.getDatabaseVersion();
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
                        requestCallback.onFailure("ChatInit", error);
                    }
                } catch (Exception e) {
                    requestCallback.onFailure("ChatInit", "" + e);
                    Log.d("ChatSyncNetworkService", "Exception Occurred" + e);
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
        callback.header("UserId", selectedTenant.getUserId().toString());
        callback.header("AppId", EwpSession.PLATFORM_APPLICATION_ID);
        callback.header(Commons.APP_SUITE_VERSION, appVersion);
        callback.header(Commons.DB_VERSION, dbVersion);
        callback.method(AQuery.METHOD_GET);
        callback.timeout(600000);
        AQuery aQuery = new AQuery(ContextHelper.getContext());
        aQuery.ajax(callback);
        clientCallTime = new Date();
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
                requestCallback.onFailure("ChatInit", AppMessage.INITIALIZATION_FAILED);
                return;
            }
            if (syncRequest.isInitCompleted()) {
                DeviceInfoData deviceInfoData = new DeviceInfoData();
                RestService restService = new RestService();
                // Method is used to set the initialization process is completed.
                deviceInfoData.updateDeviceInitilizationCompleted(restService.getAppConfig().getClientDeviceId());
                requestCallback.onSuccess("ChatInit", null);
                new SyncNetworkService().addUpdateHistory(false, "ChatInit", syncRequest, syncReply, "", null);
                EwpSession.getSharedInstance().setSession(selectedTenant.getTenantId(), selectedTenant.getUserId(), "", selectedTenant.getLoginToken());
                EwpSession.getSharedInstance().refresh();
            } else if (syncReply.getStatus() == EnumsForExceptions.ErrorType.SUCCESS.getId()) {
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
                            requestCallback.onFailure("ChatInit", AppMessage.INITIALIZATION_FAILED);
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
                    initializeDataForTenant(lastSyncItemNumber, checkSizeInByte, requestCallback);
                    new SyncNetworkService().addUpdateHistory(false, "ChatInit", syncRequest, syncReply, "", null);
                } else {
                    // Method is used to update the last sync transaction number.
                    // boolean lastSyncCount = deviceInfoData.updateDeviceLastSyncCount(restService.appConfig.getClientDeviceId(), syncReply.getLastSyncRowNumber());
                    new SyncNetworkService().addUpdateHistory(false, "ChatInit", syncRequest, syncReply, "", null);
                    Log.d(this.getClass().getName(), "Last SYNC Count not updated from processChunkData()");
                    requestCallback.onFailure("ChatInit", AppMessage.INITIALIZATION_FAILED);
                }
            } else {
                requestCallback.onFailure("ChatInit", AppMessage.INITIALIZATION_FAILED);
                Log.d(this.getClass().getName(), "Last SYNC Count not updated from processChunkData()");
            }
        } catch (EwpException ex) {
            requestCallback.onFailure("ChatInit", "" + ex);
        }
    }


    public int logTime(String preMsg, String msg, Date startTime) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int second = calendar.get(Calendar.MINUTE) * 60;
        second += calendar.get(Calendar.SECOND);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(startTime);
        String format = getFormattedTime(second - ((calendar.get(Calendar.MINUTE) * 60) + cal2.get(Calendar.SECOND)));
        Log.d("ChatSyncNetworkService", preMsg + ", " + msg + format);
        return second;
    }

    public String getFormattedTime(int second) {
        int min = second / 60;
        second = second - (min * 60);
        return min + ":" + second + ".000";
    }

    public void addUpdateHistory(boolean isAdd, String action, SyncRequest syncRequest, SyncReply syncReply, String syncReplyData, String syncRequestData) throws EwpException {
        SyncHistoryData syncHistoryData = new SyncHistoryData();
        SyncHistory history = new SyncHistory();

        if (!isAdd) {
            if (syncHistoryId == null) {
                return;
            }
            SyncHistory result = syncHistoryData.getEntity(syncHistoryId);
            if (result == null) {
                return;
            }
            history = result;
        }
        history.setStatusCode("0");
        history.setStatusMessage("OK");
        if (syncReplyData != null) {
            history.setSyncReplyData(syncReplyData);
        }
        if (syncRequestData != null) {
            history.setSyncRequestData(syncRequestData);
        }

        if (syncReply != null) {
            history.setActualAfterSyncTime(syncReply.getAfterSyncTime());
            history.setActualToSyncTime(syncReply.getToSyncTime());
            history.setConflicts(syncReply.getConflictList().isEmpty());
            history.setStatusCode(syncReply.getStatus() == 0 ? "0" : "1");
            history.setStatusMessage(syncReply.getMessage());
            history.setActualAfterSyncTime(syncReply.getAfterSyncTime());
            history.setActualToSyncTime(syncReply.getToSyncTime());
            history.setPartial(syncReply.isPartialSync());
        }
        if (syncRequest != null) {
            history.setUpdatedAt(syncRequest.getAfterSyncTime());
            history.setActualToSyncTime(syncRequest.getToSyncTime());
            history.setNumTrans(syncRequest.getSyncTransactionList().size());
            int syncOpCount = 0;
            for (int i = 0; i < syncRequest.getSyncTransactionList().size(); i++) {
                if (syncRequest.getSyncTransactionList().get(i).getSyncOpList() != null) {
                    syncOpCount += syncRequest.getSyncTransactionList().get(i).getSyncOpList().size();
                }
            }
            history.setNumOps(syncOpCount);
        }

        if (isAdd) {
            history.setActionStartTime(new Date());
            history.setAction(action);
            Object val = syncHistoryData.add(history);
            if (val != null) {
                syncHistoryId = (UUID) val;
            }
        } else {
            history.setActionEndTime(new Date());
            syncHistoryData.update(history);
        }
    }
}
