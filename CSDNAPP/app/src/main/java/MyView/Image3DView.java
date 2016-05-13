package MyView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by joehu on 2016/3/30.
 */
public class Image3DView extends ImageView {
    //旋转角度的基准值
    private static final float BASE_DEGREE = 50f;

    //旋转深度的基准值
    private static final float BASE_DEEP = 150f;

    private Camera mCamera;
    private Matrix mMatrix;
    private Bitmap mBitmap;

    //当前图片对应的下标
    private int mIndex;
    /**
     * 在前图片在X轴方向滚动的距离
     */
    private int mScrollX;

    private int mLayoutWidth;

    private int mWidth;

    private float mRotateDegree;

    //旋转的中心点
    private float mDx;

    //旋转的深度
    private float mDeep;

    public Image3DView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCamera = new Camera();
        mMatrix = new Matrix();
    }

    /**
     * 初始化Image3DView所需要的信息，包括图片宽度，截取背景图等。
     */
    public void initImageViewBitmap() {
        if(mBitmap == null) {
            setDrawingCacheEnabled(true);
            buildDrawingCache();
            mBitmap = getDrawingCache();
        }
        mLayoutWidth = Image3DSwitchView.mWidth;
        mWidth = getWidth() + Image3DSwitchView.IMAGE_PADDING * 2;
    }

    /**
     * 设置旋转角度。
     *
     * @param index
     *            当前图片的下标
     * @param scrollX
     *            当前图片在X轴方向滚动的距离
     */
    public void setRotateData(int index, int scrollX) {
        mIndex = index;
        mScrollX = scrollX;
    }

    public void recycleBitmap() {
        if(!mBitmap.isRecycled() && mBitmap != null) {
            mBitmap.recycle();
        }
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        mBitmap = null;
        initImageViewBitmap();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        mBitmap = null;
        initImageViewBitmap();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap = null;
        initImageViewBitmap();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmap = null;
        initImageViewBitmap();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mBitmap == null) {
            super.onDraw(canvas);
        }else {
            if(isImageVisible()) {
                //只有图片可见的时候才进行绘制。节省运算效率
                computeRotateData();
                mCamera.save();
                mCamera.translate(0.0f, 0.0f, mDeep);
                mCamera.rotateY(mRotateDegree);
                mCamera.getMatrix(mMatrix);
                mCamera.restore();
                mMatrix.preTranslate(-mDx, -getHeight() / 2);
                mMatrix.postTranslate(mDx, getHeight() / 2);
                canvas.drawBitmap(mBitmap, mMatrix, null);
            }
        }

    }

    /**
     * 在这里计算所有旋转所需要的数据。
     */
    private void computeRotateData() {
        float degreePerPix = BASE_DEGREE / mWidth;
        float deepPerPix = BASE_DEEP / ((mLayoutWidth - mWidth) / 2);
        switch (mIndex) {
            case 0:
                mDx = mWidth;
                mRotateDegree = 360f - (2 * mWidth + mScrollX) * degreePerPix;
                if (mScrollX < -mWidth) {
                    mDeep = 0;
                } else {
                    mDeep = (mWidth + mScrollX) * deepPerPix;
                }
                break;
            case 1:
                if (mScrollX > 0) {
                    mDx = mWidth;
                    mRotateDegree = (360f - BASE_DEGREE) - mScrollX * degreePerPix;
                    mDeep = mScrollX * deepPerPix;
                } else {
                    if (mScrollX < -mWidth) {
                        mDx = -Image3DSwitchView.IMAGE_PADDING * 2;
                        mRotateDegree = (-mScrollX - mWidth) * degreePerPix;
                    } else {
                        mDx = mWidth;
                        mRotateDegree = 360f - (mWidth + mScrollX) * degreePerPix;
                    }
                    mDeep = 0;
                }
                break;
            case 2:
                if (mScrollX > 0) {
                    mDx = mWidth;
                    mRotateDegree = 360f - mScrollX * degreePerPix;
                    mDeep = 0;
                    if (mScrollX > mWidth) {
                        mDeep = (mScrollX - mWidth) * deepPerPix;
                    }
                } else {
                    mDx = -Image3DSwitchView.IMAGE_PADDING * 2;
                    mRotateDegree = -mScrollX * degreePerPix;
                    mDeep = 0;
                    if (mScrollX < -mWidth) {
                        mDeep = -(mWidth + mScrollX) * deepPerPix;
                    }
                }
                break;
            case 3:
                if (mScrollX < 0) {
                    mDx = -Image3DSwitchView.IMAGE_PADDING * 2;
                    mRotateDegree = BASE_DEGREE - mScrollX * degreePerPix;
                    mDeep = -mScrollX * deepPerPix;
                } else {
                    if (mScrollX > mWidth) {
                        mDx = mWidth;
                        mRotateDegree = 360f - (mScrollX - mWidth) * degreePerPix;
                    } else {
                        mDx = -Image3DSwitchView.IMAGE_PADDING * 2;
                        mRotateDegree = BASE_DEGREE - mScrollX * degreePerPix;
                    }
                    mDeep = 0;
                }
                break;
            case 4:
                mDx = -Image3DSwitchView.IMAGE_PADDING * 2;
                mRotateDegree = (2 * mWidth - mScrollX) * degreePerPix;
                if (mScrollX > mWidth) {
                    mDeep = 0;
                } else {
                    mDeep = (mWidth - mScrollX) * deepPerPix;
                }
                break;
        }
    }

    private boolean isImageVisible() {
        boolean isVisible = false;
        switch(mIndex) {
            case 0:
                if (mScrollX < (mLayoutWidth - mWidth) / 2 - mWidth) {
                    isVisible = true;
                } else {
                    isVisible = false;
                }
                break;
            case 1:
                if (mScrollX > (mLayoutWidth - mWidth) / 2) {
                    isVisible = false;
                } else {
                    isVisible = true;
                }
                break;
            case 2:
                if (mScrollX > mLayoutWidth / 2 + mWidth / 2
                        || mScrollX < -mLayoutWidth / 2 - mWidth / 2) {
                    isVisible = false;
                } else {
                    isVisible = true;
                }
                break;
            case 3:
                if (mScrollX < -(mLayoutWidth - mWidth) / 2) {
                    isVisible = false;
                } else {
                    isVisible = true;
                }
                break;
            case 4:
                if (mScrollX > mWidth - (mLayoutWidth - mWidth) / 2) {
                    isVisible = true;
                } else {
                    isVisible = false;
                }
                break;
        }
        return isVisible;
    }

}
