//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/14/2015
//===============================================================================
package com.eworkplaceapps.platform.utils.enums;

/**
 * Specifies the different possible values of AppType picklist items.
 */
public enum AppTypeItem {

    // Specifies AppType2.
    ALL(1),
    // Specifies AppType2.
    TASK_MANAGEMENT(2),
    // Specifies Issue Tracker application.
    ISSUE_TRACKER(3),
    // Specifies EMPLOYEE Directory application.
    EMPLOYEE_DIRECTORY(4);

    private int id;

    AppTypeItem(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
