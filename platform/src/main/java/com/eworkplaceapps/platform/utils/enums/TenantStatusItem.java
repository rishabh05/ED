//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/14/2015
//===============================================================================
package com.eworkplaceapps.platform.utils.enums;

/**
 * Specifies the different possible values of TENANT_STATUS picklist items.
 */
public enum TenantStatusItem {

    // Specifies that tenant is active.
    ACTIVE(1),
    // Specifies that tenant is in-active.
    INACTIVE(2),
    // Specifies that tenant is onhold.
    ON_HOLD(3);

    private int id;

    TenantStatusItem(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String toDisplayString() {
        switch (this) {
            case ACTIVE:
                return "ACTIVE";
            case INACTIVE:
                return "INACTIVE";
            case ON_HOLD:
                return "OnHold";
            default:
                return "";
        }
    }

    public String enumIntAsString() {
        switch (this) {
            case ACTIVE:
                return "1";
            case INACTIVE:
                return "2";
            case ON_HOLD:
                return "3";
            default:
               return "";
        }
    }
}
