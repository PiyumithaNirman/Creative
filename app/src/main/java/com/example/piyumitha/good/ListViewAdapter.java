package com.example.piyumitha.good;

import android.widget.BaseAdapter;

public abstract class ListViewAdapter<T> extends BaseAdapter {
    private T[] data;

    protected ListViewAdapter(T[] data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public T getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
