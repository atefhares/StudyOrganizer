package com.example.atefhares.study_organizer.Schedule;


import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atefhares.study_organizer.DataClasses.examData;
import com.example.atefhares.study_organizer.DataClasses.yearData;
import com.example.atefhares.study_organizer.DatabaseRelated.DBHelper;
import com.example.atefhares.study_organizer.NewOrEditActivities.AddOrEditExamActivity;
import com.example.atefhares.study_organizer.R;

import java.util.ArrayList;

/**
 * Created by Atef Hares on 20-Apr-16.
 */

public class ExamsFragment extends Fragment {
    private DBHelper mDbHelper;
    private ListView mListView;
    private TextView NoExamsTV;

    private String CurrentSelectedYearID = "", CurrentSelectedTermID = "";

    public ExamsListViewAdapter mExamsListViewAdapter;

    BroadcastReceiver broadcastReceiver;

    public ExamsFragment() {
        // Required empty public constructor
    }

    private static ExamsFragment sInstance;

    public static synchronized ExamsFragment getInstance() {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx

        return sInstance;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_exams, container, false);
        mDbHelper = new DBHelper(getActivity());

        NoExamsTV = (TextView) rootView.findViewById(R.id.NoExamsTV);
        mListView = (ListView) rootView.findViewById(R.id.ExamsFragmetListView);


        Bundle bundle = getArguments();
        if (bundle != null) {
            CurrentSelectedTermID = bundle.getString("CurrentSelectedTermID");
            CurrentSelectedYearID = bundle.getString("CurrentSelectedYearID");
        }

        UpdateExamsList();

        FloatingActionButton floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<yearData> tempYears = mDbHelper.getAllYears();
                if (tempYears != null && !tempYears.isEmpty()) {
                    Intent intent = new Intent(getActivity(), AddOrEditExamActivity.class);
                    intent.putExtra("Edit_or_AddNew", false);
                    startActivityForResult(intent, 1);
                } else {
                    Toast.makeText(getActivity(), "Please Add New Year/Term first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                UpdateExamsList();
            }
        };

        return rootView;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                UpdateExamsList();
            }
        }
    }

    public void UpdateExamsList() {
        if (!CurrentSelectedTermID.equals("")) {
            ArrayList<examData> Exams = mDbHelper.getAllExams_forAllClasses_InSomeTerm(CurrentSelectedTermID);
            mExamsListViewAdapter = new ExamsListViewAdapter(this, Exams);
            if (!Exams.isEmpty()) {
                NoExamsTV.setVisibility(View.GONE);
                mListView.setAdapter(mExamsListViewAdapter);
            } else {
                mListView.setAdapter(mExamsListViewAdapter);
                NoExamsTV.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);

    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().registerReceiver( broadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

    }
}
