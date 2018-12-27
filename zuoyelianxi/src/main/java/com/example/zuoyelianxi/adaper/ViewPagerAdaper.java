package com.example.zuoyelianxi.adaper;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.example.zuoyelianxi.bean.ShowBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdaper extends PagerAdapter {
    private List<ShowBean.DataBean> mData;
    private Context mContext;

    public ViewPagerAdaper(Context mContext) {
        this.mContext = mContext;
        mData = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        SimpleDraweeView draweeView = new SimpleDraweeView(mContext);
        String url = mData.get(position).getImages().split("\\|")[0].replace("https","http");
        Uri uri = Uri.parse(url);
        draweeView.setImageURI(uri);
        return draweeView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
