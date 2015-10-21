//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/14/2015
//===============================================================================
package com.eworkplaceapps.platform.utils.enums;


public enum EDEntityType {
    NONE(0), ALL_ED(1000), EMPLOYEE(1001), EMPLOYEE_STATUS(1002), EMPLOYEE_GROUP(1003), EMPLOYEE_GROUP_MEMBER(1004),

    /// The TERMINOLOGY entity.
    TERMINOLOGY(1005),

    /// Entity Field Details
    ENTITY_FIELD_DETAILS(1006),

    /// ROLE
    ROLE(1007),

    /// The EMPLOYEE Diretory APPLICATION Notification enttiy.
    ED_NOTIFICATION(1008),

    /// EMPLOYEE Group Follower
    GROUP_FOLLOWER(1009),

    /// PING message.
    PING_MESSAGE(1010),
    /// Location
    LOCATION(1011),
    /// Department
    DEPARTMENT(1012),
    /// Its like a Emergency Contact.
    EMPLOYEE_CONTACT(1013);

    private int id;

    EDEntityType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        switch (this) {
            case EMPLOYEE:
                return "EMPLOYEE";
            case EMPLOYEE_GROUP:
                return "EMPLOYEE_GROUP";
            case EMPLOYEE_GROUP_MEMBER:
                return "EMPLOYEE_GROUP_MEMBER";
            case EMPLOYEE_STATUS:
                return "EMPLOYEE_STATUS";
            case TERMINOLOGY:
                return "TERMINOLOGY";
            case ENTITY_FIELD_DETAILS:
                return "ENTITY_FIELD_DETAILS";
            case ROLE:
                return "ROLE";
            case LOCATION:
                return "Location";
            case DEPARTMENT:
                return "Department";
            case ED_NOTIFICATION:
                return "ED_NOTIFICATION";
            case GROUP_FOLLOWER:
                return "GROUP_FOLLOWER";
            case PING_MESSAGE:
                return "PING_MESSAGE";
            default:
                return "NONE";
        }
    }
}
