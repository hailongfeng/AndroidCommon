package com.foxconn.androidlib.widget.rccylerview;

import android.view.View;

public interface OnItemClickListener<T> {
    void onClick(View v, T data, int type);

    boolean onLongClick(View v, T data, int type);
}