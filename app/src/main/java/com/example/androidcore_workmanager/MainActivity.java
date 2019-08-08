package com.example.androidcore_workmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String SENDER_KEY = "SENDER_KEY";
    Button button;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);

        // For sending data to doWork() method.
        Data data = new Data.Builder()
                .putString(SENDER_KEY, "This msg is send from MainActivity")
                .build();

        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build();

        // Using OneTimeWorkRequest, our work can only be executed once.
        final OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyWorkerClass.class)
                .setInputData(data)
                .setConstraints(constraints)
                .build();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkManager.getInstance(getApplicationContext()).enqueue(workRequest);
            }
        });

        //An observer which will trigger every time WorkInfo object is changed.
        WorkManager.getInstance(getApplicationContext()).getWorkInfoByIdLiveData(workRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null){
                            String status = workInfo.getState().name();
                            textView.append(status + "\n");

                            if (workInfo.getState().isFinished()){
                                Data data1 = workInfo.getOutputData();
                                String msg = data1.getString(MyWorkerClass.RECEIVER_KEY);
                                textView.append(msg);
                            }
                        }
                    }
                });
    }

}
