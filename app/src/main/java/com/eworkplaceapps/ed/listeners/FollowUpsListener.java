//===============================================================================
// (c) 2015 eWorkplace Apps.  All rights reserved.
// Original Author: Shrey Sharma
// Original Date: 6 July 2015
//===============================================================================
package com.eworkplaceapps.ed.listeners;

import com.eworkplaceapps.employeedirectory.employee.EmployeeQuickView;

/**
 * implement this listener on fragments to get notified whenever any employee gets followed up
 */
public interface FollowUpsListener {

    /**
     * calls whenever user clicks on follow ups click
     *
     * @param employeeQuickView object
     */
    public void onFollowUpClick(EmployeeQuickView employeeQuickView);
}
