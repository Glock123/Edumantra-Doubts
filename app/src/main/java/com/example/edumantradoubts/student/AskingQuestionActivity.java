package com.example.edumantradoubts.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edumantradoubts.R;
import com.example.edumantradoubts.Structure.Question;
import com.example.edumantradoubts.gettingimages.UploadImages;
import com.example.edumantradoubts.sharedpreferences.SessionManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AskingQuestionActivity extends AppCompatActivity {

    private EditText chpNo, ques_dec;
    private TextView sub;
    private String studentID, name, subject;
    private CardView proceedToUploadImages;
    private String questionId;
    private String standard = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asking_question);
        getSupportActionBar().hide();


        chpNo = findViewById(R.id.chapter_number);
        sub = findViewById(R.id.subject_fixed);
        ques_dec = findViewById(R.id.question_description);
        proceedToUploadImages = findViewById(R.id.proceed_to_upload_images);

        SessionManager mManager = new SessionManager(this);
        studentID = mManager.getStudentDetails().getStudentId();
        name = mManager.getStudentDetails().getName();

        Intent in = getIntent();
        subject = in.getStringExtra("Subject Name");
        sub.setText(subject);

        proceedToUploadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImages();
            }
        });

    }

    private void uploadImages() {
        String chapterNumber = chpNo.getText().toString();
        String questionDescription = ques_dec.getText().toString();

        if(chapterNumber.isEmpty() || questionDescription.isEmpty() || standard==null) {
            Toast.makeText(this, "None of fields should be left empty", Toast.LENGTH_SHORT).show();
            return;
        }

        questionId = studentID + "-" + subject + "-" + chapterNumber + "-" + System.currentTimeMillis();

        int date, month, year;
        Calendar calendar = Calendar.getInstance();
        date = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);


        Question question = new Question(studentID, name, questionId, subject, standard, chapterNumber,
                questionDescription, new ArrayList<>(), new ArrayList<>(), String.valueOf(date),
                String.valueOf(month), String.valueOf(year), 0);

        // Proceed to upload the images

        Intent in = new Intent(this, UploadImages.class);
        SessionManager mManager = new SessionManager(this);
        mManager.storeQuestionTemporary(question);
        startActivity(in);

    }

    public void onSelectedClass(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        if(!checked) {
            standard = null;
            return;
        }
        switch(view.getId()) {
            case R.id.ninth_class_ask_question:
                standard = "9";
                break;
            case R.id.tenth_class_ask_question:
                standard = "10";
                break;
        }
    }

}