//===============================================================================
// (c) 2015 eWorkplace Apps.  All rights reserved.
// Original Author: Sourabh Agrawal
// Original Date: 22/09/2015
//===============================================================================
package com.eworkplaceapps.platform.authentication;

import com.androidquery.AQuery;
import com.eworkplaceapps.platform.commons.Commons;
import com.eworkplaceapps.platform.context.ContextHelper;
import com.eworkplaceapps.platform.dbsync.RestService;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.requesthandler.RequestCallback;
import com.eworkplaceapps.platform.utils.AppMessage;
import com.eworkplaceapps.platform.utils.Utils;
import com.eworkplaceapps.platform.utils.enums.AuthenticationType;
import com.eworkplaceapps.platform.utils.enums.RequesterType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by eWorkplace on 9/22/2015.
 */
public class Authentication extends BaseService {
    private int commandType = -1;
    private String tokenId = "";
    private final String APP_ID = "A8920FC7-4874-4854-BCE1-4C4909C6C824";
    private RestService restService;

    public Authentication() {
        restService = new RestService();
    }

    /**
     * Method is used to authenticate user. It will call rest service to authenticate the user.
     *
     * @param tenantName
     * @param firstName
     * @param lastName
     * @param userEmail
     * @param requestCallback
     * @throws EwpException
     */
    public void signUp(String tenantName, String firstName, String lastName, String userEmail, final RequestCallback requestCallback) throws EwpException {
        validateSignupTenant(tenantName, firstName, lastName, userEmail);
        String urlString = getFortyOneUrl() + "employees/signuptenant";
        this.method(AQuery.METHOD_POST);
        super.callerName = Commons.SIGNUP_USER_A;
        super.requestCallback = requestCallback;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("TenantName", tenantName);
        params.put("PrimaryEmployeeFisrtName", firstName);
        params.put("PrimaryEmployeeLastName", lastName);
        params.put("PrimaryEmployeeEmail", userEmail);
        AQuery aQuery = new AQuery(ContextHelper.getContext());
        // Ajax call with POST method
        aQuery.ajax(urlString, params, JSONObject.class, this);
    }

