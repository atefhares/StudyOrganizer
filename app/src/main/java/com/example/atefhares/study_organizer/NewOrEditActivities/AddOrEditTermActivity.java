package com.example.atefhares.study_organizer.NewOrEditActivities;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.atefhares.study_organizer.DataClasses.termData;
import com.example.atefhares.study_organizer.DatabaseRelated.DBHelper;
import com.example.atefhares.study_organizer.R;
import com.example.atefhares.study_organizer.UtilitiesClasses.Utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddOrEditTermActivity extends AppCompatActivity {
    private DBHelper mdbHelper;

    private int mSelectedStartYear, mSelectedStartMonth, mSelectedStartDay;
    private int mSelectedEndYear, mSelectedEndMonth, mSelectedEndDay;
    private boolean mUseDefault = true;
    private ArrayList<termData> mTermsDataList = new ArrayList<termData>();
    private ArrayList<termData> mDeletedTermsDataList = new ArrayList<termData>();

    private TextView mErrorTV;
    private TextView startDateTV;
    private TextView endDateTV;
    private EditText mTermNameTV;

    private Calendar mStartCal = Calendar.getInstance();
    private Calendar mEndCal = Calendar.getInstance();
    private boolean mAddNewMode_Or_EditMode;

    private termData mTerm; // only used in Edit
    private String mDefaultDateDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_term);


        mdbHelper = DBHelper.getInstance(this);
        /*****************************************************************************************************************************************************/
        startDateTV = (TextView) findViewById(R.id.TermStartDateTV);
        endDateTV = (TextView) findViewById(R.id.TermEndDateTV);
        mErrorTV = (TextView) findViewById(R.id.TermErrorTV);
        mTermNameTV = (EditText) findViewById(R.id.termName);

        /*****************************************************************************************************************************************************/
        if (getIntent().hasExtra("CurrentYearStartDate") && getIntent().hasExtra("CurrentYearEndDate")) {
            String year_startDate = getIntent().getStringExtra("CurrentYearStartDate");
            String year_startDate_details[] = year_startDate.split("/");
            mStartCal.set(Calendar.YEAR, Integer.parseInt(year_startDate_details[2]));
            mStartCal.set(Calendar.MONTH, Integer.parseInt(year_startDate_details[1]));
            mStartCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(year_startDate_details[0]));

            String year_endDate = getIntent().getStringExtra("CurrentYearEndDate");
            String year_endDate_details[] = year_endDate.split("/");
            mEndCal.set(Calendar.YEAR, Integer.parseInt(year_endDate_details[2]));
            mEndCal.set(Calendar.MONTH, Integer.parseInt(year_endDate_details[1]));
            mEndCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(year_endDate_details[0]));
        }
        /*****************************************************************************************************************************************************/

        Intent intent = getIntent();
        mAddNewMode_Or_EditMode = intent.getBooleanExtra("Edit_or_AddNew", false);
        if (mAddNewMode_Or_EditMode && intent.hasExtra("Term")) {
            /* mAddNewMode_Or_EditMode = True = Edit Mode */
            mTerm = intent.getParcelableExtra("Term");
            if (mTerm != null)
                Initialize_UI_For_Edit_Mode(mTerm);
        } else {
            /* mAddNewMode_Or_EditMode = False = Add New Mode */
            Initialize_UI_For_Add_New_Mode();
        }
        /*****************************************************************************************************************************************************/

        //Getting the previous chosen terms and the deleted_by_user terms for this year to use in "Check_Current_chosen_Term_if_Selected_Before"
        if (getIntent().hasExtra("mTermsDataList_bundle")) {
            Bundle bundle = getIntent().getBundleExtra("mTermsDataList_bundle");
            if (bundle != null) {
                mTermsDataList = bundle.getParcelableArrayList("mTermsDataList");
                mDeletedTermsDataList = bundle.getParcelableArrayList("mDeletedTermsDataList");
            }
        }


        if (savedInstanceState != null) {
            mStartCal.set(Calendar.YEAR, mSelectedStartYear);
            mStartCal.set(Calendar.MONTH, mSelectedStartMonth);
            mStartCal.set(Calendar.DAY_OF_MONTH, mSelectedStartDay);

            mEndCal.set(Calendar.YEAR, mSelectedEndYear);
            mEndCal.set(Calendar.MONTH, mSelectedEndMonth);
            mEndCal.set(Calendar.DAY_OF_MONTH, mSelectedEndDay);

        }


        /*****************************************************************************************************************************************************/

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //setting back button color to white
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.close, null);
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, Color.WHITE);
            getSupportActionBar().setHomeAsUpIndicator(drawable);
        } catch (Exception e) {
        }

        /*****************************************************************************************************************************************************/


        Set_Pickers();


    }

    private void Set_Pickers() {
        /*****************************************************************************************************************************************************/
        if (startDateTV != null) {
            startDateTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar calendar = mStartCal;
                    DatePickerDialog datePickerDialog = new DatePickerDialog(AddOrEditTermActivity.this, R.style.Dialog, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            if (dayOfMonth < mStartCal.get(Calendar.DAY_OF_MONTH) && monthOfYear == mStartCal.get(Calendar.MONTH)) {
                                Utilities.Show_Error_MSG("Error: Term Start Date Cannot be before it's Year Start Date !", mErrorTV);
                            } else {
                                //step 1
                                mSelectedStartDay = dayOfMonth;
                                mSelectedStartMonth = monthOfYear;
                                mSelectedStartYear = year;
                                String StartDate = mSelectedStartDay + "/" + mSelectedStartMonth + "/" + mSelectedStartYear;
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, monthOfYear);
                                //step 2 : has a condition
                                if (mUseDefault) {
                                    String DefaultEndDate = Utilities.get_End_Date_Using_StartDate_and_DateDuration(StartDate, mDefaultDateDuration);
                                    String CurrentYearEndDate = mEndCal.get(Calendar.DAY_OF_MONTH) + "/" + mEndCal.get(Calendar.MONTH) + "/" + mEndCal.get(Calendar.YEAR);
                                    int check = Utilities.Compare_Two_Calendar_Dates_Givin_As_String(DefaultEndDate, CurrentYearEndDate);
                                    if (check == -1 || check == 0) {
                                        Utilities.Show_Dates_on_Two_TextViews(StartDate, DefaultEndDate, startDateTV, endDateTV);
                                        String DefaultEndDate_details[] = DefaultEndDate.split("/");
                                        mSelectedEndDay = Integer.parseInt(DefaultEndDate_details[0]);
                                        mSelectedEndMonth = Integer.parseInt(DefaultEndDate_details[1]);
                                        mSelectedEndYear = Integer.parseInt(DefaultEndDate_details[2]);
                                    } else {
                                        if (Utilities.Deference_Between_two_Calendar_dates_given_as_string(CurrentYearEndDate, StartDate) >= 28) {
                                            //at least one month
                                            DefaultEndDate = CurrentYearEndDate;
                                            Utilities.Show_Dates_on_Two_TextViews(StartDate, DefaultEndDate, startDateTV, endDateTV);
                                            String DefaultEndDate_details[] = DefaultEndDate.split("/");
                                            mSelectedEndDay = Integer.parseInt(DefaultEndDate_details[0]);
                                            mSelectedEndMonth = Integer.parseInt(DefaultEndDate_details[1]);
                                            mSelectedEndYear = Integer.parseInt(DefaultEndDate_details[2]);
                                        } else {
                                            Utilities.Show_Error_MSG("Error: terms dates cannot overlap, must be at least 1 month duration and of course cannot exceed year's date range", mErrorTV);
                                        }
                                    }
                                } else {
                                    Utilities.Show_Date_on_one_TextView(StartDate, startDateTV);
                                }
                            }

                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.setTitle("Select Term's Start Date");
                    datePickerDialog.getDatePicker().setMinDate(mStartCal.getTime().getTime());
                    datePickerDialog.getDatePicker().setMaxDate(mEndCal.getTime().getTime());
                    datePickerDialog.show();
                }
            });
        }

           /*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

        if (endDateTV != null) {
            endDateTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_MONTH, mSelectedEndDay);
                    calendar.set(Calendar.YEAR, mSelectedEndYear);
                    calendar.set(Calendar.MONTH, mSelectedEndMonth);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(AddOrEditTermActivity.this, R.style.Dialog, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            if (dayOfMonth > mEndCal.get(Calendar.DAY_OF_MONTH) && monthOfYear == mEndCal.get(Calendar.MONTH)) {
                                Utilities.Show_Error_MSG("Error: Term End Date Cannot be After it's Year End Date !", mErrorTV);
                            } else {
                                if (year < mSelectedStartYear) {
                                    Utilities.Show_Error_MSG("Error: Term End Date Cannot be before it's Start Date !", mErrorTV);
                                } else if (year == mSelectedStartYear) {
                                    if (monthOfYear < mSelectedStartMonth) {
                                        Utilities.Show_Error_MSG("Error: Term End Date Cannot be before it's Start Date !", mErrorTV);
                                    } else if (monthOfYear == mSelectedStartMonth) {
                                        if (dayOfMonth < mSelectedStartDay) {
                                            Utilities.Show_Error_MSG("Error: Term End Date Cannot be before it's Start Date !", mErrorTV);
                                        } else if (dayOfMonth - mSelectedStartDay < 28) {
                                            Utilities.Show_Error_MSG("Error: Term must be at least of 1 month duration", mErrorTV);
                                        } else {
                                            mSelectedEndDay = dayOfMonth;
                                            mSelectedEndMonth = monthOfYear;
                                            mSelectedEndYear = year;
                                            String EndDate = mSelectedEndDay + "/" + mSelectedEndMonth + "/" + mSelectedEndYear;
                                            Utilities.Show_Date_on_one_TextView(EndDate, endDateTV);
                                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                            calendar.set(Calendar.YEAR, year);
                                            calendar.set(Calendar.MONTH, monthOfYear);
                                        }
                                    } else {
                                        mSelectedEndDay = dayOfMonth;
                                        mSelectedEndMonth = monthOfYear;
                                        mSelectedEndYear = year;
                                        String EndDate = mSelectedEndDay + "/" + mSelectedEndMonth + "/" + mSelectedEndYear;
                                        Utilities.Show_Date_on_one_TextView(EndDate, endDateTV);
                                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                        calendar.set(Calendar.YEAR, year);
                                        calendar.set(Calendar.MONTH, monthOfYear);
                                    }
                                } else {
                                    mSelectedEndDay = dayOfMonth;
                                    mSelectedEndMonth = monthOfYear;
                                    mSelectedEndYear = year;
                                    String EndDate = mSelectedEndDay + "/" + mSelectedEndMonth + "/" + mSelectedEndYear;
                                    Utilities.Show_Date_on_one_TextView(EndDate, endDateTV);
                                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                    calendar.set(Calendar.YEAR, year);
                                    calendar.set(Calendar.MONTH, monthOfYear);
                                }
                                mUseDefault = false;
                            }
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.setTitle("Select Term's End Date");
                    datePickerDialog.getDatePicker().setMinDate(mStartCal.getTime().getTime());
                    datePickerDialog.getDatePicker().setMaxDate(mEndCal.getTime().getTime());
                    datePickerDialog.show();
                }
            });
        }
        /*****************************************************************************************************************************************************/
    }

    private void Initialize_UI_For_Edit_Mode(termData Term) {

        //Step 1 : Set title
        setTitle("Edit " + Term.getTermName());

        //Step 2 : Initializing Date Data with Default Start/End Dates
        SharedPreferences sharedPreferences= getSharedPreferences(String.valueOf(R.string.SharedPreferencesName),MODE_PRIVATE);
        mDefaultDateDuration = sharedPreferences.getString("DefaultDateDuration" , getResources().getString(R.string.DefaultDuration));

        String termStartDate_details[] = Term.getStartDate().split("/");
        mSelectedStartYear = Integer.parseInt(termStartDate_details[2]);
        mSelectedStartMonth = Integer.parseInt(termStartDate_details[1]);
        mSelectedStartDay = Integer.parseInt(termStartDate_details[0]);

        String StartDate = mSelectedStartDay + "/" + mSelectedStartMonth + "/" + mSelectedStartYear;

        String[] EndDate_details = Term.getEndDate().split("/");
        mSelectedEndYear = Integer.parseInt(EndDate_details[2]);
        mSelectedEndMonth = Integer.parseInt(EndDate_details[1]);
        mSelectedEndDay = Integer.parseInt(EndDate_details[0]);
        String EndDate = mSelectedEndDay + "/" + mSelectedEndMonth + "/" + mSelectedEndYear;

        //Step 3 : Show Default Start/End Dates on Activity start
        Utilities.Show_Dates_on_Two_TextViews(StartDate, EndDate, startDateTV, endDateTV);

        //Step 4 : Setting Term Name on UI
        mTermNameTV.setText(Term.getTermName());
        /*****************************************************************************************************************************************************/

    }

    private void Initialize_UI_For_Add_New_Mode() {
        //Step 1 : Set title
        setTitle("New Term");

        //Step 2 : Initializing Date Data with Default Start/End Dates
        SharedPreferences sharedPreferences= getSharedPreferences(String.valueOf(R.string.SharedPreferencesName),MODE_PRIVATE);
        mDefaultDateDuration = sharedPreferences.getString("DefaultDateDuration" , getResources().getString(R.string.DefaultDuration));

//        mDefaultDateDuration = getResources().getString(R.string.DefaultDateDuration);//to set the default Duration
        mSelectedStartYear = mStartCal.get(Calendar.YEAR);
        mSelectedStartMonth = mStartCal.get(Calendar.MONTH);
        mSelectedStartDay = mStartCal.get(Calendar.DAY_OF_MONTH);
        String DefaultStartDate = mSelectedStartDay + "/" + mSelectedStartMonth + "/" + mSelectedStartYear;
        String DefaultEndDate = Utilities.get_End_Date_Using_StartDate_and_DateDuration(DefaultStartDate, mDefaultDateDuration);
        String[] DefaultEndDate_details = DefaultEndDate.split("/");
        mSelectedEndYear = Integer.parseInt(DefaultEndDate_details[2]);
        mSelectedEndMonth = Integer.parseInt(DefaultEndDate_details[1]);
        mSelectedEndDay = Integer.parseInt(DefaultEndDate_details[0]);

        //Step 3 : Show Default Start/End Dates on Activity start
        Utilities.Show_Dates_on_Two_TextViews(DefaultStartDate, DefaultEndDate, startDateTV, endDateTV);
    }

    private boolean Check_Current_chosen_Date_if_Not_Available_Or_Overlapping_Existing_DateRange(termData GivenTermData) {
        ArrayList<termData> DB_termsDataList = mdbHelper.getAllTerms();

        //step 1: removing the current being edited term from comparing
        if (mAddNewMode_Or_EditMode && mTerm!=null) { /* True == Edit Mode ,  False == AddNew Mode*/
            for (termData data : DB_termsDataList) {
                if (data.getID().equals(mTerm.getID())) {
                    DB_termsDataList.remove(data);
                    break;
                }
            }
        }

        // step 2 : removing the deleted by user terms from comparing
        for(termData deleted_data : mDeletedTermsDataList) {
            for (termData data : DB_termsDataList) {
                if (data.getID().equals(deleted_data.getID())) {
                    DB_termsDataList.remove(data);
                    break;
                }
            }
        }


        if (DB_termsDataList != null) {
            for (termData mTermData : DB_termsDataList) {
                if (mTermData != null)
                    if (compare_two_TermData_if_Equal_or_Overlapped(mTermData, GivenTermData))
                        return true;//Current time is Already chosen before
            }
        }
        return false;
    }

    private boolean Check_Current_chosen_Term_if_Equal_oR_Overlapping_an_Already_Selected_Term_Before(termData mTermData) {
        if (mTermsDataList == null) {
            return false;//Current term is not chosen before
        } else {
            for (termData Data : mTermsDataList) {
                if (Data != null)
                    if (compare_two_TermData_if_Equal_or_Overlapped(Data, mTermData))
                        return true;//Current term is Already chosen before
            }
        }
        return false;
    }

    private boolean compare_two_TermData_if_Equal_or_Overlapped(termData termData1, termData termData2) {
        if (termData1.getStartDate().equals(termData2.getStartDate()) &&
                termData1.getEndDate().equals(termData2.getEndDate()))
            return true;//They are Equal
        else if (check_if_two_TermData_are_Overlapped(termData1, termData2)) {
            return true;//They are Overlapped
        } else
            return false;//They are Different and Distinct
    }

    private boolean check_if_two_TermData_are_Overlapped(termData termData1, termData termData2) {
        if (isOverlapping(termData1.getStartDate(), termData1.getEndDate(), termData2.getStartDate(), termData2.getEndDate())) {
            return true;
        }
        return false;
    }

    private boolean isOverlapping(String startDate1, String endDate1, String startDate2, String endDate2) {
        String startDate1_details[] = startDate1.split("/");
        int startDate1_day = Integer.parseInt(startDate1_details[0]);
        int startDate1_month = Integer.parseInt(startDate1_details[1]);
        int startDate1_year = Integer.parseInt(startDate1_details[2]);
        Date StartDate1 = new Date();
        StartDate1.setDate(startDate1_day);
        StartDate1.setYear(startDate1_year);
        StartDate1.setMonth(startDate1_month);

        String endDate1_details[] = endDate1.split("/");
        int endDate1_day = Integer.parseInt(endDate1_details[0]);
        int endDate1_month = Integer.parseInt(endDate1_details[1]);
        int endDate1_year = Integer.parseInt(endDate1_details[2]);
        Date EndDate1 = new Date();
        EndDate1.setDate(endDate1_day);
        EndDate1.setYear(endDate1_year);
        EndDate1.setMonth(endDate1_month);

        String startDate2_details[] = startDate2.split("/");
        int startDate2_day = Integer.parseInt(startDate2_details[0]);
        int startDate2_month = Integer.parseInt(startDate2_details[1]);
        int startDate2_year = Integer.parseInt(startDate2_details[2]);
        Date StartDate2 = new Date();
        StartDate2.setDate(startDate2_day);
        StartDate2.setYear(startDate2_year);
        StartDate2.setMonth(startDate2_month);

        String endDate2_details[] = endDate2.split("/");
        int endDate2_day = Integer.parseInt(endDate2_details[0]);
        int endDate2_month = Integer.parseInt(endDate2_details[1]);
        int endDate2_year = Integer.parseInt(endDate2_details[2]);
        Date EndDate2 = new Date();
        EndDate2.setDate(endDate2_day);
        EndDate2.setYear(endDate2_year);
        EndDate2.setMonth(endDate2_month);

        return StartDate1.getTime()<EndDate2.getTime() && StartDate2.getTime()<EndDate1.getTime();
//        return StartDate1.before(EndDate2) && StartDate2.before(EndDate1);
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
            String SelectedTermStartDate = mSelectedStartDay + "/" + mSelectedStartMonth + "/" + mSelectedStartYear;
            String SelectedTermEndDate = mSelectedEndDay + "/" + mSelectedEndMonth + "/" + mSelectedEndYear;
            termData Data = new termData();
            Data.setStartDate(SelectedTermStartDate);
            Data.setEndDate(SelectedTermEndDate);
            Data.setName(mTermNameTV.getText().toString());
            if (mAddNewMode_Or_EditMode && mTerm!=null) {
                /*True == Edit Mode , False == AddNew Mode*/
                Data.setID(mTerm.getID()); //the same Edited Term ID
            } else {
                Data.setID(Data.getStartDate() + Data.getEndDate()); //Some Useless ID
            }

            //Handling Possible Errors before sending data back
            if (Data.getTermName().equals("")) {
                Utilities.Show_Error_MSG("Error: Please Enter the Term Name.", mErrorTV);
            } else if (Check_Current_Chosen_Term_if_equal_an_already_chosen_term_name(Data)) {
                Utilities.Show_Error_MSG("Error: Term Name Already Chosen Before. please choose another name", mErrorTV);
            } else if (Data.getTermName().length() < 2) {
                Utilities.Show_Error_MSG("Error: Term Name length cannot be less than two Characters", mErrorTV);
//            } else if (! terms_ranges_are_correct()) {
//                // just to be more sure that every thing is correct
            } else {
                if (!Check_Current_chosen_Date_if_Not_Available_Or_Overlapping_Existing_DateRange(Data)) {
                    if (!Check_Current_chosen_Term_if_Equal_oR_Overlapping_an_Already_Selected_Term_Before(Data)) {
                        SendResult(Data);
                    } else {
                        Utilities.Show_Error_MSG("Error: Selected Date Range is Overlapping an Already chosen before Term' Date Range, Terms Date Ranges Cannot Overlap.", mErrorTV);
                    }
                } else {
                    Utilities.Show_Error_MSG("Error: Selected Date Range is Overlapping another Year/Term 's Date Range, Year's Terms's Date Ranges Cannot Overlap.", mErrorTV);
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void SendResult(termData Data) {
        Intent return_intent = new Intent();
        //String[] res = {SelectedTermStartDate, SelectedTermEndDate, Data.getTermName(), Data.getID()};
        return_intent.putExtra("ResultedTerm", Data);
        return_intent.putExtra("Edit_or_AddNew", mAddNewMode_Or_EditMode);
        setResult(Activity.RESULT_OK, return_intent);
        this.finish();
    }

    private boolean Check_Current_Chosen_Term_if_equal_an_already_chosen_term_name(termData mTermData) {
        if (mTermsDataList == null) {
            return false;//Current term is not chosen before
        } else {
            for (termData Data : mTermsDataList) {
                if (Data != null)
                    if (Data.getTermName().equals(mTermData.getTermName()))
                        return true;//Current term is Already chosen before
            }
        }
        return false;

    }

    private boolean terms_ranges_are_correct() {
        if (mSelectedStartYear < mStartCal.get(Calendar.YEAR)) {
            Utilities.Show_Error_MSG("Error: Term Date Range is out of year's date range", mErrorTV);
            return false;
        } else if (mSelectedStartYear == mStartCal.get(Calendar.YEAR)) {
            if (mSelectedStartMonth < mStartCal.get(Calendar.MONTH)) {
                Utilities.Show_Error_MSG("Error: Term Date Range is out of year's date range", mErrorTV);
                return false;
            } else if (mSelectedStartMonth == mStartCal.get(Calendar.MONTH)) {
                if (mSelectedStartDay - mStartCal.get(Calendar.DAY_OF_MONTH) < 28) {
                    Utilities.Show_Error_MSG("Error: Term must be at least 1 month duration. LL", mErrorTV);
                    return false;
                }
            }
        }


        if (mSelectedEndYear > mEndCal.get(Calendar.YEAR)) {
            Utilities.Show_Error_MSG("Error: Term Date Range is out of year's date range", mErrorTV);
            return false;
        } else if (mSelectedEndYear == mEndCal.get(Calendar.YEAR)) {
            if (mSelectedEndMonth > mEndCal.get(Calendar.MONTH)) {
                Utilities.Show_Error_MSG("Error: Term Date Range is out of year's date range", mErrorTV);
                return false;
            } else if (mSelectedEndMonth == mEndCal.get(Calendar.MONTH)) {
                if (mEndCal.get(Calendar.DAY_OF_MONTH) - mSelectedEndDay < 28) {
                    Utilities.Show_Error_MSG("Error: Term must be at least 1 month duration. KK", mErrorTV);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("mTermsDataList", mTermsDataList);

        outState.putInt("mSelectedStartYear", mSelectedStartYear);
        outState.putInt("mSelectedStartMonth", mSelectedStartMonth);
        outState.putInt("mSelectedStartDay", mSelectedStartDay);
        outState.putInt("mSelectedEndYear", mSelectedEndYear);
        outState.putInt("mSelectedEndMonth", mSelectedEndMonth);
        outState.putInt("mSelectedEndDay", mSelectedEndDay);

        outState.putParcelable("mTerm", mTerm);
        outState.putString("mDefaultDateDuration", mDefaultDateDuration);

        outState.putBoolean("mAddNewMode_Or_EditMode", mAddNewMode_Or_EditMode);
        outState.putBoolean("mUseDefault", mUseDefault);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mTermsDataList = savedInstanceState.getParcelableArrayList("mTermsDataList");
        mSelectedStartYear = savedInstanceState.getInt("mSelectedStartYear");
        mSelectedStartMonth = savedInstanceState.getInt("mSelectedStartMonth");
        mSelectedStartDay = savedInstanceState.getInt("mSelectedStartDay");
        mSelectedEndYear = savedInstanceState.getInt("mSelectedEndYear");
        mSelectedEndMonth = savedInstanceState.getInt("mSelectedEndMonth");
        mSelectedEndDay = savedInstanceState.getInt("mSelectedEndDay");

        mTerm = savedInstanceState.getParcelable("mTerm");
        mDefaultDateDuration = savedInstanceState.getString("mDefaultDateDuration");

        mAddNewMode_Or_EditMode = savedInstanceState.getBoolean("mAddNewMode_Or_EditMode");
        mUseDefault = savedInstanceState.getBoolean("mUseDefault");

    }
}

