package com.example.atefhares.study_organizer.Schedule;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;

/**
 * Created by Atef Hares on 20-Apr-16.
 */
public class ScheduleFragment_Pager_adapter extends FragmentStatePagerAdapter {

    private String CurrentSelectedYearID="",CurrentSelectedTermID="";

    public void setData(String SelectedYearID , String SelectedTermID){
        CurrentSelectedYearID = SelectedYearID;
        CurrentSelectedTermID = SelectedTermID;
    }
    public ScheduleFragment_Pager_adapter(FragmentManager fm , String SelectedYearID , String SelectedTermID) {
        super(fm);
        CurrentSelectedYearID = SelectedYearID;
        CurrentSelectedTermID = SelectedTermID;
    }

    @Override
    public Fragment getItem(int position) {

        Bundle bundle = new Bundle();
        bundle.putString("CurrentSelectedYearID", CurrentSelectedYearID);
        bundle.putString("CurrentSelectedTermID",CurrentSelectedTermID);

        if(position==0)
        {
            ClassesFragment classesFragment = new ClassesFragment();
            classesFragment.setArguments(bundle);
            return classesFragment;
        }
        else if(position==1)
        {
            ExamsFragment examsFragment = new ExamsFragment();
            examsFragment.setArguments(bundle);
            return examsFragment;
        }

        return null;
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Classes";
            case 1:
                return "Exams";
        }
        return null;
    }
}
