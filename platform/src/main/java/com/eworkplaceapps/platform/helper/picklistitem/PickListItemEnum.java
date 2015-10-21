//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/20/2015
//===============================================================================
package com.eworkplaceapps.platform.helper.picklistitem;

public class PickListItemEnum {


    /**
     * Specifies the different possible values of Notification Delivery PICK_LIST items.
     */
    public enum NotificationDeliveryItem {

        // Represents a 'NONE' notification delivery. It does not has picklist item.
        NONE(0),

        // Represents a 'IMMEDIATE' notification delivery picklist item.
        IMMEDIATE(1),

        // Represents a 'DAILY Summary' notification delivery picklist item.
        DAILY_SUMMARY(2),

        // Represents a 'DAILY Details' notification delivery picklist item.
        DAILY_DETAILS(3);

        private int id;

        NotificationDeliveryItem(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

}
