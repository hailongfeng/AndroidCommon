package com.foxconn.androidlib.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DemoFragment extends Fragment {

    private RecyclerView rvMessageList;


    /**
     * 静态工厂方法需要一个int型的值来初始化fragment的参数，
     * 然后返回新的fragment到调用者
     */
    public static DemoFragment newInstance(String name) {
        DemoFragment f = new DemoFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        TextView view =new TextView(getActivity());
        view.setText(getArguments().getString("name"));
        return view;
    }


}
