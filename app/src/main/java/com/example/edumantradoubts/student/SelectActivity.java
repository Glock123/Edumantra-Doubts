package com.example.edumantradoubts.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.edumantradoubts.R;
import com.example.edumantradoubts.questionsandanswers.ViewQuestions;
import com.example.edumantradoubts.sharedpreferences.SessionManager;

import static com.example.edumantradoubts.loginsignup.SignUpStudentActivity.STUDENTS;

public class SelectActivity extends AppCompatActivity {

    private LinearLayout askQuestion, viewAllQuestion;
    private String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        getSupportActionBar().hide();

        askQuestion = findViewById(R.id.ask_new_question);
        viewAllQuestion = findViewById(R.id.view_all_questions);

        Intent in = getIntent();
        subject = in.getStringExtra("Subject Name");
        //Toast.makeText(this, subject + " selected ", Toast.LENGTH_SHORT).show();

        askQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askQuestionMethod();
            }
        });

        // TODO: Implement viewAllQuestion.setOnClickListener : DONE

        viewAllQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(SelectActivity.this, ViewQuestions.class);
                SessionManager sessionManager = new SessionManager(SelectActivity.this);
                sessionManager.storeQueryForViewQuestions(subject);
                startActivity(in);
            }
        });

    }

    void askQuestionMethod() {
        Intent in = new Intent(this, AskingQuestionActivity.class);
        in.putExtra("Subject Name", subject);
        startActivity(in);
    }
}