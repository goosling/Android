package PrivateView;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by JOE on 2016/3/20.
 *
 * 此类进行大图展示和放大缩小等功能实现
 */
public class ZoomImageView extends View {

    //初始化状态常量
    private static final int STATUS_INIT = 1;

    private static final int STATUS_ZOOM_OUT = 2;

    private static final int STATUS_ZOOM_IN = 3;

    private static final int STATUS_MOVE = 4;

    //对于图片进行移动和缩放变换的矩阵
    private Matrix matrix = new Matrix();

    //待展示的bitmap对象
    private Bitmap sourceBitmap;

    private int currentStatus;

    //zoomImageView控件的宽度
    private int width;

    private int height;

    private float centerPointX;

    private float centerPointY;

    private float currentBitmapWidth;

    private float currentBitmapHeight;

    //记录上次手指移动的横坐标
    private float lastXMove = -1;

    private float lastYMove = -1;

    //手指在横坐标上的移动距离
    private float moveDistanceX;

    private float moveDistanceY;

    //记录图片在矩阵上的横向偏移量
    private float totalTranslateX;

    private float totalTranslateY;

    //记录图片的总缩放比例
    private float totalRatio;

    //记录手指移动的距离所造成的缩放比例
    private float scaledRatio;

    private float initRatio;

    private double lastFingerDis;

