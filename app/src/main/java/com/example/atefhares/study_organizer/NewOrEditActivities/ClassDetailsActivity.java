package com.example.atefhares.study_organizer.NewOrEditActivities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.example.atefhares.study_organizer.DataClasses.classData;
import com.example.atefhares.study_organizer.DataClasses.examData;
import com.example.atefhares.study_organizer.DataClasses.termData;
import com.example.atefhares.study_organizer.DataClasses.timeData;
import com.example.atefhares.study_organizer.DataClasses.yearData;
import com.example.atefhares.study_organizer.DatabaseRelated.DBHelper;
import com.example.atefhares.study_organizer.R;
import com.example.atefhares.study_organizer.UtilitiesClasses.Utilities;

import java.util.ArrayList;
import java.util.Calendar;

public class ClassDetailsActivity extends AppCompatActivity {

    private String mClassID,mClassYearID,mClassTermID;

    private DBHelper mDBHelper;

    TextView mClassNameTV,mClassModuleTV,mYearNameTV,mTermNameTV,mInstructorTV,mClassName2TV,mClassPlaceTV, mYearDateTV , mTermDateTV;
    private LinearLayout mClassTimesLinearLayout,mClassExamsLinearLayout,mExamsMainVL;

    private boolean EditedOrNot,CameFromCalendarFragment;

