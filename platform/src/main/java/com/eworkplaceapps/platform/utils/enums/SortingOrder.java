//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/14/2015
//===============================================================================
package com.eworkplaceapps.platform.utils.enums;

/**
 * Specifies the property constant in which to sorting order of a list.
 */
public enum SortingOrder {

    // Sort order is none.
    NONE(0),
    // Sort on ascending order.
    ASC(1),
    // Sort on descending order.
    DESC(2);

    private int id;

    SortingOrder(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        switch (this) {
            case ASC:
                return "ASC";
            case DESC:
                return "DESC";
            default:
                return "NONE";
        }
    }
}
