package com.kaissersoft.minesweepergame.activities;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;

import com.kaissersoft.minesweepergame.R;
import com.kaissersoft.minesweepergame.fragments.MineField;
import com.kaissersoft.minesweepergame.fragments.ScoreScreen;
import com.kaissersoft.minesweepergame.logic.Game;
import com.kaissersoft.minesweepergame.pojos.Tile;
import com.kaissersoft.minesweepergame.util.Loggable;

import static com.kaissersoft.minesweepergame.pojos.Tile.Status.COVERED;
import static com.kaissersoft.minesweepergame.pojos.Tile.Status.FLAG;
import static com.kaissersoft.minesweepergame.pojos.Tile.Status.GUESS;

/**
 * Created by eefret on 21/04/15.
 */
public class GameActivity extends Base
        implements Loggable, SoundPool.OnLoadCompleteListener,
        MineField.MineFieldListeners{

    //===================FIELDS====================
    public static final String DIFFICULTY = "Difficulty";
    private SoundPool mSounds;
    private SoundClips mSoundClips;
    Game.Difficulty difficulty;
    private static Game mGame;

    //===================OVERRIDEN METHODS====================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarIcon(R.drawable.ic_action_ic_main);
        createSoundPool();
        Intent i = getIntent();
        difficulty = (Game.Difficulty) i.getSerializableExtra(DIFFICULTY);

        //Create the game
        if(mGame == null){
            mGame = new Game(difficulty);
        }
        //Adding fragments
        setMineFragment();
        setScoreScreenFragment();


    }

    @Override
    protected boolean needToolbar() {
        return true;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_game;
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        if(status == 0) {
            if(sampleId == mSoundClips.getFlipSoundID()){
                mSoundClips.flipSoundEnabled = true;
            }
            if (sampleId == mSoundClips.getBombSoundID()){
                mSoundClips.bombSoundEnabled = true;
            }
        }
    }

    @Override
    public void onTileClicked(View tileView, int position, long tileId) {
        MineField mineField = (MineField) getFragmentManager().findFragmentByTag(MineField.TAG);
        ScoreScreen scoreScreen = (ScoreScreen) getFragmentManager().findFragmentByTag(ScoreScreen.TAG);
        if (mineField == null ||
                mGame.getStatus() != Game.GameStatus.PLAYING ||
                scoreScreen == null){
            return;
        }
        Tile tile = mineField.getAdapter().getItem(position);

        switch (tile.getStatus()){
            case COVERED:
            case GUESS:
                mGame.flipTile(position);
                mineField.getAdapter().notifyDataSetChanged();
                playSound(SOUNDS.FLIP);
                scoreScreen.setFaceType(ScoreScreen.FaceType.NORMAL);
                break;
            case UNCOVERED:
            case FLAG:
                return;
        }
        scoreScreen.setScore(mGame.getScore());

        if(mGame.getStatus() == Game.GameStatus.LOST){
            lost();
        }
    }

    @Override
    public void onTileLongPressed(View tileView, int position, long tileId) {
        MineField mineField = (MineField) getFragmentManager().findFragmentByTag(MineField.TAG);
        ScoreScreen scoreScreen = (ScoreScreen) getFragmentManager().findFragmentByTag(ScoreScreen.TAG);
        if (mineField == null ||
                mGame.getStatus() != Game.GameStatus.PLAYING||
                scoreScreen == null){
            return;
        }

        Tile tile = mineField.getAdapter().getItem(position);

        switch (tile.getStatus()){
            case UNCOVERED:
                return;
            case FLAG:
                tile.setStatus(GUESS);
                scoreScreen.setFaceType(ScoreScreen.FaceType.GUESSED);
                mGame.increaseFlagCount();
                break;
            case COVERED:
                if(mGame.getFlagCount()==0){
                    return;
                }
                tile.setStatus(FLAG);
                scoreScreen.setFaceType(ScoreScreen.FaceType.FLAGGED);
                mGame.decreaseFlagCount();
                break;
            case GUESS:
                tile.setStatus(COVERED);
                scoreScreen.setFaceType(ScoreScreen.FaceType.NORMAL);
                break;
        }
        mineField.getAdapter().notifyDataSetChanged();
        scoreScreen.setFlagCount(mGame.getFlagCount());
    }

    @Override
    public void onMineFieldTouched(MotionEvent event) {
        ScoreScreen scoreScreen = (ScoreScreen) getFragmentManager().findFragmentByTag(ScoreScreen.TAG);
        if(mGame.getStatus() != Game.GameStatus.PLAYING||
                scoreScreen == null){
            return;
        }
        if(event.getActionMasked() == MotionEvent.ACTION_DOWN){
            scoreScreen.setFaceType(ScoreScreen.FaceType.FLIPPING);
        }
    }

    @Override
    public Game getActiveGame() {
        return mGame;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.cheat:
                mGame.cheat();
                MineField mineField = (MineField) getFragmentManager().findFragmentByTag(MineField.TAG);
                if(mineField != null){
                    mineField.getAdapter().notifyDataSetChanged();
                }
                return true;
            case R.id.done:
                if(mGame.getStatus() == Game.GameStatus.WIN){
                    won();
                }else {
                    lost();
                }
                return true;
            case R.id.reset:
                mGame = null;
                recreate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //===================METHODS====================

    /**
     * Creates the Sound pool to play sfx
     */
    @SuppressWarnings("deprecation")
    private void createSoundPool(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            mSounds = new SoundPool.Builder()
                    .setAudioAttributes(attributes)
                    .build();
        }else {
            mSounds = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        }
        mSounds.setOnLoadCompleteListener(this);
        mSoundClips = new SoundClips(
                mSounds.load(this, R.raw.pop, 1),
                mSounds.load(this, R.raw.explosion,1));
    }

    /**
     * Play the sound given
     * @param sound SOUNDS
     */
    private void playSound(SOUNDS sound){
        switch (sound){
            case FLIP:
                if(mSoundClips.flipSoundEnabled){
                    mSounds.play(
                            mSoundClips.getFlipSoundID(),
                            1f,1f, 0, 0, 1.0f);
                }
            case BOMB:
                if(mSoundClips.bombSoundEnabled){
                    mSounds.play(
                            mSoundClips.getBombSoundID(),
                            1f,1f, 0, 0, 1.0f);
                }
        }
    }

    /**
     * Callback for game win
     */
    public void won(){
        ScoreScreen scoreScreen = (ScoreScreen) getFragmentManager().findFragmentByTag(ScoreScreen.TAG);
        if(scoreScreen == null){
            return;
        }
        scoreScreen.setFaceType(ScoreScreen.FaceType.WON);
        mGame = null;
        new AlertDialog.Builder(this)
                .setTitle(R.string.won_dialog_title)
                .setMessage(R.string.won_dialog_message)
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mGame = null;
                        recreate();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.CANCEL, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * callback for game lose
     */
    public void lost(){
        MineField mineField = (MineField) getFragmentManager().findFragmentByTag(MineField.TAG);
        ScoreScreen scoreScreen = (ScoreScreen) getFragmentManager().findFragmentByTag(ScoreScreen.TAG);
        if(scoreScreen == null){
            return;
        }
        mGame.showAllMines();
        mineField.getAdapter().notifyDataSetChanged();
        scoreScreen.setFaceType(ScoreScreen.FaceType.LOST);

        new AlertDialog.Builder(this)
                .setTitle(R.string.lost_dialog_title)
                .setMessage(R.string.lost_dialog_message)
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mGame = null;
                        recreate();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.CANCEL, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * sets the MineFragment
     */
    public void setMineFragment(){
        getFragmentManager().beginTransaction().replace(R.id.a_game_minefield_container,
                MineField.newInstance(), MineField.TAG).commit();
    }

    /**
     * sets the score fragment
     */
    public void setScoreScreenFragment(){
        ScoreScreen scoreScreen = ScoreScreen.newInstance();

        Bundle args = new Bundle();
        args.putInt(ScoreScreen.FLAG_COUNT_BUNDLE,mGame.getFlagCount());
        args.putInt(ScoreScreen.SCORE_BUNDLE, mGame.getScore());

        scoreScreen.setArguments(args);

        getFragmentManager().beginTransaction().replace(R.id.a_game_counter_container,
                scoreScreen, ScoreScreen.TAG).commit();
    }

    /**
     * clears the game
     */
    public void clearGame(){
        mGame = new Game();
    }

    //===================INNER CLASSES====================

    /**
     * Inner class that holds the soundclips and their flags
     */
    private class SoundClips {
        public SoundClips(int flipSound, int bombSound){
            this.flipSound = flipSound;
            this.bombSound = bombSound;
        }
        private final int flipSound;
        private final int bombSound;
        private boolean flipSoundEnabled = false;
        private boolean bombSoundEnabled = false;

        public int getFlipSoundID() {
            return flipSound;
        }

        public int getBombSoundID() {
            return bombSound;
        }
    }

    private enum SOUNDS{
        FLIP, BOMB
    }
}
