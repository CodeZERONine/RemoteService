package com.akshanshgusain.serviceonly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mStartService, mStopService;
    private Intent mServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStartService = findViewById(R.id.button_start_service);
        mStopService = findViewById(R.id.button_stop_service);
        mServiceIntent = new Intent(this, MyService.class);

        mStartService.setOnClickListener(this);
        mStopService.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.button_start_service:
                startService(mServiceIntent);
                break;
            case R.id.button_stop_service:
                stopService(mServiceIntent);
                break;
        }

    }
}
