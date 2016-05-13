package com.example.joe.nine9gag.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by joe on 2016/4/19.
 */
public abstract class BaseDataHelper {

    private Context mContext;

    public BaseDataHelper(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    protected abstract Uri getContentUri();

    public void notifyChange() {
        mContext.getContentResolver().notifyChange(getContentUri(), null);
    }

    protected Cursor query(Uri uri, String[] projection, String selection,
                           String[] selectionArgs, String sortOrder) {
        return mContext.getContentResolver().query(uri, projection, selection,
                selectionArgs, sortOrder);
    }

    protected Cursor query(String[] projection, String selection,
                           String[] selectionArgs, String sortOrder) {
        return mContext.getContentResolver().query(getContentUri(), projection, selection,
                selectionArgs, sortOrder);
    }

    protected final Uri insert(ContentValues values) {
        return mContext.getContentResolver().insert(getContentUri(), values);
    }

    protected final int bulkInsert(ContentValues[] values) {
        return mContext.getContentResolver().bulkInsert(getContentUri(), values);
    }

    protected final int update(ContentValues values, String where, String[] whereArgs) {
        return mContext.getContentResolver().update(getContentUri(), values, where, whereArgs);
    }

    protected final int delete(Uri uri, String selection, String[] selectionArgs) {
        return mContext.getContentResolver().delete(uri, selection, selectionArgs);
    }

    protected final Cursor getList(String[] projection, String selection, String[] whereArgs,
                                   String sortOrder) {
        return mContext.getContentResolver().query(getContentUri(), projection, selection,
                whereArgs, sortOrder);
    }

    public CursorLoader getCursorLoader(Context context) {
        return getCursorLoader(context, null, null, null, null);
    }

    public CursorLoader getCursorLoader(Context context, String[] projection, String selection,
                                        String[] whereArgs, String sortOrder) {
        return new CursorLoader(context, getContentUri(), projection, selection, whereArgs, sortOrder);
    }

}
