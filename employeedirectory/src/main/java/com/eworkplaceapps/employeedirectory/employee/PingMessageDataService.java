//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/22/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;


import android.util.Log;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.DataServiceErrorHandler;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.notification.NotificationEnum;
import com.eworkplaceapps.platform.notificationworkflow.NotificationRecipientDetail;
import com.eworkplaceapps.platform.notificationworkflow.NotificationWorkflow;
import com.eworkplaceapps.platform.notificationworkflow.RawNotification;
import com.eworkplaceapps.platform.tenant.Tenant;
import com.eworkplaceapps.platform.tenant.TenantDataService;
import com.eworkplaceapps.platform.userpreference.UserPreference;
import com.eworkplaceapps.platform.userpreference.UserPreferenceDataService;
import com.eworkplaceapps.platform.utils.enums.EDEntityType;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * PingMessage Service class provide facility tp send messages to the user.
 * User can send Email and SMS message to their specific id as! per the their preferences.
 * User can set their message setting in preferences.
 * As per message preference setting user can get email/sms.
 */
public class PingMessageDataService extends BaseDataService<PingMessage> {

    private PingMessageData dataDelegate = new PingMessageData();

    /**
     * Initializes a new instance of the PingMessageDataService class.
     */
    PingMessageDataService() {
        super("PingMessageDataService");
    }

    @Override
    public BaseData getDataClass() {
        return dataDelegate;
    }

    /**
     * It validate an entity and returns result as! EwpError object variable.
     *
     * @param entity
     */
    public void validateOnAddAndUpdate(PingMessage entity) throws EwpException {
        boolean isValidated = entity.validate();
        if (!isValidated) {
            DataServiceErrorHandler.defaultDataServiceErrorHandler().logError(new EwpException("validation error"));
        }
    }

    /**
     * It is used to get currently chat(ping) message history.
     *
     * @return List<BaseEntity>
     */
    public List<PingMessage> getPingMessageListFromSenderAndReceiverId(UUID messageSenderId, UUID messageReceiverId) throws EwpException {
        List<PingMessage> pingMessageList = dataDelegate.getPingMessageListFromSenderAndReceiverId(messageSenderId, messageReceiverId);
        if (pingMessageList == null) {
            throw new EwpException("Exception PingMessageList is null");
        }
        return pingMessageList;
    }

    /**
     * It s used the send the ping message to another employee.
     *
     * @param senderEmployeeId    senderEmployeeId: Message sender employeeid.
     * @param recipientEmployeeId recipientEmployeeId: Message receiver employeeid.
     * @param message             message: Text message to send.
     * @return boolean
     */
    public boolean sendPing(UUID senderEmployeeId, UUID recipientEmployeeId, String message) throws EwpException {
        EmployeeDataService employeeDS = new EmployeeDataService();
        UserPreferenceDataService userPreferenceDS = new UserPreferenceDataService();
        BaseEntity resultTupleSender = employeeDS.getEntity(senderEmployeeId);

        if (resultTupleSender == null) {
            /// If any error occured then, log error and return error.
            Log.d(this.getClass().getName(), "" + resultTupleSender);
            return false;
        }
        Employee senderEmployee = (Employee) resultTupleSender;
        Employee recipientEmployee = (Employee) employeeDS.getEntity(recipientEmployeeId);

        if (recipientEmployee == null) {
            /// If any error occured then, log error and return error.
            Log.d(this.getClass().getName(), "" + recipientEmployee);
            return false;
        }
        Tenant tenant = (Tenant) new TenantDataService().getEntity(senderEmployee.getTenantId());

        if (tenant == null) {
            /// If any error occured then, log error and return error.
            Log.d(this.getClass().getName(), "" + tenant);
            return false;
        }
        /// Check ping preferences if its set then proceed.
        UserPreference resultEmailPreference = userPreferenceDS.getUserPreference(recipientEmployee.getTenantUserId(), EDApplicationInfo.getAppId().toString(), EDUserPreferenceKeyConstants.NOTIFICATION_PREFERENCE_KEY, EDUserPreferenceKeyConstants.PING_NOTIFICATION_EMAIL_DELIVERY_TYPE_KEY);

        if (resultEmailPreference == null) {
            /// If any error occured then, log error and return error.
            Log.d(this.getClass().getName(), "" + resultEmailPreference);
            return false;
        }
        UserPreference emailPreference = resultEmailPreference;
        boolean isNotEmpty = !"".equals(recipientEmployee.getLoginEmail());
        if (!"1".equals(emailPreference.getDataValue()) && isNotEmpty) {
            SendEmailPing(senderEmployee, recipientEmployee, message, tenant);
        }
        UserPreference resultSMSPreference = userPreferenceDS.getUserPreference(recipientEmployee.getTenantUserId(), EDApplicationInfo.getAppId().toString(), EDUserPreferenceKeyConstants.NOTIFICATION_PREFERENCE_KEY,
                EDUserPreferenceKeyConstants.PING_NOTIFICATION_SMS_DELIVERY_TYPE_KEY);

        //if (resultSMSPreference.getEntityId()!=null) {
        // let smsPreference = (UserPreference)resultSMSPreference;
//            if smsPreference.dataValue == "1" && !String.stringIsNilOrEmpty(recipientEmployee.smsEmail) {
//              sendSMSPing(senderEmployee, recipientEmployee: recipientEmployee, message: message);
        //}
        //  }

        return true;
    }

