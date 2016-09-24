package com.paya.authomation.Cache;

import android.support.v4.util.LruCache;

import com.paya.authomation.main.DetailLetters;

import java.util.List;

/**
 * Created by Administrator on 08/06/2016.
 */

public final class Detailletter  extends LruCache<String, List<DetailLetters>> {

    private static Detailletter instance;

    private Detailletter (final int maxSize) {
        super(maxSize);
    }

    public static Detailletter  get() {
        if (instance == null) {
            final int cacheSize = (int) (Runtime.getRuntime().maxMemory()) / 1024;
            instance = new Detailletter (cacheSize);
        }
        return instance;
    }



}