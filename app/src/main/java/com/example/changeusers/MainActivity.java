package com.example.changeusers;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity {
Button btn1,btn2;

static final int RESULT_ENABLE = 1;
DevicePolicyManager devicePolicyManager;
ComponentName componentName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = findViewById(R.id.enable);
        btn2 = findViewById(R.id.disable);

        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(MainActivity.this,Controller.class);

        final boolean active = devicePolicyManager.isAdminActive(componentName);

        if(active){
            btn1.setText("Disable");
            btn2.setVisibility(View.VISIBLE);
        }else{
            btn1.setText("Enable");
            btn2.setVisibility(View.GONE);
        }


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean active = devicePolicyManager.isAdminActive(componentName);
                if(active){
                    devicePolicyManager.removeActiveAdmin(componentName);
                    btn1.setText("Enable");
                    btn2.setVisibility(View.GONE);
                }else{
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,componentName);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"You should enable the app!");
                    startActivityForResult(intent,RESULT_ENABLE);

                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devicePolicyManager.lockNow();

            }
        });

    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        switch (requestCode){
            case RESULT_ENABLE:
                if(resultCode==Activity.RESULT_OK){
                    btn1.setText("Disable");
                    btn2.setVisibility(View.VISIBLE);
                }else {
                    Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show();
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
