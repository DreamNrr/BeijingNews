package com.example.wzh.beijingnews.fragment;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.wzh.beijingnews.R;
import com.example.wzh.beijingnews.activity.MainActivity;
import com.example.wzh.beijingnews.base.BaseFragment;
import com.example.wzh.beijingnews.base.BasePager;
import com.example.wzh.beijingnews.pager.HomePager;
import com.example.wzh.beijingnews.pager.NewsPager;
import com.example.wzh.beijingnews.pager.SettingPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by WZH on 2017/6/2.
 */

public class ContentFragment extends BaseFragment {
    @InjectView(R.id.vp)
    ViewPager vp;
    @InjectView(R.id.rb_home)
    RadioButton rbHome;
    @InjectView(R.id.rb_news)
    RadioButton rbNews;
    @InjectView(R.id.rb_setting)
    RadioButton rbSetting;
    @InjectView(R.id.rg_main)
    RadioGroup rgMain;
    private TextView textView;
    private ArrayList<BasePager> pagers;

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.fragment_content, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        //设ViewPager的数据-适配器
        //准备数据
        pagers = new ArrayList<>();
        pagers.add(new HomePager(context));//主页面
        pagers.add(new NewsPager(context));//新闻中心
        pagers.add(new SettingPager(context));//设置中心

        vp.setAdapter(new MyAdapter());
        //设置RadioGroup的监听
        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_home:
                        vp.setCurrentItem(0,false);
                        break;
                    case R.id.rb_news:
                        vp.setCurrentItem(1,false);
                        break;
                    case R.id.rb_setting:
                        vp.setCurrentItem(2,false);
                        break;
                }
            }
        });
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pagers.get(position).initData();
                if(position == 1) {
                    isEnableSlidingMenu(context, SlidingMenu.TOUCHMODE_FULLSCREEN);
                }else {
                    isEnableSlidingMenu(context, SlidingMenu.TOUCHMODE_NONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pagers.get(0).initData();
        rgMain.check(R.id.rb_home);
        isEnableSlidingMenu( context, SlidingMenu.TOUCHMODE_NONE);

    }

    private static void isEnableSlidingMenu(Context context,int touchmodeFullscreen) {
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.getSlidingMenu().setTouchModeAbove(touchmodeFullscreen);
    }

    public NewsPager getNewsPager() {
        return (NewsPager) pagers.get(1);
    }

    class MyAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = pagers.get(position);//HomePager,NewsPager,SettingPager
            View rootView = basePager.rootView;
            //调用initData方法
          //  basePager.initData();//HomePager,NewsPager,SettingPager
            container.addView(rootView);
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view ==object;
        }

        @Override
        public int getCount() {
            return pagers.size();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.rg_main)
    public void onViewClicked() {
    }
}
