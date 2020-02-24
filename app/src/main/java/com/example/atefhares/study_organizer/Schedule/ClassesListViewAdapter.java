package com.example.atefhares.study_organizer.Schedule;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.atefhares.study_organizer.DataClasses.classData;
import com.example.atefhares.study_organizer.DataClasses.timeData;
import com.example.atefhares.study_organizer.DatabaseRelated.DBHelper;
import com.example.atefhares.study_organizer.NewOrEditActivities.ClassDetailsActivity;
import com.example.atefhares.study_organizer.R;
import com.example.atefhares.study_organizer.UtilitiesClasses.Utilities;

import java.util.ArrayList;

/**
 * Created by Atef Hares on 04-May-16.
 */
public class ClassesListViewAdapter extends BaseAdapter {
    private ClassesFragment mThis;
    private DBHelper mDbHelper;
    private ArrayList<classData> mClassesList = new ArrayList<classData>();


    public ClassesListViewAdapter(ClassesFragment context, ArrayList<classData> ClassesList) {
        mClassesList = ClassesList;
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
        return mClassesList.size();
    }

    private class ViewHolder {

        TextView ColorTV, DataTV;
        LinearLayout VL;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity) mThis.getActivity()).getLayoutInflater();
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.classes_list_view_item, parent, false);
            holder = new ViewHolder();
            holder.ColorTV = (TextView) convertView.findViewById(R.id.ColorTV);
            holder.DataTV = (TextView) convertView.findViewById(R.id.DataTV);
            holder.VL = (LinearLayout) convertView.findViewById(R.id.classes_list_view_item_VL);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.VL.removeAllViews();
        }

        // initialize your view here
            final classData Class = mClassesList.get(position);

            if( !Class.getInstructorName().equals("") )
                holder.DataTV.setText(Class.getName() + ", " + Class.getInstructorName() + " : " + Class.getModule());
            else
                holder.DataTV.setText(Class.getName()+" : " + Class.getModule());

            holder.ColorTV.setBackgroundColor(mThis.getResources().getColor(Integer.parseInt(Class.getColor())));
            ArrayList<timeData> ClassTimes = mDbHelper.getAllTimes_ForSomeClass(Class.getID());

            if (ClassTimes != null && !ClassTimes.isEmpty()) {
                for (timeData data : ClassTimes) {
                    TextView t = new TextView(mThis.getActivity());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    t.setLayoutParams(params);
                    String StartTimeDetails[] = data.getStartTime().split(":");
                    String Formatted_StartTimeDetails[] = Utilities.get_Time_Correct_Style(Integer.parseInt(StartTimeDetails[0]), Integer.parseInt(StartTimeDetails[1]));
                    int[] EndTimeDetails = Utilities.get_EndTime_Using_StartTime(Integer.parseInt(StartTimeDetails[0]), Integer.parseInt(StartTimeDetails[1]), Integer.parseInt(data.getDuration()));
                    String Formatted_EndTimeDetails[] = Utilities.get_Time_Correct_Style(EndTimeDetails[0], EndTimeDetails[1]);

                    if (Formatted_StartTimeDetails[2].equals(Formatted_EndTimeDetails[2])) {
                        t.setText(data.getDay() + ", " + Formatted_StartTimeDetails[0] + ":" + Formatted_StartTimeDetails[1] + " - "
                                + Formatted_EndTimeDetails[0] + ":" + Formatted_EndTimeDetails[1] + " " + Formatted_EndTimeDetails[2]);
                    } else {
                        t.setText(data.getDay() + ", " + Formatted_StartTimeDetails[0] + ":" + Formatted_StartTimeDetails[1] + " " + Formatted_StartTimeDetails[2]
                                + " - " + Formatted_EndTimeDetails[0] + ":" + Formatted_EndTimeDetails[1] + " " + Formatted_EndTimeDetails[2]);
                    }
                    holder.VL.addView(t);
                }

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent SendIntent = new Intent(mThis.getActivity(), ClassDetailsActivity.class);
                        SendIntent.putExtra("ClassID", Class.getID());
                        mThis.startActivityForResult(SendIntent, 1);
                        mThis.getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    }
                });
            }


        return convertView;
    }


}
