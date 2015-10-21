//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/21/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

/**
 * Defines constants for group follower type.
 */
public enum FollowerType {

    /// Define Custom Group.
    GROUP(1),
    /// Employee manages directly
    YOUR_DIRECT_REPORTS(2),
    /// All the employees that are managed by them or anyone that they manage.
    ALL_YOUR_REPORTS(3);

    public String toString() {
        switch (this) {
            case ALL_YOUR_REPORTS:
                return "AllYourReports";
            case YOUR_DIRECT_REPORTS:
                return "YourDirectReports";
            default:
                return "Group";
        }
    }

    private int id;

    FollowerType(int id) {
        this.id = id;
    }

    private int getId() {
        return id;
    }
}
