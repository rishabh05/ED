//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/21/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.location;

import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.eworkplaceapps.employeedirectory.employee.EDApplicationInfo;
import com.eworkplaceapps.employeedirectory.employee.EmployeeDataService;
import com.eworkplaceapps.employeedirectory.employee.EmployeeQuickView;
import com.eworkplaceapps.employeedirectory.employee.GroupBy;
import com.eworkplaceapps.employeedirectory.employee.ViewEmployeeAddress;
import com.eworkplaceapps.platform.address.Address;
import com.eworkplaceapps.platform.address.AddressDataService;
import com.eworkplaceapps.platform.commons.Commons;
import com.eworkplaceapps.platform.communication.Communication;
import com.eworkplaceapps.platform.communication.CommunicationDataService;
import com.eworkplaceapps.platform.context.ContextHelper;
import com.eworkplaceapps.platform.db.DatabaseOps;
import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions.ErrorDataType;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.requesthandler.RequestCallback;
import com.eworkplaceapps.platform.security.EntityAccess;
import com.eworkplaceapps.platform.utils.AppMessage;
import com.eworkplaceapps.platform.utils.Utils;
import com.eworkplaceapps.platform.utils.enums.DatabaseOperationType;
import com.eworkplaceapps.platform.utils.enums.EDEntityType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * It expand the BaseDataService to provide services to Location entities.
 */
public class LocationDataService extends BaseDataService<Location> {

    private LocationData dataDelegate = new LocationData();
    private LocationModel locationModel = new LocationModel();

    private String getAlphaOne(){
        return "http://139.144.10.111/Services/Utils/TZandGeoPoints";
    }

    private String getPMUrl() {
        return "http://ewp-dev39.eworkplace.com/alpha1/Services/Utils/TZandGeoPoints";
    }

    private String getDevelopmentUrl() {
        return "http://172.16.1.105:84/Services/Utils/TZandGeoPoints";
    }

    private String getFortyUrl() {
        return "http://172.16.1.105/Services/Utils/TZandGeoPoints";
    }

    private String getFortyOneUrl() {
        return "http://ewp-dev41.eworkplace.com/Services/Utils/TZandGeoPoints";
    }

    private String getQAUrl(){
        return "http://ewp-dev55.eworkplace.com/Services/Utils/TZandGeoPoints";
    }

    private String getAlpha2PMUrl(){
        return "http://ewp-dev39.eworkplace.com/Alpha2/Services/Utils/TZandGeoPoints";
    }

    private String getAlpha2QAUrl(){
        return "http://ewp-dev55.eworkplace.com/Alpha2/Services/Utils/TZandGeoPoints";
    }

    public interface AddressCallback {
        public void onLocationArrival(LocationModel locationModel);
    }

    /**
     * Initializes a new instance of the TaskDataService class.
     */
    public LocationDataService() {
        super("LocationDataService");
    }

    @Override
    public BaseData<Location> getDataClass() {
        return dataDelegate;
    }

    /**
     * Get an Location entity for given entity id
     * :param: id: To get entity an from id.
     * :returns: Return LOCATION entity.
     *
     * @param id
     * @return Location
     */
    @Override
    public Location getEntity(Object id) throws EwpException {
        Location location = super.getEntity(id);
        return location;
    }

    /**
     * Add an entity
     *
     * @param entity
     * @return Object
     */
    @Override
    public Object add(Location entity) throws EwpException {
        dataDelegate.insertEntity(entity);
        return entity.getEntityId();
    }

    /**
     * Update an entity
     *
     * @param entity
     */
    @Override
    public void update(Location entity) throws EwpException {
        super.update(entity);
    }

    @Override
    public void validateOnAddAndUpdate(BaseEntity entity) throws EwpException {
        entity.validate();
        Location loc = (Location) entity;
        boolean result = dataDelegate.locationExists(loc.getEntityId(), loc.getName(), loc.getTenantId());
        if (result) {
            EwpException ewpException = new EwpException();
            ewpException.errorType = EnumsForExceptions.ErrorType.DUPLICATE;
            ewpException.setLocalizedMessage("A Location with that name already exists.");
            throw ewpException;
        }
    }

