//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/19/2015
//===============================================================================
package com.eworkplaceapps.platform.userentitylink;

import com.eworkplaceapps.platform.db.DatabaseOps;
import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.security.EntityAccess;
import com.eworkplaceapps.platform.utils.enums.LinkType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * It expand the BaseDataService to provide services to PFUserEntityLink entities.
 * It contains the link information from an entity. A UserEntityLink may link from an entity by following information like
 * source type, source id, linktype and tenantid.
 */
public class PFUserEntityLinkDataService extends BaseDataService<PFUserEntityLink> {
    private PFUserEntityLinkData dataDelegate = new PFUserEntityLinkData();

    /**
     * Initializes a new instance of the EmployeeStatusDataService class.
     */
    public PFUserEntityLinkDataService() {
        super("PFUserEntityLinkDataService");
    }

    @Override
    public BaseData<PFUserEntityLink> getDataClass() {
        return dataDelegate;
    }

    /**
     * Method is used to add UserEntityLink from source type, source id, linktype.
     * UserEntityLink can save 3 type of value for an entity.
     * :param: LinkType: Type of link. (How a userentity link from an entity). for example An employee may save more then one link type like "Following", "Favorite".
     * :param: sourceEntityType: It is source type for which entity, we have created the UserEntityLink.
     * :param: sourceEntityId: It is source id for which entity, we have created the UserEntityLink.
     * :param: intVal: It is is the value for which entity we are saving.
     * :param: boolVal: It is the  boolean type value for which entity we are saving.
     * :param: stringVal: It is string type value for which entity we are saving.
     *
     * @param linkType         linkType
     * @param sourceEntityType sourceEntityType
     * @param sourceEntityId   sourceEntityId
     * @param intVal           intVal
     * @param boolVal          boolVal
     * @param stringValue      stringValue
     * @param applicationId    applicationId
     * @return Object response
     */
    public Object addUserEntityFromSourceIdSourceTypeAndLinkType(int linkType, int sourceEntityType, UUID sourceEntityId, int intVal, boolean boolVal, String stringValue, String applicationId) throws EwpException {
        PFUserEntityLink link = new PFUserEntityLink();
        link.setSourceEntityId(sourceEntityId);
        link.setSourceEntityType(sourceEntityType);
        link.setLinkType(linkType);
        link.setTenantId(EwpSession.getSharedInstance().getTenantId());
        link.setIntValue(intVal);
        link.setBoolValue(boolVal);
        link.setStringValue(stringValue);
        link.setApplicationId(applicationId);
        if (linkType == LinkType.FAVOURITE.getId()) {
            int result = dataDelegate.getMaxSortOrderFromSourceTypeAndLinkType(link.getTenantId(), linkType, sourceEntityType, EwpSession.getSharedInstance().getUserId());
            link.setSortOrder(result + 1);
        }
        Object response = dataDelegate.add(link);
        if (linkType == LinkType.FAVOURITE.getId()) {
            rearrangeSortOrder(linkType,sourceEntityType,null,EwpSession.getSharedInstance().getUserId());
        }

            return response;
    }

    /**
     * This method is used to check permission on given operation.
     *
     * @param entity    entity
     * @param operation operation
     * @return boolean permission
     */
    public boolean checkPermissionOnOperation(PFUserEntityLink entity, EntityAccess.CheckOperationPermission operation) {
     /*   PFUserEntityLinkAccess access = new PFUserEntityLinkAccess();
        if (operation == EntityAccess.CheckOperationPermission.ADD || operation == EntityAccess.CheckOperationPermission.UPDATE) {
            EmployeeDataService service = new EmployeeDataService();
            let resultTuple = service.getEmployeeFromUserId(EwpSession.getSharedInstance().getUserId());
        }
        return access.checkPermissionOnOperation(operation, EnumsForExceptions.ErrorModule.DATA_SERVICE);*/
        return true;
    }


    /**
     * Method is used to get UserEntityLink from source type, source id, linktype and tenantid.
     * :param: tenantId: Id of logged in tenant.
     * :param: linkType: Type of link. (How a userentity link from an entity) for example An employee may save more then one link type like "Following", "Favorite".
     * :param: sourceEntityType: It is source type for which entity, we have created the UserEntityLink.
     * :param: entityId: It is source id for which entity, we have created the UserEntityLink.
     * :param: createdById: It is id of logged in user who created the reference entity.
     *
     * @param tenantId         tenantId
     * @param linkType         linkType
     * @param sourceEntityType sourceEntityType
     * @param entityId         entityId
     * @param createdById      createdById
     * @return PFUserEntityLink
     * @throws EwpException
     */
    public PFUserEntityLink getUserEntityFromSourceIdSourceTypeAndLinkType(UUID tenantId, int linkType, int sourceEntityType, UUID entityId, UUID createdById) throws EwpException {
        PFUserEntityLink response = dataDelegate.getUserEntityFromSourceIdSourceTypeAndLinkType(tenantId, linkType, sourceEntityType, entityId, createdById);
        return response;
    }

