package com.example.dell.netrecycleview.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by huangshuisheng on 2017/9/30.
 */

public class LoadMoreRecyclerLayout extends SwipeRefreshLayout {
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    RecyclerView recyclerView;
    RefreshConfig refreshConfig;
    LoadMoreConfig loadMoreConfig;
    PageStateConfig pageStateConfig;
    RecyclerViewConfig recyclerViewConfig;

    PageCustomConfig pageCustomConfig;




    public LoadMoreRecyclerLayout(Context context) {
        this(context,null);
    }

    public LoadMoreRecyclerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initData(context);
    }

    private void initData(Context context) {

    }

    private void initView(Context context) {
        recyclerView = new RecyclerView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        recyclerView.setLayoutParams(params);
        this.addView(recyclerView);
    }

    public LoadMoreRecyclerLayout setPageCustomConfig(PageCustomConfig pageCustomConfig){
        this.pageCustomConfig = pageCustomConfig;
        return this;
    }

    /*public LoadMoreRecyclerLayout setRefreshConfig(RefreshConfig refreshConfig){
        return this;
    }
    public LoadMoreRecyclerLayout setLoadMoreConfig(LoadMoreConfig loadMoreConfig){
        return this;
    }
    public LoadMoreRecyclerLayout setPageStateConfig(PageStateConfig pageStateConfig){
        return this;
    }
    public LoadMoreRecyclerLayout setRecyclerViewConfig(RecyclerViewConfig recyclerViewConfig){
        if(recyclerViewConfig==null){
            return this;
        }

        this.recyclerViewConfig = recyclerViewConfig;


        return this;
    }*/
}
