package com.example.changeusers;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class Controller extends DeviceAdminReceiver {

    @Override
    public void onEnabled(@NonNull Context context, @NonNull Intent intent) {
        Toast.makeText(context,"Enabled!",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisabled(@NonNull Context context, @NonNull Intent intent) {
        Toast.makeText(context,"Disabled!",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserStarted(@NonNull Context context, @NonNull Intent intent, @NonNull UserHandle startedUser) {
        Toast.makeText(context,"User has started!",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserSwitched(@NonNull Context context, @NonNull Intent intent, @NonNull UserHandle switchedUser) {
        Toast.makeText(context,"Switched!",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserAdded(@NonNull Context context, @NonNull Intent intent, @NonNull UserHandle newUser) {
        Toast.makeText(context,"Added!",Toast.LENGTH_SHORT).show();
    }
}