    /**
     * Method is used to get employee is followed by Logged in user.
     * :param: sourceEntityId: It is source id for which entity, we have created the UserEntityLink.
     *
     * @param sourceEntityId
     * @return
     */
    public boolean isEmployeeFollowing(UUID sourceEntityId) throws EwpException {
        return dataDelegate.isEmployeeFollowing(sourceEntityId);
    }

    /**
     * Method is used to get employee is followed by Logged in user.
     * @param sourceEntityId It is source id for which entity, we have created the UserEntityLink.
     * @param linkType
     * @return
     * @throws EwpException
     */
    public boolean isUserEntityLinkExist(UUID sourceEntityId, int linkType) throws EwpException {
        return dataDelegate.isUserEntityLinkExist(sourceEntityId, linkType);
    }

    /**
     * Method is used to get UserEntityLink from source type, source id, linktype and tenantid.
     * @param tenantId Id of logged in tenant.
     * @param linkType Type of link. (How a userentity link from an entity) for example An employee may save more then one link type like "Following", "Favorite".
     * @param sourceEntityType It is source type for which entity, we have created the UserEntityLink.
     * @param createdById It is id of logged in user who created the reference entity.
     * @return
     * @throws EwpException
     */
    public List<PFUserEntityLink> getUserEntityListFromSourceTypeAndLinkType(UUID tenantId, int linkType, int sourceEntityType, UUID createdById) throws EwpException {
        List<PFUserEntityLink> response = dataDelegate.getUserEntityListFromSourceTypeAndLinkType(tenantId, linkType, sourceEntityType, createdById);
        return response;
    }


    /**
     * It is used to Delete UserEntityLink.
     * @param idList
     * @param sourceEntityType
     * @param linkType
     * @throws EwpException
     */
    public void deleteUserEntityLink(List<UUID> idList, int sourceEntityType, int linkType) throws EwpException {
        DatabaseOps.defaultDatabase().beginTransaction();

        int sortOrder = 0;
        for (int i = 0; i < idList.size(); i++) {
            BaseEntity response = getEntity(idList.get(i));
            if (response == null) {
                continue;
            }
            PFUserEntityLink pfUserEntityLink = (PFUserEntityLink) response;
            dataDelegate.delete(pfUserEntityLink);
            int order = pfUserEntityLink.getSortOrder();
            if (sortOrder == 0) {
                sortOrder = order;
            } else if (sortOrder > order) {
                sortOrder = order;
            }
        }
        if (linkType == LinkType.FAVOURITE.getId()) {
            List<PFUserEntityLink> pfUserEntityLinks = getUserEntityListFromSourceTypeAndLinkType(EwpSession.getSharedInstance().getTenantId(), linkType, sourceEntityType, EwpSession.getSharedInstance().getUserId());
            PFUserEntityLink userLink;
            for (int j = 0; j < pfUserEntityLinks.size(); j++) {
                userLink = pfUserEntityLinks.get(j);
                if (userLink.getSortOrder() >= sortOrder) {
                    userLink.setSortOrder(j + 1);
                    super.update(userLink);

                }
            }
        }
        DatabaseOps.defaultDatabase().commitTransaction();
    }

    public void rearrangeSortOrder(int linkType, int sourceEntityType, UUID sourceEntityId, UUID createdBy) throws EwpException {
        dataDelegate.rearrangeSortOrder(linkType,sourceEntityType,sourceEntityId,createdBy);
    }

    /**
     * Method is used to sort item from source type, linktype and sourceEntityId and created by.
     * @param linkType Type of link. (How a userentity link from a entity) for example employee may save more then one link type like "Following", "Favorite".
     * @param createdBy Id of user.s Who created the UserEntityLink.
     * @throws EwpException
     */
    public void rearrangeUsersSortOrder(int linkType,ArrayList<String> createdBy)throws EwpException {
         dataDelegate.rearrangeUsersSortOrder(linkType,createdBy);
    }

    public ArrayList<String> getFavoriteUserList(int linkType, UUID sourceEntityId) throws EwpException {
        return dataDelegate.getFavoriteUserList(linkType,sourceEntityId);
    }

    /// It is used to Delete UserEntityLink.
    public boolean deleteUserEntityFromSourceEntityId(UUID sourceEntityId) throws EwpException {
        return dataDelegate.deleteUserEntityFromSourceEntityId(sourceEntityId);
    }
}
