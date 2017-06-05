package com.example.wzh.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.wzh.beijingnews.activity.MainActivity;
import com.example.wzh.beijingnews.base.BasePager;
import com.example.wzh.beijingnews.base.MenuDetailBasePager;
import com.example.wzh.beijingnews.daomain.NewsCenterBean;
import com.example.wzh.beijingnews.detailpager.InteractMenuDetailPager;
import com.example.wzh.beijingnews.detailpager.NewsMenuDetailPager;
import com.example.wzh.beijingnews.detailpager.PhotosMenuDetailPager;
import com.example.wzh.beijingnews.detailpager.TopicMenuDetailPager;
import com.example.wzh.beijingnews.detailpager.VoteMenuDetailPager;
import com.example.wzh.beijingnews.fragment.LeftMenuFragment;
import com.example.wzh.beijingnews.utils.CacheUtils;
import com.example.wzh.beijingnews.utils.ConstantUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


public class NewsPager extends BasePager {
    private List<NewsCenterBean.DataBean> datas;

    public NewsPager(Context context) {
        super(context);
    }
    private List<MenuDetailBasePager> basePagers;


    @Override
    public void initData() {
        super.initData();
        //把数据绑定到视图上

        //设置标题
        tv_title.setText("新闻");
        ib_menu.setVisibility(View.VISIBLE);

        //创建子类的视图
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);

        //添加到布局上
        fl_content.addView(textView);
        String saveJson = CacheUtils.getString(context, ConstantUtils.NEWSCENTER_PAGER_URL);//
        if(!TextUtils.isEmpty(saveJson)){//当不是null,""
            processData(saveJson);
            Log.e("TAG","取出缓存的数据..=="+saveJson);
        }
        getDataFromNet();
    }
    private void getDataFromNet() {
        //新闻中心的网络路径
        String url = ConstantUtils.NEWSCENTER_PAGER_URL;
        OkHttpUtils
                .get()
                .url(url)
//                .addParams("username", "hyman")
//                .addParams("password", "123")
                .build()
                .execute(new StringCallback()
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG","请求失败=="+e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("TAG","请求成功=="+response);
                        CacheUtils.putString(context,ConstantUtils.NEWSCENTER_PAGER_URL,response);
                        processData(response);
                    }
                });
    }

    private void processData(String json) {

        NewsCenterBean newsCenterBean = parseJson(json);

       // NewsCenterBean newsCenterBean = new Gson().fromJson(json,NewsCenterBean.class);
//        Log.e("TAG","解析成功了哦=="+ newsCenterBean.getData().get(0).getChildren().get(0).getTitle());
        datas = newsCenterBean.getData();
        MainActivity mainActivity = (MainActivity) context;
        basePagers = new ArrayList<>();
        basePagers.add(new NewsMenuDetailPager(context));
        basePagers.add(new TopicMenuDetailPager(context));
        basePagers.add(new PhotosMenuDetailPager(context));
        basePagers.add(new InteractMenuDetailPager(context));
        basePagers.add(new VoteMenuDetailPager(context));

        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
        //设置数据
        leftMenuFragment.setData(datas);

    }
    List<NewsCenterBean.DataBean> data;
    private NewsCenterBean parseJson(String json) {

        NewsCenterBean newsCenterBean = new NewsCenterBean();
        try {
        JSONObject jsonObject = new JSONObject(json);
        int retcode = jsonObject.optInt("retcode");
        newsCenterBean.setRetcode(retcode);
        JSONArray jsonArray = jsonObject.optJSONArray("data");
        data = new ArrayList<>();
        newsCenterBean.setData(data);
        for(int i = 0; i < jsonArray.length(); i++) {
            NewsCenterBean.DataBean dataBean = new NewsCenterBean.DataBean();
            JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
            if(jsonObject1 != null) {

                int id = jsonObject1.optInt("id");
                dataBean.setId(id);
                String title = jsonObject1.optString("title");
                dataBean.setTitle(title);
                int type = jsonObject1.optInt("type");
                dataBean.setType(type);
                String url = jsonObject1.optString("url");
                dataBean.setUrl(url);

                JSONArray childrenData = jsonObject1.optJSONArray("children");
                if(childrenData != null && childrenData.length() > 0) {
                    List<NewsCenterBean.DataBean.ChildrenBean> children =  new ArrayList<>();
                    NewsCenterBean.DataBean dataBean1 = new NewsCenterBean.DataBean();
                    dataBean1.setChildren(children);
                    for(int j = 0; j < childrenData.length(); j++) {
                        JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
                        if(jsonObject2 != null) {
                            NewsCenterBean.DataBean.ChildrenBean childrenData1 = new NewsCenterBean.DataBean.ChildrenBean();
                            childrenData1.setId(jsonObject2.optInt("id"));
                            childrenData1.setTitle(jsonObject2.optString("title"));
                            childrenData1.setType(jsonObject2.optInt("type"));
                            childrenData1.setUrl(jsonObject2.optString("url"));
                            children.add(childrenData1);
                        }
                    }
                }
            }
            data.add(dataBean   );
        }
        } catch (JSONException e) {
        e.printStackTrace();
    }
        return newsCenterBean;
    }

    public void swichPager(int prePosition) {

        MenuDetailBasePager basePager = basePagers.get(prePosition);//NewsMenuDetailPager,TopicMenuDetailPager...
        View rootView = basePager.rootView;
        fl_content.removeAllViews();//把之前显示的给移除

        fl_content.addView(rootView);

        //调用InitData
        basePager.initData();

    }
}
