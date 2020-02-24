package com.example.atefhares.study_organizer.NewOrEditActivities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.Space;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atefhares.study_organizer.DataClasses.termData;
import com.example.atefhares.study_organizer.DataClasses.yearData;
import com.example.atefhares.study_organizer.DatabaseRelated.DBHelper;
import com.example.atefhares.study_organizer.R;
import com.example.atefhares.study_organizer.UtilitiesClasses.Utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddOrEditYearActivity extends AppCompatActivity {
    private DBHelper mdbHelper;
    private ArrayList<termData> mTermsDataList = new ArrayList<termData>();
    private ArrayList<termData> mDeletedTermsDataList = new ArrayList<termData>(); //for edit mode


    Activity mActivity;
    private boolean mAddNewMode_Or_EditMode; /* True=Edit Mode ,  False=Add New Mode */
    private int mSelectedStartYear, mSelectedStartMonth, mSelectedStartDay;
    private int mSelectedEndYear, mSelectedEndMonth, mSelectedEndDay;
    private boolean mUseDefault = true; //default Value is True

    private TextView mErrorTV;
    private LinearLayout mVerticalLinearLayout;
    private TextView mStartDateTV;
    private TextView mEndDateTV;
    private TextView mAddNewTermTxtView;
    private TextView mDeleteTV;

    private String YearID; // only Used fro Edit Mode

    private AddOrEditYearActivity mThisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_year);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mThisActivity= this;

        mdbHelper = DBHelper.getInstance(this);

        mAddNewTermTxtView = (TextView) findViewById(R.id.addNewTerm);
        mVerticalLinearLayout = (LinearLayout) findViewById(R.id.VL);
        mErrorTV = (TextView) findViewById(R.id.YearErrorTV);
        mStartDateTV = (TextView) findViewById(R.id.startDateTV);
        mEndDateTV = (TextView) findViewById(R.id.endDateTV);
        mDeleteTV = (TextView) findViewById(R.id.DeleteTV);
        mDeleteTV.setBackgroundResource(R.drawable.ripple_effect_square);


        Intent intent = getIntent();
        mAddNewMode_Or_EditMode = intent.getBooleanExtra("Edit", false);

        if (mAddNewMode_Or_EditMode) {
            /* mAddNewMode_Or_EditMode = True = Edit Mode */
            if (intent.hasExtra("YearID")) {
                YearID = intent.getStringExtra("YearID");
                if (!YearID.equals(""))
                    Initialize_UI_For_Edit_Mode(YearID);
            }
        } else {
            /* mAddNewMode_Or_EditMode = False = Add New Mode */
            Initialize_Data_For_Add_New_Mode();
        }
        /*****************************************************************************************************************************************************/

        SetDatePickers();

        /*****************************************************************************************************************************************************/
        if (mAddNewTermTxtView != null) {
            mAddNewTermTxtView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        AddNew_OR_Edit_Term(false, "");
                }
            });
        }
        /*****************************************************************************************************************************************************************/
        if (savedInstanceState != null && savedInstanceState.containsKey("mTermsDataList")) {
            if(mAddNewMode_Or_EditMode) { //in Edit Mode the terms is always created within the method "Initialize_UI_For_Edit_Mode"
                mVerticalLinearLayout.removeAllViews();
            }
            for (termData termData : mTermsDataList) {
                Create_Terms_Views(termData);
            }
        }

        if (savedInstanceState != null
                && savedInstanceState.containsKey("mSelectedStartDay")
                && savedInstanceState.containsKey("mSelectedStartMonth")
                && savedInstanceState.containsKey("mSelectedStartYear")
                && savedInstanceState.containsKey("mSelectedEndDay")
                && savedInstanceState.containsKey("mSelectedEndMonth")
                && savedInstanceState.containsKey("mSelectedEndYear")) {
            String LastStartDate = mSelectedStartDay + "/" + mSelectedStartMonth + "/" + mSelectedStartYear;
            String LastEndDate = mSelectedEndDay + "/" + mSelectedEndMonth + "/" + mSelectedEndYear;
            Utilities.Show_Dates_on_Two_TextViews(LastStartDate, LastEndDate, mStartDateTV, mEndDateTV);
        }
        /*****************************************************************************************************************************************************************/
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
    }

    private void Initialize_UI_For_Edit_Mode(String yearID) {
        //Step 1
        yearData year = mdbHelper.getYear(yearID);
        getSupportActionBar().setTitle("Edit " + year.getYearName());

        //Step 2
        String yearStartDate_details[] = year.getStartDate().split("/");
        mSelectedStartYear = Integer.parseInt(yearStartDate_details[2]);
        mSelectedStartMonth = Integer.parseInt(yearStartDate_details[1]);
        mSelectedStartDay = Integer.parseInt(yearStartDate_details[0]);

        String yearEndDate_details[] = year.getEndDate().split("/");
        mSelectedEndYear = Integer.parseInt(yearEndDate_details[2]);
        mSelectedEndMonth = Integer.parseInt(yearEndDate_details[1]);
        mSelectedEndDay = Integer.parseInt(yearEndDate_details[0]);
        Utilities.Show_Dates_on_Two_TextViews(year.getStartDate(), year.getEndDate(), mStartDateTV, mEndDateTV);//Show Default Start/End Dates on Activity start

        Make_Delete_Button_Visible();

        add_Terms_of_Year_to_layout(year);
    }

    private void add_Terms_of_Year_to_layout(yearData year) {
        ArrayList<termData> Terms = mdbHelper.getAllTerms(year);
        for (termData term : Terms) {
            mTermsDataList.add(term);
            Create_Terms_Views(term);
        }
    }

    private void Make_Delete_Button_Visible() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mDeleteTV.setLayoutParams(params);

        mDeleteTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mThisActivity)
                        .setTitle("Confirm Deleting "+ mSelectedStartYear+"-"+mSelectedEndYear)
                        .setMessage("Are you sure you want to delete this year?\nDeletion of a year will also delete all of it's Terms and Classes")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mdbHelper.deleteYear(YearID);
                                setResult(Activity.RESULT_OK);
                                finish();
                                Toast.makeText(mThisActivity,mSelectedStartYear+"-"+mSelectedEndYear+" is Deleted from Your Database",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(mThisActivity,"Nothing Deleted",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setIcon(R.drawable.alert)
                        .show();

            }
        });
    }

    private void SetDatePickers() {
        if (mStartDateTV != null) {
            mStartDateTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar StartCalendar = Calendar.getInstance();
                    StartCalendar.set(Calendar.DAY_OF_MONTH, mSelectedStartDay);
                    StartCalendar.set(Calendar.MONTH, mSelectedStartMonth);
                    StartCalendar.set(Calendar.YEAR, mSelectedStartYear);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(AddOrEditYearActivity.this, R.style.Dialog, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            //step 1
                            mSelectedStartDay = dayOfMonth;
                            mSelectedStartMonth = monthOfYear;
                            mSelectedStartYear = year;
                            String StartDate = mSelectedStartDay + "/" + mSelectedStartMonth + "/" + mSelectedStartYear;
                            StartCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            StartCalendar.set(Calendar.YEAR, year);
                            StartCalendar.set(Calendar.MONTH, monthOfYear);

                            //step 2 : has a condition
                            if (mUseDefault) {
                                String DefaultEndDate = Utilities.get_End_Date_Using_StartDate_and_DateDuration(StartDate, getResources().getString(R.string.DefaultYearDuration));
                                Utilities.Show_Dates_on_Two_TextViews(StartDate, DefaultEndDate, mStartDateTV, mEndDateTV);//Show Default Start/End Dates on Activity start
                                String DefaultEndDate_details[] = DefaultEndDate.split("/");
                                mSelectedEndDay = Integer.parseInt(DefaultEndDate_details[0]);
                                mSelectedEndMonth = Integer.parseInt(DefaultEndDate_details[1]);
                                mSelectedEndYear = Integer.parseInt(DefaultEndDate_details[2]);
                            } else {
                                Utilities.Show_Date_on_one_TextView(StartDate, mStartDateTV);
                            }

                        }
                    }, StartCalendar.get(Calendar.YEAR), StartCalendar.get(Calendar.MONTH), StartCalendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                }
            });
        }

           /*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

        if (mEndDateTV != null) {
            mEndDateTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar EndCalendar = Calendar.getInstance();
                    EndCalendar.set(Calendar.YEAR, mSelectedEndYear);
                    EndCalendar.set(Calendar.DAY_OF_MONTH, mSelectedEndDay);
                    EndCalendar.set(Calendar.MONTH, mSelectedEndMonth);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(AddOrEditYearActivity.this, R.style.Dialog, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            if (year < mSelectedStartYear) {
                                Utilities.Show_Error_MSG("Error: Year End Date Cannot be before it's Start Date ! - 1", mErrorTV);
                            } else if (year == mSelectedStartYear) {
                                if (monthOfYear < mSelectedStartMonth) {
                                    Utilities.Show_Error_MSG("Error: Year End Date Cannot be before it's Start Date ! - 2", mErrorTV);
                                } else if (monthOfYear == mSelectedStartMonth || monthOfYear - mSelectedStartMonth < (5)) {
                                    Utilities.Show_Error_MSG("Error: Year must be at least of 6 months duration", mErrorTV);
                                } else {
                                    mSelectedEndDay = dayOfMonth;
                                    mSelectedEndMonth = monthOfYear;
                                    mSelectedEndYear = year;
                                    String EndDate = mSelectedEndDay + "/" + mSelectedEndMonth + "/" + mSelectedEndYear;
                                    Utilities.Show_Date_on_one_TextView(EndDate, mEndDateTV);
                                    EndCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                    EndCalendar.set(Calendar.YEAR, year);
                                    EndCalendar.set(Calendar.MONTH, monthOfYear);
                                }
                            } else {
                                mSelectedEndDay = dayOfMonth;
                                mSelectedEndMonth = monthOfYear;
                                mSelectedEndYear = year;
                                String EndDate = mSelectedEndDay + "/" + mSelectedEndMonth + "/" + mSelectedEndYear;
                                EndCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                EndCalendar.set(Calendar.YEAR, year);
                                EndCalendar.set(Calendar.MONTH, monthOfYear);
                                Utilities.Show_Date_on_one_TextView(EndDate, mEndDateTV);
                            }
                            mUseDefault = false;
                        }
                    }, EndCalendar.get(Calendar.YEAR), EndCalendar.get(Calendar.MONTH), EndCalendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                }
            });
        }
    }

    private void Initialize_Data_For_Add_New_Mode() {

        //Step 1 : Setting the Activity's title
        try {
            getSupportActionBar().setTitle("Add a new academic year");
        } catch (Exception e) {
            Log.e("AddOrEditYearActivity", e.getMessage());
        }


        //Step 2: Initializing Data with Default Start/End Dates
        final String DefaultDateDuration = getResources().getString(R.string.DefaultYearDuration);//to set the default Duration

        final Calendar calendar = Calendar.getInstance();

        mSelectedStartYear = calendar.get(Calendar.YEAR);
        mSelectedStartMonth = calendar.get(Calendar.MONTH);
        mSelectedStartDay = calendar.get(Calendar.DAY_OF_MONTH);
        String DefaultStartDate = mSelectedStartDay + "/" + mSelectedStartMonth + "/" + mSelectedStartYear;

        String DefaultEndDate = Utilities.get_End_Date_Using_StartDate_and_DateDuration(DefaultStartDate, DefaultDateDuration);
        String[] DefaultEndDate_details = DefaultEndDate.split("/");
        mSelectedEndYear = Integer.parseInt(DefaultEndDate_details[2]);
        mSelectedEndMonth = Integer.parseInt(DefaultEndDate_details[1]);
        mSelectedEndDay = Integer.parseInt(DefaultEndDate_details[0]);

        //Step 3 : Showing the dates on UI
        Utilities.Show_Dates_on_Two_TextViews(DefaultStartDate, DefaultEndDate, mStartDateTV, mEndDateTV);//Show Default Start/End Dates on Activity start
    }

    private void Create_Terms_Views(final termData Data) {

        //Step 1 : Extract Data
        String StartDate = Data.getStartDate();
        String EndDate = Data.getEndDate();
        String TermName = Data.getTermName();

        String[] CorrectStartDate_Details = Utilities.get_Date_Correct_Style(StartDate);
        String CorrectStartDate = CorrectStartDate_Details[0] + " " + CorrectStartDate_Details[1] + ", " + CorrectStartDate_Details[2];
        String[] CorrectEndDate_Details = Utilities.get_Date_Correct_Style(EndDate);
        String CorrectEndDate = CorrectEndDate_Details[0] + " " + CorrectEndDate_Details[1] + ", " + CorrectEndDate_Details[2];


        //Step 2 : Show Data
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);

        final LinearLayout SubVerticallyLayout = new LinearLayout(this);
        LinearLayout.LayoutParams SubVerticallyLayout_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        SubVerticallyLayout.setLayoutParams(SubVerticallyLayout_params);
        SubVerticallyLayout.setOrientation(LinearLayout.VERTICAL);

        TextView TermNameTV = new TextView(this);
        TermNameTV.setLayoutParams(params);
        TermNameTV.setText(TermName);
        TermNameTV.setGravity(Gravity.CENTER);

        if (Data.getID().length() > 4) {
            String DataID_details[] = Data.getID().split("/");
            TermNameTV.setId(Integer.parseInt(DataID_details[0]) + Integer.parseInt(DataID_details[1]) + Integer.parseInt(DataID_details[2]) + Integer.parseInt(DataID_details[3]) + Integer.parseInt(DataID_details[4]) + 1);
        } else {
            TermNameTV.setId(Integer.parseInt(Data.getID()) + 1 + Integer.parseInt(Data.getID()));
        }

        SubVerticallyLayout.addView(TermNameTV);


        TextView DateTV = new TextView(this);
        DateTV.setLayoutParams(params);
        if (CorrectStartDate_Details[2].equals(CorrectEndDate_Details[2])) {
            DateTV.setText(CorrectStartDate_Details[0] + " " + CorrectStartDate_Details[1] + " - " + CorrectEndDate_Details[0] + " " + CorrectEndDate_Details[1] + ", " + CorrectEndDate_Details[2]);
        } else {
            DateTV.setText(CorrectStartDate + " - " + CorrectEndDate);
        }
        DateTV.setGravity(Gravity.CENTER);

        if (Data.getID().length() > 4) {
            String DataID_details[] = Data.getID().split("/");
            DateTV.setId(Integer.parseInt(DataID_details[0]) + Integer.parseInt(DataID_details[1]) + Integer.parseInt(DataID_details[2]) + Integer.parseInt(DataID_details[3]) + Integer.parseInt(DataID_details[4]) + 2);
        } else {
            DateTV.setId(Integer.parseInt(Data.getID()) + 2 + Integer.parseInt(Data.getID()));
        }

        SubVerticallyLayout.addView(DateTV);

        final LinearLayout HlinearLayout = new LinearLayout(this);
        HlinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams HlinearLayout_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        HlinearLayout.setLayoutParams(HlinearLayout_params);

        HlinearLayout.addView(SubVerticallyLayout);


        ImageView imageView = new ImageView(this);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.gravity = Gravity.CENTER_VERTICAL;
        LinearLayout.LayoutParams imageView_params = new LinearLayout.LayoutParams(50, 50);
        imageView_params.gravity = Gravity.CENTER_VERTICAL;
        imageView.setLayoutParams(imageView_params);
        imageView.setImageResource(R.drawable.delete);
        imageView.setPadding(0, 0, 10, 0);


        HlinearLayout.addView(imageView);
        mVerticalLinearLayout.addView(HlinearLayout);

        final Space space_above = new Space(this);
        LinearLayout.LayoutParams space_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 10);
        space_above.setLayoutParams(space_params);
        mVerticalLinearLayout.addView(space_above);

        final View line = new View(this);
        LinearLayout.LayoutParams line_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        line.setLayoutParams(line_params);
        line.setBackgroundColor(getResources().getColor(R.color.LayoutDivider));
        mVerticalLinearLayout.addView(line);

        final Space space_below = new Space(this);
        space_below.setLayoutParams(space_params);
        mVerticalLinearLayout.addView(space_below);

        TypedValue outValue = new TypedValue();
        this.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);

        imageView.setBackgroundResource(outValue.resourceId);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVerticalLinearLayout.removeView(HlinearLayout);
                mVerticalLinearLayout.removeView(line);
                mVerticalLinearLayout.removeView(space_above);
                mVerticalLinearLayout.removeView(space_below);

                // TODO: 22-Apr-16 :delete from arraylist
                if (mAddNewMode_Or_EditMode) {/* True=Edit Mode ,  False=Add New Mode */
                    mDeletedTermsDataList.add(Data);
                    mTermsDataList.remove(Data);
                } else {
                    mTermsDataList.remove(Data);
                }
            }
        });

        SubVerticallyLayout.setBackgroundResource(outValue.resourceId);
        SubVerticallyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNew_OR_Edit_Term(true, Data.getID());//true is edit Mode
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_CANCELED) {
            // do nothing

        } else if (resultCode == Activity.RESULT_OK) {
            //step 1
            termData Res_term = data.getParcelableExtra("ResultedTerm");

            boolean Came_from_EditMode_OR_AddNewMode = data.getBooleanExtra("Edit_or_AddNew", false);//false is AddNewMode
            if (Came_from_EditMode_OR_AddNewMode) { // true is EditMode
                //Step 1 : Editing
                for (termData term : mTermsDataList) {
                    if (term.getID().equals(Res_term.getID())) {
                        int position_of_the_edited_term = mTermsDataList.indexOf(term);
                        mTermsDataList.set(position_of_the_edited_term, Res_term);
                    }
                }


                //Step 2 : Showing edited Data
                String[] CorrectStartDate_Details = Utilities.get_Date_Correct_Style(Res_term.getStartDate());
                String CorrectStartDate = CorrectStartDate_Details[0] + " " + CorrectStartDate_Details[1] + ", " + CorrectStartDate_Details[2];
                String[] CorrectEndDate_Details = Utilities.get_Date_Correct_Style(Res_term.getEndDate());
                String CorrectEndDate = CorrectEndDate_Details[0] + " " + CorrectEndDate_Details[1] + ", " + CorrectEndDate_Details[2];


                if (Res_term.getID().length() > 4) {
                    String mTermDataID_details[] = Res_term.getID().split("/");
                    TextView TermNameTV = (TextView) findViewById(Integer.parseInt(mTermDataID_details[0]) + Integer.parseInt(mTermDataID_details[1]) + Integer.parseInt(mTermDataID_details[2]) + Integer.parseInt(mTermDataID_details[3]) + Integer.parseInt(mTermDataID_details[4]) + 1);
                    TermNameTV.setText(Res_term.getTermName());

                    TextView TermDateTV = (TextView) findViewById(Integer.parseInt(mTermDataID_details[0]) + Integer.parseInt(mTermDataID_details[1]) + Integer.parseInt(mTermDataID_details[2]) + Integer.parseInt(mTermDataID_details[3]) + Integer.parseInt(mTermDataID_details[4]) + 2);
                    if (CorrectStartDate_Details[2].equals(CorrectEndDate_Details[2])) {
                        TermDateTV.setText(CorrectStartDate_Details[0] + " " + CorrectStartDate_Details[1] + " - " + CorrectEndDate_Details[0] + " " + CorrectEndDate_Details[1] + ", " + CorrectEndDate_Details[2]);
                    } else {
                        TermDateTV.setText(CorrectStartDate + " - " + CorrectEndDate);
                    }

                } else {
                    TextView TermNameTV = (TextView) findViewById(Integer.parseInt(Res_term.getID()) + 1 + Integer.parseInt(Res_term.getID()));
                    TermNameTV.setText(Res_term.getTermName());

                    TextView TermDateTV = (TextView) findViewById(Integer.parseInt(Res_term.getID()) + 2 + Integer.parseInt(Res_term.getID()));
                    if (CorrectStartDate_Details[2].equals(CorrectEndDate_Details[2])) {
                        TermDateTV.setText(CorrectStartDate_Details[0] + " " + CorrectStartDate_Details[1] + " - " + CorrectEndDate_Details[0] + " " + CorrectEndDate_Details[1] + ", " + CorrectEndDate_Details[2]);
                    } else {
                        TermDateTV.setText(CorrectStartDate + " - " + CorrectEndDate);
                    }
                }

            } else {
                //step 1: Save Returned Data to use it on Save_the_Current_Class_Data +++OR+++ Edit_the_Current_Class_Data
                mTermsDataList.add(Res_term);

                //step 2 : Showing Data
                Create_Terms_Views(Res_term);
            }


        }
    }

    private void AddNew_OR_Edit_Term(boolean Edit_or_AddNew, String termID) {

        Bundle bundle = new Bundle();
        Intent Send_intent = new Intent(this, AddOrEditTermActivity.class);

        termData term = new termData();
        if (Edit_or_AddNew && !termID.equals("")) {
            /*True == Edit , False == AddNew*/
            ArrayList<termData> data = new ArrayList<termData>();
            for (termData SendData : mTermsDataList) {
                if (!SendData.getID().equals(termID)) {
                    data.add(SendData);
                } else {
                    term = SendData;
                }
            }
            bundle.putParcelableArrayList("mTermsDataList", data);
            bundle.putParcelableArrayList("mDeletedTermsDataList", mDeletedTermsDataList);


        } else {
            bundle.putParcelableArrayList("mTermsDataList", mTermsDataList);
            bundle.putParcelableArrayList("mDeletedTermsDataList", mDeletedTermsDataList);
        }


        Send_intent.putExtra("mTermsDataList_bundle", bundle);
        Send_intent.putExtra("Edit_or_AddNew", Edit_or_AddNew);
        if (!termID.equals(""))
            Send_intent.putExtra("Term", term);

        Send_intent.putExtra("CurrentYearStartDate", mSelectedStartDay + "/" + mSelectedStartMonth + "/" + mSelectedStartYear);
        Send_intent.putExtra("CurrentYearEndDate", mSelectedEndDay + "/" + mSelectedEndMonth + "/" + mSelectedEndYear);

        startActivityForResult(Send_intent, 1);
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
                Save_the_Current_Year_Data();
            } else {
                Edit_the_Current_Year_Data();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void ShowExitDialog() {
        String DialogTitle="",DialogMSG="",DialogNO="";
        if(mAddNewMode_Or_EditMode){//true == EditMode
            DialogTitle="Canceling "+mSelectedStartYear+"-"+mSelectedEndYear+" Editing process";
            DialogMSG="Are you sure you want to Cancel Editing "+mSelectedStartYear+"-"+mSelectedEndYear+" ?";
        }else {
            DialogTitle="Canceling adding a new year process";
            DialogMSG="Are you sure you want to Cancel Adding a new year?";
        }
        new AlertDialog.Builder(mThisActivity)
                .setTitle(DialogTitle)
                .setMessage(DialogMSG)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(Activity.RESULT_CANCELED);
                        finish();
                        String ToastSuccessMSG="";
                        if(mAddNewMode_Or_EditMode) {//true == EditMode
                            ToastSuccessMSG="Editing process Canceled";
                        }else {
                            ToastSuccessMSG="Adding process Canceled";
                        }
                        Toast.makeText(mThisActivity,ToastSuccessMSG,Toast.LENGTH_SHORT).show();
                    }
                })
                .setIcon(R.drawable.alert)
                .show();
    }

    @Override
    public void onBackPressed() {
        ShowExitDialog();
    }

    private boolean Check_Current_chosen_DateRange_if_Not_Available_Or_OverLapping_Existing_Range(yearData data) {
        ArrayList<yearData> DB_yearsDataList = mdbHelper.getAllYears();

        if (mAddNewMode_Or_EditMode) { /* True == Edit Mode ,  False == AddNew Mode*/
            for (yearData year : DB_yearsDataList) {
                if (year.getID().equals(data.getID())) {
                    DB_yearsDataList.remove(year);
                    break;
                }
            }
        }


        if (DB_yearsDataList != null && !DB_yearsDataList.isEmpty()) {
            for (yearData year_Data : DB_yearsDataList) {
                if (data != null) {
                    if (compare_two_yearData_if_Equal_or_Overlapped(year_Data, data)) {
                        return true;//Current time is Already chosen before
                    }
                }
            }
        }

        return false;
    }

    private boolean compare_two_yearData_if_Equal_or_Overlapped(yearData year_data, yearData data) {
        if (year_data.getStartDate().equals(data.getStartDate()) && year_data.getEndDate().equals(data.getEndDate()))
            return true;//They are Equal
        else if (check_if_two_yearData_are_Overlapped(year_data, data)) {
            return true;//They are Overlapped
        } else
            return false;//They are Different and Distinct
    }

    private boolean check_if_two_yearData_are_Overlapped(yearData year_data, yearData data) {
        if (isOverlapping(year_data.getStartDate(), year_data.getEndDate(), data.getStartDate(), data.getEndDate())) {
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

        return StartDate1.before(EndDate2) && StartDate2.before(EndDate1);
    }

    private void Save_the_Current_Year_Data() {
        yearData data = new yearData();
        data.setStartDate(mSelectedStartDay + "/" + mSelectedStartMonth + "/" + mSelectedStartYear);
        data.setEndDate(mSelectedEndDay + "/" + mSelectedEndMonth + "/" + mSelectedEndYear);
        data.setNameYear(mSelectedStartYear + "-" + mSelectedEndYear);
        if (!Check_Current_chosen_DateRange_if_Not_Available_Or_OverLapping_Existing_Range(data)) {
            if (!mTermsDataList.isEmpty()) {
                if (terms_ranges_are_correct()) {
                    save_year();
                    save_terms_of_this_year();
                    Intent resIntent = new Intent();
                    setResult(Activity.RESULT_OK, resIntent);
                    this.finish();
                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Nothing Saved", Toast.LENGTH_SHORT).show();
                }
            } else {
                Utilities.Show_Error_MSG("Error: This academic-year does not have any terms yet.", mErrorTV);
            }
        } else {
            Utilities.Show_Error_MSG("Error: This academic-year date range is overlapping another academic-year's date range, Academic-Years Dates Cannot Overlap.", mErrorTV);
        }
    }

    private void save_terms_of_this_year() {
        for (termData data : mTermsDataList) {
            String CurrentYearID = getLastEnteredYearID();
            data.setYearID(CurrentYearID);
            mdbHelper.addTerm(data);
        }
    }

    private String getLastEnteredYearID() {
        return mdbHelper.getLastInsertedRowID("years") + "";
    }

    private void save_year() {
        yearData data = new yearData();
        data.setStartDate(mSelectedStartDay + "/" + mSelectedStartMonth + "/" + mSelectedStartYear);
        data.setEndDate(mSelectedEndDay + "/" + mSelectedEndMonth + "/" + mSelectedEndYear);
        data.setNameYear(mSelectedStartYear + " - " + mSelectedEndYear);
        mdbHelper.addYear(data);
    }

    private void Edit_the_Current_Year_Data() {
        yearData data = new yearData();
        data.setStartDate(mSelectedStartDay + "/" + mSelectedStartMonth + "/" + mSelectedStartYear);
        data.setEndDate(mSelectedEndDay + "/" + mSelectedEndMonth + "/" + mSelectedEndYear);
        data.setNameYear(mSelectedStartYear + "-" + mSelectedEndYear);
        data.setID(YearID);
        if (!Check_Current_chosen_DateRange_if_Not_Available_Or_OverLapping_Existing_Range(data)) {
            if (!mTermsDataList.isEmpty()) {
                if (terms_ranges_are_correct()) {
                    Update_Year(data, YearID);
                    Update_terms_of_this_year(YearID);
                    Intent resIntent = new Intent();
                    setResult(Activity.RESULT_OK, resIntent);
                    this.finish();
                    Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Nothing Updated", Toast.LENGTH_SHORT).show();
                }
            } else {
                Utilities.Show_Error_MSG("Error: This academic-year does not have any terms yet.", mErrorTV);
            }
        } else {
            Utilities.Show_Error_MSG("Error: This academic-year date range is overlapping another academic-year's date range, Academic-Years Dates Cannot Overlap.", mErrorTV);
        }
    }

    private void Update_Year(yearData NewYear, String yearID) {
        mdbHelper.UpdateYear(yearID, NewYear);
    }

    private void Update_terms_of_this_year(String yearID) {
        //step 1 : adding new terms or updating the updated terms
        if(mTermsDataList!=null && !mTermsDataList.isEmpty()) {
            for (termData data : mTermsDataList) {
                data.setYearID(yearID);
                if (data.getID().length() > 4) {
                    mdbHelper.addTerm(data);
                } else {
                    mdbHelper.UpdateTerm(data);
                }
            }
        }

        //step 2 : Deleting deleted terms
        if(mDeletedTermsDataList!=null && !mDeletedTermsDataList.isEmpty()) {
            for (termData data : mDeletedTermsDataList) {
                mdbHelper.deleteTerm(data.getID());
            }
        }

    }

    private boolean terms_ranges_are_correct() {
        for (termData term_Data : mTermsDataList) {
            String term_Data_Start_details[] = term_Data.getStartDate().split("/");
            int term_Data_Start_year = Integer.parseInt(term_Data_Start_details[2]);
            int term_Data_Start_month = Integer.parseInt(term_Data_Start_details[1]);
            int term_Data_Start_day = Integer.parseInt(term_Data_Start_details[0]);

            String term_Data_End_details[] = term_Data.getEndDate().split("/");
            int term_Data_End_year = Integer.parseInt(term_Data_End_details[2]);
            int term_Data_End_month = Integer.parseInt(term_Data_End_details[1]);
            int term_Data_End_day = Integer.parseInt(term_Data_End_details[0]);

            if (term_Data_Start_year < mSelectedStartYear) {
                Utilities.Show_Error_MSG("Error: Term Date Range is out of year's date range" + ", at the term: " + term_Data.getTermName(), mErrorTV);
                return false;
            } else if (term_Data_Start_year == mSelectedStartYear) {
                if (term_Data_Start_month < mSelectedStartMonth) {
                    Utilities.Show_Error_MSG("Error: Term Date Range is out of year's date range" + ", at the term: " + term_Data.getTermName(), mErrorTV);
                    return false;
                } else if (term_Data_Start_month == mSelectedStartMonth) {
                    if (term_Data_Start_day < mSelectedStartDay) {
                        Utilities.Show_Error_MSG("Error: Term Date Range is out of year's date range" + ", at the term: " + term_Data.getTermName(), mErrorTV);
                        return false;
                    }
//                    }else if (term_Data_Start_day - mSelectedStartDay < 28) {
//                        Utilities.Show_Error_MSG("Error: Term must be at least 1 month durationZ." + ", at the term: "+term_Data.nameTerm,mErrorTV);
//                        return false;
//                    }
                }
            }


            if (term_Data_End_year > mSelectedEndYear) {
                Utilities.Show_Error_MSG("Error: Term Date Range is out of year's date range" + ", at the term: " + term_Data.getTermName(), mErrorTV);
                return false;
            } else if (term_Data_End_year == mSelectedEndYear) {
                if (term_Data_End_month > mSelectedEndMonth) {
                    Utilities.Show_Error_MSG("Error: Term Date Range is out of year's date range" + ", at the term: " + term_Data.getTermName(), mErrorTV);
                    return false;
                } else if (term_Data_End_month == mSelectedEndMonth) {
                    if (term_Data_End_day > mSelectedEndDay) {
                        Utilities.Show_Error_MSG("Error: Term Date Range is out of year's date range" + ", at the term: " + term_Data.getTermName(), mErrorTV);
                        return false;
                    }
//                    }else if (mSelectedEndDay - term_Data_End_day < 28) {
//                        Utilities.Show_Error_MSG("Error: Term must be at least 1 month durationQ." + ", at the term: "+term_Data.nameTerm,mErrorTV);
//                        return false;
//                    }
                }
            }
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("mTermsDataList", mTermsDataList);
        outState.putParcelableArrayList("mDeletedTermsDataList", mDeletedTermsDataList);


        outState.putInt("mSelectedStartYear", mSelectedStartYear);
        outState.putInt("mSelectedStartMonth", mSelectedStartMonth);
        outState.putInt("mSelectedStartDay", mSelectedStartDay);
        outState.putInt("mSelectedEndYear", mSelectedEndYear);
        outState.putInt("mSelectedEndMonth", mSelectedEndMonth);
        outState.putInt("mSelectedEndDay", mSelectedEndDay);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mTermsDataList = savedInstanceState.getParcelableArrayList("mTermsDataList");
        mDeletedTermsDataList = savedInstanceState.getParcelableArrayList("mDeletedTermsDataList");

        mSelectedStartYear = savedInstanceState.getInt("mSelectedStartYear");
        mSelectedStartMonth = savedInstanceState.getInt("mSelectedStartMonth");
        mSelectedStartDay = savedInstanceState.getInt("mSelectedStartDay");
        mSelectedEndYear = savedInstanceState.getInt("mSelectedEndYear");
        mSelectedEndMonth = savedInstanceState.getInt("mSelectedEndMonth");
        mSelectedEndDay = savedInstanceState.getInt("mSelectedEndDay");

    }
}
