package com.example.dell.netrecycleview.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.dell.netrecycleview.qxinli.ArticleInfo;
import com.hss01248.net.builder.StandardJsonRequestBuilder;
import com.hss01248.net.wrapper.HttpUtil;
import com.hss01248.net.wrapper.MyNetListener;
import com.hss01248.pagestate.PageListener;
import com.hss01248.pagestate.PageManager;

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
        if(pageStateConfig ==null){
            pageStateConfig = new PageStateConfig();
        }
        if(recyclerViewConfig == null){
            recyclerViewConfig = new RecyclerViewConfig(getContext());
        }
        if(refreshConfig == null){
            refreshConfig = new RefreshConfig();
        }
        if(loadMoreConfig ==null){
            loadMoreConfig = new LoadMoreConfig();
        }

        //recycleview
        recyclerView.setLayoutManager(recyclerViewConfig.layoutManager);
        recyclerView.setItemAnimator(recyclerViewConfig.itemAnimator);
        recyclerView.addItemDecoration(recyclerViewConfig.itemDecoration);
        currentPageIndex = 0;


        //refresh下拉颜色-该api会导致页面加载状态失常
        //setColorSchemeResources(refreshConfig.colorIntRes);


        //页面状态管理  todo 页面内自定义api的实现
        pageManager = PageManager.init(recyclerView, pageStateConfig.msg_empty, pageStateConfig.showLoadingFirstIn, new Runnable() {
            @Override
            public void run() {
                getData(0);
            }
        });
        // 后面再设置会导致前面retryevent失效
        /*pageManager.mLoadingAndRetryLayout.setLoadingView(pageStateConfig.loadingLayoutId);
        pageManager.mLoadingAndRetryLayout.setEmptyView(pageStateConfig.emptyLayoutId);
        pageManager.mLoadingAndRetryLayout.setRetryView(pageStateConfig.errorLayoutId);*/


        //initPageManager();


        initAdapter();


        getData(0);

    }

    private void initPageManager() {

        /*pageManager.mLoadingAndRetryLayout.setLoadingView(pageStateConfig.loadingLayoutId);
        pageManager.mLoadingAndRetryLayout.setEmptyView(pageStateConfig.emptyLayoutId);
        pageManager.mLoadingAndRetryLayout.setRetryView(pageStateConfig.errorLayoutId);*/
        pageManager = PageManager.generate(recyclerView, new PageListener() {
            public void setRetryEvent(View retryView) {
                if(retryView==null){
                    return;
                }
                retryView.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if(!PageManager.isNetWorkAvailable(recyclerView.getContext())) {
                            PageManager.showNoNetWorkDlg(recyclerView);
                        } else {
                            getData(0);
                        }

                    }
                });
            }
/*
            @Override
            public int generateRetryLayoutId() {
                return pageStateConfig.errorLayoutId;
            }

            @Override
            public int generateLoadingLayoutId() {
                return pageStateConfig.loadingLayoutId;
            }

            @Override
            public int generateEmptyLayoutId() {
                return pageStateConfig.emptyLayoutId;
            }*/

            /*public View generateEmptyLayout() {
                return PageManager.generateCustomEmptyView(emptyMsg);
            }*/
        });

        pageManager.mLoadingAndRetryLayout.setLoadingView(pageStateConfig.loadingLayoutId);
        pageManager.mLoadingAndRetryLayout.setEmptyView(pageStateConfig.emptyLayoutId);
        pageManager.mLoadingAndRetryLayout.setRetryView(pageStateConfig.errorLayoutId);

        if(pageStateConfig.showLoadingFirstIn) {
            pageManager.showLoading();
        } else {
            pageManager.showContent();
        }
    }

    private void initAdapter() {
        adapter = pageCustomConfig.adapter;
        recyclerView.setAdapter(adapter);
        adapter.setLoadMoreView(loadMoreConfig.loadMoreView);
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
            .addParam(loadMoreConfig.key_pageSize,""+PAGE_SIZE)
            .addParam(loadMoreConfig.key_pageIndex,index+"")
            .addParams(pageCustomConfig.getParams());

            builder.postAsync(new MyNetListener<ArticleInfo>() {
                @Override
                public void onSuccess(ArticleInfo o, String s, boolean b) {

                }

                @Override
                public void onSuccessArr(List<ArticleInfo> response, String resonseStr, boolean isFromCache) {
                    super.onSuccessArr(response, resonseStr, isFromCache);

                    if(pageIndex ==0){//第一次进入
                        currentPageIndex = 1;
                        pageManager.showContent();
                        adapter.replaceData(response);
                    }else if(pageIndex ==1){//刷新
                        currentPageIndex = 1;
                        adapter.replaceData(response);
                        //pageManager.showContent();
                        setRefreshing(false);

                    }else {//加载更多
                        currentPageIndex = pageIndex;
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

    public LoadMoreRecyclerLayout setRefreshConfig(RefreshConfig refreshConfig){
        this.refreshConfig = refreshConfig;
        return this;
    }
    public LoadMoreRecyclerLayout setLoadMoreConfig(LoadMoreConfig loadMoreConfig){
        this.loadMoreConfig = loadMoreConfig;
        return this;
    }
    public LoadMoreRecyclerLayout setPageStateConfig(PageStateConfig pageStateConfig){
        this.pageStateConfig = pageStateConfig;
        return this;
    }
    public LoadMoreRecyclerLayout setRecyclerViewConfig(RecyclerViewConfig recyclerViewConfig){
        this.recyclerViewConfig = recyclerViewConfig;
        return this;
    }
}