    /**
     * It is overrided by Location service class.
     * If Location entity will be delete then it reference entity(address) will also be delete.
     *
     * @param entity
     * @throws EwpException
     */
    @Override
    public void delete(Location entity) throws EwpException {
        if (entity != null) {
            DatabaseOps.defaultDatabase().beginDeferredTransaction();
            super.delete(entity);
            AddressDataService addressDataService = new AddressDataService();
            Address add=addressDataService.getAddressFromSourceEntityIdAndType(entity.getEntityId(), EDEntityType.LOCATION.getId());
            if(add!=null)
                addressDataService.delete(add);
            CommunicationDataService commService = new CommunicationDataService();
            commService.deleteCommunicationListFromSourceEntityIdAndType(entity.getEntityId(), EDEntityType.LOCATION.getId());
            EmployeeDataService empService = new EmployeeDataService();
            empService.updateEmployeeLocationEmpty(entity.getEntityId());
            DatabaseOps.defaultDatabase().commitTransaction();
        }
    }

    /**
     * Method is used to check permission on given operation.
     *
     * @param entity
     * @param operationPermission
     * @return boolean
     */
    public boolean checkPermissionOnOperation(Location entity, EntityAccess.CheckOperationPermission operationPermission) {
        LocationAccess access = new LocationAccess();
        return access.checkPermissionOnOperation(operationPermission, EnumsForExceptions.ErrorModule.DATA_SERVICE);
    }


    /**
     * Method is used to get LOCATION list from tenantId
     *
     * @param tenantId
     * @return List<Location>
     */
    public List<Location> getLocationListFromTenantId(UUID tenantId) throws EwpException {
        List<Location> response = dataDelegate.getLocationListFromTenantId(tenantId);
        if (response == null) {
            throw new EwpException("Error at getAddressFromSourceEntityIdAndType method in LocationDataService");
        }
        return response;
    }

    /// Method is used to get address from EntityType and EntiyId
    /// :param: sourceEntityid, It source entityId, For which we have saved the address
    public ViewEmployeeAddress getLocationViewAddressFromSourceEntityIdAndType(UUID sourceEntityId) throws EwpException {
        AddressDataService addressDataService = new AddressDataService();
        ViewEmployeeAddress response = getViewAddressFromSourceEntityIdAndType(sourceEntityId, EDEntityType.LOCATION.getId());
        return response;
    }

    /// Method is used to get address from EntityType and EntiyId
    /// :param: sourceEntityid, It source entityId, For which we have saved the address
    /// :param: sourceEntityType, It source entity type, For which we have saved the address
    private ViewEmployeeAddress getViewAddressFromSourceEntityIdAndType(UUID sourceEntityid, int sourceEntityType) throws EwpException {
        AddressDataService addService = new AddressDataService();
        Address response = addService.getAddressFromSourceEntityIdAndType(sourceEntityid, sourceEntityType);
        if (response != null) {
            ViewEmployeeAddress vAddress = ViewEmployeeAddress.setViewAddressPropertiesFromAddressEntity(response);
            return vAddress;
        }
        return null;
    }

    /**
     * @param locModel
     * @throws EwpException
     */
    public void updateLocation(LocationModel locModel) throws EwpException {
        DatabaseOps.defaultDatabase().beginDeferredTransaction();
        super.update(locModel.getLocation());
        // add/update address information.
        if (locModel.getAddress() != null) {
            AddressDataService addressService = new AddressDataService();
            locModel.getAddress().setSourceEntityId(locModel.getLocation().getEntityId());
            locModel.getAddress().setSourceEntityType(EDEntityType.LOCATION.getId());
            addressService.addUpdateAddress(locModel.getAddress(), locModel.getLocation().getEntityId(), EDEntityType.LOCATION.getId(), EwpSession.getSharedInstance().getUserId());
        }
        CommunicationDataService commService = new CommunicationDataService();
        commService.updateCommunicationList(locModel.getCommunications(), locModel.getLocation().getEntityId(), EDEntityType.LOCATION.getId(), EDApplicationInfo.getAppId().toString());
        EmployeeDataService empService = new EmployeeDataService();
        empService.setEmployeeLocation(locModel.getEmployeeList(), locModel.getLocation().getEntityId());
        DatabaseOps.defaultDatabase().commitTransaction();
    }

