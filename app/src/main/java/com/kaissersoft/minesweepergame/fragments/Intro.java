package com.kaissersoft.minesweepergame.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kaissersoft.minesweepergame.R;
import com.kaissersoft.minesweepergame.util.Loggable;

/**
 * Created by eefret on 21/04/15.
 */
public class Intro extends Fragment
        implements Loggable, View.OnClickListener{

    //===================FIELDS====================
    Button mBtnNewGame;
    Button mBtnContinueGame;
    Button mBtnHighScore;
    Button mBtnExit;

    IntroButtonListeners mListeners;
    Context mContext;

    public static final String TAG = "INTRO_TAG";
    //===================OVERRIDEN METHODS====================
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_intro,container,false);
        initialize(v);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mContext = activity;
            mListeners = (IntroButtonListeners) activity;
        }catch (ClassCastException e){
            log.wtf("Your Activity is not implementing IntroButtonListeners");
            throw new ClassCastException(activity.toString());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_new_game:
                mListeners.onNewGamePressed();
                break;
//            case R.id.btn_continue_game:
//                mListeners.onContinueGamePressed();
//                break;
//            case R.id.btn_high_scores:
//                mListeners.onHighScorePressed();
//                break;
            case R.id.btn_exit:
                mListeners.onExitPressed();
                break;
        }
    }

    //===================METHODS====================

    /**
     * Method that initialize all widgets inside the Layout
     * @param v View
     */
    private void initialize(View v){
        //Buttons
        mBtnNewGame = (Button) v.findViewById(R.id.btn_new_game);
//        mBtnContinueGame = (Button) v.findViewById(R.id.btn_continue_game);
//        mBtnHighScore = (Button) v.findViewById(R.id.btn_high_scores);
        mBtnExit = (Button) v.findViewById(R.id.btn_exit);

        mBtnNewGame.setOnClickListener(this);
        //mBtnContinueGame.setOnClickListener(this);
        //mBtnHighScore.setOnClickListener(this);
        mBtnExit.setOnClickListener(this);
    }

    //===================INNER CLASSES====================
    public interface IntroButtonListeners{
        public void onNewGamePressed();
        public void onContinueGamePressed();
        public void onHighScorePressed();
        public void onExitPressed();
    }
}
