//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/29/2015
//===============================================================================
package com.eworkplaceapps.platform.cyclicnotification;

import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.notification.NotificationRecipient;
import com.eworkplaceapps.platform.notification.NotificationRecipientDataService;
import com.eworkplaceapps.platform.utils.Utils;
import com.eworkplaceapps.platform.utils.enums.PFEntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class CyclicNotificationDataService extends BaseDataService<CyclicNotification> {

    CyclicNotificationData dataDelegate = new CyclicNotificationData();

    // Initializes a new instance of the CyclicNotificationDataService class.
    public CyclicNotificationDataService() {
        super("CYCLIC_NOTIFICATION");
    }

    @Override
    public CyclicNotificationData getDataClass() {
        return dataDelegate;
    }

    //----------------------- Begin Class Methods ---------------

    public CyclicNotification getCyclicNotificationByUserIdAndEntityTypeAndEntityId(UUID userId, UUID entityId, UUID tenantId, int entityType) throws EwpException {
        CyclicNotification cyclicNotification = dataDelegate.getCyclicNotificationByUserIdAndEntityTypeAndEntityId(userId, entityId, tenantId, entityType);

        if (cyclicNotification != null) {
            return cyclicNotification;
        }
        return null;
    }

    public UUID addCyclicNotificationInfo(CyclicNotificationInfo cyclicNotificationInfo) throws EwpException {
        UUID cyclicNotificationId = Utils.emptyUUID();

        // Operation type ADD.
        //TODO HERE no property lastOperationType in  CYCLIC_NOTIFICATION
         //cyclicNotificationInfo.getCyclicNotificationEntity().lastOperationType = DatabaseOperationType.ADD;

        // Validate CYCLIC_NOTIFICATION info.
        //ValidateRepetitionInfoOnAddUpdate(cyclicNotificationInfo);

        // ADD CYCLIC_NOTIFICATION.
        cyclicNotificationId = (UUID) super.add(cyclicNotificationInfo.getCyclicNotificationEntity());

        // ADD CYCLIC_NOTIFICATION_DETAILS
        if (!cyclicNotificationInfo.getCyclicNotificationDetailsList().isEmpty()) {
            List<CyclicNotificationDetails> cyclicNotificationDetailDS = new ArrayList<>();
            for (CyclicNotificationDetails cyclicNotificationDetails : cyclicNotificationInfo.getCyclicNotificationDetailsList()) {
                // Assign Parent CyclicNotificationId And ApplicationId
                cyclicNotificationDetails.setCyclicNotificationId(cyclicNotificationId);
                cyclicNotificationDetails.setApplicationId(cyclicNotificationInfo.getApplicationId());
                cyclicNotificationDetailDS.add(cyclicNotificationDetails);
            }
        }

        // ADD CYCLIC_NOTIFICATION_LINKING
        cyclicNotificationInfo.getCyclicNotificationLinkingEntity().setCyclicNotificationId(cyclicNotificationId);
        cyclicNotificationInfo.getCyclicNotificationLinkingEntity().setApplicationId(cyclicNotificationInfo.getApplicationId());
       // CyclicNotificationLinkingDataService repetitionLinkingOps = new CyclicNotificationLinkingDataService();
     //   UUID repetitionLinkingId = (UUID) repetitionLinkingOps.add(cyclicNotificationInfo.getCyclicNotificationLinkingEntity());

        // ADD CYCLIC Notification Scheduler Record
        CyclicNotificationSchedulerRecord schedulerRecords = new CyclicNotificationSchedulerRecord();
        schedulerRecords.setStartDate(cyclicNotificationInfo.getCyclicNotificationEntity().getRepeatStartDate());
        schedulerRecords.setEndDate(cyclicNotificationInfo.getCyclicNotificationEntity().getRepeatEndDate());
        schedulerRecords.setCyclicNotificationId(cyclicNotificationId);
        // ToDo: check this line.
        //TODO HERE RepetitionRules class is used in  getFirstExecutionDate method. getFirstExecutionDate Need to be implemented in different way.
       // schedulerRecords.setNextReminderDate(getFirstExecutionDate(cyclicNotificationInfo, false));

        CyclicNotificationSchedulerRecordDataService schedulerRecordDS = new CyclicNotificationSchedulerRecordDataService();
        schedulerRecordDS.add(schedulerRecords);

        // ADD Recipients.
        NotificationRecipientDataService notificationRecipientDS = new NotificationRecipientDataService();
        // ADD each recipient's record.
        for (NotificationRecipient recipient : cyclicNotificationInfo.getRecipientsList()) {
            recipient.setSourceEntityId(cyclicNotificationId);
            recipient.setSourceEntityType(PFEntityType.CYCLIC_NOTIFICATION.getValue());
            notificationRecipientDS.add(recipient);
        }
        return cyclicNotificationId;
    }

    //TODO HERE
    // Method returns next execution date.
/*    private DATE getFirstExecutionDate(CyclicNotificationInfo info, Boolean onlyTime) {
        RepetitionRules rules = new RepetitionRules();
        rules.startDate = info.getCyclicNotificationEntity().getRepeatStartDate();
        rules.endDate = info.getCyclicNotificationEntity().getRepeatEndDate();


        for (CYCLIC_NOTIFICATION_DETAILS dl : info.getCyclicNotificationDetailsList()) {
            rules.addTriggerPart(dl.getSubscriptionDateTime(), onlyTime);
        }

        rules.cyclicalDayMask = info.getCyclicNotificationEntity().getDays();
        rules.cyclicalInterval = info.getCyclicNotificationEntity().getFrequencyInterval();
        // FrequencyUnit and tTime unit both are same.
        rules.cyclicalTimeUnit = RepetitionRules.TimeUnit(info.getCyclicNotificationEntity().getFrequencyUnit());
        rules.cyclicalType = RepetitionRules.CyclicalType(info.getCyclicNotificationEntity().getCyclicPattern());
        rules.repetitionType = RepetitionRules.RepetitionType(info.getCyclicNotificationEntity().getCyclicSubtype());
        if (info.getCyclicNotificationEntity().entityId.toString() != Utils.emptyUUID().toString()) {
            return rules.firstTriggerAt();
        } else {
            return rules.firstTriggerAt();
        }
    }*/
}
