package com.example.edumantradoubts.Structure;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Question implements Parcelable {

    /**
     * StudentId: String
     * StudentName: String
     * QuestionId: String
     * Subject: String
     * Standard: String
     * ChapterNumber: String
     * QuestionDescription: String
     * Answers: String[] -> Carrying name of answer documents
     * QuestionImages: String[] -> Path to images for images
     * date: Date of asking
     * month: Month of asking
     * year: Year of asking
     */

    private String studentId;
    private String studentName;
    private String questionId;
    private String subject;
    private String standard;
    private String chapterNumber;
    private String questionDescription;
    private String date;
    private String month;
    private String year;
    private int numAns;

    private List<String> answers, questionImages;


    public Question() {}

    public Question(String studentId, String studentName, String questionId, String subject, String standard, String chapterNumber, String questionDescription, List<String> answers, List<String> questionImages, String date, String month, String year, int numAns) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.questionId = questionId;
        this.subject = subject;
        this.standard = standard;
        this.chapterNumber = chapterNumber;
        this.questionDescription = questionDescription;
        this.answers = answers;
        this.questionImages = questionImages;
        this.date = date;
        this.month = month;
        this.year = year;
        this.numAns = numAns;
    }

    protected Question(Parcel in) {
        studentId = in.readString();
        studentName = in.readString();
        questionId = in.readString();
        subject = in.readString();
        standard = in.readString();
        chapterNumber = in.readString();
        questionDescription = in.readString();
        answers = in.createStringArrayList();
        questionImages = in.createStringArrayList();
        date = in.readString();
        month = in.readString();
        year = in.readString();
        numAns = in.readInt();
    }
    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };


    public List<String> getQuestionImages() {
        return questionImages;
    }

    public void setQuestionImages(List<String> questionImages) {
        this.questionImages = questionImages;
    }

    public int getNumAns() {
        return numAns;
    }

    public void setNumAns(int numAns) {
        this.numAns = numAns;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(String chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public String getQuestionDescription() {
        return questionDescription;
    }

    public void setQuestionDescription(String questionDescription) {
        this.questionDescription = questionDescription;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(studentId);
        dest.writeString(studentName);
        dest.writeString(questionId);
        dest.writeString(subject);
        dest.writeString(standard);
        dest.writeString(chapterNumber);
        dest.writeString(questionDescription);
        dest.writeStringList(answers);
        dest.writeStringList(questionImages);
        dest.writeString(date);
        dest.writeString(month);
        dest.writeString(year);
        dest.writeInt(numAns);
    }
}
