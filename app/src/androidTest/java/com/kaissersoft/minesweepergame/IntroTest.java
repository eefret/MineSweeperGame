package com.kaissersoft.minesweepergame;

import android.test.ActivityInstrumentationTestCase2;

import com.kaissersoft.minesweepergame.activities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by eefret on 22/04/15.
 */
public class IntroTest extends ActivityInstrumentationTestCase2<MainActivity> {

    //===================Constructors====================
    public IntroTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testStartNewGame(){
        onView(withId(R.id.btn_new_game)).perform(click());
    }

    public void testExit(){
        onView(withId(R.id.btn_exit)).perform(click());
    }
}
