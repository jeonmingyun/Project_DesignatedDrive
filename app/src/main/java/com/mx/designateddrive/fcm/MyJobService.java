package com.mx.designateddrive.fcm;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;


public class MyJobService extends JobService {

    private static final String TAG = "MyJobService";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.e(TAG, "Performing long running task in scheduled job");
//        Intent starter = new Intent(getApplicationContext(), MyFirebaseInstanceIDService.class);
//        startService(starter);
        // TODO(developer): add long running task here.
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

}
