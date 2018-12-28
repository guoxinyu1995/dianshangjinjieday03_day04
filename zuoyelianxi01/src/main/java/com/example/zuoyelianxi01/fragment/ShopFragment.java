package com.example.zuoyelianxi01.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zuoyelianxi01.R;
import com.example.zuoyelianxi01.activity.ShowActivity;
import com.example.zuoyelianxi01.api.Apis;
import com.example.zuoyelianxi01.bean.EventBean;
import com.example.zuoyelianxi01.bean.ShowBean;
import com.example.zuoyelianxi01.presents.PresenterImpl;
import com.example.zuoyelianxi01.view.Iview;
import com.facebook.drawee.view.SimpleDraweeView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoaderInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ShopFragment extends Fragment implements Iview {
    @BindView(R.id.banners)
    Banner banners;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.price)
    TextView price;
    Unbinder unbinder;
    private PresenterImpl presenter;
    private List<String> image = new ArrayList<>();
    private ShowBean.DataBean data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shop_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new PresenterImpl(this);
        Intent intent = getActivity().getIntent();
        int pid = intent.getIntExtra("pid", 0);
        Map<String, String> map = new HashMap<>();
        map.put("pid", String.valueOf(pid));
        presenter.startRequest(Apis.URL_SHOW, map, ShowBean.class);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        presenter.onDetach();
    }

    @Override
    public void requestData(Object o) {
        if (o instanceof ShowBean) {
            ShowBean bean = (ShowBean) o;
            data = bean.getData();
            if (bean == null || !bean.isSuccess()) {
                Toast.makeText(getActivity(), bean.getMsg(), Toast.LENGTH_SHORT).show();
            } else {
                banners.setBannerStyle(BannerConfig.NUM_INDICATOR);
                banners.setImageLoader(new ImageLoaderInterface<SimpleDraweeView>() {

                    @Override
                    public void displayImage(Context context, Object path, SimpleDraweeView imageView) {
                        Uri uri = Uri.parse((String) path);
                        imageView.setImageURI(uri);
                    }

                    @Override
                    public SimpleDraweeView createImageView(Context context) {
                        SimpleDraweeView draweeView = new SimpleDraweeView(context);
                        draweeView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        return draweeView;
                    }
                });
                sub(bean.getData().getImages());
                banners.setImages(image);
                banners.start();
                title.setText(bean.getData().getTitle());
                price.setText("价格:￥" + bean.getData().getPrice());
            }
        }
    }

    public void sub(String url) {
        //获取以“|”为截取的下标位置
        int i = url.indexOf("|");
        if (i >= 0) {
            String substring = url.substring(0, i);
            image.add(substring);
            sub(url.substring(i + 1, url.length()));
        } else {
            image.add(url);
        }
    }

    @Override
    public void requestFail(Object o) {
        if (o instanceof Exception) {
            Exception e = (Exception) o;
            e.printStackTrace();
        }
        Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.title, R.id.price})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title:
               //EventBus.getDefault().postSticky(new EventBean(1,data.getTitle()));
                EventBus.getDefault().postSticky(data.getTitle());
                ((ShowActivity)getActivity()).setCurren(1);
                break;
            case R.id.price:
                //EventBus.getDefault().postSticky(new EventBean(2,data.getPrice()+""));
                EventBus.getDefault().postSticky(data.getPrice());
                ((ShowActivity)getActivity()).setCurren(2);
                break;
                default:
                    break;
        }
    }

}
