package com.gjn.universaladapterlibrary;

import java.util.List;

/**
 * @author gjn
 * @time 2018/9/29 11:22
 */

public interface IAdapterItem<T> {
    T getItem(int pos);

    List<T> getData();

    void setData(List<T> list);

    void add(int pos, T item);

    void add(T item);

    void add(List<T> items);

    void addStart(T item);

    void delete(int pos);

    void delete(T item);

    void delete();

    void change(int pos, T item);

    void move(int from, int to);

    void clear();
}
