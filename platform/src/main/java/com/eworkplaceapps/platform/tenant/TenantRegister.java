//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/22/2015
//===============================================================================
package com.eworkplaceapps.platform.tenant;

import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.utils.Utils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This is a TenantRegister class for tenant Register. Points to note about the class:
 * This class provides parsing of the xml string.
 */
public class TenantRegister {

    public TenantRegister() {
    }

    // Getter/setter for tenant name.
    private String name;
    // The Getter/setter property represents uniqueness for tenant.
    private UUID tenantId = Utils.emptyUUID();
    // The Getter/setter property represents uniqueness for user.
    private UUID userId = Utils.emptyUUID();
    private String loginToken = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    /**
     * parse the multiple tenant objects
     *
     * @param object
     * @return
     */
    public static List<TenantRegister> parseXmlList(XmlPullParser object) throws EwpException {
        try {
            List<TenantRegister> list = new ArrayList<TenantRegister>();
            TenantRegister tenantRegister = null;
            String text = "";
            int event = object.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = object.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (name != null && "Tenant".equals(name)) {
                            tenantRegister = new TenantRegister();
                            list.add(tenantRegister);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = object.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (name != null && "TenantId".equals(name)) {
                            tenantRegister.setTenantId(UUID.fromString(text));
                        } else if (name != null && "UserId".equals(name)) {
                            tenantRegister.setUserId(UUID.fromString(text));
                        } else if (name != null && "Name".equals(name)) {
                            tenantRegister.setName(text);
                        }
                        break;
                    default:
                        break;
                }
                event = object.next();
            }
            return list;
        } catch (XmlPullParserException e) {
            EwpException ex = new EwpException(e);
            throw ex;
        } catch (IOException e) {
            EwpException ex = new EwpException(e);
            throw ex;
        }
    }
}
