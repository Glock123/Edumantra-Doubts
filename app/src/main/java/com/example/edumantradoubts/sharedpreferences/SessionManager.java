package com.example.edumantradoubts.sharedpreferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.edumantradoubts.Structure.Question;
import com.example.edumantradoubts.Structure.Student;
import com.example.edumantradoubts.Structure.Teacher;
import com.google.gson.Gson;

import static com.example.edumantradoubts.loginsignup.SignUpStudentActivity.STUDENTS;
import static com.example.edumantradoubts.loginsignup.SignUpTeacherActivity.TEACHERS;

public class SessionManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static final String SHARED_PREF_NAME = "session";

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(String user, Object object) {
        if(user.equals(STUDENTS)) {
            Student student = (Student) object;
            editor.putString("type", STUDENTS);
            editor.putString("studentid", student.getStudentId());
            editor.putString("name", student.getName());
            editor.putString("phoneno", student.getPhoneno());
            editor.putString("email", student.getEmailId());
            editor.putString("password", student.getPassword());
            editor.putString("standard", student.getStandard());
            editor.commit();
        }
        else {
            Teacher teacher = (Teacher) object;
            editor.putString("type", TEACHERS);
            editor.putString("teacherid", teacher.getTeacherId());
            editor.putString("subject", teacher.getSubject());
            editor.putString("name", teacher.getName());
            editor.putString("phoneno", teacher.getPhoneNo());
            editor.putString("email", teacher.getEmailId());
            editor.putString("password", teacher.getPassword());

            editor.commit();
        }
    }

    public String getUserType() {
        return sharedPreferences.getString("type", null);
    }

    public Student getStudentDetails() {
        return new Student(
            sharedPreferences.getString("studentid", null),
            sharedPreferences.getString("name", null),
            sharedPreferences.getString("email", null),
                sharedPreferences.getString("phoneno", null),
                sharedPreferences.getString("password", null),
                sharedPreferences.getString("standard", null));
    }

    public Teacher getTeacherDetails() {
        return new Teacher (
                sharedPreferences.getString("teacherid", null),
                sharedPreferences.getString("name", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("phoneno", null),
                sharedPreferences.getString("subject", null),
                sharedPreferences.getString("password", null)
        );
    }

    public void storeQuestionTemporary(Question question) {
        Gson gson = new Gson();
        String json = gson.toJson(question);
        editor.putString("QUESTION", json);
        editor.commit();
    }

    public Question getStoredQuestion() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("QUESTION", null);
        if(json != null) return gson.fromJson(json, Question.class);
        return null;
    }

    public void destroyQuestion() {
        editor.remove("QUESTION");
        editor.apply();
    }

    public void removeSession() {
        editor.clear();
        editor.apply();
    }

    public void storeQueryForViewQuestions(String query) {

        /**
          query of 3 types:
          a. ALL: View all questions of all subjects and all students
          b. STUDENT: View all questions asked by current student (search by studentId)
          c. TEACHER: View all questions in a specific subject (search by subject of teacher)
         */

        editor.putString("FILTER", query);
        editor.commit();
        editor.apply();
    }

    public String getStoredQueryForViewQuestions() {
        String ret = sharedPreferences.getString("FILTER", null);

        // Destroy the stored query, so that new query can be stored again
        editor.remove("FILTER");
        editor.apply();
        return ret;
    }

}
