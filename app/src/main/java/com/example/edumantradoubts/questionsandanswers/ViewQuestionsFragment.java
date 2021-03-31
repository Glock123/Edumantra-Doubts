package com.example.edumantradoubts.questionsandanswers;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.edumantradoubts.R;
import com.example.edumantradoubts.Structure.Question;
import com.example.edumantradoubts.Structure.Student;
import com.example.edumantradoubts.Structure.Teacher;
import com.example.edumantradoubts.adapters.ViewQuestionsAdapter;
import com.example.edumantradoubts.sharedpreferences.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.edumantradoubts.gettingimages.UploadImages.QUESTIONS;
import static com.example.edumantradoubts.loginsignup.SignUpStudentActivity.STUDENTS;

public class ViewQuestionsFragment extends Fragment {

    FirebaseFirestore db;
    public static List<Question> questions;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_questions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /**
         * Three possibilities for myDoubt queries
         * a. Show all Doubts:
         * b. Show doubts of specific student
         * c. Show doubts of specific student
         */

        SessionManager sessionManager = new SessionManager(getContext());
        String queryString = sessionManager.getStoredQueryForViewQuestions();

        Query query;

        Bundle bundle = getArguments();
        if(bundle!=null && bundle.getString("PENDING ONLY")!=null) {
            // Only pending answers to be shown
            query = db.collection(QUESTIONS).whereEqualTo("numAns", 0).
                    whereEqualTo("subject", sessionManager.getTeacherDetails().getSubject());
        }
        else {

            if (queryString != null) {
                //Toast.makeText(getContext(), "QUERY: ALL", Toast.LENGTH_SHORT).show();
                Student student = sessionManager.getStudentDetails();
                // TODO: Add "standard" field in student: DONE
                query = db.collection(QUESTIONS).whereEqualTo("standard", student.getStandard()).whereEqualTo("subject", queryString);
            } else {
                if (sessionManager.getUserType().equals(STUDENTS)) {
                    //Toast.makeText(getContext(), "QUERY: STUDENT", Toast.LENGTH_SHORT).show();

                    Student student = sessionManager.getStudentDetails();
                    query = db.collection(QUESTIONS).whereEqualTo("studentId", student.getStudentId());
                } else {
                    //Toast.makeText(getContext(), "QUERY: TEACHER", Toast.LENGTH_SHORT).show();
                    Teacher teacher = sessionManager.getTeacherDetails();
                    query = db.collection(QUESTIONS).whereEqualTo("subject", teacher.getSubject());
                }
            }
        }
        // Now we can query the "Questions" collection

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Fetching your questions...");
        progressDialog.show();

        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressDialog.dismiss();
                        if(!task.isSuccessful() || task.getResult().isEmpty()) {
                            Toast.makeText(getContext(), "No Questions found", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            questions = task.getResult().toObjects(Question.class);
                            //Toast.makeText(getContext(), "#Questions: " + questions.size(), Toast.LENGTH_SHORT).show();
                            // Sorting based on recent questions first
                            Collections.sort(questions, new Comparator<Question>() {
                                @Override
                                public int compare(Question o1, Question o2) {
                                    int yr1 = Integer.parseInt(o1.getYear()), yr2 = Integer.parseInt(o2.getYear());
                                    if(yr1!=yr2) return -(yr1-yr2);
                                    int mn1 = Integer.parseInt(o1.getMonth()), mn2 = Integer.parseInt(o2.getMonth());
                                    if(mn1!=mn2) return -(mn1-mn2);
                                    int dt1 = Integer.parseInt(o1.getDate()), dt2 = Integer.parseInt(o2.getDate());
                                    if(dt1!=dt2) return -(dt1-dt2);
                                    int ans1 = o1.getAnswers().size(), ans2 = o2.getAnswers().size();
                                    return -(ans1-ans2);
                                }
                            });

                            RecyclerView recyclerView = view.findViewById(R.id.rv_view_questions);
                            ViewQuestionsAdapter mAdapter = new ViewQuestionsAdapter(questions, getContext());
                            recyclerView.setAdapter(mAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
}