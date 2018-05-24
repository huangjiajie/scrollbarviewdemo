package com.huangjiajie.scrollbarviewdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    @Bind(R.id.scroll_Viewpager)
    ListViewPager scrollViewpager;
    @Bind(R.id.scroll_pagerSlidingTabStrip)
    PagerSlidingTabStrip scrollPagerSlidingTabStrip;
    @Bind(R.id.scroll_appbar)
    AppBarLayout scrollAppbar;
    @Bind(R.id.scroll_coordinatorLayout)
    CoordinatorLayout scrollCoordinatorLayout;
    @Bind(R.id.ptr_recycler_view_frame)
    PtrClassicFrameLayout ptr_recycler_view_frame;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tv_title;


    private List<TabFragment> fragmentList = new ArrayList<>();
    private List<String> tabList = new ArrayList<>();
    private ListFragmentPagerAdapter listStripFragmentPagerAdapter;
    private FragmentPagerAdapter fragmentPagerAdapter;


    private int curTab = 0;
    private int scrollOffset = 4444;
    private int scrollOffsetSquare = 0;
    private int scrollOffsetAttention = 0;
    private TabFragment fragment1;
    private TabFragment fragment2;
    public final static int MINE_TAB_TYPE = 0;
    public final static int LIKE_TAB_TYPE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        resolveViewPager();
        setListener();




    }


    private void resolveViewPager() {
        /**设置了tab的样式*/
        scrollPagerSlidingTabStrip.setShouldExpand(true);
        scrollPagerSlidingTabStrip.setDividerColor(getResources().getColor(R.color.cardview_dark_background));
        scrollPagerSlidingTabStrip.setIndicatorColor(getResources().getColor(R.color.cardview_dark_background));
        scrollPagerSlidingTabStrip.setUnderlineColor(Color.TRANSPARENT);
        scrollPagerSlidingTabStrip.setTabTextSelectColor(getResources().getColor(R.color.cardview_dark_background));


        fragment1 = TabFragment.newInstance(MINE_TAB_TYPE);
        fragment2 = TabFragment.newInstance(LIKE_TAB_TYPE);

        fragment1.setPtrClassicFrameLayout(ptr_recycler_view_frame);
        fragment2.setPtrClassicFrameLayout(ptr_recycler_view_frame);

        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        tabList.add("Tab1");
        tabList.add("Tab2");

        scrollViewpager.setAdapter(fragmentPagerAdapter);
        scrollViewpager.setOffscreenPageLimit(2);
        scrollViewpager.setPtrClassicFrameLayout(ptr_recycler_view_frame);

        listStripFragmentPagerAdapter = new ListFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, tabList);
        initFragmentPager();


    }


    public void initFragmentPager() {

        scrollViewpager.setAdapter(listStripFragmentPagerAdapter);
        scrollPagerSlidingTabStrip.setViewPager(scrollViewpager);

        scrollPagerSlidingTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                /**不同的recyclerView的列表对应的位置不同，需要处理对应的顶部banner是否隐藏*/

                curTab = i;


                scrollAppbar.removeOnOffsetChangedListener(MainActivity.this);

                int scrollHeight = (i == 0) ? scrollOffsetSquare : scrollOffsetAttention;

                if (scrollHeight == -scrollAppbar.getTotalScrollRange()) {

                    CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams)
                            scrollAppbar.getLayoutParams()).getBehavior();

                    behavior.onNestedPreScroll(scrollCoordinatorLayout, scrollAppbar,
                            fragmentList.get(i).getScrollableView(), 0, scrollAppbar.getTotalScrollRange(), new int[]{0, 0});

                    onOffsetChanged(scrollAppbar, scrollAppbar.getTotalScrollRange());

                }

                ptr_recycler_view_frame.setEnabled(false);
                scrollAppbar.addOnOffsetChangedListener(MainActivity.this);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        scrollViewpager.setCurrentItem(0);


    }

    /**
     * 根据appber的返回，判断此时的tab和banner是位置和显示效果
     */
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {


        if (Math.abs(i) > scrollAppbar.getTotalScrollRange() / 1.2) {
            tv_title.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);

        } else {
            tv_title.setVisibility(View.INVISIBLE);
            toolbar.setVisibility(View.GONE);

        }

//         LogUtils.i("---Math.abs(i)---" + Math.abs(i) + "--headHeight::" + headHeight + "--titleHeight::" + titleHeight);

        if (i != scrollOffset) {
            if (curTab == 0) {
                scrollOffsetSquare = i;
            } else {
                scrollOffsetAttention = i;
            }
            scrollOffset = i;

            ptr_recycler_view_frame.setEnabled(i == 0);
            scrollViewpager.setIsTop(i == 0);
        }


    }


    private void setListener() {


        scrollAppbar.addOnOffsetChangedListener(this);

        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return fragmentList.get(arg0);
            }
        };

        ptr_recycler_view_frame.setOnLoadMoreListener(null);
        ptr_recycler_view_frame.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

                if (curTab == 0) {
                    fragment1.refresh();
                } else {
                    fragment2.refresh();
                }


            }
        });


    }


}
