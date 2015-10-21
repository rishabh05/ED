//===============================================================================
// (c) 2015 eWorkplace Apps.  All rights reserved.
// Original Author: Sourabh Agrawal
// Original Date: 22/09/2015
//===============================================================================
package com.eworkplaceapps.platform.authentication;

import android.os.AsyncTask;
import android.util.Log;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.eworkplaceapps.platform.commons.Commons;
import com.eworkplaceapps.platform.dbsync.RestService;
import com.eworkplaceapps.platform.dbsync.SyncService;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.requesthandler.RequestCallback;
import com.eworkplaceapps.platform.tenant.TenantRegister;
import com.eworkplaceapps.platform.utils.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

public class BaseService extends AjaxCallback<JSONObject> {

    private String serverResponseData;
    private SyncService service;
    private RestService restService;
    private boolean logInSyncHistory = false;
    protected RequestCallback requestCallback = null;
    private static final String TAG = "TenantRegistration";
    protected String callerName;
    private AuthenticationReply authenticationReply;

    public BaseService() {
    }

    public BaseService(String callerName) throws EwpException {
        service = new SyncService();
        restService = new RestService();
        this.callerName = callerName;
    }

    public BaseService(SyncService service, RestService restService, String callerName) throws EwpException {
        this.service = service;
        this.restService = restService;
        this.callerName = callerName;
    }

    /**
     * It give the server url to connect.
     * Its overridable method to change the server url.
     *
     * @return
     */

    public String getFortyOneUrl() {
        return "http://ewp-dev41.eworkplace.com/services/ed/";
    }

