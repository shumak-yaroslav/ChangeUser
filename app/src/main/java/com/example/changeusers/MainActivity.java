package com.example.changeusers;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {




Button sw;
RelativeLayout rel;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sw = findViewById(R.id.sw);
        rel = findViewById(R.id.rel);

        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchUser();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void switchUser() {

        DevicePolicyManager dpm =  (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName admin = new ComponentName(MainActivity.this,Controller.class);

        // If device admin not active, take user to settings
        if (!dpm.isAdminActive(admin)) {
            Intent activateDeviceAdminIntent =  new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            activateDeviceAdminIntent.putExtra(
                    DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                    admin
            );

            startActivityForResult(activateDeviceAdminIntent, 1);
            return;
        }


        if (dpm.isDeviceOwnerApp(admin.getPackageName())) {  // Return false if app run as secondary user
            rel.setBackgroundColor(Color.BLUE);
            List<UserHandle> secondaryUsers = dpm.getSecondaryUsers(admin);
            if (!secondaryUsers.isEmpty()) {  // Switch to secondary user
                dpm.switchUser(admin, secondaryUsers.get(0));
            } else {    // Create and switch to new user
                Set<String> identifiers = dpm.getAffiliationIds(admin);
                if (identifiers.isEmpty()) {
                    identifiers.add(UUID.randomUUID().toString());
                    dpm.setAffiliationIds(admin, identifiers);
                }
                PersistableBundle adminExtras = new PersistableBundle();

                adminExtras.putString("AFFILIATION_ID_KEY", identifiers.iterator().next());

                try {
                    UserHandle newUser = dpm.createAndManageUser(
                            admin,
                            "Foo",
                            admin,
                            adminExtras,
                            DevicePolicyManager.MAKE_USER_EPHEMERAL |
                                    DevicePolicyManager.SKIP_SETUP_WIZARD |
                                    DevicePolicyManager.LEAVE_ALL_SYSTEM_APPS_ENABLED);
                    dpm.switchUser(admin, newUser);


                } catch (UserManager.UserOperationException e) {
                    Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }

    }



}
