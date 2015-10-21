//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/22/2015
//===============================================================================
package com.eworkplaceapps.platform.authentication;

import com.eworkplaceapps.platform.tenant.TenantRegister;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * It contain all the properties to authenticate user.
 * Properties will be initialized when user signed in and authenticate from the server.
 * It is also used to parse the authenticate reply xml.
 */
public class AuthenticationReply {

    public AuthenticationReply() {

    }

    private Boolean valid = false;

    private Boolean systemAdmin = false;

    private Boolean multiTenantUser = false;

    private String loginToken;
    private UUID userID = Utils.emptyUUID();
    private List<TenantRegister> tenantList = new ArrayList<TenantRegister>();

    private volatile boolean parsingComplete = true;

    private String urlString = null;

    public AuthenticationReply(String url) {
        this.urlString = url;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Boolean getSystemAdmin() {
        return systemAdmin;
    }

    public void setSystemAdmin(Boolean systemAdmin) {
        this.systemAdmin = systemAdmin;
    }

    public Boolean getMultiTenantUser() {
        return multiTenantUser;
    }

    public void setMultiTenantUser(Boolean multiTenantUser) {
        this.multiTenantUser = multiTenantUser;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public List<TenantRegister> getTenantList() {
        return tenantList;
    }

    public void setTenantList(List<TenantRegister> tenantList) {
        this.tenantList = tenantList;
    }

    public boolean isParsingComplete() {
        return parsingComplete;
    }

    public void setParsingComplete(boolean parsingComplete) {
        this.parsingComplete = parsingComplete;
    }

    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }
}
