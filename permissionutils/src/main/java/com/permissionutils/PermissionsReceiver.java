package com.permissionutils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.permissionutils.interfaces.ResultListener;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.Arrays;

final class PermissionsReceiver extends BroadcastReceiver {

    private static final IntentFilter FILTER = new IntentFilter(Constants.PERMISSIONS_FILTER);

    private final Context mContext;

    private ResultListener mCallback;
    private ArrayList<PermissionWrapper> mPermissions;

    PermissionsReceiver(Context context) {
        mContext = context;
    }

    void setCallback(ResultListener callback) {
        mCallback = callback;
    }

    void setPermissions(PermissionWrapper... permissions) {
        mPermissions = new ArrayList<>(Arrays.asList(permissions));
    }

    void setPermissions(ArrayList<PermissionWrapper> permissions) {
        mPermissions = permissions;
    }

    void requestPermissions() {
        LocalBroadcastManager.getInstance(mContext).registerReceiver(this, FILTER);
        Intent intent = new Intent(mContext, PermissionsActivity.class);
        intent.putParcelableArrayListExtra(Constants.PERMISSIONS, mPermissions);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        PermissionsResult result = intent.getParcelableExtra(Constants.PERMISSIONS_RESULT);
        mCallback.onPermissionsResult(result);
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(this);
    }
}
