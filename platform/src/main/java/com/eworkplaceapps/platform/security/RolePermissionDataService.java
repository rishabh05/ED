//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/21/2015
//===============================================================================
package com.eworkplaceapps.platform.security;

import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.exception.DataServiceErrorHandler;
import com.eworkplaceapps.platform.exception.EwpErrorHandler;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.helper.EwpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class RolePermissionDataService extends BaseDataService<RolePermission> {

    private RolePermissionData dataDelegate;

    /**
     * Initializes a new instance of the RolePermissionDataService class.
     */
    public RolePermissionDataService() {
        super("ROLE_PERMISSION");
        dataDelegate = new RolePermissionData();
    }

    @Override
    public RolePermissionData getDataClass() {
        return dataDelegate;
    }

    // MARK: Class Members

    /**
     * GET ROLE_PERMISSION list as ResultSet for given tenantid.
     *
     * @param tenantId
     * @return
     */
    public Cursor getRoleListForTenantAsResultSet(UUID tenantId) {
        Cursor result = null;
        try {
            result = dataDelegate.getRolePermissionListFromTenantIdAsResultSet(tenantId);
        } catch (EwpException ewpError) {
            DataServiceErrorHandler.defaultDataServiceErrorHandler().handleEwpError(ewpError, EwpErrorHandler.ErrorPolicy.WRAP, new ArrayList<String>(), EnumsForExceptions.ErrorModule.DATA_SERVICE, "", "", 0, false);
        }
        return result;
    }

    /**
     * GET ROLE_PERMISSION entity list for an given tenantid.
     *
     * @param tenantId
     * @return
     */
    public List<RolePermission> getRoleListForTenant(UUID tenantId) {
        List<RolePermission> result = null;
        try {
            result = dataDelegate.getRolePermissionListFromTenantId(tenantId);
        } catch (EwpException ewpError) {
            DataServiceErrorHandler.defaultDataServiceErrorHandler().handleEwpError(ewpError, EwpErrorHandler.ErrorPolicy.WRAP, new ArrayList<String>(), EnumsForExceptions.ErrorModule.DATA_SERVICE, "", "", 0, false);
        }
        return result;
    }

    /**
     * Method will return the role permission on given userid, entitytype and applicationtype.
     *
     * @param userId
     * @param tenantId
     * @param applicationId
     * @param entityType
     * @param entityId
     * @return
     */
    public RolePermission getRolePermissionByUserAndTenantAndApplicationAndEntity(UUID userId, UUID tenantId, String applicationId, int entityType, UUID entityId) throws EwpException {
        RolePermission result = null;
        result = dataDelegate.getRolePermissionByUserAndTenantAndApplicationAndSourceIdAndEntityType(userId, tenantId, applicationId, entityType, entityId);
        return result;
    }


    /**
     * Method will return the role permission on logged in userid and given entitytype.
     *
     * @param entityType
     * @return
     */
    public RolePermission getRolePermissionByLoginUserWithEntityType(int entityType) {
        RolePermission result = null;
        try {
            result = dataDelegate.getRolePermissionByLoginUserByEntityType(EwpSession.getSharedInstance().getUserId(), EwpSession.getSharedInstance().getTenantId(), entityType);
        } catch (EwpException ewpError) {
            DataServiceErrorHandler.defaultDataServiceErrorHandler().handleEwpError(ewpError, EwpErrorHandler.ErrorPolicy.WRAP, new ArrayList<String>(), EnumsForExceptions.ErrorModule.DATA_SERVICE, "", "", 0, false);
        }
        return result;
    }
}
