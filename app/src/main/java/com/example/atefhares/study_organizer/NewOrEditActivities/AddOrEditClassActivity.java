package com.example.atefhares.study_organizer.NewOrEditActivities;

import android.app.Activity;
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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atefhares.study_organizer.DataClasses.classData;
import com.example.atefhares.study_organizer.DataClasses.termData;
import com.example.atefhares.study_organizer.DataClasses.timeData;
import com.example.atefhares.study_organizer.DataClasses.yearData;
import com.example.atefhares.study_organizer.DatabaseRelated.DBHelper;
import com.example.atefhares.study_organizer.R;
import com.example.atefhares.study_organizer.UtilitiesClasses.Utilities;

import java.util.ArrayList;

public class AddOrEditClassActivity extends AppCompatActivity {

    private DBHelper mdbHelper;

    private boolean mAddNewMode_Or_EditMode; /* True=Edit Mode ,  False=Add New Mode */

    private LinearLayout mVerticalLinearLayout;

    private String mSelectedYearID = "", mSelectedTermID = "", mSelectedColor = "";

    private ArrayList<yearData> Years = new ArrayList<>();
    private ArrayList<timeData> mTimesDataList = new ArrayList<timeData>();
    private ArrayList<timeData> mDeletedTimesDataList = new ArrayList<timeData>(); //for edit mode

    private ArrayList<String> YearsNames = new ArrayList<>();
    private ArrayList<termData> Terms = new ArrayList<>();
    private ArrayList<String> TermsNames = new ArrayList<>();

    private ArrayAdapter<String> Choose_Term_Spinner_adapter;

    private EditText mClassNameTV, mClassModuleTV, mClassPlaceTV, mClassInstructorNameTV;

    private Spinner Choose_Year_Spinner, Choose_Term_Spinner, Colors_Spinner;
    private int Choose_Year_Spinner_currentSelectedItemPosition = 0, Choose_Term_Spinner_currentSelectedItemPosition = 0, Colors_Spinner_currentSelectedItemPosition = 0;
    private classData mClassData = new classData();
    private TextView mErrorTV, mDeleteTV;

    private String YearID, CurrentClassID, TermID; // only Used fro Edit Mode

    private AddOrEditClassActivity mThisActivity;

    //step 1
    final ArrayList<Integer> colors = new ArrayList<Integer>() {{
        add(R.color.Class_Color1);
        add(R.color.Class_Color2);
        add(R.color.Class_Color3);
        add(R.color.Class_Color4);
        add(R.color.Class_Color5);
        add(R.color.Class_Color6);
        add(R.color.Class_Color7);
        add(R.color.Class_Color8);
        add(R.color.Class_Color9);
        add(R.color.Class_Color10);
        add(R.color.Class_Color11);
        add(R.color.Class_Color12);
        add(R.color.Class_Color13);
        add(R.color.Class_Color14);
        add(R.color.Class_Color15);
        add(R.color.Class_Color16);
        add(R.color.Class_Color17);
        add(R.color.Class_Color18);
        add(R.color.Class_Color19);
        add(R.color.Class_Color20);
    }};

