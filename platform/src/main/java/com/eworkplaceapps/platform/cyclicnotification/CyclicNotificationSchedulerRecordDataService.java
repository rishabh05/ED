//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/29/2015
//===============================================================================

package com.eworkplaceapps.platform.cyclicnotification;


import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.exception.EwpException;

import java.util.Date;

public class CyclicNotificationSchedulerRecordDataService extends BaseDataService<CyclicNotificationSchedulerRecord> {


    CyclicNotificationSchedulerRecordData dataDelegate = new CyclicNotificationSchedulerRecordData();

    /**
     * Initializes a new instance of the CyclicNotificationSchedulerRecordDataService class.
     */
    public CyclicNotificationSchedulerRecordDataService() {
        super("CYCLIC_NOTIFICATION_SCHEDULER_RECORD");
    }

    @Override
    public CyclicNotificationSchedulerRecordData getDataClass() {
        return dataDelegate;
    }

    /**
     * ----------------------- Begin Class Methods ---------------
     *
     * @param tickTime DATE
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */

    public Cursor getScheduledRecordsWithCyclicNotificationAsResultSet(Date tickTime) throws EwpException {
        return dataDelegate.getScheduledRecordsWithCyclicNotificationAsResultSet(tickTime);
    }

}
