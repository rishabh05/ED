//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/14/2015
//===============================================================================
package com.eworkplaceapps.platform.utils.enums;

/**
 * Specifies the different possible values of USER_STATUS picklist items.
 */
public enum UserStatusItem {

    // This type is for internal use only.

    ALL(0),
    // Specifies that user is active.
    ACTIVE(1),
    // Specifies that user is in-active.
    INACTIVE(2),
    // Sent Invitation.
    SENT_INVITATION(3);

    private int id;

    UserStatusItem(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String enumIntAsString() {
        switch (this) {
            case ALL:
                return "0";
            case ACTIVE:
                return "1";
            case INACTIVE:
                return "2";
            case SENT_INVITATION:
                return "3";
            default:
                return "";
        }
    }
}
