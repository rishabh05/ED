//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/20/2015
//===============================================================================
package com.eworkplaceapps.platform.helper.picklistitem;

import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class PickListItemDataService extends BaseDataService<PickListItem> {

    private PickListItemData dataDelegate = new PickListItemData();

    // Initializes a new instance of the PicklistItemDataService class.
    public PickListItemDataService() {
        super("PicklistItemDataService");
    }

    public BaseData getDataClass() {
        return dataDelegate;
    }

    /**
     * GET PICK_LIST_ITEM list as ResultSet for given tenantid.
     *
     * @return
     */
    public Cursor getPicklistItemListForTenantAsResultSet(UUID tenantId) throws EwpException {
        Cursor response = dataDelegate.getPicklistListFromTenantAsResultSet(tenantId);
        // EwpError serviceError = DataServiceErrorHandler.defaultDataServiceErrorHandler().handleEwpError(response.error, EwpErrorHandler.ErrorPolicy.WRAP, new ArrayList<STRING>().add("ERROR at getPicklistItemListForTenantAsResultSet method in PicklistItemDataService"), EnumsForExceptions.ErrorModule.DATA_SERVICE);
        //return (response, serviceError);
        return response;
    }

    /**
     * It validate  PicklistItemm entity required fields and also validate duplicate name.
     * Name is required field for  PICK_LIST_ITEM. Also no duplicate name,text allow.
     *
     * @param entity
     */
    public void validateOnAddAndUpdate(BaseEntity entity) throws EwpException {
        boolean error = entity.validate();
        if (!error) {
            //TODO throw EwpException from here
            //return error;
        }
        // Check for duplicate picklist text.
        if (dataDelegate.isDuplicatePicklistItem((PickListItem) entity)) {
            Map<EnumsForExceptions.ErrorDataType, String[]> dicError = new HashMap<EnumsForExceptions.ErrorDataType, String[]>();
            dicError.put(EnumsForExceptions.ErrorDataType.DUPLICATE, new String[]{"Name"});
            //TODO throw EwpException from here
            //       return new EwpError(EnumsForExceptions.ErrorType.VALIDATION_ERROR, new ArrayList<STRING>().add(AppMessage.DUPLICATE_NAME),
            //       EnumsForExceptions.ErrorModule.DATA_SERVICE, dicError, 0);
        }
        //TODO throw EwpException from here
    }

    /**
     * It is used to get the system item list.
     *
     * @param name
     * @return
     */
    public Cursor getSystemPicklist(String name) throws EwpException {
        Cursor resultSet = dataDelegate.getSystemPicklist(name);
        // EwpError serviceError = DataServiceErrorHandler.defaultDataServiceErrorHandler().handleEwpError(response.error, EwpErrorHandler.ErrorPolicy.WRAP, new ArrayList<STRING>().add("ERROR at getSystemPicklist method in PicklistItemDataService"), EnumsForExceptions.ErrorModule.DATA_SERVICE);
        //return (resultSet, serviceError);
        return resultSet;
    }

    /**
     * It is used to get PICK_LIST_ITEM item list from name.
     *
     * @param name
     * @return
     */
    public List<PickListItem> getPicklistFromName(String name) throws EwpException {
        List<PickListItem> picklist = dataDelegate.getPicklistFromName(name);
        //  EwpError serviceError = DataServiceErrorHandler.defaultDataServiceErrorHandler().handleEwpError(response.error, EwpErrorHandler.ErrorPolicy.WRAP, new ArrayList<STRING>().add("ERROR at getPicklistFromName method in PicklistItemDataService"), EnumsForExceptions.ErrorModule.DATA_SERVICE);
        //return (response.entityList, serviceError)
        return picklist;
    }

    /**
     * It is used to get the custom picklist item list.
     *
     * @param parentId
     * @param ownerId
     * @param tenantId
     * @return
     */
    public Cursor getCustomPicklist(UUID parentId, UUID ownerId, UUID tenantId) throws EwpException {
        Cursor resultSet = dataDelegate.getCustomPicklist(parentId, ownerId, tenantId);
        //  EwpError serviceError = DataServiceErrorHandler.defaultDataServiceErrorHandler().handleEwpError(response.error, EwpErrorHandler.ErrorPolicy.WRAP, new ArrayList<STRING>().add("ERROR at getCustomPicklist method in PicklistItemDataService"), EnumsForExceptions.ErrorModule.DATA_SERVICE);
        // return (response.resultSet, serviceError)
        return resultSet;
    }

    /**
     * It is used to get the template picklist item.
     *
     * @return
     */
    public Cursor getTemplatePicklist(String name, UUID ownerId, UUID tenantId) throws EwpException {
        Cursor resultSet = dataDelegate.getTemplatePicklist(name, ownerId, tenantId);
        // EwpError serviceError = DataServiceErrorHandler.defaultDataServiceErrorHandler().handleEwpError(response.error, EwpErrorHandler.ErrorPolicy.WRAP, new ArrayList<STRING>().add("ERROR at getTemplatePicklist method in PicklistItemDataService"), EnumsForExceptions.ErrorModule.DATA_SERVICE);
        //return (resultSet, serviceError)
        return resultSet;
    }

}
