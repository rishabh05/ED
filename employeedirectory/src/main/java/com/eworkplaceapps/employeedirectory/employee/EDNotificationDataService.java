//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/22/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import android.database.Cursor;

import com.eworkplaceapps.platform.cyclicnotification.CyclicNotification;
import com.eworkplaceapps.platform.cyclicnotification.CyclicNotificationCallbackResult;
import com.eworkplaceapps.platform.cyclicnotification.CyclicNotificationDataService;
import com.eworkplaceapps.platform.cyclicnotification.CyclicNotificationDetails;
import com.eworkplaceapps.platform.cyclicnotification.CyclicNotificationInfo;
import com.eworkplaceapps.platform.cyclicnotification.CyclicNotificationLinking;
import com.eworkplaceapps.platform.cyclicnotification.NotificationAttachment;
import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.DataServiceErrorHandler;
import com.eworkplaceapps.platform.exception.EwpErrorHandler;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.notification.EventNotification;
import com.eworkplaceapps.platform.notification.EventNotificationDataService;
import com.eworkplaceapps.platform.notification.NotificationEnum;
import com.eworkplaceapps.platform.notification.NotificationInfo;
import com.eworkplaceapps.platform.notification.NotificationMessageInfo;
import com.eworkplaceapps.platform.notification.NotificationQueue;
import com.eworkplaceapps.platform.notification.NotificationRecipient;
import com.eworkplaceapps.platform.notification.NotificationRecipientDataService;
import com.eworkplaceapps.platform.notification.UnPreparedNotificationQueue;
import com.eworkplaceapps.platform.notificationworkflow.NotificationRecipientDetail;
import com.eworkplaceapps.platform.tenant.Tenant;
import com.eworkplaceapps.platform.tenant.TenantDataService;
import com.eworkplaceapps.platform.utils.Tuple;
import com.eworkplaceapps.platform.utils.Utils;
import com.eworkplaceapps.platform.utils.enums.DatabaseOperationType;
import com.eworkplaceapps.platform.utils.enums.EDEntityType;
import com.eworkplaceapps.platform.utils.enums.PFEntityType;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.eworkplaceapps.employeedirectory.employee.EDApplicationInfo.EDEmployeeEventNotification.EMPLOYEE_OUT_OF_OFFICE;

public class EDNotificationDataService {

    private static EDNotificationDataService edNotificationDataService;
    private static EventNotificationDataService eventNotificationDS;

    /// Instance of platform data service interface.
    static {
        edNotificationDataService = new EDNotificationDataService();
        eventNotificationDS = new EventNotificationDataService();
    }

    /**
     * @param subscribe
     * @return UUID
     * @throws EwpException
     */
    public UUID updateOutOfOfficeStatusNotification(boolean subscribe) throws EwpException {
        UUID eventNotificationId = Utils.emptyUUID();
        EmployeeDataService employeeDS = new EmployeeDataService();
        Employee loginEmployee = employeeDS.getEmployeeFromUserId(EwpSession.getSharedInstance().getUserId());
        List<EventNotification> eventNotifications = edNotificationDataService.eventNotificationDS.getEntityListByParentEntityTypeAndIdAndEventTypeNo(EDEntityType.EMPLOYEE.getId(), loginEmployee.getEntityId(), EMPLOYEE_OUT_OF_OFFICE.getId());
        /// If any error occurred then return from here.
        if (eventNotifications == null) {
            List<String> messages = new ArrayList<>();
            throw DataServiceErrorHandler.defaultDataServiceErrorHandler().handleEwpError(new EwpException("null eventNotifications list"), EwpErrorHandler.ErrorPolicy.WRAP, messages, EnumsForExceptions.ErrorModule.DATA_SERVICE);
        }
        EventNotification outOfOfficeNotification = null;
        if (eventNotifications.size() != 0) {
            outOfOfficeNotification = eventNotifications.get(0);
        }
        if (subscribe) {
            if (outOfOfficeNotification == null) {
                outOfOfficeNotification = new EventNotification();
                outOfOfficeNotification.setLastOperationType(DatabaseOperationType.ADD);
                outOfOfficeNotification.setApplicationId(EDApplicationInfo.getAppId().toString());
                outOfOfficeNotification.setEntityType(EDEntityType.EMPLOYEE.getId());
                outOfOfficeNotification.setParentEntityType(EDEntityType.EMPLOYEE.getId());
                outOfOfficeNotification.setParentEntityId(loginEmployee.getEntityId());
                outOfOfficeNotification.setEventTypeNo(EMPLOYEE_OUT_OF_OFFICE.getId());
                outOfOfficeNotification.setOwnerId(loginEmployee.getEntityId());
                /// Add login user as a recipient.
                NotificationRecipient recipient = new NotificationRecipient();
                recipient.setLastOperationType(DatabaseOperationType.ADD);
                recipient.setRecipientType(NotificationEnum.NotificationRecipientType.INTERNAL_RECIPIENT.getId());
                recipient.setRecipientId(loginEmployee.getEntityId());
                recipient.setSourceEntityType(PFEntityType.EVENT_NOTIFICATION.getValue());
                List<NotificationRecipient> recipientList = new ArrayList<NotificationRecipient>();
                recipientList.add(recipient);
                UUID resultTuple = edNotificationDataService.eventNotificationDS.addEventNotificationAndRecipientList(outOfOfficeNotification, recipientList);
                /// If any error occured then return from here.
                if (resultTuple == null) {
                    List<String> messages = new ArrayList<String>();
                    throw DataServiceErrorHandler.defaultDataServiceErrorHandler().handleEwpError(new EwpException(""), EwpErrorHandler.ErrorPolicy.WRAP, messages, EnumsForExceptions.ErrorModule.DATA_SERVICE, "updateOutOfOfficeStatusNotification", "EDNotificationDataService", 82, false);
                }
                return (UUID) resultTuple;
            }
        } else if (outOfOfficeNotification != null) {
            edNotificationDataService.eventNotificationDS.delete(outOfOfficeNotification);
            return eventNotificationId;
        }
        return eventNotificationId;
    }

