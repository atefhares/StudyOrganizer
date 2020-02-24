package com.example.atefhares.study_organizer.Calendar;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.atefhares.study_organizer.DataClasses.classData;
import com.example.atefhares.study_organizer.DataClasses.termData;
import com.example.atefhares.study_organizer.DataClasses.timeData;
import com.example.atefhares.study_organizer.DatabaseRelated.DBHelper;
import com.example.atefhares.study_organizer.NewOrEditActivities.ClassDetailsActivity;
import com.example.atefhares.study_organizer.R;
import com.example.atefhares.study_organizer.UtilitiesClasses.Utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Atef Hares on 20-Apr-16.
 */

public class CalendarFragment extends Fragment {

    private DBHelper mDBHelper;
    private float mRow_Height;
    private int mColumn_Width;
    private RelativeLayout RL;
    private ScrollView mScrollView;
    private boolean mCurrentWeekOrNot;

    private int CurrentAdapterPosition;
    private String mFirstDayOfWeek = "", mLastDayOfWeek = "";
    View rootView;

    static boolean DefaultScrollChanged = false;


    public CalendarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        RL = (RelativeLayout) rootView.findViewById(R.id.RL);

        //Step 1
        mDBHelper = DBHelper.getInstance(getActivity());

        //Step 2
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCurrentWeekOrNot = bundle.getBoolean("CurrentWeek");
            CurrentAdapterPosition = bundle.getInt("CurrentViewPagerPosition");
        }


        Log.w("POOOOS", CurrentAdapterPosition + "");

        String Week_Date_details[] = getCurrentWeekDate(CurrentAdapterPosition - 50000);
        mFirstDayOfWeek = Week_Date_details[0];
        mLastDayOfWeek = Week_Date_details[1];

        //Step 3
        Create_Events_On_the_Calendar(false, rootView);


        /***************************************************************************************/
        mScrollView = (ScrollView) rootView.findViewById(R.id.SV);

        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences(String.valueOf(R.string.SharedPreferencesName), getActivity().MODE_PRIVATE);
        mScrollView.post(new Runnable() {
            public void run() {
//                    scrolling to 8 am
//                int def = sharedPreferences.getInt("defSCR",1248);
                mScrollView.scrollTo(0, (int) (7.8 * mRow_Height));
            }
        });

//        mScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                sharedPreferences.edit().putInt("LastScrollY", scrollY).apply();
//            }
//        });

//        mScrollView.post(new Runnable() {
//            public void run() {
////                if (!DefaultScrollChanged) {
////                     float scrollY = Float.parseFloat(sharedPreferences.getString("LastStartTimeForCalendar","7.8"));
////                    scrolling to 8 am
////                    mScrollView.scrollTo(0, (int) (7.8 * mRow_Height));
////                } else {
//                    int scrollY = sharedPreferences.getInt("LastScrollY", 8);
//                    mScrollView.scrollTo(0, scrollY );
////                }
//            }
//        });

