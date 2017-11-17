package android.permissionutils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class PermissionsResult implements Parcelable {

    public List<PermissionWrapper> granted = new ArrayList<>();
    public List<PermissionWrapper> denied = new ArrayList<>();
    public List<PermissionWrapper> blocked = new ArrayList<>();

    PermissionsResult() { }

    private PermissionsResult(Parcel in) {
        granted = in.createTypedArrayList(PermissionWrapper.CREATOR);
        denied = in.createTypedArrayList(PermissionWrapper.CREATOR);
        blocked = in.createTypedArrayList(PermissionWrapper.CREATOR);
    }

    public static final Creator<PermissionsResult> CREATOR = new Creator<PermissionsResult>() {
        @Override
        public PermissionsResult createFromParcel(Parcel in) {
            return new PermissionsResult(in);
        }

        @Override
        public PermissionsResult[] newArray(int size) {
            return new PermissionsResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(granted);
        parcel.writeTypedList(denied);
        parcel.writeTypedList(blocked);
    }
}
