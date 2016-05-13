package PrivateView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.bidirslidinglayout.R;

import java.io.InputStream;
import java.lang.reflect.Field;

/**
 * Created by JOE on 2016/3/21.
 * 可以播放动画的类
 */
public class PowerImageView extends ImageView implements View.OnClickListener{

    //可以播放动画的关键类
    private Movie mMovie;

    //开始播放按钮动画
    private Bitmap mStartButton;

    //开始动画时间
    private long mMovieStart;

    //GIF图片的宽度
    private int mImageWidth;

    private int mImageHeight;

    private boolean isAutoPlay;

    private boolean isPlaying;

    public PowerImageView(Context context) {
        super(context);
    }

    /**
     * PowerImageView构造函数。
     *
     * @param context
     */
    public PowerImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * PowerImageView构造函数，在这里完成所有必要的初始化操作。
     *
     * @param context
     */
    public PowerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PowerImageView);
        int resourceId = getResourceId(a, context, attrs);
        if(resourceId != 0) {
            //当资源id不等于0，则获取该资源的流
            InputStream in = getResources().openRawResource(resourceId);
            //使用movie类对流进行解码
            mMovie = Movie.decodeStream(in);
            if(mMovie != null) {
                //如果返回值不为空，则证明这是一个GIF图片，下面获取自动播放属性
                isAutoPlay = a.getBoolean(R.styleable.PowerImageView_auto_play, false);
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                mImageWidth = bitmap.getWidth();
                mImageHeight = bitmap.getHeight();
                bitmap.recycle();
                if(!isAutoPlay) {
                    //当不允许自动播放的时候，得到开始的第一帧图片，并设置点击事件
                    mStartButton = BitmapFactory.decodeResource(getResources(), R.drawable.bmp1);
                    setOnClickListener(this);
                }
            }
        }
    }

    public void onClick(View v) {
        if(v.getId() == getId()) {
            isPlaying = true;
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mMovie == null) {
            super.onDraw(canvas);
        }else {
            if(isAutoPlay) {
                playMovie(canvas);
                invalidate();
            }else {
                if(isPlaying) {
                    if(playMovie(canvas)) {
                        isPlaying = false;
                    }
                    invalidate();
                }else {
                    mMovie.setTime(0);
                    mMovie.draw(canvas, 0, 0);
                    int offsetW = (mImageWidth - mStartButton.getWidth()) /2;
                    int offsetH = (mImageHeight - mStartButton.getHeight()) / 2;
                    canvas.drawBitmap(mStartButton, offsetW, offsetH, null);
                }
            }
        }
    }

    /**在这个方法中我们进行判断，如果这是一张GIF图片，
    则需要将PowerImageView的宽高重定义，使得控件的大小刚好可以放得下这张GIF图片。*/
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(mMovie != null) {
            setMeasuredDimension(mImageWidth, mImageHeight);
        }
    }

    /**
     * 开始播放GIF动画，播放完成返回true，未完成返回false。
     *
     * @param canvas
     * @return 播放完成返回true，未完成返回false。
     * 可以看到，首先会对动画开始的时间做下记录，然后对动画持续的时间做下记录，
     * 接着使用当前的时间减去动画开始的时间，
     * 得到的时间就是此时PowerImageView应该显示的那一帧，然后借助Movie对象将这一帧绘制到屏幕上即可。
     *
     *
     */
    private boolean playMovie(Canvas canvas) {
        long now = SystemClock.uptimeMillis();
        if(mMovieStart == 0) {
            mMovieStart = now;
        }
        int duration = mMovie.duration();
        if(duration == 0) {
            duration = 1000;
        }

        int relTime = (int)((now - mMovieStart)%duration);
        mMovie.setTime(relTime);
        mMovie.draw(canvas, 0, 0);
        if((now - mMovieStart) > duration) {
            mMovieStart = 0;
            return true;
        }
        return false;
    }

    /**
     * 通过Java反射，获取到src指定图片资源所对应的id。
     *
     * @param a
     * @param context
     * @param attrs
     * @return 返回布局文件中指定图片资源所对应的id，没有指定任何图片资源就返回0。
     */
    private int getResourceId(TypedArray a, Context context, AttributeSet attrs) {
        try {
            Field field = TypedArray.class.getDeclaredField("mValue");
            field.setAccessible(true);
            TypedValue typedValueObject = (TypedValue) field.get(a);
            return typedValueObject.resourceId;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
        return 0;
    }
}
