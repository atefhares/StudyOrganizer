package com.example.atefhares.study_organizer.NewOrEditActivities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.atefhares.study_organizer.DataClasses.classData;
import com.example.atefhares.study_organizer.DataClasses.examData;
import com.example.atefhares.study_organizer.DataClasses.termData;
import com.example.atefhares.study_organizer.DataClasses.yearData;
import com.example.atefhares.study_organizer.DatabaseRelated.DBHelper;
import com.example.atefhares.study_organizer.R;
import com.example.atefhares.study_organizer.UtilitiesClasses.Utilities;

import java.util.ArrayList;
import java.util.Calendar;

public class AddOrEditExamActivity extends AppCompatActivity {
    private DBHelper mdbHelper;
    private AddOrEditExamActivity mThisActivity;

    private boolean mAddNewMode_Or_EditMode; /* True=Edit Mode ,  False=Add New Mode */

    private EditText mExamModuleTV, mExamPlaceTV;

    private Spinner Choose_Year_Spinner, Choose_Term_Spinner, Choose_Class_Spinner, Choose_Duration_Spinner;

    private Calendar mExamDateCal = Calendar.getInstance();
    private Calendar mSelectedTermStartCal = Calendar.getInstance();
    private Calendar mSelectedTermEndCal = Calendar.getInstance();
    private TextView mErrorTV, mDeleteTV, mExamDateTV, mExamStartTimeTV, mClassColorTV;


    private int mExamSHour, mExamSMin;
    private String mCurrentExamID;//used in Edit Mode
    private String mSelectedClassID, mSelectedDuration, mSelectedYearID, mSelectedTermID;

    ArrayList<yearData> Years;
    ArrayList<termData> Terms;
    ArrayList<String> TermsNames;
    ArrayAdapter<String> Choose_Term_Spinner_adapter;
    ArrayAdapter<String> Choose_Class_Spinner_adapter;
    ArrayList<String> ClassesNames;
    ArrayList<classData> mClasses;
    ArrayList<String> YearsNames;

    private examData mExam = new examData();//for editing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_exam);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mdbHelper = DBHelper.getInstance(this);

        Choose_Year_Spinner = (Spinner) findViewById(R.id.YearSpinner);
        Choose_Term_Spinner = (Spinner) findViewById(R.id.TermSpinner);
        Choose_Class_Spinner = (Spinner) findViewById(R.id.ClassSpinner);
        Choose_Duration_Spinner = (Spinner) findViewById(R.id.DurationSpinner);
        mExamModuleTV = (EditText) findViewById(R.id.ExamModuleTV);
        mExamPlaceTV = (EditText) findViewById(R.id.ExamPlace);
        mExamDateTV = (TextView) findViewById(R.id.ExamDateTV);
        mExamStartTimeTV = (TextView) findViewById(R.id.EXstartTimeTV);
        mClassColorTV = (TextView) findViewById(R.id.SelectedClassColorTV);
        mErrorTV = (TextView) findViewById(R.id.ExamErrorTV);
        mDeleteTV = (TextView) findViewById(R.id.DeleteTV);
        mDeleteTV.setBackgroundResource(R.drawable.ripple_effect_square);


