package com.foxconn.androidlib.widget.rccylerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.HashMap;

/**
 * @author hailong.feng
 * @description 设置RecyclerView Item的边距
 * @email hai-long.feng@mail.foxconn.com
 * @date 2018/12/4 14:39
 */
public class RecyclerViewSpacesItemDecoration extends RecyclerView.ItemDecoration {

    private HashMap<String, Integer> mSpaceValueMap;

    public static final String TOP_DECORATION = "top_decoration";
    public static final String BOTTOM_DECORATION = "bottom_decoration";
    public static final String LEFT_DECORATION = "left_decoration";
    public static final String RIGHT_DECORATION = "right_decoration";
    public RecyclerViewSpacesItemDecoration(HashMap<String, Integer> mSpaceValueMap) {
        this.mSpaceValueMap = mSpaceValueMap;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        if (mSpaceValueMap.get(TOP_DECORATION) != null)
            outRect.top = mSpaceValueMap.get(TOP_DECORATION);
        if (mSpaceValueMap.get(LEFT_DECORATION) != null)
            outRect.left = mSpaceValueMap.get(LEFT_DECORATION);
        if (mSpaceValueMap.get(RIGHT_DECORATION) != null)
            outRect.right = mSpaceValueMap.get(RIGHT_DECORATION);
        if (mSpaceValueMap.get(BOTTOM_DECORATION) != null)
            outRect.bottom = mSpaceValueMap.get(BOTTOM_DECORATION);

    }

}
