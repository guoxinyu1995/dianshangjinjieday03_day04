package com.example.zuoyelianxi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zuoyelianxi.api.Apis;
import com.example.zuoyelianxi.bean.ShowBean;
import com.example.zuoyelianxi.presents.PresenterImpl;
import com.example.zuoyelianxi.view.Iview;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoaderInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShowActivity extends AppCompatActivity implements Iview {
    @BindView(R.id.show_image)
    SimpleDraweeView showImage;
    @BindView(R.id.show_banner)
    Banner showBanner;
    @BindView(R.id.show_title)
    TextView showTitle;
    @BindView(R.id.show_price)
    TextView showPrice;
    @BindView(R.id.show_bean)
    Button showBean;
    private PresenterImpl presenter;
    private List<String> image= new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        presenter = new PresenterImpl(this);
        ButterKnife.bind(this);
        initData();
    }


    @OnClick(R.id.show_bean)
    public void btnClick(){
        UMShareAPI umShareAPI = UMShareAPI.get(ShowActivity.this);
        umShareAPI.getPlatformInfo(ShowActivity.this, SHARE_MEDIA.QQ, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                //获取名字
                String name  = map.get("screen_name");
                //获取头像
                String img  = map.get("profile_image_url");
                Uri uri = Uri.parse(img);
                showImage.setImageURI(uri);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void initData() {
        Intent intent = getIntent();
        int pid = intent.getIntExtra("pid", 0);
        Map<String,String> map = new HashMap<>();
        map.put("pid",String.valueOf(pid));
        presenter.startRequest(Apis.URL_SHOW,map,ShowBean.class);
    }

    @Override
    public void requestData(Object o) {
        if(o instanceof ShowBean){
            ShowBean bean = (ShowBean) o;
            if(bean==null || !bean.isSuccess()){
                Toast.makeText(ShowActivity.this,bean.getMsg(),Toast.LENGTH_SHORT).show();
            }else{
                showTitle.setText(bean.getData().getTitle());
                showPrice.setText("价格:"+bean.getData().getPrice());
                showBanner.setBannerStyle(BannerConfig.NUM_INDICATOR);
                showBanner.setImageLoader(new ImageLoaderInterface<SimpleDraweeView>() {
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
                showBanner.setImages(image);
                showBanner.start();
            }

        }
    }
    public void sub(String url){
        //获取以“|”为截取的下标位置
        int i = url.indexOf("|");
        if (i>=0){
            String substring = url.substring(0, i);
            image.add(substring);
            sub(url.substring(i+1,url.length()));
        }else{
            image.add(url);
        }
    }
    @Override
    public void requestFail(Object o) {
        if(o instanceof Exception){
            Exception e = (Exception) o;
            e.printStackTrace();
        }
        Toast.makeText(ShowActivity.this,"网络请求失败",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}
