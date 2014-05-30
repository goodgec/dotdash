package com.groupa.dotdash.dotdash;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

/**
 * Created by himelica on 5/29/14.
 */
public class TabListener<T extends Fragment> implements ActionBar.TabListener {

    private Fragment fragment;
    private final Activity activity;
    private final String tag;
    private final Class<T> cl;

    public TabListener(Activity activity, String tag, Class<T> cl) {
        this.activity = activity;
        this.tag = tag;
        this.cl = cl;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // Check if the fragment is already initialized
        if (fragment == null) {
            // If not, instantiate and add it to the activity
            fragment = Fragment.instantiate(activity, cl.getName());
            fragmentTransaction.add(android.R.id.content, fragment, tag);
        } else {
            // If it exists, simply attach it in order to show it
            fragmentTransaction.attach(fragment);
        }
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

    }
}

