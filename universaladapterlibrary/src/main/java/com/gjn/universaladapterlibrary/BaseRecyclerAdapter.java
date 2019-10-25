package com.gjn.universaladapterlibrary;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseRecyclerAdapter
 * Author: gjn.
 * Time: 2017/6/9.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> implements IAdapterItem<T> {

    protected List<T> mData;
    protected Context mContext;
    protected Activity mActivity;
    protected int mLayoutId;
    protected OnItemClickListener mOnItemClickListener;
    protected OnItemLongClickListener mOnItemLongClickListener;

    public BaseRecyclerAdapter(Context ctx, int layoutId, List<T> list) {
        mData = (list != null) ? list : new ArrayList<T>();
        mContext = ctx;
        mActivity = (Activity) ctx;
        mLayoutId = layoutId;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolder holder = RecyclerViewHolder.getHolder(mContext, parent, mLayoutId);
        addItemClick(holder);
        return holder;
    }

    protected void addItemClick(final RecyclerViewHolder holder) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, holder.getLayoutPosition());
                }
            });
        }
        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemLongClickListener.onItemLongClick(v, holder.getLayoutPosition());
                    return true;
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        bindData(holder, mData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public T getItem(int pos) {
        return mData.get(pos);
    }

    @Override
    public List<T> getData() {
        return mData;
    }

    @Override
    public void setData(List<T> list) {
        clear();
        add(list);
    }

    @Override
    public void add(int pos, T item) {
        if (pos > getItemCount()) {
            pos = getItemCount();
        }
        if (pos < 0) {
            pos = 0;
        }
        mData.add(pos, item);
        notifyItemInserted(pos);
    }

    @Override
    public void add(T item) {
        add(getItemCount(), item);
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
        delete(getItemCount() - 1);
    }

    @Override
    public void change(int pos, T item) {
        mData.set(pos, item);
        notifyItemChanged(pos, item);
    }

    @Override
    public void move(int from, int to) {
        T temp = mData.get(from);

        mData.remove(from);
        mData.add(to, temp);

        notifyItemMoved(from, to);
    }

    @Override
    public void clear() {
        mData = new ArrayList<T>();
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

    public abstract void bindData(RecyclerViewHolder holder, T item, int position);

    public interface OnItemClickListener {
        void onItemClick(View view, int pos);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int pos);
    }

}
