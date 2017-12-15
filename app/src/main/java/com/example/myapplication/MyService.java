package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.permissionutils.PermissionWrapper;
import android.permissionutils.PermissionsListener;
import android.permissionutils.PermissionsRequest;
import android.permissionutils.PermissionsResult;
import android.util.Log;
import android.widget.Toast;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MyService extends Service {

    public static final PermissionWrapper[] ALL_PERMISSIONS = new PermissionWrapper[]{
            new PermissionWrapper(ACCESS_COARSE_LOCATION, "Need coarse location"),
            new PermissionWrapper(ACCESS_FINE_LOCATION, "Need fine location"),
            new PermissionWrapper(WRITE_EXTERNAL_STORAGE, "Need write external storage"),
            new PermissionWrapper(READ_CONTACTS, "Need read contacts")
    };

    public MyService() { }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new PermissionsRequest(this).withPermissions(ALL_PERMISSIONS).withCallback(new PermissionsListener() {

            @Override
            public void onPermissionsResult(PermissionsResult result) {
                Log.d("permissionutils",
                        "granted permissions: " + result.granted.size() +
                        " denied permissions: " + result.denied.size() +
                        " blocked permissions: " + result.blocked.size());
                Toast.makeText(MyService.this, getString(R.string.result, result.granted.size(), result.denied.size(), result.blocked.size()), Toast.LENGTH_SHORT).show();
                stopSelf();
            }
        }).build();

        return super.onStartCommand(intent, flags, startId);
    }
}
