//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/14/2015
//===============================================================================
package com.eworkplaceapps.platform.utils.enums;

/**
 * // Operation Type Enum
 */
public enum DatabaseOperationType {

    // No operation is performed.
    NONE(0),

    // Enum to represent GET operation.
    GET(1),

    // Enum to represent ADD operation.
    ADD(2),

    // Enum to represent UPDATE operation.
    UPDATE(3),

    // Enum to represent DELETE operation.
    DELETE(4);

    private int id;

    DatabaseOperationType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
