//package com.example.atefhares.study_organizer.notifications_related;
//
//import android.app.IntentService;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.util.Log;
//
///**
// * Created by Atef Hares on 26-Jun-16.
// */
//
//public class MyService extends IntentService {
//
//    public MyService() {
//        super("MyServiceName");
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        Log.d("MyService", "About to execute MyTask");
//        new MyTask().execute();
//    }
//
//    private class MyTask extends AsyncTask<String, Void, Boolean> {
//        @Override
//        protected Boolean doInBackground(String... strings) {
//            Log.d("MyService - MyTask", "Calling doInBackground within MyTask");
//            return false;
//        }
//    }
//}
