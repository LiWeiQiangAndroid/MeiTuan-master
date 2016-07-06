package com.lzctbyong.www.meituan;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jauker.widget.BadgeView;
import com.lzctbyong.www.meituan.CityPicker.CityiPckerActivity;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    public static final int City_RequestCode = 0x1;
    private android.widget.Button activitymaincity;
    private android.widget.TextView activitymainserach;
    private android.widget.ImageButton activitymainerweima;
    private android.widget.ImageButton activitymainmsg;
    BadgeView mBadgeView;
    SQLiteDatabase mCookies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.activitymainmsg = (ImageButton) findViewById(R.id.activity_main_msg);
        this.activitymainerweima = (ImageButton) findViewById(R.id.activity_main_erweima);
        this.activitymainserach = (TextView) findViewById(R.id.activity_main_serach);
        this.activitymaincity = (Button) findViewById(R.id.activity_main_city);
        activitymainmsg.setOnClickListener(this);
        activitymainerweima.setOnClickListener(this);
        /*消息提醒*/
        mBadgeView = new BadgeView(this);
        mBadgeView.setTargetView(activitymainmsg);
        mBadgeView.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
        mBadgeView.setBackgroundColor(Color.RED);

        /*建立数据库*/
        mCookies = openOrCreateDatabase("cookies", 0, null);
        mCookies.execSQL("create table if not exists msgcookies (\n" +
                "    id integer primary key autoincrement unique,\n" +
                "    title text ,\n" +
                "    content text ,\n" +
                "    isread boolean \n" +
                ")");

        /*获取未读消息*/
        Cursor mCursor = mCookies.rawQuery("select id from msgcookies where isread = 0 ", null);
        msgcount = mCursor.getCount();
        mBadgeView.setBadgeCount(msgcount);

        /*二维码*/
    }


    /*选择城市*/
    public void choosecity(View view) {
        startActivityForResult(new Intent(this, CityiPckerActivity.class), City_RequestCode);
    }

    /*城市选择*/
    public void startpickcity(View view) {
        Intent mIntent = new Intent(this, CityiPckerActivity.class);
        startActivityForResult(mIntent, City_RequestCode);
    }

    /*返回数据*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == City_RequestCode && resultCode == RESULT_OK) {
            Bundle mExtras = data.getExtras();
            String mString = mExtras.getString(CityiPckerActivity.EXTRA_KEY);
            String mString2 = mExtras.getString(CityiPckerActivity.EXTRA_KEY2);
            if (mString2 != null) {
                activitymaincity.setText(mString + mString2);
            } else {
                activitymaincity.setText(mString);
            }
        }
    }

    private int msgcount = 0;
    private static final String TAG = "MainActivity";
    public static final int Message_msg = 0x1;

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
                break;
        }
    }

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
