package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.os.Bundle;
import android.permissionutils.PermissionUtils;
import android.permissionutils.PermissionWrapper;
import android.permissionutils.PermissionsResult;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.pm.PackageManager.GET_META_DATA;

public class DialogWrapper extends DialogFragment {

    private DialogInterface.OnClickListener mListener;

    public static DialogWrapper getInstance(PermissionsResult result) {
        DialogWrapper dialog = new DialogWrapper();
        Bundle args = new Bundle();
        args.putParcelable("result", result);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (DialogInterface.OnClickListener) context;
        }
        catch (Exception ignored) { }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        PermissionsResult result = getArguments().getParcelable("result");
        return new AlertDialog.Builder(getContext())
                .setView(buildRationaleBody(result.denied))
                .setPositiveButton(android.R.string.ok, mListener)
                .setNegativeButton(android.R.string.cancel, mListener)
                .setNeutralButton(R.string.settings, mListener)
                .create();
    }

    private View buildRationaleBody(List<PermissionWrapper> permissions) {
        LinearLayout body = (LinearLayout) View.inflate(getContext(), R.layout.view_dialog_body, null);
        ViewGroup list = body.findViewById(R.id.permissions);
        Set<CharSequence> groups = getPermissionGroups(permissions);
        for(CharSequence info : groups) {
            TextView group = new TextView(getContext());
            group.setText(info);
            list.addView(group);
        }
        return body;
    }

    @NonNull
    private Set<CharSequence> getPermissionGroups(List<PermissionWrapper> permissions) {
        PackageManager pm = getContext().getPackageManager();
        Set<CharSequence> groups = new HashSet<>();
        for (PermissionWrapper wrapper : permissions) {
            if (PermissionUtils.hasPermission(getContext(), wrapper.getPermission())) continue;
            try {
                PermissionInfo info = pm.getPermissionInfo(wrapper.getPermission(), GET_META_DATA);
                PermissionGroupInfo group = pm.getPermissionGroupInfo(info.group, GET_META_DATA);
                groups.add(group.loadLabel(pm));
            } catch (Exception ignored) { }
        }
        return groups;
    }
}
