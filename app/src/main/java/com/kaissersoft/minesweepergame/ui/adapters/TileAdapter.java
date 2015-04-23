package com.kaissersoft.minesweepergame.ui.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.kaissersoft.minesweepergame.R;
import com.kaissersoft.minesweepergame.pojos.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eefret on 21/04/15.
 */
public class TileAdapter extends ArrayAdapter<Tile>{
    //===================FIELDS====================

    private final Activity mContext;
    private final ArrayList<Tile> mTiles;
    //===================CONSTRUCTORS====================

    public TileAdapter(Activity context, List<Tile> tiles) {
        super(context, R.layout.tile, tiles);
        mContext = context;
        mTiles = new ArrayList<>(tiles);
    }
    //===================OVERRIDEN METHODS====================
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null){
            LayoutInflater inflater = mContext.getLayoutInflater();
            view = inflater.inflate(R.layout.tile, parent, false);

            TileHolder holder = new TileHolder();
            holder.btn = (Button) view.findViewById(R.id.list_item);
            view.setTag(holder);
        }

        TileHolder holder = (TileHolder) view.getTag();
        Tile tile = mTiles.get(position);

        switch (tile.getStatus()){
            case FLAG:
                holder.btn.setBackgroundResource(R.drawable.filled_tile_flag);
                break;
            case UNCOVERED:
                if (tile.isMine()){
                    holder.btn.setBackgroundResource(R.drawable.empty_tile_bomb);
                }else{
                    holder.btn.setBackgroundResource(R.drawable.empty_tile);
                    holder.btn.setText("");
                    if (tile.getAdjacentMinesCount() > 0){
                        holder.btn.setText("" + tile.getAdjacentMinesCount());
                        holder.btn.setTextColor(tile.getColor());
                    }
                }
                break;
            case COVERED:
                holder.btn.setBackgroundResource(R.drawable.filled_tile);
                break;
            case GUESS:
                holder.btn.setBackgroundResource(R.drawable.filled_tile_guess);
                break;
        }
        return view;
    }

    //===================INNER CLASSES/ LISTENERS/ ENUMS====================
    static class TileHolder{
        Button btn;
    }
}
