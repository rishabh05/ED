//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/22/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;


import android.database.Cursor;

import com.eworkplaceapps.platform.address.Address;
import com.eworkplaceapps.platform.address.AddressDataService;
import com.eworkplaceapps.platform.communication.Communication;
import com.eworkplaceapps.platform.communication.CommunicationDataService;
import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.utils.enums.DatabaseOperationType;
import com.eworkplaceapps.platform.utils.enums.EDEntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * It expand the BaseDataService to provide services to EmployeeContact entities.
 * It provides the detail EmployeeContact information.
 * It contains the detail information about an EmployeeContact with reference to an entity.
 */
public class EmployeeContactDataService extends BaseDataService {

    private EmployeeContactData dataDelegate = new EmployeeContactData();

    /// Initializes a new instance of the EmployeeContactDataService class.
    public EmployeeContactDataService() {
        super("EmployeeContactDataService");
    }

    @Override
    public BaseData getDataClass() {
        return dataDelegate;
    }

    /**
     * Method is used to get EmployeeContact list from EntityType and EntiyId.
     * :param: sourceEntityid, It source entityId, For which we saved the EmployeeContact.
     *
     * @param sourceEntityid
     * @return List<EmployeeContact>
     * @throws EwpException
     */
    public List<EmployeeContact> getEmployeeContactFromSourceEntityIdAndType(UUID sourceEntityid) throws EwpException {
        List<EmployeeContact> response = dataDelegate.getEmployeeContactListFromSourceEntityIdAndType(sourceEntityid);
        if (response == null) {
            throw new EwpException("Error at getEmployeeContactFromSourceEntityIdAndType method in EmployeeContactDataService");
        }
        return response;
    }

    /**
     * Method is used to get EmployeeContact list from EntityType and EntityId.
     * param: sourceEntityid, It source entityId, For which we saved the EmployeeContact.
     *
     * @param sourceEntityId
     * @return List<ViewEmployeeContact>
     */
    public List<ViewEmployeeContact> getViewEmployeeContactFromSourceEntityIdAndType(UUID sourceEntityId) throws EwpException {
        Cursor cursor = dataDelegate.getEmployeeContactListFromSourceEntityIdAndTypeAsResultSet(sourceEntityId);
        if (cursor == null) {
            new EwpException("Error at getEmployeeContactFromSourceEntityIdAndType method in EmployeeContactDataService");
        }
        List<ViewEmployeeContact> viewList = ViewEmployeeContact.setPropertiesAndGetViewEmployeeCommunicationList(cursor);
        return viewList;
    }

    /**
     * Method is used to get EmployeeContact detail list from EntityType and EntityId.
     * :param: sourceEntityid, It source entityId, For which we saved the EmployeeContact.
     *
     * @param sourceEntityId
     * @return List<EmployeeContact>
     */
    public List<ViewEmployeeContact> getViewEmployeeContactListFromSourceEntityIdAndType(UUID sourceEntityId) throws EwpException {
        List<EmployeeContact> response = getEmployeeContactFromSourceEntityIdAndType(sourceEntityId);
        //viewEmployeeContactList
        if (response != null) {
            List<ViewEmployeeContact> emergencyContactDetailList = new ArrayList<ViewEmployeeContact>();
            // Object can be nil
            CommunicationDataService commService = new CommunicationDataService();
            for (int i = 0; i < response.size(); i++) {
                ViewEmployeeContact employeeEmergencyContact = new ViewEmployeeContact();
                employeeEmergencyContact.setContactName(response.get(i).getName());
                //emargencyContactDetail.employeeContact.append(response.entityList![i])
                // Findind Emargancy contact address.
                //let  resultAddTuple = getViewAddressFromSourceEntityIdAndType(response.entityList![i].entityId, sourceEntityType: EDEntityType.EmployeeContact.rawValue)
                //employeeEmargencyContact.addressInfo = resultAddTuple.entity
                /// Emargancy communication contact list like Work phone, Home phone, mobile email etc.
                List<ViewEmployeeCommunication> resultCommTuple = getViewCommunicationListFromSourceEntityIdAndType(response.get(i).getEntityId(), EDEntityType.EMPLOYEE_CONTACT.getId());
                employeeEmergencyContact.setViewCommunicationList(resultCommTuple);
                // Add the contact detail
                emergencyContactDetailList.add(employeeEmergencyContact);
            }
            return emergencyContactDetailList;
        }
        return null;
    }

