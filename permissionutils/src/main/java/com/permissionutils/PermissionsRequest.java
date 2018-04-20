package com.permissionutils;

import android.content.Context;
import com.permissionutils.interfaces.Callback;
import com.permissionutils.interfaces.Permissions;
import com.permissionutils.interfaces.ResultListener;
import com.permissionutils.interfaces.Request;

import java.util.ArrayList;

public class PermissionsRequest {

    private RequestWrapper mRequestWrapper;

    public PermissionsRequest(Context context) {
        mRequestWrapper = new RequestWrapper(context);
    }

    public Callback withPermissions(PermissionWrapper... permissions) {
        return mRequestWrapper.withPermissions(permissions);
    }

    @SuppressWarnings("unused")
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
        public Request withCallback(ResultListener callback) {
            mPermissionsReceiver.setCallback(callback);
            return this;
        }

        @Override
        public void show() {
            mPermissionsReceiver.requestPermissions();
        }
    }
}