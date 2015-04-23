package com.kaissersoft.minesweepergame.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.kaissersoft.minesweepergame.R;
import com.kaissersoft.minesweepergame.logic.Game;
import com.kaissersoft.minesweepergame.pojos.Tile;
import com.kaissersoft.minesweepergame.ui.adapters.TileAdapter;
import com.kaissersoft.minesweepergame.util.Loggable;

import java.util.List;

/**
 * Created by eefret on 22/04/15.
 */
public class MineField extends Fragment
        implements Loggable{

    //===================FIELDS====================
    private GridView gvMineField;
    private TileAdapter mAdapter;
    private Context mContext;
    private MineFieldListeners mListeners;
    public static final String TAG = "MineField_tag";

    //===================CONSTRUCTORS====================

    public static MineField newInstance(){
        return new MineField();
    }

    //===================OVERRIDEN METHODS====================


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mContext = getActivity();
            mListeners = (MineFieldListeners) getActivity();
        }catch (ClassCastException e){
            log.wtf("Your Activity is not implementing MineFieldListeners");
            throw new ClassCastException(activity.toString());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_minefield, container, false);
        initialize(view);
        log.d("0");

        List<Tile> tiles = mListeners.getActiveGame().getTiles();

        mAdapter = new TileAdapter(getActivity(), tiles);
        gvMineField.setAdapter(mAdapter);
//        gvMineField.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                mListeners.onMineFieldTouched(event);
//                log.d("0");
//                return true;
//            }
//        });
        gvMineField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListeners.onTileClicked(view, position, id);
                log.d("0");
            }
        });

        gvMineField.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mListeners.onTileLongPressed(view, position, id);
                log.d("0");
                return false;
            }
        });
        return view;
    }


    //===================METHODS====================

    /**
     * Method that initialize all widgets inside the Layout
     * @param v View
     */
    public void initialize(View v){
        gvMineField = (GridView) v.findViewById(R.id.f_minefield_gridview);
    }

    public TileAdapter getAdapter() {
        return mAdapter;
    }

    public GridView getMineField(){
        return gvMineField;
    }

    //===================INNER CLASSES/ INTERFACES/ ENUMS====================

    public interface MineFieldListeners{
        void onTileClicked(View tileView, int position, long tileId);
        void onTileLongPressed(View tileView, int position, long tileId);
        void onMineFieldTouched(MotionEvent event);
        Game getActiveGame();
    }

}
