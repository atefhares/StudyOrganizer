package com.example.atefhares.study_organizer.Schedule;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.atefhares.study_organizer.DataClasses.termData;
import com.example.atefhares.study_organizer.DataClasses.yearData;
import com.example.atefhares.study_organizer.DatabaseRelated.DBHelper;
import com.example.atefhares.study_organizer.NewOrEditActivities.AddOrEditYearActivity;
import com.example.atefhares.study_organizer.R;
import com.example.atefhares.study_organizer.SlidingTabesLayout.SlidingTabLayout;
import com.example.atefhares.study_organizer.UtilitiesClasses.Utilities;

import java.util.ArrayList;


/**
 * Created by Atef Hares on 20-Apr-16.
 */
public class ScheduleFragment extends Fragment {

    private LinearLayout mActionBar_Select_Year_term;
    Fragment Frag;
    String mSelectedTermID = "", mSelectedYearID = "";
    DBHelper mdbHelper;

    ViewPager mViewPager;
    SlidingTabLayout Tabs;
    ScheduleFragment_Pager_adapter scheduleFragment_pager_adapter;

//    private static ScheduleFragment sInstance;
//    public static synchronized ScheduleFragment getInstance() {
//
//        // Use the application context, which will ensure that you
//        // don't accidentally leak an Activity's context.
//        // See this article for more information: http://bit.ly/6LRzfx
//        if (sInstance == null) {
//            sInstance = new ScheduleFragment();
//        }
//        return sInstance;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        Tabs = (SlidingTabLayout) rootView.findViewById(R.id.tabs);


        mdbHelper = DBHelper.getInstance(getActivity());
        Frag = this;

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        View v = inflater.inflate(R.layout.action_bar_schedule_activity, null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(v);

        mActionBar_Select_Year_term = (LinearLayout) ((AppCompatActivity) getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.LL);
        mActionBar_Select_Year_term.setBackgroundResource(R.drawable.ripple_effect_circle);

        Set_ActionBar_Title_first_time(savedInstanceState);


        scheduleFragment_pager_adapter = new ScheduleFragment_Pager_adapter(getChildFragmentManager(), mSelectedYearID, mSelectedTermID);
        mViewPager.setAdapter(scheduleFragment_pager_adapter);
        Tabs.setViewPager(mViewPager);

        Set_Filter_By_ActionBar_Event();
        if(getArguments()!=null) {
            Bundle b = getArguments();
            if (b.getBoolean("CameFromDashBorad")){
                mViewPager.setCurrentItem(1);
            }
        }

        return rootView;
    }

    private void Set_ActionBar_Title_first_time(Bundle savedInstanceState) {
        if(savedInstanceState == null ) {
            ArrayList<yearData> yearData_ArrayList = mdbHelper.getAllYears();

            //Step 1
            if (yearData_ArrayList != null && !yearData_ArrayList.isEmpty()) {
                for (yearData year : yearData_ArrayList) {
                    ArrayList<termData> Year_terms_ArrayList = mdbHelper.getAllTerms(year);
                    if (Utilities.year_is_Current_year(year)) {
                        mSelectedYearID = year.getID();
                        for (termData term : Year_terms_ArrayList) {
                            if (Utilities.this_term_is_the_current_term(term)) {
                                mSelectedTermID = term.getID();
                                break;
                            }
                        }
                        break;
                    }
                }


                //Step 2
                if (mSelectedTermID.equals("")) {
                    if (!yearData_ArrayList.isEmpty())
                        mSelectedYearID = yearData_ArrayList.get(0).getID();

                    ArrayList<termData> Year_terms_ArrayList = mdbHelper.getAllTerms(yearData_ArrayList.get(0));

                    if (!Year_terms_ArrayList.isEmpty())
                        mSelectedTermID = Year_terms_ArrayList.get(0).getID();
                }
            }
        }else {
                mSelectedTermID = savedInstanceState.getString("mSelectedTermID");
                mSelectedYearID = savedInstanceState.getString("mSelectedYearID");
        }

        //Step 3
        Update_Actionbar_YearTerm_Title(mActionBar_Select_Year_term, mSelectedYearID, mSelectedTermID);
    }

    private void Update_Actionbar_YearTerm_Title(LinearLayout mActionBar_select_year_term, String mSelectedYearID, String mSelectedTermID) {
        TextView Year_Term = (TextView) mActionBar_select_year_term.findViewById(R.id.Year_Term_TV);
        if (!mSelectedYearID.equals("") && !mSelectedTermID.equals("")) {
            String yearName = "", termName = "";
            yearData year = mdbHelper.getYear(mSelectedYearID);
            if (year != null) {
                yearName = year.getYearName();
            }
            termData term = mdbHelper.getTerm(mSelectedTermID);
            if (term != null) {
                termName = term.getTermName();
            }
            if (yearName != null && termName != null) {
                Year_Term.setText(yearName + " | " + termName);
            } else {
                Year_Term.setText("No Selected Year/Term");
            }

        } else {
            Year_Term.setText("No Selected Year/Term");
        }

        if(scheduleFragment_pager_adapter!=null) {
            scheduleFragment_pager_adapter.setData(mSelectedYearID, mSelectedTermID);
            scheduleFragment_pager_adapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.schedule_fargment_menu, menu);
        MenuItem item = menu.getItem(0);
        if (!mSelectedYearID.equals("")) {
            String yearName = mdbHelper.getYear(mSelectedYearID).getYearName();
            if (yearName != null && !yearName.equals("")) {
                item.setEnabled(true);
                item.setTitle("Edit: " + yearName);
            } else {
                item.setEnabled(false);
            }
        } else {
            item.setEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = new Intent(getActivity(), AddOrEditYearActivity.class);
        switch (id) {
            case R.id.edit_Year:
                intent.putExtra("Edit", true);
                intent.putExtra("YearID", mSelectedYearID);
                startActivityForResult(intent, 1);
                break;
            case R.id.add_new_year:
                intent.putExtra("Edit", false);
                startActivityForResult(intent, 1);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void Set_Filter_By_ActionBar_Event() {
//        Update_Actionbar_YearTerm_Title(mActionBar_Select_Year_term,mSelectedYearID, mSelectedTermID);
        mActionBar_Select_Year_term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogFragment dialogFragment = new CustomDialogFragment();
                int REQUEST_CODE = 0;
                dialogFragment.setTargetFragment(Frag, REQUEST_CODE);
                dialogFragment.setStyle(CustomDialogFragment.STYLE_NORMAL, R.style.CustomDialog);

                Bundle bundle = new Bundle();
                bundle.putString("mSelectedYearID", mSelectedYearID);
                bundle.putString("mSelectedTermID", mSelectedTermID);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getFragmentManager(), "Dialog");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                mSelectedTermID = data.getStringExtra("mSelectedTermID");
                mSelectedYearID = data.getStringExtra("mSelectedYearID");

                Update_Actionbar_YearTerm_Title(mActionBar_Select_Year_term, mSelectedYearID, mSelectedTermID);

//                scheduleFragment_pager_adapter.setData(mSelectedYearID, mSelectedTermID);
//                scheduleFragment_pager_adapter.notifyDataSetChanged();
            }
        } else if (requestCode == 1) {
                mSelectedTermID="";
                mSelectedYearID="";
                Set_ActionBar_Title_first_time(null);
                getActivity().invalidateOptionsMenu();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("mSelectedTermID",mSelectedTermID);
        outState.putString("mSelectedYearID",mSelectedYearID);

    }
}
