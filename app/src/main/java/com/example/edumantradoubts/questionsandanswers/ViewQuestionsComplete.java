 package com.example.edumantradoubts.questionsandanswers;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.edumantradoubts.R;
import com.example.edumantradoubts.Structure.Question;
import com.example.edumantradoubts.dispimagesanswers.ImagesFromStorageFragment;
import com.example.edumantradoubts.sharedpreferences.SessionManager;
import com.google.firestore.v1.Write;

import static com.example.edumantradoubts.loginsignup.SignUpStudentActivity.STUDENTS;
import static com.example.edumantradoubts.loginsignup.SignUpTeacherActivity.TEACHERS;
import static com.example.edumantradoubts.student.ui.home.HomeFragment.MATHS;
import static com.example.edumantradoubts.student.ui.home.HomeFragment.SST;

public class ViewQuestionsComplete extends AppCompatActivity {

    public static Question question=null;
    private TextView standard, subjectAndChapter, questionDescription, date;
    private LinearLayout teachersOnly;
    Button viewAllAnswers, writeAnswer;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_questions_complete);

        standard = findViewById(R.id.grade_of_question);
        subjectAndChapter = findViewById(R.id.subject_and_chapter);
        questionDescription = findViewById(R.id.tv_question_description);
        date = findViewById(R.id.asked_date);
        teachersOnly = findViewById(R.id.teacher_only_layout);
        viewAllAnswers = findViewById(R.id.view_all_answers);
        writeAnswer = findViewById(R.id.write_answer);

        Intent in = getIntent();
        if(in!=null) {
            question = (Question) in.getParcelableExtra("QUESTION");
        }

        if(question==null) {
            Log.e("QUESTION STATUS", "NULL");
            return;
        }

        String std;
        if(question.getStandard().equals("9")) std = "IX";
        else std = "X";
        standard.setText("Class " + std);

        date.setText("Asked on " + question.getDate() + "/" + question.getMonth() + "/" + question.getYear());
        subjectAndChapter.setText(question.getSubject() + ": Chapter " + question.getChapterNumber());
        questionDescription.setText(question.getQuestionDescription());

        SessionManager mManager = new SessionManager(this);
        if(mManager.getUserType().equals(STUDENTS)) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
            teachersOnly.setLayoutParams(lp);
        }

        // Attaching the images fragment
        ImagesFromStorageFragment imageFrag = new ImagesFromStorageFragment();
        Bundle args = new Bundle();
        args.putStringArray("IMAGE PATH", question.getQuestionImages().toArray(new String[0]));
        imageFrag.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_view_images, imageFrag).commit();

        // Attaching the answers fragment
        attachAnswerFragment();

        viewAllAnswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachAnswerFragment();
            }
        });

        writeAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace with write Answer Fragment
                Bundle bundle = new Bundle();
                bundle.putParcelable("QUESTION", question);
                WriteAnswerFragment writeAnsFrag = new WriteAnswerFragment();
                writeAnsFrag.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_view_answers, writeAnsFrag).commit();
            }
        });
    }

    void attachAnswerFragment() {
        ViewAnswersFragment answerFrag = new ViewAnswersFragment();
        Bundle ansArgs = new Bundle();
        ansArgs.putStringArray("ANSWERS PATH", question.getAnswers().toArray(new String[0]));
        answerFrag.setArguments(ansArgs);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_view_answers, answerFrag).commit();
    }

}