    /**
     * Method is used to add new location. It will take LocationModel as a parameter object.
     * It will add all location related information like with location it will add location addess,
     * location communication detail as well as set select employees location.
     * :param: locModel: It is a location model object, It is a set of Location, Address, communication and employees list.
     * :returns: It will returns the location id and
     *
     * @param locModel
     * @return Object
     * @throws EwpException
     */
    public Object addLocation(LocationModel locModel) throws EwpException {
        DatabaseOps.defaultDatabase().beginDeferredTransaction();
        Location loc = locModel.getLocation();
        Object id = super.add(loc);
        loc.setEntityId((UUID) id);
        if (locModel.getAddress() != null) {
            AddressDataService addressService = new AddressDataService();
            locModel.getAddress().setSourceEntityId(loc.getEntityId());
            locModel.getAddress().setSourceEntityType(EDEntityType.LOCATION.getId());
            locModel.getAddress().setTenantId(EwpSession.getSharedInstance().getTenantId());
            addressService.addUpdateAddress(locModel.getAddress(), loc.getEntityId(), EDEntityType.LOCATION.getId(), EwpSession.getSharedInstance().getUserId());
        }
        CommunicationDataService commService = new CommunicationDataService();
        commService.addCommunicationList(locModel.getCommunications(), locModel.getLocation().getEntityId(), EDEntityType.LOCATION.getId(), EwpSession.EMPLOYEE_APPLICATION_ID);
        EmployeeDataService empService = new EmployeeDataService();
        empService.setEmployeeLocation(locModel.getEmployeeList(), loc.getEntityId());
        DatabaseOps.defaultDatabase().commitTransaction();
        return id;
    }

    /**
     *
     * @param locModel
     * @throws EwpException
     */
    public void makeAddressCall(final LocationModel locModel, final RequestCallback requestCallback) throws EwpException, JSONException, UnsupportedEncodingException {
        validateOnAddAndUpdate(locModel.getLocation());
        if (locModel.getAddress() == null || "".equals(locModel.getAddress().getAddress1())) {
            EwpException e = new EwpException();
            e.errorType = EnumsForExceptions.ErrorType.VALIDATION_ERROR;
            e.dataList.put(ErrorDataType.REQUIRED, new String[]{"Address"});
            throw e;
        }
        this.locationModel = locModel;
        String url = getAlpha2PMUrl();

        AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                super.callback(url, object, status);
                String ianaTimeZone = "";
                double lat = 0d, lng = 0d;
                try {
                    if (status.getCode() == 200 && object != null) {
                        lat = Double.parseDouble(object.getString("Latitude"));
                        lng = Double.parseDouble(object.getString("Longitude"));
                        ianaTimeZone = object.getString("IANATimeZoneId");

                        requestCallback.onSuccess(Commons.GET_LATITUDE_LONGITUDE, "");
                    } else {
                        String error = "";
                        EwpException ex = EwpException.handleError(null, status);
                        if (ex != null && ex.message != null && ex.message.size() > 0)
                            error = ex.message.get(0);
                        requestCallback.onFailure(Commons.GET_LATITUDE_LONGITUDE, error);
                    }
                } catch (Exception e) {

                    requestCallback.onFailure(Commons.GET_LATITUDE_LONGITUDE, "" + e);
                    e.printStackTrace();
                }


            }
        };
        cb.method(AQuery.METHOD_PUT);
        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("Address", getAddress(locModel.getAddress()));
        cb.params(params);
        cb.header(Commons.LOGIN_TOKEN,EwpSession.getSharedInstance().getLoginToken());
        AQuery aQuery = new AQuery(ContextHelper.getContext());
        aQuery.ajax(url, params, JSONObject.class, cb);

    }

    /**
     * @param id
     * @return LocationModel
     * @throws EwpException
     */
    public LocationModel getLocationEntityAsModel(UUID id) throws EwpException {
        Location location = dataDelegate.getEntity(id);
        LocationModel locModel = new LocationModel();
        locModel.setLocation(location);
        AddressDataService addService = new AddressDataService();
        Address response = addService.getAddressFromSourceEntityIdAndType(id, EDEntityType.LOCATION.getId());
        locModel.setAddress(response);
        CommunicationDataService commService = new CommunicationDataService();
        // Getting the employee contact infromation and map contact information to ViewEmployee object.
        // Employee communication contact list like Work phone, Home phone, mobile email etc.
        List<Communication> communicationList = commService.getCommunicationListFromSourceEntityIdAndType(id, EDEntityType.LOCATION.getId());
        if (communicationList != null) {
            locModel.setCommunications(communicationList);
        }
        List<EmployeeQuickView> employeeQuickViews = EmployeeQuickView.getEmployeeQuickViewList(GroupBy.LOCATION, null, id);
        locModel.setEmployeeList(employeeQuickViews);
        return locModel;
    }
    /**
     *
     * @param address
     * @return
     */
