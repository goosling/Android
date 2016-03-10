package com.example.joe.alltest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import com.facebook.cache.common.CacheKey;
import com.facebook.common.references.CloseableReference;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;

import java.util.List;

/**
 * Created by JOE on 2016/3/9.
 */
public class FrescoTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);

        setContentView(R.layout.fresco_layout);

        Uri uri = Uri.parse("https://raw.githubusercontent.com/facebook/fresco/gh-pages/static/fresco-logo.png");
        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.my_image_view);
        draweeView.setImageURI(uri);

        List<Drawable> backgoundList;
        List<Drawable> overlayList;
        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(300)
                .setPlaceholderImage(new MyCustomDrawable())
                .setBackgrounds(backgoundList)
                .setOverlays(overlayList)
                .build();
        //对于同一个view，请不要多次调用setHierarchy，因为比较耗时间
        draweeView.setHierarchy(hierarchy);

        //另一个方法中
        GenericDraweeHierarchy hierarchy1 = draweeView.getHierarchy();

        //修改缩放类型:
        hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);

        //圆角
        RoundingParams roundingParams = hierarchy.getRoundingParams();
        roundingParams.setCornersRadius(10);
        hierarchy.setRoundingParams(roundingParams);

        //DraweeController
        ControllerListener listener = new BaseControllerListener() {

        };

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setTapToRetryEnabled(true)
                //在指定一个新的controller的时候，使用setOldController，这可节省不必要的内存分配。
                .setOldController(draweeView.getController())
                .setControllerListener(listener)
                .build();

        draweeView.setController(controller);

        //后处理器
        Postprocessor myPostprocessor = new Postprocessor() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public CloseableReference<Bitmap> process(Bitmap sourceBitmap, PlatformBitmapFactory bitmapFactory) {
                return null;
            }

            @Override
            public CacheKey getPostprocessorCacheKey() {
                return null;
            }
        };
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setPostprocessor(myPostprocessor)
                .build();

        DraweeController controller1 = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(draweeView.getController())
                        // 其他设置
                .build();
    }
}