//        mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                Log.w("GetY " +CurrentAdapterPosition+" = ",mScrollView.getScrollY()+"");
//                sharedPreferences.edit().putInt("defSCR",mScrollView.getScrollY()).apply();
//            }
//        });
        /***************************************************************************************/


        return rootView;
    }

    private String[] getCurrentWeekDate(int Indicator) {
        Calendar c = Calendar.getInstance();
        /**************************************************************************************************************************/
        c.add(Calendar.DATE, Indicator * 7);
        /*************************************************======= Generating the Title =====*********************************************/
        int CurrentDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int FirstDayOfWeek = 0, LastDayOfWeek = 0, MonthOfTheFirstDayOfWeek = 0, MonthOfTheLastDayOfWeek = 0, YearOfTheFirstDayOfWeek = 0, YearOfTheLastDayOfWeek = 0;

        switch (CurrentDayOfWeek) {
            case Calendar.SATURDAY:
                c.add(Calendar.DATE, -0);
                FirstDayOfWeek = c.get(Calendar.DAY_OF_MONTH);
                MonthOfTheFirstDayOfWeek = c.get(Calendar.MONTH);
                YearOfTheFirstDayOfWeek = c.get(Calendar.YEAR);
                break;
            case Calendar.SUNDAY:
                c.add(Calendar.DATE, -1);
                FirstDayOfWeek = c.get(Calendar.DAY_OF_MONTH);
                MonthOfTheFirstDayOfWeek = c.get(Calendar.MONTH);
                YearOfTheFirstDayOfWeek = c.get(Calendar.YEAR);
                break;
            case Calendar.MONDAY:
                c.add(Calendar.DATE, -2);
                FirstDayOfWeek = c.get(Calendar.DAY_OF_MONTH);
                MonthOfTheFirstDayOfWeek = c.get(Calendar.MONTH);
                YearOfTheFirstDayOfWeek = c.get(Calendar.YEAR);
                break;
            case Calendar.TUESDAY:
                c.add(Calendar.DATE, -3);
                FirstDayOfWeek = c.get(Calendar.DAY_OF_MONTH);
                MonthOfTheFirstDayOfWeek = c.get(Calendar.MONTH);
                YearOfTheFirstDayOfWeek = c.get(Calendar.YEAR);
                break;
            case Calendar.WEDNESDAY:
                c.add(Calendar.DATE, -4);
                FirstDayOfWeek = c.get(Calendar.DAY_OF_MONTH);
                MonthOfTheFirstDayOfWeek = c.get(Calendar.MONTH);
                YearOfTheFirstDayOfWeek = c.get(Calendar.YEAR);
                break;
            case Calendar.THURSDAY:
                c.add(Calendar.DATE, -5);
                FirstDayOfWeek = c.get(Calendar.DAY_OF_MONTH);
                MonthOfTheFirstDayOfWeek = c.get(Calendar.MONTH);
                YearOfTheFirstDayOfWeek = c.get(Calendar.YEAR);
                break;
            case Calendar.FRIDAY:
                c.add(Calendar.DATE, -6);
                FirstDayOfWeek = c.get(Calendar.DAY_OF_MONTH);
                MonthOfTheFirstDayOfWeek = c.get(Calendar.MONTH);
                YearOfTheFirstDayOfWeek = c.get(Calendar.YEAR);
                break;
        }


        c.add(Calendar.DATE, 6);
        LastDayOfWeek = c.get(Calendar.DAY_OF_MONTH);
        MonthOfTheLastDayOfWeek = c.get(Calendar.MONTH);
        YearOfTheLastDayOfWeek = c.get(Calendar.YEAR);

        return new String[]{FirstDayOfWeek + "/" + MonthOfTheFirstDayOfWeek + "/" + YearOfTheFirstDayOfWeek, LastDayOfWeek + "/" + MonthOfTheLastDayOfWeek + "/" + YearOfTheLastDayOfWeek};
    }

    private String Which_Term_this_Day_belongs_to(Calendar dayDate) {
        ArrayList<termData> Terms = mDBHelper.getAllTerms();
        for (termData term : Terms) {
            if (This_dayDate_belongsTo_That_term(dayDate, term)) {
                return term.getID();
            }
        }
        return null;
    }

    private boolean This_dayDate_belongsTo_That_term(Calendar dayDate, termData term) {

        Date DayDate = dayDate.getTime();

        String term_StartDate_Details[] = term.getStartDate().split("/");
        Calendar StartCalendar = Calendar.getInstance();
        StartCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(term_StartDate_Details[0]));
        StartCalendar.set(Calendar.MONTH, Integer.parseInt(term_StartDate_Details[1]));
        StartCalendar.set(Calendar.YEAR, Integer.parseInt(term_StartDate_Details[2]));
        Date term_StartDate = StartCalendar.getTime();

        String term_EndDate_Details[] = term.getEndDate().split("/");
        Calendar EndCalendar = Calendar.getInstance();
        EndCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(term_EndDate_Details[0]));
        EndCalendar.set(Calendar.MONTH, Integer.parseInt(term_EndDate_Details[1]));
        EndCalendar.set(Calendar.YEAR, Integer.parseInt(term_EndDate_Details[2]));
        Date term_EndDate = EndCalendar.getTime();

        return DayDate.after(term_StartDate) && DayDate.before(term_EndDate);
