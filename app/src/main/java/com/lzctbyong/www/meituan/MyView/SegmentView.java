package com.lzctbyong.www.meituan.MyView;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzctbyong.www.meituan.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by lzctbyong on 2016/6/24.
 */
public class SegmentView extends LinearLayout {

    Context mContext;

    public SegmentView(Context context) {
        super(context);
    }


    public SegmentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public SegmentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("deprecation")
    private void init() {
        final TextView mTextView1 = new TextView(mContext);
        final TextView mTextView2 = new TextView(mContext);
        LayoutParams mParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        mTextView1.setLayoutParams(mParams);
        mTextView2.setLayoutParams(mParams);
        mTextView1.setText(R.string.titilebarleft);
        mTextView2.setText(R.string.titilebarlright);
        /*颜色*/
        XmlResourceParser mXml = getResources().getXml(R.xml.titilebarcolor);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(), mXml);
            mTextView1.setTextColor(csl);
            mTextView2.setTextColor(csl);
        } catch (XmlPullParserException mE) {
            mE.printStackTrace();
        } catch (IOException mE) {
            mE.printStackTrace();
        }
        /**/
        mTextView1.setGravity(Gravity.CENTER);
        mTextView2.setGravity(Gravity.CENTER);
        mTextView1.setPadding(3, 6, 3, 6);
        mTextView2.setPadding(3, 6, 3, 6);
        mTextView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        mTextView2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        mTextView1.setBackgroundResource(R.drawable.titilebarleft);
        mTextView2.setBackgroundResource(R.drawable.titilebarright);
        mTextView1.setSelected(true);
        removeAllViews();
        addView(mTextView1);
        addView(mTextView2);
        invalidate();
        mTextView1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextView1.setSelected(true);
                mTextView2.setSelected(false);
            }
        });
        mTextView2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextView1.setSelected(false);
                mTextView2.setSelected(true);
            }
        });
    }

    private OnclickListener mOnclickListener;
    interface OnclickListener {
        public void Onclick(View v, int position);
    }
}
