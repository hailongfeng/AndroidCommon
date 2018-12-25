package com.foxconn.androidlib.widget.rccylerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    /**
     * TODO
     * single view may be direct construction, eg: TextView view = new TextView(context);
     *
     * @param parent current no use, may be future use
     * @param view
     */
    public BaseViewHolder(ViewGroup parent, View view,int viewType) {
        super(view);
        findViews(viewType);
    }


    /**
     * find all views
     */
    public abstract void findViews(int viewType);

    /**
     * bind view holder
     *
     * @param data
     */
    public abstract void onBindViewHolder(T data);

    /**
     * holder click enable
     *
     * @return
     */
    public boolean enable() {
        return true;
    }

}