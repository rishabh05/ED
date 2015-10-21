//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/28/2015
//===============================================================================
package com.eworkplaceapps.platform.dbsync;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.eworkplaceapps.platform.commons.Commons;
import com.eworkplaceapps.platform.context.ContextHelper;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.AppConfig;
import com.eworkplaceapps.platform.helper.AppConfigData;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.logging.LogConfigurer;
import com.eworkplaceapps.platform.requesthandler.RequestCallback;
import com.eworkplaceapps.platform.requesthandler.ScreenRefreshListener;
import com.eworkplaceapps.platform.utils.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

/**
 * This class provides higher level network service for sync process.
 * It implements complete control flow to sync with the server.
 * There are two main control flows:
 * (1) Client sends its data to the server for syncing.
 * (2) Client requests data from the server to sync with.
 * Note that both control flows are initiated from the client end.
 * Server needs to just respond correctly.
 * <p/>
 * The control flow in each case is broken down in to several unit steps.
 * The caller must call the steps in the right sequence.
 * The steps are designed to manage the asynchronous calls to the server
 * in custom handlers provided by the caller.
 * The caller of this service may be either the user initiating sync, or
 * the interval timer tick initiating sync. Or some other method.
 */
public class SyncNetworkService {

    // Save server response data for use by the caller.
    // Used between various control flow steps as! described below.
    private String serverResponseData;
    private UUID syncHistoryId = Utils.emptyUUID();
    private final Logger log = Logger.getLogger(SyncNetworkService.class);
    public static  String timeZoneName = "";

    public SyncNetworkService() {
    }

    /**
     * sync my data with server 1
     *
     * @throws EwpException
     * @throws IOException
     */
    public void syncMyDataWithServer_Part1(RequestCallback requestCallback) throws EwpException, IOException {
        new SendRequestToSync().execute(requestCallback);
    }

    /**
     * sync my data with server part 2
     *
     * @param object
     * @throws EwpException
     * @throws IOException
     */
    public void syncMyDataWithServer_Part2(XmlPullParser object) throws EwpException, IOException {
        // Parse server's reply
        SyncReply syncReply = SyncReply.parseXml(object);
        // Resolve conflicts
        SyncService service = new SyncService();
        service.processRemoteReply(syncReply);
        Log.d(this.getClass().getName(), "Conflict count: " + syncReply.getConflictList().size());
        log.info("Conflict count: " + syncReply.getConflictList().size());
        XmlSerializer xmlSerializer = new KXmlSerializer();
        StringWriter writer = new StringWriter();
        xmlSerializer.setOutput(writer);
        SyncReply.toXmlWriter(xmlSerializer, syncReply);
        xmlSerializer.endDocument();
        String writerStr = writer.toString();
        Log.d(this.getClass().getSimpleName(), "************************ClientData Receive Start*************************");
        Log.d(this.getClass().getSimpleName(), writerStr);
        log.info(writerStr);
        log.info("************************ClientData Receive End*************************");
        if (syncReply != null) {
            addUpdateHistory(false, "ClientData", null, syncReply, serverResponseData, writerStr);
        } else {
            addUpdateHistory(false, "ClientData", null, syncReply, serverResponseData, null);
        }
    }


    /**
     * @return
     * @throws EwpException
     */
    public SyncRequest getSyncRequest() throws EwpException {
        // Setup request data.
        SyncService service = new SyncService();
        String serverDeviceId = service.getDataProvider().getServerDeviceId();
        String myDeviceId = service.getDataProvider().getMyDeviceId();
        Date lastSendTimeToServer = service.getDataProvider().lastSendTime(serverDeviceId);
        Date currentTime = new Date();
        // Generate request
        // result has! 2 value: syncRequest and error.
        SyncRequest syncRequest = service.generateRemoteRequest(myDeviceId, lastSendTimeToServer, currentTime);
        return syncRequest;
    }


    /**
     * actual task for ClientData request
     */
    class SendRequestToSync extends AsyncTask<RequestCallback, Void, Void> {


