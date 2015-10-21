//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/18/2015
//===============================================================================
package com.eworkplaceapps.platform.communication;

import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.userentitylink.PFUserEntityLinkDataService;
import com.eworkplaceapps.platform.utils.enums.CommunicationType;
import com.eworkplaceapps.platform.utils.enums.DatabaseOperationType;
import com.eworkplaceapps.platform.utils.enums.LinkType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * It expand the BaseDataService to provide services to Communication entities.
 * It provides the detail communication information.
 * It contains the detail information about a communication with reference to an entity.
 */
public class CommunicationDataService extends BaseDataService<Communication> {

    CommunicationData dataDelegate = new CommunicationData();

    /**
     * Initializing the data service entity-type
     */
    public CommunicationDataService() {
        super("CommunicationDataService");
    }

    @Override
    public BaseData<Communication> getDataClass() {
        return dataDelegate;
    }

    /**
     * Method is used to get communication from EntityType and EntiyId.
     * :param: sourceEntityId, It source entityId, For which we have saved the communication.
     * :param: sourceEntityType, It source entity type, For which we have saved the communication
     *
     * @param sourceEntityId
     * @param sourceEntityType
     * @return
     */
    public List<Communication> getCommunicationListFromSourceEntityIdAndType(UUID sourceEntityId, int sourceEntityType) throws EwpException {
        List<Communication> response = dataDelegate.getCommunicationListFromSourceEntityIdAndType(sourceEntityId, sourceEntityType);
        return response;
    }

    /**
     * Method is used to get view communication from EntityType and EntiyId.
     * :param: sourceEntityId, It source entityId, For which we have saved the communication.
     * :param: sourceEntityType, It source entity type, For which we have saved the communication
     *
     * @param sourceEntityId
     * @param sourceEntityType
     * @return
     * @throws EwpException
     */
    public Cursor getCommunicationListFromSourceEntityIdAndTypeAsResultSet(UUID sourceEntityId, int sourceEntityType) throws EwpException {
        Cursor response = dataDelegate.getCommunicationListFromSourceEntityIdAndTypeAsResultSet(sourceEntityId, sourceEntityType);
        if (response == null) {
            throw new EwpException("");
        }
        return response;
    }

    /**
     * Method is used to Add/update/delete communication list.
     *
     * @param communicationList
     * @param sourceId
     * @param sourceEntityType
     */
    public void updateCommunicationList(List<Communication> communicationList, UUID sourceId, int sourceEntityType,String applicationId) throws EwpException {
        PFUserEntityLinkDataService pfUserEntityLinkDataService = new PFUserEntityLinkDataService();
        ArrayList<String> list = new ArrayList<>();
        boolean rearrangeOrder = false;
        for (int i = 0; i < communicationList.size(); i++) {
            communicationList.get(i).setTenantId(EwpSession.getSharedInstance().getTenantId());
            if (communicationList.get(i).getLastOperationType() == DatabaseOperationType.ADD) {
                communicationList.get(i).setSourceEntityId(sourceId);
                communicationList.get(i).setSourceEntityType(sourceEntityType);
                communicationList.get(i).setApplicationId(applicationId);
                communicationList.get(i).setTenantId(EwpSession.getSharedInstance().getTenantId());
                dataDelegate.add(communicationList.get(i));
            } else if (communicationList.get(i).getLastOperationType() == DatabaseOperationType.UPDATE) {
                super.update(communicationList.get(i));
            } else if (communicationList.get(i).getLastOperationType() == DatabaseOperationType.DELETE) {
                dataDelegate.delete(communicationList.get(i));
                if(communicationList.get(i).getCommunicationType() == CommunicationType.PHONE){
                    rearrangeOrder =true;
                    ArrayList<String> userList = pfUserEntityLinkDataService.getFavoriteUserList(LinkType.FAVOURITE.getId(),communicationList.get(i).getEntityId());
                    list .addAll(userList);
                    pfUserEntityLinkDataService.deleteUserEntityFromSourceEntityId(communicationList.get(i).getEntityId());
                }

            }
        }
        if (rearrangeOrder && list.size() > 0) {
            // service.rearrangeSortOrder(LinkType.Favorite.rawValue, sourceEntityType: comm!.sourceEntityType , sourceEntityId: nil, createdBy: nil)
            pfUserEntityLinkDataService.rearrangeUsersSortOrder(LinkType.FAVOURITE.getId(),list);
        }
    }

    /**
     * Method is used to Add communication list.
     *
     * @param communicationList
     * @param sourceId
     * @param sourceEntityType
     * @param applicationId
     * @throws EwpException
     */
    public void addCommunicationList(List<Communication> communicationList, UUID sourceId, int sourceEntityType, String applicationId) throws EwpException {
        if (communicationList != null) {
            for (int i = 0; i < communicationList.size(); i++) {
                communicationList.get(i).setSourceEntityId(sourceId);
                communicationList.get(i).setSourceEntityType(sourceEntityType);
                communicationList.get(i).setApplicationId(applicationId);
                communicationList.get(i).setTenantId(EwpSession.getSharedInstance().getTenantId());
                dataDelegate.add(communicationList.get(i));
            }
        }
    }

    /**
     * Method is used to Add/update/delete communication list.
     *
     * @param sourceId
     * @param sourceEntityType
     */
    public boolean deleteCommunicationListFromSourceEntityIdAndType(UUID sourceId, int sourceEntityType) throws EwpException {
        return dataDelegate.deleteCommunicationListFromSourceEntityIdAndType(sourceId, sourceEntityType);
    }

    /**
     * Method is used to find communication exist with type and subtype.
     * :param: sourceEntityid, It is id of source entity for which we have created the row.
     * :param: type, It is a type of communication, It may be of type Phone, Email, Social.
     * :param: subType, It is a subtype of communication.
     * for example for Phone type , Subtype will be Home, Work, Mobile, Other.
     * Similarly for Email type , Subtype will be Work, Personal etc.
     *
     * @param sourceEntityid
     * @param type
     * @param subType
     * @return boolean
     * @throws EwpException
     */
    public boolean hasCommunicationInTypeAndSubtype(UUID sourceEntityid, int type, int subType) throws EwpException {
        return dataDelegate.hasCommunicationInTypeAndSubtype(sourceEntityid, type, subType);
    }

    /**
     * Method is used to find communication exist with type and subtype.
     *
     * @param sourceEntityId It is id of source entity for which we have created the row.
     * @param type           type, It is a type of communication, It may be of type Phone, Email, Social.
     * @param subType        subType, It is a subtype of communication.
     * @return List<Communication>
     * @throws EwpException
     */
    public List<Communication> getCommunicationListFromTypeAndSubtype(UUID sourceEntityId, int type, int subType) throws EwpException {
        List<Communication> communicationList = dataDelegate.getCommunicationListFromTypeAndSubtype(sourceEntityId, type, subType);
        return communicationList;
    }
}
