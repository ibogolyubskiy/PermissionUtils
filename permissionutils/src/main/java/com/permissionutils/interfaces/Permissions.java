package com.permissionutils.interfaces;

import com.permissionutils.PermissionWrapper;

import java.util.ArrayList;

public interface Permissions {
    Callback withPermissions(PermissionWrapper... permissions);
    Callback withPermissions(ArrayList<PermissionWrapper> permissions);
}