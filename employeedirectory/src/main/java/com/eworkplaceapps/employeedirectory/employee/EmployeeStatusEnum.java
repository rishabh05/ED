//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/21/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

/**
 * EmployeeStaus enums.
 * Specifies the Employee Status for a day.
 */
public enum EmployeeStatusEnum {
    NONE(0),
    OUT_OF_OFFICE(1),
    REMOTE(2),
    SICK(3),
    VACATION(4),
    BUSINESS_TRIP(5), OFF_SITE_MEETING(6),
    OTHER(7);

    private int id;

    EmployeeStatusEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        switch (this) {
            case OUT_OF_OFFICE:
                return "Out of Office";
            case REMOTE:
                return "Remote";
            case SICK:
                return "Sick";
            case VACATION:
                return "Vacation";
            case BUSINESS_TRIP:
                return "Business Trip";
            case OFF_SITE_MEETING:
                return "Offsite Meeting";
            case OTHER:
                return "Other";
            default:
                return "none";
        }
    }

    public static String[] getStatusList() {
        return new String[]{"Out of Office", "Remote", "Sick", "Vacation", "Business Trip", "Offsite Meeting", "Other"};
    }

    public static EmployeeStatusEnum[] getStatusEnumList() {
        return new EmployeeStatusEnum[]{OUT_OF_OFFICE, REMOTE, SICK, VACATION, BUSINESS_TRIP, OFF_SITE_MEETING, OTHER};
    }
}