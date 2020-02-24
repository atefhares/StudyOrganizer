package com.example.atefhares.study_organizer.NewOrEditActivities;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.atefhares.study_organizer.DataClasses.timeData;
import com.example.atefhares.study_organizer.DatabaseRelated.DBHelper;
import com.example.atefhares.study_organizer.R;
import com.example.atefhares.study_organizer.UtilitiesClasses.Utilities;

import java.util.ArrayList;

public class AddOrEditTimeActivity extends AppCompatActivity {
    DBHelper mdbHelper;

    private timeData mSelectedTimeData = new timeData();
    private timeData mTime;// only used in Edit

    private int mSHour, mSMin, mEHour, mEMin, mDefaultDuration;

    private String mSelectedTermID;

    private boolean mUseDefault = true;
    private boolean mAddNewMode_Or_EditMode;

    private ArrayList<timeData> mPreviousChosenTimesDataList = new ArrayList<timeData>();
    private ArrayList<timeData> mDeletedTimesDataList = new ArrayList<timeData>(); //for edit mode

    private TextView mErrorTV,startTimeTV,endTimeTV;
    private Spinner Days_Spinner;

    private ArrayAdapter<String> Days_Spinner_Adapter;

    final ArrayList<String> Days = new ArrayList<String>(){{add("Saturday"); add("Sunday"); add("Monday"); add("Tuesday"); add("Wednesday"); add("Thursday"); add("Friday");}};
//  final String[] Days = {"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_time);
        mdbHelper = DBHelper.getInstance(this);

