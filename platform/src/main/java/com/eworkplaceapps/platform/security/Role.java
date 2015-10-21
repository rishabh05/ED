//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: <Parikshit Patel>
// Original DATE: <4/17/2015.>
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
 * It expand the BaseEntity object. It contain all necessary properties required for ROLE object.
 * It override the validates method to validates ROLE entity.
 */

public class Role extends BaseEntity {
    private static final String ROLE_ENTITY_NAME = "ROLE";

    public Role() {
        super(ROLE_ENTITY_NAME);
    }

    /**
     * Create ROLE object and returns created object.
     *
     * @return
     */
    public static Role createEntity() {
        return new Role();
    }

    // ------------------- Begin Property section -----------------------

    private String name;

    /**
     * @return
     */
    public void setName(String name) {
        super.setPropertyChanged(this.name, name);
        this.name = name;
    }


    private String roleKey;

    /**
     * ROLE Key.
     *
     * @return
     */
    public void setRoleKey(String roleKey) {

        super.setPropertyChanged(this.roleKey, roleKey);
        this.roleKey = roleKey;
    }

    private boolean active = false;

    /**
     * True, if role is active else false.
     *
     * @return
     */
    public void setActive(boolean active) {
        super.setPropertyChanged(this.isActive(), active);
        this.active = active;
    }


    public String getName() {
        return name;
    }

    public String getRoleKey() {
        return roleKey;
    }

    public boolean isActive() {
        return active;
    }


    private String applicationId = "";

    // TENANT Id.
    private UUID tenantId;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        setPropertyChanged(this.applicationId, applicationId);
        this.applicationId = applicationId;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        setPropertyChanged(this.tenantId, tenantId);
        this.tenantId = tenantId;
    }

    // ------------------- End Property section -----------------------

    /**
     * It validate role entity.
     *
     * @return
     */
    @Override
    public Boolean validate() throws EwpException {
        List<String> message = new ArrayList<>();
        Map<EnumsForExceptions.ErrorDataType, String[]> dicError = new HashMap<EnumsForExceptions.ErrorDataType, String[]>();

        if (this.name == null && "".equals(this.name)) {
            message.add(AppMessage.NAME_REQUIRED);
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"Name"});
        }

        // Amit: Below is reason, Why i am checking length. 
        // Hari: Discuss it with the server team if they can remove this restriction. Then discuss it with me.
        // SQL-Server name field do not allow more then 200 character. So that for successfull synching, We need to apply length check on name field.
        if (this.name.length() > 200) {
            message.add("ROLE name should be less than or equal to 200 characters.");
            dicError.put(EnumsForExceptions.ErrorDataType.LENGTH, new String[]{"Name"});
        }

        if (message.isEmpty()) {
            return true;
        } else {
            throw new EwpException(new EwpException("Validation Exception in ROLE "), EnumsForExceptions.ErrorType.VALIDATION_ERROR, message, EnumsForExceptions.ErrorModule.DATA_SERVICE, dicError, 0);
        }
    }
}