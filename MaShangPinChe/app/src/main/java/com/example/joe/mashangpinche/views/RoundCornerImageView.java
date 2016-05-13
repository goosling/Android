package com.example.joe.mashangpinche.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by sodiaw on 2016/5/4.
 */
public class RoundCornerImageView extends ImageView {

    private float radius;

    public RoundCornerImageView(Context context) {
        super(context);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.RoundCornerImageView, 0, 0
        );
        try{
            radius = a.getFloat(R.styleable.RoundCornerImageView_radius, 0);
        }finally {
            a.recycle();
        }
    }

    public RoundCornerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        if(!(drawable instanceof BitmapDrawable)) {
            super.setImageDrawable(drawable);
            return;
        }

        Bitmap b = ((BitmapDrawable)drawable).getBitmap();
        Bitmap bitmap = Bitmap.createBitmap(b.getWidth(), b.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        canvas.drawARGB(0, 0, 0, 0);
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);

        paint.setColor(Color.BLACK);
        canvas.drawRoundRect(rectF, radius, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(b, rect, rect, paint);
        paint.setXfermode(null);

        super.setImageDrawable(new BitmapDrawable(bitmap));

    }
}
