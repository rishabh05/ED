//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Sourabh Agrawal
// Original DATE: 19/08/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.exception.DataServiceErrorHandler;
import com.eworkplaceapps.platform.exception.EwpErrorHandler;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;

import java.util.ArrayList;
import java.util.List;

public class EmployeeStatusConfigDataService extends BaseDataService {
    private EmployeeStatusConfigData dataDelegate = new EmployeeStatusConfigData();

    /**
     * Initializes a new instance of the EmployeeStatusDataService class.
     */
    public EmployeeStatusConfigDataService() {
        super("EmployeeStatusDataService");
    }

    @Override
    public BaseData getDataClass() {
        return dataDelegate;
    }
    public List<EmployeeStatusConfig> getStatusList() throws EwpException {
        List<EmployeeStatusConfig> resultTuple = dataDelegate.getStatusList();
        if (resultTuple == null) {
            List<String> message = new ArrayList<String>();
            throw DataServiceErrorHandler.defaultDataServiceErrorHandler().handleEwpError(new EwpException("EmployeeStatusConfig is null"), EwpErrorHandler.ErrorPolicy.WRAP, message, EnumsForExceptions.ErrorModule.DATA_SERVICE, "getStatusList", "EmployeeStatusConfigDataService", 0, false);
        }
        return resultTuple;
    }
}
