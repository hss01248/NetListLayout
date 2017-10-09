package com.example.dell.netrecycleview.acty;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.dell.netrecycleview.R;
import com.example.dell.netrecycleview.qxinli.ArticleAdapter;
import com.example.dell.netrecycleview.qxinli.ArticleInfo;
import com.example.dell.netrecycleview.view.LoadMoreRecyclerLayout;
import com.example.dell.netrecycleview.view.PageCustomConfig;

import java.util.ArrayList;

/**
 * Created by huangshuisheng on 2017/10/9.
 */

public class TestLoadMoreLayoutActy extends Activity {

    LoadMoreRecyclerLayout recyclerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_loadmore_test);
        recyclerLayout = (LoadMoreRecyclerLayout) findViewById(R.id.loadmore_layout);

        PageCustomConfig pageCustomConfig = PageCustomConfig.newBuilder()
            .adapter(new ArticleAdapter(R.layout.item_article,new ArrayList<ArticleInfo>()))
            .beanClazz(ArticleInfo.class)
            .requestUrl("article/getArticleList/v1.json")
            .addParam("categoryId","0").build();

        recyclerLayout.setPageCustomConfig(pageCustomConfig);
        recyclerLayout.start();
    }
}