    /**
     * Validate the fields required for signup.
     *
     * @param tenantName
     * @param firstName
     * @param lastName
     * @param email
     * @return
     * @throws EwpException
     */
    public boolean validateSignupTenant(String tenantName, String firstName, String lastName, String email) throws EwpException {
        List<String> message = new ArrayList<String>();
        Map<EnumsForExceptions.ErrorDataType, String[]> dicError = new HashMap<EnumsForExceptions.ErrorDataType, String[]>();

        if (email == null && email.isEmpty()) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"Email"});
            message.add(AppMessage.EMAIL_REQUIRED);
        } else if (!Utils.isValidEmail(email)) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"Email"});
            message.add(AppMessage.INVALID_EMAIL);
        }
        if (tenantName == null && tenantName.isEmpty()) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"TenantName"});
            message.add(AppMessage.NAME_REQUIRED);
        }
        if (firstName == null && firstName.isEmpty()) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"FirstName"});
            message.add(AppMessage.FIRST_NAME_IS_REQUIRED);
        }
        if (lastName == null && lastName.isEmpty()) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"LastName"});
            message.add(AppMessage.LAST_NAME_REQUIRED);
        }
        if (message.isEmpty()) {
            return true;
        } else {
            throw new EwpException(new EwpException("VALIDATION_ERROR"), EnumsForExceptions.ErrorType.VALIDATION_ERROR, message, EnumsForExceptions.ErrorModule.DATA_SERVICE, dicError, 0);
        }
    }

    /**
     * Validate the fields required for authenticating the user.
     *
     * @param email
     * @param password
     * @return
     * @throws EwpException
     */
    public boolean isValidEmailPassword(String email, String password) throws EwpException {
        List<String> message = new ArrayList<String>();
        Map<EnumsForExceptions.ErrorDataType, String[]> dicError = new HashMap<EnumsForExceptions.ErrorDataType, String[]>();

        if (email == null && email.isEmpty()) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"Email"});
            message.add(AppMessage.EMAIL_REQUIRED);
        } else if (!Utils.isValidEmail(email)) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"Email"});
            message.add(AppMessage.INVALID_EMAIL);
        }
        if (password == null && password.isEmpty()) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"Password"});
            message.add(AppMessage.PASSWORD_REQUIRED);
        }
        if (message.isEmpty()) {
            return true;
        } else {
            throw new EwpException(new EwpException("VALIDATION_ERROR"), EnumsForExceptions.ErrorType.VALIDATION_ERROR, message, EnumsForExceptions.ErrorModule.DATA_SERVICE, dicError, 0);
        }

    }

    /**
     * Method is used to change password. It will call rest service to change the user password.
     *
     * @param loginEmail
     * @param newPassword
     * @param oldPassword
     * @param requestCallback
     * @throws EwpException
     */
    public void changePassword(String loginEmail, String newPassword, String oldPassword, final RequestCallback requestCallback) throws EwpException {
        String urlString = restService.getAuthenticateServerUrlString() + "tenantusers/changePassword";
        isValidEmailPassword(loginEmail, newPassword);
        super.callerName = Commons.CHANGE_PASSWORD;
        super.requestCallback = requestCallback;
        this.method(AQuery.METHOD_PUT);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("LoginEmail", loginEmail);
        params.put("NewPassword", newPassword);
        params.put("OldPassword", oldPassword);
        params.put("ApplicationId", APP_ID);
        AQuery aQuery = new AQuery(ContextHelper.getContext());
        // Ajax call with PUT method
        aQuery.ajax(urlString, params, JSONObject.class, this);
    }

    /**
     * Method is used to reset password. It will call rest service to reset the user password.
     *
     * @param loginEmail
     * @param newPassword
     * @param requestCallback
     * @throws EwpException
     */
    public void setPrimaryUserPassword(String loginEmail, String newPassword, final RequestCallback requestCallback) throws EwpException {
        String urlString = restService.getAuthenticateServerUrlString() + "tenantusers/setupprimaryuseraccount";
        isValidEmailPassword(loginEmail, newPassword);
        super.callerName = Commons.SET_PRIMARYUSER_PASSWORD;
        super.requestCallback = requestCallback;
        this.method(AQuery.METHOD_PUT);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("LoginEmail", loginEmail);
        params.put("NewPassword", newPassword);
        params.put("RequestTokenId", EwpSession.getSharedInstance().getLoginToken());
        AQuery aQuery = new AQuery(ContextHelper.getContext());
        // Ajax call with PUT method
        aQuery.ajax(urlString, params, JSONObject.class, this);

    }

    /**
     * Method is used to reset password. It will call rest service to reset the user password.
     *
     * @param loginEmail
     * @param newPassword
     * @param requestCallback
     * @throws EwpException
     */
    public void resetPassword(String loginEmail, String newPassword, final RequestCallback requestCallback) throws EwpException {
        String urlString = restService.getAuthenticateServerUrlString() + "tenantusers/resetPassword";
        isValidEmailPassword(loginEmail, newPassword);
        super.callerName = Commons.RESET_PASSWORD;
        super.requestCallback = requestCallback;
        this.method(AQuery.METHOD_PUT);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("LoginEmail", loginEmail);
        params.put("NewPassword", newPassword);
        params.put("TokenId", EwpSession.getSharedInstance().getLoginToken());
        params.put("TenantId", EwpSession.getSharedInstance().getTenantId().toString());
        AQuery aQuery = new AQuery(ContextHelper.getContext());
        //Ajax call with PUT method
        aQuery.ajax(urlString, params, JSONObject.class, this);
    }

    /**
     * Method is used to check password. It will call rest service to reset the user password.
     *
     * @param tokenId
     * @param commandType
     * @param requestCallback
     */
    public void requiredSetPassword(String tokenId, int commandType, final RequestCallback requestCallback) {
        String urlString;
        this.commandType = commandType;
        this.tokenId = tokenId;
        super.requestCallback = requestCallback;
        if (this.commandType == AuthenticationType.EmailCommandType.InvitedEmployeeAccountSetup.getId()) {
            urlString = getFortyOneUrl() + "invitedemployees/tokens/" + tokenId + "/details";
        } else {
            urlString = getFortyOneUrl() + "employees/primaryusertokens/" + tokenId + "/details";
        }
        super.callerName = Commons.REQUIRED_SET_PASSWORD;
        this.method(AQuery.METHOD_GET);
        AQuery aQuery = new AQuery(ContextHelper.getContext());
        // Ajax call with GET method
        aQuery.ajax(urlString, JSONObject.class, this);
    }

    /**
     * @param userEmail
     * @param password
     * @param lat
     * @param lng
     * @param requestCallback
     * @throws EwpException
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public void authenticateUser(String userEmail, String password, double lat, double lng, final RequestCallback requestCallback) throws EwpException, JSONException, UnsupportedEncodingException {
        String url = restService.getAuthenticateServerUrlString() + "tenantusers/authenticateuser";
        isValidEmailPassword(userEmail, password);
        super.callerName = Commons.AUTH_USER_A;
        this.method(AQuery.METHOD_POST);
        super.requestCallback = requestCallback;
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("ApplicationId", APP_ID);
        params.put("TenantUrl", "");
        params.put("RequesterRegion", "US");
        params.put("IANATimeZone", TimeZone.getDefault().getID());
        params.put("RequesterId", restService.getAppConfig().getClientDeviceId());
        params.put("RequesterType", RequesterType.ANDROID.getId());
        params.put("Password", password);
        params.put("LoginEmail", userEmail);
        params.put("AuthenticationType", AuthenticationType.EW_APPS.getId());
        params.put("Latitude", String.valueOf(lat));
        params.put("Longitude", String.valueOf(lng));
        AQuery aQuery = new AQuery(ContextHelper.getContext());
        // Ajax call with POST method
        aQuery.ajax(url, params, JSONObject.class, this);
    }

}
