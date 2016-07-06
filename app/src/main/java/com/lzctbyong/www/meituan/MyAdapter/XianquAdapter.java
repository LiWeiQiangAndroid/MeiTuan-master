package com.lzctbyong.www.meituan.MyAdapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
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
public class XianquAdapter extends BaseAdapter {

    private Context mContext;
    private TextView hotcitylayouthotcity;
    private ArrayList<String> mStrings = new ArrayList<>();
    private static final String TAG = "HotCityAdapter";

    public XianquAdapter(Context mContext) {
        this.mContext = mContext;
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

    public void update() {
        int mId = 0;
        String arg = CityiPckerActivity.currentcity + "%";
        Cursor mCursor = CityiPckerActivity.mSQLiteDatabase.rawQuery("select id from def where  name like ?", new String[]{arg});
        if (mCursor.moveToFirst()) {
            mId = mCursor.getInt(mCursor.getColumnIndex("id"));
            mCursor.close();
        }
        if ((mId % 1000) == 0) {
            mId += 100;
        }
        arg = String.valueOf(mId / 100) + "%";
        Log.i(TAG, "XianquAdapter: " + CityiPckerActivity.currentcity + arg);
        mCursor = CityiPckerActivity.mSQLiteDatabase.rawQuery("select name from def where id like ? ",
                new String[]{arg});
        mStrings.clear();
        if (mCursor.moveToFirst()) {
            do {
                String mS = mCursor.getString(mCursor.getColumnIndex("name"));
                mStrings.add(mS);
            } while (mCursor.moveToNext());
            mCursor.close();
        }
        notifyDataSetChanged();
    }

}
