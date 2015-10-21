//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/21/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import android.database.Cursor;

import com.eworkplaceapps.platform.utils.Utils;
import com.eworkplaceapps.platform.utils.enums.DatabaseOperationType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class EditEmployeeTeam implements Serializable{

    private String teamName = "";
    private UUID teamId = Utils.emptyUUID();
    private DatabaseOperationType operationType = DatabaseOperationType.NONE;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public UUID getTeamId() {
        return teamId;
    }

    public void setTeamId(UUID teamId) {
        this.teamId = teamId;
    }

    public DatabaseOperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(DatabaseOperationType operationType) {
        this.operationType = operationType;
    }


    public static List<EditEmployeeTeam> setPropertiesAndGetEditEmployeeTeamList(Cursor cursor) {
        List<EditEmployeeTeam> viewEmployeeContactList = new ArrayList<>();
        EditEmployeeTeam viewEmployeeContact;
        while (cursor.moveToNext()) {
            viewEmployeeContact = new EditEmployeeTeam();
            String teamName = cursor.getString(cursor.getColumnIndex("Name"));
            viewEmployeeContact.setTeamName(teamName);
            String teamId = cursor.getString(cursor.getColumnIndex("TeamId"));
            if (teamId != null && !"".equals(teamId)) {
                viewEmployeeContact.setTeamId(UUID.fromString(teamId));
            }
            viewEmployeeContactList.add(viewEmployeeContact);
        }
        return viewEmployeeContactList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EditEmployeeTeam that = (EditEmployeeTeam) o;

        return teamId.equals(that.teamId);

    }

    @Override
    public int hashCode() {
        return teamId.hashCode();
    }
}