//    private String getAddress(Address address) {
//        String add = address.getAddress1();
//
//        if (add == null || add.isEmpty()) {
//            add = address.getAddress2();
//        } else if (address.getAddress2() != null && !address.getAddress2().isEmpty()) {
//            add += " " + address.getAddress2();
//        }
//
//        // address 3
//        if (add == null || add.isEmpty()) {
//            add = address.getAddress3();
//        } else if (address.getAddress3() != null && !address.getAddress3().isEmpty()) {
//            add += " " + address.getAddress3();
//        }
//
//        // address 4
//        if (add == null || add.isEmpty()) {
//            add = address.getAddress4();
//        } else if (address.getAddress4() != null && !address.getAddress4().isEmpty()) {
//            add += " " + address.getAddress4();
//        }
//
//        return add;
//    }


    /**
     *
     * @param jsonDic
     * @param error
     * @throws EwpException
     * @throws JSONException
     */
    private void locationActionResponseHandler(JSONArray jsonDic, EwpException error) throws EwpException, JSONException {
        String reply;
        boolean isSuccess = true;
        JSONArray dict;
        UUID id;
        DatabaseOperationType opType = DatabaseOperationType.NONE;
        // If json is parse successfully then convert the json dictionary into object.
        if (error.errorType == EnumsForExceptions.ErrorType.SUCCESS) {
            dict = jsonDic;
            if (dict != null) {
                if (dict.length() > 0) {
//                    this.locationModel.getLocation().setLatitude(Double.parseDouble(dict.getJSONObject(0).getString("Latitude")));
//                    this.locationModel.getLocation().setLatitude(Double.parseDouble(dict.getJSONObject(0).getString("Longitude")));
//                    this.locationModel.getLocation().setIanaTimeZone(dict.getJSONObject(0).getString("IANATimeZoneId"));
                }
            }
            if (this.locationModel.getLocation().getEntityId().equals(Utils.emptyUUID())) {
                Object result = addLocation(this.locationModel);
                id = (UUID) result;
                opType = DatabaseOperationType.ADD;
            } else {
                updateLocation(this.locationModel);
                opType = DatabaseOperationType.UPDATE;
            }
        }
    }
    public boolean validateLocationModel(LocationModel locModel ) throws EwpException {
        //if there is an error then throw EwpException
        validateOnAddAndUpdate(locModel.getLocation());

        if (locModel.getAddress() == null || "".equals(locModel.getAddress().getAddress1())) {
            EwpException e = new EwpException();
            e.errorType = EnumsForExceptions.ErrorType.VALIDATION_ERROR;
            e.dataList.put(ErrorDataType.REQUIRED, new String[]{"Address"});
            throw e;
        }
        return true;
    }
}
