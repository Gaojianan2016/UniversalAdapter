package com.gjn.universaladapterlibrary;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ListViewHolder
 * Author: gjn.
 * Time: 2017/9/5.
 */

public class ListViewHolder {
    private final SparseArray<View> mViews;
    private View mConvertView;

    private ListViewHolder(View view) {
        mConvertView = view;
        mConvertView.setTag(this);
        mViews = new SparseArray<>();
    }

    public static ListViewHolder getHolder(Context context, View convertView,
                                           int layoutId, ViewGroup parent) {
        if (convertView == null) {
            View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
            return new ListViewHolder(view);
        } else {
            return (ListViewHolder) convertView.getTag();
        }
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    public ListViewHolder setTextViewText(int id, CharSequence str) {
        getTextView(id).setText(str);
        return this;
    }

    public TextView getTextView(int id) {
        return getView(id);
    }

    public ImageView getImageView(int id) {
        return getView(id);
    }

    public ListViewHolder setClickListener(View.OnClickListener listener) {
        getConvertView().setOnClickListener(listener);
        return this;
    }

    public ListViewHolder setLongClickListener(View.OnLongClickListener listener) {
        getConvertView().setOnLongClickListener(listener);
        return this;
    }

    public ListViewHolder setClickListener(int id, View.OnClickListener listener) {
        getView(id).setOnClickListener(listener);
        return this;
    }

    public ListViewHolder setLongClickListener(int id, View.OnLongClickListener listener) {
        getView(id).setOnLongClickListener(listener);
        return this;
    }

}
