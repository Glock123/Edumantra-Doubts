package com.example.edumantradoubts.Structure;

import android.os.Parcel;
import android.os.Parcelable;

public class Teacher implements Parcelable {
    private String teacherId;
    private String name;
    private String emailId;
    private String phoneNo;

    private String subject;

    private String password;
    Teacher() {}

    public Teacher(String teacherId, String name, String emailId, String phoneNo, String subject, String password) {
        this.teacherId = teacherId;
        this.name = name;
        this.emailId = emailId;
        this.phoneNo = phoneNo;
        this.password = password;
        this.subject = subject;
    }

    protected Teacher(Parcel in) {
        teacherId = in.readString();
        name = in.readString();
        emailId = in.readString();
        phoneNo = in.readString();
        subject = in.readString();
        password = in.readString();
    }

    public static final Creator<Teacher> CREATOR = new Creator<Teacher>() {
        @Override
        public Teacher createFromParcel(Parcel in) {
            return new Teacher(in);
        }

        @Override
        public Teacher[] newArray(int size) {
            return new Teacher[size];
        }
    };

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(teacherId);
        dest.writeString(name);
        dest.writeString(emailId);
        dest.writeString(phoneNo);
        dest.writeString(subject);
        dest.writeString(password);
    }
}
