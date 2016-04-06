package MyVIew;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import Animation.ColorEvaluator;
import Animation.PointEvaluator;
import DB.Point;

/**
 * Created by joe on 2016/4/6.
 */
public class MyAnimView extends View {

    public static final float RADIUS = 50f;

    private Point currentPoint;

    private Paint mPaint;

    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        mPaint.setColor(Color.parseColor(color));
        invalidate();

    }


    public MyAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(currentPoint == null) {
            currentPoint = new Point(RADIUS, RADIUS);
            drawCicle(canvas);
            startAnimation();
        }else {
            drawCicle(canvas);
        }

    }


    private void drawCicle(Canvas canvas) {
        float x = currentPoint.getX();
        float y = currentPoint.getY();
        canvas.drawCircle(x, y, RADIUS, mPaint);
    }

    private void startAnimation() {
        Point startPoint = new Point(RADIUS, RADIUS);
        Point endPoint = new Point(getWidth() - RADIUS, getHeight() - RADIUS);
        ValueAnimator animator = ValueAnimator.ofObject(new PointEvaluator(), startPoint, endPoint);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPoint = (Point) animation.getAnimatedValue();
                invalidate();
            }
        });
        ObjectAnimator animator1 = ObjectAnimator.ofObject(this, "color", new ColorEvaluator(),
                "#FF0000", "#0000FF");
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(animator).with(animator1);

        animator.setDuration(5000);
        animator.start();
    }
}
