package com.example.atefhares.study_organizer.MainActivityRelated;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.atefhares.study_organizer.Calendar.CalendarHoldFragment;
import com.example.atefhares.study_organizer.DashboardRelated.DashboardFragment;
import com.example.atefhares.study_organizer.DataClasses.termData;
import com.example.atefhares.study_organizer.DataClasses.timeData;
import com.example.atefhares.study_organizer.DatabaseRelated.DBHelper;
import com.example.atefhares.study_organizer.R;
import com.example.atefhares.study_organizer.Schedule.ScheduleFragment;

import java.util.ArrayList;

//import com.example.atefhares.study_organizer.notifications_related.MyService;

/**
 * Created by Atef Hares on 20-Apr-16.
 */

public class StartActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static Fragment active;
    public static ArrayList<String> mPreviousFragmentsNames = new ArrayList<String>();
//    private  boolean Removed_Last_Fragment_Name_From_backList = false;

    DBHelper mdbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        new Instabug.Builder(getApplication(), "7b9b99cfaed33db5067f2913519f682f")
//                .setInvocationEvent(IBGInvocationEvent.IBGInvocationEventShake)
//                .build();

        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mdbHelper = DBHelper.getInstance(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


//        int width = getResources().getDisplayMetrics().widthPixels/1;
//        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) navigationView.getLayoutParams();
//        params.width = width;
//        navigationView.setLayoutParams(params);

        navigationView.setNavigationItemSelectedListener(this);


        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
            DashboardFragment dashboardFragment = new DashboardFragment();
            active = dashboardFragment;
            fragmentTransaction
                    .replace(R.id.RR, dashboardFragment, "nav_dashboard")
//                    .addToBackStack(dashboardFragment.getTag())
                    .commit();
            mPreviousFragmentsNames.add("nav_dashboard");
        }
    }

//        if (!isMyServiceRunning()){
//            Intent serviceIntent = new Intent(this,MyService.class);
////            Intent serviceIntent = new Intent("com.example.atefhares.study_organizer.notifications_related.MyService");
//            this.startService(serviceIntent);
//        }
//
//    }
//    private boolean isMyServiceRunning() {
//        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (MyService.class.getName().equals(service.service.getClassName())) {
//                return true;
//            }
//        }
//        return false;
//    }


    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }


//        if(!Removed_Last_Fragment_Name_From_backList){
////            mPreviousFragmentsNames.remove(mPreviousFragmentsNames.size()-1);
//            Removed_Last_Fragment_Name_From_backList = true;
//        }


        if (mPreviousFragmentsNames.size() > 1) {
            String Previous_fragment_name = mPreviousFragmentsNames.get(mPreviousFragmentsNames.size() - 2);

            switch (Previous_fragment_name) {
                case "nav_calendar":
                    if (active.getClass().getSimpleName().equals("CalendarHoldFragment")) {
                    } else {
                        CalendarHoldFragment calendarHoldFragment = new CalendarHoldFragment();
                        getFragmentManager().beginTransaction().replace(R.id.RR, calendarHoldFragment, "nav_calendar").commit();
                        active = calendarHoldFragment;
                    }
                    break;
                case "nav_Schedule":
                    if (active.getClass().getSimpleName().equals("ScheduleFragment")) {
                    } else {
                        ScheduleFragment scheduleFragment = new ScheduleFragment();
                        getFragmentManager().beginTransaction().replace(R.id.RR, scheduleFragment, "nav_Schedule").commit();
                        active = scheduleFragment;
                    }
                    break;
                case "nav_dashboard":
                    if (active.getClass().getSimpleName().equals("DashboardFragment")) {
                    } else {
                        DashboardFragment dashboardFragment = new DashboardFragment();
                        getFragmentManager().beginTransaction().replace(R.id.RR, dashboardFragment, "nav_dashboard").commit();
                        active = dashboardFragment;
                    }
                    break;
            }
            mPreviousFragmentsNames.remove(mPreviousFragmentsNames.size() - 1);
        } else {
            super.onBackPressed();
        }