    /**
     * Send Ping SMS.
     * :param: senderEmployee: Message sender employee entity.
     * :param: recipientEmployee: Message receiver employee entity.
     * :param: message: Text message to send.
     *
     * @param senderEmployee
     * @param recipientEmployee
     * @param message
     */
    private void sendSMSPing(Employee senderEmployee, Employee recipientEmployee, String message) throws EwpException {
        PingMessage pingSMSMessage = new PingMessage();
        pingSMSMessage.setFromId(senderEmployee.getEntityId());
        pingSMSMessage.setToId(recipientEmployee.getEntityId());
        pingSMSMessage.setSentTime(new Date());
        pingSMSMessage.setMessage(message);
        pingSMSMessage.setTenantId(senderEmployee.getTenantId());
        pingSMSMessage.setCreatedBy(pingSMSMessage.getFromId().toString());
        pingSMSMessage.setCreatedAt(new Date());
        pingSMSMessage.setUpdatedBy(pingSMSMessage.getFromId().toString());
        pingSMSMessage.setUpdatedAt(new Date());
        Object id = add(pingSMSMessage);

        UUID pingId = (UUID) id;
        RawNotification rawNotification = new RawNotification();
        rawNotification.setApplicationId(EDApplicationInfo.getAppId().toString());
        rawNotification.setLoginTenantId(senderEmployee.getTenantId());
        rawNotification.setLoginUserId(senderEmployee.getTenantUserId());
        rawNotification.setLoginUserName(senderEmployee.getFullName());
        rawNotification.setNotificationType(NotificationEnum.NotificationTypeEnum.AD_HOC);
        rawNotification.setNotificationEntityType(EDEntityType.PING_MESSAGE.getId());
        rawNotification.setNotificationEntityId(pingId);
        Map<String, String> otherInformation = new HashMap<String, String>();
        otherInformation.put("SenderFullName", senderEmployee.getFullName());
        otherInformation.put("PingMessage", message);
        otherInformation.put("RecipientSMSEmail", "");//recipientEmployee.smsEmail!
        rawNotification.setOtherData(otherInformation);
        NotificationRecipientDetail recipient = new NotificationRecipientDetail();
        recipient.setDeliveryTime(new Date());
        recipient.setNotificationType(NotificationEnum.NotificationTypeEnum.AD_HOC.getId());
        recipient.setRecipientDeliveryType(NotificationEnum.NotificationDeliveryType.SMS);
        recipient.setRecipientEmail(""); //recipientEmployee.smsEmail != nil ? "" : recipientEmployee.smsEmail!
        recipient.setRecipientId(recipientEmployee.getEntityId());
        recipient.setRecipientType(NotificationEnum.NotificationRecipientType.INTERNAL_RECIPIENT);
        rawNotification.setNotificationRecipientDetailList(new ArrayList<NotificationRecipientDetail>());
        rawNotification.getNotificationRecipientDetailList().add(recipient);
        /// Call Notification Workflow for sending sms.
        new NotificationWorkflow().start(rawNotification);
    }

