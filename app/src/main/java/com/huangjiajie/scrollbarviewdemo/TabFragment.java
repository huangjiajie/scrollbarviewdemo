package com.huangjiajie.scrollbarviewdemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hjj on 17/11/15.
 */
public class TabFragment extends Fragment {


    @Bind(R.id.list_item_recycler)
    XRecyclerView listItemRecycler;

    Handler handler = new Handler();

    private PtrClassicFrameLayout mPtrClassicFrameLayout;
    private int mPage;
    private LinearLayoutManager linearLayoutManager;
    public int type = 0;//默认po图
    private int page_size = 5;
    private int page = 1;
    private boolean isLoad;
    private ArrayList<String> totallist = new ArrayList<>();
    private ArrayList<String> list = new ArrayList<>();
    private MyAdapter myAdapter;


    public static TabFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        TabFragment fragment = new TabFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab, null);
        ButterKnife.bind(this, view);


        initOther();

        return view;
    }

    private void initOther() {

        mPage = getArguments().getInt("page");

        linearLayoutManager = new LinearLayoutManager(getActivity());
        listItemRecycler.setLayoutManager(linearLayoutManager);
        listItemRecycler.setPullRefreshEnabled(false);
        listItemRecycler.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);


        myAdapter = new MyAdapter();
        listItemRecycler.setAdapter(myAdapter);
        listItemRecycler.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //使用的是外部全局刷新
            }

            @Override
            public void onLoadMore() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Log.i("AAA","---onLoadMore-----");
                        isLoad = true;
                        getNetData();


                    }
                }, 2000);
            }
        });

        refresh();

    }


    /**
     * 下拉刷新
     */
    public void refresh() {

        Log.i("AAA","---refresh-----");
        isLoad = false;
        getNetData();


    }


    public void getNetData() {

        if (isLoad) {
            page = page + 1;
        } else {
            page = 1;
        }

        for (int i = 0; i < 10; i++) {
            list.add("测试" + i);
        }

        setRefreshStatus();
        totallist.addAll(list);
        myAdapter.notifyDataSetChanged();


    }


    /**
     * 设置刷新加载的状态
     */
    private void setRefreshStatus() {

        if (!isLoad) {//刷新

            totallist.clear();
            mPtrClassicFrameLayout.refreshComplete();


        } else {//加载

            listItemRecycler.loadMoreComplete();
        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);

    }


    /**
     * 回到顶部
     */
    public void changeToTop() {
        listItemRecycler.stopNestedScroll();
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.scrollToPosition(0);
    }


    public void setPtrClassicFrameLayout(PtrClassicFrameLayout ptrClassicFrameLayout) {
        this.mPtrClassicFrameLayout = ptrClassicFrameLayout;
    }


    public RecyclerView getScrollableView() {
        return listItemRecycler;
    }


    public int getPage() {

        return mPage;
    }


    private class MyAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        public MyAdapter() {

        }

        @Override
        public int getItemCount() {
            return totallist == null ? 0 : totallist.size();
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_text, parent, false);
            return new ItemViewHolder(view);
        }


        @Override
        public void onBindViewHolder(final ItemViewHolder holder, final int position) {

            holder.tv_text.setText(totallist.get(position) + "");


        }
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_text;

        public ItemViewHolder(View view) {
            super(view);
            tv_text = (TextView) view.findViewById(R.id.tv_text);
        }

    }

}