    private Space LastSpace2;
    private View LastDivider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.ClassDetailsTB);
        setSupportActionBar(toolbar);


        mDBHelper = DBHelper.getInstance(this);

        mClassNameTV = (TextView) findViewById(R.id.class_name_TV);
        mClassName2TV = (TextView) findViewById(R.id.ClassNameTV);
        mClassModuleTV = (TextView) findViewById(R.id.ClassModuleTV);
        mYearNameTV = (TextView) findViewById(R.id.YearNameTV);
        mTermNameTV = (TextView) findViewById(R.id.TermNameTV);
        mInstructorTV = (TextView) findViewById(R.id.InstructorTV);
        mClassPlaceTV = (TextView) findViewById(R.id.ClassPlaceTV);
        mYearDateTV = (TextView) findViewById(R.id.YearDateTV);
        mTermDateTV = (TextView) findViewById(R.id.TermDateTV);
        LastSpace2 = (Space) findViewById(R.id.lastSpace2);
        LastDivider = findViewById(R.id.lastDivider1);
        mClassTimesLinearLayout = (LinearLayout) findViewById(R.id.ClassTimesVL);
        mClassExamsLinearLayout = (LinearLayout) findViewById(R.id.ExamsVL);
        mExamsMainVL = (LinearLayout) findViewById(R.id.ExamsMainVL);




        Intent intent = getIntent();
        if(intent.hasExtra("ClassID")) {
            mClassID = intent.getStringExtra("ClassID");
        }
        if(intent.hasExtra("SendFromCalendarFragment")){
            CameFromCalendarFragment = intent.getBooleanExtra("SendFromCalendarFragment",false);
        }

        Initialize_the_UI();


        /***********************************************************************************************************************/
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //setting back button color to white
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.back, null);
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, Color.WHITE);
            getSupportActionBar().setHomeAsUpIndicator(drawable);
        } catch (Exception e) {
        }
        /***********************************************************************************************************************/
    }

    private void Initialize_the_UI() {
        classData Class = mDBHelper.getClass(mClassID);

        Drawable d = new ColorDrawable();
        d.setColorFilter(getResources().getColor(Integer.parseInt(Class.getColor())), PorterDuff.Mode.OVERLAY);
        getSupportActionBar().setBackgroundDrawable(d);
        getSupportActionBar().setTitle("");
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(Integer.parseInt(Class.getColor())));

        mClassNameTV.setText("Class, " + Class.getName()+ " - Details:");
        mClassNameTV.setBackgroundColor(getResources().getColor(Integer.parseInt(Class.getColor())));

        mClassModuleTV.setText(Class.getModule());

        String TermName = mDBHelper.getTerm(Class.getTermID()).getTermName();

        mClassTermID =Class.getTermID();
        mClassYearID = mDBHelper.getYear(mDBHelper.getTerm(Class.getTermID()).getYearID()).getID();

        mTermNameTV.setText(TermName);
        String YearName = mDBHelper.getYear(mDBHelper.getTerm(Class.getTermID()).getYearID()).getYearName();
        mYearNameTV.setText(YearName);
        mInstructorTV.setText(Class.getInstructorName());
        mClassName2TV.setText(Class.getName());
        mClassPlaceTV.setText(Class.getPlace());

        termData termData = mDBHelper.getTerm(Class.getTermID());
        yearData yearData =  mDBHelper.getYear(termData.getYearID());

        String yearStartDate_details[] = Utilities.get_Date_Correct_Style(yearData.getStartDate());
        String yearEndDate_details[] = Utilities.get_Date_Correct_Style(yearData.getEndDate());
        mYearDateTV.setText(yearStartDate_details[0] + " " + yearStartDate_details[1] + ", " +yearStartDate_details[2]
                + " - " +yearEndDate_details[0] + " " + yearEndDate_details[1] + ", " +yearEndDate_details[2]);


        String[] termStartDate_details= Utilities.get_Date_Correct_Style(termData.getStartDate());
        String[] termEndDate_details= Utilities.get_Date_Correct_Style(termData.getEndDate());
        mTermDateTV.setText(termStartDate_details[0] + " " +termStartDate_details[1] +", " +termStartDate_details[2]
                + "   -   " + termEndDate_details[0] + " " +termEndDate_details[1] +", " +termEndDate_details[2]);


        ArrayList<timeData> ClassTimes = mDBHelper.getAllTimes_ForSomeClass(Class.getID());
        for (timeData data : ClassTimes) {
            TextView t = new TextView(this);
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
            mClassTimesLinearLayout.addView(t);
        }

        ArrayList<examData> Exams  = mDBHelper.getExams_for_SomeClass(mClassID);
        if(Exams!=null && !Exams.isEmpty()){
            LastDivider.setVisibility(View.VISIBLE);
            LastSpace2.setVisibility(View.VISIBLE);
            mExamsMainVL.setVisibility(View.VISIBLE);
            for (final examData exam : Exams) {
                TextView examData = new TextView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.gravity= Gravity.END;
                examData.setLayoutParams(params);

                TextView examDate = new TextView(this);
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params2.gravity= Gravity.START;
                examDate.setLayoutParams(params2);


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
                examDate.setText(day_as_String+", "+ExamDate + " | at: " + Formatted_StartTimeDetails[0] + ":" + Formatted_StartTimeDetails[1] + " " + Formatted_StartTimeDetails[2] + " | For: " + exam.getDuration() + " Minutes");


                examData.setText(exam.getModule());


                LinearLayout tempVL = new LinearLayout(this);
                tempVL.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                tempVL.setLayoutParams(params3);
                tempVL.addView(examData);
                tempVL.addView(examDate);

                tempVL.setBackgroundResource(R.drawable.ripple_effect_square);

                tempVL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                 Intent SendIntent = new Intent(getApplicationContext(), ExamDetailsActivity.class);
                        SendIntent.putExtra("mCurrentExamID", exam.getID());
                        startActivityForResult(SendIntent, 1);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    }});



                mClassExamsLinearLayout.addView(tempVL);

                View divider = new View(this);
                LinearLayout.LayoutParams divider_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
                divider.setLayoutParams(divider_params);
                divider.setBackgroundColor(getResources().getColor(R.color.LayoutDivider));
                mClassExamsLinearLayout.addView(divider);

            }
        }else{
            LastDivider.setVisibility(View.INVISIBLE);
            LastSpace2.setVisibility(View.INVISIBLE);
            mExamsMainVL.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.class_details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
//            this.finish();
        } else if (item.getItemId() == R.id.Edit) {
            if(CameFromCalendarFragment){
                final Intent intent = new Intent(this, AddOrEditClassActivity.class);
                new AlertDialog.Builder(this)
                        .setTitle("Confirm Editing")
                        .setMessage("Are you sure you want to Edit this Class?\nEditing will modify all occurrences")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                intent.putExtra("Edit",true);
                                intent.putExtra("YearID", mClassYearID);
                                intent.putExtra("TermID", mClassTermID);
                                intent.putExtra("ClassID", mClassID);
                                startActivityForResult(intent,1);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(R.drawable.alert)
                        .show();
            }else{
                Intent intent = new Intent(this, AddOrEditClassActivity.class);
                intent.putExtra("Edit", true);
                intent.putExtra("YearID", mClassYearID);
                intent.putExtra("TermID", mClassTermID);
                intent.putExtra("ClassID", mClassID);
                startActivityForResult(intent,1);
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            EditedOrNot = false;
        } else if (resultCode == Activity.RESULT_OK) {
            EditedOrNot =true;
            boolean deleted_or_not = data.getBooleanExtra("deleted_or_not",false);
            if(deleted_or_not){ // True == Deleted
                // TODO: 28-May-16 : back to previous activity or fragment
                onBackPressed();
            }else{
                UpdateTheUI();
            }
        }
    }

    private void UpdateTheUI() {
        mClassTimesLinearLayout.removeAllViews();
        mClassExamsLinearLayout.removeAllViews();
        Initialize_the_UI();
    }

    @Override
    public void onBackPressed() {
        Intent resIntent = new Intent();
        resIntent.putExtra("EditedOrNot",EditedOrNot);
        setResult(Activity.RESULT_OK,resIntent);
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }
}
