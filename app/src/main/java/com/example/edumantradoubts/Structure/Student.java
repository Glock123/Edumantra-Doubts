package com.example.edumantradoubts.Structure;

import android.os.Parcel;
import android.os.Parcelable;

public class Student implements Parcelable {

    private String studentId;
    private String name;
    private String phoneno;
    private String emailId;
    private String password;


    private String standard;

    public Student() {

    }

    public Student(String studentId, String name, String emailId, String phoneno, String password, String standard) {
        this.studentId = studentId;
        this.name = name;
        this.emailId = emailId;
        this.phoneno = phoneno;
        this.password = password;
        this.standard = standard;
    }

    protected Student(Parcel in) {
        studentId = in.readString();
        name = in.readString();
        phoneno = in.readString();
        emailId = in.readString();
        password = in.readString();
        standard = in.readString();
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
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
        dest.writeString(studentId);
        dest.writeString(name);
        dest.writeString(phoneno);
        dest.writeString(emailId);
        dest.writeString(password);
        dest.writeString(standard);
    }
}
