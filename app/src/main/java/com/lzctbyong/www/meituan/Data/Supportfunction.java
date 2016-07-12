package com.lzctbyong.www.meituan.Data;

import com.lzctbyong.www.meituan.R;

import java.util.ArrayList;

/**
 * Created by lzctbyong on 2016/7/8.
 */
public class Supportfunction {
    ArrayList<category> mCategories;

    class category {
        int imgid;
        String title;

        public category(int mImgid, String mTitle) {
            imgid = mImgid;
            title = mTitle;
        }

        public int getImgid() {
            return imgid;
        }

        public String getTitle() {
            return title;
        }
    }

    public void intidata() {
        mCategories = new ArrayList<category>();
        for (int i = 0; i < 30; ++i) {
            category mCategory = new category(R.drawable.ic_category_0,String.valueOf(i));
        }
    }

}
