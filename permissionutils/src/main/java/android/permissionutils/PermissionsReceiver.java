package android.permissionutils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.Arrays;

import static android.permissionutils.Constants.PERMISSIONS;
import static android.permissionutils.Constants.PERMISSIONS_FILTER;
import static android.permissionutils.Constants.PERMISSIONS_RESULT;

final class PermissionsReceiver extends BroadcastReceiver {

    private static final IntentFilter FILTER = new IntentFilter(PERMISSIONS_FILTER);

    private final Context mContext;

    private PermissionsListener mCallback;
    private ArrayList<PermissionWrapper> mPermissions;

    PermissionsReceiver(Context context) {
        mContext = context;
    }

    void setCallback(PermissionsListener callback) {
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
        intent.putParcelableArrayListExtra(PERMISSIONS, mPermissions);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        PermissionsResult result = intent.getParcelableExtra(PERMISSIONS_RESULT);
        mCallback.onPermissionResult(result);
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(this);
    }
}
