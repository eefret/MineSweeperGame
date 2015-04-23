package com.kaissersoft.minesweepergame.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaissersoft.minesweepergame.R;

/**
 * Created by eefret on 22/04/15.
 */
public class ScoreScreen extends Fragment {

    //===================FIELDS====================
    private TextView tvFlagCount;
    private ImageView ivFace;
    private TextView tvScore;
    private Context mContext;
    public static final String TAG = "ScoreScreen_tag";
    public static final String SCORE_BUNDLE = "SCORE_BUNDLE";
    public static final String FLAG_COUNT_BUNDLE = "FLAG_COUNT_BUNDLE";

    //===================CONSTRUCTORS===================
    public static ScoreScreen newInstance(){
        return new ScoreScreen();
    }

    //===================OVERRIDEN METHODS===================
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mContext = getActivity();
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_scorescreen, container, false);
        initialize(view);
        Bundle args = getArguments();
        setScore(args.getInt(SCORE_BUNDLE));
        setFlagCount(args.getInt(FLAG_COUNT_BUNDLE));
        return view;
    }

    //===================METHODS===================

    /**
     * Method that initialize all widgets inside the Layout
     * @param v View
     */
    public void initialize(View v){
        tvFlagCount = (TextView) v.findViewById(R.id.f_scorescreen_flag_count);
        tvScore = (TextView) v.findViewById(R.id.f_scorescreen_score_count);
        ivFace = (ImageView) v.findViewById(R.id.f_scorescreen_face);
    }

    /**
     * set the score
     * @param score
     */
    public void setScore(int score){
        tvScore.setText(score+"");
    }

    /**
     * set the flag count
     * @param count
     */
    public void setFlagCount(int count){
        tvFlagCount.setText(count+"");
    }

    /**
     * set the facetype
     * @param type
     */
    public void setFaceType(FaceType type){
        ivFace.setImageResource(type.getDrawableId());
    }

    //===================INNER CLASSES/ INTERFACES/ ENUMS====================

    /**
     * enum that holds all different kinds of faces for our smiley
     */
    public enum FaceType{
        FLAGGED(R.drawable.face_flagged),
        GUESSED(R.drawable.face_guessing),
        NORMAL(R.drawable.face_normal),
        WON(R.drawable.face_won),
        LOST(R.drawable.face_lost),
        FLIPPING(R.drawable.face_scared);

        int drawableId;
        FaceType(int drawable){
            this.drawableId = drawable;
        }
        public int getDrawableId() {
            return drawableId;
        }
    }
}