    /**
     * Method is used to get view communication from EntityType and EntiyId.
     * :param: sourceEntityid, It source entityId, For which we have saved the communication.
     * :param: sourceEntityType, It source entity type, For which we have saved the communication
     *
     * @param sourceEntityId
     * @param sourceEntityType
     * @return List<ViewEmployeeCommunication>
     * @throws EwpException
     */
    private List<ViewEmployeeCommunication> getViewCommunicationListFromSourceEntityIdAndType(UUID sourceEntityId, int sourceEntityType) throws EwpException {
        CommunicationDataService commService = new CommunicationDataService();
        Cursor response = commService.getCommunicationListFromSourceEntityIdAndTypeAsResultSet(sourceEntityId, sourceEntityType);
        if (response != null) {
            List<ViewEmployeeCommunication> communicationList = ViewEmployeeCommunication.setPropertiesAndGetViewEmployeeCommunicationList(response);
            return communicationList;
        }
        return null;
    }

    /**
     * Method is used to get address from EntityType and EntiyId
     * /// :param: sourceEntityid, It source entityId, For which we have saved the address
     * /// :param: sourceEntityType, It source entity type, For which we have saved the address
     *
     * @param sourceEntityId
     * @param sourceEntityType
     * @return ViewEmployeeAddress
     */
    public ViewEmployeeAddress getViewAddressFromSourceEntityIdAndType(UUID sourceEntityId, int sourceEntityType) throws EwpException {
        AddressDataService addService = new AddressDataService();
        Address response = addService.getAddressFromSourceEntityIdAndType(sourceEntityId, sourceEntityType);
        if (response != null) {
            throw new EwpException("Error at getAddressFromSourceEntityIdAndType method in EmployeeContactDataService");
        }
        ViewEmployeeAddress vAddress = ViewEmployeeAddress.setViewAddressPropertiesFromAddressEntity(response);
        return vAddress;
    }

    /**
     * Method is used to get EmployeeContact detail list from EntityType and EntiyId.
     * /// :param: sourceEntityid, It source entityId, For which we saved the EmployeeContact.
     *
     * @param sourceEntityId
     * @return List<EmployeeEmergencyContact>
     * @throws EwpException
     */
    public List<EmployeeEmergencyContact> getEmployeeContactDetailFromSourceEntityIdAndType(UUID sourceEntityId) throws EwpException {
        List<EmployeeContact> employeeContact = getEmployeeContactFromSourceEntityIdAndType(sourceEntityId);
        //viewEmployeeContactList
        if (employeeContact != null) {
            List<EmployeeEmergencyContact> emergencyContactDetailList = new ArrayList<EmployeeEmergencyContact>();
            // Object can be nil
            AddressDataService addService = new AddressDataService();
            CommunicationDataService commService = new CommunicationDataService();
            for (int i = 0; i < employeeContact.size(); i++) {
                EmployeeEmergencyContact employeeEmergencyContact = new EmployeeEmergencyContact();
                employeeEmergencyContact.setEmployeeContact(employeeContact.get(i));
                //emergencyContactDetail.employeeContact.append(response.entityList![i])
                /// Emergency communication contact list like Work phone, Home phone, mobile email etc.
                List<Communication> communicationList = commService.getCommunicationListFromSourceEntityIdAndType(employeeContact.get(i).getEntityId(), EDEntityType.EMPLOYEE_CONTACT.getId());
                if (communicationList == null) {
                    new EwpException("Error at getCommunicationListFromSourceEntityIdAndType method in EmployeeContactDataService");
                }
                employeeEmergencyContact.getContactList().addAll(communicationList);
                emergencyContactDetailList.add(employeeEmergencyContact);
            }
            // Add the contact detail
            return emergencyContactDetailList;
        }
        return null;
    }