        startTimeTV = (TextView) findViewById(R.id.startTimeTV);
        endTimeTV = (TextView) findViewById(R.id.endTimeTV);
        mErrorTV = (TextView) findViewById(R.id.TimeErrorTV);
        Days_Spinner = (Spinner) findViewById(R.id.spinner_new_time);
        Days_Spinner_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Days);

        /*****************************************************************************************************************************************************/
        //Getting the previous chosen times for this class_logo to use in "Check_Current_chosen_Time_if_Selected_Before"
        Intent intent = getIntent();
        mAddNewMode_Or_EditMode = intent.getBooleanExtra("Edit_or_AddNew", false);
        if (mAddNewMode_Or_EditMode && intent.hasExtra("Time")) {
            /* mAddNewMode_Or_EditMode = True = Edit Mode */
            mTime = intent.getParcelableExtra("Time");
            if (mTime != null)
                Initialize_UI_For_Edit_Mode(mTime);
        } else {
            /* mAddNewMode_Or_EditMode = False = Add New Mode */
            Initialize_UI_For_Add_New_Mode();
        }

        //Getting the previous chosen times and the deleted_by_user times  for this Class to use in "Check_Current_chosen_Time_if_Selected_Before"
        if (getIntent().hasExtra("mTimesDataList_bundle")) {
            Bundle bundle = getIntent().getBundleExtra("mTimesDataList_bundle");
            if (bundle != null) {
                mPreviousChosenTimesDataList = bundle.getParcelableArrayList("mPreviousChosenTimesDataList");
                mDeletedTimesDataList = bundle.getParcelableArrayList("mDeletedTimesDataList");
            }
            mSelectedTermID = getIntent().getStringExtra("mSelectedTermID");
        }



        /*****************************************************************************************************************************************************/
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //setting back button color to white
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.close, null);
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, Color.WHITE);
            getSupportActionBar().setHomeAsUpIndicator(drawable);
        } catch (Exception e) {}
        /*****************************************************************************************************************************************************/

        Set_Pickers();
    }

    private void Set_Pickers() {
        if (Days_Spinner != null) {
            Days_Spinner.setAdapter(Days_Spinner_Adapter);
            if(mAddNewMode_Or_EditMode){ // True == EditMode
                Days_Spinner.setSelection( Days.indexOf(mSelectedTimeData.getDay()));
            }
            Days_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mSelectedTimeData.setDay(Days.get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
        /*****************************************************************************************************************************************************/
        if (startTimeTV != null) {
            startTimeTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(AddOrEditTimeActivity.this, R.style.Dialog, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            //step 1
                            mSHour = hourOfDay;
                            mSMin = minute;

                            //step 2 : has a condition
                            if (mUseDefault) {
                                int [] EndTime_H_M = Utilities.get_EndTime_Using_StartTime(mSHour,mSMin,mDefaultDuration);
                                mEHour = EndTime_H_M[0]; mEMin = EndTime_H_M[1];
                                Utilities.Show_Time_on_Two_TextViews(mSHour, mSMin, mEHour, mEMin, startTimeTV, endTimeTV);
                            } else {
                                Utilities.Show_Time_on_one_TextView(mSHour, mSMin, startTimeTV);
                            }
                        }
                    }, mSHour, mSMin, false);
                    timePickerDialog.show();
                }
            });
        }
                                  /*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
        if (endTimeTV != null) {
            endTimeTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(AddOrEditTimeActivity.this, R.style.Dialog, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                            if(hourOfDay>mSHour) {
                                //step 1
                                mEHour = hourOfDay;
                                mEMin = minute;

                                //step 2
                                Utilities.Show_Time_on_one_TextView(mEHour, mEMin, endTimeTV);

                                //step 3 : don't use default duration for startTime next Change events because the user has changed the EndTime Manually
                                mUseDefault = false;

                            }else if(hourOfDay == mSHour){
                                if(minute>mSMin && minute-mSMin >=30){
                                    //step 1
                                    mEHour = hourOfDay;
                                    mEMin = minute;

                                    //step 2
                                    Utilities.Show_Time_on_one_TextView(mEHour, mEMin, endTimeTV);

                                    //step 3 : don't use default duration for startTime next Change events because the user has changed the EndTime Manually
                                    mUseDefault = false;
                                }else if(minute-mSMin < 30 && minute-mSMin >=0 ){
                                    Utilities.Show_Error_MSG("Error: Class must be at least of 30 minutes duration.",mErrorTV);
                                }else{
                                    Utilities.Show_Error_MSG("Error: End Time cannot be before Start Time",mErrorTV);
                                }
                            }else{
                                Utilities.Show_Error_MSG("Error: End Time cannot be before Start Time",mErrorTV);
                            }

                        }
                    }, mEHour, mEMin, false);
                    timePickerDialog.show();
                }
            });
        }
        /*****************************************************************************************************************************************************/
    }

    private void Initialize_UI_For_Add_New_Mode() {
        //Step 1 : Set title
        setTitle("New Time");

        mSelectedTimeData.setDay(Days.get(0));
        /*****************************************************************************************************************************************************/

        //Initializing mSelectedTimeData with Default Start/End Times
        SharedPreferences sharedPreferences= getSharedPreferences(String.valueOf(R.string.SharedPreferencesName),MODE_PRIVATE);
        mDefaultDuration = Integer.parseInt(sharedPreferences.getString("DefaultDuration", getResources().getString(R.string.DefaultDuration)));//to set the default Duration
        mSHour = Integer.parseInt(sharedPreferences.getString("DefaultStartTime_hour",getResources().getString(R.string.DefaultStartTime_hour)));//to set the default start hour
        mSMin = Integer.parseInt(sharedPreferences.getString("DefaultStartTime_min",getResources().getString(R.string.DefaultStartTime_min)));//to set the default start Minute
        int [] EndTime_H_M = Utilities.get_EndTime_Using_StartTime(mSHour,mSMin,mDefaultDuration);
        mEHour = EndTime_H_M[0]; mEMin = EndTime_H_M[1];
        Utilities.Show_Time_on_Two_TextViews(mSHour, mSMin,mEHour,mEMin, startTimeTV, endTimeTV);//Show Default Start/EndTime on Activity start
        /*****************************************************************************************************************************************************/
    }

    private void Initialize_UI_For_Edit_Mode(timeData mTime) {
        //Step 1 : Set title
        setTitle("Edit Time");

        SharedPreferences sharedPreferences= getSharedPreferences(String.valueOf(R.string.SharedPreferencesName),MODE_PRIVATE);
        mDefaultDuration = Integer.parseInt(sharedPreferences.getString("DefaultDuration", getResources().getString(R.string.DefaultDuration)));//to set the default Duration

        mSelectedTimeData.setDay(mTime.getDay());
        //Spinner selection is made in side setPikers()
        //Initializing mSelectedTimeData with mTime Start/End Times
        String mTime_StartTime_details[] = mTime.getStartTime().split(":");
        mSHour = Integer.parseInt(mTime_StartTime_details[0]);
        mSMin = Integer.parseInt(mTime_StartTime_details[1]);

        String mTime_EndTime_details[] = mTime.getEndTime().split(":");
        mEHour = Integer.parseInt(mTime_EndTime_details[0]);
        mEMin = Integer.parseInt(mTime_EndTime_details[1]);


        Utilities.Show_Time_on_Two_TextViews(mSHour, mSMin,mEHour,mEMin, startTimeTV, endTimeTV);//Show Default Start/EndTime on Activity start
    }

    private boolean Check_Current_chosen_Time_if_Not_Available_Or_Overlapping_Existing_Time() {
        ArrayList<timeData> TimesDataList = mdbHelper.getAllTimes_ForSomeTerm(mSelectedTermID);
        //step 1: removing the current being edited time from comparing
        if (mAddNewMode_Or_EditMode && mTime!=null) { /* True == Edit Mode ,  False == AddNew Mode*/
            for (timeData data : TimesDataList) {
                if (data.getID().equals(mTime.getID())) {
                    TimesDataList.remove(data);
                    break;
                }
            }
        }

        // step 2 : removing the deleted_by_user terms from comparing
        for(timeData deleted_data : mDeletedTimesDataList) {
            for (timeData data : TimesDataList) {
                if (data.getID().equals(deleted_data.getID())) {
                    TimesDataList.remove(data);
                    break;
                }
            }
        }

        if(TimesDataList!=null) {
            for (timeData Data : TimesDataList) {
                if(Data!=null)
                    if(Data.getDay().equals(mSelectedTimeData.getDay())) {
                        String Data_startTime_details[]=Data.getStartTime().split(":");
                        int [] res  = Utilities.get_EndTime_Using_StartTime(Integer.parseInt(Data_startTime_details[0]),Integer.parseInt(Data_startTime_details[1]),Integer.parseInt(Data.getDuration()));
                        String Data_EndTime = res[0]+":"+res[1];
                        Data.setEndTime(Data_EndTime);
                        if (Utilities.compare_two_classTimesData_if_Equal_or_Overlapped(mSelectedTimeData, Data))
                            return true;//Current time is Already chosen before
                    }
            }
        }


        return false;
    }

    private boolean Check_Current_choose_Time_if_Equal_oR_Overlapping_an_Already_Selected_Time_Before() {
        if (mPreviousChosenTimesDataList == null) {
            return false;//Current time is not chosen before
        } else {
            for (timeData data : mPreviousChosenTimesDataList) {
                if(data!=null)
                    if(data.getDay().equals(mSelectedTimeData.getDay()))
                        if (Utilities.compare_two_classTimesData_if_Equal_or_Overlapped(mSelectedTimeData, data))
                            return true;//Current time is Already chosen before
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_new_time_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Intent return_intent = new Intent();
            setResult(Activity.RESULT_CANCELED, return_intent);
            this.finish();
        } else if (item.getItemId() == R.id.Save) {
            mSelectedTimeData.setStartTime(mSHour + ":" + mSMin);
            mSelectedTimeData.setEndTime(mEHour + ":" + mEMin);
            // mSelectedTimeData.mDay hs been set on the spinner event
            if (mAddNewMode_Or_EditMode && mTime!=null) {
                /*True == Edit Mode , False == AddNew Mode*/
                mSelectedTimeData.setID(mTime.getID()); //the same Edited Term ID
            } else {
                mSelectedTimeData.setID(mSelectedTimeData.getStartTime() + mSelectedTimeData.getEndTime()); //Some Useless ID
            }

            if (!Check_Current_chosen_Time_if_Not_Available_Or_Overlapping_Existing_Time()) {
                if (!Check_Current_choose_Time_if_Equal_oR_Overlapping_an_Already_Selected_Time_Before()) {
                    if(Utilities.time_is_correct(mSelectedTimeData , 30)) {
                        SendResult(mSelectedTimeData);
                    }else{
                        Utilities.Show_Error_MSG("Error: Selected time is not correct OR Class duration is less than 30 min.",mErrorTV);
                    }
                } else {
                    Utilities.Show_Error_MSG("Error: Selected time is Overlapping an Already chosen time period, Time periods Cannot Overlap.",mErrorTV);
                }
            }else {
                Utilities.Show_Error_MSG("Error: Selected time is Overlapping another class time period, Time periods Cannot Overlap.",mErrorTV);
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void SendResult(timeData mTimeData) {
        Intent return_intent = new Intent();
        //String[] res = {SelectedTermStartDate, SelectedTermEndDate, Data.getTermName(), Data.getID()};
        return_intent.putExtra("ResultedTime", mTimeData);
        return_intent.putExtra("Edit_or_AddNew", mAddNewMode_Or_EditMode);
        setResult(Activity.RESULT_OK, return_intent);
        this.finish();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("mSelectedTimeData",mSelectedTimeData);
        outState.putParcelable("mTime",mTime);// only used in Edit

        outState.putInt("mSHour",mSHour);
        outState.putInt("mSMin",mSMin);
        outState.putInt("mEHour",mEHour);
        outState.putInt("mEMin",mEMin);
        outState.putInt("mDefaultDuration",mDefaultDuration);

        outState.putBoolean("mUseDefault",mUseDefault);
        outState.putBoolean("mAddNewMode_Or_EditMode",mAddNewMode_Or_EditMode);


        outState.putParcelableArrayList("mPreviousChosenTimesDataList",mPreviousChosenTimesDataList);
        outState.putParcelableArrayList("mDeletedTimesDataList",mDeletedTimesDataList);

        outState.putString("mSelectedTermID",mSelectedTermID);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mSelectedTimeData = savedInstanceState.getParcelable("mSelectedTimeData");
        mTime = savedInstanceState.getParcelable("mTime");// only used in Edit

        mSHour = savedInstanceState.getInt("mSHour");
        mSMin = savedInstanceState.getInt("mSMin");
        mEHour = savedInstanceState.getInt("mEHour");
        mEMin = savedInstanceState.getInt("mEMin");
        mDefaultDuration = savedInstanceState.getInt("mDefaultDuration");

        mUseDefault = savedInstanceState.getBoolean("mUseDefault");
        mAddNewMode_Or_EditMode = savedInstanceState.getBoolean("mAddNewMode_Or_EditMode");


        mPreviousChosenTimesDataList = savedInstanceState.getParcelableArrayList("mPreviousChosenTimesDataList");
        mDeletedTimesDataList =savedInstanceState.getParcelableArrayList("mDeletedTimesDataList");

        mSelectedTermID = savedInstanceState.getString("mSelectedTermID");
    }
}

