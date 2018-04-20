package com.permissionutils.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.permissionutils.PermissionUtils;
import com.permissionutils.PermissionWrapper;
import com.permissionutils.PermissionsRequest;
import com.permissionutils.PermissionsResult;
import com.permissionutils.interfaces.ResultListener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.GET_META_DATA;

public class MyActivity extends AppCompatActivity implements ResultListener, OnClickListener {

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
                .show();
    }

    @Override
    public void onPermissionsResult(PermissionsResult result) {
        List<PermissionWrapper> denied = result.denied;
        Log.d("activity permissions", "denied permissions: " + denied.size() + " blocked permissions: " + result.blocked.size());
        if (!denied.isEmpty())
            new AlertDialog.Builder(this)
                    .setView(buildRationaleBody(result.denied))
                    .setPositiveButton(android.R.string.ok, this)
                    .setNegativeButton(android.R.string.cancel, this)
                    .setNeutralButton(R.string.settings, this)
                    .show();
        else
            Toast.makeText(this, R.string.all_permissions_granted, Toast.LENGTH_SHORT).show();
    }

    private View buildRationaleBody(List<PermissionWrapper> permissions) {
        LinearLayout body = (LinearLayout) View.inflate(this, R.layout.view_dialog_body, null);
        ViewGroup list = body.findViewById(R.id.permissions);
        Set<CharSequence> groups = getPermissionGroups(permissions);
        for(CharSequence info : groups) {
            TextView group = new TextView(this);
            group.setText(info);
            list.addView(group);
        }
        return body;
    }

    @NonNull
    private Set<CharSequence> getPermissionGroups(List<PermissionWrapper> permissions) {
        PackageManager pm = getPackageManager();
        Set<CharSequence> groups = new HashSet<>();
        for (PermissionWrapper wrapper : permissions) {
            if (PermissionUtils.hasPermission(this, wrapper.getPermission())) continue;
            try {
                PermissionInfo info = pm.getPermissionInfo(wrapper.getPermission(), GET_META_DATA);
                PermissionGroupInfo group = pm.getPermissionGroupInfo(info.group, GET_META_DATA);
                groups.add(group.loadLabel(pm));
            } catch (Exception ignored) { }
        }
        return groups;
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
