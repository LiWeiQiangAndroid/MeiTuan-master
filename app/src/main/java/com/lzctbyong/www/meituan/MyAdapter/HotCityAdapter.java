package com.lzctbyong.www.meituan.MyAdapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lzctbyong.www.meituan.CityPicker.CityiPckerActivity;
import com.lzctbyong.www.meituan.R;

import java.util.ArrayList;

/**
 * Created by lzctbyong on 2016/6/25.
 */
public  class HotCityAdapter extends BaseAdapter {

    private Context mContext;
    private android.widget.TextView hotcitylayouthotcity;
    private  ArrayList<String> mStrings = new ArrayList<>();
    private static final String TAG = "HotCityAdapter";

    public HotCityAdapter(Context mContext) {
        this.mContext = mContext;
        Cursor mCursor = CityiPckerActivity.mSQLiteDatabase.rawQuery("select NAME from city where RANK = 'S' or RANK = " +
                "'A'", null);
        if (mCursor.moveToFirst()) {
            do {
                String mS = mCursor.getString(mCursor.getColumnIndex("NAME"));
                mStrings.add(mS);
            } while (mCursor.moveToNext());
        }
        mCursor.close();
    }

    @Override
    public int getCount() {
        return mStrings.size();
    }

    @Override
    public Object getItem(int position) {
        return mStrings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.hotcity_layout, parent, false);
        this.hotcitylayouthotcity = (TextView) mView.findViewById(R.id.hotcity_layout_hotcity);
        hotcitylayouthotcity.setText(mStrings.get(position));
        return mView;
    }


}