//        return dayDate.getTimeInMillis() >= StartCalendar.getTimeInMillis() && dayDate.getTimeInMillis() <= EndCalendar.getTimeInMillis();

    }

    private void Create_Events_On_the_Calendar(final boolean Update, final View rootView) {
        final TextView heightTextView = (TextView) rootView.findViewById(R.id.h12pm);
        final TextView widthTextView = (TextView) rootView.findViewById(R.id.SatTextView);
        final ViewTreeObserver viewTreeObserver = RL.getViewTreeObserver();

        //****************************************************Dynamic view will be created from here**********************************************************//
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                RL.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                mColumn_Width = widthTextView.getWidth();//getting the width of days Text View
                mRow_Height = heightTextView.getHeight();//getting the height of hours Text View

                /*********************************************************************************************************************/
                //Creating the lines indicating half hours for the whole day
                for (int i = 0; i < 24; i++) {
                    View tv = new View(getActivity().getApplicationContext());
                    RelativeLayout.LayoutParams Params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 1);
                    tv.setLayoutParams(Params1);
                    tv.setBackgroundResource(R.color.HalfHoursTime_Line);
                    tv.setY((float) (minutes_to_hours(i * 60 + 30) * mRow_Height));
                    RL.addView(tv);
                }
                /*********************************************************************************************************************/

                /*********************************************************************************************************************/
                //Creating the lines indicating hours for the whole day
                for (int i = 0; i < 24; i++) {
                    View tv = new View(getActivity().getApplicationContext());
                    RelativeLayout.LayoutParams Params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 2);
                    tv.setLayoutParams(Params1);
                    tv.setBackgroundResource(R.color.HalfHoursTime_Line);
                    tv.setY((float) (minutes_to_hours(i * 60) * mRow_Height));
                    RL.addView(tv);
                }
                /*********************************************************************************************************************/

                /*********************************************************************************************************************/
                //Creating the lines indicating hours for the whole day
                for (int i = 0; i < 7; i++) {
                    View tv = new View(getActivity().getApplicationContext());
                    RelativeLayout.LayoutParams Params1 = new RelativeLayout.LayoutParams(2, RelativeLayout.LayoutParams.MATCH_PARENT);
                    tv.setLayoutParams(Params1);
                    tv.setBackgroundResource(R.color.HalfHoursTime_Line);
                    tv.setX((float) (i * mColumn_Width));
                    RL.addView(tv);
                }
                /*********************************************************************************************************************/


                if (mCurrentWeekOrNot) {
                    make_CurrentDay_Column_Colored(rootView);
                }


                /*********************************************************************************************************************/
                //Creating Classes events [classes times] on the calendar

                String[] mFirstDayOfWeek_details = mFirstDayOfWeek.split("/");
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(mFirstDayOfWeek_details[0]));
                calendar.set(Calendar.MONTH, Integer.parseInt(mFirstDayOfWeek_details[1]));
                calendar.set(Calendar.YEAR, Integer.parseInt(mFirstDayOfWeek_details[2]));

                for (int i = 0; i < 7; i++) {
                    String TermID = Which_Term_this_Day_belongs_to(calendar);
                    ArrayList<timeData> times = mDBHelper.getAllTimes_ForTheCalendar(TermID, Utilities.GetDayName(calendar.get(Calendar.DAY_OF_WEEK)));
                    if (!times.isEmpty()) {
                        for (timeData data : times) {
                            if (Update) {
                                classData temp = mDBHelper.getClass(data.getClassID());
                                RL.removeView(RL.findViewById(Integer.parseInt(temp.getID())));
                            }

                            CreateEvent(
                                    Integer.parseInt(data.getDuration()),
                                    data.getStartTime(),
                                    Utilities.getCalendarDay_from_name(data.getDay()),
                                    mDBHelper.getClass(data.getClassID())
                            );
                        }
                    }
                    calendar.add(Calendar.DATE, 1);
                }

                if (CurrentAdapterPosition <= 50000) {
                    Show_finished_days_style();
                }

