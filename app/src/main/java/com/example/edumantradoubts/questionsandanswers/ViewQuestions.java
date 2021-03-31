package com.example.edumantradoubts.questionsandanswers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.edumantradoubts.R;

public class ViewQuestions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_questions);

        // Populating the screen with view ALl Questions fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_all_question_fragment, new ViewQuestionsFragment()).commit();
    }
}