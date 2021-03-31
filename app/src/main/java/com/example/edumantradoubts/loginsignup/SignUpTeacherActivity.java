package com.example.edumantradoubts.loginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.edumantradoubts.R;
import com.example.edumantradoubts.Structure.Teacher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import static com.example.edumantradoubts.student.ui.home.HomeFragment.MATHS;
import static com.example.edumantradoubts.student.ui.home.HomeFragment.SCIENCE;
import static com.example.edumantradoubts.student.ui.home.HomeFragment.SST;

public class SignUpTeacherActivity extends AppCompatActivity {

    private String subject = null;
    private EditText teacherIdWidget, nameWidget, emailidWidget, phonenoWidget, passwordWidget, confirmPasswordWidget;
    private Button register;
    FirebaseFirestore db;

    public static String REGISTERED_TEACHER_COLLECTION = "Registered Teachers";
    public static String TEACHERS = "Teachers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_teacher);
        getSupportActionBar().hide();

        db = FirebaseFirestore.getInstance();

        teacherIdWidget = findViewById(R.id.teacher_id);
        nameWidget = findViewById(R.id.teacher_name);
        emailidWidget = findViewById(R.id.teacher_email);
        phonenoWidget = findViewById(R.id.teacher_phone);
        passwordWidget = findViewById(R.id.teacher_password);
        confirmPasswordWidget = findViewById(R.id.teacher_confirm_password);
        register = findViewById(R.id.teacher_register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerMe();
            }
        });

    }

    public void onSelectSubject(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        if(!checked) {
            subject = null;
            Log.e("SUBJECT", null);
            return;
        }
        switch(view.getId()) {
            case R.id.science:
                subject = SCIENCE;
                break;
            case R.id.maths:
                subject = MATHS;
                break;
            case R.id.sst:
                subject = SST ;
                break;
        }
        Log.e("SUBJECT", subject);
    }

    void registerMe() {
        String id = teacherIdWidget.getText().toString();
        String name = nameWidget.getText().toString();
        String emailid = emailidWidget.getText().toString();
        String phoneno = phonenoWidget.getText().toString();
        String password = passwordWidget.getText().toString();
        String confirmPassword = confirmPasswordWidget.getText().toString();

        if(id.isEmpty() || name.isEmpty() || emailid.isEmpty() || phoneno.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "None of the field should be kept empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(subject==null) {
            Toast.makeText(this, "Subject Not Selected", Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length() < 6) {
            Toast.makeText(this, "Length of password should not be less than 6", Toast.LENGTH_LONG).show();
            return;
        }

        if(password.compareTo(confirmPassword) != 0) {
            Toast.makeText(this, "Password not same in both fields", Toast.LENGTH_SHORT).show();
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Registering...Please wait");
        progressDialog.show();
        // Check if Teacher is registered, if yes then create the account
        db.collection(REGISTERED_TEACHER_COLLECTION)
                .whereArrayContains("List", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && !Objects.requireNonNull(task.getResult()).isEmpty()) {

                            db.collection(TEACHERS)
                                    .document(id)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()) {
                                                DocumentSnapshot documentSnapshot = task.getResult();
                                                if(documentSnapshot.exists()) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(SignUpTeacherActivity.this, "Teacher already registered with same Teacher ID", Toast.LENGTH_LONG).show();
                                                }
                                                else {
                                                    Teacher teacher = new Teacher(id, name, emailid, phoneno, subject, password);
                                                    db.collection(TEACHERS)
                                                            .document(id)
                                                            .set(teacher)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(SignUpTeacherActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                                                    Intent in = new Intent(SignUpTeacherActivity.this, LoginActivity.class);
                                                                    startActivity(in);
                                                                    finish();
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(SignUpTeacherActivity.this, "Cannot be added, try again", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            }
                                        }
                                    });


                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(SignUpTeacherActivity.this, "Teacher is not registered", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(SignUpTeacherActivity.this, "Error occurred, try again\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}