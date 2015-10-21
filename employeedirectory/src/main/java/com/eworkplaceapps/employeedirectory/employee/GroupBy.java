package com.eworkplaceapps.employeedirectory.employee;

/**
 *
 */
public enum GroupBy {
    NONE("None"),
    LOCATION("Location"),
    DEPARTMENT("Department"),
    TEAM("Team"),
    FAVOURITE("Favorite"),
    EMPLOYEE_STATUS("EmployeeStatus");

    private String groupBy = "";

    GroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public String getGroupBy() {
        return groupBy;
    }
}
