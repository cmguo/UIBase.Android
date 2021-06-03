package com.eazy.uibase.widget.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.eazy.uibase.R;
import com.eazy.uibase.widget.calendar.CalendarAdapter.DayViewHolder;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarView extends FrameLayout {
    private ViewPager viewPager;
    private boolean isMonthPage = true;
    private long currentSelectedDay = 0;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewPager = new ViewPager(context);
        addView(viewPager, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        CalendarAdapter adapter = new CalendarAdapter(context);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(Integer.MAX_VALUE / 2);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
    }

    class CalendarAdapter extends PagerAdapter implements AdapterView.OnItemClickListener {
        private SparseArray<RecyclerView> mViewMap = new SparseArray<>();
        private Context context;
        private int currentPosition;

        public CalendarAdapter(Context context) {
            this.context = context;
            currentPosition = Integer.MAX_VALUE / 2;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            RecyclerView recyclerView = new RecyclerView(context);
            GridLayoutManager layoutManager = new GridLayoutManager(context, 7);
            recyclerView.setLayoutManager(layoutManager);
            mViewMap.put(position, recyclerView);
            Calendar calendar = Calendar.getInstance();
            if (isMonthPage) {
                calendar.add(Calendar.MONTH, position - currentPosition);
            } else {
                calendar = DateHelper.getCalendarWeekByDiff(position - currentPosition);
            }

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int week = calendar.get(Calendar.WEEK_OF_YEAR);
            DateBean dateBean = new DateBean(year, month + 1, week);
            recyclerView.setAdapter(new MonthGridAdapter(context, dateBean));
            container.addView(recyclerView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            return recyclerView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MonthGridAdapter gridAdapter = (MonthGridAdapter) parent.getAdapter();
            int day = gridAdapter.getItem(position);
            if (day == -1) {
                return;
            }
            DateBean bean = gridAdapter.getDateBean();
        }
    }

    class MonthGridAdapter extends RecyclerView.Adapter<DayViewHolder> {


        private DateBean mDateBean;
        private int days;
        private int dayOfWeeks;
        private Context context;


        public DateBean getDateBean() {
            return mDateBean;
        }

        public MonthGridAdapter(Context context, DateBean dateBean) {
            this.mDateBean = dateBean;
            this.context = context;
            GregorianCalendar c = new GregorianCalendar(dateBean.currentYear, dateBean.currentMonth - 1, 0);
            if (isMonthPage) {
                days = DateHelper.getDaysOfMonth(dateBean.currentYear, dateBean.currentMonth); //返回当前月的总天数。
                dayOfWeeks = c.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeeks == 7) {
                    dayOfWeeks = 0;
                }
            } else {
                days = 7;
            }

        }

        public int getItem(int i) {
            if (isMonthPage) {
                if (i < dayOfWeeks) {
                    return -1;
                } else {
                    return i - dayOfWeeks;
                }
            } else {
                GregorianCalendar c = new GregorianCalendar(mDateBean.currentYear, 0, 0);
                c.set(Calendar.WEEK_OF_YEAR, mDateBean.currentWeek);
                c.set(Calendar.DAY_OF_WEEK, 1 + i);
                return c.get(Calendar.DAY_OF_MONTH) - 1;
            }
        }

        @NonNull
        @Override
        public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.date_select_item_day, null);
            return new com.eazy.uibase.widget.calendar.CalendarAdapter.DayViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
            Resources resources = context.getResources();
            String dayString = getItem(position) >= 0 ? String.valueOf(getItem(position) + 1) : "";
            holder.day.setText(dayString);
            holder.day.setTextColor(resources.getColor(R.color.day_text_past_gray));
            holder.itemView.setOnClickListener(v -> {
            });
        }

        @Override
        public int getItemCount() {
            if (isMonthPage)
                return days + dayOfWeeks;
            else
                return days;

        }

    }

    class DateBean {
        int currentYear;
        int currentMonth;
        int currentWeek;

        public DateBean(int currentYear, int currentMonth, int currentWeek) {
            this.currentYear = currentYear;
            this.currentMonth = currentMonth;
            this.currentWeek = currentWeek;
        }
    }


    class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        @Override
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}
