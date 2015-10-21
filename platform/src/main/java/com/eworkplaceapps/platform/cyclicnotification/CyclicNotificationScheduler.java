//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/29/2015
//===============================================================================
package com.eworkplaceapps.platform.cyclicnotification;

import android.database.Cursor;

import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.notification.NotificationEnum;
import com.eworkplaceapps.platform.notification.NotificationRecipientDataService;
import com.eworkplaceapps.platform.notificationworkflow.RawNotification;
import com.eworkplaceapps.platform.utils.Utils;
import com.eworkplaceapps.platform.utils.enums.PFEntityType;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class CyclicNotificationScheduler {

    //---------------------- Begin Member Variables And Components

    // Time interval (in minutes) to check for reminder execution.
    int executionFrequency = 1;

    // Timer last trigger time to schedule the timer for next execution.
    Date lastExecutionTime;

    CyclicNotificationSchedulerRecordDataService cyclicNotificationSchedulerRecordDataService;
    NotificationRecipientDataService notificationRecipientDataService;

    //---------------------- End Member Variables And Components

    //----------------------  Begin init

    /// Default constructor to initialize the member variables and other components.
    public CyclicNotificationScheduler() {
        //TODO HERE getDateMinvalue not present
        // lastExecutionTime = Utils.getDateMinvalue();
        cyclicNotificationSchedulerRecordDataService = new CyclicNotificationSchedulerRecordDataService();
        notificationRecipientDataService = new NotificationRecipientDataService();
    }

    //----------------------  End Init

    //---------------------- Begin Public Methods

    public void execute(Date tickTime) throws EwpException {

        // Gets ALL Scheduled CYCLIC Notification Records.
        Cursor resultSet = cyclicNotificationSchedulerRecordDataService.getScheduledRecordsWithCyclicNotificationAsResultSet(tickTime);

        if (resultSet != null) {
            // Gets ALL Recipients using Parent Type and Id.
            while (resultSet.moveToNext()) {

                RawNotification rawNotification = new RawNotification();
                String applicationId = resultSet.getColumnName(resultSet.getColumnIndex("ApplicationId"));
                if (applicationId != null) {
                    rawNotification.setApplicationId(applicationId);
                }

                String tenantId = resultSet.getColumnName(resultSet.getColumnIndex("TenantId"));
                if (tenantId != null) {
                    rawNotification.setLoginTenantId(UUID.fromString(tenantId));
                }

                rawNotification.setNotificationType(NotificationEnum.NotificationTypeEnum.CYCLIC);

                String sourceEntityType = resultSet.getColumnName(resultSet.getColumnIndex("SourceEntityType"));
                if (sourceEntityType != null) {
                    rawNotification.setNotificationEntityType(Integer.parseInt(sourceEntityType));
                }

                String sourceEntityId = resultSet.getColumnName(resultSet.getColumnIndex("SourceEntityId"));
                if (sourceEntityId != null) {
                    rawNotification.setNotifierEntityId(UUID.fromString(sourceEntityId));
                }

                rawNotification.setNotificationEntityType(PFEntityType.CYCLIC_NOTIFICATION.getValue());

                String cyclicNotificationId = resultSet.getColumnName(resultSet.getColumnIndex("CyclicNotificationId"));
                if (cyclicNotificationId != null) {
                    rawNotification.setCyclicNotificationId(UUID.fromString(cyclicNotificationId));
                    rawNotification.setNotificationEntityId(UUID.fromString(cyclicNotificationId));
                }

                String schedulerRecordId = resultSet.getColumnName(resultSet.getColumnIndex("EntityId"));
                UUID cyclicNotificationSchedulerRecordId = Utils.emptyUUID();
                if (schedulerRecordId != null) {
                    cyclicNotificationSchedulerRecordId = UUID.fromString(schedulerRecordId);
                }

                // Calculate Next Execution DATE and UPDATE it in scheduler record.
                CyclicNotificationScheduler.calculateNextExecutionDate(rawNotification.getCyclicNotificationId(), cyclicNotificationSchedulerRecordId);

                // Gets CYCLIC Notification Recipient List Using Callback.
                // TODO: Use Callback instead of direct reference.
                //TODO HERE
                //  List resultList = EDNotificationDataService.edNotificationDataService.instance.getWeekEmailRecipientAndOtherInformation();

                // Call Workflow.
                //TODO HERE
                //for (ResultItem resultItem : resultList) {
                //  rawNotification.otherData = resultItem.otherInformationList;
                //rawNotification.notificationRecipientDetailList = resultItem.recipientList;
                //NotificationHelper.StartEventNotificationWorkflow(rawNotification, true, null);
                //}

            }
        }

    }

    private static void calculateNextExecutionDate(UUID cyclicNotificationId, UUID cyclicSchedulerRecordId) throws EwpException {
        CyclicNotificationDataService cyclicNotificationDS = new CyclicNotificationDataService();
        CyclicNotificationSchedulerRecordDataService cyclicNotificationSchedulerRecordDS = new CyclicNotificationSchedulerRecordDataService();

        // Getting repetition.
        CyclicNotification entity = cyclicNotificationDS.getEntity(cyclicNotificationId);
        if (entity == null) {
            // If any error occured or object is nil then return from here.
            return;
        }

        // Getting CYCLIC_NOTIFICATION_DETAILS list from CyclicNotificationId.
        List<CyclicNotificationDetails> repetitionDetails = new CyclicNotificationDetailsDataService().getCyclicNotificationDetailsListByCyclicNotificationId(cyclicNotificationId);
        if (repetitionDetails == null) {
            // If any error occured or object is nil then return from here.
            return;
        }
        // Getting CYCLIC_NOTIFICATION_SCHEDULER_RECORD
        CyclicNotificationSchedulerRecord schedulerRecord = cyclicNotificationSchedulerRecordDS.getEntity(cyclicSchedulerRecordId);
        if (schedulerRecord == null) {
            // If any error occured or object is nil then return from here.
            return;
        }

        // Getting minimum date.
        //TODO HERE
        // DATE minDate = Utils.getDateMinvalue();
        // Creating instance of RepetitionRules.
        //TODO HERE
        //RepetitionRules repetitionRules = new RepetitionRules();

        //TODO HERE
        //for (obj: repetitionDetails) {

        // CYCLIC_NOTIFICATION_DETAILS repetitionDetail = (CYCLIC_NOTIFICATION_DETAILS) obj;
        // repetitionRules.addTriggerPart(repetitionDetail.getSubscriptionDateTime(), timeOnly);
        // Utils.compareDatesWithMinutes(repetitionDetail.getSubscriptionDateTime(), minDate);
        //    }

        //TODO HERE
      /*  repetitionRules.startDate = repetition.repeatStartDate;
        repetitionRules.endDate = repetition.repeatEndDate;
        repetitionRules.repetitionType = RepetitionRules.RepetitionType(rawValue:
        repetition.cyclicSubtype)!
                repetitionRules.cyclicalType = RepetitionRules.CyclicalType(rawValue:
        repetition.cyclicPattern)!
                repetitionRules.cyclicalTimeUnit = RepetitionRules.TimeUnit(rawValue:
        repetition.frequencyUnit)!
                repetitionRules.cyclicalInterval = repetition.frequencyInterval;
        repetitionRules.cyclicalDayMask = repetition.days;*/

        //  DATE scheduleTime = schedulerRecord.getNextReminderDate();

        //    do {
        //TODO HERE
        //    schedulerRecord.setNextReminderDate(repetitionRules.nextTriggerAt(scheduleTime));
        //  }
        //TODO HERE
        // while (schedulerRecord.getNextReminderDate() != null && Utils.compareDatesWithMinutes(schedulerRecord.getNextReminderDate(), scheduleTime) <= 0);
        cyclicNotificationSchedulerRecordDS.update(schedulerRecord);

    }
}
