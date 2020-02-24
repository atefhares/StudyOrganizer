package com.example.atefhares.study_organizer.UtilitiesClasses;

import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.atefhares.study_organizer.DataClasses.termData;
import com.example.atefhares.study_organizer.DataClasses.timeData;
import com.example.atefhares.study_organizer.DataClasses.yearData;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Atef Hares on 30-Apr-16.
 */
public class Utilities {

    public static void Show_Dates_on_Two_TextViews(String S_Date, String E_Date, TextView StartDateTV, TextView EndDateTV) {
        Show_Date_on_one_TextView(S_Date, StartDateTV);
        Show_Date_on_one_TextView(E_Date, EndDateTV);
    }
    public static void Show_Date_on_one_TextView(String Date, TextView textView) {
        String Year_as_String = "";
        String Month_as_String = "";
        String day_as_String = "";

        String[] res = get_Date_Correct_Style(Date);
        day_as_String = res[0];
        Month_as_String = res[1];
        Year_as_String = res[2];

        textView.setText(day_as_String + ", " + Month_as_String + " " + Year_as_String);
    }
    public static String[] get_Date_Correct_Style(String Date) {
        String Year_as_String = "";
        String Month_as_String = "";
        String day_as_String = "";
        String[] Date_Details = Date.split("/");
        day_as_String = Date_Details[0];
        Month_as_String = Date_Details[1];
        Year_as_String = Date_Details[2];

        switch (Integer.parseInt(Month_as_String)) {
            case Calendar.JANUARY:
                Month_as_String = "Jan";
                break;
            case Calendar.FEBRUARY:
                Month_as_String = "Feb";
                break;
            case Calendar.MARCH:
                Month_as_String = "Mar";
                break;
            case Calendar.APRIL:
                Month_as_String = "Apr";
                break;
            case Calendar.MAY:
                Month_as_String = "May";
                break;
            case Calendar.JUNE:
                Month_as_String = "Jun";
                break;
            case Calendar.JULY:
                Month_as_String = "Jul";
                break;
            case Calendar.AUGUST:
                Month_as_String = "Aug";
                break;
            case Calendar.SEPTEMBER:
                Month_as_String = "Sep";
                break;
            case Calendar.OCTOBER:
                Month_as_String = "Oct";
                break;
            case Calendar.NOVEMBER:
                Month_as_String = "Nov";
                break;
            case Calendar.DECEMBER:
                Month_as_String = "Dec";
                break;
        }

        String[] Result = {day_as_String, Month_as_String, Year_as_String};
        return Result;
    }
    public static void Show_Error_MSG(String msg, final TextView mErrorTV) {
        final LinearLayout.LayoutParams[] layoutParams = {new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)};
        mErrorTV.setLayoutParams(layoutParams[0]);
        mErrorTV.setText(msg);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 5seconds
                layoutParams[0] = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
                mErrorTV.setLayoutParams(layoutParams[0]);
            }
        }, 6000);
    }
    public static String get_End_Date_Using_StartDate_and_DateDuration(String CurrentDate, String Date_Default_Duration) {
        Calendar calendar = Calendar.getInstance();
        String[] CurrentDate_Details = CurrentDate.split("/");
        int StartYear = Integer.parseInt(CurrentDate_Details[2]);
        int StartMonth = Integer.parseInt(CurrentDate_Details[1]);
        int StartDay = Integer.parseInt(CurrentDate_Details[0]);

        calendar.set(Calendar.YEAR, StartYear);
        calendar.set(Calendar.MONTH, StartMonth);
        calendar.set(Calendar.DAY_OF_MONTH, StartDay);

        calendar.add(Calendar.DATE, Integer.parseInt(Date_Default_Duration));
        int EndYear = calendar.get(Calendar.YEAR);
        int EndMonth = calendar.get(Calendar.MONTH);
        int EndDay = calendar.get(Calendar.DAY_OF_MONTH);
        String EndDate = EndDay + "/" + EndMonth + "/" + EndYear;
        return EndDate;
    }
    public static int Compare_Two_Calendar_Dates_Givin_As_String(String Date1, String Date2) {
        Calendar Cal1 = Calendar.getInstance(), Cal2 = Calendar.getInstance();
        String Date1_details[] = Date1.split("/");
        Cal1.set(Calendar.DAY_OF_MONTH, Integer.parseInt(Date1_details[0]));
        Cal1.set(Calendar.MONTH, Integer.parseInt(Date1_details[1]));
        Cal1.set(Calendar.YEAR, Integer.parseInt(Date1_details[2]));

        String Date2_details[] = Date2.split("/");
        Cal2.set(Calendar.DAY_OF_MONTH, Integer.parseInt(Date2_details[0]));
        Cal2.set(Calendar.MONTH, Integer.parseInt(Date2_details[1]));
        Cal2.set(Calendar.YEAR, Integer.parseInt(Date2_details[2]));

        Long Date1_time = Cal1.getTimeInMillis();
        Long Date2_time = Cal2.getTimeInMillis();

        if (Date1_time < Date2_time)
            return -1;
        else if (Date1_time == Date2_time)
            return 0;
        else {
            return 1;
        }
    }
    public static int Deference_Between_two_Calendar_dates_given_as_string(String Date1, String Date2) {
        Calendar Cal1 = Calendar.getInstance(), Cal2 = Calendar.getInstance();
        String Date1_details[] = Date1.split("/");
        Cal1.set(Calendar.DAY_OF_MONTH, Integer.parseInt(Date1_details[0]));
        Cal1.set(Calendar.MONTH, Integer.parseInt(Date1_details[1]));
        Cal1.set(Calendar.YEAR, Integer.parseInt(Date1_details[2]));

        String Date2_details[] = Date2.split("/");
        Cal2.set(Calendar.DAY_OF_MONTH, Integer.parseInt(Date2_details[0]));
        Cal2.set(Calendar.MONTH, Integer.parseInt(Date2_details[1]));
        Cal2.set(Calendar.YEAR, Integer.parseInt(Date2_details[2]));

        long difference = Cal1.getTimeInMillis() - Cal2.getTimeInMillis();
        return (int) (difference / (1000*60*60*24));
    }
    public static  int Calculate_Duration(String StartTime, String EndTime) {
        int duration=0;
        String StartTime_H_M [] = StartTime.split(":");
        String EndTime_H_M [] = EndTime.split(":");
        int StartTime_H = Integer.parseInt(StartTime_H_M[0]);
        int StartTime_M = Integer.parseInt(StartTime_H_M[1]);
        int EndTime_H = Integer.parseInt(EndTime_H_M[0]);
        int EndTime_M = Integer.parseInt(EndTime_H_M[1]);
        duration =(EndTime_H*60 + EndTime_M) - (StartTime_H*60 + StartTime_M);
        return duration;
    }
    public static void Show_Time_on_Two_TextViews(int Shour, int Smin, int Ehour, int Emin, TextView StartTimeTV, TextView EndTimeTV) {
        Show_Time_on_one_TextView(Shour,Smin,StartTimeTV);
        Show_Time_on_one_TextView(Ehour,Emin,EndTimeTV);
    }
    public static void Show_Time_on_one_TextView(int hour, int min, TextView textView) {
        String hour_as_String = "";
        String Min_as_String = "";
        String AM_or_PM = "";

        String[] res = get_Time_Correct_Style(hour, min);
        hour_as_String = res[0];
        Min_as_String = res[1];
        AM_or_PM = res[2];

        textView.setText(hour_as_String + ":" + Min_as_String + " " + AM_or_PM);
    }
    public static String[] get_Time_Correct_Style(int hour, int min) {
        String[] Result = {"", "", ""};
        String hour_as_String = "";
        String Min_as_String = "";
        String AM_or_PM = "";

        if (hour > 12) {
            hour -= 12;
            if (hour < 10)
                hour_as_String = "0" + hour;
            else
                hour_as_String = "" + hour;
            AM_or_PM = "PM";
        } else if (hour == 0) {
            hour_as_String = "12";
            AM_or_PM = "AM";
        } else if (hour < 12) {
            if (hour < 10)
                hour_as_String = "0" + hour;
            else
                hour_as_String = "" + hour;
            AM_or_PM = "AM";
        } else if (hour == 12) {
            hour_as_String = "" + hour;
            AM_or_PM = "PM";
        }


        if (min < 10)
            Min_as_String = "0" + min;
        else
            Min_as_String = "" + min;

        Result[0] = hour_as_String;
        Result[1] = Min_as_String;
        Result[2] = AM_or_PM;
        return Result;
    }
    public static int[] get_EndTime_Using_StartTime(int Shour, int SMin, int duration) {
        int EHour = get_hour_from_duration(duration) + Shour;//to set the default End hour using Duration
        int EMin;//to set the default End Minute using Duration

        if(get_mins_from_duration(duration) + SMin >= 60){//to set the default End Minute using Duration
            int h = (get_mins_from_duration(duration) + SMin ) / 60;
            int Remain_mins=0;
            for (int i=1 ; i<=h ; i++) {
                EHour+=1;
                Remain_mins = get_mins_from_duration(duration) + SMin-60;
            }
            EMin = Remain_mins;
        }else{
            EMin = get_mins_from_duration(duration) + SMin;
        }
        int [] x= {EHour,EMin};
        return x;
    }
    public static int get_hour_from_duration(int duration) {
        int hours = duration / 60; //since both are ints, you get an int
        return hours;
    }
    public static int get_mins_from_duration(int duration) {
        int minutes = duration % 60;
        return minutes;
    }
    public static int getCalendarDay_from_name(String day){
        switch (day) {
            case "Saturday":
                return 0;
            case "Sunday":
                return 1;
            case "Monday":
                return 2;
            case "Tuesday":
                return 3;
            case "Wednesday":
                return 4;
            case "Thursday":
                return 5;
            case "Friday":
                return 6;
        }
        return -1;
    }
    public static boolean time_is_correct(timeData TimeData, int duration) {
        String H_M_Data1_mStartTime []= TimeData.getStartTime().split(":");
        String H_M_data1_mEndTime []= TimeData.getEndTime().split(":");
        int Start_Hour_Data1 = Integer.parseInt(H_M_Data1_mStartTime[0]);
        int Start_Min_Data1 = Integer.parseInt(H_M_Data1_mStartTime[1]);
        int End_Hour_Data1 = Integer.parseInt(H_M_data1_mEndTime[0]);
        int End_Min_Data1 = Integer.parseInt(H_M_data1_mEndTime[1]);

        Date Start_Hour_TimeData_DATE = new Date();//Use This as Start
        Start_Hour_TimeData_DATE.setHours(Start_Hour_Data1);
        Start_Hour_TimeData_DATE.setMinutes(Start_Min_Data1);

        Date End_Hour_TimeData_DATE = new Date();//Use This as End
        End_Hour_TimeData_DATE.setHours(End_Hour_Data1);
        End_Hour_TimeData_DATE.setMinutes(End_Min_Data1);

        return  End_Hour_TimeData_DATE.getTime() - Start_Hour_TimeData_DATE.getTime() >= duration*60*1000 ;

    }

    public static boolean compare_two_classTimesData_if_Equal_or_Overlapped(timeData data1, timeData data2) {
        if (data1.getStartTime().equals(data2.getStartTime()) && data1.getEndTime().equals(data2.getEndTime())) {
            return true;//They are Equal
        } else if(check_if_two_TimeData_are_Overlapped(data1,data2)){
            return true;//They are Overlapped
        }else
            return false;//They are Different and Distinct
    }
    public static boolean isOverlapping(Date start1, Date end1, Date start2, Date end2) {
        return start1.getTime()< end2.getTime() && start2.getTime()<end1.getTime();
//        return start1.before(end2) && start2.before(end1);
    }
    public static boolean check_if_two_TimeData_are_Overlapped(timeData Data1, timeData data2) {
        String H_M_Data1_mStartTime []= Data1.getStartTime().split(":");
        String H_M_data1_mEndTime []= Data1.getEndTime().split(":");
        int Start_Hour_Data1 = Integer.parseInt(H_M_Data1_mStartTime[0]);
        int Start_Min_Data1 = Integer.parseInt(H_M_Data1_mStartTime[1]);
        int End_Hour_Data1 = Integer.parseInt(H_M_data1_mEndTime[0]);
        int End_Min_Data1 = Integer.parseInt(H_M_data1_mEndTime[1]);

        Date Start_Hour_data1_DATE = new Date();//Use This as Start 1
        Start_Hour_data1_DATE.setHours(Start_Hour_Data1);
        Start_Hour_data1_DATE.setMinutes(Start_Min_Data1);
        Date End_Hour_data1_DATE = new Date();//Use This as End 1
        End_Hour_data1_DATE.setHours(End_Hour_Data1);
        End_Hour_data1_DATE.setMinutes(End_Min_Data1);

        String H_M_data2_mStartTime []= data2.getStartTime().split(":");
        String H_M_data2_mEndTime []= data2.getEndTime().split(":");
        int Start_Hour_data2= Integer.parseInt(H_M_data2_mStartTime[0]);
        int Start_Min_data2 = Integer.parseInt(H_M_data2_mStartTime[1]);
        int End_Hour_data2 = Integer.parseInt(H_M_data2_mEndTime[0]);
        int End_Min_data2 = Integer.parseInt(H_M_data2_mEndTime[1]);

        Date H_M_data2_mStartTime_DATE = new Date();//Use This as Start 2
        H_M_data2_mStartTime_DATE.setHours(Start_Hour_data2);
        H_M_data2_mStartTime_DATE.setMinutes(Start_Min_data2);
        Date H_M_data2_mEndTime_DATE = new Date();//Use This as End 2
        H_M_data2_mEndTime_DATE.setHours(End_Hour_data2);
        H_M_data2_mEndTime_DATE.setMinutes(End_Min_data2);

        if( isOverlapping(Start_Hour_data1_DATE,End_Hour_data1_DATE, H_M_data2_mStartTime_DATE,H_M_data2_mEndTime_DATE))
        {
            return true;
        }

        return false;
    }
    public static String GetDayName(int day) {
        switch (day) {
            case Calendar.SATURDAY:
                return "Saturday";
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
        }
        return null;
    }
    public static boolean year_is_Current_year(yearData year) {
        Calendar c =Calendar.getInstance();
        Date Current_Date= c.getTime();

        String year_StartDate_Details[] = year.getStartDate().split("/");
        Calendar Startcalendar = Calendar.getInstance();
        Startcalendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(year_StartDate_Details[0]));
        Startcalendar.set(Calendar.MONTH,Integer.parseInt(year_StartDate_Details[1]));
        Startcalendar.set(Calendar.YEAR,Integer.parseInt(year_StartDate_Details[2]));
        Date year_StartDate = Startcalendar.getTime();

        String year_EndDate_Details[] = year.getEndDate().split("/");
        Calendar Endcalendar = Calendar.getInstance();
        Endcalendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(year_EndDate_Details[0]));
        Endcalendar.set(Calendar.MONTH,Integer.parseInt(year_EndDate_Details[1]));
        Endcalendar.set(Calendar.YEAR,Integer.parseInt(year_EndDate_Details[2]));
        Date year_EndDate = Endcalendar.getTime();

        return  Current_Date.after(year_StartDate) && Current_Date.before(year_EndDate);
    }
    public static boolean this_term_is_the_current_term(termData term) {
        Calendar c =Calendar.getInstance();
        Date Current_Date= c.getTime();

        String term_StartDate_Details[] = term.getStartDate().split("/");
        Calendar Startcalendar = Calendar.getInstance();
        Startcalendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(term_StartDate_Details[0]));
        Startcalendar.set(Calendar.MONTH,Integer.parseInt(term_StartDate_Details[1]));
        Startcalendar.set(Calendar.YEAR,Integer.parseInt(term_StartDate_Details[2]));
        Date term_StartDate = Startcalendar.getTime();

        String term_EndDate_Details[] = term.getEndDate().split("/");
        Calendar Endcalendar = Calendar.getInstance();
        Endcalendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(term_EndDate_Details[0]));
        Endcalendar.set(Calendar.MONTH,Integer.parseInt(term_EndDate_Details[1]));
        Endcalendar.set(Calendar.YEAR,Integer.parseInt(term_EndDate_Details[2]));
        Date term_EndDate = Endcalendar.getTime();

        return  Current_Date.after(term_StartDate) && Current_Date.before(term_EndDate);
    }
}
