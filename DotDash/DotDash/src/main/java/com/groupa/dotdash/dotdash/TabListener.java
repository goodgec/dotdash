package com.groupa.dotdash.dotdash;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.util.Log;

/**
 * Created by himelica on 5/29/14.
 */
public class TabListener implements ActionBar.TabListener {

    private Fragment fragment;
    private final Activity activity;
    private final String tag;
    private final int tabNumber;

    public TabListener(Fragment fragment, Activity activity, String tag, int tabNumber) {
        this.fragment = fragment;
        this.activity = activity;
        this.tag = tag;
        this.tabNumber = tabNumber;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // Check if the fragment is already initialized
//        if (fragment == null) {
//            // If not, instantiate and add it to the activity
//            fragment = Fragment.instantiate(activity, cl.getName());
//            fragmentTransaction.add(android.R.id.content, fragment, tag);
//        } else {
            // If it exists, simply attach it in order to show it
        activity.setTitle(tag);
        ((DotDash)activity).setTabNumber(tabNumber);
        fragmentTransaction.add(android.R.id.content, fragment, tag);
        fragmentTransaction.attach(fragment);
//        }
        // update title text
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        if (fragmentTransaction != null) {
            // Detach the fragment, because another one is being attached
            fragmentTransaction.detach(fragment);
        }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // this should be empty
    }
}