    /**
     * It is used to resolve event base notification list from list of notification.
     *
     * @param resolvedEventNotifications
     * @param tenantId
     * @param notifierEntityType
     * @param notifierEntityId
     * @return List<NotificationRecipientDetail>
     * @throws EwpException
     */
    public List<NotificationRecipientDetail> getResolvedEventNotificationRecipientDetailList(List<EventNotification> resolvedEventNotifications, UUID tenantId, EDEntityType notifierEntityType, UUID notifierEntityId) throws EwpException {
        List<NotificationRecipientDetail> resolvedRecipientDetailList = new ArrayList<>();
        NotificationRecipientDataService recipientDS = new NotificationRecipientDataService();
        for (EventNotification eventNotification : resolvedEventNotifications) {
            List<NotificationRecipientDetail> detailList = getResolvedEventNotificationRecipientDetail(eventNotification, tenantId, notifierEntityType, notifierEntityId);
            resolvedRecipientDetailList.addAll(detailList);
        }
        return resolvedRecipientDetailList;
    }

    /**
     * It is used to resolve event
     * base notification
     * list from
     * list of
     * notification.
     * For example if user changes his status then we will send the notfication to those users, are linked or connected to that user.
     * In case of change the status we will pass EDEntityType as! Employee and notifierEntityId will be employeeid, then with respact to that employee
     * we will get the list of notfier user list and will send notification.
     *
     * @param eventNotification
     * @param tenantId
     * @param notifierEntityType
     * @param notifierEntityId
     * @return List<NotificationRecipientDetail>
     * @throws EwpException
     */
    public List<NotificationRecipientDetail> getResolvedEventNotificationRecipientDetail(EventNotification eventNotification, UUID tenantId, EDEntityType notifierEntityType, UUID notifierEntityId) throws EwpException {
        List<NotificationRecipientDetail> resolvedRecipientDetailList = new ArrayList<NotificationRecipientDetail>();
        NotificationRecipientDataService recipientDS = new NotificationRecipientDataService();
        /// Get mapped recipient detail list.
        List<NotificationRecipientDetail> recipientDetailList = recipientDS.getNotificationRecipientDetailListByEntityTypeAndId(PFEntityType.EVENT_NOTIFICATION.getValue(), eventNotification.getEntityId());
        if (recipientDetailList.isEmpty()) {
            return resolvedRecipientDetailList;
        }
        for (NotificationRecipientDetail recipientDetail : recipientDetailList) {
            if (recipientDetail.getRecipientType() == NotificationEnum.NotificationRecipientType.PSEUDO_RECIPIENT) {
                /// Resolve Pseudo Recipient
                resolvePseudoRecipient(notifierEntityType, notifierEntityId, recipientDetail);
            } else if (recipientDetail.getRecipientType() == NotificationEnum.NotificationRecipientType.EXTERNAL_EMAIL_RECIPIENT) {

            }
            NotificationInfo notificationInfo = new NotificationInfo();
            List<NotificationInfo> list = EDApplicationInfo.getNotificationInformationList();

            /// Finding the Event Notification that is event type notification.
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getNotificationTypeEnumId() == NotificationEnum.NotificationTypeEnum.EVENT
                        && list.get(i).getParentEntityType() == eventNotification.getParentEntityType()
                        && list.get(i).getEntityType() == eventNotification.getEntityType()
                        && list.get(i).getNotificationEnumId() == eventNotification.getEventTypeNo()) {
                    /// If found the break from loop.
                    notificationInfo = list.get(i);
                    break;
                }
            }
            List<NotificationEnum.NotificationDeliveryType> supportedDeliveryType = notificationInfo.getSupportedDeliveryTypes();
            for (int i = 0; i < supportedDeliveryType.size(); i++) {
                for (int j = 0; ((j < notificationInfo.getSupportedLocalNotificationType().size() && supportedDeliveryType.get(i) == NotificationEnum.NotificationDeliveryType.LOCAL_NOTIFICATION) || j == 0); j++) {
                    NotificationRecipientDetail clonedrecipientDetail = recipientDetail;
                    clonedrecipientDetail.setDeliveryTime(new Date());
                    clonedrecipientDetail.setRecipientDeliveryType(supportedDeliveryType.get(i));
                    if (supportedDeliveryType.get(i) == NotificationEnum.NotificationDeliveryType.LOCAL_NOTIFICATION) {
                        clonedrecipientDetail.setRecipientSubDeliveryType(notificationInfo.getSupportedLocalNotificationType().get(i).getId());
                    }
                    resolvedRecipientDetailList.add(clonedrecipientDetail);
                }
            }
        }
        return resolvedRecipientDetailList;
    }


    public Tuple.Tuple2<NotificationQueue, List<NotificationAttachment>, String> resolveUNQToNotification(UnPreparedNotificationQueue unqEntity) throws EwpException {
        if (unqEntity.getSourceType() == PFEntityType.EVENT_NOTIFICATION.getValue()) {
            EventNotification resultTuple = new EventNotificationDataService().getEntity(unqEntity.getSourceId());
            if (resultTuple == null) {
                return new Tuple.Tuple2<NotificationQueue, List<NotificationAttachment>, String>(null, null, "");
            }
            int eventTypeNo = resultTuple.getEventTypeNo();
            switch (EDApplicationInfo.EDEmployeeEventNotification.values()[eventTypeNo]) {
                case EMPLOYEE_OUT_OF_OFFICE:
                    return resolveOutOfOfficeNotification(unqEntity);
            }
        } else if (unqEntity.getSourceType() == PFEntityType.CYCLIC_NOTIFICATION.getValue()) {
            ///Error appCyclicNotificationTypeNo  is zero.
            CyclicNotification resultTuple = new CyclicNotificationDataService().getEntity(unqEntity.getSourceId());
            if (resultTuple == null) {
                return new Tuple.Tuple2<NotificationQueue, List<NotificationAttachment>, String>(null, null, "");
            }
            int appCyclicNotificationTypeNo = resultTuple.getAppCyclicNotificationTypeNo();
            switch (EmployeeEnums.EDCyclicNotification.values()[appCyclicNotificationTypeNo]) {
                case WEEKLY_DIGEST:
                    return resolveWeekEmailNotification(unqEntity);
                default:
                    break;
            }
        }
        return new Tuple.Tuple2<NotificationQueue, List<NotificationAttachment>, String>(null, null, "");
    }

    private Tuple.Tuple2<NotificationQueue, List<NotificationAttachment>, String> resolveWeekEmailNotification(UnPreparedNotificationQueue unqEntity) throws EwpException {
        List<NotificationAttachment> attachmentList = new ArrayList<NotificationAttachment>();
        Map<EmployeeStatusEnum, UUID> statusIds = new HashMap<EmployeeStatusEnum, UUID>();
        /// Get OutEmployee Information And Creates XML.
        /// XmlDocument xmlDoc = new XmlDocument();
        /// Parse OtherInformation XML Document.
        ///XDocument notificationOtherData = XDocument.Parse(unqEntity.OtherInformation);
        /// Get General Information
        ///var generalElementList = notificationOtherData.Descendants("Data").Where(x => x.Descendants("Information").First(y => y.FirstAttribute.Value == "RecordType" && y.LastAttribute.Value == "General").HasElements);
        ///XElement dataElement = notificationOtherData.Descendants("Data").First();
        String tenantName = ""; /// dataElement.Descendants("Information").FirstOrDefault(x => x.FirstAttribute.Value == "TenantName").LastAttribute.Value;
        ///XmlElement employeeListNode = xmlDoc.CreateElement("EmployeeList");
        ///xmlDoc.AppendChild(employeeListNode);

        ///XmlElement tenantInformationElement = xmlDoc.CreateElement("TenantInformation");
        ///employeeListNode.AppendChild(tenantInformationElement);

        ///XmlElement outEmployeeCountElement = xmlDoc.CreateElement("OutEmployeeCount");
        ///outEmployeeCountElement.InnerText = dataElement.Descendants("Information").FirstOrDefault(x => x.FirstAttribute.Value == "OutEmployeeCount").LastAttribute.Value; ;
        ///tenantInformationElement.AppendChild(outEmployeeCountElement);

        ///XmlElement newEmployeeCountElement = xmlDoc.CreateElement("NewEmployeeCount");
        ///newEmployeeCountElement.InnerText = dataElement.Descendants("Information").FirstOrDefault(x => x.FirstAttribute.Value == "NewEmployeeCount").LastAttribute.Value; ;
        ///tenantInformationElement.AppendChild(newEmployeeCountElement);

        ///XmlElement tenantNameElement = xmlDoc.CreateElement("TenantName");
        ///tenantNameElement.InnerText = tenantName;
        ///tenantInformationElement.AppendChild(tenantNameElement);


        ///XmlElement tenantUrlElement = xmlDoc.CreateElement("TenantUrl");
        ///Todo: Add logic to get tenant url.
        ///tenantUrlElement.InnerText = "http:///www.tenantusl.com";
        ///tenantInformationElement.AppendChild(tenantUrlElement);

        ///XmlElement tenantImageIdElement = xmlDoc.CreateElement("TenantImageId");
        ///tenantImageIdElement.InnerText = Guid.NewGuid().ToString();
        ///tenantInformationElement.AppendChild(tenantImageIdElement);

        ///XmlElement applicationImageIdElement = xmlDoc.CreateElement("ApplicationImageId");
        ///applicationImageIdElement.InnerText = Guid.NewGuid().ToString();
        ///tenantInformationElement.AppendChild(applicationImageIdElement);

        ///XmlElement editNotificationUrlElement = xmlDoc.CreateElement("EditNotificationUrl");
        /// Todo: Please add notification page url.
        ///editNotificationUrlElement.InnerText = "http:///www.editnotificationurl.com";
        ///tenantInformationElement.AppendChild(editNotificationUrlElement);

        ///XmlElement webUrlElement = xmlDoc.CreateElement("WebUrl");
        /// Todo: Please add web url.
        ///webUrlElement.InnerText = "http:///www.weburl.com";
        ///tenantInformationElement.AppendChild(webUrlElement);
        NotificationAttachment tenantLogoImageAttachment = new NotificationAttachment();
        tenantLogoImageAttachment.setApplicationId(EDApplicationInfo.getAppId().toString());
        tenantLogoImageAttachment.setEmbeddedAttachment(true);
        tenantLogoImageAttachment.setTenantId(unqEntity.getTenantId().toString());
        tenantLogoImageAttachment.setFileString("");// dataElement.Descendants("Information").FirstOrDefault(x => x.FirstAttribute.Value == "TenantImage").LastAttribute.Value;
        tenantLogoImageAttachment.setFileName(""); ///tenantImageIdElement.InnerText;
        tenantLogoImageAttachment.setTenantId(unqEntity.getTenantId().toString());
        attachmentList.add(tenantLogoImageAttachment);
        NotificationAttachment appLogoImageAttachment = new NotificationAttachment();
        appLogoImageAttachment.setApplicationId(EDApplicationInfo.getAppId().toString());
        appLogoImageAttachment.setEmbeddedAttachment(true);
        appLogoImageAttachment.setTenantId(unqEntity.getTenantId().toString());
        appLogoImageAttachment.setFileString(""); /// FileHelper.ConvertImageToBase64(EDResource.ED_Image_AppLogo, EDResource.ED_Image_AppLogo.RawFormat);
        appLogoImageAttachment.setFileName(UUID.randomUUID().toString());
        appLogoImageAttachment.setTenantId(unqEntity.getTenantId().toString());
        attachmentList.add(appLogoImageAttachment);
        ///var outEmployeeElementList = notificationOtherData.Descendants("Data").Where(x => x.Element("Information").Attribute("Key").Value == "RecordType" && x.Element("Information").Attribute("Value").Value == "OutEmployee");
        String xslt = ""; ///EDResource.ED_Notification_XSL_WeekEmail;
        String title = "";
        String body = "";/// XSLHelper.XSLTransform(xslt, xmlDoc, out title);

        String recipientTargetId = "";
        if (unqEntity.getRecipientType() == NotificationEnum.NotificationRecipientType.INTERNAL_RECIPIENT.getId()) {
            BaseEntity resultTuple = new EmployeeDataService().getEntity(unqEntity.getRecipientId());
            if (resultTuple != null) {
                Employee emp = (Employee) resultTuple;
                recipientTargetId = emp.getLoginEmail() != null ? emp.getLoginEmail() : "";
            }
        } else if (unqEntity.getRecipientType() == NotificationEnum.NotificationRecipientType.INTERNAL_RECIPIENT.getId()) {
            recipientTargetId = unqEntity.getExternalRecipientEmail();
        }
        NotificationQueue notificationQueue = new NotificationQueue();
        notificationQueue.setDeliveryType(unqEntity.getDeliveryType());
        notificationQueue.setDeliverySubType(unqEntity.getDeliverySubType());
        notificationQueue.setDeliveryTime(unqEntity.getDeliveryTime());
        notificationQueue.setMessage1(title);
        notificationQueue.setMessage2(body);
        notificationQueue.setLastOperationType(unqEntity.getLastOperationType());
        notificationQueue.setSenderId(unqEntity.getSenderId().toString());
        notificationQueue.setTargetInfo(recipientTargetId);
        notificationQueue.setTenantId(unqEntity.getTenantId());
        return new Tuple.Tuple2<NotificationQueue, List<NotificationAttachment>, String>(notificationQueue, attachmentList, "");
    }

    /**
     * Resolve 'Out of Office' notification body.
     *
     * @param unqEntity
     * @return Tuple.Tuple2<NotificationQueue, List<NotificationAttachment>, String>
     */
    private Tuple.Tuple2<NotificationQueue, List<NotificationAttachment>, String> resolveOutOfOfficeNotification(UnPreparedNotificationQueue unqEntity) throws EwpException {
        List<NotificationAttachment> attachmentList = new ArrayList<NotificationAttachment>();
        ///XDocument notificationOtherData = XDocument.Parse(unqEntity.OtherInformation);
        ///XElement dataElement = notificationOtherData.Descendants("Data").First();
        String employeeStatus = ""; ///(Status)Convert.ToInt32(dataElement.Descendants("Information").FirstOrDefault(x => x.FirstAttribute.Value == "StatusId").LastAttribute.Value);
        /// Resolve XSL to final notification email/sms.
        String xslString = ""; ///EDResource.ED_Notification_XSL_NewStatusEmail;
        /// Prepare XSL parameter list.
        Map<String, String> param = new HashMap<String, String>();
        param.put("EmployeeFullName", "");
        param.put("StatusName", "");
        param.put("StatusDuration", "");
        param.put("StatusDescription", "");
        param.put("StatusDescriptionAndDurforKey: ation", "");
        param.put("StatusOperforKey: ation", "");
        param.put("StaforKey: tusId", "");
        param.put("TenantName", "");
        param.put("EmployeeInitials", "");
        param.put("LowerStatusName", "");
        ///TODO:Update below parameter from config file.
        param.put("NotificationSettingEditPageUrl", "http:///notificationSettingEditPageUrl.com");
        param.put("LoginPageUrl", "http:///loginPageUrl.com");
        param.put("UserProfilePageUrl", "http:///UserProfilePage.com");
        param.put("TenantUrl", "http:///TenantUrl.com");
        xslString = ""; ///EDResource.ED_Notification_XSL_NewStatusEmail;
        String title = "";
        String body = ""; ///XSLHelper.XSLTransform(xslString, string.Empty, out title, param);

        NotificationQueue notificationQueue = new NotificationQueue();
        notificationQueue.setDeliveryType(unqEntity.getDeliveryType());
        notificationQueue.setDeliverySubType(unqEntity.getDeliverySubType());
        notificationQueue.setDeliveryTime(unqEntity.getDeliveryTime());
        notificationQueue.setMessage1(title);
        notificationQueue.setMessage2(body);
        notificationQueue.setLastOperationType(unqEntity.getLastOperationType());
        notificationQueue.setSenderId(unqEntity.getSenderId().toString());
        ///ToDo: Think about recipient information. Either it get using UNQ recipient id or Otherinformation xml.
        EmployeeDataService empDS = new EmployeeDataService();
        BaseEntity resultTuple = empDS.getEntity(unqEntity.getRecipientId());
        if (resultTuple != null) {
            Employee employee = (Employee) resultTuple;
            notificationQueue.setTargetInfo(employee.getLoginEmail());
        } else {
            notificationQueue.setTargetInfo(""); /// notificationOtherData.Descendants("Information").FirstOrDefault(x => x.FirstAttribute.Value == "EmployeeEmail").LastAttribute.Value;
        }
        notificationQueue.setTenantId(unqEntity.getTenantId());
        ///Guid queueId = notificationQueueDS.add(notificationQueue);
        NotificationAttachment tenantLogoImageAttachment = new NotificationAttachment();
        tenantLogoImageAttachment.setApplicationId(EDApplicationInfo.getAppId().toString());
        tenantLogoImageAttachment.setEmbeddedAttachment(true);
        tenantLogoImageAttachment.setTenantId(unqEntity.getTenantId().toString());
        tenantLogoImageAttachment.setFileString(""); /// notificationOtherData.Descendants("Information").FirstOrDefault(x => x.FirstAttribute.Value == "TenantImage").LastAttribute.Value;
        tenantLogoImageAttachment.setFileName("TenantLogo");
        ///tenantLogoImageAttachment.notificationQueueId = queueId;
        tenantLogoImageAttachment.setTenantId(unqEntity.getTenantId().toString());
        attachmentList.add(tenantLogoImageAttachment);
        NotificationAttachment noAvatarImageAttachment = new NotificationAttachment();
        noAvatarImageAttachment.setApplicationId(EDApplicationInfo.getAppId().toString());
        noAvatarImageAttachment.setEmbeddedAttachment(true);
        noAvatarImageAttachment.setTenantId(unqEntity.getTenantId().toString());
        noAvatarImageAttachment.setFileString("");///notificationOtherData.Descendants("Information").FirstOrDefault(x => x.FirstAttribute.Value == "EmployeeImage").LastAttribute.Value;
        noAvatarImageAttachment.setFileName("NoAvatarImage");
        ///noAvatarImageAttachment.notificationQueueId = queueId;
        noAvatarImageAttachment.setTenantId(unqEntity.getTenantId().toString());
        attachmentList.add(noAvatarImageAttachment);
        NotificationAttachment statusImageAttachment = new NotificationAttachment();
        statusImageAttachment.setApplicationId(EDApplicationInfo.getAppId().toString());
        statusImageAttachment.setEmbeddedAttachment(true);
        statusImageAttachment.setTenantId(unqEntity.getTenantId().toString());
        statusImageAttachment.setFileString(""); /// (EDDataServiceFactory.GetDataService<EmployeeStatus>(EDEntityType.EmployeeStatus) as! IEmployeeStatusDataService).GetStatusImageStringByStatusId(employeeStatus);
        statusImageAttachment.setFileName("StatusImage");
        ///statusImageAttachment.notificationQueueId = queueId;
        statusImageAttachment.setTenantId(unqEntity.getTenantId().toString());
        attachmentList.add(statusImageAttachment);

        NotificationAttachment applicationImageAttachment = new NotificationAttachment();
        applicationImageAttachment.setApplicationId(EDApplicationInfo.getAppId().toString());
        applicationImageAttachment.setEmbeddedAttachment(true);
        applicationImageAttachment.setTenantId(unqEntity.getTenantId().toString());
        applicationImageAttachment.setFileString(""); /// FileHelper.ConvertImageToBase64(EDResource.ED_Image_AppLogo, EDResource.ED_Image_AppLogo.RawFormat);
        applicationImageAttachment.setFileName("ApplicationImage");
        ///applicationImageAttachment.notificationQueueId = queueId;
        applicationImageAttachment.setTenantId(unqEntity.getTenantId().toString());
        attachmentList.add(applicationImageAttachment);
        return new Tuple.Tuple2<NotificationQueue, List<NotificationAttachment>, String>(null, null, "");
    }


    /**
     * It is
     * used to
     * resulve psudo
     * users.
     *
     * @param notifierEntityType
     * @param notifierEntityId
     * @param pseudoRecipient
     */
    private void resolvePseudoRecipient(EDEntityType notifierEntityType, UUID notifierEntityId, NotificationRecipientDetail pseudoRecipient) throws EwpException {
        if (pseudoRecipient.getPseudoUserId() == 1 /** EDEmployeePseudoUser.ReportTo.rawValue*/) {
            EmployeeDataService empDS = new EmployeeDataService();
            Employee employee = (Employee) empDS.getEntity(notifierEntityId);
            pseudoRecipient.setRecipientType(NotificationEnum.NotificationRecipientType.INTERNAL_RECIPIENT);
            pseudoRecipient.setRecipientId(employee.getReportTo() == null ? Utils.emptyUUID() : UUID.fromString(employee.getReportTo()));
        }
    }

    /**
     * It is used to resolve event base notification list from list of notification.
     *
     * @param tenantId
     * @param notifierEntityType
     * @param notifierEntityId
     * @return NotificationMessageInfo
     */
    public NotificationMessageInfo getResolvedNotificationQueueMessageBody(UUID tenantId, EDEntityType notifierEntityType, UUID notifierEntityId) throws EwpException {
        NotificationQueue notificationQueue = new NotificationQueue();
        List<NotificationAttachment> attachmentList = new ArrayList<>();
        switch (notifierEntityType) {
            case EMPLOYEE_STATUS:
                EmployeeStatusDataService statusService = new EmployeeStatusDataService();
                BaseEntity resultTuple = statusService.getEntity(notifierEntityId);
                if (resultTuple != null) {
                    EmployeeStatus status = (EmployeeStatus) resultTuple;
                    String str = status.getEmployeeFullName() + " is " + status.getStatusDisplayString();
                    notificationQueue.setMessage1(str);
                }
                break;
            default:
                break;
        }
        return new NotificationMessageInfo(notificationQueue, attachmentList);
    }

    /**
     * @param subscribe
     * @return UUID
     */
    public UUID updateThisWeekEmailNotification(boolean subscribe) throws EwpException {
        UUID weekCyclicNotificationId = Utils.emptyUUID();
        CyclicNotificationDataService cyclicNotificationDS = new CyclicNotificationDataService();
        CyclicNotification weekEmailNotification = null;
        Employee employee = new EmployeeDataService().getEmployeeFromUserId(EwpSession.getSharedInstance().getUserId());
        if (subscribe) {
            if (weekEmailNotification == null) {
                weekEmailNotification = getCyclicNotification();
                CyclicNotificationDetails weekEmailDetail = new CyclicNotificationDetails();
                weekEmailDetail.setSubscriptionDateTime(new Date());
                weekEmailDetail.setSubscriptionDay(NotificationEnum.DayMask.SUNDAY_MASK.getId());
                weekEmailDetail.setApplicationId(EDApplicationInfo.getAppId().toString());
                weekEmailDetail.setTenantId(EwpSession.getSharedInstance().getTenantId());
                CyclicNotificationLinking employeeWeekEmailLinking = new CyclicNotificationLinking();
                employeeWeekEmailLinking.setApplicationId(EDApplicationInfo.getAppId().toString());
                employeeWeekEmailLinking.setSourceEntityType(EDEntityType.EMPLOYEE.getId());
                employeeWeekEmailLinking.setSourceEntityId(employee.getEntityId());
                employeeWeekEmailLinking.setTenantId(EwpSession.getSharedInstance().getTenantId());
                NotificationRecipient weekEmailRecipient = new NotificationRecipient();
                weekEmailRecipient.setRecipientId(EwpSession.getSharedInstance().getUserId());
                weekEmailRecipient.setRecipientType(NotificationEnum.NotificationRecipientType.INTERNAL_RECIPIENT.getId());
                weekEmailRecipient.setSourceEntityType(PFEntityType.CYCLIC_NOTIFICATION.getValue());
                weekEmailRecipient.setTenantId(EwpSession.getSharedInstance().getTenantId());
                CyclicNotificationInfo notificationInfo = new CyclicNotificationInfo();
                notificationInfo.setCyclicNotificationEntity(weekEmailNotification);
                notificationInfo.setCyclicNotificationDetailsList(new ArrayList<CyclicNotificationDetails>());
                notificationInfo.getCyclicNotificationDetailsList().add(weekEmailDetail);
                notificationInfo.setCyclicNotificationLinkingEntity(employeeWeekEmailLinking);
                notificationInfo.setRecipientsList(new ArrayList<NotificationRecipient>());
                notificationInfo.getRecipientsList().add(weekEmailRecipient);
                notificationInfo.setApplicationId(EDApplicationInfo.getAppId().toString());
                cyclicNotificationDS.addCyclicNotificationInfo(notificationInfo);
            }
        } else if (subscribe == false && weekEmailNotification != null) {
            cyclicNotificationDS.delete(weekEmailNotification);
        }
        return weekCyclicNotificationId;
    }

    /**
     * @return CyclicNotification
     */
    private CyclicNotification getCyclicNotification() {
        CyclicNotification weekEmailNotification = new CyclicNotification();
        weekEmailNotification.setActive(true);
        weekEmailNotification.setCyclicType(NotificationEnum.CyclicType.REPETITION.getId());
        weekEmailNotification.setCyclicSubtype(NotificationEnum.CyclicSubType.CYCLIC.getId());
        weekEmailNotification.setCyclicPattern(NotificationEnum.CyclicPattern.WEEKDAYS.getId());
        weekEmailNotification.setFrequencyInterval(1);
        weekEmailNotification.setFrequencyUnit(NotificationEnum.FrequencyUnit.WEEK.getId());
        weekEmailNotification.setRepeatStartDate(new Date());
        ///weekEmailNotification.re
        weekEmailNotification.setApplicationId(EDApplicationInfo.getAppId().toString());
        weekEmailNotification.setActionId(EmployeeEnums.EDCyclicNotificationActionId.SEND_WEEK_EMAIL.getId());
        weekEmailNotification.setAppCyclicNotificationTypeNo(EmployeeEnums.EDCyclicNotification.WEEKLY_DIGEST.getId());
        return weekEmailNotification;
    }

    /**
     * @return boolean
     * @throws EwpException
     */
    public boolean getLoginEmployeeStatusUpdatesEmailSubscription() throws EwpException {
        Employee employee = new EmployeeDataService().getEmployeeFromUserId(EwpSession.getSharedInstance().getUserId());
        if (employee == null) {
            return false;
        }
        EventNotificationDataService s = new EventNotificationDataService();
        EventNotification eventNotification = new EventNotificationDataService().getEventNotificationByOwnerAndEntityTypeAndEntityIdAndTenantIdAndAppId(employee.getEntityId(), EDEntityType.EMPLOYEE.getId(), employee.getEntityId(), EwpSession.getSharedInstance().getTenantId(), EDApplicationInfo.getAppId().toString());
        if (eventNotification == null) {
            return false;
        } else {
            return true;
        }
    }


    public List<CyclicNotificationCallbackResult> getWeekEmailRecipientAndOtherInformation() throws EwpException {
        List<CyclicNotificationCallbackResult> resultList = new ArrayList<CyclicNotificationCallbackResult>();
        EmployeeDataService employeeDS = new EmployeeDataService();
        List<NotificationRecipientDetail> recipientDetailList = getWeekEmailRecipientDetailsList();

        for (NotificationRecipientDetail recipient : recipientDetailList) {
            CyclicNotificationCallbackResult result = new CyclicNotificationCallbackResult();
            BaseEntity resultTuple = employeeDS.getEntity(recipient.getRecipientId());
            if (resultTuple == null) {
                continue;
            }
            UUID tenantId = ((Employee) resultTuple).getTenantId();
            result.setApplicationId(EDApplicationInfo.getAppId().toString());
            result.setTenantId(tenantId);
            result.getRecipientList().add(recipient);
            result.setOtherInformationList(getWeekEmailInformationByTenantId(tenantId));
            resultList.add(result);
        }
        return resultList;
    }

    private Map<String, String> getWeekEmailInformationByTenantId(UUID tenantId) throws EwpException {
        EmployeeDataService employeeDS = new EmployeeDataService();
        TenantDataService tenantDS = new TenantDataService();
        Map<String, String> otherInformation = new HashMap<String, String>();
        Cursor cursor = employeeDS.getCurrentWeekEmployeeListByTenantIdAsResultSet(tenantId);
        BaseEntity resultTupleTenant = tenantDS.getEntity(tenantId);
        if (resultTupleTenant == null) {
            return otherInformation;
        }
        Tenant tenant = (Tenant) resultTupleTenant;
        Map<String, String> generalInformation = new HashMap<String, String>();
        generalInformation.put("RecordType", "General");
        generalInformation.put("NewEmployeeCount", "0");
        generalInformation.put("TenantImage", tenant.getLogoAsBase64String());
        generalInformation.put("TenantName", tenant.getName());
        ///otherInformation.append(generalInformation);
        getCurrentWeekEmployeeList(tenantId, otherInformation);
        /// Append here because, we will get outof employee count after calling above method here.
        generalInformation.put("OutEmployeeCount", String.valueOf(otherInformation.size()));
        otherInformation.putAll(generalInformation);
        List<Employee> newEmployeeList = employeeDS.getLastWeekCreatedEmployeeListByTenantId(tenantId);
        if (newEmployeeList == null) {
            return otherInformation;
        }
        generalInformation.put("NewEmployeeCount", String.valueOf(newEmployeeList.size()));
        String date = "";
        for (int i = 0; i < newEmployeeList.size(); i++) {
            /// Initialize Other Information.
            Employee employee = newEmployeeList.get(i);
            Map<String, String> newEmployeeInformation = new HashMap<String, String>();
            newEmployeeInformation.put("RecordType", "NewEmployee");
            newEmployeeInformation.put("EmployeeFullName", employee.getFullName());
            date = Utils.stringFromDate(employee.getCreatedAt());
            if (date != null && !"".equals(date)) {
                newEmployeeInformation.put("CreatedDateString", date);
            } else {
                newEmployeeInformation.put("CreatedDateString", "");
            }
            newEmployeeInformation.put("EmployeeInitials", employee.getFirstName());
            newEmployeeInformation.put("EmployeeEmail", employee.getLoginEmail());
            newEmployeeInformation.put("EmployeeId", employee.getEntityId().toString());
            //newEmployeeInformation.updateValue("EmployeeImage",  forKey: employee.getPictureAsBase64String());
            newEmployeeInformation.put("EmployeeImage", "");
            otherInformation.putAll(newEmployeeInformation);
        }
        return otherInformation;
    }

    /**
     * It is used to get current week employee status list.
     *
     * @param tenantId
     * @param otherInformation
     */
    private void getCurrentWeekEmployeeList(UUID tenantId, Map<String, String> otherInformation) throws EwpException {
        EmployeeDataService employeeDS = new EmployeeDataService();
        Cursor cursor = employeeDS.getCurrentWeekEmployeeListByTenantIdAsResultSet(tenantId);
        /// If error occured then return as! 0.
        if (cursor == null) {
            return;
        }
        /// loop through the item and create the entity and set repactive properties.
        while (cursor.moveToNext()) {
            /// getting the entity instance
            /// Initialize Other Information.
            Map<String, String> outEmployeeInformation = new HashMap<String, String>();
            outEmployeeInformation.put("RecordType", "OutEmployee");
            String fullName = cursor.getString(cursor.getColumnIndex("FullName"));
            outEmployeeInformation.put("EmployeeFullName", fullName == null ? "" : fullName);
            String status = cursor.getString(cursor.getColumnIndex("EmpStatus"));
            if (status != null && !"".equals(status)) {
                outEmployeeInformation.put("StatusName", EmployeeStatusEnum.values()[Integer.parseInt(status)].toString());
                outEmployeeInformation.put("StatusId", status);
            } else {
                outEmployeeInformation.put("StatusName", "");
                outEmployeeInformation.put("StatusId", "0");
            }
            String statusDuration = cursor.getString(cursor.getColumnIndex("EmpStatus"));
            if (statusDuration != null && !"".equals(statusDuration)) {
                outEmployeeInformation.put("StatusDuration", statusDuration);
            } else {
                outEmployeeInformation.put("StatusDuration", "");
            }
            String desc = cursor.getString(cursor.getColumnIndex("Description"));
            if (desc != null) {
                outEmployeeInformation.put("StatusDescription", desc);
            } else {
                outEmployeeInformation.put("StatusDescription", "");
            }
            String employeeInitials = cursor.getString(cursor.getColumnIndex("FullName"));
            if (employeeInitials != null) {
                outEmployeeInformation.put("EmployeeInitials", employeeInitials);
            } else {
                outEmployeeInformation.put("EmployeeInitials", "");
            }
            String employeeEmail = cursor.getString(cursor.getColumnIndex("Email"));
            if (employeeEmail != null) {
                outEmployeeInformation.put("EmployeeEmail", employeeEmail);
            } else {
                outEmployeeInformation.put("EmployeeEmail", "");
            }

            String employeeId = cursor.getString(cursor.getColumnIndex("EmployeeId"));
            if (employeeId != null) {
                outEmployeeInformation.put("EmployeeId", employeeId);
            } else {
                outEmployeeInformation.put("EmployeeId", "");
            }

            String picture = cursor.getString(cursor.getColumnIndex("Picture"));
            if (picture != null) {
                outEmployeeInformation.put("EmployeeImage", picture);
            } else {
                outEmployeeInformation.put("EmployeeImage", "");
            }
            otherInformation.putAll(outEmployeeInformation);
        }
    }

    public List<NotificationRecipientDetail> getWeekEmailRecipientDetailsList() throws EwpException {
        EmployeeDataService employeeDS = new EmployeeDataService();
        List<NotificationRecipientDetail> recipients = new ArrayList<NotificationRecipientDetail>();
        /// Get Recipient List.
        /// Get All Users Who Subscribes For Week Email.
        List<Employee> employees = employeeDS.getWeekEmailSubscribedEmployeeList();
        if (employees == null) {
            return recipients;
        }

        for (Employee emp : employees) {
            /// Since we donopt have any pseudo user and external user email functionality for week email so we dont handle it here
            /// otherwise external email and pseudo user resolution code come here.
            NotificationRecipientDetail recipient = new NotificationRecipientDetail();
            Employee employee = emp;
            recipient.setDeliveryTime(new Date());
            recipient.setNotificationRecipientId(employee.getEntityId());
            recipient.setNotificationType(EmployeeEnums.EDCyclicNotification.WEEKLY_DIGEST.getId());
            recipient.setRecipientDeliveryType(NotificationEnum.NotificationDeliveryType.EMAIL);
            recipient.setRecipientEmail(employee.getLoginEmail());
            recipient.setRecipientId(employee.getEntityId());
            recipient.setRecipientType(NotificationEnum.NotificationRecipientType.INTERNAL_RECIPIENT);
            recipients.add(recipient);
        }
        return recipients;
    }

    public static EDNotificationDataService getEdNotificationDataService() {
        return edNotificationDataService;
    }


    public static EventNotificationDataService getEventNotificationDS() {
        return eventNotificationDS;
    }

}
