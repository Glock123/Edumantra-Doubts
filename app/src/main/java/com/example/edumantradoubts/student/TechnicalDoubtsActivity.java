package com.example.edumantradoubts.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.edumantradoubts.R;
import com.example.edumantradoubts.Structure.Question;
import com.example.edumantradoubts.Structure.Technical;
import com.example.edumantradoubts.adapters.TechnicalDoubtsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TechnicalDoubtsActivity extends AppCompatActivity {

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technical_doubts);
        RecyclerView rv = findViewById(R.id.rv_technical);
        getSupportActionBar().hide();

        db = FirebaseFirestore.getInstance();

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading Questions...");
        progressDialog.show();

        db.collection("Technical")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            progressDialog.dismiss();
                            List<Technical> technical = Objects.requireNonNull(task.getResult()).toObjects(Technical.class);
                            TechnicalDoubtsAdapter mAdapter = new TechnicalDoubtsAdapter(new ArrayList<>(technical), TechnicalDoubtsActivity.this);
                            rv.setAdapter(mAdapter);
                            rv.setLayoutManager(new LinearLayoutManager(TechnicalDoubtsActivity.this));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(TechnicalDoubtsActivity.this, "Error getting the questions, try again", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(TechnicalDoubtsActivity.this, StudentNavActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}