//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Sourabh Agrawal
// Original DATE:  10/7/2015.
//===============================================================================
package com.eworkplaceapps.employeedirectory.common;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.entity.BaseDataService;


public class EventSubscriptionDataService extends BaseDataService {
    /**
     * Initializing the data service entity-type
     *
     * @param entityName of STRING
     */
    public EventSubscriptionDataService(String entityName) {
        super(entityName);
    }

    @Override
    public BaseData getDataClass() {
        return null;
    }
}
