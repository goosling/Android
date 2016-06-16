package com.example.joe.mashangpinche.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveStep;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JOE on 2016/5/18.
 */
public class MapUtils {
    public static final int CONS_LINE_WIDTH = 10;

    /**
     * 给地图加入路线。该方法基于高德地图的DrivingRouteOverlay。addToMap方法改写。
     * @param aMap
     * @param drivePath
     * @param startPoint
     * @param endPoint
     */
    public static void addRouteLineToMap(AMap aMap, DrivePath drivePath, LatLng startPoint,
                                         LatLng endPoint) {
        List<Polyline> allPolyLines = new ArrayList<>();
        List<DriveStep> drivePaths = drivePath.getSteps();
        for(int i=0; i < drivePaths.size(); i++) {
            DriveStep driveStep = drivePaths.get(i);
            LatLng latLng = convertToLatLng(driveStep.getPolyline().get(0));
            if(i < drivePaths.size() - 1) {
                if(i == 0) {
                    Polyline oneLine = aMap.addPolyline(new PolylineOptions()
                        .add(startPoint, latLng).color(Color.parseColor("#537edc"))
                        .width(CONS_LINE_WIDTH));
                    allPolyLines.add(oneLine);
                }

                LatLng latlngEnd = convertToLatLng(driveStep.getPolyline()
                    .get(driveStep.getPolyline().size() - 1));
                LatLng latLngStart = convertToLatLng(drivePaths.get(i + 1)
                    .getPolyline().get(0));

                if(!(latlngEnd.equals(latLngStart))) {
                    Polyline breakLine = aMap.addPolyline(new PolylineOptions()
                        .add(latlngEnd, latLngStart).color(Color.parseColor("#537edc"))
                        .width(CONS_LINE_WIDTH));
                    allPolyLines.add(breakLine);
                }
            }else {
                LatLng latLng1 = convertToLatLng(driveStep.getPolyline()
                    .get(driveStep.getPolyline().size() - 1));
                Polyline endLine = aMap.addPolyline(new PolylineOptions()
                        .add(latLng1, endPoint).color(Color.parseColor("#537edc"))
                        .width(CONS_LINE_WIDTH));
                allPolyLines.add(endLine);
            }

            Polyline driveLine = aMap.addPolyline(new PolylineOptions()
                    .addAll(convertArrayList(driveStep
                            .getPolyline())).color(Color.parseColor("#537edc"))
                    .width(CONS_LINE_WIDTH));
            allPolyLines.add(driveLine);
        }
    }

    public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    public static LatLonPoint convertToLatLonPoint(LatLng latlng) {
        return new LatLonPoint(latlng.latitude, latlng.longitude);
    }

    public static ArrayList<LatLng> convertArrayList(List<LatLonPoint> shapes) {
        ArrayList<LatLng> lineShapes = new ArrayList<>();
        for(LatLonPoint point : shapes) {
            LatLng latLngTemp = convertToLatLng(point);
            lineShapes.add(latLngTemp);
        }
        return lineShapes;
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, float res) {
        if(bitmap == null) {
            return null;
        }

        int width, height;
        width = (int)(bitmap.getWidth() * res);
        height = (int)(bitmap.getHeight() * res);
        Bitmap newbmp = Bitmap.createScaledBitmap(bitmap, width, height, true);
        return newbmp;
    }

}