        @Override
        protected Void doInBackground(RequestCallback... params) {
            URL url = null;
            HttpResponse response = null;
            HttpPost httpPost = null;
            HttpClient httpClient = null;
            try {
                RestService restService = new RestService();
                String urlString = restService.serverUrlString() + "clientdata";
                SyncService service = new SyncService();
                String myDeviceId = service.getDataProvider().getMyDeviceId();
                SyncRequest syncRequest = getSyncRequest();
                if (syncRequest == null) {
                    return null;
                }
                // url = new URL("http://ewp-dev39.eworkplace.com:84/api/ED/Employee/AddEmployees");
                url = new URL(urlString);
                httpClient = new DefaultHttpClient();
                httpPost = new HttpPost(url.toURI());

                XmlSerializer xmlSerializer = new KXmlSerializer();
                StringWriter writer = new StringWriter();
                xmlSerializer.setOutput(writer);
                SyncRequest.toXmlWriter(xmlSerializer, syncRequest);
                xmlSerializer.endDocument();
                String data = writer.toString();
                httpPost.setEntity(new StringEntity(data, "UTF-8"));
                httpPost.setHeader("Content-Type", "application/xml");
                httpPost.setHeader("Accept", "application/xml");
                httpPost.setHeader(Commons.ACTION, "ClientData");
                httpPost.setHeader(Commons.REQ_DEVICE_ID, myDeviceId);
                httpPost.setHeader(Commons.LOGIN_TOKEN, EwpSession.getSharedInstance().getLoginToken());
                // Execute POST
                response = httpClient.execute(httpPost);
                Log.d(this.getClass().getSimpleName(), "*************************ClientData Start**********************");
                log.info("*************************ClientData Start**********************");
                Log.d(this.getClass().getSimpleName(), data);
                log.info(data);
                Log.d(this.getClass().getSimpleName(), "***********************ClientData End**********************");
                log.info("**********************ClientData End********************");
                if (response.getStatusLine().getStatusCode() == 200) {
                    String out = Utils.convertResponseToString(response);
                    if (out != null) {
                        params[0].onSuccess("ClientData", out);
                    }
                } else {
                    String error = "";
                    EwpException ex = EwpException.handleError(response, null);
                    if (ex != null && ex.message != null && ex.message.size() > 0)
                        error = ex.message.get(0);
                    params[0].onFailure("ClientData", error);
                }

            } catch (EwpException e) {
                params[0].onFailure("ClientData", "" + e);
                Log.d(this.getClass().getName(), "ClientData onFailure " + e);
                log.info("ClientData onFailure " + e);
            } catch (UnsupportedEncodingException e) {
                params[0].onFailure("ClientData", "" + e);
                Log.d(this.getClass().getName(), "ClientData onFailure " + e);
                log.info("ClientData onFailure " + e);
            } catch (IOException e) {
                params[0].onFailure("ClientData", "" + e);
                Log.d(this.getClass().getName(), "ClientData onFailure " + e);
                log.info("ClientData onFailure " + e);
            } catch (URISyntaxException e) {
                params[0].onFailure("ClientData", "" + e);
                Log.d(this.getClass().getName(), "ClientData onFailure " + e);
                log.info("ClientData onFailure " + e);
            } catch (Exception e) {
                params[0].onFailure("ClientData", "" + e);
                Log.d(this.getClass().getName(), "ClientData onFailure " + e);
                log.info("ClientData onFailure " + e);
            }
            return null;
        }
    }

    // -------------------- SYNC With Server DATA --------------------
    // This control flow executes the following operations:
    // (1) Send a request to the server to send its data for syncing.
    // (2) Wait for the server response to (1)
    // (3) GET server data and sync with our data.
    // (4) Generate SyncReply to (3) with conflicts if any.
    // (5) Send SyncReply to server.
    // (6) Wait for server to respond.
    // (7) Process server response for any errors.
    // Part1 of flow includes Steps 1.
    // Part2 of flow includes Step 3, 4, and 5.
    // Part3 of flow includes Steps 7.

    /**
     * sync with server 1
     *
     * @throws EwpException
     */
    public void syncWithServerData_Part1(RequestCallback requestCallback, ScreenRefreshListener screenRefreshListener) throws EwpException {
        // Send request to server
        sendRequestToGetData(requestCallback, screenRefreshListener);
    }

