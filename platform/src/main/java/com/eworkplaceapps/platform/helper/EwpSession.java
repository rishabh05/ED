//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/18/2015
//===============================================================================
package com.eworkplaceapps.platform.helper;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.eworkplaceapps.platform.context.ContextHelper;
import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.tenant.Tenant;
import com.eworkplaceapps.platform.tenant.TenantUser;
import com.eworkplaceapps.platform.tenant.TenantUserDataService;
import com.eworkplaceapps.platform.utils.Tuple;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

/**
 * It initialize all session variable.
 */
public class EwpSession {
    private static EwpSession sharedInstance;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    public static final String PLATFORM_APPLICATION_ID = "6C7CAF42-6A0D-416B-9BE9-3B91F648562A";
    public static final String EMPLOYEE_APPLICATION_ID = "A8920FC7-4874-4854-BCE1-4C4909C6C824";
    public static final String TASK_MANAGEMENT_APPLICATION_ID = "4B898494-9F5B-4EA1-98FF-E207F0E36F14";

    private String appName = "";
    private UUID tenantId = null;
    private String tenantName = "<<TenantName>>";
    private UUID userId = null;
    private String userName = "<<UserName>>";
    private boolean isSystemAdmin = false;
    private boolean isAccountAdmin = false;
    private Map<String, Object> extendedProperties = new HashMap<String, Object>();

    public String getAppName() {
        return appName;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isSystemAdmin() {
        return isSystemAdmin;
    }

    public boolean isAccountAdmin() {
        return isAccountAdmin;
    }

    public Map<String, Object> getExtendedProperties() {
        return extendedProperties;
    }

    public static EwpSession getSharedInstance() {
        if (sharedInstance == null) {
            sharedInstance = new EwpSession();
            preferences = PreferenceManager.getDefaultSharedPreferences(ContextHelper.getContext());
            editor = preferences.edit();
            try {
                sharedInstance.refresh();
            } catch (EwpException ewpError) {
                Log.d(sharedInstance.getClass().getName(), "" + ewpError);
            }
        }
        return sharedInstance;
    }

    private EwpSession() {
        // Set all properties
    }

    public void refresh() throws EwpException {
        // Reset all or some properties

        boolean isLoggedIn = preferences.getBoolean("UserLoggedIn", false);
        if (isLoggedIn) {

            String id = preferences.getString("LoggedInUserID", "");
            String inTenantID = preferences.getString("LoggedInTenantID", "");
            String name = preferences.getString("LoggedInUserName", "");
            if (id != null && !"".equals(id)) {
                this.userId = UUID.fromString(id);
                this.tenantId = UUID.fromString(inTenantID);
                this.userName = name;
                TenantUserDataService tUserService = new TenantUserDataService();

                Tuple.Tuple2<Boolean, Boolean, Boolean> resultTuple = tUserService.isAccountOrSystemAdminUser(userId);
                if (resultTuple != null) {
                    isSystemAdmin = resultTuple.getT2();
                    isAccountAdmin = resultTuple.getT1();
                }
                // It is used to assign the logged in tenant name.
                TenantUserDataService tService = new TenantUserDataService();
                BaseEntity tenantResultTuple = tService.getEntity(this.tenantId);
                if (tenantResultTuple != null) {
                    this.tenantName = ((TenantUser) tenantResultTuple).getFullName();
                }
            }
        }
    }

    public boolean isUserLoggedIn() throws EwpException {
        // Reset all or some properties
        boolean isLoggedIn = preferences.getBoolean("UserLoggedIn", false);

        if (isLoggedIn) {
            String id = preferences.getString("LoggedInUserID", "");
            String inTenantID = preferences.getString("LoggedInTenantID", "");
            String name = preferences.getString("LoggedInUserName", "");
            if (id != null && !"".equals(id)) {
                this.userId = UUID.fromString(id);
                this.tenantId = UUID.fromString(inTenantID);
                this.userName = name;
                TenantUserDataService tUserService = new TenantUserDataService();

                Tuple.Tuple2<Boolean, Boolean, Boolean> resultTuple = tUserService.isAccountOrSystemAdminUser(userId);
                if (resultTuple != null) {
                    isSystemAdmin = resultTuple.getT2();
                    isAccountAdmin = resultTuple.getT1();
                }

                // It is used to assign the logged in tenant name.
                TenantUserDataService tService = new TenantUserDataService();
                BaseEntity tenantResultTuple = tService.getEntity(this.tenantId);
                if (tenantResultTuple != null) {
                    return true;
                }

            }
        }
        return isLoggedIn;
    }

    public String getSessionValueFromKey(String key) {
        return preferences.getString(key, "");
    }

    public void setSession(UUID tenantId, UUID userId, String userName, String loginToken) throws EwpException {
        editor.putString("LoggedInUserID", String.valueOf(userId));
        editor.putString("LoggedInTenantID", String.valueOf(tenantId));
        editor.putString("LoggedInUserName", userName);
        editor.putBoolean("UserLoggedIn", true);
        editor.putString("LoginToken", loginToken);
        editor.putString("LoggedInUserTimeZone", TimeZone.getDefault().getID());
        editor.commit();
        // Reinit the session.
        EwpSession.getSharedInstance().refresh();
    }

    /**
     * It will call when user click on Logout button.
     * It will clear the session variables.
     */
    public void clearSession() {
        this.userId = UUID.randomUUID();
        this.tenantId = UUID.randomUUID();
        this.userName = "";

        //in android calling clear() over editor clears  entire preferences(Shrey)
        editor.clear();
        this.isSystemAdmin = false;
        this.isAccountAdmin = false;
    }

    /**
     * It is used to check user is exist or not, If user exist then return true.
     * If any error occur then it raise EwpError and return false.
     *
     * @return boolean
     */
    public static boolean isUserExist() throws EwpException {
        boolean result = DeviceInfoData.isUserExist();
        return result;
    }

    /**
     * @return string login token
     */
    public static String getLoginToken() {
        return preferences.getString("LoginToken", "");
    }

    /**
     * It is used to set new TimeZone if it is change.
     * @param value
     * @throws EwpException
     */

    public static void setTimeZoneInSession(String value) {
        editor.putString("LoggedInUserTimeZone", value);
        editor.commit();
    }

    /**
     * This method is use to check TimeZone is change or not, If TimeZones are same then method will return false.
     * @param newTimeZoneValue
     * @return
     */
    public static boolean isTimeZoneChanged(String newTimeZoneValue) {
        String value =  preferences.getString("LoggedInUserTimeZone", "");
        if (!value.equalsIgnoreCase(newTimeZoneValue)) {
            return true;
        }
        return false;
    }
}