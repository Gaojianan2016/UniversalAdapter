package com.gjn.universaladapterlibrary;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * BaseTypeRecyclerAdapter
 * Author: gjn.
 * Time: 2017/9/25.
 */

public abstract class BaseTypeRecyclerAdapter<T> extends BaseRecyclerAdapter<T> {

    protected TypeSupport<T> mTypeSupport;

    public BaseTypeRecyclerAdapter(Context ctx, TypeSupport<T> typeSupport) {
        this(ctx, null, typeSupport);
    }

    public BaseTypeRecyclerAdapter(Context ctx, List<T> list, TypeSupport<T> typeSupport) {
        super(ctx, -1, list);
        mTypeSupport = typeSupport;
    }

    @Override
    public int getItemViewType(int position) {
        return mTypeSupport.getType(position,mData.get(position));
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = mTypeSupport.getLayoutId(viewType);
        View view = mTypeSupport.getLayoutView(viewType);
        RecyclerViewHolder holder = null;
        if (layoutId != 0) {
            holder = RecyclerViewHolder.getHolder(mContext, parent, layoutId);
        }
        if (view != null) {
            holder = RecyclerViewHolder.getHolder(view);
        }
        addItemClick(holder);
        return holder;
    }

    public abstract static class SimpleTypeSupport<T> implements TypeSupport<T>{
        @Override
        public View getLayoutView(int type) {
            return null;
        }
    }

    public interface TypeSupport<T>{
        View getLayoutView(int type);
        int getLayoutId(int type);
        int getType(int position, T t);
    }
}
