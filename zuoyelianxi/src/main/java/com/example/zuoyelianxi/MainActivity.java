package com.example.zuoyelianxi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zuoyelianxi.adaper.SearchAdaper;
import com.example.zuoyelianxi.api.Apis;
import com.example.zuoyelianxi.bean.UserBean;
import com.example.zuoyelianxi.presents.PresenterImpl;
import com.example.zuoyelianxi.view.Iview;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements Iview {

    @BindView(R.id.text_map)
    TextView textMap;
    @BindView(R.id.recycle)
    XRecyclerView xRecyclerView;
    private PresenterImpl presenter;
    private SearchAdaper adaper;
    private int mPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new PresenterImpl(this);
        initView();
    }

    private void initView() {
        mPage = 1;
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        xRecyclerView.setLayoutManager(layoutManager);
        adaper = new SearchAdaper(this);
        xRecyclerView.setAdapter(adaper);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                initData();
            }

            @Override
            public void onLoadMore() {
                initData();
            }
        });
        initData();
        adaper.setClickCallBack(new SearchAdaper.OnClickCallBack() {
            @Override
            public void clickCallBack(int pid) {
                Intent intent = new Intent(MainActivity.this,ShowActivity.class);
                intent.putExtra("pid",pid);
                startActivity(intent);
            }
        });
        //长按删除
        adaper.setOnLongCallBack(new SearchAdaper.OnLongCallBack() {
            @Override
            public void longCallBack(int i) {
                adaper.delData(i);
            }
        });
    }

    private void initData() {
        Map<String,String> map = new HashMap<>();
        map.put("keywords","手机");
        map.put("page",String.valueOf(mPage));
        presenter.startRequest(Apis.URL_SEARCH,map,UserBean.class);
    }

    @Override
    public void requestData(Object o) {
        if(o instanceof UserBean){
            UserBean bean = (UserBean) o;
            if(bean==null || !bean.isSuccess()){
                Toast.makeText(MainActivity.this,bean.getMsg(),Toast.LENGTH_SHORT).show();
            }else{
                if(mPage == 1){
                    adaper.setmData(bean.getData());
                }else{
                    adaper.addmData(bean.getData());
                }
                mPage++;
                xRecyclerView.loadMoreComplete();
                xRecyclerView.refreshComplete();
            }
        }
    }

    @Override
    public void requestFail(Object o) {
        if(o instanceof Exception){
            Exception e = (Exception) o;
            e.printStackTrace();
        }
        Toast.makeText(MainActivity.this,"网络请求错误",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}
