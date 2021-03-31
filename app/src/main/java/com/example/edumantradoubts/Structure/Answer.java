package com.example.edumantradoubts.Structure;

import java.util.List;

public class Answer {
    private String teacherId;
    private String teacherName;
    private String questionId;
    private String answerId;
    private String answerDescription;
    private String subject;
    private String date;
    private String month;
    private String year;
    private List<String> answerImages;

    public Answer() {}

    public Answer(String teacherId, String teacherName, String subject, String answerDescription, String date,
                  String month, String year, List<String> answerImages, String questionId, String answerId) {
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.subject = subject;
        this.answerDescription = answerDescription;
        this.date = date;
        this.month = month;
        this.year = year;
        this.answerImages = answerImages;
        this.questionId = questionId;
        this.answerId = answerId;
    }


    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

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

    public String getAnswerDescription() {
        return answerDescription;
    }

    public void setAnswerDescription(String answerDescription) {
        this.answerDescription = answerDescription;
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

    public List<String> getAnswerImages() {
        return answerImages;
    }

    public void setAnswerImages(List<String> answerImages) {
        this.answerImages = answerImages;
    }


    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

}
