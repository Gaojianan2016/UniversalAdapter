package com.gjn.universaladapterlibrary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

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
        RecyclerViewHolder holder = RecyclerViewHolder.getHolder(mContext,parent,layoutId);
        addItemClick(holder);
        return holder;
    }

    public interface TypeSupport<T>{
        int getLayoutId(int type);
        int getType(int position, T t);
    }
}
