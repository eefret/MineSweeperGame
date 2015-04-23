package com.kaissersoft.minesweepergame.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.kaissersoft.minesweepergame.R;
import com.kaissersoft.minesweepergame.util.Loggable;

/**
 * Created by eefret on 21/04/15.
 */
public abstract class Base extends ActionBarActivity implements Loggable{

    //===================FIELDS====================
    protected Toolbar mToolBar;
    protected FragmentManager mFragmentManager;

    //===================OVERRIDEN METHODS====================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        if(needToolbar()){
            log.d("0");
            mToolBar = (Toolbar) findViewById(R.id.toolbar);
            if( mToolBar != null){
                setSupportActionBar(mToolBar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
        }

        if (mFragmentManager == null){
            mFragmentManager = getSupportFragmentManager();
        }
    }

    //===================METHODS====================
    protected abstract boolean needToolbar();
    protected abstract int getLayoutResource();
    protected void setActionBarIcon(int iconRes){
        mToolBar.setNavigationIcon(iconRes);
    }
}
