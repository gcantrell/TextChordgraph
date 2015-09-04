/*
 * Copyright (c) 2015. Greg Cantrell, All rights reserved.
 */

package com.examples.gregcantrell.textchordgraph;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

/**
 * The Main Activity of this app. It contains the Chordgraph Fragment.
 */
public class MainActivity extends FragmentActivity {

    /**
     * Creates the MainActivity layout from the activity_main layout file and places the
     * ChorGraphFragment in the layout.
     * @param savedInstanceState - the saved state Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm  = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_area, new ChordGraphFragment());
        fragmentTransaction.commit();
    }

    /**
     * We don't currently use the menu, but we may in the future
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * We con't currently use the menu, but we may in the future.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
