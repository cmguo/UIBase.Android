package com.eazy.uibase.widget.calendar;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.eazy.uibase.R;


@SuppressLint("AppCompatCustomView")
public class DayTextView extends TextView {

    /**
     * 记录当前日期状态,正常状态，选中状态，选中范围开始，选中范围结束,选中范围中间
     */
    public enum State {
        Normal, SelectStart, SelectEnd, Selected, SelectPeriod
    }

    private State mState = State.Normal;

    private int selectedBgColor;
    private int selectStartBgColor;
    private int selectEndBgColor;
    private int selectPeriodBgColor;
    private int normalBgColor;
    private Drawable selectedDrawable;
    private Paint mBgPaint;
    private boolean isColumnStart = false;
    private boolean isColumnEnd = false;

    public void setState(State state) {
        this.mState = state;
    }

    public State getState() {
        return mState;
    }


    public DayTextView(Context context) {
        super(context);
        init(context, null);
    }

    public DayTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attributeSet) {
        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.DayTextView);
        selectedBgColor = ta.getColor(R.styleable.DayTextView_selectedBg, 0);
        selectStartBgColor = ta.getColor(R.styleable.DayTextView_selectStartBg, 0);
        selectEndBgColor = ta.getColor(R.styleable.DayTextView_selectEndBg, 0);
        selectPeriodBgColor = ta.getColor(R.styleable.DayTextView_selectPeriodBg, 0);
        normalBgColor = ta.getColor(R.styleable.DayTextView_selectedNormalBg, 0);
        selectedDrawable = ta.getDrawable(R.styleable.DayTextView_selectedBgDrawable);
        ta.recycle();

        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setStyle(Paint.Style.FILL);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 先绘制背景
        drawBg(canvas);
        super.onDraw(canvas);
    }

    private void drawBg(Canvas canvas) {
        int height = getHeight();
        int width = getWidth();
        if (height > width) {
            return;
        }
        switch (mState) {
            case Normal:
                if (normalBgColor != 0) {
                    mBgPaint.setColor(normalBgColor);
                    canvas.drawRect((width - height) * 0.5f, 0, (width + height) * 0.5f, height, mBgPaint);
                }
                break;
            case Selected:
                if (selectedBgColor != 0) {
                    mBgPaint.setColor(selectedBgColor);
                    canvas.drawRect((width - height) * 0.5f, 0, (width + height) * 0.5f, height, mBgPaint);
                } else if (selectedDrawable != null) {
                    selectedDrawable.setBounds((int) ((width - height) * 0.5f), 0, (int) ((width + height) * 0.5f), height);
                    selectedDrawable.draw(canvas);
                }
                break;
            case SelectEnd:
                // 选中end是列首，要不绘制选中背景
                if (selectPeriodBgColor != 0 && !isColumnStart) {
                    mBgPaint.setColor(selectPeriodBgColor);
                    canvas.drawRect(0, 0, (width + height) * 0.5f, height, mBgPaint);
                }
                if (selectEndBgColor != 0) {
                    mBgPaint.setColor(selectEndBgColor);
                    canvas.drawRect((width - height) * 0.5f, 0, (width + height) * 0.5f, height, mBgPaint);
                } else if (selectedDrawable != null) {
                    selectedDrawable.setBounds((int) ((width - height) * 0.5f), 0, (int) ((width + height) * 0.5f), height);
                    selectedDrawable.draw(canvas);
                }
                break;
            case SelectStart:
                // 选中首个是列尾是否处理
                if (selectPeriodBgColor != 0 && !isColumnEnd) {
                    mBgPaint.setColor(selectPeriodBgColor);
                    canvas.drawRect((width + height) * 0.5f, 0, width, height, mBgPaint);
                }

                if (selectStartBgColor != 0) {
                    mBgPaint.setColor(selectStartBgColor);
                    canvas.drawRect((width - height) * 0.5f, 0, (width + height) * 0.5f, height, mBgPaint);
                } else if (selectedDrawable != null) {
                    selectedDrawable.setBounds(((int) ((width - height) * 0.5f)), 0, (int) ((width + height) * 0.5f), height);
                    selectedDrawable.draw(canvas);
                }
                break;
            case SelectPeriod:
                // 列首列尾巴不同的处理
                int left = isColumnStart ? (int) ((width - height) * 0.5) : 0;
                int right = isColumnEnd ? (int) ((width + height) * 0.5f) : width;
                if (selectPeriodBgColor != 0) {
                    mBgPaint.setColor(selectPeriodBgColor);
                    canvas.drawRect(left, 0, right, height, mBgPaint);
                }
                break;
        }
    }

    public void setColumnStart(boolean columnStart) {
        isColumnStart = columnStart;
    }

    public void setColumnEnd(boolean columnEnd) {
        isColumnEnd = columnEnd;
    }

}
