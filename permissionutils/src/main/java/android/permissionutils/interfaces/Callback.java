package android.permissionutils.interfaces;

import android.permissionutils.PermissionsListener;

public interface Callback {
    Request withCallback(PermissionsListener callback);
}