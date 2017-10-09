package com.hss01248.netlist.view;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hss01248.net.util.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangshuisheng on 2017/9/30.
 */

public class PageCustomConfig {

    public String requestUrl;
    public Map<String,String> params ;
    public Class beanClazz;
    public BaseQuickAdapter adapter;
    //RecyclerView.LayoutManager layoutManager;


    public String getRequestUrl() {
        return requestUrl;
    }



    public Map<String, String> getParams() {
        return params;
    }



    public Class getBeanClazz() {
        return beanClazz;
    }



    public BaseQuickAdapter getAdapter() {
        return adapter;
    }


    private PageCustomConfig(Builder builder) {
        requestUrl = builder.requestUrl;
        params = builder.params;
        beanClazz = builder.beanClazz;
        adapter = builder.adapter;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(PageCustomConfig copy) {
        Builder builder = new Builder();
        builder.requestUrl = copy.getRequestUrl();
        builder.params = copy.getParams();
        builder.beanClazz = copy.getBeanClazz();
        builder.adapter = copy.getAdapter();
        return builder;
    }


    public static final class Builder {
        private String requestUrl;
        private Map<String, String> params;
        private Class beanClazz;
        private BaseQuickAdapter adapter;

        private Builder() {
        }

        public Builder requestUrl(String val) {
            requestUrl = val;
            return this;
        }

        public Builder addParam(String key,String val) {
            if(params == null){
                params = new HashMap<>();
            }
            if(TextUtils.isEmpty(key) || val==null){
                throw new RuntimeException("key or value in http can not be null") ;
            }
            params.put(key,val);
            return this;
        }

        public Builder beanClazz(Class val) {
            beanClazz = val;
            return this;
        }

        public Builder adapter(BaseQuickAdapter val) {
            adapter = val;
            return this;
        }

        public PageCustomConfig build() {
            return new PageCustomConfig(this);
        }
    }
}
