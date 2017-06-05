package com.example.wzh.beijingnews.base;

import android.content.Context;
import android.view.View;

/**
 * Created by WZH on 2017/6/3.
 */

public abstract class MenuDetailBasePager {
    public View rootView;

    public final Context context;

    public MenuDetailBasePager(Context context) {
        this.context = context;
        rootView = initView();
    }

    public abstract View initView();

    public void initData(){

    }


}
