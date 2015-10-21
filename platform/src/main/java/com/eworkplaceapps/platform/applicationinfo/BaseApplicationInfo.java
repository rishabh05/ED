//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/17/2015
//===============================================================================
package com.eworkplaceapps.platform.applicationinfo;

import java.util.Map;
import java.util.UUID;

/**
 * A base class provides application information .
 */
public class BaseApplicationInfo {

    private UUID applicationId;
    private String applicationVersion;
    private String name;
    private String abbreviation;
    private String applicationUrl = "";

    public UUID getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(UUID applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getApplicationUrl() {
        return applicationUrl;
    }

    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }

    /**
     * Call back delegate to get application data
     *
     * @param command AppCallbackCommand
     * @param dict    Map<STRING, Object>
     * @return Object
     */
    public Object applicationCallbackHandler(ApplicationManager.AppCallbackCommand command, Map<String, Object> dict) {
        return null;
    }
}
