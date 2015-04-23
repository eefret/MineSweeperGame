package com.kaissersoft.minesweepergame.logic;

import android.os.Parcel;
import android.os.Parcelable;

import com.kaissersoft.minesweepergame.pojos.Tile;
import com.kaissersoft.minesweepergame.util.Loggable;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by eefret on 21/04/15.
 */
public class Game implements Loggable{
    //===================FIELDS====================
    private Difficulty mDifficulty;
    private GameStatus mStatus;
    private final List<Tile> mTiles;
    private int mFlagCount;
    private int mListCap;

    //===================CONSTRUCTORS====================
    public Game(){
        this(Difficulty.EASY);
    }

    public Game( Difficulty diff){
        mDifficulty = diff;
        mStatus = GameStatus.PLAYING;
        mListCap = diff.getRows() * diff.getCols();
        mTiles = new ArrayList<Tile>(mListCap);
        mFlagCount = diff.getMines();
        //Setting all tiles blanks
        for(int i = 0; i < mListCap; i++){
            mTiles.add(new Tile());
        }
        plantMines();
    }

    //===================METHODS====================

    /**
     * Method used to Win the Game Automatically, not fair isnt it ?
     */
    public void cheat(){
        for (Tile tile: mTiles){
            if(tile.isMine()){
                tile.setStatus(Tile.Status.FLAG);
            }
            tile.setStatus(Tile.Status.UNCOVERED);
        }
        mStatus = GameStatus.WIN;
    }

    /**
     * Method to plant mines in different positions
     */
    public void plantMines(){
        Random rand = new Random();
        //Used set so we dont get duplicates
        Set<Integer> mineCoords = new LinkedHashSet<>(mDifficulty.getMines());
        //First we randomly select all coordenates
        while (mineCoords.size() < mDifficulty.getMines()){
            Integer coord = rand.nextInt(mListCap) + 1;
            mineCoords.add(coord);
        }
        //Now we can set the mines accordingly
        for (Integer coord: mineCoords){
            mTiles.get(coord-1).setMine(true);
            log.d(coord+"");
            for (Integer adjacent: getAdjacentIndexes(coord-1)){
                mTiles.get(adjacent).increaseAdjacentMinesCount();
            }
        }
    }

    /**
     * Method that returns indexes of all neighbors
     * @param coord int
     * @return ArrayList<Integer>
     */
    public ArrayList<Integer> getAdjacentIndexes(int coord){
        ArrayList<Integer> ints = new ArrayList<>();
        try{
            if (mTiles.get(coord-mDifficulty.getRows()-1)!= null) ints.add(coord-mDifficulty.getRows()-1);
            if (mTiles.get(coord-mDifficulty.getRows())!= null) ints.add(coord-mDifficulty.getRows());
            if (mTiles.get(coord-mDifficulty.getRows()+1)!= null) ints.add(coord-mDifficulty.getRows()+1);
            if (mTiles.get(coord-1)!= null) ints.add(coord-1);
            if (mTiles.get(coord+1)!= null) ints.add(coord+1);
            if (mTiles.get(coord+mDifficulty.getRows()-1)!= null) ints.add(coord+mDifficulty.getRows()-1);
            if (mTiles.get(coord+mDifficulty.getRows())!= null) ints.add(coord+mDifficulty.getRows());
            if (mTiles.get(coord+mDifficulty.getRows()+1)!= null) ints.add(coord+mDifficulty.getRows()+1);
        }catch (IndexOutOfBoundsException e) {
            log.e("surpassed index" + coord );
        }
        return ints;
    }

    /**
     * Method that flips tiles recursively
     * @param coords int
     */
    public void flipTile(int coords){
        if(coords < 0 || coords > mListCap || mTiles.get(coords) == null){
            return;
        }

        Tile tile = mTiles.get(coords);
        if (tile.getStatus() != Tile.Status.COVERED && tile.getStatus() != Tile.Status.GUESS){
            return;
        }

        tile.setStatus(Tile.Status.UNCOVERED);
        log.d(tile.getAdjacentMinesCount()+"");
        if(!tile.isMine() && tile.getAdjacentMinesCount() == 0){
            log.d("0");
            for (Integer i: getAdjacentIndexes(coords)){
                flipTile(i);
            }
        }
    }

    public void showAllMines(){
        for(Tile tile: mTiles){
            if(tile.isMine()){
                tile.setStatus(Tile.Status.UNCOVERED);
            }
        }
    }

    /**
     * Method that calculates the GameStatus
     * @return GameStatus
     */
    public GameStatus getStatus() {
        boolean allFlipped = true;
        for(Tile tile: mTiles){
            if(tile.getStatus() == Tile.Status.UNCOVERED && tile.isMine()){
                return GameStatus.LOST;
            }
            if (tile.getStatus() == Tile.Status.COVERED){
                allFlipped = false;
            }
        }
        if(allFlipped){
            return GameStatus.WIN;
        }
        return GameStatus.PLAYING;
    }

    public int getScore(){
        int score = 0;
        for(Tile tile: mTiles){
            if(tile.getStatus() == Tile.Status.UNCOVERED){
                score += tile.getAdjacentMinesCount();
            }
        }
        return score;
    }

    public List<Tile> getTiles() {
        return mTiles;
    }

    public int getFlagCount() {
        return mFlagCount;
    }

    public void increaseFlagCount() {
        this.mFlagCount ++;
    }
    public void decreaseFlagCount() {
        this.mFlagCount --;
    }

    public int getMinesCount(){
        return mDifficulty.getMines();
    }

    //===================INNER CLASSES/ LISTENERS/ ENUMS====================

    /**
     * Enum that defines the Difficulty
     */
    public enum Difficulty {
        EASY(8, 8, 10), MEDIUM(12, 12, 20), HARD(16, 16, 30);

        private int rows;
        private int cols;
        private int mines;
        Difficulty(int rows, int cols, int mines){
            this.rows = rows;
            this.cols = cols;
            this.mines = mines;
        }

        public int getRows() {
            return rows;
        }

        public int getCols() {
            return cols;
        }

        public int getMines() {
            return mines;
        }

    }

    /**
     * Enum that defines the game status
     */
    public enum GameStatus {
        PLAYING, WIN, LOST
    }
}
