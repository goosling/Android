package com.example.joe.mashangpinche.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.SeekBar;

import com.example.joe.mashangpinche.R;

/**
 * Created by joe on 2016/4/26.
 */
public class SeekBarWithText extends SeekBar {

    private Drawable thumb;

    public SeekBarWithText(Context context) {
        this(context, null);
    }

    public SeekBarWithText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeekBarWithText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setThumbWithText(String text) {
        Drawable drawable = getResources().getDrawable(
                R.drawable.main_seekbar_thumb);
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(new Rect(0, 0, canvas.getWidth(), canvas.getHeight()));
        drawable.draw(canvas);

        Paint p = new Paint();
        TypedValue textSizeValue = new TypedValue();
        getResources().getValue(R.dimen.main_seekbar_thumb_text_size,
                textSizeValue, true);
        p.setTextSize(textSizeValue.getFloat());
        p.setColor(getResources().getColor(R.color.themeYellow));

        int textWidth = (int)p.measureText(text);
        int yPos = (int)((canvas.getHeight() / 2) - ((p.descent() + p.ascent()) / 2));
        canvas.drawText(text, (bitmap.getWidth() - textWidth) / 2, yPos, p);

        thumb = new BitmapDrawable(getResources(), bitmap);

        updateThumbPos(getWidth(), getHeight());

        this.setThumb(thumb);
    }


    private void setThumbPos(int w, Drawable drawable, float scale, int gap) {
        int available = w - getPaddingLeft() - getPaddingRight();
        int thumbWidth = drawable.getIntrinsicWidth();
        int thumbHeight = drawable.getIntrinsicHeight();
        available -= thumbWidth;

        // The extra space for the thumb to move on the track
        available += getThumbOffset() * 2;

        int thumbPos = (int)(scale * available);

        int topBound, bottomBound;
        if(gap == Integer.MIN_VALUE) {
            Rect oldBounds = drawable.getBounds();
            topBound = oldBounds.top;
            bottomBound = oldBounds.bottom;
        }else {
            topBound = gap;
            bottomBound = gap + thumbHeight;
        }

        // Canvas will be translated, so 0,0 is where we start drawing
        thumb.setBounds(thumbPos, topBound, thumbPos + thumbWidth, bottomBound);
    }

    private void updateThumbPos(int w, int h) {
        Drawable d = getResources().getDrawable(R.drawable.main_seekbar_thumb);
        int thumbHeight = thumb == null ? 0 : thumb.getIntrinsicHeight();
        // The max height does not incorporate padding, whereas the height
        // parameter does
        int trackHeight = Math.min(getMax(), h - getPaddingTop()
                - getPaddingBottom());

        int max = getMax();
        float scale = max > 0 ? (float) getProgress() / (float) max : 0;

        if (thumbHeight > trackHeight) {
            if (thumb != null) {
                setThumbPos(w, thumb, scale, 0);
            }
            int gapForCenteringTrack = (thumbHeight - trackHeight) / 2;
            if (d != null) {
                // Canvas will be translated by the padding, so 0,0 is where we
                // start drawing
                d.setBounds(0, gapForCenteringTrack, w - getPaddingRight()
                        - getPaddingLeft(), h - getPaddingBottom()
                        - gapForCenteringTrack - getPaddingTop());
            }
        } else {
            if (d != null) {
                // Canvas will be translated by the padding, so 0,0 is where we
                // start drawing
                d.setBounds(0, 0, w - getPaddingRight() - getPaddingLeft(), h
                        - getPaddingBottom() - getPaddingTop());
            }
            int gap = (trackHeight - thumbHeight) / 2;
            if (thumb != null) {
                setThumbPos(w, thumb, scale, gap);
            }
        }
    }
}