    /**
     * It is a callback method , It will be execute after restservice call.
     *
     * @param url
     * @param object
     * @param status
     */
    @Override
    public void callback(String url, JSONObject object, AjaxStatus status) {
        super.callback(url, object, status);
        try {
            if (status.getCode() == 200 && object != null) {
                if (this.callerName.equalsIgnoreCase(Commons.AUTH_USER_A)) {
                    //  set jsonObject value into AuthenticationReply.
                    authenticationReply = parseAuthenticationReply(object);
                    if (authenticationReply != null && authenticationReply.getTenantList().size() == 1) {
                        authenticationReply.getTenantList().get(0).setLoginToken(authenticationReply.getLoginToken());
                        requestCallback.onSuccess(this.callerName, authenticationReply);

                    }
                } else {
                    requestCallback.onSuccess(this.callerName, object);

                }
            } else {
                String error = "";
                EwpException ex = EwpException.handleError(null, status);
                if (ex != null && ex.message != null && ex.message.size() > 0)
                    error = ex.message.get(0);
                requestCallback.onFailure(this.callerName, error);
                Log.e(TAG, error);
            }
        } catch (Exception e) {
            requestCallback.onFailure(this.callerName, e.getMessage());
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * set jsonObject value into AuthenticationReply
     *
     * @param object
     * @return AuthenticationReply
     * @throws JSONException
     */
    public AuthenticationReply parseAuthenticationReply(JSONObject object) throws JSONException {
        AuthenticationReply authReply = new AuthenticationReply();
        authReply.setValid("true".equals(object.getString("Success")));
        authReply.setMultiTenantUser("true".equals(object.getString("MultiTenant")));
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

    protected class NetworkAsyncTask extends AsyncTask<RequestCallback, Void, Void> {

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
                case "POST":
                    return callHttpPostMethodBackground(params);
                case "PUT":
                    callHttpPutMethodBackground(params);
                    break;
                case "DELETE":
                    callHttpNoneWithIdMethodBackground(params);
                    break;
            }
            return null;
        }

        private Void callHttpPostMethodBackground(RequestCallback... params) {
            URL url = null;
            HttpResponse response = null;
            HttpPost httpPost = null;
            HttpClient httpClient = null;


            try {
                url = new URL(this.url);
                httpClient = new DefaultHttpClient();
                httpPost = new HttpPost(url.toURI());
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                httpPost.setEntity(new StringEntity(jsonString, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // Set up the header types needed to properly transfer JSON
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader(Commons.LOGIN_TOKEN, EwpSession.getSharedInstance().getLoginToken());
            // Execute POST
            try {
                response = httpClient.execute(httpPost);
            } catch (IOException e) {
                params[0].onFailure(methodName, e);
                e.printStackTrace();
            }
            try {
                if (response.getStatusLine().getStatusCode() == 200) {
                    String out = Utils.convertResponseToString(response);
                    Log.d("reponse", out);
                        params[0].onSuccess(methodName, out);
                } else {
                    String error = "";
                    EwpException ex;
                    if (response.getStatusLine().getStatusCode() == 500) {
                       ex = EwpException.handleError(response, null);
                        if (ex != null && ex.message != null && ex.message.size() > 0)
                            error = ex.message.get(0);
                    } else {
                         ex = new EwpException();
                        ex.localizedMessage = response.getStatusLine().getReasonPhrase();
                        error = response.getStatusLine().getReasonPhrase();
                    }
                    params[0].onFailure(methodName, ex);
                }
            } catch (Exception e) {
                params[0].onFailure(methodName, e);

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
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                httpPut.setEntity(new StringEntity(jsonString, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // Set up the header types needed to properly transfer JSON
            httpPut.setHeader("Content-Type", "application/json");
            httpPut.setHeader("Accept", "application/json");
            httpPut.setHeader(Commons.LOGIN_TOKEN, EwpSession.getSharedInstance().getLoginToken());
            // Execute POST
            try {
                response = httpClient.execute(httpPut);
            } catch (IOException e) {
                params[0].onFailure(methodName, e);
                e.printStackTrace();
            }
            try {
                if (response.getStatusLine().getStatusCode() == 200) {
                    String out = Utils.convertResponseToString(response);
                    params[0].onSuccess(methodName, "Model updated successfully.");
                    Log.d("reponse", out);
                } else if (response.getStatusLine().getStatusCode() == 500) {
                    String error = "";
                    EwpException ex = EwpException.handleError(response, null);
                    params[0].onFailure(methodName, ex);
                } else {
                    EwpException ex = new EwpException();
                    ex.localizedMessage = response.getStatusLine().getReasonPhrase();
                    String error = response.getStatusLine().getReasonPhrase();
                    params[0].onFailure(methodName, ex);
                }
            } catch (Exception e) {
                params[0].onFailure(methodName, e);
                e.printStackTrace();
            }
            return null;
        }

        private Void callHttpNoneWithIdMethodBackground(RequestCallback... params) {
                URL url = null;
                HttpResponse response = null;
                HttpDelete httpDelete = null;
                HttpClient httpClient = null;

                try {
                    url = new URL(this.url);
                    httpClient = new DefaultHttpClient();
                    httpDelete = new HttpDelete(url.toURI());
                } catch (Exception e) {
                    e.printStackTrace();
                }


                // Set up the header types needed to properly transfer JSON
            httpDelete.setHeader("Content-Type", "application/json");
            httpDelete.setHeader("Accept", "application/json");
            httpDelete.setHeader(Commons.LOGIN_TOKEN, EwpSession.getSharedInstance().getLoginToken());
                // Execute POST
                try {
                    response = httpClient.execute(httpDelete);
                } catch (IOException e) {
                    params[0].onFailure(methodName, e);
                    e.printStackTrace();
                }
                try {
                    if (response.getStatusLine().getStatusCode() == 200) {
                        String out = Utils.convertResponseToString(response);
                        params[0].onSuccess(methodName, "Model updated successfully.");
                        Log.d("reponse", out);
                    } else if (response.getStatusLine().getStatusCode() == 500) {
                        String error = "";
                        EwpException ex = EwpException.handleError(response, null);
                        params[0].onFailure(methodName, ex);
                    } else {
                        EwpException ex = new EwpException();
                        ex.localizedMessage = response.getStatusLine().getReasonPhrase();
                        String error = response.getStatusLine().getReasonPhrase();
                        params[0].onFailure(methodName, error);
                    }
                } catch (Exception e) {
                    params[0].onFailure(methodName, e);
                    e.printStackTrace();
                }
            return null;
        }
    }

    /**
     *
     * @param userDic
     * @param key
     * @return
     * @throws JSONException
     */
    public Object getValueFromKey(JSONArray userDic, String key) throws JSONException {
        for (int i = 0; i < userDic.length(); i++) {
            Object mKey = userDic.getJSONObject(i).get("Key");
            String keyValue = "";
            if (mKey != null) {
                keyValue = (String) mKey;
            }
            if (keyValue == key) {
                return userDic.getJSONObject(i).get("Value");
            }
        }
        return null;
    }
}
