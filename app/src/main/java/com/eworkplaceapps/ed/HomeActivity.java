//===============================================================================
// (c) 2015 eWorkplace Apps.  All rights reserved.
// Original Author: Dheeraj Nagar
// Original Date: 11 May 2015
//===============================================================================
package com.eworkplaceapps.ed;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.eworkplaceapps.ed.adapter.ViewPagerAdapter;
import com.eworkplaceapps.ed.fragment.AllFragment;
import com.eworkplaceapps.ed.listeners.FragmentBackPressedListener;
import com.eworkplaceapps.ed.slidingtabs.SlidingTabLayout;
import com.eworkplaceapps.ed.utils.Utils;
import com.eworkplaceapps.ed.view.NonSwipingViewPager;
import com.eworkplaceapps.platform.context.ContextHelper;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.ToggleDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.model.interfaces.OnCheckedChangeListener;
import com.mikepenz.octicons_typeface_library.Octicons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * base activity for all fragments, tabs and sliding menu
 */
public class HomeActivity extends AppCompatActivity implements Animation.AnimationListener {
    private static final int PROFILE_SETTING = 1;
    private AccountHeader.Result headerResult = null;
    private Drawer.Result result = null;
    private SlidingTabLayout tabs;
    private CharSequence Titles[] = {"All", "Favorites", "My Teams", "Status", "More"};
    private int numOfTabs = 1;
    private NonSwipingViewPager pager;
    private ViewPagerAdapter adapter;
    private Animation animFadeIn, animFadeOut;
    private Toolbar toolbar;

    /**
     * method for initialize the tabs and sliding menu
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dark_toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Utils.actionBarTitle("All Employees"));
        animInit();
        int[] imageResId = {
                R.drawable.all_white,
        };
        adapter = new ViewPagerAdapter(this, getSupportFragmentManager(), Titles, numOfTabs, imageResId);
        pager = (NonSwipingViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(4);
        pager.setAdapter(adapter);
        tabs.setCustomTabView(R.layout.custom_tab_title, R.id.tabtext);
        tabs.setDistributeEvenly(true);
        tabs.setViewPager(pager);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.orange);
            }
        });
        final IProfile profile = new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon("https://avatars3.githubusercontent.com/u/1476232?v=3&s=460");
        final IProfile profile2 = new ProfileDrawerItem().withName("Bernat Borras").withEmail("alorma@github.com").withIcon(Uri.parse("https://avatars3.githubusercontent.com/u/887462?v=3&s=460"));
        final IProfile profile3 = new ProfileDrawerItem().withName("Max Muster").withEmail("max.mustermann@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile2));
        final IProfile profile4 = new ProfileDrawerItem().withName("Felix House").withEmail("felix.house@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile3));
        final IProfile profile5 = new ProfileDrawerItem().withName("Mr. X").withEmail("mister.x.super@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile4)).withIdentifier(4);
        final IProfile profile6 = new ProfileDrawerItem().withName("Batman").withEmail("batman@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile5));

        headerResult = new AccountHeader()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        profile,
                        profile2,
                        profile3,
                        profile4,
                        profile5,
                        profile6,
                        //don't ask but google uses 14dp for the add account icon in gmail but 20dp for the normal icons (like manage account)
                        new ProfileSettingDrawerItem().withName("Add Account").withDescription("Add new GitHub Account").withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_add).actionBarSize().paddingDp(5).colorRes(R.color.material_drawer_primary_text)).withIdentifier(PROFILE_SETTING),
                        new ProfileSettingDrawerItem().withName("Manage Account").withIcon(GoogleMaterial.Icon.gmd_settings)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        //sample usage of the onProfileChanged listener
                        //if the clicked item has the identifier 1 add a new profile ;)
                        if (profile instanceof IDrawerItem && ((IDrawerItem) profile).getIdentifier() == PROFILE_SETTING) {
                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Batman").withEmail("batman@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile5));
                            if (headerResult.getProfiles() != null) {
                                //we know that there are 2 setting elements. set the new profile above them ;)
                                headerResult.addProfile(newProfile, headerResult.getProfiles().size() - 2);
                            } else {
                                headerResult.addProfiles(newProfile);
                            }
                        }

                        //false if you have not consumed the event and it should close the drawer
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        //Create the drawer
        result = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Copy Database to SD Card").withIcon(GoogleMaterial.Icon.gmd_wb_sunny).withIdentifier(1).withCheckable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_action_bar).withIcon(FontAwesome.Icon.faw_home).withIdentifier(2).withCheckable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_multi_drawer).withIcon(FontAwesome.Icon.faw_gamepad).withIdentifier(3).withCheckable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_non_translucent_status_drawer).withIcon(FontAwesome.Icon.faw_eye).withIdentifier(4).withCheckable(false),
                        new PrimaryDrawerItem().withDescription("A more complex sample").withName(R.string.drawer_item_complex_header_drawer).withIcon(GoogleMaterial.Icon.gmd_adb).withIdentifier(5).withCheckable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_simple_fragment_drawer).withIcon(GoogleMaterial.Icon.gmd_style).withIdentifier(6).withCheckable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_embedded_drawer_dualpane).withIcon(GoogleMaterial.Icon.gmd_battery_charging_30).withIdentifier(7).withCheckable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_fullscreen_drawer).withIcon(GoogleMaterial.Icon.gmd_style).withIdentifier(8).withCheckable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_custom_container_drawer).withIcon(GoogleMaterial.Icon.gmd_my_location).withIdentifier(9).withCheckable(false),
                        new SectionDrawerItem().withName(R.string.drawer_item_section_header),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github).withIdentifier(20).withCheckable(false),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(GoogleMaterial.Icon.gmd_format_color_fill).withIdentifier(10).withTag("Bullhorn"),
                        new DividerDrawerItem(),
                        new SwitchDrawerItem().withName("Switch").withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener),
                        new SwitchDrawerItem().withName("Switch2").withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener),
                        new ToggleDrawerItem().withName("Toggle").withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener)
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the header
                        //--> click on the footer
                        //those items don't contain a drawerItem

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
//                                //action for copy DB to sd card
                                copyDBToSDCard();
                            } else if (drawerItem.getIdentifier() == 2) {
//                                intent = new Intent(SimpleHeaderDrawerActivity.this, ActionBarDrawerActivity.class);
                            } else if (drawerItem.getIdentifier() == 3) {
//                                intent = new Intent(SimpleHeaderDrawerActivity.this, MultiDrawerActivity.class);
                            } else if (drawerItem.getIdentifier() == 4) {
//                                intent = new Intent(SimpleHeaderDrawerActivity.this, SimpleNonTranslucentDrawerActivity.class);
                            } else if (drawerItem.getIdentifier() == 5) {
//                                intent = new Intent(SimpleHeaderDrawerActivity.this, ComplexHeaderDrawerActivity.class);
                            } else if (drawerItem.getIdentifier() == 6) {
//                                intent = new Intent(SimpleHeaderDrawerActivity.this, SimpleFragmentDrawerActivity.class);
                            } else if (drawerItem.getIdentifier() == 7) {
//                                intent = new Intent(SimpleHeaderDrawerActivity.this, EmbeddedDrawerActivity.class);
                            } else if (drawerItem.getIdentifier() == 8) {
//                                intent = new Intent(SimpleHeaderDrawerActivity.this, FullscreenDrawerActivity.class);
                            } else if (drawerItem.getIdentifier() == 9) {
//                                intent = new Intent(SimpleHeaderDrawerActivity.this, CustomContainerActivity.class);
                            } else if (drawerItem.getIdentifier() == 20) {
                                intent = new Libs.Builder()
                                        .withFields(R.string.class.getFields())
                                        .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                                        .intent(HomeActivity.this);
                            }
                            if (intent != null) {
                                HomeActivity.this.startActivity(intent);
                            }
                        }
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
        if (savedInstanceState == null) {
            result.setSelectionByIdentifier(10, false);
            headerResult.setActiveProfile(profile3);
        }
    }

    private void animInit() {
        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        animFadeIn.setAnimationListener(this);
        animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);
        animFadeOut.setAnimationListener(this);
    }

    public void changeMyActionBarTitle(SpannableString title) {
        getSupportActionBar().setTitle(title);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int whiteSpace = 0;
        if (AllFragment.relativePanel != null) {
            whiteSpace = AllFragment.relativePanel.getHeight();
        }
        float touchArea = getResources().getDisplayMetrics().heightPixels - whiteSpace;
        if (Utils.isDataNotLoaded && (ev.getRawY() >= touchArea && Utils.FRAGMENT_POSITION == 3)) {
            return true;
        }
        // condition to lock the screen at the time of refreshing
        if ((Utils.moreFragmentLoading != null && Utils.moreFragmentLoading.isShown()) && (ev.getRawY() >= touchArea && Utils.FRAGMENT_POSITION == 4)) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    public void showMyActionBar() {
        getSupportActionBar().show();
    }

    /**
     * custom method for show Tabs after returning from search widget screen
     */
    public void showMyTabs() {
        tabs.setVisibility(View.VISIBLE);
        //     tabs.startAnimation(animFadeIn);
    }

