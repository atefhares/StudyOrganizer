package com.example.atefhares.study_organizer.Calendar;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;

import java.util.Locale;

/**
 * Created by Dr.h3cker on 14/03/2015.
 */
public class CalendarFragment_pager_adapter extends FragmentStatePagerAdapter {
    public CalendarFragment_pager_adapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int i) {

        CalendarFragment calendarFragment = new CalendarFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("CurrentViewPagerPosition",i);
        calendarFragment.setArguments(bundle);
        if(i==50000)//The Current Week Fragment Number
        {
            bundle.putBoolean("CurrentWeek",true);
        }
        calendarFragment.setArguments(bundle);
        return calendarFragment;
    }

    @Override
    public int getCount() {
        return 100000;
    }//set the number of tabs

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        return null;
    }

}
