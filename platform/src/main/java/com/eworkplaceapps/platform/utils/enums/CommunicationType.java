//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/19/2015
//===============================================================================
package com.eworkplaceapps.platform.utils.enums;

/**
 *
 */
public enum CommunicationType {
    // Enum(Email, Phone, Social)
    /// Phone
    PHONE(1),
    /// Email
    EMAIL(2),
    /// Phone
    SOCIAL(3);
    private int id;

    CommunicationType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static CommunicationType getType(String type) {
        switch (type) {
            case "EMAIL":
                return CommunicationType.EMAIL;
            case "PHONE":
                return CommunicationType.PHONE;
            case "SOCIAL":
                return CommunicationType.SOCIAL;
            default:
                return CommunicationType.PHONE;
        }
    }
}
