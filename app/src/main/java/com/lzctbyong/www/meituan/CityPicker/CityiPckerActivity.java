package com.lzctbyong.www.meituan.CityPicker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lzctbyong.www.meituan.CityPicker.Interface.OnLetterChangedListener;
import com.lzctbyong.www.meituan.CityPicker.V.SideLetterBar;
import com.lzctbyong.www.meituan.MyAdapter.AllCityAdapter;
import com.lzctbyong.www.meituan.MyAdapter.CityAdapter;
import com.lzctbyong.www.meituan.MyAdapter.HotCityAdapter;
import com.lzctbyong.www.meituan.MyAdapter.XianquAdapter;
import com.lzctbyong.www.meituan.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CityiPckerActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CityiPckerActivity";
    private android.widget.TextView tvletteroverlay;
    private com.lzctbyong.www.meituan.CityPicker.V.SideLetterBar sideletterbar;
    private android.widget.SearchView activitycityipckersearch;
    private TextView activitycityipckerleft;
    private android.widget.GridView activitycityipckergridview;
    private android.widget.ListView activitycityipckerallcity;
    private TextView activitycityipckerleft2;
    private RelativeLayout activitycityipckercurrentcity_RelativeLayout;
    private TextView activitycityipckercurrentcity_textright;
    private boolean OnOffallcity = false;
    private boolean OnOffhotcity = true;
    private boolean OnOffcurrentcity = false;
    private GridView activitycityipckercurentcity;
    private Drawable mDrawable1;
    private Drawable mDrawable2;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private TextView activitycityipckergpscity;
    private android.widget.ImageView activitycityipckerrefreshgps;
    FileOutputStream mFileOutputStream;
    InputStream mInputStream;
    public static SQLiteDatabase mSQLiteDatabase;
    public static String currentcity = null;
    public static String District = null;
    private TextView activitycityipckercurrentcity_textleft;
    private ImageButton activitycityipckercurrentcity_titilebar_back;
    XianquAdapter mXianquAdapter;
    private ListView activitycityipckerlistview;
    
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cityi_pcker);
        /*资源*/
        mDrawable1 = getResources().getDrawable(R.drawable.arrow_up);
        mDrawable2 = getResources().getDrawable(R.drawable.arrow_down);
        /*获取控件*/
        this.activitycityipckerlistview = (ListView) findViewById(R.id.activity_cityi_pcker_listview);
        this.activitycityipckerrefreshgps = (ImageView) findViewById(R.id.activity_cityi_pcker_refreshgps);
        this.activitycityipckergpscity = (TextView) findViewById(R.id.activity_cityi_pcker_gpscity);
        this.activitycityipckercurentcity = (GridView) findViewById(R.id.activity_cityi_pcker_curentcity);
        this.sideletterbar = (SideLetterBar) findViewById(R.id.side_letter_bar);
        this.tvletteroverlay = (TextView) findViewById(R.id.tv_letter_overlay);
        this.activitycityipckergridview = (GridView) findViewById(R.id.activity_cityi_pcker_gridview);
        this.activitycityipckerleft = (TextView) findViewById(R.id.activity_cityi_pcker_left);
        this.activitycityipckersearch = (SearchView) findViewById(R.id.activity_cityi_pcker_search);
        this.activitycityipckerallcity = (ListView) findViewById(R.id.activity_cityi_pcker_allcity);
        this.activitycityipckerleft2 = (TextView) findViewById(R.id.activity_cityi_pcker_left2);
        this.activitycityipckercurrentcity_textright = (TextView) findViewById(R.id.currentcity_textright);
        this.activitycityipckercurrentcity_textleft = (TextView) findViewById(R.id.currentcity_textleft);
        this.activitycityipckercurrentcity_RelativeLayout = (RelativeLayout) findViewById(R.id.currentcity_layout);
        this.activitycityipckercurrentcity_titilebar_back = (ImageButton) findViewById(R.id.titilebar_back);

        /*点击事件*/
        activitycityipckerleft.setOnClickListener(this);
        activitycityipckerleft2.setOnClickListener(this);
        activitycityipckerrefreshgps.setOnClickListener(this);
        activitycityipckercurrentcity_RelativeLayout.setOnClickListener(this);
        activitycityipckercurrentcity_titilebar_back.setOnClickListener(this);

        /*取出SharePreferes*/
        SharedPreferences mCityOld = getSharedPreferences("city", MODE_PRIVATE);
        mCityOld.getString("city", currentcity);
        mCityOld.getString("District", District);
        mCityOld.getString(EXTRA_KEY, currentcity);
        mCityOld.getString(EXTRA_KEY2, District);
        if (District != null) {
            activitycityipckercurrentcity_textleft.setText("当前城市:" + currentcity+District);
        } else {
            activitycityipckercurrentcity_textleft.setText("当前城市:" + currentcity);
        }

        //TODO: 2016/6/28 拷贝数据库
        /*如果文件不存在拷贝*/
        String mPath = "/data/data/" + getPackageName() + "/databases/city2";
        if (!new File(mPath).exists()) {
            String mDir = "/data/data/" + getPackageName() + "/databases";
            new File(mDir).mkdirs();
            mInputStream = getResources().openRawResource(R.raw.meituan);
            try {
                mFileOutputStream = new FileOutputStream(mPath);
                byte[] mBytes = new byte[1024];
                int count = 0;
                while ((count = mInputStream.read(mBytes)) > 0) {
                    mFileOutputStream.write(mBytes, 0, count);
                }
            } catch (Exception mE) {
                mE.printStackTrace();
            } finally {
                if (mFileOutputStream != null) {
                    try {
                        mInputStream.close();
                        mFileOutputStream.close();
                    } catch (IOException mE) {
                        mE.printStackTrace();
                    }
                }
            }
        } else {
            Log.i(TAG, "file exists");
        }

        //TODO: 2016/6/28 百度地图
        /*LocationClient类*/
        mLocationClient = new LocationClient(getApplicationContext());
        /*初始化*/
        initLocation();
        /*注册监听函数*/
        mLocationClient.registerLocationListener(myListener);


        //TODO: 2016/6/28 打开数据库
        mSQLiteDatabase = openOrCreateDatabase("city2", 0, null);

        //TODO: 2016/6/29 当前城市县区
        mXianquAdapter = new XianquAdapter(this);
        activitycityipckercurentcity.setAdapter(mXianquAdapter);
        activitycityipckercurentcity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                District = (String) mXianquAdapter.getItem(position);
                activitycityipckercurrentcity_textleft.setText("当前城市:" + currentcity + District);
            }
        });

        //TODO: 2016/6/29 热门城市
        final HotCityAdapter mHotCityAdapter = new HotCityAdapter(this);
        activitycityipckergridview.setAdapter(mHotCityAdapter);
        /*监听器*/
        activitycityipckergridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentcity = (String) mHotCityAdapter.getItem(position);
                activitycityipckercurrentcity_textleft.setText("当前城市:" + currentcity);
                mXianquAdapter.update();
            }
        });


        //TODO: 2016/6/29 所有城市
        final AllCityAdapter<String> mAllCityAdapter = new AllCityAdapter<String>(activitycityipckerallcity, R.layout
                .list_header, R.layout.list_last);

        /*查询城市数据库*/
        for (char i = 'a'; i < 'z' + 1; ++i) {
            ArrayList<String> mStrings = new ArrayList<>();
            String arg = String.valueOf(i) + "%";
            Cursor mCursor = mSQLiteDatabase.rawQuery("select NAME from city where PINYIN like ? order by PINYIN asc ",
                    new String[]{arg});
            if (mCursor.moveToFirst()) {
                do {
                    String mS = mCursor.getString(mCursor.getColumnIndex("NAME"));
                    mStrings.add(mS);
                } while (mCursor.moveToNext());
                sideletterbar.setLetter(String.valueOf(i).toUpperCase());
                mCursor.close();
            }
            mAllCityAdapter.addSection(String.valueOf(i).toUpperCase(), (String[]) mStrings.toArray(new String[0]));
        }
        activitycityipckerallcity.setAdapter(mAllCityAdapter);
       /*点击事件*/
        activitycityipckerallcity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentcity = mAllCityAdapter.finditem(position);
                activitycityipckercurrentcity_textleft.setText("当前城市:" + currentcity);
                mXianquAdapter.update();
            }
        });

        //TODO: 2016/6/28 右侧滑动栏
        /*传递textview到view*/
        sideletterbar.postview(tvletteroverlay);
        /*右侧滑动栏 联动 Listview*/
        sideletterbar.setOnLetterChangedListener(new OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter, int position) {
                activitycityipckerallcity.setSelection(mAllCityAdapter.finditemhead(position));
            }
        });

        //TODO: 2016/6/29 搜索框
        activitycityipckerlistview.setAdapter(new CityAdapter(this));
        activitycityipckersearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    activitycityipckerlistview.clearTextFilter();
                } else {
                    activitycityipckerlistview.setFilterText(newText);
                    activitycityipckerlistview.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });
    }
    
    @Override
    public void onBackPressed() {
        backtomain();
    }
    
    public static final String EXTRA_KEY = "city";
    public static final String EXTRA_KEY2 = "District";

    //    TODO: 2016/7/4 返回主页
    public void backtomain() {
        Intent mIntent = new Intent();
        SharedPreferences.Editor mCity = getSharedPreferences("city", MODE_PRIVATE).edit();
        if (District != null) {
            mCity.putString("city", currentcity);
            mCity.putString("District", District);
            mIntent.putExtra(EXTRA_KEY, currentcity);
            mIntent.putExtra(EXTRA_KEY2, District);
        } else {
            mCity.putString("city", currentcity);
            mIntent.putExtra(EXTRA_KEY, currentcity);
        }
        mCity.commit();
        setResult(RESULT_OK, mIntent);
        finish();
    }
    
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(false);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
        // .getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*热门城市视图*/
            case R.id.activity_cityi_pcker_left:
                if (OnOffallcity) {
                    activitycityipckergridview.setVisibility(View.VISIBLE);
                } else {
                    activitycityipckergridview.setVisibility(View.GONE);
                }
                OnOffallcity = !OnOffallcity;
                break;
             /*所有城市视图*/
            case R.id.activity_cityi_pcker_left2:
                if (OnOffhotcity) {
                    activitycityipckerallcity.setVisibility(View.VISIBLE);
                    sideletterbar.setVisibility(View.VISIBLE);
                } else {
                    activitycityipckerallcity.setVisibility(View.GONE);
                    sideletterbar.setVisibility(View.GONE);
                }
                OnOffhotcity = !OnOffhotcity;
                break;
            /*当前城市视图*/
            case R.id.currentcity_layout:
                if (currentcity != null) {
                    switchcurrentview();
                }
                break;

            /*进行定位城市*/
            case R.id.activity_cityi_pcker_refreshgps:
                mLocationClient.start();
                break;
            /*返回*/
            case R.id.titilebar_back:
                backtomain();
                break;
        }
    }
    
    private void switchcurrentview() {
        mDrawable1.setBounds(0, 0, mDrawable1.getMinimumWidth(), mDrawable1.getMinimumHeight());
        mDrawable2.setBounds(0, 0, mDrawable2.getMinimumWidth(), mDrawable2.getMinimumHeight());
        OnOffcurrentcity = !OnOffcurrentcity;
        if (OnOffcurrentcity) {
            mXianquAdapter.update();
            activitycityipckercurrentcity_textright.setCompoundDrawables(null, null, mDrawable1, null);
            activitycityipckercurentcity.setVisibility(View.VISIBLE);
        } else {
            activitycityipckercurrentcity_textright.setCompoundDrawables(null, null, mDrawable2, null);
            activitycityipckercurentcity.setVisibility(View.GONE);
        }
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.unRegisterLocationListener(myListener); //注销监听
        mLocationClient.stop(); //停止定位服务
    }
    
    
    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            String localaddr = "";
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                //                StringBuffer sb = new StringBuffer(256);
                //                sb.append("time : ");
                //                sb.append(location.getTime());
                //                sb.append("\nerror code : ");
                //                sb.append(location.getLocType());
                //                sb.append("\nlatitude : ");
                //                sb.append(location.getLatitude());
                //                sb.append("\nlontitude : ");
                //                sb.append(location.getLongitude());
                //                sb.append("\nradius : ");
                //                sb.append(location.getRadius());
                //                sb.append("\nCountryCode : ");
                //                sb.append(location.getCountryCode());
                //                sb.append("\nCountry : ");
                //                sb.append(location.getCountry());
                //                sb.append("\ncitycode : ");
                //                sb.append(location.getCityCode());
                //                sb.append("\ncity : ");
                //                sb.append(location.getCity());
                //                sb.append("\nDistrict : ");
                //                sb.append(location.getDistrict());
                //                sb.append("\nStreet : ");
                //                sb.append(location.getStreet());
                //                sb.append("\naddr : ");
                //                sb.append(location.getAddrStr());
                //                sb.append("\nDescribe: ");
                //                sb.append(location.getLocationDescribe());
                //                sb.append("\nDirection(not all devices have value): ");
                //                sb.append(location.getDirection());
                //                sb.append("\nPoi: ");
                //                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                //                    for (int i = 0; i < location.getPoiList().size(); i++) {
                //                        Poi poi = (Poi) location.getPoiList().get(i);
                //                        sb.append(poi.getName() + ";");
                //                    }
                //                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    //                    sb.append("\nspeed : ");
                    //                    sb.append(location.getSpeed());// 单位：km/h
                    //                    sb.append("\nsatellite : ");
                    //                    sb.append(location.getSatelliteNumber());
                    //                    sb.append("\nheight : ");
                    //                    sb.append(location.getAltitude());// 单位：米
                    //                    sb.append("\ndescribe : ");
                    //                    sb.append("gps定位成功");
                    localaddr = "定位城市:" + location.getCity() + location.getDistrict();
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    //                    // 运营商信息
                    //                    sb.append("\noperationers : ");
                    //                    sb.append(location.getOperators());
                    //                    sb.append("\ndescribe : ");
                    //                    sb.append("网络定位成功");
                    localaddr = "定位城市:" + location.getCity() + location.getDistrict();
                }
                //                else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                //                    //                    sb.append("\ndescribe : ");
                //                    //                    sb.append("离线定位成功，离线定位结果也是有效的");
                //                    localaddr = "定位城市:" + location.getCity() + location.getDistrict();
                //                }
                else if (location.getLocType() == BDLocation.TypeServerError) {
                    //                    sb.append("\ndescribe : ");
                    //                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                    localaddr = "定位城市:服务端网络定位失败";
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    //                    sb.append("\ndescribe : ");
                    //                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                    localaddr = "定位城市:请检查网络是否通畅";
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    //                    sb.append("\ndescribe : ");
                    //                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                    localaddr = "定位城市:无法获取有效定位";
                } else {
                    localaddr = "定位城市:无法获请刷新";
                }
            }
            activitycityipckergpscity.setText(localaddr);
            currentcity = location.getCity();
            District = location.getDistrict();
            if (currentcity != null) {
                activitycityipckercurrentcity_textleft.setText("当前城市:" + currentcity + District);
                mXianquAdapter.update();
            }
            mLocationClient.stop(); //停止定位服务
        }
    }
}

