package com.example.zuoyelianxi.adaper;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zuoyelianxi.R;
import com.example.zuoyelianxi.bean.UserBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UserBean.DataBean> mData;
    private Context mContext;

    public SearchAdaper(Context mContext) {
        this.mContext = mContext;
        mData = new ArrayList<>();
    }
    public void setmData(List<UserBean.DataBean> datas){
        mData.clear();
        if(datas!=null){
            mData.addAll(datas);
        }
        notifyDataSetChanged();
    }
    public void addmData(List<UserBean.DataBean> datas){
        if(datas!=null){
            mData.addAll(datas);
        }
        notifyDataSetChanged();
    }
    public List<UserBean.DataBean> getDaata(){
        return mData;
    }

    public void delData(int i){
        mData.remove(i);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycle_item, viewGroup, false);
        ViewHolderSearch holderSearch = new ViewHolderSearch(view);
        return holderSearch;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolderSearch holderSearch = (ViewHolderSearch) viewHolder;
        holderSearch.title.setText(mData.get(i).getTitle());
        holderSearch.price.setText("价格:" + mData.get(i).getPrice());
        String url = mData.get(i).getImages().split("\\|")[0].replace("https","http");
        Uri uri = Uri.parse(url);
        holderSearch.simple.setImageURI(uri);
        //点击
        holderSearch.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickCallBack!=null){
                    onClickCallBack.clickCallBack(mData.get(i).getPid());
                }
            }
        });
        //长按
        holderSearch.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onLongCallBack!=null){
                    onLongCallBack.longCallBack(i);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolderSearch extends RecyclerView.ViewHolder {
        @BindView(R.id.simple)
        public SimpleDraweeView simple;
        @BindView(R.id.title)
        public TextView title;
        @BindView(R.id.price)
        public TextView price;
        @BindView(R.id.con)
        public ConstraintLayout layout;
        public ViewHolderSearch(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    //点击
    private OnClickCallBack onClickCallBack;
    public void setClickCallBack(OnClickCallBack onClickCallBack){
        this.onClickCallBack = onClickCallBack;
    }
    //定义接口
    public interface OnClickCallBack{
        void clickCallBack(int pid);
    }
    //长按
    private OnLongCallBack onLongCallBack;
    public void setOnLongCallBack(OnLongCallBack onLongCallBack){
        this.onLongCallBack = onLongCallBack;
    }
    public interface OnLongCallBack{
        void longCallBack(int i);
    }
}
