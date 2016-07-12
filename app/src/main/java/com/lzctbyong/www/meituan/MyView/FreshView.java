package com.lzctbyong.www.meituan.MyView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**
 * Created by lzctbyong on 2016/7/8.
 */
public class FreshView extends ListView {
    View mInflate;

    public FreshView(Context context) {
        super(context);
    }
    
    public FreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public FreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
