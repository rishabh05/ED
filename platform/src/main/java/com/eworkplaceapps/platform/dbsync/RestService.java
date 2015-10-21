//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/25/2015
//===============================================================================
package com.eworkplaceapps.platform.dbsync;

import android.util.Log;

import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.AppConfig;
import com.eworkplaceapps.platform.helper.AppConfigData;

/**
 * This class provides a common interface to REST service calls to server.
 * It supports POST and GET only.
 * The whole sequence of REST service interface is divided in to several unit steps.
 * The result of a step is saved in instance properties (like, Request object.)
 * This makes it possible for the caller of the service to customize any data
 * after it has been created by a unit step in this class.
 * <p/>
 * The response to the service request is handled in a completion handler.
 * Note that the service call is executed in a background thread.
 * The completion handler can be customized. The the custom handler
 * may call the default handler of this class for standard response parsing.
 * <p/>
 * The unit step and custom handler design provides many options to the
 * caller of this code to manage its control flow.
 */
public class RestService {

    private AppConfig appConfig = null;

    /**
     * Needed to create an instance
     */
    public RestService() {
        try {
            appConfig = new AppConfigData().getAppConfig();
        } catch (EwpException e) {
            Log.e(this.getClass().getName(), "" + e);
        }
    }

    /**
     * GET the default server URL
     *
     * @return
     */
    public String serverUrlString() {
        // GET it from SystemInfo
        String url = appConfig.getServerUrl()+"ed/";// + "api/Sync/";
        return url;
    }

    // Get the default server URL.
    public String serverChatUrlString() {
        // Get it from SystemInfo
        String url = appConfig.getServerUrl() + "api/Sync/Chat/";
        return url;
    }

    /**
     * GET the default server URL.
     *
     * @return
     */
    public String getAuthenticateServerUrlString() {
        // GET it from SystemInfo
        String url = appConfig.getAuthenticateUrl();
        return url;
    }

    public AppConfig getAppConfig() {
        return appConfig;
    }
}
