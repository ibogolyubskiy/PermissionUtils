package android.permissionutils.interfaces;

import android.permissionutils.PermissionWrapper;

import java.util.ArrayList;

public interface Permissions {
    Callback withPermissions(PermissionWrapper... permissions);
    Callback withPermissions(ArrayList<PermissionWrapper> permissions);
}