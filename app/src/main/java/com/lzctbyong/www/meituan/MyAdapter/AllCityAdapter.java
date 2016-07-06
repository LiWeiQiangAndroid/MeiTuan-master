package com.lzctbyong.www.meituan.MyAdapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lzctbyong on 2016/6/25.
 */
public  class AllCityAdapter<T> extends BaseAdapter implements AdapterView.OnItemClickListener {
    public static final int HEADER = 0;
    public static final int ITEM = 1;
    private LayoutInflater mLayoutInflater;
    private int mHeaderResid;
    private int mItemResid;
    private ArrayList<SectionItem<T>> mArrayList;
    private SparseArray<SectionItem<T>> mSparseArray;
    private static final String TAG = "AllCityAdapter";

    public AllCityAdapter(ListView v, int headerResid, int itemResid) {
        mLayoutInflater = LayoutInflater.from(v.getContext());
        mHeaderResid = headerResid;
        mItemResid = itemResid;
        mArrayList = new ArrayList<>();
        mSparseArray = new SparseArray<>();
    }

    public void addSection(String mTitle, T[] mItems) {
        SectionItem<T> mTSectionItem = new SectionItem<>(mTitle, mItems);
        int mIndexOf = mArrayList.indexOf(mTSectionItem);
        if (mIndexOf >= 0) {
            mArrayList.remove(mTSectionItem);
            mArrayList.add(mIndexOf, mTSectionItem);
        } else {
            mArrayList.add(mTSectionItem);
        }
        reorderSections();
        notifyDataSetChanged();
    }

    private void reorderSections() {
        mSparseArray.clear();
        int startpos = 0;
        for (SectionItem<T> item : mArrayList) {
            mSparseArray.put(startpos, item);
            startpos += item.getcount();
        }
    }

    @Override
    public int getCount() {
        int count = 0;
        for (SectionItem<T> item : mArrayList) {
            count += item.getcount();
        }
        return count;
    }

    public boolean ishead(int pos) {
        for (int i = 0; i < mSparseArray.size(); ++i) {
            if (pos == mSparseArray.keyAt(i)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemViewType(int position) {
        if (ishead(position)) {
            return HEADER;
        } else {
            return ITEM;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public T finditem(int pos) {
        int first, last;
        for (int i = 0; i < mSparseArray.size(); ++i) {
            first = mSparseArray.keyAt(i);
            last = first + mSparseArray.valueAt(i).getcount();
            if (pos >= first && pos < last) {
                int mpos = pos - first - 1;
                return mSparseArray.valueAt(i).getItems(mpos);
            }
        }
        return null;
    }

    public int finditemhead(int pos) {
        return mSparseArray.keyAt(pos);
    }

    @Override
    public T getItem(int position) {
        return finditem(position);
    }

    @Override
    public boolean isEnabled(int position) {
        return !ishead(position);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (getItemViewType(position)) {
            case HEADER:
                return getheadview(position, convertView, parent);

            case ITEM:
                return getitemview(position, convertView, parent);

            default:
                return convertView;
        }
    }

    public View getheadview(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mHeaderResid, parent, false);
        }
        SectionItem<T> mItem = mSparseArray.get(position);
        TextView mTextView = (TextView) convertView.findViewById(android.R.id.text1);
        mTextView.setText(mItem.getTitle());
        return convertView;
    }

    public View getitemview(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mItemResid, parent, false);
        }
        T mFinditem = finditem(position);
        TextView mTextView = (TextView) convertView.findViewById(android.R.id.text1);
        mTextView.setText(mFinditem.toString());
        return convertView;
    }

    //    public abstract void OnitemClick(T item);

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    class SectionItem<T> {
        private String title;
        private T[] items;

        public SectionItem(String mTitle, T[] mItems) {
            if (mTitle == null) {
                mTitle = "";
            }
            title = mTitle;
            items = mItems;
        }

        public String getTitle() {
            return title;
        }

        public T getItems(int position) {
            return items[position];
        }

        public int getcount() {
            return items == null ? 1 : items.length+1;
        }

        @Override
        public boolean equals(Object obj) {

            if (obj != null && obj instanceof SectionItem) {
                return ((SectionItem) obj).getTitle().equals(title);
            }
            return false;
        }
    }
}
