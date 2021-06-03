package com.eazy.uibase.widget.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.eazy.uibase.R;
import com.eazy.uibase.widget.calendar.bean.DateBean;
import com.eazy.uibase.widget.calendar.bean.DayBean;
import com.eazy.uibase.widget.calendar.bean.MonthBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class CalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int DAY_VT = 1003;
    private static final int MONTH_VT = 1005;
    private final List<DateBean> items;
    private final Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public CalendarAdapter(Context mContext, List<DateBean> items) {
        this.items = items;
        this.mContext = mContext;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == DAY_VT) {
            @SuppressLint("InflateParams") View view = LayoutInflater.from(mContext).inflate(R.layout.date_select_item_day, null);
            view.setTag(DAY_VT);
            return new DayViewHolder(view);
        } else {
            @SuppressLint("InflateParams") View view = LayoutInflater.from(mContext).inflate(R.layout.date_select_dialog_item_month, null);
            view.setTag(MONTH_VT);
            return new MonthViewHolder(view);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@Nullable RecyclerView.ViewHolder holder, final int position) {
        Resources resources = mContext.getResources();
        if (holder instanceof DayViewHolder) {
            DayBean item = (DayBean) items.get(position);
            if (item == null)
                return;
            if (item.getDay() > 0)
                ((DayViewHolder) holder).day.setText("" + item.getDay());
            else ((DayViewHolder) holder).day.setText("");
            if (item.getState() == DayBean.STATE_FUTURE) {
                ((DayViewHolder) holder).day.setTextColor(resources.getColor(R.color.day_text_past_gray));
            } else if (item.getState() == DayBean.STATE_TODAY) {
                ((DayViewHolder) holder).day.setTextColor(resources.getColor(R.color.day_text_normal));
            } else {
                ((DayViewHolder) holder).day.setTextColor(resources.getColor(R.color.day_text_normal_black));
            }

            ((DayViewHolder) holder).day.setState(DayTextView.State.Normal);
            ((DayViewHolder) holder).day.setColumnStart(item.isColumnStart());
            ((DayViewHolder) holder).day.setColumnEnd(item.isColumnEnd());
            if (item.getState() == DayBean.STATE_STARTED) {
                ((DayViewHolder) holder).day.setTextColor(resources.getColor(R.color.bluegrey_00));
                ((DayViewHolder) holder).day.setState(DayTextView.State.SelectStart);
            } else if (item.getState() == DayBean.STATE_END) {
                ((DayViewHolder) holder).day.setTextColor(resources.getColor(R.color.bluegrey_00));
                ((DayViewHolder) holder).day.setState(DayTextView.State.SelectEnd);
            } else if (item.getState() == DayBean.STATE_SELECTED) {
                ((DayViewHolder) holder).day.setTextColor(resources.getColor(R.color.bluegrey_00));
                ((DayViewHolder) holder).day.setState(DayTextView.State.Selected);
            } else if (item.getState() == DayBean.STATE_PERIOD_SELECTED) {
                ((DayViewHolder) holder).day.setState(DayTextView.State.SelectPeriod);
            }
            holder.itemView.setOnClickListener(v -> {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(position);
            });
        } else if (holder instanceof MonthViewHolder) {
            MonthBean item = (MonthBean) items.get(position);
            if (item != null && item.getMonth() != null) {
                ((MonthViewHolder) holder).month.setText(item.getMonth());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        DateBean item = items.get(position);
        if (item instanceof DayBean) {
            return DAY_VT;
        } else {
            return MONTH_VT;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public static class DayViewHolder extends RecyclerView.ViewHolder {
        DayTextView day;

        public DayViewHolder(View view) {
            super(view);
            day = view.findViewById(R.id.day);
        }
    }

    static class MonthViewHolder extends RecyclerView.ViewHolder {
        TextView month;

        public MonthViewHolder(View view) {
            super(view);
            month = view.findViewById(R.id.date_month_name);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
