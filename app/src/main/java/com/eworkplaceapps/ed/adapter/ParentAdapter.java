package com.eworkplaceapps.ed.adapter;

import android.widget.BaseAdapter;

import com.eworkplaceapps.employeedirectory.employee.EmployeeQuickView;

/**
 * Created by Shrey on 6/26/2015.
 */
public abstract class ParentAdapter extends BaseAdapter {

    public static abstract class Row {
    }

    public static final class Section extends Row {
        public final String text;

        public Section(String text) {
            this.text = text;
        }
    }

    public static final class Item extends Row {
        public final EmployeeQuickView employeeQuickView;

        public Item(EmployeeQuickView employeeQuickView) {
            this.employeeQuickView = employeeQuickView;
        }
    }
}
