//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/21/2015
//===============================================================================
package com.eworkplaceapps.platform.userpreference;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.exception.EwpException;

import java.util.UUID;

/**
 * UserPreferenceDataService
 */
public class UserPreferenceDataService extends BaseDataService<UserPreference> {

    private UserPreferenceData dataDelegate = new UserPreferenceData();

    public UserPreferenceDataService() {
        super("UserPreferenceDataService");
    }

    @Override
    public BaseData<UserPreference> getDataClass() {
        return dataDelegate;
    }

    /**
     * @param userId        UUID
     * @param applicationId STRING
     * @param prefName1     STRING
     * @param prefName2     STRING
     * @return USER_PREFERENCE
     * @throws EwpException
     */
    public UserPreference getUserPreference(UUID userId, String applicationId, String prefName1, String prefName2) throws EwpException {
        UserPreference preference = dataDelegate.getUserPreference(userId, applicationId, prefName1, prefName2);
        return preference;
    }
}
