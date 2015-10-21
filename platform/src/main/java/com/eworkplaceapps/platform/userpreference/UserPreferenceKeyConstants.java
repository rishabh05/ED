//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/21/2015
//===============================================================================
package com.eworkplaceapps.platform.userpreference;

/**
 * Class contains all user preference key string values as constant.
 * These constant key is required by client application to access any user preference value.
 * These constant values will be stored in PreferenceName1 and PreferenceName2 column of USER_PREFERENCE table.
 */
public interface UserPreferenceKeyConstants {
    //Key constant for notification user preference name 1.
    String NOTIFICATION_PREFERENCE_KEY = "NotificationPreferences";

    //Key constant for notification delivery type name 2.
    String NOTIFICATION_DELIVERY_TYPE_KEY = "DeliveryType";

    /// Key constant for notification delivery name 2.
    String NOTIFICATION_DELIVERY_KEY = "NotificationDelivery";

    /// Key constant for notification delivery time name2.
    String DAILY_NOTIFICATION_DELIVERY_TIME_KEY = "DailyNotificationDeliveryTime";

    /// Default time for daily (summary or detail) notification.
    String DEFAULT_DAILY_NOTIFICATION_DELIVERY_TIME = "12:00 am";

    /// Key constant for user's notification email preference.
    String NOTIFICATION_EMAIL_KEY = "NotificationEmail";

    /// Key constant for user's notification SMS email preference.
    String SMS_EMAIL_KEY = "SMSEmail";

    /// USER's 'Home Time Zone'.
    String TIME_ZONE_KEY = "HomeTimeZone";

    /// The Default time zone enum value.
    int DEFAULT_TIME_ZONE_ENUM_VALUE = IANATimeZone.IANATimeZoneEnum.US_PACIFIC.getId();

}
