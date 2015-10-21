//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/18/2015
//===============================================================================
package com.eworkplaceapps.platform.address;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.utils.Utils;
import com.eworkplaceapps.platform.utils.enums.DatabaseOperationType;

import java.util.UUID;

/**
 * It expand the BaseDataService to provide services to Address entities.
 * It provides the detail address information.
 * It contains the detail information about an address with reference to an entity.
 */
public class AddressDataService extends BaseDataService<Address> {
    private AddressData dataDelegate = new AddressData();


    /**
     * Initializes a new instance of the AddressDataService class.
     */
    public AddressDataService() {
        super("AddressDataService");
    }

    @Override
    public BaseData<Address> getDataClass() {
        return dataDelegate;
    }

    /**
     * Method is used to get address from EntityType and EntiyId
     * :param: sourceEntityid, It source entityId, For which we have saved the address
     * :param: sourceEntityType, It source entity type, For which we have saved the address
     *
     * @param sourceEntityId
     * @param sourceEntityType
     * @return
     * @throws EwpException
     */
    public Address getAddressFromSourceEntityIdAndType(UUID sourceEntityId, int sourceEntityType) throws EwpException {
        Address response = dataDelegate.getAddressFromSourceEntityIdAndType(sourceEntityId, sourceEntityType);
        return response;
    }

    /**
     * Method is used to Add/update/delete communication list.
     *
     * @param sourceId
     * @param sourceEntityType
     * @return
     */
    public boolean deleteAddressFromSourceEntityIdAndType(UUID sourceId, int sourceEntityType) {
        return dataDelegate.deleteAddressFromSourceEntityIdAndType(sourceId, sourceEntityType);
    }

    /**
     * Add/Update note entity.
     *
     * @param entity
     * @param entityId
     * @param sourceEntityType
     * @param createdById
     */
    public void addUpdateAddress(Address entity, UUID entityId, int sourceEntityType, UUID createdById) throws EwpException {
        if (entity.getEntityId().equals(Utils.emptyUUID()) || entity.getLastOperationType() == DatabaseOperationType.ADD) {
            entity.setLastOperationType(DatabaseOperationType.ADD);
            entity.setSourceEntityType(sourceEntityType);
            entity.setSourceEntityId(entityId);
            entity.setTenantId(EwpSession.getSharedInstance().getTenantId());
            entity.setCreatedBy(createdById.toString());
        } else {
            entity.setLastOperationType(DatabaseOperationType.UPDATE);
        }
        if (entity.getLastOperationType() == DatabaseOperationType.ADD) {
            super.add(entity);
        } else {
            super.update(entity);
        }
    }

}
