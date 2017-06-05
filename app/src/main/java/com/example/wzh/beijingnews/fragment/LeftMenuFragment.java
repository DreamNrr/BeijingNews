package com.example.wzh.beijingnews.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wzh.beijingnews.R;
import com.example.wzh.beijingnews.activity.MainActivity;
import com.example.wzh.beijingnews.base.BaseFragment;
import com.example.wzh.beijingnews.daomain.NewsCenterBean;
import com.example.wzh.beijingnews.pager.NewsPager;

import java.util.List;

/**
 * Created by WZH on 2017/6/2.
 */

public class LeftMenuFragment extends BaseFragment {
    private ListView listView;
    private List<NewsCenterBean.DataBean> datas;
    private LeftMenuAdapter adapter;
    private int prePosition = 0;
    @Override
    public View initView() {
        listView = new ListView(context);
        listView.setPadding(0,40,0,0);
       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
               prePosition = position;
               adapter.notifyDataSetChanged();
               MainActivity mainActivity = (MainActivity) context;
               //左菜单缩回去了
               mainActivity.getSlidingMenu().toggle();

               switchPager(prePosition);

           }
       });
        return listView;
    }

    private void switchPager(int position) {
        MainActivity mainActivity = (MainActivity) context;
        ContentFragment contentFragment = mainActivity.getContentFragment();
        NewsPager newsPager = contentFragment.getNewsPager();
        newsPager.swichPager(position);
    }

    @Override
    public void initData() {
        super.initData();
    }
    public void setData(List<NewsCenterBean.DataBean> datas) {
        this.datas = datas;
        adapter = new LeftMenuAdapter();
        listView.setAdapter(adapter);

        switchPager(prePosition);

    }
    class LeftMenuAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return datas == null ? 0 : datas.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = (TextView) View.inflate(context, R.layout.item_leftmenu, null);
            if(prePosition==position){
                textView.setEnabled(true);
            }else{
                textView.setEnabled(false);
            }
            NewsCenterBean.DataBean dataBean = datas.get(position);
            textView.setText(dataBean.getTitle());
            return textView;
        }
    }
}