    /**
     * Send message as Email.
     * :param: senderEmployee: Message sender employee entity.
     * :param: recipientEmployee: Message receiver employee entity.
     * :param: message: Text message to send.
     *
     * @param senderEmployee
     * @param recipientEmployee
     * @param message
     * @param tenant
     */
    private void SendEmailPing(Employee senderEmployee, Employee recipientEmployee, String message, Tenant tenant) throws EwpException {
        PingMessage pingEmailMessage = new PingMessage();
        pingEmailMessage.setFromId(senderEmployee.getEntityId());
        pingEmailMessage.setToId(recipientEmployee.getEntityId());
        pingEmailMessage.setSentTime(new Date());
        pingEmailMessage.setMessage(message);
        pingEmailMessage.setTenantId(senderEmployee.getTenantId());
        pingEmailMessage.setCreatedBy(pingEmailMessage.getFromId().toString());
        pingEmailMessage.setCreatedAt(new Date());
        pingEmailMessage.setUpdatedBy(pingEmailMessage.getFromId().toString());
        pingEmailMessage.setUpdatedAt(new Date());
        Object id = add(pingEmailMessage);
        UUID pingId = (UUID) id;
        RawNotification rawNotification = new RawNotification();
        rawNotification.setApplicationId(EDApplicationInfo.getAppId().toString());
        rawNotification.setLoginTenantId(senderEmployee.getTenantId());
        rawNotification.setLoginUserId(senderEmployee.getTenantUserId());
        rawNotification.setLoginUserName(senderEmployee.getFullName());
        rawNotification.setNotificationType(NotificationEnum.NotificationTypeEnum.AD_HOC);
        rawNotification.setNotificationEntityType(EDEntityType.PING_MESSAGE.getId());
        rawNotification.setNotificationEntityId(pingId);
        Map<String, String> otherInformation = new HashMap<String, String>();
        otherInformation.put("RecipientFullName", recipientEmployee.getFullName());
        otherInformation.put("SenderFullName", senderEmployee.getFullName());
        otherInformation.put("RecipientEmail", "");//recipientEmployee.email;
        otherInformation.put("RecipientInitials", recipientEmployee.getFullName());
        otherInformation.put("RecipientSMSEmail", "");// recipientEmployee.smsEmail == nil ? "" : recipientEmployee.smsEmail!
        ///ToDo: Add Mobile No
        ///otherInformation.Add("RecipientMobile", recipientEmployee);

///        let empPicture = recipientEmployee.getPictureAsBase64String()
///        if String.stringIsNilOrEmpty(empPicture) {
///            otherInformation["RecipientImage"] = Utils.getDefaultEmployeePictureAsBase64String()
///        }
///        else {
///            otherInformation["RecipientImage"] = empPicture
///        }
        otherInformation.put("RecipientId", recipientEmployee.getEntityId().toString());
        otherInformation.put("TenantName", tenant.getName());
///        let logo = tenant.getLogoAsBase64String()
///        if String.stringIsNilOrEmpty(logo) {
///            otherInformation["TenantImage"]  = Utils.getDefaultTenantLogoAsBase64String()
///        }
///        else {
///            otherInformation["TenantImage"]  = logo
///        }
        otherInformation.put("TenantId", tenant.getEntityId().toString());
        otherInformation.put("PingOperation", "Ping Received"); /// EDResource.ED_Ping_PingReceived
        otherInformation.put("PingMessage", message);
        ///otherInformation["ApplicationImage"] = Utils.getDefaultTenantLogoAsBase64String()
        otherInformation.put("ApplicationId", EDApplicationInfo.getAppId().toString());
        rawNotification.setOtherData(otherInformation);
        NotificationRecipientDetail recipient = new NotificationRecipientDetail();
        recipient.setDeliveryTime(new Date());
        recipient.setNotificationType(NotificationEnum.NotificationTypeEnum.AD_HOC.getId());
        recipient.setRecipientDeliveryType(NotificationEnum.NotificationDeliveryType.EMAIL);
        recipient.setRecipientEmail(recipientEmployee.getLoginEmail());
        recipient.setRecipientId(recipientEmployee.getEntityId());
        recipient.setRecipientType(NotificationEnum.NotificationRecipientType.INTERNAL_RECIPIENT);
        rawNotification.setNotificationRecipientDetailList(new ArrayList<NotificationRecipientDetail>());
        rawNotification.getNotificationRecipientDetailList().add(recipient);
        /// Call Notification Workflow for email.
        new NotificationWorkflow().start(rawNotification);
    }

    public static void testPing() throws EwpException {
        PingMessageDataService s = new PingMessageDataService();
        s.sendPing(UUID.fromString("52B8DB1A-5095-4788-990C-B447D78947AA"), UUID.fromString("81CBD4C5-7F73-47B8-8AD5-EF32CFEDFADE"),
                "Hi, This is Anotherby ME(Amit) Ping.");
    }

}