package com.gjn.universaladapterlibrary;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * HFAdapterWrapper
 * Author: gjn.
 * Time: 2017/9/4.
 */

public class HFAdapterWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0x10001;
    private static final int TYPE_FOOTER = 0x20001;

    private SparseArrayCompat<FixedViewInfo> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<FixedViewInfo> mFooterViews = new SparseArrayCompat<>();
    private OnBindHFDataListener onBindHFDataListener;
    private OnHFClickListener onHFClickListener;
    private RecyclerView.Adapter mInnerAdapter;

    public HFAdapterWrapper(RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;
    }

    public void setAdapter(RecyclerView.Adapter adapter){
        mInnerAdapter = adapter;
        notifyDataSetChanged();
    }

    public boolean isHeader(int pos) {
        return pos < getHeaderCount();
    }

    public boolean isFooter(int pos) {
        return pos >= (getHeaderCount() + getRealItemCount());
    }

    public int getHeaderCount() {
        return mHeaderViews.size();
    }

    public int getFooterCount() {
        return mFooterViews.size();
    }

    public int getRealItemCount() {
        return mInnerAdapter.getItemCount();
    }

    public void addHeaderView(View v) {
        addHeaderView(v, null);
    }

    public void addHeaderView(View v, Object data) {
        final FixedViewInfo info = new FixedViewInfo();
        info.view = v;
        info.data = data;
        mHeaderViews.put(mHeaderViews.size() + TYPE_HEADER, info);
        notifyDataSetChanged();
    }

    public void addFooterView(View v) {
        addFooterView(v, null);
    }

    public void addFooterView(View v, Object data) {
        final FixedViewInfo info = new FixedViewInfo();
        info.view = v;
        info.data = data;
        mFooterViews.put(mFooterViews.size() + TYPE_FOOTER, info);
        notifyDataSetChanged();
    }

    public void clearFooterView() {
        mFooterViews.clear();
        notifyDataSetChanged();
    }

    public void clearHeaderView() {
        mHeaderViews.clear();
        notifyDataSetChanged();
    }

    public boolean hasHeaderView() {
        return getHeaderCount() > 0;
    }

    public boolean hasFooterView() {
        return getFooterCount() > 0;
    }

    public void setOnBindHFDataListener(OnBindHFDataListener onBindHFDataListener) {
        this.onBindHFDataListener = onBindHFDataListener;
    }

    public void setOnHFClickListener(OnHFClickListener onHFClickListener) {
        this.onHFClickListener = onHFClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooter(position)) {
            return mFooterViews.keyAt(position - getHeaderCount() - getRealItemCount());
        }
        return mInnerAdapter.getItemViewType(position - getHeaderCount());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            return RecyclerViewHolder.getHolder(mHeaderViews.get(viewType).view);
        } else if (mFooterViews.get(viewType) != null) {
            return RecyclerViewHolder.getHolder(mFooterViews.get(viewType).view);
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final int type = getItemViewType(position);
        if (isHeader(position)) {
            bindHFData(type, position, TYPE_HEADER);
            addHFClick(type, position, TYPE_HEADER);
        } else if (isFooter(position)) {
            bindHFData(type, position, TYPE_FOOTER);
            addHFClick(type, position, TYPE_FOOTER);
        } else {
            mInnerAdapter.onBindViewHolder(holder, position - getHeaderCount());
        }
    }

    private void addHFClick(final int type, final int position, int typeHF) {
        if (typeHF == TYPE_HEADER) {
            final View view = mHeaderViews.get(type).view;
            if (onHFClickListener != null) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onHFClickListener.headerItemClick(v, mHeaderViews.get(type).data,
                                position);
                    }
                });

                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return onHFClickListener.headerItemLongClick(v, mHeaderViews.get(type).data,
                                position);
                    }
                });
            }
        } else {
            final View view = mFooterViews.get(type).view;
            if (onHFClickListener != null) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onHFClickListener.footerItemClick(v, mFooterViews.get(type).data,
                                position - getHeaderCount() - getRealItemCount());
                    }
                });
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return onHFClickListener.footerItemLongClick(v, mFooterViews.get(type).data,
                                position - getHeaderCount() - getRealItemCount());
                    }
                });
            }
        }
    }

    private void bindHFData(int type, int position, int typeHF) {
        if (typeHF == TYPE_HEADER) {
            final View view = mHeaderViews.get(type).view;
            if (onBindHFDataListener != null) {
                onBindHFDataListener.bindHeaderView(view, mHeaderViews.get(type).data, position);
            }
        } else {
            final View view = mFooterViews.get(type).view;
            if (onBindHFDataListener != null) {
                onBindHFDataListener.bindFooterView(view, mFooterViews.get(type).data, position);
            }
        }
    }

    @Override
    public int getItemCount() {
        return getHeaderCount() + getFooterCount() + getRealItemCount();
    }

    public interface OnBindHFDataListener {
        void bindHeaderView(View view, Object o, int position);

        void bindFooterView(View view, Object o, int position);
    }

    public interface OnHFClickListener {
        void headerItemClick(View v, Object o, int position);

        boolean headerItemLongClick(View v, Object o, int position);

        void footerItemClick(View v, Object o, int position);

        boolean footerItemLongClick(View v, Object o, int position);
    }

    public abstract static class SimpleHFClickListener implements OnHFClickListener {
        @Override
        public void headerItemClick(View v, Object o, int position) {
        }

        @Override
        public boolean headerItemLongClick(View v, Object o, int position) {
            return false;
        }

        @Override
        public void footerItemClick(View v, Object o, int position) {
        }

        @Override
        public boolean footerItemLongClick(View v, Object o, int position) {
            return false;
        }
    }

    public abstract static class SimpleBindHFDataListener implements OnBindHFDataListener {
        @Override
        public void bindHeaderView(View view, Object o, int position) {
        }

        @Override
        public void bindFooterView(View view, Object o, int position) {
        }
    }

    public class FixedViewInfo {
        public View view;
        public Object data;
    }
}
