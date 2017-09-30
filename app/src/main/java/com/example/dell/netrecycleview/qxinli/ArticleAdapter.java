package com.example.dell.netrecycleview.qxinli;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.dell.netrecycleview.R;

import java.util.List;

/**
 * Created by huangshuisheng on 2017/9/29.
 */

public class ArticleAdapter extends BaseQuickAdapter<ArticleInfo,BaseViewHolder> {
    public ArticleAdapter(@LayoutRes int layoutResId, @Nullable List<ArticleInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ArticleInfo item) {
        helper.setText(R.id.tv_title, item.title);
       helper.setText(R.id.tv_author,item.cover);
    }
}
