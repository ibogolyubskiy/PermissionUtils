package android.permissionutils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.permissionutils.interfaces.Callback;
import android.permissionutils.interfaces.Permissions;
import android.permissionutils.interfaces.Request;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.permissionutils.Constants.SETTINGS_REQUEST;
import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class PermissionsRequest {

    private RequestWrapper mRequestWrapper;

    public PermissionsRequest(Context context) {
        mRequestWrapper = new RequestWrapper(context);
    }

    public Callback withPermissions(PermissionWrapper... permissions) {
        return mRequestWrapper.withPermissions(permissions);
    }

    public Callback withPermissions(ArrayList<PermissionWrapper> permissions) {
        return mRequestWrapper.withPermissions(permissions);
    }

    public Callback withPermissions(String... permissions) {
        PermissionWrapper[] wrapper = new PermissionWrapper[permissions.length];
        for (int i = 0; i<permissions.length; i++) {
            wrapper[i] = new PermissionWrapper(permissions[i]);
        }
        return mRequestWrapper.withPermissions(wrapper);
    }

    private class RequestWrapper implements Permissions, Callback, Request {

        private PermissionsReceiver mPermissionsReceiver;

        RequestWrapper (Context context) {
            mPermissionsReceiver = new PermissionsReceiver(context);
        }

        @Override
        public Callback withPermissions(PermissionWrapper... permissions) {
            mPermissionsReceiver.setPermissions(permissions);
            return this;
        }

        @Override
        public Callback withPermissions(ArrayList<PermissionWrapper> permissions) {
            mPermissionsReceiver.setPermissions(permissions);
            return this;
        }

        @Override
        public Request withCallback(PermissionsListener callback) {
            mPermissionsReceiver.setCallback(callback);
            return this;
        }

        @Override
        public void build() {
            mPermissionsReceiver.requestPermissions();
        }
    }

    public static void showAppSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, SETTINGS_REQUEST);
    }

    public static boolean hasPermission(Context context, String permission) {
        return checkSelfPermission(context, permission) == PERMISSION_GRANTED;
    }

    public static boolean hasRationale(Activity activity, String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }
}