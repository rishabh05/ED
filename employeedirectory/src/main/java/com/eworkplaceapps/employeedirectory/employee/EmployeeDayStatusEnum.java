//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/21/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

/**
 * Employee Sub status enums.
 * Specifies the Employee sub status for a day.
 */
public enum EmployeeDayStatusEnum {
    NONE(0),
    ALL_DAY_TODAY(1),
    THIS_MORNING(2),
    THIS_AFTERNOON(3),
    ALL_DAY_TOMORROW(4),
    ANOTHER_TIME(5);

    private int id;

    EmployeeDayStatusEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        switch (this) {
            case THIS_MORNING:
                return "This Morning";
            case THIS_AFTERNOON:
                return "This Afternoon";
            case ANOTHER_TIME:
                return "Another Time";
            case ALL_DAY_TOMORROW:
                return "All Day Tomorrow";
            default:
                return "All Day Today";
        }
    }

    public static String getValue(String statusPeriod) {
        switch (statusPeriod) {
            case "2":
                return "This Morning";
            case "3":
                return "This Afternoon";
            case "4":
                return "All Day Tomorrow";
            case "1":
                return "All Day Today";
            default:
                return "Another Time";
        }
    }

    public static String[] getStatusList() {
        return new String[]{"All Day Today", "This Morning", "This Afternoon", "All Day Tomorrow", "Another Time"};
    }

    public static EmployeeDayStatusEnum[] getStatusEnumList() {
        return new EmployeeDayStatusEnum[]{THIS_MORNING, THIS_AFTERNOON, ALL_DAY_TODAY, ALL_DAY_TOMORROW};
    }

}
