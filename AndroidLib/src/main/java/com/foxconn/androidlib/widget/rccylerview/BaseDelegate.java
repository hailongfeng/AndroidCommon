package com.foxconn.androidlib.widget.rccylerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseDelegate<T> {

    /**
     * crate view holder by view type
     *
     * @param parent
     * @param viewType
     * @return
     */
    public abstract BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    /**
     * get view type by data
     *
     * @param data
     * @return
     */
    public abstract int getItemViewType(T data);

    /**
     * get layout id by view type
     *
     * @param viewType
     * @return
     */
    public abstract int getLayoutId(int viewType);

    /**
     * get item view
     *
     * @param parent
     * @param viewType
     * @return
     */
    public View getItemView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false);
    }
}