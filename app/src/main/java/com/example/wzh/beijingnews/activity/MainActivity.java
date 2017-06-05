package com.example.wzh.beijingnews.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.wzh.beijingnews.R;
import com.example.wzh.beijingnews.fragment.ContentFragment;
import com.example.wzh.beijingnews.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {
    public static final String LEFT_TAG = "left_tag";
    public static final String MAIN_TAG = "main_tag";
    private LeftMenuFragment leftMenuFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSlidingMenu();
        initFragment();

    }
    private void initFragment() {
        //1.得到FragmentManger
        FragmentManager fm = getSupportFragmentManager();
        //2.开启事务
        FragmentTransaction ft = fm.beginTransaction();
        //3.替换两个Fragment
        ft.replace(R.id.fl_left,new LeftMenuFragment(), LEFT_TAG);
        ft.replace(R.id.activity_main,new ContentFragment(), MAIN_TAG);
        //4.提交事务
        ft.commit();

    }

    private void initSlidingMenu() {
        //设置左侧菜单
        setBehindContentView(R.layout.left_menu);
        SlidingMenu slidingMenu = getSlidingMenu();
        //设置模式：左侧+主页；左侧+主页+右侧；主页+右侧
        slidingMenu.setMode(SlidingMenu.LEFT);
        //设置滑动模式：不可用滑动，滑动编边缘，全屏滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //设置主页面占的宽度
        slidingMenu.setBehindOffset(400);
    }

    public LeftMenuFragment getLeftMenuFragment() {
        return (LeftMenuFragment) getSupportFragmentManager().findFragmentByTag(LEFT_TAG);
    }

    public ContentFragment getContentFragment() {
        return (ContentFragment) getSupportFragmentManager().findFragmentByTag(MAIN_TAG);
    }
}
