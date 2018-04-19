package com.permissionutils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import static android.R.string.cancel;
import static android.R.string.ok;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.permissionutils.Constants.APP_SETTINGS;
import static com.permissionutils.Constants.PERMISSIONS;
import static com.permissionutils.Constants.PERMISSIONS_FILTER;
import static com.permissionutils.Constants.PERMISSIONS_REQUEST;
import static com.permissionutils.Constants.PERMISSIONS_RESULT;
import static com.permissionutils.Constants.SETTINGS_REQUEST;

public class PermissionsActivity extends AppCompatActivity implements DialogInterface.OnClickListener {

    private ArrayList<PermissionWrapper> mPermissions;
    private PermissionsResult mResult = new PermissionsResult();
    private boolean mAppSettings = false;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        Bundle extras = getIntent().getExtras();
        if (extras == null) extras = new Bundle();
        Bundle bundle = savedState == null ? extras : savedState;
        mAppSettings = bundle.getBoolean(APP_SETTINGS, false);
        mPermissions = bundle.getParcelableArrayList(PERMISSIONS);
        if (mAppSettings) {
            onActivityResult(SETTINGS_REQUEST, RESULT_OK, null);
        }
        else {
            if (mPermissions != null) {
                buildPermissionsRequest();
            } else {
                cancelRequest();
            }
        }
    }

    private void cancelRequest() {
        Intent intent = new Intent(PERMISSIONS_FILTER);
        intent.putExtra(PERMISSIONS_RESULT, mResult);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(PERMISSIONS, mPermissions);
        outState.putBoolean(APP_SETTINGS, mAppSettings);
    }

    @Override
    public void onRequestPermissionsResult(int code, @NonNull String[] permissions, @NonNull int[] results) {
        super.onRequestPermissionsResult(code, permissions, results);
        if (code == PERMISSIONS_REQUEST) {
            mResult = new PermissionsResult();
            List<String> rationales = new ArrayList<>();
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                PermissionWrapper current = null;
                for (PermissionWrapper wrapper : mPermissions) {
                    if (wrapper.is(permission)) {
                        current = wrapper;
                        break;
                    }
                }
                if (results[i] != PERMISSION_GRANTED) {
                    if (!PermissionUtils.hasRationale(this, permission)) {
                        if (current != null && current.hasRationale())
                            rationales.add(current.getRationale());
                        mResult.blocked.add(current);
                    }
                    mResult.denied.add(current);
                }
                else {
                    mResult.granted.add(current);
                }
            }
            if (rationales.size() > 0) {
                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage(buildRationalMessage(rationales))
                        .setPositiveButton(ok, this)
                        .setNegativeButton(cancel, this)
                        .setNeutralButton(R.string.settings, this)
                        .show();
            }
            else {
                cancelRequest();
            }
        }
    }

    private void buildPermissionsRequest() {
        String[] request = new String[mPermissions.size()];
        for (int i = 0; i< mPermissions.size(); i++) {
            PermissionWrapper wrapper = mPermissions.get(i);
            request[i] = wrapper.getPermission();
        }
        ActivityCompat.requestPermissions(this, request, PERMISSIONS_REQUEST);
    }

    @NonNull
    private String buildRationalMessage(@NonNull List<String> messages) {
        StringBuilder sb = new StringBuilder();
        for (String msg : messages) {
            sb.append("\u2022").append("\u0009").append(msg).append("\n");
        }
        return sb.toString();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                buildPermissionsRequest();
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                showAppSettings();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                cancelRequest();
                break;
        }
    }

    private void showAppSettings() {
        mAppSettings = true;
        PermissionUtils.showAppSettings(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST) {
            mResult = new PermissionsResult();
            for (PermissionWrapper wrapper : mPermissions) {
                String permission = wrapper.getPermission();
                if (!PermissionUtils.hasPermission(this, permission)) {
                    if (!PermissionUtils.hasRationale(this, permission))
                        mResult.blocked.add(wrapper);

                    mResult.denied.add(wrapper);
                }
                else {
                    mResult.granted.add(wrapper);
                }
            }
            cancelRequest();
        }
    }
}
