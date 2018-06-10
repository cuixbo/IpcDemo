package demo.cxb.com.ipcaidl;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    public String name;
    public int age;
    public int sex;
    public boolean verified;

    public User(String name, int age, int sex, boolean verified) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.verified = verified;
    }

    protected User(Parcel in) {
        name = in.readString();
        age = in.readInt();
        sex = in.readInt();
        verified = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
        dest.writeInt(sex);
        dest.writeByte((byte) (verified ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                ", verified=" + verified +
                '}';
    }
}
