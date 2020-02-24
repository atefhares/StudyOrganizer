package com.example.atefhares.study_organizer.Schedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atefhares.study_organizer.DataClasses.classData;
import com.example.atefhares.study_organizer.DataClasses.yearData;
import com.example.atefhares.study_organizer.DatabaseRelated.DBHelper;
import com.example.atefhares.study_organizer.NewOrEditActivities.AddOrEditClassActivity;
import com.example.atefhares.study_organizer.R;

import java.util.ArrayList;

/**
 * Created by Atef Hares on 20-Apr-16.
 */

public class ClassesFragment extends android.app.Fragment {
    private DBHelper mDbHelper;

    private ListView mListView;
    private TextView NoClassesTV;

    private String CurrentSelectedYearID="",CurrentSelectedTermID="";

    public  ClassesListViewAdapter classesListViewAdapter;

    public ClassesFragment() {
        // Required empty public constructor
    }

//    private static ClassesFragment sInstance;
//    public static synchronized ClassesFragment getInstance() {
//
//        // Use the application context, which will ensure that you
//        // don't accidentally leak an Activity's context.
//        // See this article for more information: http://bit.ly/6LRzfx
//        if (sInstance == null) {
//            sInstance = new ClassesFragment();
//        }
//        return sInstance;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_classes, container, false);
        mDbHelper = new DBHelper(getActivity());

        NoClassesTV = (TextView) rootView.findViewById(R.id.NoClassesTV);
        mListView = (ListView) rootView.findViewById(R.id.ClassesFragmentListView);


        Bundle bundle = getArguments();
        if(bundle!=null){
            CurrentSelectedTermID = bundle.getString("CurrentSelectedTermID");
            CurrentSelectedYearID = bundle.getString("CurrentSelectedYearID");
        }

        UpdateClassesList();

        FloatingActionButton floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<yearData> tempYears = mDbHelper.getAllYears();
                if (tempYears != null && !tempYears.isEmpty()) {
                    Intent intent = new Intent(getActivity(), AddOrEditClassActivity.class);
                    intent.putExtra("Edit",false);
                    startActivityForResult(intent, 1);
                } else {
                    Toast.makeText(getActivity(), "Please Add New Year/Term first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                UpdateClassesList();
                Intent intent = new Intent();
                intent.putExtra("ClassesFragment","ClassesFragment");
                getActivity().sendBroadcast(intent);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();


        /******************======= Choosing the Calendar Item from Navigation Menu=====*********/
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_Schedule);//set menu selection to the first item
        /****************************************************************************************/
    }

    public void UpdateClassesList() {
        if(!CurrentSelectedTermID.equals("")) {
            ArrayList<classData> Classes = mDbHelper.getAllClasses(CurrentSelectedTermID);
            classesListViewAdapter = new ClassesListViewAdapter(this, Classes);
            if (!Classes.isEmpty()) {
                NoClassesTV.setVisibility(View.GONE);
                mListView.setAdapter(classesListViewAdapter);
            } else {
                mListView.setAdapter(classesListViewAdapter);
                NoClassesTV.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
