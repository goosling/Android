package com.example.joe.mashangpinche.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;

import com.example.joe.mashangpinche.R;

/**
 * Created by JOE on 2016/5/24.
 * 快捷方式工具类
 */
public class ShortcutUtils {
    //添加当前桌面的快捷方式
    public static void addShortcut(Context context) {
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        Intent shortcutIntent = context.getPackageManager().getLaunchIntentForPackage(
                context.getPackageName());
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);

        //获取当前快捷方式名称
        String title = null;
        try{
            final PackageManager pm = context.getPackageManager();
            title = pm.getApplicationLabel(pm.getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA)).toString();
        }catch(Exception e) {
            e.printStackTrace();
        }

        //快捷方式名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        // 不允许重复创建（不一定有效）
        shortcut.putExtra("duplicate", false);
        // 快捷方式的图标
        Parcelable iconResource = Intent.ShortcutIconResource.fromContext(context,
                R.mipmap.ic_launcher);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconResource);
        context.sendBroadcast(shortcut);
    }

    /**
     * 删除当前应用的桌面快捷方式
     *
     * @param cx
     */
    public static void delShortcut(Context cx) {
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        // 获取当前应用名称
        String title = null;
        try {
            final PackageManager pm = cx.getPackageManager();
            title = pm.getApplicationLabel(
                    pm.getApplicationInfo(cx.getPackageName(),
                            PackageManager.GET_META_DATA)).toString();
        } catch (Exception e) {
        }
        // 快捷方式名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        Intent shortcutIntent = cx.getPackageManager()
                .getLaunchIntentForPackage(cx.getPackageName());
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        cx.sendBroadcast(shortcut);


    }

    public static boolean hasShortcut(Context context) {
        boolean result = false;
        //获取当前应用名称
        String title = null;
        try{
            final PackageManager pm = context.getPackageManager();
            title = pm.getApplicationLabel(pm.getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA)).toString();
        }catch (Exception e) {
            e.printStackTrace();
        }
        final String uriStr;
        if(Build.VERSION.SDK_INT < 8) {
            uriStr = "content://com.android.launcher.settings/favorites?notify=true";
        }else {
            uriStr = "content://com.android.launcher2.settings/favorites?notify=true";
        }

        final Uri CONTENT_URI = Uri.parse(uriStr);
        final Cursor c = context.getContentResolver().query(CONTENT_URI, null,
                "title=?", new String[]{title}, null);
        if(c != null && c.getCount() > 0) {
            result = true;
        }
        return result;
    }
}
