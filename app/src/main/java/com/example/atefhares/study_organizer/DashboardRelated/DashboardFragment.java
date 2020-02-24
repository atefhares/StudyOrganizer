package com.example.atefhares.study_organizer.DashboardRelated;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.atefhares.study_organizer.Calendar.CalendarHoldFragment;
import com.example.atefhares.study_organizer.DataClasses.classData;
import com.example.atefhares.study_organizer.DataClasses.examData;
import com.example.atefhares.study_organizer.DataClasses.termData;
import com.example.atefhares.study_organizer.DataClasses.timeData;
import com.example.atefhares.study_organizer.DatabaseRelated.DBHelper;
import com.example.atefhares.study_organizer.MainActivityRelated.StartActivity;
import com.example.atefhares.study_organizer.NewOrEditActivities.ClassDetailsActivity;
import com.example.atefhares.study_organizer.NewOrEditActivities.ExamDetailsActivity;
import com.example.atefhares.study_organizer.R;
import com.example.atefhares.study_organizer.Schedule.ScheduleFragment;
import com.example.atefhares.study_organizer.UtilitiesClasses.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Math.abs;

/**
 * Created by Atef Hares on 20-Apr-16.
 */

public class DashboardFragment extends android.app.Fragment {

    private TextView TodayClassesNoTV, TomorrowClassesNoTV, TodayExamsNoTV, next7ExamsNoTV, NoClassesTV, NoExamsTV;
    private TextView TodayDateTV;

    private LinearLayout TodayClassesHL, TomorrowClassesHL, TodayExamsHL, Next7ExamsHL, RemainingClassesVL, RemainingExamsVL;
    DBHelper mdbHelper;

    ArrayList<timeData> TodayTimes;
    ArrayList<examData> examsToday;

