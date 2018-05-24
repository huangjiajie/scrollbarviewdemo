/*
Copyright 2015 Chanven

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.chanven.lib.cptr.loadmore;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chanven.lib.cptr.R;


/**
 * default load more view
 */
public class DefaultLoadMoreViewFooter implements ILoadMoreViewFactory {

    private Context context;
    public DefaultLoadMoreViewFooter(Context context) {
        this.context=context;
    }

    @Override
    public ILoadMoreView madeLoadMoreView() {
        return new LoadMoreHelper();
    }


    private class LoadMoreHelper implements ILoadMoreView {

        protected View footerView;
        protected TextView footerTv;
        protected ProgressBar footerBar;

        protected OnClickListener onClickRefreshListener;
        private ImageView iv_loading;
        private Animation animation;

        @Override
        public void init(FootViewAdder footViewHolder, OnClickListener onClickRefreshListener) {
            footerView = footViewHolder.addFootView(com.chanven.lib.cptr.R.layout.loadmore_default_footer);
            footerTv = (TextView) footerView.findViewById(com.chanven.lib.cptr.R.id.loadmore_default_footer_tv);
            footerBar = (ProgressBar) footerView.findViewById(com.chanven.lib.cptr.R.id.loadmore_default_footer_progressbar);

            iv_loading = (ImageView) footerView.findViewById(com.chanven.lib.cptr.R.id.iv_loading);
            this.onClickRefreshListener = onClickRefreshListener;
            showNormal();
        }

        @Override
        public void showNormal() {
           // footerTv.setText("点击加载更多");
            footerTv.setText(context.getResources().getString(R.string.loaded));
           // footerBar.setVisibility(View.GONE);
            iv_loading.setVisibility(View.GONE);
            
            
            if (animation!=null){
                iv_loading.clearAnimation();
                iv_loading.setVisibility(View.GONE);
            }
            
           // footerView.setOnClickListener(onClickRefreshListener);
        }

        @Override
        public void showLoading() {
            footerTv.setText(context.getResources().getString(R.string.cube_ptr_refreshing));
           // footerBar.setVisibility(View.VISIBLE);

            //使用AnimationUtils类的静态方法loadAnimation()来加载XML中的动画XML文件  
            animation = AnimationUtils.loadAnimation(context, com.chanven.lib.cptr.R.anim.rotating);
            iv_loading.startAnimation(animation);
            
            
            footerView.setOnClickListener(null);
        }

        @Override
        public void showFail(Exception exception) {
            footerTv.setText(context.getString(R.string.fail_load));
//            footerBar.setVisibility(View.GONE);

            if (animation!=null){
                iv_loading.clearAnimation();
                iv_loading.setVisibility(View.GONE);
            }
            
            footerView.setOnClickListener(onClickRefreshListener);
        }

        @Override
        public void showNomore() {

            footerTv.setText(context.getResources().getString(R.string.no_empty));
//            footerBar.setVisibility(View.GONE);

            if (animation!=null){
                iv_loading.clearAnimation();
                iv_loading.setVisibility(View.GONE);
            }
            
            footerView.setOnClickListener(null);
        }

        @Override
        public void setFooterVisibility(boolean isVisible) {
            footerView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }


    }





}
