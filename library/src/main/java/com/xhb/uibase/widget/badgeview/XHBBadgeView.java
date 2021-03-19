package com.xhb.uibase.widget.badgeview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SizeF;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;

import com.xhb.uibase.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chqiu
 *         Email:qstumn@163.com
 */

public class XHBBadgeView extends View {

    public enum DragState {
        START,
        DRAGGING,
        DRAGGING_OUT_OF_RANGE,
        CANCELED,
        SUCCEED,
    }

    @FunctionalInterface
    public interface OnDragStateChangedListener {
        void onDragStateChanged(DragState dragState, XHBBadgeView badge, View targetView);
    }

    protected int mColorBackground;
    protected int mColorBackgroundBorder;
    protected int mColorBadgeText;
    protected Drawable mDrawableBackground;
    protected Bitmap mBitmapClip;
    protected boolean mDrawableBackgroundClip;
    protected float mBackgroundBorderWidth;
    protected float mBadgeTextSize;
    protected float mBadgePadding;
    protected int mBadgeNumber;
    protected String mBadgeText;
    protected boolean mDraggable;
    protected boolean mDragging;
    protected boolean mExact;
    protected boolean mShowShadow;
    protected int mBadgeGravity;
    protected float mGravityOffsetX;
    protected float mGravityOffsetY;

    protected float mDefalutRadius;
    protected float mFinalDragDistance;
    protected int mDragQuadrant;
    protected boolean mDragOutOfRange;

    protected RectF mBadgeTextRect;
    protected RectF mBadgeBackgroundRect;
    protected Path mDragPath;

    protected Paint.FontMetrics mBadgeTextFontMetrics;

    protected PointF mBadgeCenter;
    protected PointF mDragCenter;
    protected PointF mRowBadgeCenter;
    protected PointF mControlPoint;

    protected List<PointF> mInnertangentPoints;

    protected View mTargetView;

    protected int mWidth;
    protected int mHeight;

    protected TextPaint mBadgeTextPaint;
    protected Paint mBadgeBackgroundPaint;
    protected Paint mBadgeBackgroundBorderPaint;

    protected BadgeAnimator mAnimator;

    protected OnDragStateChangedListener mDragStateChangedListener;

    protected ViewGroup mActivityRoot;

    public XHBBadgeView(Context context) {
        this(context, null);
    }

