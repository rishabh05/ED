package com.eworkplaceapps.platform.security;

import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.exception.DataServiceErrorHandler;
import com.eworkplaceapps.platform.exception.EwpErrorHandler;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class RoleLinkingDataService extends BaseDataService {

    RoleLinkingData dataDelegate = new RoleLinkingData();

    // Initializes a new instance of the RoleLinkingDataService class.
    public RoleLinkingDataService() {
        super("RoleLinkingDataService");
    }

    /**
     * Get RoleLinking list as FMResultSet for given tenantId.
     *
     * @param tenantId
     * @return Cursor
     */
    public Cursor getRoleLinkingListForTenantAsResultSet(UUID tenantId) throws EwpException {
        Cursor response = dataDelegate.getRoleLinkingListFromTenantIdAsResultSet(tenantId);
        if (response == null) {
            List<String> message = new ArrayList<String>();
            message.add("Error at getRoleLinkingListForTenantAsFMResultSet method in RoleLinkingDataService");
            EwpException serviceError = DataServiceErrorHandler.defaultDataServiceErrorHandler().handleEwpError(new EwpException(""), EwpErrorHandler.ErrorPolicy.WRAP, message,
                    EnumsForExceptions.ErrorModule.DATA_SERVICE);
        }
        return response;
    }

    /**
     * Get RoleLinking entity list for given entity.
     *
     * @param tenantId
     * @return List<RoleLinking>
     * @throws EwpException
     */
    public List<RoleLinking> getRoleListForTenant(UUID tenantId) throws EwpException {
        List<RoleLinking> response = dataDelegate.getRoleLinkingListFromTenantId(tenantId);
        if (response == null) {
            List<String> message = new ArrayList<String>();
            message.add("Error at getRoleListForTenant method in RoleLinkingDataService");
            EwpException serviceError = DataServiceErrorHandler.defaultDataServiceErrorHandler().handleEwpError(new EwpException(""), EwpErrorHandler.ErrorPolicy.WRAP, message, EnumsForExceptions.ErrorModule.DATA_SERVICE);
        }
        return response;
    }

    @Override
    public BaseData<RoleLinking> getDataClass() {
        return dataDelegate;
    }
}
