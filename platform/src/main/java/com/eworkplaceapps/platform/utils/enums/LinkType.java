//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/19/2015
//===============================================================================
package com.eworkplaceapps.platform.utils.enums;

/**
 *
 */
public enum LinkType {
    /// Follow Up link type.
    FOLLOW_UP(1),
    /// Favorite link type.
    FAVOURITE(2);
    private int id;

    LinkType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
