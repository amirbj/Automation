package com.paya.authomation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.paya.authomation.main.R;

/**
 * Created by Administrator on 07/31/2016.
 */
public class WebviewFragment extends Fragment {
    WebView webView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View view= inflater.inflate(R.layout.webviewdetail,container, false);
        webView = (WebView) view.findViewById(R.id.fullweb);
       String html= getArguments().getString("html");
        webView.loadData(html, "text/html; charset=utf-8", "UTF-8");

        return view;
    }



}
