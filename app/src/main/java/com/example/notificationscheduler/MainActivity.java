package com.example.notificationscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;

import com.example.notificationscheduler.databinding.ActivityMainBinding;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textview.MaterialTextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SwitchMaterial switchIdle = binding.switchIdle;
        SwitchMaterial switchCharging = binding.switchCharging;

        switchIdle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) switchIdle.setText(R.string.active);
                else switchIdle.setText(R.string.idle);
            }
        });

        switchCharging.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) switchCharging.setText(R.string.charging);
                else switchCharging.setText(R.string.not_charging);
            }
        });
    }
}