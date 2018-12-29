package com.example.zuoyelianxi;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zuoyelianxi.adaper.SearchAdaper;
import com.example.zuoyelianxi.api.Apis;
import com.example.zuoyelianxi.bean.GreenBean;
import com.example.zuoyelianxi.bean.UserBean;
import com.example.zuoyelianxi.greendao.DaoMaster;
import com.example.zuoyelianxi.greendao.DaoSession;
import com.example.zuoyelianxi.greendao.GreenBeanDao;
import com.example.zuoyelianxi.presents.PresenterImpl;
import com.example.zuoyelianxi.util.OkHttpUtil;
import com.example.zuoyelianxi.view.Iview;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private GreenBeanDao greenBeanDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new PresenterImpl(this);
        initView();
    }
    //数据库
    private void initDB() {
        DaoMaster.DevOpenHelper helper  = new DaoMaster.DevOpenHelper(this,"greenDB");
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        greenBeanDao = daoSession.getGreenBeanDao();
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
        initDB();
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
        if(!OkHttpUtil.getIntance().hasNetWork(this)){
            List<UserBean.DataBean> dataBeans = new ArrayList<>();
            List<GreenBean> list = greenBeanDao.queryBuilder().list();
            for(int i = 0;i<list.size();i++){
                UserBean.DataBean dataBean = new UserBean.DataBean();
                dataBean.setPid((int) list.get(i).getPid());
                dataBean.setTitle(list.get(i).getTitle());
                dataBean.setPrice(list.get(i).getPrice());
                dataBean.setImages(list.get(i).getImages());
                dataBeans.add(dataBean);
            }
            adaper.setmData(dataBeans);
        }
        Map<String,String> map = new HashMap<>();
        map.put("keywords","手机");
        map.put("page",String.valueOf(mPage));
        presenter.startRequest(Apis.URL_SEARCH,map,UserBean.class);
    }

    @Override
    public void requestData(Object o) {
        if(o instanceof UserBean){
            UserBean bean = (UserBean) o;
            List<UserBean.DataBean> data = bean.getData();
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
                for(int i=0;i<data.size();i++){
                    GreenBean greenBean = new GreenBean();
                    greenBean.setPid(data.get(i).getPid());
                    greenBean.setTitle(data.get(i).getTitle());
                    greenBean.setPrice(data.get(i).getPrice());
                    greenBean.setImages(data.get(i).getImages());
                    greenBeanDao.insertOrReplace(greenBean);
                }
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
