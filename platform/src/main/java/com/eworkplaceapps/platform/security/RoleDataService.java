//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/16/2015
//===============================================================================
package com.eworkplaceapps.platform.security;

import android.database.Cursor;

import com.eworkplaceapps.platform.db.DatabaseOps;
import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.exception.DataServiceErrorHandler;
import com.eworkplaceapps.platform.exception.EwpErrorHandler;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.utils.AppMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * It expand the BaseDataService to provide services to ROLE entity.
 * It is used to retrieve list of ROLE from tenantid.
 */
public class RoleDataService extends BaseDataService<Role> {


    // Initializes a new instance of the RoleDataService class.

    public RoleDataService() {
        super("ROLE");
    }

    public BaseData getDataClass() {
        return dataDelegate;
    }

    RoleData dataDelegate = new RoleData();

    /**
     * It validate an entity and returns result as EwpError object variable.
     * Returns success(ErrorType) for successfully validation and array for error message.
     */
    public Boolean validateOnAddAndUpdate(Role entity) throws EwpException {
        try {
            entity.validate();
            Role role = entity;
            boolean resultTuple = dataDelegate.isRoleExist(role.getName(), role.getTenantId(), role.getApplicationId());

            if (resultTuple) {
                List<String> message = new ArrayList<String>();
                Map<EnumsForExceptions.ErrorDataType, String[]> dicError = new HashMap<EnumsForExceptions.ErrorDataType, String[]>();
                message.add(AppMessage.DUPLICATE_NAME);
                dicError.put(EnumsForExceptions.ErrorDataType.DUPLICATE, new String[]{"Name"});
                throw new EwpException(new EwpException("Validation error in RoleDataService"), EnumsForExceptions.ErrorType.VALIDATION_ERROR, message, EnumsForExceptions.ErrorModule.DATA_SERVICE, dicError, 0);
            }
        } catch (EwpException ewpError) {
            DataServiceErrorHandler.defaultDataServiceErrorHandler().logError(ewpError);
            throw ewpError;
        }
        return true;
    }

    /**
     * GET ROLE list as ResultSet for given tenantId.
     *
     * @param tenantId
     * @return
     */
    public Cursor getRoleListForTenantAsResultSet(UUID tenantId) throws EwpException {
        Cursor response = null;
        try {
            response = dataDelegate.getRoleListFromTenantIdAsResultSet(tenantId);
        } catch (EwpException ewpError) {
            List<String> messages = new ArrayList<String>();
            messages.add("ERROR at getRoleListForTenantAsResultSet method in RoleDataService");
            DataServiceErrorHandler.defaultDataServiceErrorHandler().handleEwpError(ewpError, EwpErrorHandler.ErrorPolicy.WRAP, messages, EnumsForExceptions.ErrorModule.DATA_SERVICE);
            throw ewpError;
        }
        return response;
    }

    /**
     * GET ROLE entity list for an given TENANT id.
     *
     * @param tenantId
     * @return
     */
    public List<Role> getRoleListForTenant(UUID tenantId) throws EwpException {
        List<Role> response = null;
        try {
            response = dataDelegate.getRoleListFromTenantId(tenantId);
        } catch (EwpException ewpError) {
            List<String> messages = new ArrayList<String>();
            messages.add("ERROR at getRoleListForTenant method in RoleDataService");
            DataServiceErrorHandler.defaultDataServiceErrorHandler().handleEwpError(ewpError, EwpErrorHandler.ErrorPolicy.WRAP, messages, EnumsForExceptions.ErrorModule.DATA_SERVICE);
            throw ewpError;
        }
        return response;
    }

    /**
     * add ROLE and ROLE Permission
     *
     * @param role
     * @param rolePermission
     * @return
     */
    public Object addRoleAndRolePermission(Role role, RolePermission rolePermission) throws EwpException {
        Object result = null;
        try {
            DatabaseOps.defaultDatabase().beginTransaction();
            result = add(role);
        } catch (Exception ex) {
            DatabaseOps.defaultDatabase().getWritableDatabase().endTransaction();
            DataServiceErrorHandler.defaultDataServiceErrorHandler().handleEwpError(new EwpException(ex), EwpErrorHandler.ErrorPolicy.WRAP, null, EnumsForExceptions.ErrorModule.DATA_SERVICE);
            throw ex;
        }
        UUID roleId = (UUID) result;
        RolePermissionDataService service = new RolePermissionDataService();
        rolePermission.setRoleId(roleId);
        Object resultRolePermission = null;
        try {
            resultRolePermission = service.add(rolePermission);
        } catch (Exception ex) {
            DatabaseOps.defaultDatabase().getWritableDatabase().endTransaction();
            DataServiceErrorHandler.defaultDataServiceErrorHandler().handleEwpError(new EwpException(ex), EwpErrorHandler.ErrorPolicy.WRAP, null, EnumsForExceptions.ErrorModule.DATA_SERVICE);
            throw ex;
        }
        DatabaseOps.defaultDatabase().commitTransaction();
        return roleId;
    }

    /**
     * returns deafault role
     *
     * @param roleName
     * @return
     * @throws EwpException
     */
    public Role getDefaultRoleByName(String roleName) throws EwpException {
        return dataDelegate.getDefaultRoleByName(roleName);
    }

    /**
     * return default employee role
     *
     * @return
     * @throws EwpException
     */
    public Role getDefaultEmployeeRole() throws EwpException {
        return getDefaultRoleByName("EMPLOYEE");
    }
}
