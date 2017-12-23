package android.permissionutils;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

public class PermissionWrapper implements Parcelable {

    private PermissionWrapper(Parcel in) {
        mPermission = in.readString();
        mRationale = in.readString();
    }

    @SuppressWarnings("WeakerAccess")
    public static final Creator<PermissionWrapper> CREATOR = new Creator<PermissionWrapper>() {
        @Override
        public PermissionWrapper createFromParcel(Parcel in) {
            return new PermissionWrapper(in);
        }

        @Override
        public PermissionWrapper[] newArray(int size) {
            return new PermissionWrapper[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PermissionWrapper that = (PermissionWrapper) o;

        return TextUtils.equals(mPermission, that.mPermission);
    }

    @SuppressWarnings("WeakerAccess")
    public boolean is(String permission) {
        return TextUtils.equals(mPermission, permission);
    }

    @Override
    public int hashCode() {
        int result = mPermission.hashCode();
        result = 31 * result + (mRationale != null ? mRationale.hashCode() : 0);
        return result;
    }

    public String getPermission() {
        return mPermission;
    }

    @SuppressWarnings("WeakerAccess")
    public String getRationale() {
        return mRationale;
    }

    @SuppressWarnings("WeakerAccess")
    public boolean hasRationale() {
        return !TextUtils.isEmpty(mRationale);
    }

    private String mPermission;
    private String mRationale;

    @SuppressWarnings("WeakerAccess")
    public PermissionWrapper(@NonNull String permission) {
        mPermission = permission;
    }

    public PermissionWrapper(@NonNull String permission, String rationale) {
        mPermission = permission;
        mRationale = rationale;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mPermission);
        parcel.writeString(mRationale);
    }
}
