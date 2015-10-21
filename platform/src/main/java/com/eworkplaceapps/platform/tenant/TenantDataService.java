package com.eworkplaceapps.platform.tenant;

import android.database.Cursor;
import android.util.Log;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.exception.DataServiceErrorHandler;
import com.eworkplaceapps.platform.exception.EwpErrorHandler;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.utils.enums.TenantStatusItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * It expand the BaseDataService to provide services to tenant entities.
 * It is used to get the tenant by unique loginemail.
 * It allow to create TENANT_USER when a new tenant signup. It also override validate method to check duplicacy of tenant.
 *
 */
public class TenantDataService extends BaseDataService {

    private static final String TAG = "TenantDataService";
    TenantData dataDelegate = new TenantData();

    /**
     * Initializes a new instance of the TenantDataService class.
     */
    public TenantDataService() {
        super(TAG);
    }

    /**
     * return the data class, responsible to do any database operation on TENANT entity.
     *
     * @return
     */
    @Override
    public BaseData getDataClass() {
        return dataDelegate;
    }

    /**
     * This method will be used to getTenantList
     */
    public void getLogin() {
        // Send it to server
        //TODO left for implementation
        //sendRequestToRegisterDevice();
    }


    //TODO left for implementation

    /**
     * Signs up tenant creates its login id and send notification
     * Returns EwResponse.
     *
     * @param businessName
     * @param userFirstName
     * @param userLastName
     * @param userLoginEmail
     * @param userPWD
     */
    public void signUpTenant(String businessName, String userFirstName, String userLastName, String userLoginEmail, String userPWD) throws EwpException {
        UUID newTenantId = UUID.randomUUID();
        boolean userAlreadyExist = false;
        Tenant tenant = new Tenant();
        tenant.setName(businessName);
        tenant.setTenantStatus(TenantStatusItem.ACTIVE.getId());
      //  tenant.setAccountContactEmail(userLoginEmail);
      //  tenant.setFirstName(userFirstName);
      //  tenant.setLastName(userLastName);
      //  tenant.setAccountContactFullName(tenant.getFullName());
        validateOnAddAndUpdate(tenant);
        tenant.setEntityId(UUID.randomUUID());
        // ADD tenant details.
        Object response = dataDelegate.add(tenant);
        if (response != null) {
            newTenantId = ((UUID) response).randomUUID();
        } else {
            List<String> messages = new ArrayList<String>();
            messages.add("ERROR at signUpTenant method in TenantDataService");
            throw DataServiceErrorHandler.defaultDataServiceErrorHandler().handleEwpError(new EwpException("ERROR at signUpTenant method in TenantDataService"), EwpErrorHandler.ErrorPolicy.
                            WRAP, messages,
                    EnumsForExceptions.ErrorModule.
                            DATA_SERVICE);
        }
        if (newTenantId.toString() != String.valueOf(UUID.randomUUID())) {
            applySecurity = false;
            // Check that tenant user already exists or not.
            TenantUserDataService tenantUserDS = new TenantUserDataService();// DataFactory.getDataService(EntityType.TENANT_USER) as! TenantUserDataService
            Cursor cursor = tenantUserDS.getUsersByLoginEmailAndStatusAsResultSet(userLoginEmail,null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    // old password work for new user record.
                    userAlreadyExist = true;
                    UUID ewResponse = tenantUserDS.signUpTenantUser(newTenantId, "");
                    UUID newUserId = null;
                    if (ewResponse != null) {
                        newUserId = ewResponse;
                    }
                    Tenant response1 = dataDelegate.getEntity(newTenantId);
                    if (response1 == null) {
                        List<String> messages = new ArrayList<String>();
                        messages.add("ERROR at signUpTenant method in TenantDataService");
                        throw DataServiceErrorHandler.defaultDataServiceErrorHandler().handleEwpError(new EwpException("ERROR at signUpTenant method in TenantDataService"), EwpErrorHandler.ErrorPolicy.
                                        WRAP, messages,
                                EnumsForExceptions.ErrorModule.
                                        DATA_SERVICE);
                    }
                    UUID tenant1 = response1.getEntityId();
                    // Now update createdBy and updatedBy details.
                    tenant.setCreatedBy(newUserId.toString());
                    tenant.setCreatedAt(new Date());
                    tenant.setUpdatedBy(newUserId.toString());
                    tenant.setUpdatedAt(new Date());
                    applySecurity = false;
                    update(tenant);
                }
            } else {
                userAlreadyExist = false;
            }
        }
    }

    //TODO left for implementation
    public void sendRequestToRegisterDevice() throws EwpException {
        // generate HTTP request and session
        //RestService restService = new RestService();
        //STRING urlString = restService.serverUrlString() + "RegisterDevice";
        //SyncService service = new SyncService();
        //STRING myDeviceId = service.dataProvider.getMyDeviceId();
        // restService.createRequestForLoginAndTenant(urlString, action: "RegisterDevice", requesterDeviceId: myDeviceId, postOrGet: false)
        //restService.session = restService.createSession();
        //STRING data = "";
        // Logger.defaultLogger.indenter.indent()
        Log.d(TAG, "-------------------- Sending data to sync --------------------");
        // Logger.defaultLogger.indenter.undent()
        // Execute HTTP request
        // Use our custom handler for this purpose
        //TODO pass Ajax Callback here
        //restService.executeRequest(data, processRegisterDeviceResponse);
        // Wait
    }

    //TODO left for implementation
  /*  *//**
     * CUSTOM handler to store data temporarily in serverResponseData
     *//*
    public void processRegisterDeviceResponse(NSData data, NSURLResponse response, NSError error) {
        // Call the default handler for normal response processing.
        RESTService restService = new RESTService();
        restService.processResponse(data, response, error);
        STRING p = restService.responseDataAsString;
    }*/
}
