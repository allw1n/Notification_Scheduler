package com.example.notificationscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.pm.ComponentInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.notificationscheduler.databinding.ActivityMainBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textview.MaterialTextView;

public class MainActivity extends AppCompatActivity {

    private RadioGroup radioGroupNetworkType;
    private int selectedNetworkOption;
    private ActivityMainBinding binding;
    private JobScheduler jobScheduler = null;
    private static final int JOB_ID = 0;
    private SwitchMaterial switchIdle, switchCharging;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        switchIdle = binding.switchIdle;
        switchCharging = binding.switchCharging;

        MaterialTextView viewSwitchIdle = binding.viewSwitchIdle;
        MaterialTextView viewSwitchCharging = binding.viewSwitchCharging;
        MaterialTextView viewSeekBarProgress = binding.viewSeekBarProgress;

        seekBar = binding.seekBar;

        MaterialButton buttonSchedule = binding.buttonSchedule;
        MaterialButton buttonCancel = binding.buttonCancel;

        radioGroupNetworkType = binding.radioGroupNetworkType;

        switchIdle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) viewSwitchIdle.setText(R.string.active);
                else viewSwitchIdle.setText(R.string.idle);
            }
        });

        switchCharging.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) viewSwitchCharging.setText(R.string.charging);
                else viewSwitchCharging.setText(R.string.not_charging);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if (progress > 0) {
                    String setProgress = progress + " s";
                    viewSeekBarProgress.setText(setProgress);
                }
                else viewSeekBarProgress.setText(getString(R.string.not_set));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        buttonSchedule.setOnClickListener(view -> {

            int selectedNetworkID = radioGroupNetworkType.getCheckedRadioButtonId();
            if (selectedNetworkID == binding.radioNone.getId()) {
                selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;
            }
            else if (selectedNetworkID == binding.radioAny.getId()) {
                selectedNetworkOption = JobInfo.NETWORK_TYPE_ANY;
            }
            else {
                selectedNetworkOption = JobInfo.NETWORK_TYPE_UNMETERED;
            }
            scheduleJob();
        });

        buttonCancel.setOnClickListener(view -> {
            cancelJobs();
        });
    }

    private void scheduleJob() {

        int seekBarValue = seekBar.getProgress();
        boolean seekBarSet = seekBarValue > 0;

        jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        ComponentName serviceName = new ComponentName(
                getPackageName(),
                NotificationJobService.class.getName());

        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceName);
        builder.setRequiredNetworkType(selectedNetworkOption)
                .setRequiresDeviceIdle(switchIdle.isChecked())
                .setRequiresCharging(switchCharging.isChecked());

        Log.d("Switch Idle", String.valueOf(switchIdle.isChecked()));
        Log.d("Switch Charging", String.valueOf(switchCharging.isChecked()));

        if (seekBarSet) builder.setOverrideDeadline(seekBarValue * 1000L);

        boolean constraintSet = (selectedNetworkOption != JobInfo.NETWORK_TYPE_NONE) ||
                switchIdle.isChecked() || switchCharging.isChecked() || seekBarSet;

        if (constraintSet) {
            JobInfo jobInfo = builder.build();
            jobScheduler.schedule(jobInfo);

            Toast.makeText(this, "Job scheduled! Job will run " +
                    "when constraints are met.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Select at least one constraint!", Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelJobs() {
        if (jobScheduler != null) {
            jobScheduler.cancelAll();
            jobScheduler = null;
            Toast.makeText(this, "Jobs cancelled!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "No existing jobs found!", Toast.LENGTH_SHORT).show();
        }
    }
}