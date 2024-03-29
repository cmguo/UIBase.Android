package com.eazy.uibase.widget.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.eazy.uibase.R;
import com.eazy.uibase.widget.calendar.CalendarAdapter.DayViewHolder;
import com.eazy.uibase.widget.calendar.bean.DayBean;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class CalendarView extends FrameLayout {
    private static final String[] WeekSymbolString = {"日", "一", "二", "三", "四", "五", "六"};
    private static final long ONE_WEEK = 1000 * 60 * 60 * 24 * 7;

    private long startTime;
    private long endTime;
    private long selectedTime = System.currentTimeMillis();
    private DayBean selectedDay;
    private IDaySelectedCallback selectedCallback;
    private final BounceBackViewPager viewPager;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        @SuppressLint("Recycle") TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CalendarView);
        String startTimeString = ta.getString(R.styleable.CalendarView_startTime);
        if (!TextUtils.isEmpty(startTimeString))
            startTime = Long.parseLong(startTimeString);
        String endTimeString = ta.getString(R.styleable.CalendarView_endTime);
        if (!TextUtils.isEmpty(endTimeString))
            endTime = Long.parseLong(endTimeString);

        // week symbol view
        LinearLayout weekSymbolLayout = new LinearLayout(context);
        for (String symbol : WeekSymbolString) {
            TextView textView = new TextView(context);
            textView.setText(symbol);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            textView.setTextColor(context.getResources().getColor(R.color.bluegrey_500));
            textView.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
            layoutParams.weight = 1.0f;
            weekSymbolLayout.addView(textView, layoutParams);
        }
        addView(weekSymbolLayout, new LayoutParams(LayoutParams.MATCH_PARENT, UIUtil.dip2px(context, 34)));

        // week content view
        viewPager = new BounceBackViewPager(context);
        viewPager.setPadding(0, UIUtil.dip2px(context, 30), 0, 0);
        addView(viewPager, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    private void refreshViewPager() {
        if (viewPager == null)
            return;
        CalendarAdapter adapter = new CalendarAdapter(getContext());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(adapter.currentPages);
        adapter.notifyDataSetChanged();
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
        refreshViewPager();
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
        refreshViewPager();
    }

    public void setSelectedTime(long selectedTime) {
        this.selectedTime = selectedTime;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(selectedTime);
        selectedDay = new DayBean(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), DayBean.STATE_NORMAL);
        refreshViewPager();
    }

    public void setSelectedCallback(IDaySelectedCallback callback) {
        this.selectedCallback = callback;
    }

    class CalendarAdapter extends PagerAdapter {
        private final Context context;
        private final int pages;
        int currentPages;
        boolean isMonthPage = false;

        public CalendarAdapter(Context context) {
            this.context = context;
            Calendar calendar = Calendar.getInstance();
            if (isMonthPage) {
                calendar.setTimeInMillis(startTime);
                int startMonth = calendar.get(Calendar.DAY_OF_MONTH);
                calendar.setTimeInMillis(endTime);
                pages = calendar.get(Calendar.MONTH) - startMonth + 1;
                calendar.setTimeInMillis(selectedTime);
                currentPages = calendar.get(Calendar.MONTH) - startMonth;
            } else {
                calendar.setTimeInMillis(startTime);
                calendar.set(Calendar.DAY_OF_WEEK, 1);
                long startWeekTime = calendar.getTimeInMillis();
                calendar.setTimeInMillis(endTime);
                calendar.set(Calendar.DAY_OF_WEEK, 1);
                long endWeekTime = calendar.getTimeInMillis();
                pages = (int) ((endWeekTime - startWeekTime) / ONE_WEEK) + 1;
                calendar.setTimeInMillis(selectedTime);
                calendar.set(Calendar.DAY_OF_WEEK, 1);
                long currentWeekTime = calendar.getTimeInMillis();
                currentPages = (int) ((currentWeekTime + ONE_WEEK - 24*60*60*1000 - startTime) / ONE_WEEK);// 一天时间差校验
            }

        }

        @Override
        public int getCount() {
            return pages;
        }


        @NotNull
        @Override
        public Object instantiateItem(@NotNull ViewGroup container, int position) {
            RecyclerView recyclerView = new RecyclerView(context);
            GridLayoutManager layoutManager = new GridLayoutManager(context, 7);
            recyclerView.setLayoutManager(layoutManager);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(startTime);
            if (isMonthPage) {
                calendar.add(Calendar.MONTH, position - currentPages);
                recyclerView.setAdapter(new GridAdapter(context, calendar, new MonthCalendarDataStrategy()));
            } else {
                calendar = DateHelper.getCalendarWeekByDiff(startTime, position);
                recyclerView.setAdapter(new GridAdapter(context, calendar, new WeekCalendarDataStrategy()));
            }

            container.addView(recyclerView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            return recyclerView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, @NotNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NotNull View view, @NotNull Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(@NotNull Object object) {
            return POSITION_NONE;
        }
    }

    public interface IDaySelectedCallback {
        void daySelected(long time);
    }

    class GridAdapter extends RecyclerView.Adapter<DayViewHolder> {
        private final ArrayList<DayBean> dayList;
        private final Context context;

        public GridAdapter(Context context, Calendar calendar, ICalendarDataStrategy dataStrategy) {
            this.context = context;
            dayList = dataStrategy.getCalendarData(calendar);
            resetDayState();
        }


        @NonNull
        @Override
        public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.date_select_item_day, null);
            return new DayViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
            Resources resources = context.getResources();
            DayBean dayBean = dayList.get(position);
            if (dayBean.getState() == DayBean.STATE_TODAY) {
                holder.day.setTextColor(resources.getColor(R.color.blue_600));
            } else if (dayBean.getState() == DayBean.STATE_FUTURE) {
                holder.day.setTextColor(resources.getColor(R.color.bluegrey_900));
            } else if (dayBean.getState() == DayBean.STATE_SELECTED) {
                holder.day.setTextColor(resources.getColor(R.color.static_white_100));
            } else {
                holder.day.setTextColor(resources.getColor(R.color.bluegrey_500));
            }

            holder.day.setState(DayTextView.State.Normal);
            if (dayBean.getState() == DayBean.STATE_SELECTED) {
                holder.day.setState(DayTextView.State.Selected);
            }

            if (dayBean.isToady()) {
                holder.day.setText("今");
            } else {
                holder.day.setText(String.valueOf(dayBean.isSpace() ? "" : dayBean.getDay()));
            }

            holder.itemView.setOnClickListener(v -> {
                if (!dayBean.isSpace()) {
                    if (selectedDay == null || !dayBean.isSameDay(selectedDay)) {
                        selectedDay = dayBean;
                    }
                    if (CalendarView.this.selectedCallback != null) {
                        CalendarView.this.selectedCallback.daySelected(dayBean.getDayTime());
                    }
                    resetDayState();
                    notifyDataSetChanged();
                    Objects.requireNonNull(viewPager.getAdapter()).notifyDataSetChanged();

                }
            });
        }

        @Override
        public int getItemCount() {
            return dayList.size();
        }

        public void resetDayState() {
            for (DayBean bean : dayList) {
                if (selectedDay != null && bean.isSameDay(selectedDay)) {
                    bean.setState(DayBean.STATE_SELECTED);
                } else if (bean.isToady())
                    bean.setState(DayBean.STATE_TODAY);
                else if (bean.withByDayRange(startTime, endTime))
                    bean.setState(DayBean.STATE_FUTURE);
                else {
                    bean.setState(DayBean.STATE_NORMAL);
                }
            }
        }

    }

    interface ICalendarDataStrategy {
        ArrayList<DayBean> getCalendarData(Calendar calendar);
    }

    static class WeekCalendarDataStrategy implements ICalendarDataStrategy {
        @Override
        public ArrayList<DayBean> getCalendarData(Calendar calendar) {
            ArrayList<DayBean> dayList = new ArrayList<>(7);
            for (int i = 0; i < 7; i++) {
                calendar.set(Calendar.DAY_OF_WEEK, i + 1);
                DayBean dayBean = new DayBean(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), DayBean.STATE_NORMAL);
                dayList.add(dayBean);
            }
            return dayList;
        }
    }

    static class MonthCalendarDataStrategy implements ICalendarDataStrategy {
        @Override
        public ArrayList<DayBean> getCalendarData(Calendar calendar) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            ArrayList<DayBean> dayList = new ArrayList<>();
            int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            for (int i = 0; i < firstDayOfWeek - 1; i++) { //空格部分
                DayBean dayBean = new DayBean();
                dayBean.setSpace(true);
                dayList.add(dayBean);
            }

            for (int i = 1; i <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                DayBean dayBean = new DayBean(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), i, DayBean.STATE_NORMAL);
                dayList.add(dayBean);
            }
            return dayList;
        }
    }


    public static class BounceBackViewPager extends ViewPager {

        private int currentPosition = 0;
        private final Rect mRect = new Rect();
        private boolean handleDefault = true;
        private float preX = 0f;
        private float startX = 0f;
        private static final float RATIO = 0.5f;
        private static final float SCROLL_WIDTH = 10f;

        public BounceBackViewPager(Context context) {
            super(context);
        }

        public BounceBackViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
        }


        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {

            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                preX = ev.getRawX();
                startX = ev.getRawX();
                currentPosition = getCurrentItem();
                mRect.setEmpty();
            }
            if (ev.getAction() == MotionEvent.ACTION_MOVE) {
                int endX = (int) ev.getRawX();
                if (Math.abs(endX - startX) > SCROLL_WIDTH) {
                    if (endX > startX) {
                        if (getCurrentItem() == 0) {
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }
                    } else {
                        if (getCurrentItem() == Objects.requireNonNull(getAdapter()).getCount() - 1) {
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }
                    }
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
            }
            return super.dispatchTouchEvent(ev);
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    onTouchActionUp();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (Objects.requireNonNull(getAdapter()).getCount() == 1) {
                        float nowX = ev.getRawX();
                        float offset = nowX - preX;
                        float diff = nowX - startX;
                        preX = nowX;
                        if (diff > SCROLL_WIDTH) {
                            whetherConditionIsRight(offset);
                        } else if (diff < -SCROLL_WIDTH) {
                            whetherConditionIsRight(offset);
                        } else if (!handleDefault) {
                            if (getLeft() + (int) (offset * RATIO) != mRect.left) {
                                layout(getLeft() + (int) (offset * RATIO), getTop(), getRight() + (int) (offset * RATIO), getBottom());
                            }
                        }
                    } else if ((currentPosition == 0 || currentPosition == getAdapter().getCount() - 1)) {
                        float nowX = ev.getRawX();
                        float offset = nowX - preX;
                        float diff = nowX - startX;
                        preX = nowX;
                        if (currentPosition == 0) {
                            if (diff > SCROLL_WIDTH) {
                                whetherConditionIsRight(offset);
                            } else if (!handleDefault) {
                                if (getLeft() + (int) (offset * RATIO) >= mRect.left) {
                                    layout(getLeft() + (int) (offset * RATIO), getTop(), getRight() + (int) (offset * RATIO), getBottom());
                                }
                            }
                        } else {
                            if (diff < -SCROLL_WIDTH) {
                                whetherConditionIsRight(offset);
                            } else if (!handleDefault) {
                                if (getRight() + (int) (offset * RATIO) <= mRect.right) {
                                    layout(getLeft() + (int) (offset * RATIO), getTop(), getRight() + (int) (offset * RATIO), getBottom());
                                }
                            }
                        }
                    } else {
                        handleDefault = true;
                    }

                    if (!handleDefault) {
                        return true;
                    }
                default:
                    break;
            }
            return super.onTouchEvent(ev);
        }


        private void whetherConditionIsRight(float offset) {
            if (mRect.isEmpty()) {
                mRect.set(getLeft(), getTop(), getRight(), getBottom());
            }
            handleDefault = false;
            layout(getLeft() + (int) (offset * RATIO), getTop(), getRight() + (int) (offset * RATIO), getBottom());
        }

        private void onTouchActionUp() {
            if (!mRect.isEmpty()) {
                recoveryPosition();
            }
        }

        private void recoveryPosition() {
            TranslateAnimation ta = new TranslateAnimation(getLeft(), mRect.left, 0, 0);
            ta.setDuration(300);
            startAnimation(ta);
            layout(mRect.left, mRect.top, mRect.right, mRect.bottom);
            mRect.setEmpty();
            handleDefault = true;
        }

    }
}
