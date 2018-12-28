package com.example.zuoyelianxi01.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.zuoyelianxi01.R;
import com.example.zuoyelianxi01.adaper.ViewPageAdaper;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowActivity extends AppCompatActivity {
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private ViewPageAdaper adaper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        viewpager.setOffscreenPageLimit(3);
        adaper = new ViewPageAdaper(getSupportFragmentManager());
        viewpager.setAdapter(adaper);
        tab.setupWithViewPager(viewpager);
    }
    //跳转方法
    public void setCurren(int i){
        viewpager.setCurrentItem(i);
    }

}