    final Integer colors2[] = {R.color.Class_Color1, R.color.Class_Color2, R.color.Class_Color3, R.color.Class_Color4, R.color.Class_Color5, R.color.Class_Color6,
            R.color.Class_Color7, R.color.Class_Color8, R.color.Class_Color9, R.color.Class_Color10, R.color.Class_Color11, R.color.Class_Color12,
            R.color.Class_Color13, R.color.Class_Color14, R.color.Class_Color15,R.color.Class_Color16, R.color.Class_Color17, R.color.Class_Color18,
            R.color.Class_Color19, R.color.Class_Color20};
    CustomSpinnerAdapter customSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mdbHelper = DBHelper.getInstance(this);
        mThisActivity = this;
        customSpinnerAdapter = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, colors2);

        mVerticalLinearLayout = (LinearLayout) findViewById(R.id.VL);
        Choose_Year_Spinner = (Spinner) findViewById(R.id.YearSpinner);
        Choose_Term_Spinner = (Spinner) findViewById(R.id.TermSpinner);
        Colors_Spinner = (Spinner) findViewById(R.id.Colors_Spinner);
        TextView addNewTimeTxtView = (TextView) findViewById(R.id.addNewTime);
        mClassNameTV = (EditText) findViewById(R.id.ClassName);
        mClassModuleTV = (EditText) findViewById(R.id.ClassModule);
        mClassPlaceTV = (EditText) findViewById(R.id.ClassPlace);
        mClassInstructorNameTV = (EditText) findViewById(R.id.InstructorTV);
        mErrorTV = (TextView) findViewById(R.id.ClassErrorTV);
        mDeleteTV = (TextView) findViewById(R.id.DeleteTV);
        mDeleteTV.setBackgroundResource(R.drawable.ripple_effect_square);



        //New Mode or Edit Mode
        Intent intent = getIntent();

            mAddNewMode_Or_EditMode = intent.getBooleanExtra("Edit",false);

        if (mAddNewMode_Or_EditMode) /* True=Edit Mode ,  False=Add New Mode */ {
            /* mAddNewMode_Or_EditMode = True = Edit Mode */
            if (intent.hasExtra("YearID") && intent.hasExtra("TermID") && intent.hasExtra("ClassID")) {
                YearID = intent.getStringExtra("YearID");
                TermID = intent.getStringExtra("TermID");
                CurrentClassID = intent.getStringExtra("ClassID");
                if (!YearID.equals(""))
                    Initialize_UI_For_Edit_Mode(YearID, TermID, CurrentClassID);
            }
        } else {
            /* mAddNewMode_Or_EditMode = False = Add New Mode */
            Initialize_UI_For_AddNew_Mode();
        }


        if (addNewTimeTxtView != null) {
            addNewTimeTxtView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddNew_OR_Edit_Time(false, "");
                }
            });
        }


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

        if (savedInstanceState != null && savedInstanceState.containsKey("mTimesDataList")) {
            mTimesDataList = savedInstanceState.getParcelableArrayList("mTimesDataList");
            if (mAddNewMode_Or_EditMode) { //in Edit Mode the terms is always created within the method "Initialize_UI_For_Edit_Mode"
                mVerticalLinearLayout.removeAllViews();
            }
            for (timeData timeData1 : mTimesDataList) {
                Create_Times_Views(timeData1);
            }
        }
        if (savedInstanceState != null) {
            Choose_Term_Spinner.setSelection(Choose_Term_Spinner_currentSelectedItemPosition);
            Choose_Year_Spinner.setSelection(Choose_Year_Spinner_currentSelectedItemPosition);
            Colors_Spinner.setSelection(Colors_Spinner_currentSelectedItemPosition);
        }
    }

    private void Initialize_UI_For_Edit_Mode(String yearID, String termID, String ClassID) {
        //step 0
        mClassData = mdbHelper.getClass(ClassID);
        setTitle("Edit Class: " + mClassData.getName());

        //step 1
        Colors_Spinner.setAdapter(customSpinnerAdapter);
        Colors_Spinner.setSelection(colors.indexOf(Integer.parseInt(mClassData.getColor())));
        Colors_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mClassData.setColor(colors.get(position) + "");
                Colors_Spinner_currentSelectedItemPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //step 2
        YearsNames = get_Years_For_Spinner();
        mSelectedYearID = yearID;
        mSelectedTermID = termID;

        //step 3
        final ArrayAdapter<String> Choose_Year_Spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, YearsNames);
        if (Choose_Year_Spinner != null) {
            Choose_Year_Spinner.setAdapter(Choose_Year_Spinner_adapter);
            Choose_Year_Spinner.setSelection(YearsNames.indexOf(mdbHelper.getYear(yearID).getYearName()));
            Choose_Year_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String NewSelectionYearID = Years.get(position).getID();
                    if(! NewSelectionYearID.equals(mSelectedYearID)) {
                        mSelectedYearID = NewSelectionYearID;
                        Update_Terms_Spinner_Adapter(mSelectedYearID);
                        Choose_Year_Spinner_currentSelectedItemPosition = position;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        //step 4
        Update_Terms_Spinner_Adapter(mSelectedYearID);
        if (Choose_Term_Spinner != null) {
            Choose_Term_Spinner.setSelection(TermsNames.indexOf(mdbHelper.getTerm(termID).getTermName()));
            Choose_Term_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mSelectedTermID = Terms.get(position).getID();
                    Choose_Term_Spinner_currentSelectedItemPosition = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        //step 5
        mClassNameTV.setText(mClassData.getName());
        mClassModuleTV.setText(mClassData.getModule());
        mClassPlaceTV.setText(mClassData.getPlace());
        mClassInstructorNameTV.setText(mClassData.getInstructorName());

        add_Times_of_Class_to_layout(mClassData);

        Make_Delete_Button_Visible();

    }

    private void add_Times_of_Class_to_layout(classData data) {
        ArrayList<timeData> Times = mdbHelper.getAllTimes_ForSomeClass(data.getID());
        if (Times != null && !Times.isEmpty()) {
            for (timeData time : Times) {
                mTimesDataList.add(time);
                Create_Times_Views(time);
            }
        }
    }

    private void Make_Delete_Button_Visible() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mDeleteTV.setLayoutParams(params);

        mDeleteTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mThisActivity)
                        .setTitle("Confirm Deleting Class: " + mClassData.getName())
                        .setMessage("Are you sure you want to delete this Class?\nDeletion of a Class will also delete all of it's Times and Exams")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mdbHelper.deleteClass(mClassData.getID());
                                Intent resIntent = new Intent();
                                resIntent.putExtra("deleted_or_not",true);
                                setResult(Activity.RESULT_OK, resIntent);
                                finish();
                                Toast.makeText(mThisActivity, "Class: " + mClassData.getName() + " is Deleted from Your Database", Toast.LENGTH_SHORT).show();
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

    private void Initialize_UI_For_AddNew_Mode() {

        setTitle("Add New Class");

        Colors_Spinner.setAdapter(customSpinnerAdapter);
        Colors_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mClassData.setColor(colors.get(position) + "");
                Colors_Spinner_currentSelectedItemPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /***********************************************************************************************************************/

        //step 2
        YearsNames = get_Years_For_Spinner();
        mSelectedYearID = Years.get(0).getID();
        final ArrayAdapter<String> Choose_Year_Spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, YearsNames);
        if (Choose_Year_Spinner != null) {
            Choose_Year_Spinner.setAdapter(Choose_Year_Spinner_adapter);
            Choose_Year_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mSelectedYearID = Years.get(position).getID();
                    Update_Terms_Spinner_Adapter(mSelectedYearID);
                    Choose_Year_Spinner_currentSelectedItemPosition = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        /***********************************************************************************************************************/

        //step 3
        Update_Terms_Spinner_Adapter(mSelectedYearID);
        mSelectedTermID = Terms.get(0).getID();
        if (Choose_Term_Spinner != null) {
            Choose_Term_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mSelectedTermID = Terms.get(position).getID();
                    Choose_Term_Spinner_currentSelectedItemPosition = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private void Update_Terms_Spinner_Adapter(String mSelectedYearID) {
        TermsNames = get_Terms_For_Spinner(mSelectedYearID);
        Choose_Term_Spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, TermsNames);
        if (Choose_Term_Spinner != null) {
            Choose_Term_Spinner.setAdapter(Choose_Term_Spinner_adapter);
        }
    }

    private ArrayList<String> get_Terms_For_Spinner(String yearID) {
        Terms = mdbHelper.getAllTerms(mdbHelper.getYear(yearID));
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

    public ArrayList<String> get_Years_For_Spinner() {
        // TODO: 21-Apr-16 : Get data from Database
        Years = mdbHelper.getAllYears();
        ArrayList<String> _Data_For_Spinner = new ArrayList<String>();
        if (!Years.isEmpty()) {
            for (yearData year : Years) {
                _Data_For_Spinner.add(year.getYearName());
            }
        } else {
            _Data_For_Spinner.add("No Years Found");
        }
        return _Data_For_Spinner;
    }

    private void AddNew_OR_Edit_Time(boolean Edit_or_AddNew, String timeID) {

        Bundle bundle = new Bundle();

        Intent Send_intent = new Intent(this, AddOrEditTimeActivity.class);
        timeData time = new timeData();
        if (Edit_or_AddNew && !timeID.equals("")) {
            /*True == Edit , False == AddNew*/
            ArrayList<timeData> data = new ArrayList<timeData>();
            for (timeData SendData : mTimesDataList) {
                if (!SendData.getID().equals(timeID)) {
                    data.add(SendData);
                } else {
                    time = SendData;
                }
            }
            bundle.putParcelableArrayList("mPreviousChosenTimesDataList", data);
            bundle.putParcelableArrayList("mDeletedTimesDataList", mDeletedTimesDataList);

        } else {
            //Adding a new time
            bundle.putParcelableArrayList("mPreviousChosenTimesDataList", mTimesDataList);
            bundle.putParcelableArrayList("mDeletedTimesDataList", mDeletedTimesDataList);
        }


        Send_intent.putExtra("mTimesDataList_bundle", bundle);
        Send_intent.putExtra("Edit_or_AddNew", Edit_or_AddNew);


        if (!timeID.equals(""))
            Send_intent.putExtra("Time", time);

        Send_intent.putExtra("mSelectedTermID", mSelectedTermID);

        startActivityForResult(Send_intent, 1);
    }

    private void Create_Times_Views(final timeData Data) {

        //Step 1 : Extract Data
        final timeData timeData = Data;
        String[] mStartTime_parts = timeData.getStartTime().split(":");
        String[] mEndTime_parts = new String[2];

        if (mAddNewMode_Or_EditMode) {// True == EditMode
            int Duration = Utilities.Calculate_Duration(Data.getStartTime(), Data.getEndTime());
            Data.setDuration(Duration + "");
            Data.setTermID(mSelectedTermID);
            Data.setClassID(mClassData.getID());
//            int[] mEndTime_details = Utilities.get_EndTime_Using_StartTime(Integer.parseInt(mStartTime_parts[0]), Integer.parseInt(mStartTime_parts[1]), Integer.parseInt(Data.getDuration()));
            mEndTime_parts =Data.getEndTime().split(":");
//            mEndTime_parts[0] = mEndTime_details[0] + "";
//            mEndTime_parts[1] = mEndTime_details[1] + "";
        } else {
            mEndTime_parts = timeData.getEndTime().split(":");
        }


        String[] res_StartTimeTV = Utilities.get_Time_Correct_Style(Integer.parseInt(mStartTime_parts[0]), Integer.parseInt(mStartTime_parts[1]));
        String StartTimeTV_hour_Correct_Style_as_String = res_StartTimeTV[0];
        String StartTimeTV_Min_Correct_Style_as_String = res_StartTimeTV[1];
        String StartTimeTV_Correct_Style_AM_or_PM = res_StartTimeTV[2];

        String[] res_mEndTime = Utilities.get_Time_Correct_Style(Integer.parseInt(mEndTime_parts[0]), Integer.parseInt(mEndTime_parts[1]));
        String mEndTime_hour_Correct_Style_as_String = res_mEndTime[0];
        String mEndTime_Min_Correct_Style_as_String = res_mEndTime[1];
        String mEndTime_Correct_Style_AM_or_PM = res_mEndTime[2];


        //Step 2 : Show Data
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);

        final LinearLayout SubVerticallyLayout = new LinearLayout(this);
        LinearLayout.LayoutParams SubVerticallyLayout_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        SubVerticallyLayout.setLayoutParams(SubVerticallyLayout_params);
        SubVerticallyLayout.setOrientation(LinearLayout.VERTICAL);

        TextView DayTV = new TextView(this);
        DayTV.setLayoutParams(params);
        DayTV.setText(timeData.getDay());
        DayTV.setGravity(Gravity.CENTER);
        if (Data.getID().length() > 4) {
            String DataID_details[] = Data.getID().split(":");
            DayTV.setId(Integer.parseInt(DataID_details[0]) + Integer.parseInt(DataID_details[1] + DataID_details[2]) + 2);
        } else {
            DayTV.setId(Integer.parseInt(Data.getID()) + 2 + Integer.parseInt(Data.getID()));
        }

        SubVerticallyLayout.addView(DayTV);


        TextView TimeTV = new TextView(this);
        TimeTV.setLayoutParams(params);
        if (StartTimeTV_Correct_Style_AM_or_PM.equals(mEndTime_Correct_Style_AM_or_PM)) {
            TimeTV.setText(StartTimeTV_hour_Correct_Style_as_String + ":" + StartTimeTV_Min_Correct_Style_as_String + " - " +
                    mEndTime_hour_Correct_Style_as_String + ":" + mEndTime_Min_Correct_Style_as_String + " " + StartTimeTV_Correct_Style_AM_or_PM);
        } else {
            TimeTV.setText(StartTimeTV_hour_Correct_Style_as_String + ":" + StartTimeTV_Min_Correct_Style_as_String + " " + StartTimeTV_Correct_Style_AM_or_PM +
                    " - " + mEndTime_hour_Correct_Style_as_String + ":" + mEndTime_Min_Correct_Style_as_String + " " + mEndTime_Correct_Style_AM_or_PM);
        }
        TimeTV.setGravity(Gravity.CENTER);

        if (Data.getID().length() > 4) {
            String DataID_details[] = Data.getID().split(":");
            TimeTV.setId(Integer.parseInt(DataID_details[0]) + Integer.parseInt(DataID_details[1] + DataID_details[2]) + 1);
        } else {
            TimeTV.setId(Integer.parseInt(Data.getID()) + 1 + Integer.parseInt(Data.getID()));
        }

        SubVerticallyLayout.addView(TimeTV);

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

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVerticalLinearLayout.removeView(HlinearLayout);
                mVerticalLinearLayout.removeView(line);
                mVerticalLinearLayout.removeView(space_above);
                mVerticalLinearLayout.removeView(space_below);

                // TODO: 22-Apr-16 :delete from arraylist using ID
                if (mAddNewMode_Or_EditMode) {/* True=Edit Mode ,  False=Add New Mode */
                    mDeletedTimesDataList.add(Data);
                    mTimesDataList.remove(Data);
                } else {
                    mTimesDataList.remove(Data);
                }
            }
        });

        TypedValue outValue = new TypedValue();
        this.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        SubVerticallyLayout.setBackgroundResource(outValue.resourceId);
        SubVerticallyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNew_OR_Edit_Time(true, Data.getID());//true is edit Mode
            }
        });
    }

    private void Edit_the_Current_Class_Data() {
        if (!Check_If_Some_Field_Is_Empty_Or_WrongWritten()) { //note, MSGs of this check is shown in the method: Check_If_Some_Field_Is_Empty_Or_WrongWritten
            if (!mTimesDataList.isEmpty()) {

                //Step 1 : Saving the Class Data
                mClassData.setClassName(mClassNameTV.getText().toString());
                mClassData.setModule(mClassModuleTV.getText().toString());
                mClassData.setPlace(mClassPlaceTV.getText().toString());
                mClassData.setInstructorName(mClassInstructorNameTV.getText().toString());
                mClassData.setTermID(mSelectedTermID);
                //Class's color is putted in the spinner

                if (!Times_are_wrong()) {
                    Update_Class(mClassData);
                    Update_times_of_this_Class(mClassData.getID());
                    Intent resIntent = new Intent();
                    resIntent.putExtra("deleted_or_not",false);
                    setResult(Activity.RESULT_OK, resIntent);
                    this.finish();
                    Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                } else {
                    Utilities.Show_Error_MSG("Error: One or more of the selected times is not Available during Selected term", mErrorTV);
                    Toast.makeText(this, "Nothing Updated", Toast.LENGTH_SHORT).show();
                }

            } else {
                Utilities.Show_Error_MSG("Error: You have'nt assigned any times for this Class.", mErrorTV);
            }
        }
    }

    private void Update_times_of_this_Class(String id) {

        //step 1 : adding new times or updating the updated times
        if (mTimesDataList != null && !mTimesDataList.isEmpty()) {
            for (timeData data : mTimesDataList) {
                int Duration = Utilities.Calculate_Duration(data.getStartTime(), data.getEndTime());
                data.setDuration(Duration + "");
                data.setTermID(mSelectedTermID);
                data.setClassID(mClassData.getID());

                if (data.getID().length() > 4) {
                    mdbHelper.addTime(data);
                } else {
                    mdbHelper.UpdateTime(data);
                }
            }
        }

        //step 2 : Deleting deleted times
        if (mDeletedTimesDataList != null && !mDeletedTimesDataList.isEmpty()) {
            for (timeData data : mDeletedTimesDataList) {
                mdbHelper.deleteTime(data.getID());
            }
        }
    }

    private void Update_Class(classData mClassData) {
        mdbHelper.UpdateClass(mClassData);
    }

    private boolean Times_are_wrong() {
        for (timeData data : mTimesDataList) {
            if (Check_Current_chosen_Times_if_Not_Available(data)) {
                return true;
            }
        }
        return false;
    }

    private boolean Check_Current_chosen_Times_if_Not_Available(timeData mTimeData) {
        ArrayList<timeData> TimesDataList = mdbHelper.getAllTimes_ForSomeTerm(mSelectedTermID);

        //step 1: removing the current being edited time from comparing
        if (mAddNewMode_Or_EditMode && mTimeData!=null) { /* True == Edit Mode ,  False == AddNew Mode*/
            for (timeData data : TimesDataList) {
                if (data.getID().equals(mTimeData.getID())) {
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
                    if(Data.getDay().equals(mTimeData.getDay())) {
                        String Data_startTime_details[]=Data.getStartTime().split(":");
                        int [] res  = Utilities.get_EndTime_Using_StartTime(Integer.parseInt(Data_startTime_details[0]),Integer.parseInt(Data_startTime_details[1]),Integer.parseInt(Data.getDuration()));
                        String Data_EndTime = res[0]+":"+res[1];
                        Data.setEndTime(Data_EndTime);
                        if (Utilities.compare_two_classTimesData_if_Equal_or_Overlapped(mTimeData, Data))
                            return true;//Current time is Already chosen before
                    }
            }
        }


        return false;
    }

    private void Save_the_Current_Class_Data() {
        if (!Times_are_wrong()) {
            if (!Check_If_Some_Field_Is_Empty_Or_WrongWritten()) {
                if (!mTimesDataList.isEmpty()) {

                    //Step 1 : Saving the Class Data
                    mClassData.setClassName(mClassNameTV.getText().toString());
                    mClassData.setModule(mClassModuleTV.getText().toString());
                    mClassData.setPlace(mClassPlaceTV.getText().toString());
                    mClassData.setInstructorName(mClassInstructorNameTV.getText().toString());
                    mClassData.setTermID(mSelectedTermID);
                    //Class's color is putted in the spinner
                    mdbHelper.addClass(mClassData);

                    //Step 2 : Saving the Class Times
                    for (timeData Data : mTimesDataList) {
                        timeData class_Times_Data = new timeData();
                        class_Times_Data.setDay(Data.getDay());
                        class_Times_Data.setStartTime(Data.getStartTime());
                        int Duration = Utilities.Calculate_Duration(Data.getStartTime(), Data.getEndTime());
                        class_Times_Data.setDuration(Duration + "");
                        class_Times_Data.setTermID(mSelectedTermID);
                        class_Times_Data.setClassID(mdbHelper.getLastInsertedRowID("classes") + "");
                        mdbHelper.addTime(class_Times_Data);
                    }

                    //step 3
                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                    this.finish();
                } else {
                    Utilities.Show_Error_MSG("Error: You have'nt assigned any times for this Class.", mErrorTV);
                }
            } else {
//                Toast.makeText(this, "Nothing Saved", Toast.LENGTH_SHORT).show();
            }
        } else {
            Utilities.Show_Error_MSG("Error: One or more of the selected times is not Available during Selected term", mErrorTV);
        }
    }

    private boolean Check_If_Some_Field_Is_Empty_Or_WrongWritten() {
        if (mClassNameTV.getText().toString().equals("")) {
            Utilities.Show_Error_MSG("You have't Entered a Class Name !", mErrorTV);
            return true;
        } else if (mClassModuleTV.getText().toString().equals("")) {
            Utilities.Show_Error_MSG("Please fill the Class Module field.", mErrorTV);
            return true;
        } else if (mClassPlaceTV.getText().toString().equals("")) {
            Utilities.Show_Error_MSG("Could you please Check the Place field again.", mErrorTV);
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_CANCELED) {

        } else if (resultCode == Activity.RESULT_OK) {
            //step 1
            timeData Res_time = data.getParcelableExtra("ResultedTime");

            boolean Came_from_EditMode_OR_AddNewMode = data.getBooleanExtra("Edit_or_AddNew", false);//false is AddNewMode
            if (Came_from_EditMode_OR_AddNewMode) { // true is EditMode
                //Step 1 : Editing
                for (timeData time : mTimesDataList) {
                    if (time.getID().equals(Res_time.getID())) {
                        int position_of_the_edited_term = mTimesDataList.indexOf(time);
                        mTimesDataList.set(position_of_the_edited_term, Res_time);
                    }
                }


                //Step 2 : Showing edited Data
                String[] mStartTime_parts = Res_time.getStartTime().split(":");
                String[] mEndTime_parts = Res_time.getEndTime().split(":");

                String[] res_StartTimeTV = Utilities.get_Time_Correct_Style(Integer.parseInt(mStartTime_parts[0]), Integer.parseInt(mStartTime_parts[1]));
                String StartTimeTV_hour_Correct_Style_as_String = res_StartTimeTV[0];
                String StartTimeTV_Min_Correct_Style_as_String = res_StartTimeTV[1];
                String StartTimeTV_Correct_Style_AM_or_PM = res_StartTimeTV[2];

                String[] res_mEndTime = Utilities.get_Time_Correct_Style(Integer.parseInt(mEndTime_parts[0]), Integer.parseInt(mEndTime_parts[1]));
                String mEndTime_hour_Correct_Style_as_String = res_mEndTime[0];
                String mEndTime_Min_Correct_Style_as_String = res_mEndTime[1];
                String mEndTime_Correct_Style_AM_or_PM = res_mEndTime[2];


                if (Res_time.getID().length() > 4) {
                    String mTimeDataID_details[] = Res_time.getID().split(":");
                    TextView TimeTV = (TextView) findViewById(Integer.parseInt(mTimeDataID_details[0]) + Integer.parseInt(mTimeDataID_details[1] + mTimeDataID_details[2]) + 1);
                    if (StartTimeTV_Correct_Style_AM_or_PM.equals(mEndTime_Correct_Style_AM_or_PM)) {
                        TimeTV.setText(StartTimeTV_hour_Correct_Style_as_String + ":" + StartTimeTV_Min_Correct_Style_as_String + " - " +
                                mEndTime_hour_Correct_Style_as_String + ":" + mEndTime_Min_Correct_Style_as_String + " " + StartTimeTV_Correct_Style_AM_or_PM);
                    } else {
                        TimeTV.setText(StartTimeTV_hour_Correct_Style_as_String + ":" + StartTimeTV_Min_Correct_Style_as_String + " " + StartTimeTV_Correct_Style_AM_or_PM +
                                " - " + mEndTime_hour_Correct_Style_as_String + ":" + mEndTime_Min_Correct_Style_as_String + " " + mEndTime_Correct_Style_AM_or_PM);
                    }

                    TextView DayTV = (TextView) findViewById(Integer.parseInt(mTimeDataID_details[0]) + Integer.parseInt(mTimeDataID_details[1] + mTimeDataID_details[2]) + 2);
                    DayTV.setText(Res_time.getDay());

                } else {
                    TextView TimeTV = (TextView) findViewById(Integer.parseInt(Res_time.getID()) + 1 + Integer.parseInt(Res_time.getID()));
                    if (StartTimeTV_Correct_Style_AM_or_PM.equals(mEndTime_Correct_Style_AM_or_PM)) {
                        TimeTV.setText(StartTimeTV_hour_Correct_Style_as_String + ":" + StartTimeTV_Min_Correct_Style_as_String + " - " +
                                mEndTime_hour_Correct_Style_as_String + ":" + mEndTime_Min_Correct_Style_as_String + " " + StartTimeTV_Correct_Style_AM_or_PM);
                    } else {
                        TimeTV.setText(StartTimeTV_hour_Correct_Style_as_String + ":" + StartTimeTV_Min_Correct_Style_as_String + " " + StartTimeTV_Correct_Style_AM_or_PM +
                                " - " + mEndTime_hour_Correct_Style_as_String + ":" + mEndTime_Min_Correct_Style_as_String + " " + mEndTime_Correct_Style_AM_or_PM);
                    }

                    TextView DayTV = (TextView) findViewById(Integer.parseInt(Res_time.getID()) + 2 + Integer.parseInt(Res_time.getID()));
                    DayTV.setText(Res_time.getDay());
                }

            } else {
                //step 1: Save Returned Data to use it on Save_the_Current_Class_Data +++OR+++ Edit_the_Current_Class_Data
                mTimesDataList.add(Res_time);

                //step 2 : Showing Data
                Create_Times_Views(Res_time);
            }
        }
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
                Save_the_Current_Class_Data();
            } else {
                Edit_the_Current_Class_Data();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void ShowExitDialog() {
        String DialogTitle = "", DialogMSG = "", DialogNO = "";
        if (mAddNewMode_Or_EditMode) {//true == EditMode
            DialogTitle = "Canceling " + mClassData.getName() + " Editing process";
            DialogMSG = "Are you sure you want to Cancel Editing " + mClassData.getName() + " ?";
        } else {
            DialogTitle = "Canceling adding a new Class process";
            DialogMSG = "Are you sure you want to Cancel Adding this new Class?";
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

    @Override
    public void onBackPressed() {
        ShowExitDialog();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("mTimesDataList", mTimesDataList);
        outState.putParcelableArrayList("mDeletedTimesDataList", mDeletedTimesDataList);

        outState.putString("mSelectedTermID", mSelectedTermID);
        outState.putString("mSelectedColor", mSelectedColor);
        outState.putString("mSelectedYearID", mSelectedYearID);
        outState.putString("YearID", YearID);
        outState.putString("CurrentClassID", CurrentClassID);
        outState.putString("TermID", TermID);
        outState.putInt("Choose_Term_Spinner_currentSelectedItemPosition", Choose_Term_Spinner_currentSelectedItemPosition);
        outState.putInt("Choose_Year_Spinner_currentSelectedItemPosition", Choose_Year_Spinner_currentSelectedItemPosition);
        outState.putInt("Colors_Spinner_currentSelectedItemPosition", Colors_Spinner_currentSelectedItemPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mTimesDataList = savedInstanceState.getParcelableArrayList("mTimesDataList");
        mDeletedTimesDataList = savedInstanceState.getParcelableArrayList("mDeletedTimesDataList");

        mSelectedColor = savedInstanceState.getString("mSelectedColor");
        mSelectedTermID = savedInstanceState.getString("mSelectedTermID");
        mSelectedYearID = savedInstanceState.getString("mSelectedYearID");
        Choose_Term_Spinner_currentSelectedItemPosition = savedInstanceState.getInt("Choose_Term_Spinner_currentSelectedItemPosition");
        Choose_Year_Spinner_currentSelectedItemPosition = savedInstanceState.getInt("Choose_Year_Spinner_currentSelectedItemPosition");
        Colors_Spinner_currentSelectedItemPosition = savedInstanceState.getInt("Colors_Spinner_currentSelectedItemPosition");
    }
}
