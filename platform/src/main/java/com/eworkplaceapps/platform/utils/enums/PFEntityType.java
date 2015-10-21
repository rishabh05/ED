//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/21/2015
//===============================================================================
package com.eworkplaceapps.platform.utils.enums;


public enum PFEntityType {
    NONE(0), ALL(1), TENANT(2), TENANT_USER(3),
    // The 'ROLE' entity type.
    ROLE(4),

    // The 'ROLE Permission' entity type
    ROLE_PERMISSION(5),

    // The 'ROLE_LINKING' entity type.
    ROLE_LINKING(6),

    // The 'EntityTemplateField' entity type.
    ENTITY_CUSTOM_FIELD(7),

    // The 'UDF_VALUES' entity type.
    UDF_VALUES(8),

    // The PickList entity type.
    PICK_LIST(9),

    // The PICK_LIST_ITEM entity type.
    PICK_LIST_ITEM(10),

    // The 'USER_ENTITY_LINK' entity type.
    USER_ENTITY_LINK(11),

    // The 'UserPreferences' entity type.
    USER_PREFERENCE(12),

    // The tenant application entity type.
    TENANT_APPLICATION(13),

    // The token info entity type.
    TOKEN_INFO(14),

    // Entity field detail.
    ENTITY_FIELD_DETAIL(15),

    // Entity field permission.
    ENTITY_FIELD_PERMISSION(16),

    // The NotificationDefinition entity type.
    EVENT_NOTIFICATION(17),

    // The 'NOTIFICATION_RECIPIENT' entity type.
    NOTIFICATION_RECIPIENT(18),

    // The 'UNPREPARED_NOTIFICATION_QUEUE' entity type.
    UNPREPARED_NOTIFICATION_QUEUE(19),

    // The 'NOTIFICATION_QUEUE' entity type.
    NOTIFICATION_QUEUE(20),

    // The 'DELIVERED_NOTIFICATION_LOG' entity type.
    DELIVERED_NOTIFICATION_LOG(21),

    // CYCLIC notification
    CYCLIC_NOTIFICATION(22),

    // The 'CYCLIC_NOTIFICATION_DETAILS' entity type.
    CYCLIC_NOTIFICATION_DETAILS(23),

    // The 'CyclicNotifictationLinking' entity type.
    CYCLIC_NOTIFICATION_LINKING(24),

    // The 'CYCLIC_NOTIFICATION_SCHEDULER_RECORD' entity type.
    CYCLIC_NOTIFICATION_SCHEDULER_RECORD(25),

    // The 'NOTIFICATION_ATTACHMENT' entity type.
    NOTIFICATION_ATTACHMENT(26),

    // Import history entity type.
    IMPORT_HISTORY(27);

    private final int id;

    PFEntityType(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }

    @Override
    public String toString() {
        switch (this) {
            case ALL:
                return "ALL";
            case TENANT:
                return "TENANT";
            case TENANT_USER:
                return "TENANT_USER";
            default:
                return "NONE";
        }
    }
}
