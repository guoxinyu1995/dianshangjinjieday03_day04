package com.example.zuoyelianxi01.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zuoyelianxi01.R;
import com.example.zuoyelianxi01.bean.EventBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CommentFragment extends Fragment {
    @BindView(R.id.comment)
    TextView comment;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comment_fragment, container, false);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        unbinder = ButterKnife.bind(this, view);
        return view;
    }
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void getEvent(Object o) {
        if (o instanceof Double) {
            Double s = (Double) o;
            comment.setText(s+"");
        }
       /* if (o instanceof EventBean) {
            EventBean bean = (EventBean) o;
            if(bean.getNum() == 2){
                comment.setText(bean.getName());
            }

        }*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }
}
