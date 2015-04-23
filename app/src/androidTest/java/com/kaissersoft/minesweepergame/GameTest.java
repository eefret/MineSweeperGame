package com.kaissersoft.minesweepergame;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.test.ActivityInstrumentationTestCase2;

import com.kaissersoft.minesweepergame.activities.GameActivity;
import com.kaissersoft.minesweepergame.fragments.MineField;
import com.kaissersoft.minesweepergame.logic.Game;
import com.kaissersoft.minesweepergame.pojos.Tile;
import com.kaissersoft.minesweepergame.util.Loggable;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Random;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by eefret on 22/04/15.
 */
public class GameTest extends ActivityInstrumentationTestCase2<GameActivity> implements Loggable{

    public GameTest() {
        super(GameActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Intent i = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(GameActivity.DIFFICULTY, Game.Difficulty.EASY);
        i.putExtras(bundle);
        setActivityIntent(i);
        getActivity();
    }

    public void testLoseGame(){
        Random rand = new Random();
        while (getActivity().getActiveGame().getStatus() != Game.GameStatus.LOST) {
            int position = rand.nextInt(63);
            wait(500);
            onData(allOf(instanceOf(Tile.class)))
                    .inAdapterView(withId(R.id.f_minefield_gridview))
                    .atPosition(position)
                    .perform(click());
        }

        onView(withText(R.string.lost_dialog_title))
                .check(matches(isDisplayed()));

    }

    public void testWinGame(){
        Game game = getActivity().getActiveGame();
        game.cheat();
        wait(250);
        onView(withId(R.id.done))
                .check(matches(isDisplayed()))
                .perform(click());
        wait(250);
        onView(withText(R.string.OK))
                .check(matches(isDisplayed()))
                .perform(click());

    }

    private void wait(int milis){
        try {
            Thread.sleep(milis);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Matcher<Tile> isMineMatcher(final boolean flag){
        return new TypeSafeMatcher<Tile>() {
            @Override
            public boolean matchesSafely(Tile tile) {
                return tile.isMine() == flag;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("expected "+ flag);
            }
        };
    }
}
