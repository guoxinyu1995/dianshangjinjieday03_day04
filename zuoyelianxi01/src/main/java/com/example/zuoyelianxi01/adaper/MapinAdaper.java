package com.example.zuoyelianxi01.adaper;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zuoyelianxi01.R;
import com.example.zuoyelianxi01.bean.SearchBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.Url;

public class MapinAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Boolean b;
    private Context mContext;
    private List<SearchBean.DataBean> mData;

    public MapinAdaper(Boolean b, Context mContext) {
        this.b = b;
        this.mContext = mContext;
        mData = new ArrayList<>();
    }
    public void setmData(List<SearchBean.DataBean> datas){
        mData.clear();
        if(datas!=null){
            mData.addAll(datas);
        }
        notifyDataSetChanged();
    }
    public List<SearchBean.DataBean> getData(){
        return mData;
    }
    public void addmData(List<SearchBean.DataBean> datas){
        if(datas!=null){
            mData.addAll(datas);
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder holder = null;
        if (b) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.linear_item, viewGroup, false);
            holder = new ViewHolderLinear(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.grid_item, viewGroup, false);
            holder = new ViewHolderGrid(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        SearchBean.DataBean dataBean = mData.get(i);
        String images = dataBean.getImages();
        String[] img = images.split("\\|");
        if (b) {
            ViewHolderLinear holderLinear = (ViewHolderLinear) viewHolder;
            holderLinear.title.setText(dataBean.getTitle());
            holderLinear.price.setText("价格:￥"+dataBean.getPrice());
            holderLinear.salenum.setText("销量:"+dataBean.getSalenum());
            Uri uri = Uri.parse(img[0]);
            holderLinear.image.setImageURI(uri);
            //点击
            holderLinear.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickCallBack!=null){
                        clickCallBack.callBack(mData.get(i).getPid());
                    }
                }
            });
        }else{
            ViewHolderGrid holderGrid = (ViewHolderGrid) viewHolder;
            holderGrid.title.setText(dataBean.getTitle());
            holderGrid.price.setText("价格:￥"+dataBean.getPrice());
            holderGrid.salenum.setText("销量:"+dataBean.getSalenum());
            Uri uri = Uri.parse(img[0]);
            holderGrid.image.setImageURI(uri);
            //点击
            holderGrid.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickCallBack!=null){
                        clickCallBack.callBack(mData.get(i).getPid());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolderLinear extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        SimpleDraweeView image;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.salenum)
        TextView salenum;
        @BindView(R.id.conlin)
        ConstraintLayout layout;
        public ViewHolderLinear(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    class ViewHolderGrid extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        SimpleDraweeView image;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.salenum)
        TextView salenum;
        @BindView(R.id.congrid)
        ConstraintLayout layout;
        public ViewHolderGrid(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    //定义接口
    private ClickCallBack clickCallBack;
    public void setClickCallBack(ClickCallBack clickCallBack){
        this.clickCallBack = clickCallBack;
    }
    public interface ClickCallBack{
        void callBack(int pid);
    }
}
