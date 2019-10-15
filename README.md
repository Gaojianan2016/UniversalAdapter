# UniversalAdapter
自定义万用适配器

- 依赖使用
```
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}


dependencies {
    implementation 'com.github.Gaojianan2016:UniversalAdapter:2.0.0x'
}
```

# 基础使用

```
package com.gjn.universaladapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gjn.universaladapterlibrary.BaseListAdapter;
import com.gjn.universaladapterlibrary.BaseRecyclerAdapter;
import com.gjn.universaladapterlibrary.BaseTypeRecyclerAdapter;
import com.gjn.universaladapterlibrary.HFAdapterWrapper;
import com.gjn.universaladapterlibrary.ListViewHolder;
import com.gjn.universaladapterlibrary.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ListView listView;
    Context context;
    BaseListAdapter<String> listAdapter;
    BaseRecyclerAdapter<String> recyclerAdapter;
    HFAdapterWrapper adapterWrapper;
    BaseTypeRecyclerAdapter<String> typeRecyclerAdapter;
    List<String> strings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        listView = findViewById(R.id.listView);
        context = this;
        addClick();

        strings = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {
            strings.add("Item  " + i + "\nSub Item  " + i);
        }

        listAdapter = new BaseListAdapter<String>(context, android.R.layout.simple_list_item_1, strings) {
            @Override
            protected void bindData(ListViewHolder holder, String item, int position) {
                holder.setTextViewText(android.R.id.text1, item);
            }
        };

        recyclerAdapter = new BaseRecyclerAdapter<String>(context, android.R.layout.simple_list_item_1, strings) {
            @Override
            public void bindData(RecyclerViewHolder holder, String item, int position) {
                holder.setTextViewText(android.R.id.text1, item);
            }
        };

        BaseTypeRecyclerAdapter.TypeSupport<String> support = new BaseTypeRecyclerAdapter.SimpleTypeSupport<String>() {
            @Override
            public int getLayoutId(int type) {
                if (type == 1) {
                    return android.R.layout.simple_list_item_2;
                }
                return android.R.layout.simple_list_item_1;
            }

            @Override
            public int getType(int position, String s) {
                if (position % 2 == 0) {
                    return 1;
                }
                return 0;
            }
        };

        typeRecyclerAdapter = new BaseTypeRecyclerAdapter<String>(context, strings, support) {
            @Override
            public void bindData(RecyclerViewHolder holder, String item, int position) {
                holder.setTextViewText(android.R.id.text1, item);
                if (position % 2 == 0) {
                    holder.setTextViewText(android.R.id.text2, "------>" + item);
                    holder.getTextView(android.R.id.text2).setTextColor(Color.GRAY);
                }
            }
        };

        listView.setAdapter(listAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapterWrapper = new HFAdapterWrapper(recyclerAdapter);
        recyclerView.setAdapter(adapterWrapper);

        adapterWrapper.setOnBindHFDataListener(new HFAdapterWrapper.SimpleBindHFDataListener() {
            @Override
            public void bindHeaderView(View view, Object o, final int position) {
                TextView textView = (TextView) view;
                textView.setText((String) o);
            }

            @Override
            public void bindFooterView(View view, Object o, final int position) {
                TextView textView = (TextView) view;
                textView.setText((String) o);
            }
        });

        adapterWrapper.setOnHFClickListener(new HFAdapterWrapper.SimpleHFClickListener() {
            @Override
            public void headerItemClick(View v, Object o, int position) {
                Toast.makeText(context, "点击头部 " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void footerItemClick(View v, Object o, int position) {
                Toast.makeText(context, "点击尾部 " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addClick() {
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);

                recyclerAdapter.addStart("新的首项");
                adapterWrapper.setAdapter(recyclerAdapter);
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);

                recyclerAdapter.add("新的尾项");
                adapterWrapper.setAdapter(recyclerAdapter);
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = new TextView(context);
                textView.setText("我是头部");
                listView.addHeaderView(textView);

                TextView textView2 = new TextView(context);
                textView2.setText("我是头部");
                String str = "我是奇怪的头部  " + adapterWrapper.getHeaderCount();
                adapterWrapper.addHeaderView(textView2, str);
            }
        });

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = new TextView(context);
                textView.setText("我是尾部");
                listView.addFooterView(textView);

                TextView textView2 = new TextView(context);
                textView2.setText("我是尾部");
                String str = "我是奇怪的尾部  " + adapterWrapper.getFooterCount();
                adapterWrapper.addFooterView(textView2, str);
            }
        });

        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.VISIBLE);

                adapterWrapper.clearHeaderView();
                adapterWrapper.clearFooterView();
                recyclerView.setAdapter(adapterWrapper);
            }
        });

        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);

                recyclerView.setAdapter(typeRecyclerAdapter);
            }
        });
    }
}
```
