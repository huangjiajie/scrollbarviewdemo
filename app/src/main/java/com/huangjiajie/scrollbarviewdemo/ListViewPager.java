package com.huangjiajie.scrollbarviewdemo;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.chanven.lib.cptr.PtrClassicFrameLayout;


/**
 * Created by shuyu on 2016/8/11.
 */

public class ListViewPager extends ViewPager {

    private int touchPadding;

    public ListViewPager(Context context) {
        super(context);
    }

    public ListViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private float mDownX, mDownY;
    private boolean mIsTop = true;
    private PtrClassicFrameLayout ptr_recycler_view_frame;

    public PtrClassicFrameLayout getPtrClassicFrameLayout() {
        return ptr_recycler_view_frame;
    }

    public void setPtrClassicFrameLayout(PtrClassicFrameLayout ptr_recycler_view_frame) {
        this.ptr_recycler_view_frame = ptr_recycler_view_frame;
    }


    /**
     * 通过手势判断，设置焦点的切换和刷新的是否可用，根据不同手势势能刷新
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getY() < touchPadding)
            return false;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                ptr_recycler_view_frame.setEnabled(false);
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getX() - mDownX) > Math.abs(ev.getY() - mDownY)) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    ptr_recycler_view_frame.setEnabled(false);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    enableSwipeRefresh();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                enableSwipeRefresh();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void enableSwipeRefresh() {
        if (ptr_recycler_view_frame != null && mIsTop) {
            ptr_recycler_view_frame.setEnabled(true);
        } else {
            ptr_recycler_view_frame.setEnabled(false);
        }
    }

    public void setTouchPadding(int touchPadding) {
        this.touchPadding = touchPadding;
    }

    public void setIsTop(boolean isTop) {
        this.mIsTop = isTop;
    }
}