    public void hideTabs() {
        this.tabs.setVisibility(View.GONE);
        //   this.tabs.startAnimation(animFadeOut);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
            if (drawerItem instanceof Nameable) {
                Log.i("material-drawer", "DrawerItem: " + ((Nameable) drawerItem).getName() + " - toggleChecked: " + isChecked);
            } else {
                Log.i("material-drawer", "toggleChecked: " + isChecked);
            }
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = result.saveInstanceState(outState);
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else if (adapter != null) {
            Fragment fragment = (Fragment) adapter.instantiateItem(pager, Utils.FRAGMENT_POSITION);
            if (fragment instanceof FragmentBackPressedListener) {
                ((FragmentBackPressedListener) fragment).onFragmentBackPressed();
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }


    private void copyDBToSDCard() {
        try {
            File dbFile = ContextHelper.getContext().getDatabasePath("ewAppsData.db");
            if (dbFile.exists()) {
                String sdCardFolderPath = Environment.getExternalStorageDirectory() + File.separator + "ConnectDB";
                File f1 = new File(sdCardFolderPath);
                if (!f1.exists())
                    f1.mkdir();
                String sdCardDBFilePath = sdCardFolderPath + File.separator + "ewAppsData.db";
                File f2 = new File(sdCardDBFilePath);
                if (f2.exists()) {
                    //delete previous file
                    f2.delete();
                }
                FileInputStream fis = new FileInputStream(dbFile);
                FileOutputStream fos = new FileOutputStream(f2);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
                // Close the streams
                fos.flush();
                fos.close();
                fis.close();
                if (new File(sdCardDBFilePath).exists()) {
                    Toast.makeText(this, "database copied To sd card", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "error occurred copying database to sd card", Toast.LENGTH_LONG).show();
                }
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "error occurred copying database to sd card " + e, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "error occurred copying database to sd card " + e, Toast.LENGTH_LONG).show();
        }
    }

}