    public XHBBadgeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XHBBadgeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mBadgeTextRect = new RectF();
        mBadgeBackgroundRect = new RectF();
        mDragPath = new Path();
        mBadgeCenter = new PointF();
        mDragCenter = new PointF();
        mRowBadgeCenter = new PointF();
        mControlPoint = new PointF();
        mInnertangentPoints = new ArrayList<>();
        mBadgeTextPaint = new TextPaint();
        mBadgeTextPaint.setAntiAlias(true);
        mBadgeTextPaint.setSubpixelText(true);
        mBadgeTextPaint.setFakeBoldText(true);
        mBadgeTextPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        mBadgeBackgroundPaint = new Paint();
        mBadgeBackgroundPaint.setAntiAlias(true);
        mBadgeBackgroundPaint.setStyle(Paint.Style.FILL);
        mBadgeBackgroundBorderPaint = new Paint();
        mBadgeBackgroundBorderPaint.setAntiAlias(true);
        mBadgeBackgroundBorderPaint.setStyle(Paint.Style.STROKE);
        mColorBackground = getColor(R.color.badge_view_background_color);
        mColorBadgeText = getColor(R.color.badge_view_text_color);
        mBadgeTextSize = getDimension(R.dimen.badge_view_text_size);
        mBadgePadding = getDimension(R.dimen.badge_view_padding);
        mBadgeNumber = 0;
        mBadgeGravity = Gravity.END | Gravity.TOP;
        mGravityOffsetX = getDimension(R.dimen.badge_view_gravity_offset_x);
        mGravityOffsetY = getDimension(R.dimen.badge_view_gravity_offset_y);
        mFinalDragDistance = getDimension(R.dimen.badge_view_final_drag_distance);
        mShowShadow = true;
        mDrawableBackgroundClip = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTranslationZ(1000);
        }
    }

    public void bindTarget(final View targetView) {
        if (targetView == null) {
            throw new IllegalStateException("targetView can not be null");
        }
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
        ViewParent targetParent = targetView.getParent();
        if (targetParent != null && targetParent instanceof ViewGroup) {
            mTargetView = targetView;
            if (targetParent instanceof BadgeContainer) {
                ((BadgeContainer) targetParent).addView(this);
            } else {
                ViewGroup targetContainer = (ViewGroup) targetParent;
                int index = targetContainer.indexOfChild(targetView);
                ViewGroup.LayoutParams targetParams = targetView.getLayoutParams();
                targetContainer.removeView(targetView);
                final BadgeContainer badgeContainer = new BadgeContainer(getContext());
                if(targetContainer instanceof RelativeLayout){
                    badgeContainer.setId(targetView.getId());
                }
                targetContainer.addView(badgeContainer, index, targetParams);
                badgeContainer.addView(targetView);
                badgeContainer.addView(this);
            }
        } else {
            throw new IllegalStateException("targetView must have a parent");
        }
    }

    public View getTargetView() {
        return mTargetView;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mActivityRoot == null) findViewRoot(mTargetView);
    }

    private void findViewRoot(View view) {
        mActivityRoot = (ViewGroup) view.getRootView();
        if (mActivityRoot == null) {
            findActivityRoot(view);
        }
    }

    private void findActivityRoot(View view) {
        if (view.getParent() != null && view.getParent() instanceof View) {
            findActivityRoot((View) view.getParent());
        } else if (view instanceof ViewGroup) {
            mActivityRoot = (ViewGroup) view;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                float x = event.getX();
                float y = event.getY();
                if (mDraggable && event.getPointerId(event.getActionIndex()) == 0
                        && (x > mBadgeBackgroundRect.left && x < mBadgeBackgroundRect.right &&
                        y > mBadgeBackgroundRect.top && y < mBadgeBackgroundRect.bottom)
                        && mBadgeText != null) {
                    initRowBadgeCenter();
                    mDragging = true;
                    updataListener(DragState.START);
                    mDefalutRadius = getDimensionPixelSize(R.dimen.badge_view_default_radius);
                    getParent().requestDisallowInterceptTouchEvent(true);
                    screenFromWindow(true);
                    mDragCenter.x = event.getRawX();
                    mDragCenter.y = event.getRawY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mDragging) {
                    mDragCenter.x = event.getRawX();
                    mDragCenter.y = event.getRawY();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                if (event.getPointerId(event.getActionIndex()) == 0 && mDragging) {
                    mDragging = false;
                    onPointerUp();
                }
                break;
        }
        return mDragging || super.onTouchEvent(event);
    }

    private void onPointerUp() {
        if (mDragOutOfRange) {
            animateHide(mDragCenter);
            updataListener(DragState.SUCCEED);
        } else {
            reset();
            updataListener(DragState.CANCELED);
        }
    }

    protected Bitmap createBadgeBitmap() {
        int bitmapInflect = getDimensionPixelSize(R.dimen.badge_view_bitmap_inflect);
        Bitmap bitmap = Bitmap.createBitmap((int) mBadgeBackgroundRect.width() + bitmapInflect,
                (int) mBadgeBackgroundRect.height() + bitmapInflect, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawBadge(canvas, new PointF(canvas.getWidth() / 2f, canvas.getHeight() / 2f), getBadgeCircleRadius());
        return bitmap;
    }

    protected void screenFromWindow(boolean screen) {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
        if (screen) {
            mActivityRoot.addView(this, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT));
        } else {
            bindTarget(mTargetView);
        }
    }

    private void showShadowImpl(boolean showShadow) {
        int x = getDimensionPixelSize(R.dimen.badge_view_shadow_offset_x);
        int y = getDimensionPixelSize(R.dimen.badge_view_shadow_offset_y);
        switch (mDragQuadrant) {
            case 1:
                y = -y;
                break;
            case 2:
                x = -x;
                y = -y;
                break;
            case 3:
                x = -x;
                break;
            case 4:
                break;
        }
        mBadgeBackgroundPaint.setShadowLayer(showShadow ? getDimensionPixelSize(R.dimen.badge_view_shadow_radius)
                : 0, x, y, 0x33000000);
    }

    private int getDimensionPixelSize(@DimenRes int dimen) {
        return getContext().getResources().getDimensionPixelSize(dimen);
    }

    private float getDimension(@DimenRes int dimen) {
        return getContext().getResources().getDimension(dimen);
    }

    private int getColor(int p) {
        return getContext().getResources().getColor(p);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.draw(canvas);
            return;
        }
        if (mBadgeText != null) {
            initPaints();
            float badgeRadius = getBadgeCircleRadius();
            float startCircleRadius = mDefalutRadius * (1 - MathUtil.getPointDistance
                    (mRowBadgeCenter, mDragCenter) / mFinalDragDistance);
            if (mDraggable && mDragging) {
                mDragQuadrant = MathUtil.getQuadrant(mDragCenter, mRowBadgeCenter);
                showShadowImpl(mShowShadow);
                if (mDragOutOfRange = startCircleRadius < getDimensionPixelSize(R.dimen.badge_view_drag_start_radius)) {
                    updataListener(DragState.DRAGGING_OUT_OF_RANGE);
                    drawBadge(canvas, mDragCenter, badgeRadius);
                } else {
                    updataListener(DragState.DRAGGING);
                    drawDragging(canvas, startCircleRadius, badgeRadius);
                    drawBadge(canvas, mDragCenter, badgeRadius);
                }
            } else {
                findBadgeCenter();
                drawBadge(canvas, mBadgeCenter, badgeRadius);
            }
        }
    }

    private void initPaints() {
        showShadowImpl(mShowShadow);
        mBadgeBackgroundPaint.setColor(mColorBackground);
        mBadgeBackgroundBorderPaint.setColor(mColorBackgroundBorder);
        mBadgeBackgroundBorderPaint.setStrokeWidth(mBackgroundBorderWidth);
        mBadgeTextPaint.setColor(mColorBadgeText);
        mBadgeTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    private void drawDragging(Canvas canvas, float startRadius, float badgeRadius) {
        float dy = mDragCenter.y - mRowBadgeCenter.y;
        float dx = mDragCenter.x - mRowBadgeCenter.x;
        mInnertangentPoints.clear();
        if (dx != 0) {
            double k1 = dy / dx;
            double k2 = -1 / k1;
            MathUtil.getInnertangentPoints(mDragCenter, badgeRadius, k2, mInnertangentPoints);
            MathUtil.getInnertangentPoints(mRowBadgeCenter, startRadius, k2, mInnertangentPoints);
        } else {
            MathUtil.getInnertangentPoints(mDragCenter, badgeRadius, 0d, mInnertangentPoints);
            MathUtil.getInnertangentPoints(mRowBadgeCenter, startRadius, 0d, mInnertangentPoints);
        }
        mDragPath.reset();
        mDragPath.addCircle(mRowBadgeCenter.x, mRowBadgeCenter.y, startRadius,
                mDragQuadrant == 1 || mDragQuadrant == 2 ? Path.Direction.CCW : Path.Direction.CW);
        mControlPoint.x = (mRowBadgeCenter.x + mDragCenter.x) / 2.0f;
        mControlPoint.y = (mRowBadgeCenter.y + mDragCenter.y) / 2.0f;
        mDragPath.moveTo(mInnertangentPoints.get(2).x, mInnertangentPoints.get(2).y);
        mDragPath.quadTo(mControlPoint.x, mControlPoint.y, mInnertangentPoints.get(0).x, mInnertangentPoints.get(0).y);
        mDragPath.lineTo(mInnertangentPoints.get(1).x, mInnertangentPoints.get(1).y);
        mDragPath.quadTo(mControlPoint.x, mControlPoint.y, mInnertangentPoints.get(3).x, mInnertangentPoints.get(3).y);
        mDragPath.lineTo(mInnertangentPoints.get(2).x, mInnertangentPoints.get(2).y);
        mDragPath.close();
        canvas.drawPath(mDragPath, mBadgeBackgroundPaint);

        //draw dragging border
        if (mColorBackgroundBorder != 0 && mBackgroundBorderWidth > 0) {
            mDragPath.reset();
            mDragPath.moveTo(mInnertangentPoints.get(2).x, mInnertangentPoints.get(2).y);
            mDragPath.quadTo(mControlPoint.x, mControlPoint.y, mInnertangentPoints.get(0).x, mInnertangentPoints.get(0).y);
            mDragPath.moveTo(mInnertangentPoints.get(1).x, mInnertangentPoints.get(1).y);
            mDragPath.quadTo(mControlPoint.x, mControlPoint.y, mInnertangentPoints.get(3).x, mInnertangentPoints.get(3).y);
            float startY;
            float startX;
            if (mDragQuadrant == 1 || mDragQuadrant == 2) {
                startX = mInnertangentPoints.get(2).x - mRowBadgeCenter.x;
                startY = mRowBadgeCenter.y - mInnertangentPoints.get(2).y;
            } else {
                startX = mInnertangentPoints.get(3).x - mRowBadgeCenter.x;
                startY = mRowBadgeCenter.y - mInnertangentPoints.get(3).y;
            }
            float startAngle = 360 - (float) MathUtil.radianToAngle(MathUtil.getTanRadian(Math.atan(startY / startX),
                    mDragQuadrant - 1 == 0 ? 4 : mDragQuadrant - 1));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mDragPath.addArc(mRowBadgeCenter.x - startRadius, mRowBadgeCenter.y - startRadius,
                        mRowBadgeCenter.x + startRadius, mRowBadgeCenter.y + startRadius, startAngle,
                        180);
            } else {
                mDragPath.addArc(new RectF(mRowBadgeCenter.x - startRadius, mRowBadgeCenter.y - startRadius,
                        mRowBadgeCenter.x + startRadius, mRowBadgeCenter.y + startRadius), startAngle, 180);
            }
            canvas.drawPath(mDragPath, mBadgeBackgroundBorderPaint);
        }
    }

    private void drawBadge(Canvas canvas, PointF center, float radius) {
        if (center.x == -1000 && center.y == -1000) {
            return;
        }
        if (mBadgeText.isEmpty() || mBadgeText.length() == 1) {
            mBadgeBackgroundRect.left = center.x - (int) radius;
            mBadgeBackgroundRect.top = center.y - (int) radius;
            mBadgeBackgroundRect.right = center.x + (int) radius;
            mBadgeBackgroundRect.bottom = center.y + (int) radius;
            if (mDrawableBackground != null) {
                drawBadgeBackground(canvas);
            } else {
                canvas.drawCircle(center.x, center.y, radius, mBadgeBackgroundPaint);
                if (mColorBackgroundBorder != 0 && mBackgroundBorderWidth > 0) {
                    canvas.drawCircle(center.x, center.y, radius, mBadgeBackgroundBorderPaint);
                }
            }
        } else {
            mBadgeBackgroundRect.left = center.x - (mBadgeTextRect.width() / 2f + mBadgePadding);
            mBadgeBackgroundRect.top = center.y - (mBadgeTextRect.height() / 2f + mBadgePadding * 0.5f);
            mBadgeBackgroundRect.right = center.x + (mBadgeTextRect.width() / 2f + mBadgePadding);
            mBadgeBackgroundRect.bottom = center.y + (mBadgeTextRect.height() / 2f + mBadgePadding * 0.5f);
            radius = mBadgeBackgroundRect.height() / 2f;
            if (mDrawableBackground != null) {
                drawBadgeBackground(canvas);
            } else {
                canvas.drawRoundRect(mBadgeBackgroundRect, radius, radius, mBadgeBackgroundPaint);
                if (mColorBackgroundBorder != 0 && mBackgroundBorderWidth > 0) {
                    canvas.drawRoundRect(mBadgeBackgroundRect, radius, radius, mBadgeBackgroundBorderPaint);
                }
            }
        }
        if (!mBadgeText.isEmpty()) {
            canvas.drawText(mBadgeText, center.x,
                    (mBadgeBackgroundRect.bottom + mBadgeBackgroundRect.top
                            - mBadgeTextFontMetrics.bottom - mBadgeTextFontMetrics.top) / 2f,
                    mBadgeTextPaint);
        }
    }

    private void drawBadgeBackground(Canvas canvas) {
        mBadgeBackgroundPaint.setShadowLayer(0, 0, 0, 0);
        int left = (int) mBadgeBackgroundRect.left;
        int top = (int) mBadgeBackgroundRect.top;
        int right = (int) mBadgeBackgroundRect.right;
        int bottom = (int) mBadgeBackgroundRect.bottom;
        if (mDrawableBackgroundClip) {
            right = left + mBitmapClip.getWidth();
            bottom = top + mBitmapClip.getHeight();
            canvas.saveLayer(left, top, right, bottom, null, Canvas.ALL_SAVE_FLAG);
        }
        mDrawableBackground.setBounds(left, top, right, bottom);
        mDrawableBackground.draw(canvas);
        if (mDrawableBackgroundClip) {
            mBadgeBackgroundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawBitmap(mBitmapClip, left, top, mBadgeBackgroundPaint);
            canvas.restore();
            mBadgeBackgroundPaint.setXfermode(null);
            if (mBadgeText.isEmpty() || mBadgeText.length() == 1) {
                canvas.drawCircle(mBadgeBackgroundRect.centerX(), mBadgeBackgroundRect.centerY(),
                        mBadgeBackgroundRect.width() / 2f, mBadgeBackgroundBorderPaint);
            } else {
                canvas.drawRoundRect(mBadgeBackgroundRect,
                        mBadgeBackgroundRect.height() / 2, mBadgeBackgroundRect.height() / 2,
                        mBadgeBackgroundBorderPaint);
            }
        } else {
            canvas.drawRect(mBadgeBackgroundRect, mBadgeBackgroundBorderPaint);
        }
    }

    private void createClipLayer() {
        if (mBadgeText == null) {
            return;
        }
        if (!mDrawableBackgroundClip) {
            return;
        }
        if (mBitmapClip != null && !mBitmapClip.isRecycled()) {
            mBitmapClip.recycle();
        }
        float radius = getBadgeCircleRadius();
        if (mBadgeText.isEmpty() || mBadgeText.length() == 1) {
            mBitmapClip = Bitmap.createBitmap((int) radius * 2, (int) radius * 2,
                    Bitmap.Config.ARGB_4444);
            Canvas srcCanvas = new Canvas(mBitmapClip);
            srcCanvas.drawCircle(srcCanvas.getWidth() / 2f, srcCanvas.getHeight() / 2f,
                    srcCanvas.getWidth() / 2f, mBadgeBackgroundPaint);
        } else {
            mBitmapClip = Bitmap.createBitmap((int) (mBadgeTextRect.width() + mBadgePadding * 2),
                    (int) (mBadgeTextRect.height() + mBadgePadding), Bitmap.Config.ARGB_4444);
            Canvas srcCanvas = new Canvas(mBitmapClip);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                srcCanvas.drawRoundRect(0, 0, srcCanvas.getWidth(), srcCanvas.getHeight(), srcCanvas.getHeight() / 2f,
                        srcCanvas.getHeight() / 2f, mBadgeBackgroundPaint);
            } else {
                srcCanvas.drawRoundRect(new RectF(0, 0, srcCanvas.getWidth(), srcCanvas.getHeight()),
                        srcCanvas.getHeight() / 2f, srcCanvas.getHeight() / 2f, mBadgeBackgroundPaint);
            }
        }
    }

    private float getBadgeCircleRadius() {
        if (mBadgeText.isEmpty()) {
            return mBadgePadding;
        } else if (mBadgeText.length() == 1) {
            return mBadgeTextRect.height() > mBadgeTextRect.width() ?
                    mBadgeTextRect.height() / 2f + mBadgePadding * 0.5f :
                    mBadgeTextRect.width() / 2f + mBadgePadding * 0.5f;
        } else {
            return mBadgeBackgroundRect.height() / 2f;
        }
    }

    private void findBadgeCenter() {
        float rectWidth = mBadgeTextRect.height() > mBadgeTextRect.width() ?
                mBadgeTextRect.height() : mBadgeTextRect.width();
        switch (mBadgeGravity) {
            case Gravity.START | Gravity.TOP:
                mBadgeCenter.x = mGravityOffsetX + mBadgePadding + rectWidth / 2f;
                mBadgeCenter.y = mGravityOffsetY + mBadgePadding + mBadgeTextRect.height() / 2f;
                break;
            case Gravity.START | Gravity.BOTTOM:
                mBadgeCenter.x = mGravityOffsetX + mBadgePadding + rectWidth / 2f;
                mBadgeCenter.y = mHeight - (mGravityOffsetY + mBadgePadding + mBadgeTextRect.height() / 2f);
                break;
            case Gravity.END | Gravity.TOP:
                mBadgeCenter.x = mWidth - (mGravityOffsetX + mBadgePadding + rectWidth / 2f);
                mBadgeCenter.y = mGravityOffsetY + mBadgePadding + mBadgeTextRect.height() / 2f;
                break;
            case Gravity.END | Gravity.BOTTOM:
                mBadgeCenter.x = mWidth - (mGravityOffsetX + mBadgePadding + rectWidth / 2f);
                mBadgeCenter.y = mHeight - (mGravityOffsetY + mBadgePadding + mBadgeTextRect.height() / 2f);
                break;
            case Gravity.CENTER:
                mBadgeCenter.x = mWidth / 2f;
                mBadgeCenter.y = mHeight / 2f;
                break;
            case Gravity.CENTER | Gravity.TOP:
                mBadgeCenter.x = mWidth / 2f;
                mBadgeCenter.y = mGravityOffsetY + mBadgePadding + mBadgeTextRect.height() / 2f;
                break;
            case Gravity.CENTER | Gravity.BOTTOM:
                mBadgeCenter.x = mWidth / 2f;
                mBadgeCenter.y = mHeight - (mGravityOffsetY + mBadgePadding + mBadgeTextRect.height() / 2f);
                break;
            case Gravity.CENTER | Gravity.START:
                mBadgeCenter.x = mGravityOffsetX + mBadgePadding + rectWidth / 2f;
                mBadgeCenter.y = mHeight / 2f;
                break;
            case Gravity.CENTER | Gravity.END:
                mBadgeCenter.x = mWidth - (mGravityOffsetX + mBadgePadding + rectWidth / 2f);
                mBadgeCenter.y = mHeight / 2f;
                break;
        }
        initRowBadgeCenter();
    }

    private void measureText() {
        mBadgeTextRect.left = 0;
        mBadgeTextRect.top = 0;
        if (TextUtils.isEmpty(mBadgeText)) {
            mBadgeTextRect.right = 0;
            mBadgeTextRect.bottom = 0;
        } else {
            mBadgeTextPaint.setTextSize(mBadgeTextSize);
            mBadgeTextRect.right = mBadgeTextPaint.measureText(mBadgeText);
            mBadgeTextFontMetrics = mBadgeTextPaint.getFontMetrics();
            mBadgeTextRect.bottom = mBadgeTextFontMetrics.descent - mBadgeTextFontMetrics.ascent;
        }
        createClipLayer();
    }

    private void initRowBadgeCenter() {
        int[] screenPoint = new int[2];
        getLocationOnScreen(screenPoint);
        mRowBadgeCenter.x = mBadgeCenter.x + screenPoint[0];
        mRowBadgeCenter.y = mBadgeCenter.y + screenPoint[1];
    }

    protected void animateHide(PointF center) {
        if (mBadgeText == null) {
            return;
        }
        if (mAnimator == null || !mAnimator.isRunning()) {
            screenFromWindow(true);
            mAnimator = new BadgeAnimator(createBadgeBitmap(), center, this);
            mAnimator.start();
            setBadgeNumber(0);
        }
    }

    public void reset() {
        mDragCenter.x = -1000;
        mDragCenter.y = -1000;
        mDragQuadrant = 4;
        screenFromWindow(false);
        getParent().requestDisallowInterceptTouchEvent(false);
        invalidate();
    }

    public void hide(boolean animate) {
        if (animate && mActivityRoot != null) {
            initRowBadgeCenter();
            animateHide(mRowBadgeCenter);
        } else {
            setBadgeNumber(0);
        }
    }

    /**
     * @param badgeNumber equal to zero badge will be hidden, less than zero show dot
     */
    public XHBBadgeView setBadgeNumber(int badgeNumber) {
        mBadgeNumber = badgeNumber;
        if (mBadgeNumber < 0) {
            mBadgeText = "";
        } else if (mBadgeNumber > 99) {
            mBadgeText = mExact ? String.valueOf(mBadgeNumber) : "99+";
        } else if (mBadgeNumber > 0 && mBadgeNumber <= 99) {
            mBadgeText = String.valueOf(mBadgeNumber);
        } else if (mBadgeNumber == 0) {
            mBadgeText = null;
        }
        measureText();
        invalidate();
        return this;
    }

    public int getBadgeNumber() {
        return mBadgeNumber;
    }

    public XHBBadgeView setBadgeText(String badgeText) {
        mBadgeText = badgeText;
        mBadgeNumber = 1;
        measureText();
        invalidate();
        return this;
    }

    public String getBadgeText() {
        return mBadgeText;
    }

    public XHBBadgeView setExactMode(boolean isExact) {
        mExact = isExact;
        if (mBadgeNumber > 99) {
            setBadgeNumber(mBadgeNumber);
        }
        return this;
    }

    public boolean isExactMode() {
        return mExact;
    }

    public XHBBadgeView setShowShadow(boolean showShadow) {
        mShowShadow = showShadow;
        invalidate();
        return this;
    }

    public boolean isShowShadow() {
        return mShowShadow;
    }

    public XHBBadgeView setBadgeBackgroundColor(int color) {
        mColorBackground = color;
        if (mColorBackground == Color.TRANSPARENT) {
            mBadgeTextPaint.setXfermode(null);
        } else {
            mBadgeTextPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        }
        invalidate();
        return this;
    }

    public void setColorBackgroundBorder(int color) {
        this.mColorBackgroundBorder = color;
        invalidate();
    }

    public void setBackgroundBorderWidth(float width) {
        this.mBackgroundBorderWidth = dp2px(getContext(), width);
        invalidate();
    }

    public XHBBadgeView stroke(int color, float width, boolean isDpValue) {
        mColorBackgroundBorder = color;
        mBackgroundBorderWidth = isDpValue ? dp2px(getContext(), width) : width;
        invalidate();
        return this;
    }

    public int getBadgeBackgroundColor() {
        return mColorBackground;
    }

    public XHBBadgeView setBadgeBackground(Drawable drawable) {
        return setBadgeBackground(drawable, false);
    }

    public XHBBadgeView setBadgeBackground(Drawable drawable, boolean clip) {
        mDrawableBackgroundClip = clip;
        mDrawableBackground = drawable;
        createClipLayer();
        invalidate();
        return this;
    }

    public Drawable getBadgeBackground() {
        return mDrawableBackground;
    }

    public XHBBadgeView setBadgeTextColor(int color) {
        mColorBadgeText = color;
        invalidate();
        return this;
    }

    public int getBadgeTextColor() {
        return mColorBadgeText;
    }

    public XHBBadgeView setBadgeTextSize(float size, boolean isSpValue) {
        mBadgeTextSize = isSpValue ? dp2px(getContext(), size) : size;
        measureText();
        invalidate();
        return this;
    }

    private static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public float getBadgeTextSize(boolean isSpValue) {
        return isSpValue ? px2dp(getContext(), mBadgeTextSize) : mBadgeTextSize;
    }

    public XHBBadgeView setBadgePadding(float padding, boolean isDpValue) {
        mBadgePadding = isDpValue ? dp2px(getContext(), padding) : padding;
        createClipLayer();
        invalidate();
        return this;
    }

    public float getBadgePadding(boolean isDpValue) {
        return isDpValue ? px2dp(getContext(), mBadgePadding) : mBadgePadding;
    }

    public void setDraggable(boolean draggable) {
        this.mDraggable = draggable;
    }

    public boolean isDraggable() {
        return mDraggable;
    }

    /**
     * @param gravity only support Gravity.START | Gravity.TOP , Gravity.END | Gravity.TOP ,
     *                Gravity.START | Gravity.BOTTOM , Gravity.END | Gravity.BOTTOM ,
     *                Gravity.CENTER , Gravity.CENTER | Gravity.TOP , Gravity.CENTER | Gravity.BOTTOM ,
     *                Gravity.CENTER | Gravity.START , Gravity.CENTER | Gravity.END
     */
    public XHBBadgeView setBadgeGravity(int gravity) {
        if (gravity == (Gravity.START | Gravity.TOP) ||
                gravity == (Gravity.END | Gravity.TOP) ||
                gravity == (Gravity.START | Gravity.BOTTOM) ||
                gravity == (Gravity.END | Gravity.BOTTOM) ||
                gravity == (Gravity.CENTER) ||
                gravity == (Gravity.CENTER | Gravity.TOP) ||
                gravity == (Gravity.CENTER | Gravity.BOTTOM) ||
                gravity == (Gravity.CENTER | Gravity.START) ||
                gravity == (Gravity.CENTER | Gravity.END)) {
            mBadgeGravity = gravity;
            invalidate();
        } else {
            throw new IllegalStateException("only support Gravity.START | Gravity.TOP , Gravity.END | Gravity.TOP , " +
                    "Gravity.START | Gravity.BOTTOM , Gravity.END | Gravity.BOTTOM , Gravity.CENTER" +
                    " , Gravity.CENTER | Gravity.TOP , Gravity.CENTER | Gravity.BOTTOM ," +
                    "Gravity.CENTER | Gravity.START , Gravity.CENTER | Gravity.END");
        }
        return this;
    }

    public int getBadgeGravity() {
        return mBadgeGravity;
    }

    public XHBBadgeView setGravityOffset(float offset, boolean isDpValue) {
        return setGravityOffset(offset, offset, isDpValue);
    }

    public XHBBadgeView setGravityOffset(float offsetX, float offsetY, boolean isDpValue) {
        mGravityOffsetX = isDpValue ? dp2px(getContext(), offsetX) : offsetX;
        mGravityOffsetY = isDpValue ? dp2px(getContext(), offsetY) : offsetY;
        invalidate();
        return this;
    }

    public float getGravityOffsetX(boolean isDpValue) {
        return isDpValue ? px2dp(getContext(), mGravityOffsetX) : mGravityOffsetX;
    }

    public float getGravityOffsetY(boolean isDpValue) {
        return isDpValue ? px2dp(getContext(), mGravityOffsetY) : mGravityOffsetY;
    }


    private DragState dragState;

    public DragState getDragState() {
        return dragState;
    }

    private void updataListener(DragState state) {
        dragState = state;
        if (mDragStateChangedListener != null)
            mDragStateChangedListener.onDragStateChanged(state, this, mTargetView);
    }

    public XHBBadgeView setOnDragStateChangedListener(OnDragStateChangedListener l) {
        //mDraggable = l != null;
        mDragStateChangedListener = l;
        return this;
    }

    public PointF getDragCenter() {
        if (mDraggable && mDragging) return mDragCenter;
        return null;
    }

    private class BadgeContainer extends ViewGroup {

        @Override
        protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
            if(!(getParent() instanceof RelativeLayout)){
                super.dispatchRestoreInstanceState(container);
            }
        }

        public BadgeContainer(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            View targetView = null, badgeView = null;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (!(child instanceof XHBBadgeView)) {
                    targetView = child;
                } else {
                    badgeView = child;
                }
            }
            if (targetView == null) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            } else {
                targetView.measure(widthMeasureSpec, heightMeasureSpec);
                if (badgeView != null) {
                    badgeView.measure(MeasureSpec.makeMeasureSpec(targetView.getMeasuredWidth(), MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(targetView.getMeasuredHeight(), MeasureSpec.EXACTLY));
                }
                setMeasuredDimension(targetView.getMeasuredWidth(), targetView.getMeasuredHeight());
            }
        }
    }
}
