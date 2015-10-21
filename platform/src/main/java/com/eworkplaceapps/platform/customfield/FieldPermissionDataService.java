//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/22/2015
//===============================================================================

package com.eworkplaceapps.platform.customfield;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.entity.BaseDataService;

/**
 * Field permission contains the custom field permissions.
 * Field Pemrission work in reverse order, It contains non-accessible field information.
 */
public class FieldPermissionDataService extends BaseDataService<FieldPermission> {

    FieldPermissionData dataDelegate = new FieldPermissionData();

    /**
     * Initializes a new instance of the FieldPermissionDataService class.
     */
    public FieldPermissionDataService() {
        super("FieldPermission");
    }

    @Override
    public BaseData getDataClass() {
        return dataDelegate;
    }

}
