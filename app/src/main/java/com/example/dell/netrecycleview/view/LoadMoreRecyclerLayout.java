package com.example.dell.netrecycleview.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.dell.netrecycleview.loadmore.CustomLoadMoreView;
import com.example.dell.netrecycleview.qxinli.ArticleInfo;
import com.hss01248.net.builder.StandardJsonRequestBuilder;
import com.hss01248.net.wrapper.HttpUtil;
import com.hss01248.net.wrapper.MyNetListener;
import com.hss01248.pagestate.PageManager;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

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


//逻辑参数
    List datas ;
    int currentPageIndex;
    int PAGE_SIZE = 10;
    PageManager pageManager;
    BaseQuickAdapter adapter;




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

    public void start(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(
            new HorizontalDividerItemDecoration.Builder(getContext())
                .color(Color.RED)
                .size(5)
                .build());
        currentPageIndex = 1;
        pageManager = PageManager.init(recyclerView, "empty", true, new Runnable() {
            @Override
            public void run() {
                getData(0);
            }
        });
        initAdapter();
        getData(0);

    }

    private void initAdapter() {
        adapter = pageCustomConfig.adapter;
        recyclerView.setAdapter(adapter);
        adapter.setLoadMoreView(new CustomLoadMoreView());
        //adapter.setEmptyView(R.layout.pager_empty);

        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData(currentPageIndex+1);

            }
        });

        setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(1);
            }
        });

    }

    private void getData(final int pageIndex) {
        datas = new ArrayList();
        if(pageIndex>1){
            adapter.notifyLoadMoreToLoading();
        }else if(pageIndex==0){
            pageManager.showLoading();
        }
        int index = pageIndex>1 ? pageIndex :1;
        StandardJsonRequestBuilder builder = HttpUtil.buildStandardJsonRequest(pageCustomConfig.getRequestUrl(), pageCustomConfig.getBeanClazz())
            .addParam("pageSize",""+PAGE_SIZE)
            .addParam("pageIndex",index+"")
            .addParams(pageCustomConfig.getParams());

            builder.postAsync(new MyNetListener<ArticleInfo>() {
                @Override
                public void onSuccess(ArticleInfo o, String s, boolean b) {

                }

                @Override
                public void onSuccessArr(List<ArticleInfo> response, String resonseStr, boolean isFromCache) {
                    super.onSuccessArr(response, resonseStr, isFromCache);
                    currentPageIndex = pageIndex;
                    if(pageIndex ==0){//第一次进入
                        currentPageIndex = 1;
                        pageManager.showContent();
                        adapter.replaceData(response);
                    }else if(pageIndex ==1){//刷新
                        currentPageIndex = 1;
                        adapter.replaceData(response);
                        setRefreshing(false);

                    }else {//加载更多
                        adapter.addData(response);
                        if(response.size()<PAGE_SIZE){
                            adapter.loadMoreEnd(false);
                        }else {
                            adapter.loadMoreComplete();
                        }
                    }


                }

                @Override
                public void onEmpty() {
                    super.onEmpty();
                    if(pageIndex>1){
                        adapter.loadMoreEnd(false);
                    }else {
                        pageManager.showEmpty();

                    }

                }

                @Override
                public void onError(String msgCanShow) {
                    if(pageIndex==0){
                        pageManager.showError();
                    }else if(pageIndex ==1){
                        setRefreshing(false);
                    }else {
                        adapter.loadMoreFail();
                    }
                }

                @Override
                public void onCodeError(String msgCanShow, String hiddenMsg, int code) {
                    super.onCodeError(msgCanShow, hiddenMsg, code);
                    if(pageIndex==0){
                        pageManager.showError();
                    }else if(pageIndex ==1){
                        setRefreshing(false);
                    }else {
                        adapter.loadMoreFail();
                    }

                }
            });



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
