//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/14/2015
//===============================================================================
package com.eworkplaceapps.platform.utils.enums;

/**
 * Specifies the property constant in which to identify the role level.
 */
public enum RoleLevel {
    // Default role level.
    GENERAL(0),
    // ADMIN role level.
    ADMIN(1),
    // SYSTEM admin role level.
    VIEWER(2),
    // SYSTEM admin role level.
    SYSTEM_ADMIN(3),
    // NONE role.
    NONE(4),
    // Contributor role.
    PROJECT_MANAGER(5);

    private int id;

    RoleLevel(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String enumIntAsString() {
        switch (this) {
            case GENERAL:
                return "0";
            case ADMIN:
                return "1";
            case VIEWER:
                return "2";
            case SYSTEM_ADMIN:
                return "3";
            case NONE:
                return "4";
            case PROJECT_MANAGER:
                return "5";
            default:
                return "";
        }
    }
}
