package com.eworkplaceapps.employeedirectory.employee;

import android.database.Cursor;

import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * It is a model class, which will be used to view as memeber item in list.
 * It will merge the employee, team and status information.
 */
public class EmployeeTeamMemberItem {

    private UUID teamId = Utils.emptyUUID();
    private UUID employeeId = Utils.emptyUUID();
    private String teamName = "";
    private String employeeName = "";
    private EmployeeStatusEnum employeeStatus;
    private String jobTitle = "";
    private String picture = "";


    public static List<EmployeeTeamMemberItem> setPropertiesAndGetEditEmployeeTeamMemberList(Cursor resultSet, boolean excludeSelf) {
        List<EmployeeTeamMemberItem> employeeTeamMemberItemList = new ArrayList<EmployeeTeamMemberItem>();
        String selfId = EwpSession.getSharedInstance().getTenantId().toString();
        while (resultSet.moveToNext()) {
            // Excluding the slogged in user member.
            if (excludeSelf) {
                String userId = resultSet.getString(resultSet.getColumnIndex("UserId"));
                if (userId != null && !"".equals(userId)) {
                    if (userId.equalsIgnoreCase(selfId)) {
                        continue;
                    }
                }
            }
            EmployeeTeamMemberItem favEmp = setFavoriteEmployeeProperties(resultSet);
            employeeTeamMemberItemList.add(favEmp);
        }
        return employeeTeamMemberItemList;
    }

    private static EmployeeTeamMemberItem setFavoriteEmployeeProperties(Cursor resultSet) {
        EmployeeTeamMemberItem viewEmployee = new EmployeeTeamMemberItem();
        String teamId = resultSet.getString(resultSet.getColumnIndex("TeamId"));
        if (teamId != null && !"".equals(teamId)) {
            viewEmployee.teamId = UUID.fromString(teamId);
        }
        String firstName = resultSet.getString(resultSet.getColumnIndex("FullName"));
        if (firstName != null && !"".equals(firstName)) {
            viewEmployee.employeeName = firstName;
        }
        String jobTitle = resultSet.getString(resultSet.getColumnIndex("JobTitle"));
        if (jobTitle != null && !"".equals(jobTitle)) {
            viewEmployee.jobTitle = jobTitle;
        }
        int status = resultSet.getInt(resultSet.getColumnIndex("Status"));
        viewEmployee.employeeStatus = EmployeeStatusEnum.values()[status];
        viewEmployee.picture = resultSet.getString(resultSet.getColumnIndex("Picture"));
        return viewEmployee;
    }

}