        //New Mode or Edit Mode
        Intent intent = getIntent();
        mAddNewMode_Or_EditMode = intent.getBooleanExtra("Edit_or_AddNew", false);
        if (mAddNewMode_Or_EditMode && intent.hasExtra("mCurrentExamID")) {
            /* mAddNewMode_Or_EditMode = True = Edit Mode */
            mCurrentExamID = intent.getStringExtra("mCurrentExamID");
            if (mCurrentExamID != null)
                Initialize_UI_For_Edit_Mode(mCurrentExamID);
        } else {
            /* mAddNewMode_Or_EditMode = False = Add New Mode */
            Initialize_UI_For_Add_New_Mode();
        }
        /*****************************************************************************************************************************************************/
        mThisActivity = this;
        /***********************************************************************************************************************/
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //setting back button color to white
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.close, null);
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, Color.WHITE);
            getSupportActionBar().setHomeAsUpIndicator(drawable);
        } catch (Exception e) {
        }
        /***********************************************************************************************************************/

    }

    private void Initialize_UI_For_Add_New_Mode() {

        setTitle("Add New Exam");

        /*+=+=+=+==+=+=+=+==+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+==+=+=+=+=*/
        Years = mdbHelper.getAllYears();
        mSelectedYearID = Years.get(0).getID();
        YearsNames = get_Years_For_Spinner(Years);
        final ArrayAdapter<String> Choose_Year_Spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, YearsNames);
        Choose_Year_Spinner.setAdapter(Choose_Year_Spinner_adapter);
        Choose_Year_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedYearID = Years.get(position).getID();
                Terms = mdbHelper.getAllTerms(mdbHelper.getYear(mSelectedYearID));
                TermsNames = get_Terms_For_Spinner(Terms);
                Choose_Term_Spinner_adapter = new ArrayAdapter<String>(mThisActivity, android.R.layout.simple_spinner_dropdown_item, TermsNames);
                Choose_Term_Spinner.setAdapter(Choose_Term_Spinner_adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*+=+=+=+==+=+=+=+==+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+==+=+=+=+=*/
        Terms = mdbHelper.getAllTerms(mdbHelper.getYear(mSelectedYearID));
        mSelectedTermID=Terms.get(0).getID();
        TermsNames = get_Terms_For_Spinner(Terms);
        String data_StartDate_details[] = Terms.get(0).getStartDate().split("/");
        mSelectedTermStartCal.set(Calendar.YEAR, Integer.parseInt(data_StartDate_details[2]));
        mSelectedTermStartCal.set(Calendar.MONTH, Integer.parseInt(data_StartDate_details[1]));
        mSelectedTermStartCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(data_StartDate_details[0]));
        Choose_Term_Spinner_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, TermsNames);
        Choose_Term_Spinner.setAdapter(Choose_Term_Spinner_adapter);
        Choose_Term_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //step 1
                mSelectedTermID = Terms.get(position).getID();

                //step 2
                mClasses = mdbHelper.getAllClasses(mSelectedTermID);
                if (!mClasses.isEmpty()) {
                    mSelectedClassID = mClasses.get(0).getID();
                    mClassColorTV.setBackgroundColor(getResources().getColor(Integer.parseInt(mClasses.get(0).getColor())));
                    ClassesNames = get_Classes_For_Spinner(mClasses);
                } else {
                    mSelectedClassID = "null";
                    mClassColorTV.setBackgroundColor(getResources().getColor(android.R.color.white));
                    ClassesNames = new ArrayList<String>() {{
                        add("Selected Term Has No Class");
                    }};
                }
                Choose_Class_Spinner_adapter = new ArrayAdapter<String>(mThisActivity, android.R.layout.simple_spinner_dropdown_item, ClassesNames);
                Choose_Class_Spinner.setAdapter(Choose_Class_Spinner_adapter);

                //step 3
                termData data = Terms.get(position);
                String data_StartDate_details[] = data.getStartDate().split("/");
                mSelectedTermStartCal.set(Calendar.YEAR, Integer.parseInt(data_StartDate_details[2]));
                mSelectedTermStartCal.set(Calendar.MONTH, Integer.parseInt(data_StartDate_details[1]));
                mSelectedTermStartCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(data_StartDate_details[0]));

                String data_EndDate_details[] = data.getEndDate().split("/");
                mSelectedTermEndCal.set(Calendar.YEAR, Integer.parseInt(data_EndDate_details[2]));
                mSelectedTermEndCal.set(Calendar.MONTH, Integer.parseInt(data_EndDate_details[1]));
                mSelectedTermEndCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(data_EndDate_details[0]));

                String Init_date = mSelectedTermStartCal.get(Calendar.DAY_OF_MONTH) + "/" + mSelectedTermStartCal.get(Calendar.MONTH) + "/" + mSelectedTermStartCal.get(Calendar.YEAR);
                Utilities.Show_Date_on_one_TextView(Init_date, mExamDateTV);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /*+=+=+=+==+=+=+=+==+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+==+=+=+=+=*/
        //step 2
        mClasses = mdbHelper.getAllClasses(mSelectedTermID);
        if (!mClasses.isEmpty()) {
            mSelectedClassID = mClasses.get(0).getID();
            mClassColorTV.setBackgroundColor(getResources().getColor(Integer.parseInt(mClasses.get(0).getColor())));
            ClassesNames = get_Classes_For_Spinner(mClasses);
        } else {
            mSelectedClassID = "null";
            mClassColorTV.setBackgroundColor(getResources().getColor(android.R.color.white));
            ClassesNames = new ArrayList<String>() {{
                add("Selected Term Has No Class");
            }};
        }
        Choose_Class_Spinner_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ClassesNames);
        Choose_Class_Spinner.setAdapter(Choose_Class_Spinner_adapter);
        Choose_Class_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!mClasses.isEmpty()) {
                    mSelectedClassID = mClasses.get(position).getID();
                    mClassColorTV.setBackgroundColor(getResources().getColor(Integer.parseInt(mClasses.get(position).getColor())));
                } else {
                    mSelectedClassID = "null";
                    mClassColorTV.setBackgroundColor(getResources().getColor(android.R.color.white));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /*+=+=+=+==+=+=+=+==+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+==+=+=+=+=*/
        final ArrayList<String> Durations = new ArrayList<String>() {{
            add("5");
            add("10");
            add("15");
            add("20");
            add("25");
            add("30");
            add("35");
            add("40");
            add("45");
            add("50");
            add("55");
            add("60");
            add("65");
            add("70");
            add("75");
            add("80");
            add("85");
            add("90");
            add("95");
            add("100");
            add("105");
            add("110");
            add("120");
            add("125");
            add("130");
            add("135");
            add("140");
            add("145");
            add("150");
            add("155");
            add("160");
            add("165");
            add("170");
            add("175");
            add("180");
            add("185");
            add("190");
            add("195");
            add("200");
            add("205");
            add("210");
            add("215");
            add("220");
            add("225");
            add("230");
            add("235");
            add("240");
            add("245");
            add("250");
            add("255");
            add("260");
            add("265");
            add("270");
            add("275");
            add("280");
            add("285");
            add("290");
            add("295");
            add("300");
        }};
        final ArrayAdapter<String> Choose_Duration_Spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Durations);
        mSelectedDuration = Durations.get(0);
        Choose_Duration_Spinner.setAdapter(Choose_Duration_Spinner_adapter);
        Choose_Duration_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedDuration = Durations.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        /*+=+=+=+==+=+=+=+==+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+==+=+=+=+=*/
        mExamDateCal.set(Calendar.DAY_OF_MONTH, mSelectedTermStartCal.get(Calendar.DAY_OF_MONTH));
        mExamDateCal.set(Calendar.YEAR, mSelectedTermStartCal.get(Calendar.YEAR));
        mExamDateCal.set(Calendar.MONTH, mSelectedTermStartCal.get(Calendar.MONTH));
        mExamDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddOrEditExamActivity.this, R.style.Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //step 1
                        mExamDateCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        mExamDateCal.set(Calendar.YEAR, year);
                        mExamDateCal.set(Calendar.MONTH, monthOfYear);

                        String _date = mExamDateCal.get(Calendar.DAY_OF_MONTH) + "/" + mExamDateCal.get(Calendar.MONTH) + "/" + mExamDateCal.get(Calendar.YEAR);
                        Utilities.Show_Date_on_one_TextView(_date, mExamDateTV);
                    }
                }, mExamDateCal.get(Calendar.YEAR), mExamDateCal.get(Calendar.MONTH), mExamDateCal.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setTitle("Select Exam Date");
                datePickerDialog.getDatePicker().setMinDate(mSelectedTermStartCal.getTime().getTime());
                datePickerDialog.getDatePicker().setMaxDate(mSelectedTermEndCal.getTime().getTime());
                datePickerDialog.show();
            }
        });
        /*+=+=+=+==+=+=+=+==+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+==+=+=+=+=*/
        SharedPreferences sharedPreferences= getSharedPreferences(String.valueOf(R.string.SharedPreferencesName),MODE_PRIVATE);
        mExamSHour = Integer.parseInt(sharedPreferences.getString("DefaultStartTime_hour",getResources().getString(R.string.DefaultStartTime_hour)));//to set the default start hour
        mExamSMin = Integer.parseInt(sharedPreferences.getString("DefaultStartTime_min",getResources().getString(R.string.DefaultStartTime_min)));//to set the default start Minute
        Utilities.Show_Time_on_one_TextView(mExamSHour, mExamSMin, mExamStartTimeTV);
        mExamStartTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddOrEditExamActivity.this, R.style.Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //step 1
                        mExamSHour = hourOfDay;
                        mExamSMin = minute;
                        Utilities.Show_Time_on_one_TextView(mExamSHour, mExamSMin, mExamStartTimeTV);
                    }
                }, mExamSHour, mExamSMin, false);
                timePickerDialog.show();
            }
        });
    }

    private void Initialize_UI_For_Edit_Mode(String mCurrentExamID) {
        final boolean[] Check = {false};
        //step 0
        mExam = mdbHelper.getExam(mCurrentExamID);
        setTitle("Edit Exam: " + mExam.getModule());
        /*+=+=+=+==+=+=+=+==+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+==+=+=+=+=*/
        mSelectedClassID = mExam.getClassID();
        mSelectedTermID = mdbHelper.getTerm(mdbHelper.getClass(mSelectedClassID).getTermID()).getID();
        mSelectedYearID = mdbHelper.getTerm(mSelectedTermID).getYearID();
        /*+=+=+=+==+=+=+=+==+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+==+=+=+=+=*/
        Years = mdbHelper.getAllYears();
//        mSelectedYearID = Years.get(0).getID();
        YearsNames = get_Years_For_Spinner(Years);
        final ArrayAdapter<String> Choose_Year_Spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, YearsNames);
        Choose_Year_Spinner.setAdapter(Choose_Year_Spinner_adapter);
        int x=YearsNames.indexOf(mdbHelper.getYear(mSelectedYearID).getYearName());
        Choose_Year_Spinner.setSelection(x);
        Choose_Year_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(Check[0]) {
                    mSelectedYearID = Years.get(position).getID();
                    Terms = mdbHelper.getAllTerms(mdbHelper.getYear(mSelectedYearID));
                    TermsNames = get_Terms_For_Spinner(Terms);
                    Choose_Term_Spinner_adapter = new ArrayAdapter<String>(mThisActivity, android.R.layout.simple_spinner_dropdown_item, TermsNames);
                    Choose_Term_Spinner.setAdapter(Choose_Term_Spinner_adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*+=+=+=+==+=+=+=+==+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+==+=+=+=+=*/
        Terms = mdbHelper.getAllTerms(mdbHelper.getYear(mSelectedYearID));
//        mSelectedTermID=Terms.get(0).getID();
        TermsNames = get_Terms_For_Spinner(Terms);
        String data_StartDate_details[] = Terms.get(TermsNames.indexOf(mdbHelper.getTerm(mSelectedTermID).getTermName())).getStartDate().split("/");
        mSelectedTermStartCal.set(Calendar.YEAR, Integer.parseInt(data_StartDate_details[2]));
        mSelectedTermStartCal.set(Calendar.MONTH, Integer.parseInt(data_StartDate_details[1]));
        mSelectedTermStartCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(data_StartDate_details[0]));
        Choose_Term_Spinner_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, TermsNames);
        Choose_Term_Spinner.setAdapter(Choose_Term_Spinner_adapter);
        int xx = TermsNames.indexOf(mdbHelper.getTerm(mSelectedTermID).getTermName());
        Choose_Term_Spinner.setSelection(xx,false);
        Choose_Term_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(Check[0]) {
                    //step 1
                    mSelectedTermID = Terms.get(position).getID();

                    //step 2
                    mClasses = mdbHelper.getAllClasses(mSelectedTermID);
                    if (!mClasses.isEmpty()) {
                        mSelectedClassID = mClasses.get(0).getID();
                        mClassColorTV.setBackgroundColor(getResources().getColor(Integer.parseInt(mClasses.get(0).getColor())));
                        ClassesNames = get_Classes_For_Spinner(mClasses);
                    } else {
                        mSelectedClassID = "null";
                        mClassColorTV.setBackgroundColor(getResources().getColor(android.R.color.white));
                        ClassesNames = new ArrayList<String>() {{
                            add("Selected Term Has No Class");
                        }};
                    }
                    Choose_Class_Spinner_adapter = new ArrayAdapter<String>(mThisActivity, android.R.layout.simple_spinner_dropdown_item, ClassesNames);
                    Choose_Class_Spinner.setAdapter(Choose_Class_Spinner_adapter);

                    //step 3
                    termData data = Terms.get(position);
                    String data_StartDate_details[] = data.getStartDate().split("/");
                    mSelectedTermStartCal.set(Calendar.YEAR, Integer.parseInt(data_StartDate_details[2]));
                    mSelectedTermStartCal.set(Calendar.MONTH, Integer.parseInt(data_StartDate_details[1]));
                    mSelectedTermStartCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(data_StartDate_details[0]));

                    String data_EndDate_details[] = data.getEndDate().split("/");
                    mSelectedTermEndCal.set(Calendar.YEAR, Integer.parseInt(data_EndDate_details[2]));
                    mSelectedTermEndCal.set(Calendar.MONTH, Integer.parseInt(data_EndDate_details[1]));
                    mSelectedTermEndCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(data_EndDate_details[0]));

                    String Init_date = mSelectedTermStartCal.get(Calendar.DAY_OF_MONTH) + "/" + mSelectedTermStartCal.get(Calendar.MONTH) + "/" + mSelectedTermStartCal.get(Calendar.YEAR);
                    Utilities.Show_Date_on_one_TextView(Init_date, mExamDateTV);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /*+=+=+=+==+=+=+=+==+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+==+=+=+=+=*/
        //step 2
        mClasses = mdbHelper.getAllClasses(mSelectedTermID);
        if (!mClasses.isEmpty()) {
//            mSelectedClassID = mClasses.get(0).getID();
            mClassColorTV.setBackgroundColor(getResources().getColor(Integer.parseInt(mdbHelper.getClass(mSelectedClassID).getColor())));
            ClassesNames = get_Classes_For_Spinner(mClasses);
        } else {
            mSelectedClassID = "null";
            mClassColorTV.setBackgroundColor(getResources().getColor(android.R.color.white));
            ClassesNames = new ArrayList<String>() {{
                add("Selected Term Has No Class");
            }};
        }
        Choose_Class_Spinner_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ClassesNames);
        Choose_Class_Spinner.setAdapter(Choose_Class_Spinner_adapter);
        if(!mSelectedClassID.equals("null")) {
            classData Class = mdbHelper.getClass(mSelectedClassID);
            final String Class_name;
            if (!Class.getInstructorName().equals("")) {
                Class_name = Class.getName() + ", " + Class.getInstructorName() + ": " + Class.getModule();
            } else {
                Class_name = Class.getName() + " : " + Class.getModule();
            }
            int y =ClassesNames.indexOf(Class_name);
               Choose_Class_Spinner.setSelection(y);
              // Choose_Class_Spinner.setSelection(y,false);
        }
        Choose_Class_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(Check[0]) {
                    if (!mClasses.isEmpty()) {
                        mSelectedClassID = mClasses.get(position).getID();
                        mClassColorTV.setBackgroundColor(getResources().getColor(Integer.parseInt(mClasses.get(position).getColor())));
                    } else {
                        mSelectedClassID = "null";
                        mClassColorTV.setBackgroundColor(getResources().getColor(android.R.color.white));
                    }
                }else {
                    Check[0] =true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /*+=+=+=+==+=+=+=+==+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+==+=+=+=+=*/
        final ArrayList<String> Durations = new ArrayList<String>() {{
            add("5");
            add("10");
            add("15");
            add("20");
            add("25");
            add("30");
            add("35");
            add("40");
            add("45");
            add("50");
            add("55");
            add("60");
            add("65");
            add("70");
            add("75");
            add("80");
            add("85");
            add("90");
            add("95");
            add("100");
            add("105");
            add("110");
            add("120");
            add("125");
            add("130");
            add("135");
            add("140");
            add("145");
            add("150");
            add("155");
            add("160");
            add("165");
            add("170");
            add("175");
            add("180");
            add("185");
            add("190");
            add("195");
            add("200");
            add("205");
            add("210");
            add("215");
            add("220");
            add("225");
            add("230");
            add("235");
            add("240");
            add("245");
            add("250");
            add("255");
            add("260");
            add("265");
            add("270");
            add("275");
            add("280");
            add("285");
            add("290");
            add("295");
            add("300");
        }};
        final ArrayAdapter<String> Choose_Duration_Spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Durations);
        mSelectedDuration = mExam.getDuration();
        Choose_Duration_Spinner.setAdapter(Choose_Duration_Spinner_adapter);
        Choose_Duration_Spinner.setSelection(Durations.indexOf(mExam.getDuration()));
        Choose_Duration_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedDuration = Durations.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        /*+=+=+=+==+=+=+=+==+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+==+=+=+=+=*/
        String ExamDate_details[] = mExam.getDate().split("/");
        mExamDateCal.set(Calendar.DAY_OF_MONTH,Integer.parseInt(ExamDate_details[0]));
        mExamDateCal.set(Calendar.YEAR, Integer.parseInt(ExamDate_details[2]));
        mExamDateCal.set(Calendar.MONTH, Integer.parseInt(ExamDate_details[1]));
        mExamDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddOrEditExamActivity.this, R.style.Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //step 1
                        mExamDateCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        mExamDateCal.set(Calendar.YEAR, year);
                        mExamDateCal.set(Calendar.MONTH, monthOfYear);

                        String _date = mExamDateCal.get(Calendar.DAY_OF_MONTH) + "/" + mExamDateCal.get(Calendar.MONTH) + "/" + mExamDateCal.get(Calendar.YEAR);
                        Utilities.Show_Date_on_one_TextView(_date, mExamDateTV);
                    }
                }, mExamDateCal.get(Calendar.YEAR), mExamDateCal.get(Calendar.MONTH), mExamDateCal.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setTitle("Select Exam Date");
                datePickerDialog.getDatePicker().setMinDate(mSelectedTermStartCal.getTime().getTime());
                datePickerDialog.getDatePicker().setMaxDate(mSelectedTermEndCal.getTime().getTime());
                datePickerDialog.show();
            }
        });
        String Init_date = mExamDateCal.get(Calendar.DAY_OF_MONTH) + "/" + mExamDateCal.get(Calendar.MONTH) + "/" + mExamDateCal.get(Calendar.YEAR);
        Utilities.Show_Date_on_one_TextView(Init_date, mExamDateTV);
        /*+=+=+=+==+=+=+=+==+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+==+=+=+=+=*/
        String ExamTime_details[] = mExam.getStartTime().split(":");
        mExamSHour = Integer.parseInt(ExamTime_details[0]);//to set the default start hour
        mExamSMin = Integer.parseInt(ExamTime_details[1]);//to set the default start Minute
        Utilities.Show_Time_on_one_TextView(mExamSHour, mExamSMin, mExamStartTimeTV);
        mExamStartTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddOrEditExamActivity.this, R.style.Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //step 1
                        mExamSHour = hourOfDay;
                        mExamSMin = minute;
                        Utilities.Show_Time_on_one_TextView(mExamSHour, mExamSMin, mExamStartTimeTV);
                    }
                }, mExamSHour, mExamSMin, false);
                timePickerDialog.show();
            }
        });

        mExamModuleTV.setText(mExam.getModule());
        mExamPlaceTV.setText(mExam.getPlace());
        Make_Delete_Button_Visible();

    }


    private ArrayList<String> get_Classes_For_Spinner(ArrayList<classData> classes) {
        ArrayList<String> _Data_For_Spinner = new ArrayList<String>();
        if (!classes.isEmpty()) {
            for (classData Class : classes) {
                if (!Class.getInstructorName().equals("")) {
                    _Data_For_Spinner.add(Class.getName() + ", " + Class.getInstructorName() + ": " + Class.getModule());
                } else {
                    _Data_For_Spinner.add(Class.getName() + " : " + Class.getModule());
                }
            }
        } else {
            _Data_For_Spinner.add("No Classes Found");
        }
        return _Data_For_Spinner;
    }

    private ArrayList<String> get_Terms_For_Spinner(ArrayList<termData> Terms) {
        ArrayList<String> _Data_For_Spinner = new ArrayList<String>();
        if (!Terms.isEmpty()) {
            for (termData term : Terms) {
                _Data_For_Spinner.add(term.getTermName());
            }
        } else {
            _Data_For_Spinner.add("No Terms Found");
        }
        return _Data_For_Spinner;
    }

    private ArrayList<String> get_Years_For_Spinner(ArrayList<yearData> years) {
        ArrayList<String> _Data_For_Spinner = new ArrayList<String>();
        if (!years.isEmpty()) {
            for (yearData year : years) {
                _Data_For_Spinner.add(year.getYearName());
            }
        } else {
            _Data_For_Spinner.add("No Years Found");
        }
        return _Data_For_Spinner;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_or_edit_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(Activity.RESULT_CANCELED);
            ShowExitDialog();
        } else if (item.getItemId() == R.id.Save) {
            if (!mAddNewMode_Or_EditMode) /* True=Edit Mode ,  False=Add New Mode */ {
                Save_the_Current_Exam_Data();
            } else {
                Edit_the_Current_Exam_Data();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void Edit_the_Current_Exam_Data() {
        if (!Check_If_Some_Field_Is_Empty_Or_WrongWritten()){
            if (!mSelectedClassID.equals("null")) {
                //Step 1 : Saving the Class Data
                mExam.setModule(mExamModuleTV.getText().toString());
                mExam.setPlace(mExamPlaceTV.getText().toString());
                mExam.setClassID(mSelectedClassID);
                mExam.setDuration(mSelectedDuration);

                String Date = mExamDateCal.get(Calendar.DAY_OF_MONTH) + "/" + mExamDateCal.get(Calendar.MONTH) + "/" + mExamDateCal.get(Calendar.YEAR);
                mExam.setDate(Date);

                mExam.setStartTime(mExamSHour + ":" + mExamSMin);
                mdbHelper.UpdateExam(mExam);

                //step 2
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("deleted_or_not",false);
                setResult(Activity.RESULT_OK, intent);
                this.finish();
            } else {
                Utilities.Show_Error_MSG("An Exam must be assigned to some Class, the selected Term has no classes !", mErrorTV);
            }
        }else {
            //do nothing, MSGs are shown
        }
    }

    private void Save_the_Current_Exam_Data() {
        if (!Check_If_Some_Field_Is_Empty_Or_WrongWritten()) {
            if (!mSelectedClassID.equals("null")) {
                //Step 1 : Saving the Class Data
                mExam.setModule(mExamModuleTV.getText().toString());
                mExam.setPlace(mExamPlaceTV.getText().toString());
                mExam.setClassID(mSelectedClassID);
                mExam.setDuration(mSelectedDuration);

                String Date = mExamDateCal.get(Calendar.DAY_OF_MONTH) + "/" + mExamDateCal.get(Calendar.MONTH) + "/" + mExamDateCal.get(Calendar.YEAR);
                mExam.setDate(Date);

                mExam.setStartTime(mExamSHour + ":" + mExamSMin);
                mdbHelper.addExam(mExam);

                //step 2
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("deleted_or_not",false);
                setResult(Activity.RESULT_OK, intent);
                this.finish();
            } else {
                Utilities.Show_Error_MSG("An Exam must be assigned to some Class, the selected Term has no classes !", mErrorTV);
            }
        } else {
            //do nothing
        }
    }

    private boolean Check_If_Some_Field_Is_Empty_Or_WrongWritten() {
        if (mExamModuleTV.getText().toString().equals("")) {
            Utilities.Show_Error_MSG("You have't Entered the Exam Module !", mErrorTV);
            return true;
        } else if (mExamPlaceTV.getText().toString().equals("")) {
            Utilities.Show_Error_MSG("Please fill the Exam Place field.", mErrorTV);
            return true;
        }
        return false;
    }

    private void ShowExitDialog() {
        String DialogTitle = "", DialogMSG = "", DialogNO = "";
        if (mAddNewMode_Or_EditMode) {//true == EditMode
            DialogTitle = "Canceling Current Editing process";
            DialogMSG = "Are you sure you want to Cancel Editing ?";
        } else {
            DialogTitle = "Canceling adding a new Exam process";
            DialogMSG = "Are you sure you want to Cancel Adding this new Exam?";
        }
        new AlertDialog.Builder(mThisActivity)
                .setTitle(DialogTitle)
                .setMessage(DialogMSG)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        String ToastSuccessMSG = "";
                        if (mAddNewMode_Or_EditMode) {//true == EditMode
                            ToastSuccessMSG = "Editing process Canceled";
                        } else {
                            ToastSuccessMSG = "Adding process Canceled";
                        }
                        Toast.makeText(mThisActivity, ToastSuccessMSG, Toast.LENGTH_SHORT).show();
                    }
                })
                .setIcon(R.drawable.alert)
                .show();

    }

    private void Make_Delete_Button_Visible() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mDeleteTV.setLayoutParams(params);

        mDeleteTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mThisActivity)
                        .setTitle("Confirm Deleting !")
                        .setMessage("Are you sure you want to delete this Exam?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mdbHelper.deleteExam(mExam.getID());
                                Intent resIntent = new Intent();
                                resIntent.putExtra("deleted_or_not", true);
                                setResult(Activity.RESULT_OK, resIntent);
                                finish();
                                Toast.makeText(mThisActivity, "Deleted !", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(mThisActivity, "Nothing Deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setIcon(R.drawable.alert)
                        .show();

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        outState.putInt("Choose_Class_Spinner_currentSelectedItemPosition", Choose_Class_Spinner_currentSelectedItemPosition);
//        outState.putInt("Choose_Duration_Spinner_currentSelectedItemPosition", Choose_Duration_Spinner_currentSelectedItemPosition);
//        outState.putInt("Choose_Term_Spinner_currentSelectedItemPosition", Choose_Term_Spinner_currentSelectedItemPosition);
//        outState.putInt("Choose_Year_Spinner_currentSelectedItemPosition", Choose_Year_Spinner_currentSelectedItemPosition);
//
//        outState.putInt("mExamSHour", mExamSHour);
//        outState.putInt("mExamSMin", mExamSMin);
//
//        outState.putString("mCurrentExamID", mCurrentExamID);//used in Edit Mode
//        outState.putString("mSelectedClassID", mSelectedClassID);
//        outState.putString("mSelectedDuration", mSelectedDuration);
//
//        int mExamDateCal_D = mExamDateCal.get(Calendar.DAY_OF_MONTH);
//        int mExamDateCal_M = mExamDateCal.get(Calendar.MONTH);
//        int mExamDateCal_Y = mExamDateCal.get(Calendar.YEAR);
//        outState.putInt("mExamDateCal_D", mExamDateCal_D);
//        outState.putInt("mExamDateCal_M", mExamDateCal_M);
//        outState.putInt("mExamDateCal_Y", mExamDateCal_Y);
//
//        int mSelectedTermStartCal_D = mSelectedTermStartCal.get(Calendar.DAY_OF_MONTH);
//        int mSelectedTermStartCal_M = mSelectedTermStartCal.get(Calendar.MONTH);
//        int mSelectedTermStartCal_Y = mSelectedTermStartCal.get(Calendar.YEAR);
//        outState.putInt("mSelectedTermStartCal_D", mSelectedTermStartCal_D);
//        outState.putInt("mSelectedTermStartCal_M", mSelectedTermStartCal_M);
//        outState.putInt("mSelectedTermStartCal_Y", mSelectedTermStartCal_Y);
//
//        int mSelectedTermEndCal_D = mSelectedTermEndCal.get(Calendar.DAY_OF_MONTH);
//        int mSelectedTermEndCal_M = mSelectedTermEndCal.get(Calendar.MONTH);
//        int mSelectedTermEndCal_Y = mSelectedTermEndCal.get(Calendar.YEAR);
//        outState.putInt("mSelectedTermEndCal_D", mSelectedTermEndCal_D);
//        outState.putInt("mSelectedTermEndCal_M", mSelectedTermEndCal_M);
//        outState.putInt("mSelectedTermEndCal_Y", mSelectedTermEndCal_Y);
//
//        outState.putBoolean("mAddNewMode_Or_EditMode", mAddNewMode_Or_EditMode);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        Choose_Term_Spinner.setSelection((int) savedInstanceState.get("Choose_Term_Spinner_currentSelectedItemPosition"));
//        Choose_Year_Spinner.setSelection((int) savedInstanceState.get("Choose_Year_Spinner_currentSelectedItemPosition"));
//        Choose_Class_Spinner.setSelection((int) savedInstanceState.get("Choose_Class_Spinner_currentSelectedItemPosition"));
//        Choose_Duration_Spinner.setSelection((int) savedInstanceState.get("Choose_Duration_Spinner_currentSelectedItemPosition"));
//
//        mExamSHour = savedInstanceState.getInt("mExamSHour");
//        mExamSMin = savedInstanceState.getInt("mExamSMin");
//
//        mCurrentExamID = savedInstanceState.getString("mCurrentExamID");//used in Edit Mode
//        mSelectedClassID = savedInstanceState.getString("mSelectedClassID");
//        mSelectedDuration = savedInstanceState.getString("mSelectedDuration");
//
//        int mExamDateCal_D = savedInstanceState.getInt("mExamDateCal_D");
//        int mExamDateCal_M = savedInstanceState.getInt("mExamDateCal_M");
//        int mExamDateCal_Y = savedInstanceState.getInt("mExamDateCal_Y");
//        mExamDateCal.set(Calendar.DAY_OF_MONTH, mExamDateCal_D);
//        mExamDateCal.set(Calendar.MONTH, mExamDateCal_M);
//        mExamDateCal.set(Calendar.YEAR, mExamDateCal_Y);
//
//
//        int mSelectedTermStartCal_D = savedInstanceState.getInt("mSelectedTermStartCal_D");
//        int mSelectedTermStartCal_M = savedInstanceState.getInt("mSelectedTermStartCal_M");
//        int mSelectedTermStartCal_Y = savedInstanceState.getInt("mSelectedTermStartCal_Y");
//        mSelectedTermStartCal.set(Calendar.DAY_OF_MONTH, mSelectedTermStartCal_D);
//        mSelectedTermStartCal.set(Calendar.MONTH, mSelectedTermStartCal_M);
//        mSelectedTermStartCal.set(Calendar.YEAR, mSelectedTermStartCal_Y);
//
//
//        int mSelectedTermEndCal_D = savedInstanceState.getInt("mSelectedTermEndCal_D");
//        int mSelectedTermEndCal_M = savedInstanceState.getInt("mSelectedTermEndCal_M");
//        int mSelectedTermEndCal_Y = savedInstanceState.getInt("mSelectedTermEndCal_Y");
//        mSelectedTermEndCal.set(Calendar.DAY_OF_MONTH, mSelectedTermEndCal_D);
//        mSelectedTermEndCal.set(Calendar.MONTH, mSelectedTermEndCal_M);
//        mSelectedTermEndCal.set(Calendar.YEAR, mSelectedTermEndCal_Y);
//
//        mAddNewMode_Or_EditMode = savedInstanceState.getBoolean("mAddNewMode_Or_EditMode");
//
//        /*****/
//        String Init_date = mExamDateCal.get(Calendar.DAY_OF_MONTH) + "/" + mExamDateCal.get(Calendar.MONTH) + "/" + mExamDateCal.get(Calendar.YEAR);
//        Utilities.Show_Date_on_one_TextView(Init_date, mExamDateTV);
//
//        Utilities.Show_Time_on_one_TextView(mExamSHour, mExamSMin, mExamStartTimeTV);
    }

    @Override
    public void onBackPressed() {
        ShowExitDialog();
    }
}