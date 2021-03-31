package com.example.edumantradoubts.loginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edumantradoubts.R;
import com.example.edumantradoubts.Structure.Student;
import com.example.edumantradoubts.Structure.Teacher;
import com.example.edumantradoubts.sharedpreferences.SessionManager;
import com.example.edumantradoubts.student.StudentNavActivity;
import com.example.edumantradoubts.teacher.TeacherNavActivity;
import com.example.edumantradoubts.student.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import static com.example.edumantradoubts.loginsignup.SignUpStudentActivity.STUDENTS;
import static com.example.edumantradoubts.loginsignup.SignUpTeacherActivity.TEACHERS;

public class LoginActivity extends AppCompatActivity {

    private EditText refNumber, password;
    private TextView createStudent, createTeacher, login;
    private String type = null;
    FirebaseFirestore db;
    private CheckBox teacher, student;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        db = FirebaseFirestore.getInstance();

        refNumber = findViewById(R.id.reference_number);
        password = findViewById(R.id.password);
        createStudent = findViewById(R.id.create_student);
        createTeacher = findViewById(R.id.create_teacher);
        teacher = findViewById(R.id.teacher);
        student = findViewById(R.id.student);
        login = findViewById(R.id.login);
        Log.e("LOGIN ACTIVITY", "VISITED");

        checkIfLoggedIn();

        createStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginActivity.this, SignUpStudentActivity.class);
                startActivity(in);
            }
        });

        createTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginActivity.this, SignUpTeacherActivity.class);
                startActivity(in);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginMe();
            }
        });

    }

    void checkIfLoggedIn() {
        SessionManager mManager = new SessionManager(this);
        String userType = mManager.getUserType();
        if(userType==null) {
            // Meaning that no user from device is actively logged in
            return;
        }
        Log.e("USER TYPE", userType);
        // TODO: Get the logged in user data and proceed to home page : DONE
        if(userType.equals(STUDENTS)) {
            Intent in = new Intent(this, StudentNavActivity.class);
            startActivity(in);
            finish();
        }
        else {
            Intent in = new Intent(this, TeacherNavActivity.class);
            startActivity(in);
            finish();
        }

    }

    void loginMe() {

        // type: teacher/student
        String id = refNumber.getText().toString();
        String pass = password.getText().toString();

        if(type==null) {
            Toast.makeText(this, "User type (Student/Teacher)\nNot Selected", Toast.LENGTH_LONG).show();
            return;
        }

        if(id.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "None of fields can be left empty", Toast.LENGTH_SHORT).show();
            return;
        }

        /**
         * Now we will validate the user info, if correct we can log the user in
         */

        CollectionReference collectionReference;
        if(type.equals(STUDENTS)) {
            collectionReference = db.collection(STUDENTS);
        }
        else {
            collectionReference = db.collection(TEACHERS);
        }

        String user = type.substring(0, type.length()-1);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...Please wait");
        progressDialog.show();

        collectionReference.document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            progressDialog.dismiss();
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot.exists()) {
                                if(type.equals(STUDENTS)) {
                                    Student student;
                                    student = documentSnapshot.toObject(Student.class);
                                    Log.e("STUDENT NAME", student.getName());
                                    Log.e("STUDENT standard", student.getStandard());
                                    if(!student.getPassword().equals(pass)) {
                                        Toast.makeText(LoginActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    // Saving to local device, so that we do not have to login again
                                    SessionManager mManager = new SessionManager(LoginActivity.this);
                                    mManager.saveSession(STUDENTS, student);

                                    //userStudent = student;

                                    Intent in = new Intent(LoginActivity.this, StudentNavActivity.class);
                                    startActivity(in);
                                    finish();

                                }
                                else {
                                    Teacher teacher;
                                    teacher = documentSnapshot.toObject(Teacher.class);
                                    if(!teacher.getPassword().equals(pass)) {
                                        Toast.makeText(LoginActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                                        return;
                                    }


                                    // Saving to local device, so that we do not have to login again
                                    SessionManager mManager = new SessionManager(LoginActivity.this);
                                    mManager.saveSession(TEACHERS, teacher);

                                    // TODO: Make TeacherNavActivity like one made in student
                                    Intent in = new Intent(LoginActivity.this, TeacherNavActivity.class);
                                    startActivity(in);
                                    finish();
                                }



                            }
                            else {
                                Toast.makeText(LoginActivity.this, "Not a valid " + user + "id", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }



    void createANewAccount() {
        Intent in = new Intent(LoginActivity.this, SignUpStudentActivity.class);
        startActivity(in);
    }

    public void checkBoxChecked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.teacher: type = TEACHERS;
            refNumber.setHint("Enter Teacher ID");
            student.setChecked(false);
            break;
            case R.id.student: type = STUDENTS;
            refNumber.setHint("Enter Student ID");
            teacher.setChecked(false);
            break;
        }

        if(!student.isChecked() && !teacher.isChecked()) {
            refNumber.setHint("Select student or teacher");
            type = null;
        }
    }


}