//                CreateEvent(30, 6.5f, 4,null);
//                CreateEvent(60, 8.2f, 6, null);
//                CreateEvent(300, 13, 6, null);
//                CreateEvent(120, 7, 7, null);
//                CreateEvent(120, 9, 7, null);
//                CreateEvent(340, 5, 2, null);
                /*********************************************************************************************************************/


                if (mCurrentWeekOrNot) {
                    /*********************************************************************************************************************/
                    //Creating the line indicating Current time
                    Calendar c = Calendar.getInstance();
                    int Am_or_PM = c.get(Calendar.AM_PM);
                    int current_hour = c.get(Calendar.HOUR);
                    int current_mins = c.get(Calendar.MINUTE);
                    if (Am_or_PM == Calendar.PM)
                        current_hour += 12;
                    int current_time_in_mins = current_hour * 60 + current_mins;
                    TextView tv = new TextView(getActivity().getApplicationContext());
                    RelativeLayout.LayoutParams Params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 5);
                    tv.setLayoutParams(Params1);
                    tv.setBackgroundResource(R.color.CurrentTime_Line);
                    tv.setY((float) (minutes_to_hours(current_time_in_mins) * mRow_Height));
                    RL.addView(tv);
                    /*********************************************************************************************************************/
                }

            }
        });
        //**********************************************************************************************************************************************************************************

    }

    private void Show_finished_days_style() {
        Calendar c = Calendar.getInstance();
        int CurrentDayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        int Number_of_finished_Days = 0;
        if(mCurrentWeekOrNot) {
            switch (CurrentDayOfWeek) {
                case Calendar.SATURDAY:
                    Number_of_finished_Days = 0;
                    break;
                case Calendar.SUNDAY:
                    Number_of_finished_Days = 1;
                    break;
                case Calendar.MONDAY:
                    Number_of_finished_Days = 2;
                    break;
                case Calendar.TUESDAY:
                    Number_of_finished_Days = 3;
                    break;
                case Calendar.WEDNESDAY:
                    Number_of_finished_Days = 4;
                    break;
                case Calendar.THURSDAY:
                    Number_of_finished_Days = 5;
                    break;
                case Calendar.FRIDAY:
                    Number_of_finished_Days = 6;
                    break;
            }
        }else{
            Number_of_finished_Days = 7;
        }

        for (int i = 0; i < Number_of_finished_Days; i++) {
            View tv = new View(getActivity().getApplicationContext());
            RelativeLayout.LayoutParams Params1 = new RelativeLayout.LayoutParams(mColumn_Width, RelativeLayout.LayoutParams.MATCH_PARENT);
            tv.setLayoutParams(Params1);
            tv.setBackgroundColor(getResources().getColor(R.color.finishedDay));
            tv.setX(i * mColumn_Width);
            RL.addView(tv);
        }
    }

    private void make_CurrentDay_Column_Colored(View rootView) {
        Calendar c = Calendar.getInstance();
        int CurrentDayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        View tv = new View(getActivity().getApplicationContext());
        RelativeLayout.LayoutParams Params1 = new RelativeLayout.LayoutParams(mColumn_Width, RelativeLayout.LayoutParams.MATCH_PARENT);
        tv.setLayoutParams(Params1);

        switch (CurrentDayOfWeek) {
            case Calendar.SATURDAY:
                TextView SatTextView = (TextView) rootView.findViewById(R.id.SatTextView);
                SatTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                tv.setX(0 * mColumn_Width);
                tv.setBackgroundColor(getResources().getColor(R.color.CalendarCurrentDay));
                break;
            case Calendar.SUNDAY:
                TextView SunTextView = (TextView) rootView.findViewById(R.id.SunTextView);
                SunTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                tv.setX(1 * mColumn_Width);
                tv.setBackgroundColor(getResources().getColor(R.color.CalendarCurrentDay));
                break;
            case Calendar.MONDAY:
                TextView MonTextView = (TextView) rootView.findViewById(R.id.MonTextView);
                MonTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                tv.setX(2 * mColumn_Width);
                tv.setBackgroundColor(getResources().getColor(R.color.CalendarCurrentDay));
                break;
            case Calendar.TUESDAY:
                TextView TueTextView = (TextView) rootView.findViewById(R.id.TueTextView);
                TueTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                tv.setX(3 * mColumn_Width);
                tv.setBackgroundColor(getResources().getColor(R.color.CalendarCurrentDay));
                break;
            case Calendar.WEDNESDAY:
                TextView WedTextView = (TextView) rootView.findViewById(R.id.WedTextView);
                WedTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                tv.setX(4 * mColumn_Width);
                tv.setBackgroundColor(getResources().getColor(R.color.CalendarCurrentDay));
                break;
            case Calendar.THURSDAY:
                TextView ThuTextView = (TextView) rootView.findViewById(R.id.ThuTextView);
                ThuTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                tv.setX(5 * mColumn_Width);
                tv.setBackgroundColor(getResources().getColor(R.color.CalendarCurrentDay));
                break;
            case Calendar.FRIDAY:
                TextView FriTextView = (TextView) rootView.findViewById(R.id.FriTextView);
                FriTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                tv.setX(6 * mColumn_Width);
                tv.setBackgroundColor(getResources().getColor(R.color.CalendarCurrentDay));
                break;
        }
        RL.addView(tv);
    }

    private float minutes_to_hours(int min) {
        //Converting the duration of event from minutes to hours
        int hours = min / 60; //since both are ints, you get an int
        int minutes = min % 60;
        float minuets = (float) hours + (float) minutes / (float) 60;
        return minuets;
    }

    private void CreateEvent(int event_duration_in_min, final String start_time, int day, final classData Class) {
        TextView tv = new TextView(RL.getContext());
        int height = (int) (minutes_to_hours(event_duration_in_min) * mRow_Height);

        GradientDrawable gradientDrawable = (GradientDrawable) getResources().getDrawable(R.drawable.rounded_corners_shape);
        gradientDrawable.setColor(getResources().getColor(Integer.parseInt(Class.getColor())));
        tv.setBackground(gradientDrawable);

//        tv.setBackgroundColor(getResources().getColor(Integer.parseInt(Class.getColor())));
//        tv.setBackgroundResource(R.drawable.ripple_effect_square);

        if( !Class.getInstructorName().equals("") )
            tv.setText(Class.getName() + ", " + Class.getInstructorName() + " : " + Class.getModule());
        else
            tv.setText(Class.getName() + " : " + Class.getModule());

        tv.setTextColor(Color.WHITE);
        tv.setPadding(5, 5, 5, 5);


        if (day != 0) {
            RelativeLayout.LayoutParams Params1 = new RelativeLayout.LayoutParams(mColumn_Width - 5, height);//const code line
            tv.setLayoutParams(Params1);
            tv.setX(day * mColumn_Width +5);
        } else {
            RelativeLayout.LayoutParams Params1 = new RelativeLayout.LayoutParams(mColumn_Width - 5, height);//const code line
            tv.setLayoutParams(Params1);
            tv.setX(day * mColumn_Width +5);
        }

        String StartTime_details[] = start_time.split(":");
        float S_H = (float) Integer.parseInt(StartTime_details[0]);
        float S_M = (float) Integer.parseInt(StartTime_details[1]);


        tv.setY((float) ((S_H + (S_M / 60)) * mRow_Height));

        tv.setId(Integer.parseInt(Class.getID()));
//        tv.setBackgroundResource(R.drawable.ripple_effect_circle);
        RL.addView(tv);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SendIntent = new Intent(getActivity(), ClassDetailsActivity.class);
                SendIntent.putExtra("ClassID", Class.getID());
                SendIntent.putExtra("SendFromCalendarFragment", true);
                getActivity().startActivityForResult(SendIntent, 1);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//                getActivity().overridePendingTransition(R.anim.fade_up,R.anim.fade_up2);


            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        /******************======= Choosing the Calendar Item from Navigation Menu=====*********/
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_calendar);//set menu selection to the first item
        /****************************************************************************************/
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

//
//        SharedPreferences sharedPreferences =getActivity().getSharedPreferences(String.valueOf(R.string.SharedPreferencesName), getActivity().MODE_PRIVATE);
//        sharedPreferences.edit().putInt("LastStartTimeForCalendar",mScrollView.getScrollY()).apply();
//        /*********************Saving Current Y-Scroll Position**************************/
//        try {
//            outState.putIntArray("VERTICLE_SCROLL_POSITION",
//                    new int[]{mScrollView.getScrollX(), mScrollView.getScrollY()});
//        } catch (Exception e) {
//
//        }
//        /*****************************************************************************/

    }
}
