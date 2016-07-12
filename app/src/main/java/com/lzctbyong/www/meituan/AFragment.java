package com.lzctbyong.www.meituan;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AFragment extends Fragment {


    private SwipeRefreshLayout fragmentaswfresh;
    private ListView fragmentalist;

    public AFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mInflate = inflater.inflate(R.layout.fragment_a, container, false);
        this.fragmentaswfresh = (SwipeRefreshLayout) mInflate.findViewById(R.id.fragment_a_swfresh);
        fragmentaswfresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(mRunnable).start();
            }
        });
        this.fragmentalist = (ListView) mInflate.findViewById(R.id.fragment_a_list);
        fragmentalist.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, new
                String[]{"a", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b"}));

//        mInflate = LayoutInflater.from(getContext()).inflate(R.layout.mainfuntionbar, null);
//        GridView mGridView   = (GridView) mInflate.findViewById(R.id.mainfuntionbar_gridview);


        return mInflate;
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x1:
                    fragmentaswfresh.setRefreshing(false);
                    break;
            }
        }
    };

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException mE) {
                mE.printStackTrace();
            }
            mHandler.sendEmptyMessage(new Message().what = 0x1);
        }
    };

}
