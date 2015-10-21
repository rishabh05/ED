//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Sourabh Agrawal
// Original DATE: 09/23/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.common;

import com.androidquery.AQuery;
import com.eworkplaceapps.employeedirectory.department.DepartmentDataService;
import com.eworkplaceapps.employeedirectory.department.DepartmentModel;
import com.eworkplaceapps.employeedirectory.employee.AddEmployeeAction;
import com.eworkplaceapps.employeedirectory.employee.EmployeeGroupDataService;
import com.eworkplaceapps.employeedirectory.employee.TeamModel;
import com.eworkplaceapps.employeedirectory.location.LocationDataService;
import com.eworkplaceapps.employeedirectory.location.LocationModel;
import com.eworkplaceapps.platform.authentication.BaseService;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.requesthandler.RequestCallback;
import com.eworkplaceapps.platform.utils.enums.DatabaseOperationType;
import com.eworkplaceapps.platform.utils.enums.EDEntityType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import static com.eworkplaceapps.platform.utils.enums.EDEntityType.LOCATION;

public class EDServices extends BaseService {

    public EDServices() {
    }

    /**
     * Method is used to get operational url.
     *
     * @param edEntityType  is a Type of EntityType.
     * @param operationType
     * @return return server url.
     */
    private String getOperationalUrl(EDEntityType edEntityType, DatabaseOperationType operationType) {
        String url = getFortyOneUrl();
        switch (edEntityType) {
            case LOCATION:
                url += "locations";
                break;
            case DEPARTMENT:
                url += "departments";
                break;
            case EMPLOYEE_GROUP:
                url += "teams";
                break;
            case EMPLOYEE:
                url += "invitedemployees";
                break;
            default:
                return "";
        }
        if (operationType == DatabaseOperationType.DELETE) {
            url += "/delete";
        }
        return url;
    }

    /**
     * Method is used to add/update entities. It will call rest service to do the operation.
     *
     * @param modelObject     is a collection of parent and child entties valiue to add/update.
     * @param edEntityType    EDEntityType is a Type of EntityType.
     * @param operationType
     * @param requestCallback is a callback, That will call once response come after rest service call.
     * @throws EwpException
     * @throws JSONException
     */
    public void addOrUpdate(Object modelObject, EDEntityType edEntityType, DatabaseOperationType operationType, final RequestCallback requestCallback) throws EwpException, JSONException {
        // departments/delete/{departmentId}
        // It will give the server url to call.
        String urlString = getOperationalUrl(edEntityType, operationType);
        String httpMethod;
        // Method is used to get the type of method. Method can be PUT, PUST or DELETE.
        if (operationType == DatabaseOperationType.ADD) {
            httpMethod = "POST";
        } else if (operationType == DatabaseOperationType.UPDATE) {
            httpMethod = "PUT";
        } else {
            httpMethod = "DELETE";
            urlString += "/";
        }
        new NetworkAsyncTask(urlString, httpMethod, edEntityType.toString(), getModelObjectAsJsonObject(modelObject, edEntityType).toString()).execute(requestCallback);


    }

    /**
     * Method is used to Delete entities. It will call rest service to do the operation.
     *
     * @param id
     * @param edEntityType
     * @param requestCallback is a callback, That will call once response come after rest service call.
     * @throws EwpException
     * @throws JSONException
     */
    public void delete(UUID id, EDEntityType edEntityType, final RequestCallback requestCallback) throws EwpException, JSONException {
        // departments/delete/{departmentId}
        // It will give the server url to call.
        String urlString = getOperationalUrl(edEntityType, DatabaseOperationType.DELETE);
        urlString += "/" + id.toString();
        new NetworkAsyncTask(urlString, "DELETE", edEntityType.toString() + "_Delete", null).execute(requestCallback);

    }


    /**
     * Method will validate the model object  if object is not valid then return EwpError.
     * If model object is valid then return Convert model object into NSData object and return.
     *
     * @param modelObject  is a collection of parent and child entties valiue to add/update.
     * @param edEntityType edEntityType is a Type of EntityType.
     * @return JSONObject
     * @throws EwpException
     * @throws JSONException
     */
    private JSONObject getModelObjectAsJsonObject(Object modelObject, EDEntityType edEntityType) throws EwpException, JSONException {

        switch (edEntityType) {
            case LOCATION:
                LocationModel loc = (LocationModel) modelObject;
                LocationDataService locationDataService = new LocationDataService();
                //Validate LocationModel
               // locationDataService.validateLocationModel(loc);
                // Convert loc object to NSData and return.
                return loc.toJsonObject();

            case DEPARTMENT:
                DepartmentModel dept = (DepartmentModel) modelObject;
                DepartmentDataService departmentDataService = new DepartmentDataService();
                //Validate DepartmentModel
               // departmentDataService.validateOnAddAndUpdate(dept.getDepartment());
                return dept.toJsonObject();

            case EMPLOYEE_GROUP:
                TeamModel team = (TeamModel) modelObject;
                EmployeeGroupDataService teamDataService = new EmployeeGroupDataService();
                //Validate TeamModel
                //teamDataService.validateOnAddAndUpdate(team.getTeam());
                // Convert team object to NSData and return.
                return team.toJsonObject();

            case EMPLOYEE:
                AddEmployeeAction addEmp = (AddEmployeeAction) modelObject;
                addEmp.validate();
                return addEmp.toInvitedEmployeeObject();

            default:
                break;
        }

        return null;
    }
}
