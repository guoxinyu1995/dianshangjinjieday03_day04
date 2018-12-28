package com.example.zuoyelianxi01.adaper;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.zuoyelianxi01.fragment.CommentFragment;
import com.example.zuoyelianxi01.fragment.ParticularsFragment;
import com.example.zuoyelianxi01.fragment.ShopFragment;

public class ViewPageAdaper extends FragmentPagerAdapter {
    String[] str = new String[]{
            "商品","详情","评论"
    };
    public ViewPageAdaper(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return new ShopFragment();
            case 1:
                return new ParticularsFragment();
            case 2:
                return new CommentFragment();
                default:
                    return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return str.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return str[position];
    }
}
