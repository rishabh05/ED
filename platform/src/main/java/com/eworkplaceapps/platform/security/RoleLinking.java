//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/16/2015
//===============================================================================
package com.eworkplaceapps.platform.security;

import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.utils.AppMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * It encapsulates all data for ROLE_LINKING entity.
 * 1) It validates ROLE_LINKING entity.
 */
public class RoleLinking extends BaseEntity {

    private static final String ROLE_LINKING_ENTITY_NAME = "ROLE_LINKING";

    public RoleLinking() {
        super(ROLE_LINKING_ENTITY_NAME);
    }

    /**
     * Create Task object and return created object.
     *
     * @return
     */
    public static RoleLinking createEntity() {
        return new RoleLinking();
    }

    // Contain the linking in between of role permission and role linking.
    private UUID roleId;

    // Associated USER Id with role and role permission.
    private UUID userId;

    // TENANT Id.
    private UUID tenantId;

    // Entity reference object id. For example, Permission may be diffrent for diffrent project or diffrent tenant.
    private UUID sourceId;

    private int sourceType;

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        setPropertyChanged(this.roleId, roleId);
        this.roleId = roleId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        setPropertyChanged(this.userId, userId);
        this.userId = userId;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        setPropertyChanged(this.tenantId, tenantId);
        this.tenantId = tenantId;
    }

    public UUID getSourceId() {
        return sourceId;
    }

    public void setSourceId(UUID sourceId) {
        setPropertyChanged(this.sourceId, sourceId);
        sourceId = sourceId;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        setPropertyChanged(this.sourceType, sourceType);
        this.sourceType = sourceType;
    }

    /**
     * Validate ROLE_LINKING entity.
     */
    @Override
    public Boolean validate() throws EwpException {
        List<String> message = new ArrayList<String>();
        Map<EnumsForExceptions.ErrorDataType, String[]> dicError = new HashMap<EnumsForExceptions.ErrorDataType, String[]>();
        if (this.roleId == null) {
            message.add(AppMessage.ROLE_ID_REQUIRED);
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"ROLE"});
        }
        if (message.isEmpty()) {
            return true;
        } else {
            throw new EwpException(new EwpException("Validation Exception in ROLE_LINKING"), EnumsForExceptions.ErrorType.VALIDATION_ERROR, message, EnumsForExceptions.ErrorModule.DATA_SERVICE, dicError, 0);
        }
    }
}
