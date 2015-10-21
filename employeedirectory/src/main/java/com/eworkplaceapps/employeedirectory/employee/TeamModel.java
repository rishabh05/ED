//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 6/01/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import com.eworkplaceapps.employeedirectory.department.Department;
import com.eworkplaceapps.platform.communication.Communication;
import com.eworkplaceapps.platform.utils.enums.DatabaseOperationType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class TeamModel {

    private EmployeeGroup team;
    private List<Communication> communications = new ArrayList<Communication>();
    private List<EmployeeQuickView> employeeList = new ArrayList<EmployeeQuickView>();

    public EmployeeGroup getTeam() {
        return team;
    }

    public void setTeam(EmployeeGroup team) {
        this.team = team;
    }

    public List<Communication> getCommunications() {
        return communications;
    }

    public void setCommunications(List<Communication> communications) {
        this.communications = communications;
    }

    public List<EmployeeQuickView> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<EmployeeQuickView> employeeList) {
        this.employeeList = employeeList;
    }

    /**
     * @return
     * @throws JSONException
     */
    public JSONObject toJsonObject() throws JSONException {
        JSONObject dict = new JSONObject();
        dict.put("TeamDetailModel", getTeamDetailAsDictionary());
        dict.put("CommunicationList", Communication.getCommunicationListAsDictionary(this.communications));
        dict.put("MembersList", getEmployeeListArray());
        return dict;
    }

    /**
     * @return
     * @throws JSONException
     */
    public JSONObject getTeamDetailAsDictionary() throws JSONException {
        JSONObject dict = new JSONObject();
        EmployeeGroup team = this.team;
        if (team != null) {
            dict.put("TeamId", team.getEntityId().toString());
            dict.put("Name", team.getName().toString());
            dict.put("Picture", team.getPicture().toString());
            dict.put("Description", team.getDescription().toString());
            dict.put("Manager", team.getManagerId().toString());
            dict.put("ManagerName", "");
        }
        if (team.getEntityId()!=null && team.getEntityId().toString().equalsIgnoreCase("00000000-0000-0000-0000-000000000000") ) {
            dict.put("OperationType", DatabaseOperationType.ADD.getId());
        } else {
            dict.put("OperationType", DatabaseOperationType.UPDATE.getId());
        }

        return dict;
    }

    /**
     * @return JSONArray of EmployeeList
     * @throws JSONException
     */
    private JSONArray getEmployeeListArray() throws JSONException {
        JSONArray dictArray = new JSONArray();
        if (employeeList != null && !employeeList.isEmpty()) {
            for (int i = 0; i < employeeList.size(); i++) {
                JSONObject dict = new JSONObject();
                dict.put("EmployeeId", employeeList.get(i).getEmployeeId().toString());
                dict.put("OperationType", employeeList.get(i).getOperationType().getId());
                dictArray.put(dict);
            }
        }

        return dictArray;
    }
}