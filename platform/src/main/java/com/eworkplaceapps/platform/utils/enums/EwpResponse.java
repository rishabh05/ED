//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/14/2015
//===============================================================================
package com.eworkplaceapps.platform.utils.enums;

/**
 * This is used to handle exception
 */
public enum EwpResponse {

    SUCCESS(0),
    ERROR(1);

    private int id;

    EwpResponse(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
