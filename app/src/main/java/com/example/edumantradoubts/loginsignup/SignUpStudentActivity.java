package com.example.edumantradoubts.loginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.edumantradoubts.R;
import com.example.edumantradoubts.Structure.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import static com.example.edumantradoubts.gettingimages.UploadImages.QUESTIONS;

public class SignUpStudentActivity extends AppCompatActivity {

    private EditText refNoWidget, nameWidget, emailidWidget, phonenoWidget, passwordWidget, confirmPasswordWidget;
    private Button register;
    FirebaseFirestore db;
    final static public String REGISTERED_STUDENT_COLLECTION = "Registered Students";
    final static public String STUDENTS = "Students";
    private String standard = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_student);
        getSupportActionBar().hide();

        db = FirebaseFirestore.getInstance();

        refNoWidget = findViewById(R.id.r_no);
        nameWidget = findViewById(R.id.name);
        emailidWidget = findViewById(R.id.emailid);
        phonenoWidget = findViewById(R.id.phone);
        passwordWidget = findViewById(R.id.password);
        confirmPasswordWidget = findViewById(R.id.confirm_password);
        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerMethod();
            }
        });

    }

    private void registerMethod() {
        // Getting text from all the entries

        String refNo = refNoWidget.getText().toString();
        String name = nameWidget.getText().toString();
        String emailid = emailidWidget.getText().toString();
        String phoneno = phonenoWidget.getText().toString();
        String password = passwordWidget.getText().toString();
        String confirmPassword = confirmPasswordWidget.getText().toString();

        /*
        * Uncomment Below later
        */
        if(refNo.isEmpty() || name.isEmpty() || emailid.isEmpty() || phoneno.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || standard==null) {
            Toast.makeText(this, "None of the field should be kept empty", Toast.LENGTH_SHORT).show();
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
        progressDialog.setTitle("Loading...Please wait");
        progressDialog.show();

        // All the primary checkups are done
        // Now we check wheather reference id is a valid reference id or not, if yes then register the student

        db.collection(REGISTERED_STUDENT_COLLECTION)
                .whereArrayContains("List", refNo)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            if(Objects.requireNonNull(task.getResult()).size()>0) {
                                // Toast.makeText(SignUpStudentActivity.this, "Student Registered", Toast.LENGTH_SHORT).show();
                                db.collection(STUDENTS).document(refNo)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful()) {
                                                    DocumentSnapshot documentSnapshot = task.getResult();
                                                    if(documentSnapshot.exists()) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(SignUpStudentActivity.this, "Student already signed up with this Student ID", Toast.LENGTH_LONG).show();
                                                    }
                                                    else {
                                                        Student student = new Student(refNo, name, emailid, phoneno, password, standard);
                                                        db.collection(STUDENTS)
                                                                .document(refNo)
                                                                .set(student)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        progressDialog.dismiss();
                                                                        Toast.makeText(SignUpStudentActivity.this, "Student registered successfully", Toast.LENGTH_SHORT).show();
                                                                        Intent in = new Intent(SignUpStudentActivity.this, LoginActivity.class);
                                                                        startActivity(in);
                                                                        finish();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        progressDialog.dismiss();
                                                                        Toast.makeText(SignUpStudentActivity.this, "Try again\n" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                                                    }
                                                                });
                                                    }
                                                }
                                            }
                                        });

                            }
                        else {
                                progressDialog.dismiss();
                                Toast.makeText(SignUpStudentActivity.this, "Student Not Registered", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(SignUpStudentActivity.this, "Error occurred, try again\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void onSelectedClass(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        if(!checked) {
            standard = null;
            return;
        }

        switch(view.getId()) {
            case R.id.ninth_class:
                standard = "9";
                break;
            case R.id.tenth_class:
                standard = "10";
                break;
        }
    }

}