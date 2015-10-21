//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/26/2015
//===============================================================================
package com.eworkplaceapps.platform.utils.enums;

/**
 *
 */
public enum AuthenticationType {

    EW_APPS(1), O_AUTH_GMAIL(2), O_AUTH_FACEBOOK(3);
    private int id;

    AuthenticationType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static enum EmailCommandType {
        PrimaryUserAccountSetup(1),
        InvitedEmployeeAccountSetup(2), ResetPassword(3);
        private int id;
        EmailCommandType(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

    }

}
