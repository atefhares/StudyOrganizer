package com.example.atefhares.study_organizer.Calendar;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.atefhares.study_organizer.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Atef Hares on 20-Apr-16.
 */

public class CalendarHoldFragment extends Fragment {
    public static ViewPager mViewPager;
    private Menu mMenu;
    private Integer mCurrentWeekFragmentItemNumber;

    private Bundle mSavedInstanceState;

    public CalendarHoldFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rooView = inflater.inflate(R.layout.fragment_calendar_hold, container, false);
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("AAAAAAAAAAAAAAAAAAAAAAAAAAAAA");//Just Sloving the problem of  truncated ActionBar Title

        /***************************************************************************************/
        mViewPager = (ViewPager) rooView.findViewById(R.id.pager);
        mViewPager.setAdapter(new CalendarFragment_pager_adapter(getChildFragmentManager()));
        mViewPager.setCurrentItem(100000 / 2);
        mCurrentWeekFragmentItemNumber = mViewPager.getCurrentItem();
//        SetCalendarTitle(0);
        /*************************************************************************************/



        if (savedInstanceState != null)
            mSavedInstanceState = savedInstanceState;
        else {
            SetCalendarTitle(0);
        }
        return rooView;
    }

    private void SetCalendarTitle(int Indicator) {
        Calendar c = Calendar.getInstance();
        /**************************************************************************************************************************/
        c.add(Calendar.DATE, Indicator * 7);
        /*************************************************======= Generating the Title =====*********************************************/
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(" MMM");
        int CurrentDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int FirstDayOfWeek = 0, LastDayOfWeek = 0, MonthOfTheFirstDayOfWeek = 0, MonthOfTheLastDayOfWeek = 0, YearOfTheFirstDayOfWeek = 0, YearOfTheLastDayOfWeek = 0;
        String MonthOfTheFirstDayOfWeek_As_String = "", MonthOfTheLastDayOfWeek_As_String = "", title = "" ;

        switch (CurrentDayOfWeek) {
            case Calendar.SATURDAY:
                c.add(Calendar.DATE, -0);
                FirstDayOfWeek = c.get(Calendar.DAY_OF_MONTH);
                MonthOfTheFirstDayOfWeek = c.get(Calendar.MONTH);
                MonthOfTheFirstDayOfWeek_As_String = simpleDateFormat.format(c.getTime());
                YearOfTheFirstDayOfWeek = c.get(Calendar.YEAR);
                break;
            case Calendar.SUNDAY:
                c.add(Calendar.DATE, -1);
                FirstDayOfWeek = c.get(Calendar.DAY_OF_MONTH);
                MonthOfTheFirstDayOfWeek = c.get(Calendar.MONTH);
                MonthOfTheFirstDayOfWeek_As_String = simpleDateFormat.format(c.getTime());
                YearOfTheFirstDayOfWeek = c.get(Calendar.YEAR);
                break;
            case Calendar.MONDAY:
                c.add(Calendar.DATE, -2);
                FirstDayOfWeek = c.get(Calendar.DAY_OF_MONTH);
                MonthOfTheFirstDayOfWeek = c.get(Calendar.MONTH);
                MonthOfTheFirstDayOfWeek_As_String = simpleDateFormat.format(c.getTime());
                YearOfTheFirstDayOfWeek = c.get(Calendar.YEAR);
                break;
            case Calendar.TUESDAY:
                c.add(Calendar.DATE, -3);
                FirstDayOfWeek = c.get(Calendar.DAY_OF_MONTH);
                MonthOfTheFirstDayOfWeek = c.get(Calendar.MONTH);
                MonthOfTheFirstDayOfWeek_As_String = simpleDateFormat.format(c.getTime());
                YearOfTheFirstDayOfWeek = c.get(Calendar.YEAR);
                break;
            case Calendar.WEDNESDAY:
                c.add(Calendar.DATE, -4);
                FirstDayOfWeek = c.get(Calendar.DAY_OF_MONTH);
                MonthOfTheFirstDayOfWeek = c.get(Calendar.MONTH);
                MonthOfTheFirstDayOfWeek_As_String = simpleDateFormat.format(c.getTime());
                YearOfTheFirstDayOfWeek = c.get(Calendar.YEAR);
                break;
            case Calendar.THURSDAY:
                c.add(Calendar.DATE, -5);
                FirstDayOfWeek = c.get(Calendar.DAY_OF_MONTH);
                MonthOfTheFirstDayOfWeek = c.get(Calendar.MONTH);
                MonthOfTheFirstDayOfWeek_As_String = simpleDateFormat.format(c.getTime());
                YearOfTheFirstDayOfWeek = c.get(Calendar.YEAR);
                break;
            case Calendar.FRIDAY:
                c.add(Calendar.DATE, -6);
                FirstDayOfWeek = c.get(Calendar.DAY_OF_MONTH);
                MonthOfTheFirstDayOfWeek = c.get(Calendar.MONTH);
                MonthOfTheFirstDayOfWeek_As_String = simpleDateFormat.format(c.getTime());
                YearOfTheFirstDayOfWeek = c.get(Calendar.YEAR);
                break;
        }


        c.add(Calendar.DATE, 6);
        LastDayOfWeek = c.get(Calendar.DAY_OF_MONTH);
        MonthOfTheLastDayOfWeek = c.get(Calendar.MONTH);
        MonthOfTheLastDayOfWeek_As_String = simpleDateFormat.format(c.getTime());
        YearOfTheLastDayOfWeek = c.get(Calendar.YEAR);

        if(YearOfTheFirstDayOfWeek == YearOfTheLastDayOfWeek) {
            if (MonthOfTheFirstDayOfWeek == MonthOfTheLastDayOfWeek) {
                title = FirstDayOfWeek + " - " + LastDayOfWeek + MonthOfTheFirstDayOfWeek_As_String + ", " + YearOfTheLastDayOfWeek;
            } else {
                title = FirstDayOfWeek + " " + MonthOfTheFirstDayOfWeek_As_String + " - " + LastDayOfWeek + " " + MonthOfTheLastDayOfWeek_As_String + ", " + YearOfTheLastDayOfWeek;
            }
        }else{
                title = FirstDayOfWeek +" " +MonthOfTheFirstDayOfWeek_As_String + ", " +YearOfTheFirstDayOfWeek + " - " + LastDayOfWeek + " "+ MonthOfTheLastDayOfWeek_As_String + ", " + YearOfTheLastDayOfWeek;
        }
        /*****************************************************************************************************************************************************/
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);

    }

    public static int getPos(){
        return mViewPager.getCurrentItem();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.w("pos leaving  ",""+position);
            }

            @Override
            public void onPageSelected(int position) {
                SetCalendarTitle(position - 50000);
                if (mViewPager.getCurrentItem() == 50000) {
                    try {
                        mMenu.getItem(0).setEnabled(false);
                        mMenu.getItem(0).setIcon(R.drawable.currentweekicon1);
                    } catch (Exception e) {
                    }
                } else {
                    try {
                        mMenu.getItem(0).setEnabled(true);
                        mMenu.getItem(0).setIcon(R.drawable.currentweekicon2);
                    } catch (Exception e) {
                    }
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.calendar_fargment_holder_menu, menu);
        mMenu = menu;

        if (mViewPager.getCurrentItem() == 50000) {
            try {
                mMenu.getItem(0).setEnabled(false);
                mMenu.getItem(0).setIcon(R.drawable.currentweekicon1);
            } catch (Exception e) {
            }
        } else {
            try {
                mMenu.getItem(0).setEnabled(true);
                mMenu.getItem(0).setIcon(R.drawable.currentweekicon2);
            } catch (Exception e) {
            }
        }
        /**************************************************************************************/
        if (mSavedInstanceState != null) {
            boolean EnabledOrNot = mSavedInstanceState.getBoolean("mMenu.getItem(0).isEnabled");
            mMenu.getItem(0).setEnabled(EnabledOrNot);
            if (EnabledOrNot) {
                mMenu.getItem(0).setIcon(R.drawable.currentweekicon2);
            } else {
                mMenu.getItem(0).setIcon(R.drawable.currentweekicon1);
            }
        }
        /**************************************************************************************/
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.GetCurrentWeek:
                mViewPager.setCurrentItem(mCurrentWeekFragmentItemNumber);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume() {
        super.onResume();
        /*************************[Setting Title to be Current Week]***************************/
//        SetCalendarTitle(0);
        /*************************************************************************************/

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            outState.putBoolean("mMenu.getItem(0).isEnabled", mMenu.getItem(0).isEnabled());
        } catch (Exception e) {
        }
    }
}