//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/22/2015
//===============================================================================

package com.eworkplaceapps.platform.customfield;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.entity.BaseDataService;

public class EntityFieldDisplayPropertyDataService extends BaseDataService<EntityFieldDisplayProperty> {


    EntityFieldDisplayPropertyData dataDelegate = new EntityFieldDisplayPropertyData();

    /**
     * Initializes a new instance of the EntityFieldDisplayPropertyDataService class.
     */
    public EntityFieldDisplayPropertyDataService() {
        super("EntityFieldDisplayProperty");
    }

    @Override
    public BaseData getDataClass() {
        return dataDelegate;
    }

    //----------------------- Begin Class Methods ---------------
}
