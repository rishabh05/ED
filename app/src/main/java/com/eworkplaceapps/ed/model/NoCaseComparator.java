//===============================================================================
// (c) 2015 eWorkplace Apps.  All rights reserved.
// Original Author: Dheeraj Nagar
// Original Date: 22 June 2015
//===============================================================================
package com.eworkplaceapps.ed.model;

import com.eworkplaceapps.employeedirectory.employee.EmployeeQuickView;

import java.util.Comparator;

/**
 * comparator for employee quick view
 */
public class NoCaseComparator implements Comparator<EmployeeQuickView> {
    public int compare(EmployeeQuickView s1, EmployeeQuickView s2) {
        return s1.getFirstName().compareToIgnoreCase(s2.getFirstName());
    }
}
