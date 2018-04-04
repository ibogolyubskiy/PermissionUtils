package com.example.myapplication;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.permissionutils.PermissionUtils;
import android.permissionutils.PermissionWrapper;
import android.permissionutils.PermissionsRequest;
import android.permissionutils.PermissionsResult;
import android.permissionutils.interfaces.ResultListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements ResultListener, OnClickListener {

    @SuppressWarnings("unused")
    public static final String[] ALL_PERMISSIONS = new String[] {
            ACCESS_COARSE_LOCATION,
            ACCESS_FINE_LOCATION,
            WRITE_EXTERNAL_STORAGE,
            READ_CONTACTS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void activity(View view) {
        buildPermissionsRequest();
    }

    public void service(View view) {
        startService(new Intent(this, MyService.class));
    }

    private void buildPermissionsRequest() {
        new PermissionsRequest(this)
                .withPermissions(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE, READ_CONTACTS)
                .withCallback(this)
                .build();
    }

    @Override
    public void onPermissionsResult(PermissionsResult result) {
        List<PermissionWrapper> denied = result.denied;
        Log.d("activity permissions", "denied permissions: " + denied.size() + " blocked permissions: " + result.blocked.size());
        if (!denied.isEmpty())
            DialogWrapper.getInstance(result).show(getSupportFragmentManager(), DialogWrapper.class.getName());
        else
            Toast.makeText(this, R.string.all_permissions_granted, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int button) {
        switch (button) {
            case DialogInterface.BUTTON_POSITIVE:
                buildPermissionsRequest();
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                PermissionUtils.showAppSettings(this);
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                break;
        }
    }
}