    //构造函数
    public ZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        currentStatus = STATUS_INIT;
    }

    //将待展示的图片设置进来
    public void setImageBitmap(Bitmap bitmap) {
        sourceBitmap = bitmap;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(changed) {
            width = getWidth();
            height = getHeight();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (initRatio == totalRatio) {
            getParent().requestDisallowInterceptTouchEvent(false);
        } else {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() == 2) {
                    //当有两个手指在屏幕上，计算两指之间的距离
                    lastFingerDis = distanceBetweenFingers(event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(event.getPointerCount() == 1) {
                    //单指在屏幕上运动时
                    float xMove = event.getX();
                    float yMove = event.getY();
                    if(lastXMove == -1 && lastYMove == -1) {
                        lastXMove = xMove;
                        lastYMove = yMove;
                    }
                    currentStatus = STATUS_MOVE;
                    moveDistanceX = xMove - lastXMove;
                    moveDistanceY = yMove - lastYMove;
                    //进行边界检查，不允许将图片拖出边界
                    if(totalTranslateX + moveDistanceX > 0) {
                        moveDistanceX = 0;
                    }else if(width - (totalTranslateX + moveDistanceX) > currentBitmapWidth) {
                        moveDistanceX = 0;
                    }
                    if(totalTranslateY + moveDistanceY > 0) {
                        moveDistanceY = 0;
                    }else if(height - (totalTranslateY + moveDistanceY) > currentBitmapHeight) {
                        moveDistanceY = 0;
                    }

                    //调用onDraw方法 绘制图片
                    invalidate();
                    lastXMove = xMove;
                    lastYMove = yMove;
                }else if(event.getPointerCount() == 2) {
                    //同时两个手指放在屏幕上，默认为缩放状态
                    centerPointBetweenFingers(event);
                    double fingerDis = distanceBetweenFingers(event);
                    if(fingerDis > lastFingerDis) {
                        currentStatus = STATUS_ZOOM_OUT;
                    }else {
                        currentStatus = STATUS_ZOOM_IN;
                    }
                    //进行缩放倍数检查，最大可以放大4倍，最小将其返回到初始状态
                    if((currentStatus == STATUS_ZOOM_OUT && totalRatio < 4*initRatio)
                            || (currentStatus == STATUS_ZOOM_IN && totalRatio > initRatio)) {
                        scaledRatio = (float) (fingerDis / lastFingerDis);
                        totalRatio = totalRatio * scaledRatio;
                        if(totalRatio > 4*initRatio) {
                            totalRatio = 4*initRatio;
                        }else if(totalRatio < initRatio) {
                            totalRatio = initRatio;
                        }
                        invalidate();
                        lastFingerDis = fingerDis;
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if(event.getPointerCount() == 2) {
                    //手指离开屏幕将临界值还原
                    lastXMove = -1;
                    lastYMove = -1;
                }
                break;
            case MotionEvent.ACTION_UP:
                lastXMove = -1;
                lastYMove = -1;
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch(currentStatus) {
            case STATUS_ZOOM_OUT:
            case STATUS_ZOOM_IN:
                zoom(canvas);
                break;
            case STATUS_MOVE:
                move(canvas);
                break;
            case STATUS_INIT:
                initBitmap(canvas);
                break;
            default:
                canvas.drawBitmap(sourceBitmap, matrix, null);
                break;
        }
    }

    /**
     * 对图片进行缩放处理。
     *
     * @param canvas
     */
    private void zoom(Canvas canvas) {
        matrix.reset();
        //将图片按比例缩放
        matrix.postScale(totalRatio, totalRatio);
        float scaledWidth = sourceBitmap.getWidth() * totalRatio;
        float scaledHeight = sourceBitmap.getHeight() * totalRatio;
        float translateX = 0f;
        float translateY = 0f;
        // 如果当前图片宽度小于屏幕宽度，则按屏幕中心的横坐标进行水平缩放。否则按两指的中心点的横坐标进行水平缩放
        if(currentBitmapWidth < width) {
            translateX = (width - scaledWidth) / 2f;
        }else {
            translateX = totalTranslateX * scaledRatio + centerPointX * (1 - scaledRatio);
            // 进行边界检查，保证图片缩放后在水平方向上不会偏移出屏幕
            if (translateX > 0) {
                translateX = 0;
            } else if (width - translateX > scaledWidth) {
                translateX = width - scaledWidth;
            }
        }

        if (currentBitmapHeight < height) {
            translateY = (height - scaledHeight) / 2f;
        } else {
            translateY = totalTranslateY * scaledRatio + centerPointY * (1 - scaledRatio);
            // 进行边界检查，保证图片缩放后在垂直方向上不会偏移出屏幕
            if (translateY > 0) {
                translateY = 0;
            } else if (height - translateY > scaledHeight) {
                translateY = height - scaledHeight;
            }
        }

        // 缩放后对图片进行偏移，以保证缩放后中心点位置不变
        matrix.postTranslate(translateX, translateY);
        totalTranslateX = translateX;
        totalTranslateY = translateY;
        currentBitmapWidth = scaledWidth;
        currentBitmapHeight = scaledHeight;
        canvas.drawBitmap(sourceBitmap, matrix, null);
    }

    private void move(Canvas canvas) {
        matrix.reset();
        float translateX = totalTranslateX + moveDistanceX;
        float translateY = totalTranslateY + moveDistanceY;
        matrix.postScale(totalRatio, totalRatio);
        totalTranslateX = translateX;
        totalTranslateY = translateY;
        canvas.drawBitmap(sourceBitmap, matrix, null);
    }


    /**
     * 对图片进行初始化操作，包括让图片居中，以及当图片大于屏幕宽高时对图片进行压缩。
     *
     * @param canvas
     */
    private void initBitmap(Canvas canvas) {
        if(sourceBitmap != null) {
            matrix.reset();
            int bitmapWidth = sourceBitmap.getWidth();
            int bitmapHeight = sourceBitmap.getHeight();
            if(bitmapWidth > width || bitmapHeight > height) {
                if(bitmapWidth - width > bitmapHeight - height) {
                    float ratio = width / (bitmapWidth*1.0f);
                    matrix.postScale(ratio, ratio);
                    float translateY = (height - (bitmapHeight * ratio)) / 2f;
                    //在坐标方向上进行偏移，保证图片居中显示
                    matrix.postScale(0, translateY);
                    totalTranslateY = translateY;
                    totalRatio = initRatio;
                }else {
                    float ratio = height / (bitmapHeight * 1.0f);
                    matrix.postScale(ratio, ratio);
                    float translateX = (width - (bitmapWidth * ratio)) / 2f;
                    matrix.postScale(translateX, 0);
                    totalTranslateX = translateX;
                    totalRatio = ratio;
                }
                currentBitmapWidth = bitmapWidth * initRatio;
                currentBitmapHeight = bitmapHeight * initRatio;
            }else {
                // 当图片的宽高都小于屏幕宽高时，直接让图片居中显示
                float translateX = (width - sourceBitmap.getWidth()) / 2f;
                float translateY = (height - sourceBitmap.getHeight()) / 2f;
                matrix.postTranslate(translateX, translateY);
                totalTranslateX = translateX;
                totalTranslateY = translateY;
                totalRatio = initRatio = 1f;
                currentBitmapWidth = bitmapWidth;
                currentBitmapHeight = bitmapHeight;
            }
            canvas.drawBitmap(sourceBitmap, matrix, null);
        }
    }

    /**
     * 计算两个手指之间的距离。
     *
     * @param event
     * @return 两个手指之间的距离
     */
    private double distanceBetweenFingers(MotionEvent event) {
        float disX = Math.abs(event.getX(0) - event.getX(1));
        float disY = Math.abs(event.getY(0) - event.getY(1));
        return Math.sqrt(disX * disX + disY * disY);
    }

    /**
     * 计算两个手指之间中心点的坐标。
     *
     * @param event
     */
    private void centerPointBetweenFingers(MotionEvent event) {
        float xPoint0 = event.getX(0);
        float yPoint0 = event.getY(0);
        float xPoint1 = event.getX(1);
        float yPoint1 = event.getY(1);
        centerPointX = (xPoint0 + xPoint1) / 2;
        centerPointY = (yPoint0 + yPoint1) / 2;
    }
}
