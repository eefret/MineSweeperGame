package com.kaissersoft.minesweepergame.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by eefret on 21/04/15.
 */
public class TileButton extends Button{

    public TileButton(Context context) {
        super(context);
    }

    public TileButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TileButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec); // This is the key that will make the height equivalent to its width
    }
}
