package com.example.wzh.beijingnews.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.wzh.beijingnews.R;
import com.example.wzh.beijingnews.utils.DensityUtil;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class GuideActivity extends AppCompatActivity {

    @InjectView(R.id.vp)
    ViewPager vp;
    @InjectView(R.id.btn_start_main)
    Button btnStartMain;
    @InjectView(R.id.ll_point_group)
    LinearLayout llPointGroup;
    @InjectView(R.id.activity_guide)
    RelativeLayout activityGuide;
    @InjectView(R.id.iv_red_point)
    ImageView ivRedPoint;
    private int leftMargin;
    private ArrayList<ImageView> imageViews;
    private int[] ids = {R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.inject(this);
        initData();
        vp.setAdapter(new MyPagerAdapter());
        vp.addOnPageChangeListener(new MyOnPageChangeListener());
//设置监听ViewPager滑动的位置变化
        vp.addOnPageChangeListener(new MyOnPageChangeListener());

        //计算两个点的间距
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //取消监听
                ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                // 间距 = 第1个点距离左边的距离 - 第0个点距离左边的距离
                leftMargin = llPointGroup.getChildAt(1).getLeft() - llPointGroup.getChildAt(0).getLeft();
                Log.e("TAG", "leftMargin==" + leftMargin);

            }
        });

    }
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * 当滑到的时候回调
         *
         * @param position             当前滑动的页面的下标位置
         * @param positionOffset       百分比
         * @param positionOffsetPixels 滑动的像数
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            //红点移动的距离： 间距 = ViewPager滑动的百分比
            //红点移动的距离 = 间距*ViewPager滑动的百分比
            float left = leftMargin *(positionOffset + position) ;

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
            params.leftMargin = (int) left;
            ivRedPoint.setLayoutParams(params);
            Log.e("TAG","position=="+position+",positionOffset=="+positionOffset+",positionOffsetPixels=="+positionOffsetPixels);


        }

        /**
         * 当选中某个页面的时候回调
         *
         * @param position
         */
        @Override
        public void onPageSelected(int position) {
            if (position == imageViews.size() - 1) {
                //最后一个页面就显示
                btnStartMain.setVisibility(View.VISIBLE);
            } else {
                //其他页面隐藏
                btnStartMain.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void initData() {
        imageViews = new ArrayList<>();
        for (int i=0;i<ids.length;i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(ids[i]);
            imageViews.add(imageView);
            //添加三个灰色的点
            ImageView point = new ImageView(this);
            point.setImageResource(R.drawable.guide_point_normal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(GuideActivity.this,10), DensityUtil.dip2px(GuideActivity.this,10));
            point.setLayoutParams(params);
            if (i != 0) {
                params.leftMargin = DensityUtil.dip2px(GuideActivity.this,10);
            }
            //添加到线性布局
            llPointGroup.addView(point);
        }


    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
            container.addView(imageView);
            return imageView;
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view ==object;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @OnClick(R.id.btn_start_main)
    public void onViewClicked() {

    }
}
