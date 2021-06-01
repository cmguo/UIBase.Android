package com.eazy.uibase.widget.calendar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.eazy.uibase.R;
import com.eazy.uibase.widget.calendar.bean.DateBean;
import com.eazy.uibase.widget.calendar.bean.DayBean;
import com.eazy.uibase.widget.calendar.bean.MonthBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


public class DateSelectedDialog extends DialogFragment implements View.OnClickListener {

    RecyclerView calendarRecycleView;
    private final List<DateBean> items = new ArrayList<>();  //所有的条目
    private final DateHelper mDateBeanHelper;   //处理日历逻辑的类
    private CalendarAdapter adapter;
    private DayBean start = new DayBean(); //选中的开始时间
    private DayBean end = new DayBean();  //选中的结束时间
    private int monthIndex = 10;          //当前最大的月份与开始的月的差
    private int startMonthIndex = -10;
    private TextView mYearTextView;
    private final DatePeriodSelectedCallback mCallback;
    private TextView mBtnOk;
    private boolean canLoadMore = true;

    public boolean isCanLoadMore() {
        return canLoadMore;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    public DateSelectedDialog(DatePeriodSelectedCallback callback, Calendar startDay, Calendar endDay) {
        mDateBeanHelper = new DateHelper();
        if (startDay != null) {
            start = mDateBeanHelper.getDayBeanByCalendar(startDay);
        }
        if (endDay != null) {
            end = mDateBeanHelper.getDayBeanByCalendar(endDay);
        }
        this.mCallback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_date_select_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        view.findViewById(R.id.current_day).setOnClickListener(this);
        view.findViewById(R.id.cancelBtn).setOnClickListener(this);
        mBtnOk = view.findViewById(R.id.okBtn);
        mBtnOk.setOnClickListener(this);
        mYearTextView = view.findViewById(R.id.current_year);
        calendarRecycleView = view.findViewById(R.id.rv_calendar);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(view.getContext(), 7);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                DateBean bean = items.get(position);
                if (bean instanceof MonthBean)
                    return 7;
                else return 1;
            }
        });
        calendarRecycleView.setLayoutManager(mGridLayoutManager);
        initItems();
        adapter = new CalendarAdapter(view.getContext(), items);
        calendarRecycleView.setAdapter(adapter);

        adapter.setOnItemClickListener(position -> {
            DayBean bean = (DayBean) items.get(position);
            if (bean == null)
                return;
            solveItemClick(bean);
        });
        ((SimpleItemAnimator) Objects.requireNonNull(calendarRecycleView.getItemAnimator())).setSupportsChangeAnimations(false);

        calendarRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mDateBeanHelper.isLastItemVisible(recyclerView, items.size()) && canLoadMore) {
                    int insertSize = mDateBeanHelper.loadMoreItems(items, monthIndex, true);
                    monthIndex++;
                    adapter.notifyItemRangeInserted(items.size() - insertSize - 1, insertSize);
                }

                if (mDateBeanHelper.isFirstItemVisible(recyclerView) && canLoadMore) {
                    startMonthIndex--;
                    int insertSize = mDateBeanHelper.loadMoreItems(items, startMonthIndex, false);
                    adapter.notifyItemRangeInserted(0, insertSize);
                }
                // 设置获取中间位置的年份展示在列表顶部
                GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                assert gridLayoutManager != null;
                int firstVisiblePosition = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
                int lastVisiblePosition = gridLayoutManager.findLastCompletelyVisibleItemPosition();
                DateBean dateBean = items.get((lastVisiblePosition + firstVisiblePosition) / 2);
                mYearTextView.setText(dateBean.getYear() + "年");
            }
        });
        calendarRecycleView.scrollToPosition(mDateBeanHelper.getTodayIndex() - 30);
        updateSelectedPeriod();
    }

    private void updateSelectedPeriod() {
        mDateBeanHelper.onEndClick(items, start, end);
        mBtnOk.setEnabled(true);
        adapter.notifyDataSetChanged();
    }

    public void show(FragmentManager manager) {
        this.show(manager, "date");
    }

    /**
     * 初始化日历控件的item
     */
    private void initItems() {
        int index = startMonthIndex;
        while (index <= monthIndex) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, index);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            items.addAll(mDateBeanHelper.getDateBeans(calendar));
            index++;
        }
    }

    /**
     * 解决点击事件
     */
    private void solveItemClick(DayBean bean) {
        if (bean.isSpace()) {
            return;
        }
        if (bean.getYear() == 0 || bean.getState() == DayBean.STATE_FUTURE)
            return;
        Calendar calendar = mDateBeanHelper.setCalendar(bean);
        Calendar startCalendar = mDateBeanHelper.setCalendar(start);
        if (start.getYear() != 0 && end.getYear() != 0) {
            start.setData(0, 0, 0);
            end.setData(0, 0, 0);
            mBtnOk.setEnabled(false);
        }
        if (start.getYear() == 0 || (calendar.getTimeInMillis() < startCalendar.getTimeInMillis())) {
            mDateBeanHelper.setAllNormal(items);
            bean.setState(DayBean.STATE_SELECTED);
            adapter.notifyDataSetChanged();
            start.setData(bean.getYear(), bean.getMonth(), bean.getDay());
            return;
        }

        if (end.getYear() == 0) {
            end.setData(bean.getYear(), bean.getMonth(), bean.getDay());
            updateSelectedPeriod();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.okBtn) {
            if (mCallback != null) {
                mCallback.onSelectedPeriod(mDateBeanHelper.setCalendar(start), mDateBeanHelper.setCalendar(end));
            }
            dismiss();
        } else if (v.getId() == R.id.cancelBtn) {
            dismiss();
        } else if (v.getId() == R.id.current_day) {
            calendarRecycleView.smoothScrollToPosition(getTodayMiddlePosition());
        }
    }

    /**
     * 准确计算能够是今天居中在列表中的position
     */
    private int getTodayMiddlePosition() {


        int firstVisiblePosition = ((GridLayoutManager) Objects.requireNonNull(calendarRecycleView.getLayoutManager())).findFirstCompletelyVisibleItemPosition();
        int lastVisiblePosition = ((GridLayoutManager) calendarRecycleView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
        int scrollToPosition;
        if (firstVisiblePosition > mDateBeanHelper.getTodayIndex()) {
            scrollToPosition = mDateBeanHelper.getTodayIndex() - (lastVisiblePosition - firstVisiblePosition) / 2;
        } else if (lastVisiblePosition < mDateBeanHelper.getTodayIndex()) {
            scrollToPosition = mDateBeanHelper.getTodayIndex() + (lastVisiblePosition - firstVisiblePosition) / 2;
        } else if (mDateBeanHelper.getTodayIndex() > (lastVisiblePosition - firstVisiblePosition) / 2) {
            scrollToPosition = mDateBeanHelper.getTodayIndex() - (lastVisiblePosition - firstVisiblePosition) / 2;
        } else {
            scrollToPosition = mDateBeanHelper.getTodayIndex() + (lastVisiblePosition - firstVisiblePosition) / 2;
        }
        return scrollToPosition;
    }

    public interface DatePeriodSelectedCallback {
        void onSelectedPeriod(Calendar start, Calendar end);
    }
}
