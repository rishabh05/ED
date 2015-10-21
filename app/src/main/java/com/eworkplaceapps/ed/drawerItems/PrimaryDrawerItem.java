package com.eworkplaceapps.ed.drawerItems;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PrimaryDrawerItem extends com.mikepenz.materialdrawer.model.PrimaryDrawerItem {

    private int backgroundRes = -1;
    private int backgroundColor = 0;


    public PrimaryDrawerItem withBackgroundRes(int backgroundRes) {
        this.backgroundRes = backgroundRes;
        return this;
    }

    public PrimaryDrawerItem withBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    @Override
    public View convertView(LayoutInflater inflater, View convertView, ViewGroup parent) {
        //use the logic of our PrimaryDrawerItem
        convertView = super.convertView(inflater, convertView, parent);

        if (backgroundColor != 0) {
            convertView.setBackgroundColor(backgroundColor);
        } else if (backgroundRes != -1) {
            convertView.setBackgroundResource(backgroundRes);
        }

        return convertView;
    }
}
