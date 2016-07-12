//TODO: 2016/7/6 主界面
package com.lzctbyong.www.meituan;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jauker.widget.BadgeView;
import com.lzctbyong.www.meituan.CityPicker.CityiPckerActivity;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends FragmentActivity implements OnClickListener {

    public static final int City_RequestCode = 0x1;
    public static final int Cap_RequestCode = 0x2;
    private android.widget.Button activitymaincity;
    private android.widget.TextView activitymainserach;
    private android.widget.ImageButton activitymainerweima;
    private android.widget.ImageButton activitymainmsg;
    private android.widget.RadioGroup activitymainradiogrop;
    BadgeView mBadgeView;
    SQLiteDatabase mCookies;
    private android.widget.ListView activitymainerweimaview;
    FragmentManager mSupportFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.activitymainerweimaview = (ListView) findViewById(R.id.activity_main_erweimaview);
        this.activitymainradiogrop = (RadioGroup) findViewById(R.id.Bottomlayout_Radiogroup);
        this.activitymainmsg = (ImageButton) findViewById(R.id.activity_main_msg);
        this.activitymainerweima = (ImageButton) findViewById(R.id.activity_main_erweima);
        this.activitymainserach = (TextView) findViewById(R.id.activity_main_serach);
        this.activitymaincity = (Button) findViewById(R.id.activity_main_city);
        activitymainmsg.setOnClickListener(this);
        activitymainerweima.setOnClickListener(this);
        //TODO: 2016/7/8 fragment碎片
        mSupportFragmentManager = getSupportFragmentManager();
        activitymainradiogrop.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.bottomlayout_rb1:
                        mSupportFragmentManager.beginTransaction().replace(R.id.activity_main_fg, new
                                AFragment()).commit();
                        break;
                    case R.id.bottomlayout_rb2:
                        mSupportFragmentManager.beginTransaction().replace(R.id.activity_main_fg, new
                                BFragment()).commit();
                        break;
                    case R.id.bottomlayout_rb3:
                        mSupportFragmentManager.beginTransaction().replace(R.id.activity_main_fg, new
                                CFragment()).commit();
                        break;
                    case R.id.bottomlayout_rb4:
                        mSupportFragmentManager.beginTransaction().replace(R.id.activity_main_fg, new
                                DFragment()).commit();
                        break;
                }
            }
        });
        activitymainradiogrop.check(R.id.bottomlayout_rb1);
        //TODO: 2016/7/8
        //TODO: 消息提醒
        mBadgeView = new BadgeView(this);
        mBadgeView.setTargetView(activitymainmsg);
        mBadgeView.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
        mBadgeView.setBackgroundColor(Color.RED);

        //TODO: 建立数据库
        mCookies = openOrCreateDatabase("cookies", 0, null);
        mCookies.execSQL("create table if not exists msgcookies (\n" +
                "    id integer primary key autoincrement unique,\n" +
                "    title text ,\n" +
                "    content text ,\n" +
                "    isread boolean \n" +
                ")");

        //TODO: 2016/7/6 获取未读信息
        Cursor mCursor = mCookies.rawQuery("select id from msgcookies where isread = 0 ", null);
        msgcount = mCursor.getCount();
        mBadgeView.setBadgeCount(msgcount);

        //TODO: 2016/7/6 二维码
        String[] mtitile = {"扫一扫", "付款码"};
        int[] imgid = {R.drawable.apollo_ic_new_address, R.drawable.hotel_ic_pay_result_qrcode};
        List<Map<String, Object>> mMaps = new ArrayList<>();
        for (int i = 0; i < mtitile.length; ++i) {
            Map<String, Object> mHashMap = new HashMap<>();
            mHashMap.put("img", imgid[i]);
            mHashMap.put("title", mtitile[i]);
            mMaps.add(mHashMap);
        }

        /*设置数据*/
        activitymainerweimaview.setAdapter(new SimpleAdapter(this, mMaps, R.layout.erweimalist,
                new String[]{"img", "title"}, new int[]{R.id.erweimalist_img, R.id.erweimalist_text}));
        /*点击事件*/
        activitymainerweimaview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, ""+position, Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0:
                        Intent mIntent = new Intent(parent.getContext(), CaptureActivity.class);
                        startActivityForResult(mIntent, Cap_RequestCode);
                        break;
                    case 1:
                        Intent mIntent2 = new Intent(parent.getContext(), FkmActivity.class);
                        startActivity(mIntent2);
                        break;
                }
            }
        });
    }

    //TODO: 2016/7/6 启动城市选择界面
    public void startpickcity(View view) {
        Intent mIntent = new Intent(this, CityiPckerActivity.class);
        startActivityForResult(mIntent, City_RequestCode);
    }

    //TODO: 2016/7/6 返回数据处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult: " + requestCode + ',' + resultCode);
        if (requestCode == City_RequestCode && resultCode == RESULT_OK) {
            Bundle mExtras = data.getExtras();
            String mString = mExtras.getString(CityiPckerActivity.EXTRA_KEY);
            String mString2 = mExtras.getString(CityiPckerActivity.EXTRA_KEY2);
            if (mString2 != null) {
                activitymaincity.setText(mString + mString2);
            } else {
                activitymaincity.setText(mString);
            }
        } else if (requestCode == Cap_RequestCode && resultCode == RESULT_OK) {
            Bundle mExtras = data.getExtras();
            String mResult = mExtras.getString("result");
            Log.i(TAG, "onActivityResult: " + mResult);
        }
    }

    private int msgcount = 0;
    private static final String TAG = "MainActivity";
    public static final int Message_msg = 0x1;

    //TODO: 2016/7/6 点击事件
    @Override
    public void onClick(View v) {
        Message mMessage = new Message();
        switch (v.getId()) {
            case R.id.activity_main_msg:
                msgcount++;
                mBadgeView.setBadgeCount(msgcount);
                mHandler.sendEmptyMessage(Message_msg);
                break;
            case R.id.activity_main_erweima:
                if (activitymainerweimaview.getVisibility() == View.VISIBLE) {
                    activitymainerweimaview.setVisibility(View.GONE);
                } else {
                    activitymainerweimaview.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    //TODO: 2016/7/6 信息处理
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Message_msg:
                    mCookies.execSQL("insert into msgcookies (id,title,content,isread) " +
                            "values (?,?,?,?)", new Object[]{msgcount, "msg" + msgcount,
                            "content for " + msgcount, 0});
                    break;

            }
        }
    };

}
