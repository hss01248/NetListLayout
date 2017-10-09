package com.example.dell.netrecycleview.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

/**
 * Created by huangshuisheng on 2017/9/30.
 */

public class RecyclerViewConfig {
    public RecyclerView.LayoutManager layoutManager;
    public RecyclerView.ItemAnimator itemAnimator;
    public RecyclerView.ItemDecoration itemDecoration;


    public RecyclerViewConfig(Context context){
        layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        itemAnimator = new DefaultItemAnimator();
        itemDecoration =  new HorizontalDividerItemDecoration.Builder(context)
            .color(Color.LTGRAY)
            .size(5)
            .build();
    }
}
