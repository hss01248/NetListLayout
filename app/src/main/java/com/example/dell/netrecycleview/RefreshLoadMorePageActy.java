package com.example.dell.netrecycleview;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hss01248.netlist.loadmore.CustomLoadMoreView;
import com.example.dell.netrecycleview.qxinli.ArticleAdapter;
import com.example.dell.netrecycleview.qxinli.ArticleInfo;
import com.hss01248.net.wrapper.HttpUtil;
import com.hss01248.net.wrapper.MyNetListener;
import com.hss01248.pagestate.PageManager;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by huangshuisheng on 2017/9/29.
 */

public class RefreshLoadMorePageActy extends Activity {

    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout refreshLayout;

    List datas ;
    int currentPageIndex;
    int PAGE_SIZE = 10;
    PageManager pageManager;
    BaseQuickAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_refreshmore);
        ButterKnife.bind(this);
        recyclerview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.addItemDecoration(
            new HorizontalDividerItemDecoration.Builder(this)
                .color(Color.RED)
                .size(5)
                .build());
        currentPageIndex = 1;
        pageManager = PageManager.init(recyclerview, "empty", true, new Runnable() {
            @Override
            public void run() {
                getData(0);
            }
        });
        initAdapter();
        getData(0);

    }

    private void initAdapter() {
        adapter = new ArticleAdapter(R.layout.item_article,datas);
        recyclerview.setAdapter(adapter);
        adapter.setLoadMoreView(new CustomLoadMoreView());
        //adapter.setEmptyView(R.layout.pager_empty);

        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData(currentPageIndex+1);

            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        }
        int index = pageIndex>1 ? pageIndex :1;
        HttpUtil.buildStandardJsonRequest("article/getArticleList/v1.json", ArticleInfo.class)
            .addParam("categoryId","0")
            .addParam("pageSize",""+PAGE_SIZE)
            .addParam("pageIndex",index+"")
            .postAsync(new MyNetListener<ArticleInfo>() {
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
                        refreshLayout.setRefreshing(false);

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
                        refreshLayout.setRefreshing(false);
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
                        refreshLayout.setRefreshing(false);
                    }else {
                        adapter.loadMoreFail();
                    }

                }
            });



    }
}
