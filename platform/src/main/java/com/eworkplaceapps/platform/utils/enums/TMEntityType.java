//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/14/2015
//===============================================================================
package com.eworkplaceapps.platform.utils.enums;


public enum TMEntityType {

    NONE(0), ALL_TM(1110), TASK(1101);

    private int id;

    TMEntityType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        switch (this) {
            case TASK:
                return "Task";
            default:
                return "NONE";
        }
    }
}
