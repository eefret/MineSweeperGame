package com.kaissersoft.minesweepergame.pojos;
import com.kaissersoft.minesweepergame.R;

/**
 * Created by eefret on 21/04/15.
 */
public class Tile {

    //===================FIELDS====================
    private boolean isMine;
    private int adjacentMinesCount;
    private Status mStatus;
    private int[] colors ={     R.color.tile_1, R.color.tile_2, R.color.tile_3,
                                R.color.tile_4, R.color.tile_5, R.color.tile_6,
                                R.color.tile_7, R.color.tile_8};

    //===================OVERRIDEN METHODS====================
    public Tile(){
        initialize();
    }
    //===================METHODS====================

    public void initialize(){
        isMine = false;
        adjacentMinesCount = 0;
        mStatus = Status.COVERED;
    }

    public int getColor(){
        if(adjacentMinesCount >= 1){
            return colors[adjacentMinesCount-1];
        }
        return 0;
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status) {
        this.mStatus = status;
    }

    public int getAdjacentMinesCount() {
        return adjacentMinesCount;
    }

    public void increaseAdjacentMinesCount(){
        if(!this.isMine){
            adjacentMinesCount += 1;
        }
    }

    public boolean isMine(){
        return isMine;
    }

    public void setMine(boolean isMine){
        this.isMine = true;
    }

    //===================INNER CLASSES/ LISTENERS/ ENUMS====================
    public enum Status{
        COVERED, UNCOVERED, FLAG, GUESS
    }
}
