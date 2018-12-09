package com.foxconn.androidlib.widget;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.foxconn.androidlib.R;
import com.foxconn.androidlib.utils.DisplayUtil;

/**
 * @author hailong.feng
 * @description 加载框
 * @email hai-long.feng@mail.foxconn.com
 * @date 2018/12/6 10:29
 */
public class LoadDialog extends AppCompatDialogFragment {
    private TextView tv_msg;
    private String msg;
    Dialog dialog ;
    public static LoadDialog newInstance(String msg) {
        LoadDialog loadDialog = new LoadDialog();
        Bundle bundle = new Bundle();
        bundle.putString("msg", msg);
        loadDialog.setArguments(bundle);
        return loadDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dialog=getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
        View view = inflater.inflate(R.layout.dialog_loading, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_msg = view.findViewById(R.id.tv_msg);
        Bundle bundle = getArguments();
        this.msg = bundle.getString("msg");
        if (!TextUtils.isEmpty(this.msg)) {
            tv_msg.setText(this.msg);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (dialog != null) {
            dialog.getWindow().setLayout(DisplayUtil.dp2px(getContext(), 110), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
