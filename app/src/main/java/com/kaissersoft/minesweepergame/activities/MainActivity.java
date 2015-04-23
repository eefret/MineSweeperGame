package com.kaissersoft.minesweepergame.activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.kaissersoft.minesweepergame.R;
import com.kaissersoft.minesweepergame.fragments.Intro;
import com.kaissersoft.minesweepergame.logic.Game;
import com.kaissersoft.minesweepergame.util.Loggable;


public class MainActivity extends Base
        implements Loggable, Intro.IntroButtonListeners{

    //===================FIELDS====================

    //===================OVERRIDEN METHODS====================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarIcon(R.drawable.ic_action_ic_main);
        initialize();
    }

    @Override
    protected boolean needToolbar() {
        return true;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }


    @Override
    public void onNewGamePressed() {
        startGame(Game.Difficulty.EASY);
    }

    private void startGame(Game.Difficulty diff){
        Intent i = new Intent(this, GameActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(GameActivity.DIFFICULTY, diff);
        i.putExtras(bundle);
        startActivity(i);
    }

    @Override
    public void onContinueGamePressed() {
        log.d("0");
    }

    @Override
    public void onHighScorePressed() {
        log.d("0");
    }

    @Override
    public void onExitPressed() {
        log.d("0");
        finish();
    }

    //===================METHODS====================

    /**
     * Initialize all widgets
     */
    private void initialize(){
        showMenuFragment();
    }

    /**
     * Show the menu fragment
     */
    private void showMenuFragment(){
        FragmentTransaction fT = getFragmentManager().beginTransaction();
        fT.addToBackStack(Intro.TAG);
        fT.replace(R.id.a_main_fragment_container, new Intro(), Intro.TAG);
        fT.commit();
    }
}
