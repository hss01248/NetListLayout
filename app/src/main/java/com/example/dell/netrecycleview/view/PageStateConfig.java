package com.example.dell.netrecycleview.view;

import android.support.annotation.IdRes;

import com.example.dell.netrecycleview.R;

/**
 * Created by huangshuisheng on 2017/9/30.
 */

public class PageStateConfig {

    public String msg_empty = "the content is empty";

    public boolean showLoadingFirstIn = true;

    public @IdRes int emptyLayoutId = R.layout.pager_empty;
    public @IdRes int loadingLayoutId = R.layout.pager_loading;
    public @IdRes int errorLayoutId = R.layout.pager_error;

}
