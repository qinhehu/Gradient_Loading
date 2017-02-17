package com.example.qinhe.loadingdialog;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

public class CircleLoadingDrawable extends Drawable {

    private static final int ANGLE_360 = 360;
    private static final int START_ANGLE = -15;
    private static final float MAX_SWIPE_ANGLE = 0.95F * ANGLE_360;

    private static final long ANIMATION_DURATION = 2000;
    private static final float DEFAULT_SIZE = 56.0f;
    private static final float DEFAULT_CENTER_RADIUS = 12.5f;
    private static final float DEFAULT_STROKE_WIDTH = 2.5f;

    private float mWidth;
    private float mHeight;
    private float mStrokeWidth;
    private float mCenterRadius;
    private float mGroupRotation;
    private float mStrokeInset;
    private float mStartAngle;
    private float mSweepAngle;

    private long mDuration;
    private boolean isFirstDraw;

    private Paint mPaint;
    @ColorInt
    private int mCurrentColor;
    private ValueAnimator mAnimator;
    private Context mContext;

    public CircleLoadingDrawable(Context context) {
        setUpPaint();
        setDefaultParams(context);
        setUpAnimators();
        start();
    }

    @Override
    public void draw(Canvas canvas) {
        int saveCount = canvas.save();

        canvas.rotate(mGroupRotation, getBounds().exactCenterX(), getBounds().exactCenterY());

        RectF arcBounds = new RectF();
        arcBounds.set(getBounds());

        mPaint.setAntiAlias(true);
        mPaint.setColor(mCurrentColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(DisplayUtils.dip2px(65));
        canvas.drawArc(arcBounds, mStartAngle, mSweepAngle, false, mPaint);

        mPaint.reset();
        mPaint.setAntiAlias(true);
        //画白圆,作为空心圆使用
        arcBounds.set(DisplayUtils.dip2px(30),
                DisplayUtils.dip2px(30),
                getBounds().width() - DisplayUtils.dip2px(30),
                getBounds().height() - DisplayUtils.dip2px(30));
        mPaint.setColor(mCurrentColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawArc(arcBounds, 0, 360, true, mPaint);
        mPaint.reset();

        canvas.restoreToCount(saveCount);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return 0;
    }

    public void start() {
        resetOriginals();
        mAnimator.setDuration(mDuration);
        mAnimator.start();
    }

    public void stop() {
        mAnimator.cancel();
    }

    private void resetOriginals() {
        mStartAngle = 315;
        mSweepAngle = 315;
    }

    private void setDefaultParams(Context context) {
        mCurrentColor = Color.WHITE;

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float screenDensity = metrics.density;

        mWidth = DisplayUtils.dip2px(DEFAULT_SIZE);
        mHeight = DisplayUtils.dip2px(DEFAULT_SIZE);
        mStrokeWidth = DisplayUtils.dip2px(DEFAULT_STROKE_WIDTH);
        mCenterRadius = DisplayUtils.dip2px(DEFAULT_CENTER_RADIUS);

        mDuration = ANIMATION_DURATION;

        mStrokeWidth = 6;
    }

    private void setUpAnimators() {
        mAnimator = ValueAnimator.ofFloat(0.0f, 2f);
        mAnimator.setRepeatCount(-1);
        mAnimator.setRepeatMode(Animation.RESTART);
        mAnimator.setInterpolator(new DecelerateInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            float centerEndAngle = 0;
            float centerStartAngle = 0;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();

                if (progress <= 1f) {
                    mStartAngle = 315 - MAX_SWIPE_ANGLE * progress / 10;
                    mSweepAngle = 360 - MAX_SWIPE_ANGLE * progress;
                    centerStartAngle = mStartAngle;
                    centerEndAngle = mSweepAngle;
                    invalidateSelf();
                }
                if (progress >= 1 && progress <= 2) {
                    mStartAngle = centerStartAngle + centerEndAngle - MAX_SWIPE_ANGLE * (progress - 1f) / 10;
                    mSweepAngle = centerEndAngle - MAX_SWIPE_ANGLE * (progress - 1f) - MAX_SWIPE_ANGLE / 10;
                    invalidateSelf();
                }
            }
        });
    }

    private void setUpPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }
}
