package com.example.atefhares.study_organizer.NewOrEditActivities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.atefhares.study_organizer.DataClasses.classData;
import com.example.atefhares.study_organizer.DataClasses.examData;
import com.example.atefhares.study_organizer.DatabaseRelated.DBHelper;
import com.example.atefhares.study_organizer.R;
import com.example.atefhares.study_organizer.UtilitiesClasses.Utilities;

import java.util.Calendar;

public class ExamDetailsActivity extends AppCompatActivity {

    private String mExamID;

    private DBHelper mDBHelper;

    TextView mExamModuleTV,mClassTV,mExamPlaceTV, mExamDateTV , mExamDurationTV,mExamStartTimeTV;

    private boolean EditedOrNot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.ExamDetailsTB);
        setSupportActionBar(toolbar);


        mDBHelper = DBHelper.getInstance(this);

        mExamModuleTV = (TextView) findViewById(R.id.ExamModuleTV);
        mClassTV = (TextView) findViewById(R.id.ExamClassTV);
        mExamPlaceTV = (TextView) findViewById(R.id.ExamPlaceTV);
        mExamDateTV = (TextView) findViewById(R.id.ExamDateTV);
        mExamDurationTV = (TextView) findViewById(R.id.ExamDurationTV);
        mExamStartTimeTV = (TextView) findViewById(R.id.ExamStartTimeTV);

        Intent intent = getIntent();
        if(intent.hasExtra("mCurrentExamID")) {
            mExamID = intent.getStringExtra("mCurrentExamID");
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
        examData Exam = mDBHelper.getExam(mExamID);
        final classData Class = mDBHelper.getClass(Exam.getClassID());

        /*************************************************************************************************/
        Drawable d = new ColorDrawable();
        d.setColorFilter(getResources().getColor(Integer.parseInt(Class.getColor())), PorterDuff.Mode.OVERLAY);
        getSupportActionBar().setBackgroundDrawable(d);
        getSupportActionBar().setTitle("");
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(Integer.parseInt(Class.getColor())));

        /*************************************************************************************************/

        if( !Class.getInstructorName().equals("") )
            mClassTV.setText(Class.getName() + ", " + Class.getInstructorName() + " : " + Class.getModule());
        else
            mClassTV.setText(Class.getName() + " : " + Class.getModule());
        mClassTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SendIntent = new Intent(getApplicationContext(), ClassDetailsActivity.class);
                SendIntent.putExtra("ClassID", Class.getID());
                startActivityForResult(SendIntent, 1);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        /*************************************************************************************************/

        mExamModuleTV.setText(Exam.getModule());

        /*************************************************************************************************/

        String Exam_Date_details[]=Exam.getDate().split("/");
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
        String ExamDate_details[] = Utilities.get_Date_Correct_Style(Exam.getDate());
        String ExamDate = ExamDate_details[1] + " " + ExamDate_details[0] + ", " + ExamDate_details[2];
        mExamDateTV.setText(day_as_String+", "+ExamDate );
        /*************************************************************************************************/

        mExamDurationTV.setText(Exam.getDuration());
        mExamPlaceTV.setText(Exam.getPlace());

        /*************************************************************************************************/

        String StartTimeDetails[] = Exam.getStartTime().split(":");
        String Formatted_StartTimeDetails[] = Utilities.get_Time_Correct_Style(Integer.parseInt(StartTimeDetails[0]), Integer.parseInt(StartTimeDetails[1]));
        mExamStartTimeTV.setText(Formatted_StartTimeDetails[0] + ":" + Formatted_StartTimeDetails[1] + " " + Formatted_StartTimeDetails[2]);

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
                Intent intent = new Intent(this, AddOrEditExamActivity.class);
                intent.putExtra("Edit_or_AddNew", true);
                intent.putExtra("mCurrentExamID", mExamID);
                startActivityForResult(intent,1);
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
        Initialize_the_UI();
    }

    @Override
    public void onBackPressed() {
//        Intent resIntent = new Intent();
//        resIntent.putExtra("EditedOrNot",EditedOrNot);
//        setResult(Activity.RESULT_OK,resIntent);
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }
}
