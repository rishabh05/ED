//===============================================================================
// copyright 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Sourabh Agrawal
// Original DATE: 09/18/2015
//===============================================================================

package com.eworkplaceapps.platform.dbsync;

import android.os.AsyncTask;
import android.util.Log;

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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
public class AutomaticSyncChatData {
    private String serverResponseData;
    ChatSyncNetworkService chatSyncNetworkService;
    SyncService service;
    AppConfig appConfig;
    public AutomaticSyncChatData() throws EwpException {
        chatSyncNetworkService = new ChatSyncNetworkService();
        appConfig = new AppConfigData().getAppConfig();
        service = new SyncService(new SyncChatSqlSupport(),appConfig);
    }

    /**
     *
      * @param requestCallback
     * @throws EwpException
     * @throws IOException
     */
    public void syncMyChatDataWithServer_Part1_AutomaticSync(RequestCallback requestCallback) throws EwpException, IOException {
        new sendRequestToSyncChatData().execute(requestCallback);
    }

    /**
     * This function is to get sync request
     * @return
     * @throws EwpException
     */
    public SyncRequest getSyncRequest() throws EwpException {
        // Setup  request chatdata.
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
     *
     * @param object
     * @param requestCallback
     * @param screenRefreshListener
     * @throws EwpException
     * @throws IOException
     */
    public void syncChatWithServerData_Part2(XmlPullParser object, RequestCallback requestCallback, ScreenRefreshListener screenRefreshListener) throws EwpException, IOException {
        // SYNC with server data
        SyncReply syncReply = syncWithServerResponseData(object, screenRefreshListener);
        if (syncReply != null) {
            // Send SyncReply to server
            new SendReplyForServerDataSync(syncReply).execute(requestCallback);
        } else {
            SyncReply reply = new SyncReply();
            reply.setStatus(1);
            requestCallback.onFailure("ServerData", null);
        }
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
        SyncReply syncReply = service.processRemoteRequest(syncRequest);
        if (syncReply == null) {
            Log.d(this.getClass().getName(), "SyncReply is null syncWithServerResponseData()");
            LogConfigurer.info(this.getClass().getName(), "SyncReply is null syncWithServerResponseData()");
        }
        if (syncRequest != null && !syncRequest.getSyncTransactionList().isEmpty()) {
            if (screenRefreshListener != null)
                screenRefreshListener.onDataArrivalInSync(syncRequest);
        }
        chatSyncNetworkService.addUpdateHistory(false, "ServerData", syncRequest, null, null, null);
        return syncReply;
    }
    /**
     * sync my data with server part 2
     *
     * @param object
     * @throws EwpException
     * @throws IOException
     */
    public void syncMyChatDataWithServer_Part2(XmlPullParser object) throws EwpException, IOException {
        // Parse server's reply
        SyncReply syncReply = SyncReply.parseXml(object);
        // Resolve conflicts
        service.processRemoteReply(syncReply);
        Log.d(this.getClass().getName(), "Conflict count: " + syncReply.getConflictList().size());
        XmlSerializer xmlSerializer = new KXmlSerializer();
        StringWriter writer = new StringWriter();
        xmlSerializer.setOutput(writer);
        SyncReply.toXmlWriter(xmlSerializer, syncReply);
        xmlSerializer.endDocument();
        String writerStr = writer.toString();
        Log.d(this.getClass().getSimpleName(), "************************ClientData Receive Start*************************");
        Log.d(this.getClass().getSimpleName(), writerStr);
        if (syncReply != null) {
            chatSyncNetworkService.addUpdateHistory(false, "ClientData", null, syncReply, serverResponseData, writerStr);
        } else {
            chatSyncNetworkService.addUpdateHistory(false, "ClientData", null, syncReply, serverResponseData, null);
        }
    }
    /**
     * Send HTTP request to server for its data. Action: "GetSyncData"
     *
     * @throws EwpException
     */
    public void sendRequestToGetChatData(final RequestCallback requestCallback, final ScreenRefreshListener screenRefreshListener) throws EwpException {
        // Note that this a GET request.
        RestService restService = new RestService();
        String urlString = restService.serverChatUrlString() + "serverdata";
        String myDeviceId = service.getDataProvider().getMyDeviceId();
        String appVersion = appConfig.getApplicationSuiteVersion();
        String dbVersion = appConfig.getDatabaseVersion();
        AjaxCallback<XmlPullParser> callback = new AjaxCallback<XmlPullParser>() {
            @Override
            public void callback(String url, XmlPullParser object, AjaxStatus status) {
                super.callback(url, object, status);
                try {
                    if (status.getCode() == 200 && object != null) {
                        Log.d(this.getClass().getSimpleName(), "******************Server Data Success**************");
                        syncChatWithServerData_Part2(object, requestCallback, screenRefreshListener);
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
     * actual task for ClientData request
     */
    class sendRequestToSyncChatData extends AsyncTask<RequestCallback, Void, Void> {


        @Override
        protected Void doInBackground(RequestCallback... params) {
            URL url = null;
            HttpResponse response = null;
            HttpPost httpPost = null;
            HttpClient httpClient = null;
            try {
                RestService restService = new RestService();
                // SyncServerWithClient
                String urlString = restService.serverChatUrlString() + "clientdata";
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
                serverResponseData = writer.toString();
                httpPost.setEntity(new StringEntity(serverResponseData, "UTF-8"));
                httpPost.setHeader("Content-Type", "application/xml");
                httpPost.setHeader("Accept", "application/xml");
                httpPost.setHeader(Commons.ACTION, "ClientData");
                httpPost.setHeader(Commons.REQ_DEVICE_ID, myDeviceId);
                httpPost.setHeader(Commons.LOGIN_TOKEN, EwpSession.getSharedInstance().getLoginToken());
                // Execute POST
                response = httpClient.execute(httpPost);
                Log.d(this.getClass().getSimpleName(), "*************************ClientData Start**********************");
                Log.d(this.getClass().getSimpleName(), serverResponseData);
                Log.d(this.getClass().getSimpleName(), "***********************ClientData End**********************");
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
            } catch (UnsupportedEncodingException e) {
                params[0].onFailure("ClientData", "" + e);
                Log.d(this.getClass().getName(), "ClientData onFailure " + e);
            } catch (IOException e) {
                params[0].onFailure("ClientData", "" + e);
                Log.d(this.getClass().getName(), "ClientData onFailure " + e);
            } catch (URISyntaxException e) {
                params[0].onFailure("ClientData", "" + e);
                Log.d(this.getClass().getName(), "ClientData onFailure " + e);
            } catch (Exception e) {
                params[0].onFailure("ClientData", "" + e);
                Log.d(this.getClass().getName(), "ClientData onFailure " + e);
            }
            return null;
        }
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
                Log.d(this.getClass().getSimpleName(), writerStr);
                Log.d(this.getClass().getSimpleName(), "******************************Sending ResolveClientConflicts Data End**************************");
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
            }  catch (UnsupportedEncodingException e) {
                params[0].onFailure("ServerData", "" + e);
                Log.d(this.getClass().getSimpleName(), "resolveclientconflicts onFailure " + e);
            } catch (IOException e) {
                params[0].onFailure("ServerData", "" + e);
                Log.d(this.getClass().getSimpleName(), "resolveclientconflicts onFailure " + e);
            } catch (URISyntaxException e) {
                params[0].onFailure("ServerData", "" + e);
                Log.d(this.getClass().getSimpleName(), "resolveclientconflicts onFailure " + e);
            } catch (Exception e) {
                params[0].onFailure("ServerData", "" + e);
                Log.d(this.getClass().getSimpleName(), "resolveclientconflicts onFailure " + e);
            }
            return null;
        }
    }


}
