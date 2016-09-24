package com.paya.authomation.Cache;

import android.support.v4.util.LruCache;

import com.paya.authomation.main.DetailLetters;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 08/06/2016.
 */

public final class ListString  extends LruCache<String, Map<String, String>> {

    private static ListString instance;

    private ListString (final int maxSize) {
        super(maxSize);
    }

    public static ListString  get() {
        if (instance == null) {
            final int cacheSize = (int) (Runtime.getRuntime().maxMemory()) / 1024;
            instance = new ListString (cacheSize);
        }
        return instance;
    }



}
