package com.xhb.uibase.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.xhb.uibase.R;

public class DeviderDecoration extends RecyclerView.ItemDecoration {

    private int deviderHeight;
    private Paint dividerPaint;

    public DeviderDecoration(Context context) {
        //设置画笔
        dividerPaint = new Paint();
        //设置分割线颜色
        dividerPaint.setColor(context == null ? Color.GRAY : context.getColor(R.color.bluegrey_100));
        //设置分割线宽度
        deviderHeight = 1;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //改变宽度
        outRect.bottom = deviderHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //得到列表所有的条目
        int childCount = parent.getChildCount();
        //得到条目的宽和高
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            //计算每一个条目的顶点和底部 float值
            float top = view.getBottom();
            float bottom = view.getBottom() + deviderHeight;
            //重新绘制
            c.drawRect(left, top, right, bottom, dividerPaint);
        }
    }
}
