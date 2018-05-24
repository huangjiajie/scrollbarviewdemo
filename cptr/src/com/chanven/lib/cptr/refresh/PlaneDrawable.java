package com.chanven.lib.cptr.refresh;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;

import com.chanven.lib.cptr.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JulyYu on 2016/10/6.
 */

public class PlaneDrawable extends RefreshDrawable implements Runnable {

    private boolean isRunning;
    private Handler mHandler = new Handler();

    protected int mOffset;
    protected float mPercent;
    protected int drawableMinddleWidth;
    protected List<Bitmap> bitmaps = new ArrayList<>();
    protected RectF rectF = new RectF();

    public PlaneDrawable(Context context, PullRefreshLayout layout) {
        super(context, layout);
        getBitmaps(context);
    }

    private void getBitmaps(Context context) {
        BitmapDrawable drawable1 = (BitmapDrawable) context.getResources().getDrawable(R.drawable.zhier1);
        drawableMinddleWidth = drawable1.getMinimumWidth() / 2;
        bitmaps.add(drawable1.getBitmap());
        bitmaps.add(((BitmapDrawable) context.getResources().getDrawable(R.drawable.zhier2)).getBitmap());
        bitmaps.add(((BitmapDrawable) context.getResources().getDrawable(R.drawable.zhier3)).getBitmap());
        bitmaps.add(((BitmapDrawable) context.getResources().getDrawable(R.drawable.zhier4)).getBitmap());
        bitmaps.add(((BitmapDrawable) context.getResources().getDrawable(R.drawable.zhier5)).getBitmap());
        bitmaps.add(((BitmapDrawable) context.getResources().getDrawable(R.drawable.zhier6)).getBitmap());
        bitmaps.add(((BitmapDrawable) context.getResources().getDrawable(R.drawable.zhier7)).getBitmap());
        bitmaps.add(((BitmapDrawable) context.getResources().getDrawable(R.drawable.zhier8)).getBitmap());
        bitmaps.add(((BitmapDrawable) context.getResources().getDrawable(R.drawable.zhier9)).getBitmap());
        bitmaps.add(((BitmapDrawable) context.getResources().getDrawable(R.drawable.zhier10)).getBitmap());
        bitmaps.add(((BitmapDrawable) context.getResources().getDrawable(R.drawable.zhier11)).getBitmap());
        bitmaps.add(((BitmapDrawable) context.getResources().getDrawable(R.drawable.zhier12)).getBitmap());
    }

    @Override
    public void setPercent(float percent) {
        mPercent = percent;
        int centerX = getBounds().centerX();
        rectF.left = centerX - drawableMinddleWidth * mPercent;
        rectF.right = centerX + drawableMinddleWidth * mPercent;
        rectF.top = -drawableMinddleWidth * 2 * mPercent;
        rectF.bottom = 0;
    }

    @Override
    public void setColorSchemeColors(int[] colorSchemeColors) {
    }

    @Override
    public void offsetTopAndBottom(int offset) {
        mOffset += offset;
        invalidateSelf();
    }

    @Override
    public void start() {
        isRunning = true;
        mHandler.postDelayed(this, 100);
    }

    @Override
    public void run() {
        if (isRunning) {
            mHandler.postDelayed(this, 100);
            invalidateSelf();
        }
    }

    @Override
    public void stop() {
        isRunning = false;
        mHandler.removeCallbacks(this);
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void draw(Canvas canvas) {
        int num = (int) (System.currentTimeMillis() / 100 % 12);
        final int saveCount = canvas.save();
        canvas.translate(0, mOffset);
        Bitmap bitmap = bitmaps.get(num);
        canvas.drawBitmap(bitmap, null, rectF, null);
        canvas.restoreToCount(saveCount);
    }
}
