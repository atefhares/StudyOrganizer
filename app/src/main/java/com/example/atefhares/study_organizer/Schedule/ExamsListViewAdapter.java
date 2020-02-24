package com.example.atefhares.study_organizer.Schedule;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.atefhares.study_organizer.DataClasses.examData;
import com.example.atefhares.study_organizer.DatabaseRelated.DBHelper;
import com.example.atefhares.study_organizer.NewOrEditActivities.ExamDetailsActivity;
import com.example.atefhares.study_organizer.R;
import com.example.atefhares.study_organizer.UtilitiesClasses.Utilities;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Atef Hares on 04-May-16.
 */
public class ExamsListViewAdapter extends BaseAdapter {
    private ExamsFragment mThis;
    private DBHelper mDbHelper;
    private ArrayList<examData> mExamsList = new ArrayList<examData>();


    public ExamsListViewAdapter(ExamsFragment context, ArrayList<examData> ExamsList) {
        mExamsList = ExamsList;
        mThis = context;
        mDbHelper = new DBHelper(mThis.getActivity());
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return mExamsList.size();
    }

    private class ViewHolder {

        TextView ColorTV, DataTV, DateTV;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity) mThis.getActivity()).getLayoutInflater();
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.exams_list_view_item, parent, false);
            holder = new ViewHolder();
            holder.ColorTV = (TextView) convertView.findViewById(R.id.ColorTV);
            holder.DataTV = (TextView) convertView.findViewById(R.id.DataTV);
            holder.DateTV = (TextView) convertView.findViewById(R.id.DateTV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // initialize your view here
        final examData exam = mExamsList.get(position);

        holder.ColorTV.setBackgroundColor(mThis.getResources().getColor(Integer.parseInt(mDbHelper.getClass(exam.getClassID()).getColor())));
        String StartTimeDetails[] = exam.getStartTime().split(":");

        String Exam_Date_details[]=exam.getDate().split("/");
        Calendar temp=Calendar.getInstance();
        temp.set(Calendar.DAY_OF_MONTH,Integer.parseInt(Exam_Date_details[0]));
        temp.set(Calendar.MONTH,Integer.parseInt(Exam_Date_details[1]));
        temp.set(Calendar.YEAR,Integer.parseInt(Exam_Date_details[2]));
        int day = temp.get(Calendar.DAY_OF_WEEK);
        String day_as_String = "";
        switch (day) {
            case Calendar.SATURDAY: day_as_String = "Sat"; break;
            case Calendar.SUNDAY:   day_as_String = "Sun";   break;
            case Calendar.MONDAY:   day_as_String = "Mon";   break;
            case Calendar.TUESDAY:  day_as_String = "Tues";  break;
            case Calendar.WEDNESDAY:day_as_String = "Wed";break;
            case Calendar.THURSDAY: day_as_String = "Thu"; break;
            case Calendar.FRIDAY:   day_as_String = "Fri";   break;
        }

        String Formatted_StartTimeDetails[] = Utilities.get_Time_Correct_Style(Integer.parseInt(StartTimeDetails[0]), Integer.parseInt(StartTimeDetails[1]));

        String ExamDate_details[] = Utilities.get_Date_Correct_Style(exam.getDate());
        String ExamDate = ExamDate_details[1] + " " + ExamDate_details[0] + ", " + ExamDate_details[2];
        holder.DateTV.setText(day_as_String+", "+ExamDate + " | at: " + Formatted_StartTimeDetails[0] + ":" + Formatted_StartTimeDetails[1] + " " + Formatted_StartTimeDetails[2] + " | For: " + exam.getDuration() + " Minutes");

        holder.DataTV.setText(exam.getModule());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SendIntent = new Intent(mThis.getActivity(), ExamDetailsActivity.class);
                SendIntent.putExtra("mCurrentExamID", exam.getID());
                mThis.startActivityForResult(SendIntent, 1);
                mThis.getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }});

        return convertView;
    }
}
