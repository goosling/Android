package com.example.baidutest;

import android.app.Activity;
import android.os.Bundle;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;

public class MainActivity extends Activity {

    MapView mMapView = null;
    private GeoPoint geoPoint;
    private BaiduMap mBaiduMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map_view);
        mBaiduMap = mMapView.getMap();
        /*mBaiduMap.setOnMarkerDragListener(new OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
                //拖拽中
            }
            public void onMarkerDragEnd(Marker marker) {
                //拖拽结束
            }
            public void onMarkerDragStart(Marker marker) {
                //开始拖拽
            }
        });*/
        addMarker(39.93923, 116.357428);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    private void addMarker(double lat, double longi) {
        LatLng point = new LatLng(lat, longi);
        //构建marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
        //构建markOption，用与在地图上添加marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap)
                .draggable(true);

        //添加marker并显示
        mBaiduMap.addOverlay(option);
    }

}