    /**
     * sync with server 2
     *
     * @throws EwpException
     * @throws IOException
     */
    public void syncWithServerData_Part2(XmlPullParser object, RequestCallback requestCallback, ScreenRefreshListener screenRefreshListener) throws EwpException, IOException {
        // SYNC with server data
        SyncReply syncReply = syncWithServerResponseData(object, screenRefreshListener);
        if (syncReply != null) {
            // Send SyncReply to server
            new SendReplyForServerDataSync(syncReply).execute(requestCallback);
            // sendReplyForServerDataSync(syncReply, requestCallback);
        } else {
            SyncReply reply = new SyncReply();
            reply.setStatus(1);
            requestCallback.onFailure("ServerData", null);
        }
    }


    /**
     * Send HTTP request to server for its data. Action: "GetSyncData"
     *
     * @throws EwpException
     */
    public void sendRequestToGetData(final RequestCallback requestCallback, final ScreenRefreshListener screenRefreshListener) throws EwpException {
        // Note that this a GET request.
        RestService restService = new RestService();
        String urlString = restService.serverUrlString() + "serverdata";
        SyncService service = new SyncService();
        String myDeviceId = service.getDataProvider().getMyDeviceId();
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
                        Log.d(this.getClass().getSimpleName(), "******************Server Data Success**************");
                        log.info("******************Server Data Success**************");
                        syncWithServerData_Part2(object, requestCallback, screenRefreshListener);
                    } else {
                        String error = "";
                        EwpException ex = EwpException.handleError(null, status);
                        if (ex != null && ex.message != null && ex.message.size() > 0)
                            error = ex.message.get(0);
                        requestCallback.onFailure("ServerData", error+"   status=  "+status.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    requestCallback.onFailure("ServerData", "" + e);
                    Log.d(this.getClass().getName(), "Exception Occurred" + e);
                    LogConfigurer.info(this.getClass().getName(), "Exception Occurred" + e);
                }
            }
        };
        callback.url(urlString);
        callback.type(XmlPullParser.class);
        callback.header(Commons.ACTION, "ServerData");
        callback.header(Commons.LOGIN_TOKEN, EwpSession.getSharedInstance().getLoginToken());
        callback.header(Commons.REQ_DEVICE_ID, myDeviceId);
        callback.header(Commons.APP_SUITE_VERSION, appVersion);
        callback.header(Commons.DB_VERSION, dbVersion);
        callback.method(AQuery.METHOD_GET);
        AQuery aQuery = new AQuery(ContextHelper.getContext());
        aQuery.ajax(callback);
    }

    /**
     * sync With Server Response DATA
     *
     * @param object
     * @return
     * @throws EwpException
     */
    public SyncReply syncWithServerResponseData(XmlPullParser object, ScreenRefreshListener screenRefreshListener) throws EwpException {
        // Create SyncRequest from the server response to GetSyncData request.
        SyncRequest syncRequest = SyncRequest.parseXml(object);
        // SYNC with our (client) data and get SyncReply to syncing.
        SyncService service = new SyncService();
        SyncReply syncReply = service.processRemoteRequest(syncRequest);
        if (syncReply == null) {
            Log.d(this.getClass().getName(), "SyncReply is null syncWithServerResponseData()");
            LogConfigurer.info(this.getClass().getName(), "SyncReply is null syncWithServerResponseData()");
        }
        if (syncRequest != null && !syncRequest.getSyncTransactionList().isEmpty()) {
            if (screenRefreshListener != null)
                screenRefreshListener.onDataArrivalInSync(syncRequest);
        }
        addUpdateHistory(false, "ServerData", syncRequest, null, null, null);
        return syncReply;
    }


    /**
     * actual task to call resolveClientConflicts call
     */
    private class SendReplyForServerDataSync extends AsyncTask<RequestCallback, Void, Void> {
        private SyncReply syncReply;

        public SendReplyForServerDataSync(SyncReply syncReply) {
            this.syncReply = syncReply;
        }

        @Override
        protected Void doInBackground(RequestCallback... params) {
            URL url = null;
            HttpResponse response = null;
            HttpPost httpPost = null;
            HttpClient httpClient = null;
            try {
                RestService restService = new RestService();
                String urlString = restService.serverUrlString() + "resolveclientconflicts";
                SyncService service = new SyncService();
                String myDeviceId = service.getDataProvider().getMyDeviceId();
                syncReply.setMyDeviceId(myDeviceId);

                // url = new URL("http://ewp-dev39.eworkplace.com:84/api/ED/Employee/AddEmployees");
                url = new URL(urlString);
                httpClient = new DefaultHttpClient();
                httpPost = new HttpPost(url.toURI());

                XmlSerializer xmlSerializer =new KXmlSerializer();
                StringWriter writer = new StringWriter();
                xmlSerializer.setOutput(writer);
                SyncReply.toXmlWriter(xmlSerializer, syncReply);
                xmlSerializer.endDocument();
                String writerStr = writer.toString();
                httpPost.setEntity(new StringEntity(writerStr, "UTF-8"));
                httpPost.setHeader("Content-Type", "application/xml");
                httpPost.setHeader("Accept", "application/xml");
                httpPost.setHeader("RequesterDeviceName", "Android");
                httpPost.setHeader(Commons.ACTION, "ResolveClientConflicts");
                httpPost.setHeader(Commons.REQ_DEVICE_ID, myDeviceId);
                httpPost.setHeader(Commons.LOGIN_TOKEN, EwpSession.getSharedInstance().getLoginToken());
                // Execute POST
                response = httpClient.execute(httpPost);
                Log.d(this.getClass().getSimpleName(), "*********************************Sending ResolveClientConflicts Data Start*************************");
                log.info("**********************************Sending ResolveClientConflicts Data Start***********************************");
                Log.d(this.getClass().getSimpleName(), writerStr);
                log.info(writerStr);
                Log.d(this.getClass().getSimpleName(), "******************************Sending ResolveClientConflicts Data End**************************");
                log.info("******************************Sending ResolveClientConflicts Data End**************************");
                if (response.getStatusLine().getStatusCode() == 200) {
                    String out = Utils.convertResponseToString(response);
                    if (out != null) {
                        params[0].onSuccess("ServerData", out);
                    }
                } else {
                    String error = "";
                    EwpException ex = EwpException.handleError(response, null);
                    if (ex != null && ex.message != null && ex.message.size() > 0)
                        error = ex.message.get(0);
                    params[0].onFailure("ServerData", error);
                }
            } catch (EwpException e) {
                params[0].onFailure("ServerData", "" + e);
                Log.d(this.getClass().getSimpleName(), "resolveclientconflicts onFailure " + e);
                log.info("resolveclientconflicts onFailure " + e);
            } catch (UnsupportedEncodingException e) {
                params[0].onFailure("ServerData", "" + e);
                Log.d(this.getClass().getSimpleName(), "resolveclientconflicts onFailure " + e);
                log.info("resolveclientconflicts onFailure " + e);
            } catch (IOException e) {
                params[0].onFailure("ServerData", "" + e);
                Log.d(this.getClass().getSimpleName(), "resolveclientconflicts onFailure " + e);
                log.info("resolveclientconflicts onFailure " + e);
            } catch (URISyntaxException e) {
                params[0].onFailure("ServerData", "" + e);
                Log.d(this.getClass().getSimpleName(), "resolveclientconflicts onFailure " + e);
                log.info("resolveclientconflicts onFailure " + e);
            } catch (Exception e) {
                params[0].onFailure("ServerData", "" + e);
                Log.d(this.getClass().getSimpleName(), "resolveclientconflicts onFailure " + e);
                log.info("resolveclientconflicts onFailure " + e);
            }
            return null;
        }
    }

    /**
     * add update history
     *
     * @param isAdd
     * @param action
     * @param syncRequest
     * @param syncReply
     * @param syncReplyData
     * @param syncRequestData
     * @throws EwpException
     */
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
        history.setStatusCode(String.valueOf(0));
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
            history.setActualAfterSyncTime(syncRequest.getAfterSyncTime());
            history.setActualToSyncTime(syncRequest.getToSyncTime());
            history.setNumTrans(syncRequest.getSyncTransactionList().size());
            int syncOpCount = 0;
            for (int i = 0; i < syncRequest.getSyncTransactionList().size(); i++) {
                if (!syncRequest.getSyncTransactionList().get(i).getSyncOpList().isEmpty()) {
                    syncOpCount += syncRequest.getSyncTransactionList().get(i).getSyncOpList().size();
                }
            }
            history.setNumOps(syncOpCount);
        }
        if (isAdd) {
            history.setActionStartTime(new Date());
            history.setAction(action);
            Object resultTuple = syncHistoryData.add(history);
            if (resultTuple != null) {
                syncHistoryId = (UUID) resultTuple;
            }
        } else {
            history.setActionEndTime(new Date());
            syncHistoryData.update(history);
        }
    }

    /**
     * This method is use to update new Timezone at server.if TimeZone successfully updated at server then we save it to local store.
     * @param requestCallback
     * @throws EwpException
     */
    public  void timeZoneChange(final RequestCallback requestCallback, double lat, double lng,String region )throws EwpException{
        String timeZone = TimeZone.getDefault().getID();
        RestService restService = new RestService();
        String strUrl = restService.getAuthenticateServerUrlString() + "tenantusers/updateTimeZone";
        if ("".equalsIgnoreCase(timeZoneName) && EwpSession.isTimeZoneChanged(timeZone)) {
            timeZoneName = timeZone;
            JSONObject locAddressDictArray = new JSONObject();
            try {
                locAddressDictArray.put("UserId", EwpSession.getSharedInstance().getUserId().toString());
                locAddressDictArray.put("Latitude", String.valueOf(lat));
                locAddressDictArray.put("Longitude",String.valueOf(lng));
                locAddressDictArray.put("IANATimeZoneId",timeZoneName);
                locAddressDictArray.put("Region", "US");

            } catch (JSONException e) {
                e.printStackTrace();
                requestCallback.onFailure("TimeZoneChange", "");
            }
            new NetworkAsyncTask(strUrl, "PUT", "TimeZoneChange", locAddressDictArray.toString()).execute(requestCallback);
        }
    }

    class NetworkAsyncTask extends AsyncTask<RequestCallback, Void, Void> {

        private String url = "";
        private String httpMethodType = "";
        private String methodName = "";
        private String jsonString = "";

        public NetworkAsyncTask(String url, String httpMethodType, String methodName, String jsonString) {
            this.url = url;
            this.httpMethodType = httpMethodType;
            this.methodName = methodName;
            this.jsonString = jsonString;
        }

        @Override
        protected Void doInBackground(RequestCallback... params) {
            switch (httpMethodType) {
                case "PUT":
                    callHttpPutMethodBackground(params);
                    break;
            }
            return null;
        }


        private Void callHttpPutMethodBackground(RequestCallback... params) {
            URL url = null;
            HttpResponse response = null;
            HttpPut httpPut = null;
            HttpClient httpClient = null;


            try {
                url = new URL(this.url);
                httpClient = new DefaultHttpClient();
                httpPut = new HttpPut(url.toURI());
                httpPut.setEntity(new StringEntity(jsonString, "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
                params[0].onFailure(methodName, e.getMessage());
            }

            // Set up the header types needed to properly transfer JSON
            httpPut.setHeader("Content-Type", "application/json");
            httpPut.setHeader("Accept", "application/json");
            httpPut.setHeader(Commons.LOGIN_TOKEN, EwpSession.getSharedInstance().getLoginToken());

            try {
                response = httpClient.execute(httpPut);
            } catch (IOException e) {
                e.printStackTrace();
                params[0].onFailure(methodName, e.getMessage());
            }

            try {
                if (response.getStatusLine().getStatusCode() == 200) {
                    String out = Utils.convertResponseToString(response);
                    params[0].onSuccess(methodName, "");
                } else {
                    String error = "";
                    if (response.getStatusLine().getStatusCode() == 500) {
                        EwpException ex = EwpException.handleError(response, null);
                        if (ex != null && ex.message != null && ex.message.size() > 0)
                            error = ex.message.get(0);
                    } else {
                        EwpException ex = new EwpException();
                        ex.localizedMessage = response.getStatusLine().getReasonPhrase();
                        error = response.getStatusLine().getReasonPhrase();
                    }
                    params[0].onFailure(methodName, error);
                }
            } catch (Exception e) {
                e.printStackTrace();
                params[0].onFailure(methodName,e.toString());

            }
            return null;
        }
    }

}
