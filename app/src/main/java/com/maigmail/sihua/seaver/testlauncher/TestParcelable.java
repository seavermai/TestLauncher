package com.maigmail.sihua.seaver.testlauncher;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Seaver on 7/14/2016.
 */
public class TestParcelable implements Parcelable {

    private int mData;
    private String testString;

    public static final Parcelable.Creator<TestParcelable> CREATOR = new Parcelable.Creator<TestParcelable>() {
        public TestParcelable createFromParcel(Parcel in) {
            return new TestParcelable(in);
        }

        public TestParcelable[] newArray(int size) {
            return new TestParcelable[size];
        }
    };

    private TestParcelable(Parcel in) {
        mData = in.readInt();
        testString = in.readString();
    }

    public TestParcelable(String string) {
        testString = string;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
        out.writeString(testString);
    }

    public int describeContents() {
        return 0;
    }

    public int getmData() {
        return mData;
    }

    public String getTestString() {
        return testString;
    }
}
