//===============================================================================
// (c) 2015 eWorkplace Apps.  All rights reserved.
// Original Author: Dheeraj Nagar
// Original Date: 12 June 2015
//===============================================================================
package com.eworkplaceapps.ed.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.eworkplaceapps.ed.fragment.AllFragment;

/**
 * Adapter for handling fragment for diff. Tabs
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private CharSequence titles[];
    private int numbOfTabs;
    private Context context;
    private int[] imageResId;

    /**
     * Build a Constructor and assign the passed Values to appropriate values in the class
     *
     * @param ctx
     * @param fm
     * @param titles
     * @param numbOfTabs
     * @param imageResId
     */
    public ViewPagerAdapter(Context ctx, FragmentManager fm, CharSequence titles[], int numbOfTabs, int[] imageResId) {
        super(fm);
        this.titles = titles;
        this.numbOfTabs = numbOfTabs;
        this.context = ctx;
        this.imageResId = imageResId;
    }

    public int getImageResourceId(int position) {
        return imageResId[position];
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                AllFragment all = new AllFragment();
                return all;

            default:
                return new AllFragment();
        }
    }


    /**
     * This method return the titles for the Tabs in the Tab Strip
     *
     * @param position
     * @return CharSequence
     */
    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = context.getResources().getDrawable(imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BASELINE);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }


    /**
     * This method return the Number of tabs for the tabs Strip
     *
     * @return int
     */
    @Override
    public int getCount() {
        return numbOfTabs;
    }
}