    /**
     * Method is used to update EmployeeContact detail list.
     *
     * @param emergencyContactDetailList
     */
    public void updateEmployeeContactDetailFromSourceEntityIdAndType(List<EmployeeEmergencyContact> emergencyContactDetailList, UUID sourceId, int sourceType) throws EwpException {
        for (int i = 0; i < emergencyContactDetailList.size(); i++) {
            if (emergencyContactDetailList.get(i).getEmployeeContact() == null) {
                continue;
            }
            if (emergencyContactDetailList.get(i).getEmployeeContact().getLastOperationType() == DatabaseOperationType.ADD) {
                addEmployeeEmergencyContactDetail(emergencyContactDetailList.get(i), sourceId, sourceType);
            } else if (emergencyContactDetailList.get(i).getEmployeeContact().getLastOperationType() == DatabaseOperationType.UPDATE) {
                updateEmployeeEmergencyContactDetail(emergencyContactDetailList.get(i));
            } else if (emergencyContactDetailList.get(i).getEmployeeContact().getLastOperationType() == DatabaseOperationType.DELETE) {
                delete(emergencyContactDetailList.get(i).getEmployeeContact());
            }
        }
    }

    /**
     * It is used to update Emergency contact information.
     *
     * @param contactDetail
     */
    public void addEmployeeEmergencyContactDetail(EmployeeEmergencyContact contactDetail, UUID sourceId, int sourceType) throws EwpException {
        EmployeeContact contact = contactDetail.getEmployeeContact();
        contact.setSourceEntityId(sourceId);
        contact.setTenantId(EwpSession.getSharedInstance().getTenantId());

        add(contactDetail.getEmployeeContact());
        if (contactDetail.getEmployeeContactAddress() != null) {
            Address address = contactDetail.getEmployeeContactAddress();
            AddressDataService addressService = new AddressDataService();
            address.setSourceEntityType(EDEntityType.EMPLOYEE_CONTACT.getId());
            address.setSourceEntityId(contactDetail.getEmployeeContact().getEntityId());
            addressService.add(address);
        }
        CommunicationDataService commService = new CommunicationDataService();
        commService.updateCommunicationList(contactDetail.getContactList(), contactDetail.getEmployeeContact().getEntityId(), EDEntityType.EMPLOYEE_CONTACT.getId(),EDApplicationInfo.getAppId().toString());
    }

    /**
     * It is used to update Emergency contact information.
     *
     * @param contactDetail
     */
    public void updateEmployeeEmergencyContactDetail(EmployeeEmergencyContact contactDetail) throws EwpException {
        update(contactDetail.getEmployeeContact());
        AddressDataService addressService = new AddressDataService();
        if(contactDetail.getEmployeeContactAddress()!=null)
        addressService.update(contactDetail.getEmployeeContactAddress());
        CommunicationDataService commService = new CommunicationDataService();
        commService.updateCommunicationList(contactDetail.getContactList(), contactDetail.getEmployeeContact().getEntityId(), EDEntityType.EMPLOYEE_CONTACT.getId(),EDApplicationInfo.getAppId().toString());
    }

    /**
     * Delete an entity
     *
     * @param entity
     * @throws EwpException
     */
    @Override
    public void delete(BaseEntity entity) throws EwpException {
        /// Set operation mode
        entity.setLastOperationType(DatabaseOperationType.DELETE);
        EmployeeContact contact = (EmployeeContact) entity;
        getDataClass().delete(entity);
        AddressDataService addressService = new AddressDataService();
        addressService.deleteAddressFromSourceEntityIdAndType(contact.getEntityId(), EDEntityType.EMPLOYEE_CONTACT.getId());
        CommunicationDataService commService = new CommunicationDataService();
        commService.deleteCommunicationListFromSourceEntityIdAndType(contact.getEntityId(), EDEntityType.EMPLOYEE_CONTACT.getId());
    }
}
