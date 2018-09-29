package com.gjn.universaladapterlibrary;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseListAdapter
 * Author: gjn.
 * Time: 2017/9/5.
 */

public abstract class BaseListAdapter<T> extends BaseAdapter implements IAdapterItem<T> {

    protected Context mContext;
    protected Activity mActivity;
    protected List<T> mData;
    protected int mLayoutId;
    protected LayoutInflater mLayoutInflater;
    protected OnItemClickListener onItemClickListener;
    protected OnItemLongClickListener onItemLongClickListener;

    public BaseListAdapter(Context context, int layoutId, List<T> data) {
        mContext = context;
        mActivity = (Activity) context;
        mData = data == null ? new ArrayList<T>() : data;
        mLayoutId = layoutId;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewHolder holder = ListViewHolder.getHolder(mContext, convertView, mLayoutId, parent);
        addItemClick(holder, position);
        bindData(holder, (T) getItem(position), position);
        return holder.getConvertView();
    }

    private void addItemClick(final ListViewHolder holder, final int position) {
        if (onItemClickListener != null) {
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, position);
                }
            });
        }

        if (onItemLongClickListener != null) {
            holder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClickListener.onItemLongClick(v, position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public List<T> getData() {
        return mData;
    }

    @Override
    public void setData(List<T> data) {
        clear();
        add(data);
    }

    @Override
    public void add(int pos, T item) {
        if (pos > getCount()) {
            pos = getCount();
        }
        if (pos < 0) {
            pos = 0;
        }
        mData.add(pos, item);
        notifyDataSetChanged();
    }

    @Override
    public void add(T item) {
        add(getCount(), item);
    }

    @Override
    public void add(List<T> items) {
        if (items != null) {
            for (T item : items) {
                add(item);
            }
        }
    }

    @Override
    public void addStart(T item) {
        add(0, item);
    }

    @Override
    public void delete(int pos) {
        mData.remove(pos);
        notifyDataSetChanged();
    }

    @Override
    public void delete(T item) {
        mData.remove(item);
        notifyDataSetChanged();
    }

    @Override
    public void delete() {
        delete(getCount() - 1);
    }

    @Override
    public void change(int pos, T item) {
        mData.set(pos, item);
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public void move(int from, int to) {}

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    protected abstract void bindData(ListViewHolder holder, T item, int position);

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View v, int pos);
    }
}
