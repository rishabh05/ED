//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/26/2015
//===============================================================================
package com.eworkplaceapps.platform.utils.enums;

/**
 *
 */
public enum RequesterType {
    OTHER(1), WC(2), I_PHONE(3), I_PAD(4), ANDROID(5);
    private int id;

    RequesterType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
