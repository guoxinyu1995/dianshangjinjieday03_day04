package com.example.zuoyelianxi01.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zuoyelianxi01.R;
import com.example.zuoyelianxi01.adaper.MapinAdaper;
import com.example.zuoyelianxi01.api.Apis;
import com.example.zuoyelianxi01.bean.SearchBean;
import com.example.zuoyelianxi01.presents.PresenterImpl;
import com.example.zuoyelianxi01.view.Iview;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements Iview {

    @BindView(R.id.image_return)
    ImageView imageReturn;
    @BindView(R.id.ed)
    EditText ed;
    @BindView(R.id.image_search)
    ImageView imageSearch;
    @BindView(R.id.image_switch)
    ImageView imageSwitch;
    @BindView(R.id.up)
    TextView up;
    @BindView(R.id.synthesize)
    TextView synthesize;
    @BindView(R.id.screen)
    TextView screen;
    @BindView(R.id.sales)
    TextView sales;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.down)
    TextView down;
    @BindView(R.id.x_recycle)
    XRecyclerView xRecycle;
    private PresenterImpl presenter;
    private int mPage;
    private int mSort = 0;
    private boolean b = true;
    private MapinAdaper adaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new PresenterImpl(this);

        changeRecycleView();
        initData();
    }

    //初始化RecycleView
    private void initRecycleView() {
        mPage = 1;
        xRecycle.setPullRefreshEnabled(true);
        xRecycle.setLoadingMoreEnabled(true);
        xRecycle.setLoadingListener(new XRecyclerView.LoadingListener() {
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
    }
    //切换布局
    private void changeRecycleView() {
        if(b){
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(OrientationHelper.VERTICAL);
            xRecycle.setLayoutManager(layoutManager);
            DividerItemDecoration decoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
            xRecycle.addItemDecoration(decoration);
        }else{
            GridLayoutManager layoutManager = new GridLayoutManager(this,2);
            layoutManager.setOrientation(OrientationHelper.VERTICAL);
            xRecycle.setLayoutManager(layoutManager);
        }
        adaper = new MapinAdaper(b,this);
        adaper.setClickCallBack(new MapinAdaper.ClickCallBack() {
            @Override
            public void callBack(int pid) {
                Intent intent = new Intent(MainActivity.this,ShowActivity.class);
                intent.putExtra("pid",pid);
                startActivity(intent);
            }
        });
        xRecycle.setAdapter(adaper);
        b=!b;
        initRecycleView();
    }

    private void initData() {
        Map<String,String> map = new HashMap<>();
        map.put("keywords",ed.getText().toString().trim());
        map.put("page",String.valueOf(mPage));
        map.put("sort",String.valueOf(mSort));
        presenter.startRequest(Apis.URL_SEARCH,map,SearchBean.class);
    }


    @Override
    public void requestData(Object o) {
        if(o instanceof SearchBean){
            SearchBean bean = (SearchBean) o;
            if(bean == null || !bean.isSuccess()){
                Toast.makeText(MainActivity.this,bean.getMsg(),Toast.LENGTH_SHORT).show();
            }else{
                if(mPage == 1){
                    adaper.setmData(bean.getData());
                }else{
                    adaper.addmData(bean.getData());
                }
                mPage++;
                xRecycle.refreshComplete();
                xRecycle.loadMoreComplete();
            }
        }
    }

    @Override
    public void requestFail(Object o) {
        if(o instanceof Exception){
            Exception e = (Exception) o;
            e.printStackTrace();
        }
        Toast.makeText(MainActivity.this,"网络请求失败",Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.image_search, R.id.image_switch, R.id.synthesize, R.id.screen, R.id.sales, R.id.price})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_search:
                Toast.makeText(MainActivity.this,"123456",Toast.LENGTH_SHORT).show();
                initRecycleView();
                initData();
                break;
            case R.id.image_switch:
                //切换按钮
                if(b){
                    imageSwitch.setImageResource(R.drawable.ic_action_name);
                }else{
                    imageSwitch.setImageResource(R.drawable.ic_grid_name);
                }
                List<SearchBean.DataBean> data = adaper.getData();
                changeRecycleView();
                adaper.setmData(data);
                break;
            case R.id.synthesize:
                if(!synthesize.isSelected()){
                    //切换时一定要从第一页获取数据，不然会造成新老数据在一起，导致排序出现问题
                    mPage = 1;
                    mSort = 0;
                    initData();
                    synthesize.setSelected(true);
                    sales.setSelected(false);
                    price.setSelected(false);
                }
                break;
            case R.id.screen:
                break;
            case R.id.sales:
                if(!sales.isSelected()){
                    mPage = 1;
                    mSort = 1;
                    initData();
                    sales.setSelected(true);
                    synthesize.setSelected(false);
                    price.setSelected(false);
                }
                break;
            case R.id.price:
                if(!price.isSelected()){
                    mPage = 1;
                    mSort = 2;
                    initData();
                    price.setSelected(true);
                    sales.setSelected(false);
                    synthesize.setSelected(false);
                }
                break;
                default:
                    break;
        }
    }
}
