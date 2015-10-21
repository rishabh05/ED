//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: <4/17/2015>
//===============================================================================
package com.eworkplaceapps.platform.applicationinfo;

import java.util.HashMap;
import java.util.Map;

/**
 * It manages all application in a dictionary against their application id.
 */
public class ApplicationManager {

    /**
     * Specifies application callback command. 60000
     */
    public enum AppCallbackCommand {
        // Specifies that sing-up tenant command.
        SIGN_UP_TENANT(1),

        // Specifies the 'Send Change Password EMAIL' command.
        SEND_CHANGE_PASSWORD_EMAIL(2),

        //  Specifies the 'Send Forgot Password EMAIL' command.
        FORGOT_PASSWORD_EMAIL(3),

        // Command to resolve event notification recipient.
        RESOLVE_NOTIFICATION_RECIPIENT(4),

        // Command to resolve event notification recipient.
        RESOLVE_NOTIFICATION_MESSAGE_BODY(5);

        private int id;

        AppCallbackCommand(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    private static Map<String, BaseApplicationInfo> registeredApplication;

    static {
        registeredApplication = new HashMap<>();
    }

    /**
     * It is called when application execute first time.
     * It adds an entry into dictionary against unique application id.
     *
     * @param applicationInfo BaseApplicationInfo
     */
    public static void register(BaseApplicationInfo applicationInfo) {
        // ADD application info in dictionary, If application already register, update application info.
        ApplicationManager.registeredApplication.put(applicationInfo.getApplicationId().toString(), applicationInfo);
    }

    /**
     * It is called on application stop.
     * It remove an entry into dictionary against unique application id.
     *
     * @param applicationInfo BaseApplicationInfo
     */
    public static void unRegister(BaseApplicationInfo applicationInfo) {
        // ADD application info in dictionary, If application already register, update application info.
        ApplicationManager.registeredApplication.remove(applicationInfo.getApplicationId().toString());
    }

    /**
     * It is used to get application info object by application id.
     * AppId: APPLICATION id to match ApplicationInfo instance.</param>
     * Return an instance of ApplicationInfo object to match application id.
     *
     * @param appId STRING
     * @return BaseApplicationInfo
     */
    public static BaseApplicationInfo getApplicationInfo(String appId) {
        return ApplicationManager.registeredApplication.get(appId);
    }

    /**
     * It is used to execute operation in target application from source application using callback handler.
     * command: App call back command enum value.
     * parms: Input parameters as a dictonary
     * APP_ID: Target application id where operation will be execute.
     * Returns object from target application method
     *
     * @param command AppCallbackCommand
     * @param params  Map<STRING, Object>
     * @param appId   STRING
     * @return Object
     */
    public static Object executeOperation(AppCallbackCommand command, Map<String, Object> params, String appId) {
        BaseApplicationInfo appInfo = ApplicationManager.getApplicationInfo(appId);
        if (appInfo != null && appInfo.applicationCallbackHandler(command, params) != null) {
            return appInfo.applicationCallbackHandler(command, params);
        }
        return null;
    }
}