//        if(getFragmentManager().getBackStackEntryCount() > 1) {
//            FragmentManager.BackStackEntry backEntry = getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 2);
//            String str = backEntry.getName();
//            active = getFragmentManager().findFragmentByTag(str);
//            if(! str.equals("nav_Schedule")){
//                getFragmentManager().popBackStack();
//            }else {
//                ScheduleFragment scheduleFragment = new ScheduleFragment();
//                active = scheduleFragment; // the magic is here, everytime you add a new fragment, keep the reference to it to know what fragment is being shown
//                getFragmentManager().beginTransaction()
//                        .replace(R.id.RR, scheduleFragment, "nav_Schedule")
//                        .addToBackStack(scheduleFragment.getTag())
//                        .commit();
//                getFragmentManager().popBackStack();
//            }
//        }
//        else
//            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.start, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            if (active.getClass().getSimpleName().equals("DashboardFragment")) {
                // then this fragment is already being shown, therefore do nothing
            } else {
                DashboardFragment dashboardFragment = new DashboardFragment();
                active = dashboardFragment; // the magic is here, everytime you add a new fragment, keep the reference to it to know what fragment is being shown
                fragmentTransaction
                        .replace(R.id.RR, dashboardFragment, "nav_dashboard")
//                        .addToBackStack(dashboardFragment.getTag())
                        .commit();

                mPreviousFragmentsNames.add("nav_dashboard");
            }
        } else if (id == R.id.nav_calendar) {
            if (active.getClass().getSimpleName().equals("CalendarHoldFragment")) {
                // then this fragment is already being shown, therefore do nothing
            } else {
                CalendarHoldFragment calendarHoldFragment = new CalendarHoldFragment();
                active = calendarHoldFragment; // the magic is here, everytime you add a new fragment, keep the reference to it to know what fragment is being shown
                fragmentTransaction
                        .replace(R.id.RR, calendarHoldFragment, "nav_calendar")
//                        .addToBackStack(calendarHoldFragment.getTag())
                        .commit();

                mPreviousFragmentsNames.add("nav_calendar");
//                final SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(R.string.SharedPreferencesName), MODE_PRIVATE);
//                sharedPreferences.edit().putInt("LastScrollY", 8).apply();

            }
        } else if (id == R.id.nav_Schedule) {
            if (active.getClass().getSimpleName().equals("ScheduleFragment")) {
                // then this fragment is already being shown, therefore do nothing
            } else {
                ScheduleFragment scheduleFragment = new ScheduleFragment();
                active = scheduleFragment; // the magic is here, everytime you add a new fragment, keep the reference to it to know what fragment is being shown
                fragmentTransaction
                        .replace(R.id.RR, scheduleFragment, "nav_Schedule")
//                        .addToBackStack(scheduleFragment.getTag())
                        .commit();
                mPreviousFragmentsNames.add("nav_Schedule");
            }
        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_send_reports) {
//            Instabug.invoke(IBGInvocationMode.IBGInvocationModeFeedbackSender);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
        This Is used when editing a class_logo from the calendar and after done then when back button is called
        (by user or programmatically when class_logo is deleted) then in both
        cases we go to ScheduleFragment instead of calendar fragment
        */
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                boolean EditedOrNot = data.getBooleanExtra("EditedOrNot", false);
                if (EditedOrNot) {
                    if (active.getClass().getSimpleName().equals("ScheduleFragment")) {
                        // then this fragment is already being shown, therefore do nothing
                    } else {
                        ScheduleFragment scheduleFragment = new ScheduleFragment();
                        getFragmentManager().beginTransaction().replace(R.id.RR, scheduleFragment, "nav_Schedule").commit();
//                        mPreviousFragmentsNames.add("nav_Schedule");
                        active = scheduleFragment; // the magic is here, everytime you add a new fragment, keep the reference to it to know what fragment is being shown
                    }
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            outState.putString("ActionBarTitle", this.getSupportActionBar().getTitle().toString());
        } catch (Exception e) {
        }

        outState.putString("CurrentActiveFragmentTag", active.getTag());

    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<termData> Terms = mdbHelper.getAllTerms();
        ArrayList<timeData> Times = new ArrayList<>();
        for (termData term : Terms) {
            ArrayList<timeData> times = mdbHelper.getAllTimes_ForSomeTerm(term.getID());
            Times.addAll(times);
        }


        SetClassesAlarms(Times);


    }

    private void SetClassesAlarms(ArrayList<timeData> times) {
//        for (timeData time : times) {
//
//            classData Class = mdbHelper.getClass(time.getClassID());
//
//            /*********************************************************************************/
//            AlarmReceiver receiver = new AlarmReceiver();
//            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//            IntentFilter filter = new IntentFilter("ALARM_ACTION");
//            registerReceiver(receiver, filter);
//            Intent intent = new Intent("ALARM_ACTION");
//            intent.putExtra("ClassID",  Class.getID());


//        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
//        notificationIntent.addCategory("android.intent.category.DEFAULT");
//        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//        alarm.set(AlarmManager.RTC_WAKEUP, (System.currentTimeMillis() + (5 * 1000)), broadcast);


//
////            notificationIntent.putExtra("ClassID", Class.getID());
//
//            Calendar cal = Calendar.getInstance();
//            String time_detaiis[] = time.getStartTime().split(":");
//            int day = 0;
//            switch (time.getDay()) {
//                case "Saturday":
//                    day = Calendar.SATURDAY;
//                    break;
//                case "Sunday":
//                    day = Calendar.SUNDAY;
//                    break;
//                case "Monday":
//                    day = Calendar.MONDAY;
//                    break;
//                case "Tuesday":
//                    day = Calendar.TUESDAY;
//                    break;
//                case "Wednesday":
//                    day = Calendar.WEDNESDAY;
//                    break;
//                case "Thursday":
//                    day = Calendar.THURSDAY;
//                    break;
//                case "Friday":
//                    day = Calendar.FRIDAY;
//                    break;
//            }
////            cal.set(Calendar.DAY_OF_WEEK, day);
//            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time_detaiis[0]));
//            cal.set(Calendar.MINUTE, Integer.parseInt(time_detaiis[1]));
////        cal.add(Calendar.SECOND, 15);
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
//        }
//
////        alarmManager.setRepeating();

//        AlarmManager alarms = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
//
//        AlarmReceiver receiver = new AlarmReceiver();
//        IntentFilter filter = new IntentFilter("ALARM_ACTION");
////        registerReceiver(receiver, filter);
//
//        Intent intent = new Intent("ALARM_ACTION");
//        intent.putExtra("param", "My scheduled action");
////        PendingIntent operation = PendingIntent.getBroadcast(this, 0, intent, 0);
//        // I choose 3s after the launch of my application
//        Calendar c = Calendar.getInstance();
////        c.set(Calendar.MONTH , 5);
////        c.set(Calendar.DAY_OF_MONTH , 27);
////        c.set(Calendar.YEAR , 2016);
////        c.set(Calendar.HOUR_OF_DAY , 3);
////        c.set(Calendar.MINUTE , 14);
////        c.set(Calendar.SECOND , 0);
//        alarms.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis()+5000, broadcast) ;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String ActionBarTitle = savedInstanceState.getString("ActionBarTitle");
        try {
            this.getSupportActionBar().setTitle(ActionBarTitle);
        } catch (Exception e) {
        }

        String CurrentActiveFragmentTag = savedInstanceState.getString("CurrentActiveFragmentTag");
        active = (Fragment) getFragmentManager().findFragmentByTag(CurrentActiveFragmentTag);
    }

}