    public DashboardFragment() {
        // Required empty public constructor

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mdbHelper = DBHelper.getInstance(getActivity());

        TodayClassesNoTV = (TextView) rootView.findViewById(R.id.TodayClassesNoTV);
        TomorrowClassesNoTV = (TextView) rootView.findViewById(R.id.TomorrowClassesNoTV);
        TodayExamsNoTV = (TextView) rootView.findViewById(R.id.TodayExams_no_TV);
        next7ExamsNoTV = (TextView) rootView.findViewById(R.id.next_7_Exams_No_TV);
        NoClassesTV = (TextView) rootView.findViewById(R.id.NoClassesTV);
        NoExamsTV = (TextView) rootView.findViewById(R.id.NoExamsTV);
        TodayDateTV = (TextView) rootView.findViewById(R.id.curDateTV);

        TodayClassesHL = (LinearLayout) rootView.findViewById(R.id.TodayClassesHL);
        TomorrowClassesHL = (LinearLayout) rootView.findViewById(R.id.TomorrowClassesHL);
        TodayExamsHL = (LinearLayout) rootView.findViewById(R.id.TodayExamsHL);
        Next7ExamsHL = (LinearLayout) rootView.findViewById(R.id.Next7ExamsHL);
        RemainingClassesVL = (LinearLayout) rootView.findViewById(R.id.ClassesVL);
        RemainingExamsVL = (LinearLayout) rootView.findViewById(R.id.ExamsVL);

        LinearLayout.OnClickListener ClassesOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarHoldFragment calendarHoldFragment = new CalendarHoldFragment();
                StartActivity.active = calendarHoldFragment; // the magic is here, everytime you add a new fragment, keep the reference to it to know what fragment is being shown
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                        .replace(R.id.RR, calendarHoldFragment, "nav_calendar")
//                        .addToBackStack(calendarHoldFragment.getTag())
                        .commit();
                StartActivity.mPreviousFragmentsNames.add("nav_calendar");

            }
        };
        LinearLayout.OnClickListener ExamsOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleFragment scheduleFragment = new ScheduleFragment();
                Bundle b = new Bundle();
                b.putBoolean("CameFromDashBorad", true);
                scheduleFragment.setArguments(b);
                StartActivity.active = scheduleFragment; // the magic is here, everytime you add a new fragment, keep the reference to it to know what fragment is being shown
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                        .replace(R.id.RR, scheduleFragment, "nav_Schedule")
//                        .addToBackStack(calendarHoldFragment.getTag())
                        .commit();
                StartActivity.mPreviousFragmentsNames.add("nav_Schedule");

            }
        };


        TodayClassesHL.setOnClickListener(ClassesOnClickListener);
        TomorrowClassesHL.setOnClickListener(ClassesOnClickListener);
        TodayExamsHL.setOnClickListener(ExamsOnClickListener);
        Next7ExamsHL.setOnClickListener(ExamsOnClickListener);
        //step 1
        Show_TodayDate(rootView);

        //step 2
        CreateTheDashboardData();


        /************===========Animating textviews from The cards============*************/
        if (savedInstanceState == null) {
            TextView t1 = (TextView) rootView.findViewById(R.id.t1);
            TextView t2 = (TextView) rootView.findViewById(R.id.t2);
            TextView t3 = (TextView) rootView.findViewById(R.id.t3);
            TextView t4 = (TextView) rootView.findViewById(R.id.t4);
            Animation fadeinAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fadin);
            fadeinAnimation.setDuration(800);
            t1.startAnimation(fadeinAnimation);
            t4.startAnimation(fadeinAnimation);

            t2.startAnimation(fadeinAnimation);
            t3.startAnimation(fadeinAnimation);
        }
        /***********************************************************************************/
        return rootView;
    }

    private void CreateTheDashboardData() {
        Calendar calendar = Calendar.getInstance();

        //step 1 : Show Today's Classes Number
        ShowSomeDayClassesNo(calendar, TodayClassesNoTV);

        //step 2 : Show Tomorrow's Classes Number
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        ShowTommorowClassesNo(calendar, TomorrowClassesNoTV);

        //step 3 : Show Today's Exams Number
        Calendar calendar2 = Calendar.getInstance();
        String Date = calendar2.get(Calendar.DAY_OF_MONTH) + "/" + calendar2.get(Calendar.MONTH) + "/" + calendar2.get(Calendar.YEAR);
        ;
        ShowSomeDayExamsNo(Date, TodayExamsNoTV);


        //step 4 : Show The next 7 days's Exams Number
        ShowNext7DaysExamsNo(next7ExamsNoTV);


        //step 5 : Show Today's Remaining Classes on UI
        if (!TodayTimes.isEmpty()) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 0);
            NoClassesTV.setLayoutParams(params);
            Show_Remaining_Classes_today();
        }

        //step 6 : Show Today's Remaining Exams on UI
        if (!examsToday.isEmpty()) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 0);
            NoExamsTV.setLayoutParams(params);
            Show_Remaining_Exams_today();
        }


    }

    private void Show_Remaining_Exams_today() {
        Calendar c = Calendar.getInstance();
        ArrayList<examData> RemainingExams = new ArrayList<>();

        for (examData exam : examsToday) {
            String S_time_details[] = exam.getStartTime().split(":");
            Calendar S_timeCal = Calendar.getInstance();
            S_timeCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(S_time_details[0]));
            S_timeCal.set(Calendar.MINUTE, Integer.parseInt(S_time_details[1]));

            if (c.getTimeInMillis() < S_timeCal.getTimeInMillis()) {
                RemainingExams.add(exam);
            }
        }
        if (!RemainingExams.isEmpty()) {
            Create_Exams_Views(RemainingExams);
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            NoExamsTV.setLayoutParams(params);
        }

    }

    private void Create_Exams_Views(ArrayList<examData> Exams) {

        for (final examData exam : Exams) {
            TextView Data = new TextView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.END;
            Data.setLayoutParams(params);
            Data.setText(exam.getModule());

            TextView RemianingTime_To_Start = new TextView(getActivity());
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params2.gravity = Gravity.START;
            RemianingTime_To_Start.setLayoutParams(params2);

            String S_time_details[] = exam.getStartTime().split(":");
            Calendar S_timeCal = Calendar.getInstance();
            S_timeCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(S_time_details[0]));
            S_timeCal.set(Calendar.MINUTE, Integer.parseInt(S_time_details[1]));


            Calendar c = Calendar.getInstance();
            Long time_in_Milli = abs(S_timeCal.getTimeInMillis() - c.getTimeInMillis());
            RemianingTime_To_Start.setText("Exam Starts In: " + Utilities.get_hour_from_duration((int) (time_in_Milli / 1000) / 60) + " Hours, " + Utilities.get_mins_from_duration((int) (time_in_Milli / 1000) / 60) + " Minutes");
            RemianingTime_To_Start.setPadding(10, 0, 0, 0);


            LinearLayout tempVL = new LinearLayout(getActivity());
            tempVL.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            tempVL.setLayoutParams(params3);
            tempVL.addView(Data);
            tempVL.addView(RemianingTime_To_Start);
            tempVL.setBackgroundResource(R.drawable.ripple_effect_square);

            tempVL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent SendIntent = new Intent(getActivity(), ExamDetailsActivity.class);
                    SendIntent.putExtra("mCurrentExamID", exam.getID());
                    startActivityForResult(SendIntent, 1);
                    getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
            });


            LinearLayout H_VL = new LinearLayout(getActivity());
            H_VL.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            H_VL.setLayoutParams(params4);


            TextView Color = new TextView(getActivity());
            LinearLayout.LayoutParams params5 = new LinearLayout.LayoutParams(30, LinearLayout.LayoutParams.MATCH_PARENT);
            Color.setLayoutParams(params5);

            classData Class = mdbHelper.getClass(exam.getClassID());
            Color.setBackgroundColor(getActivity().getResources().getColor(Integer.parseInt(Class.getColor())));

            H_VL.addView(Color);
            H_VL.addView(tempVL);

            RemainingExamsVL.addView(H_VL);


            View divider = new View(getActivity());
            LinearLayout.LayoutParams divider_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
            divider.setLayoutParams(divider_params);
            divider.setPadding(20, 0, 20, 0);
            divider.setBackgroundColor(getResources().getColor(R.color.LayoutDivider));
            RemainingExamsVL.addView(divider);

            View space = new View(getActivity());
            LinearLayout.LayoutParams space_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5);
            space.setLayoutParams(space_params);
            space.setPadding(20, 0, 20, 0);
            RemainingExamsVL.addView(space);
        }
    }

    private void ShowTommorowClassesNo(Calendar calendar, TextView tomorrowClassesNoTV) {
        String TermID = Which_Term_this_Day_belongs_to(calendar);
        ArrayList Times = mdbHelper.getAllTimes_ForTheCalendar(TermID, Utilities.GetDayName(calendar.get(Calendar.DAY_OF_WEEK)));
        tomorrowClassesNoTV.setText(Times.size() + "");
    }

    private void Show_Remaining_Classes_today() {
        Calendar c = Calendar.getInstance();
        ArrayList<timeData> RemainingTimes = new ArrayList<>();
        ArrayList<timeData> HappiningNowTimes = new ArrayList<>();

        for (timeData time : TodayTimes) {
            String S_time_details[] = time.getStartTime().split(":");
            Calendar S_timeCal = Calendar.getInstance();
            S_timeCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(S_time_details[0]));
            S_timeCal.set(Calendar.MINUTE, Integer.parseInt(S_time_details[1]));

            String E_time_details[] = time.getEndTime().split(":");
            Calendar E_timeCal = Calendar.getInstance();
            E_timeCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(E_time_details[0]));
            E_timeCal.set(Calendar.MINUTE, Integer.parseInt(E_time_details[1]));

            if (c.getTimeInMillis() < S_timeCal.getTimeInMillis()) {
                RemainingTimes.add(time);
            } else if (c.getTimeInMillis() >= S_timeCal.getTimeInMillis() && c.getTimeInMillis() < E_timeCal.getTimeInMillis()) {
                //happening now
                HappiningNowTimes.add(time);
            }
        }
        boolean check1 = false, check2 = false;
        if (!HappiningNowTimes.isEmpty()) {
            Create_Classes_Views(HappiningNowTimes, true);
        } else {
            check1 = true;
        }
        if (!RemainingTimes.isEmpty()) {
            Create_Classes_Views(RemainingTimes, false);
        } else {
            check2 = true;
        }

        if (check1 && check2) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            NoClassesTV.setLayoutParams(params);
        }

    }

    private void Create_Classes_Views(ArrayList<timeData> Times, boolean now_or_not) {

        for (timeData time : Times) {
            TextView Data = new TextView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.END;
            Data.setLayoutParams(params);

            TextView RemianingTime_To_finish = new TextView(getActivity());
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params2.gravity = Gravity.START;
            RemianingTime_To_finish.setLayoutParams(params2);

            final classData Class = mdbHelper.getClass(time.getClassID());
            if (!Class.getInstructorName().equals(""))
                Data.setText(Class.getName() + ", " + Class.getInstructorName() + " : " + Class.getModule());
            else
                Data.setText(Class.getName() + " : " + Class.getModule());

            if (now_or_not) {
                String S_time_details[] = time.getStartTime().split(":");
                Calendar S_timeCal = Calendar.getInstance();
                S_timeCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(S_time_details[0]));
                S_timeCal.set(Calendar.MINUTE, Integer.parseInt(S_time_details[1]));

                String E_time_details[] = time.getEndTime().split(":");
                Calendar E_timeCal = Calendar.getInstance();
                E_timeCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(E_time_details[0]));
                E_timeCal.set(Calendar.MINUTE, Integer.parseInt(E_time_details[1]));

                Calendar c = Calendar.getInstance();
                Long time_in_Milli = abs(E_timeCal.getTimeInMillis() - c.getTimeInMillis());
                RemianingTime_To_finish.setText("Class ends In: " + (time_in_Milli / 1000) / 60 + " Minutes");
                RemianingTime_To_finish.setPadding(10, 0, 0, 0);
            }else {
                String S_time_details[] = time.getStartTime().split(":");
                Calendar S_timeCal = Calendar.getInstance();
                S_timeCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(S_time_details[0]));
                S_timeCal.set(Calendar.MINUTE, Integer.parseInt(S_time_details[1]));

                Calendar c = Calendar.getInstance();
                Long time_in_Milli =  abs(c.getTimeInMillis() -  S_timeCal.getTimeInMillis());
                RemianingTime_To_finish.setText("Class Starts In: " + Utilities.get_hour_from_duration((int) (time_in_Milli / 1000) / 60) + " Hours, " + Utilities.get_mins_from_duration((int) (time_in_Milli / 1000) / 60) + " Minutes");
                RemianingTime_To_finish.setPadding(10, 0, 0, 0);
            }

            LinearLayout tempVL = new LinearLayout(getActivity());
            tempVL.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            tempVL.setLayoutParams(params3);
            tempVL.addView(Data);
            tempVL.addView(RemianingTime_To_finish);
            tempVL.setBackgroundResource(R.drawable.ripple_effect_square);

            tempVL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent SendIntent = new Intent(getActivity(), ClassDetailsActivity.class);
                    SendIntent.putExtra("ClassID", Class.getID());
                    SendIntent.putExtra("SendFromCalendarFragment", false);
                    getActivity().startActivityForResult(SendIntent, 1);
                    getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
            });


            LinearLayout H_VL = new LinearLayout(getActivity());
            H_VL.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            H_VL.setLayoutParams(params4);


            TextView Color = new TextView(getActivity());
            LinearLayout.LayoutParams params5 = new LinearLayout.LayoutParams(30, LinearLayout.LayoutParams.MATCH_PARENT);
            Color.setLayoutParams(params5);

            Color.setBackgroundColor(getActivity().getResources().getColor(Integer.parseInt(Class.getColor())));

            H_VL.addView(Color);
            H_VL.addView(tempVL);

            RemainingClassesVL.addView(H_VL);


            View divider = new View(getActivity());
            LinearLayout.LayoutParams divider_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
            divider.setLayoutParams(divider_params);
            divider.setPadding(20, 0, 20, 0);
            divider.setBackgroundColor(getResources().getColor(R.color.LayoutDivider));
            RemainingClassesVL.addView(divider);

            View space = new View(getActivity());
            LinearLayout.LayoutParams space_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5);
            space.setLayoutParams(space_params);
            space.setPadding(20, 0, 20, 0);
            RemainingClassesVL.addView(space);
        }
    }

    private void ShowNext7DaysExamsNo(TextView next7ExamsNoTV) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        ArrayList<examData> AllExams = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            String date = c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR);
            ArrayList<examData> examsToday = mdbHelper.getExams_byDate(date);
            AllExams.addAll(examsToday);
            c.add(Calendar.DAY_OF_MONTH, 1);
        }
        next7ExamsNoTV.setText(AllExams.size() + "");
    }

    private void ShowSomeDayExamsNo(String date, TextView TV) {
        examsToday = mdbHelper.getExams_byDate(date);
        TV.setText(examsToday.size() + "");
    }

    private void ShowSomeDayClassesNo(Calendar calendar, TextView TV) {
        String TermID = Which_Term_this_Day_belongs_to(calendar);
        TodayTimes = mdbHelper.getAllTimes_ForTheCalendar(TermID, Utilities.GetDayName(calendar.get(Calendar.DAY_OF_WEEK)));
//        for (timeData v : TodayTimes){
//            Log.w("before",v.getID());
//        }
//        Set<timeData> mySet = new TreeSet<>(new Comparator<timeData>() {
//            @Override
//            public int compare(timeData lhs, timeData rhs) {
//                if(lhs.getClassID().equals(rhs.getClassID()))
//                    return 0;
//                return -1;
//            }
//        });

//        mySet.addAll(TodayTimes);
//        for (timeData v : mySet){
//            Log.w("after",v.getID());
//        }
//        TV.setText(mySet.size()+"");
        TV.setText(TodayTimes.size() + "");
    }

    private String Which_Term_this_Day_belongs_to(Calendar dayDate) {
        ArrayList<termData> Terms = DBHelper.getInstance(getActivity()).getAllTerms();
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

    private void Show_TodayDate(View rootView) {
        /************===========Setting current date into a textview============*************/
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.SATURDAY);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMMM-yyyy");
        String formattedDate = df.format(c.getTime());
//        String[] days = new String[]{"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        int day = c.get(Calendar.DAY_OF_WEEK);
        String Today = "";
        switch (day) {
            case Calendar.SATURDAY:
                Today = "Saturday";
                break;
            case Calendar.SUNDAY:
                Today = "Sunday";
                break;
            case Calendar.MONDAY:
                Today = "Monday";
                break;
            case Calendar.TUESDAY:
                Today = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                Today = "Wednesday";
                break;
            case Calendar.THURSDAY:
                Today = "Thursday";
                break;
            case Calendar.FRIDAY:
                Today = "Friday";
                break;
        }
        TodayDateTV.setText("Today: " + Today + ", " + formattedDate);//Set the Text on textview
        /***********************************************************************************/
    }

    @Override
    public void onResume() {
        super.onResume();
        /*************************======= Changeing the Activity Title =====*********************/
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Dashboard");
        /****************************************************************************************/

        /******************======= Choosing the Calendar Item from Navgiation Menu=====*********/
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_dashboard);//set menu selection to the first item
        /****************************************************************************************/

    }
}
