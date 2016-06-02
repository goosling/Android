package com.example.joe.mi_notes.tool;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by JOE on 2016/6/1.
 */
public class DataUtils {
    public static final String TAG = "DataUtils";

    public static boolean batchDeleteNotes(ContentResolver resolver, HashSet<Long> ids) {
        if(ids == null) {
            Log.d(TAG, "the ids is null");
            return true;
        }

        if(ids.size() == 0) {
            Log.d(TAG, "no id is in hashset");
            return true;
        }

        ArrayList<ContentProviderOperation> operationList = new ArrayList<>();
        for(long id: ids) {
            if(id == Note.ID_ROOT_FOLDER) {

            }
        }

    }
}
