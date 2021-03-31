package com.example.edumantradoubts.student.ui.home;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edumantradoubts.R;
import com.example.edumantradoubts.Structure.Announcement;
import com.example.edumantradoubts.Structure.Student;
import com.example.edumantradoubts.adapters.AnnouncementAdapter;
import com.example.edumantradoubts.loginsignup.LoginActivity;
import com.example.edumantradoubts.sharedpreferences.SessionManager;
import com.example.edumantradoubts.student.SelectActivity;
import com.example.edumantradoubts.student.TechnicalDoubtsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment {
    private TextView maths, science, sst, technical, greeting, welcome;
    public static String MATHS = "Maths", SCIENCE = "Science", SST = "SST", TECHNICAL = "Technical";
    private String subject;

    FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        db = FirebaseFirestore.getInstance();

        // Current student logged in
        SessionManager mManager = new SessionManager(root.getContext());
        Student student = mManager.getStudentDetails();

        maths = root.findViewById(R.id.maths);
        science = root.findViewById(R.id.science);
        sst = root.findViewById(R.id.sst);
        technical = root.findViewById(R.id.technical);
        welcome = root.findViewById(R.id.welcome_name);
        greeting = root.findViewById(R.id.greeting);

        welcome.setText("Welcome " + student.getName());

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if(hour>=16 && hour<=23) {
            greeting.setText("Good Evening");
            greeting.setTextColor(getResources().getColor(R.color.evening));
        }
        else if(hour>=12 && hour<16) {
            greeting.setText("Good Afternoon");
            greeting.setTextColor(getResources().getColor(R.color.afternoon));
        }
        else {
            greeting.setText("Good Morning");
            greeting.setTextColor(getResources().getColor(R.color.teal_200));
        }

        db.collection("Announcements")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            List<Announcement> announcements = task.getResult().toObjects(Announcement.class);
                            Collections.sort(announcements, new Comparator<Announcement>() {
                                @Override
                                public int compare(Announcement o1, Announcement o2) {
                                    String[] date1 = o1.getDate().split("/");
                                    String[] date2 = o2.getDate().split("/");
                                    int dt1=Integer.parseInt(date1[0]), dt2=Integer.parseInt(date2[0]), mn1 = Integer.parseInt(date1[1]), mn2= Integer.parseInt(date2[1]), yr1=Integer.parseInt(date1[2]), yr2=Integer.parseInt(date2[2]);
                                    if(yr1!=yr2) return -(yr1-yr2);
                                    if(mn1!=mn2) return -(mn1-mn2);
                                    if(dt1!=dt2) return -(dt1-dt2);
                                    return 1;

                                }
                            });
                            RecyclerView rv = root.findViewById(R.id.rv_announcement);
                            AnnouncementAdapter mAdapter = new AnnouncementAdapter(getContext(), announcements);
                            rv.setAdapter(mAdapter);
                            rv.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                    }
                });

        maths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectSubject(R.id.maths);
            }
        });
        science.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectSubject(R.id.science);
            }
        });
        sst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectSubject(R.id.sst);
            }
        });
        technical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectSubject(R.id.technical);
            }
        });
        return root;

    }

    @SuppressLint("NonConstantResourceId")
    public void onSelectSubject(int id) {
        switch(id) {
            case R.id.maths:
                subject = MATHS;
                break;
            case R.id.science:
                subject = SCIENCE;
                break;
            case R.id.sst:
                subject = SST;
                break;
            case R.id.technical:
                subject = TECHNICAL;
                break;
        }

        // For School subjects, they can ask doubts
        if(subject.equals(MATHS) || subject.equals(SCIENCE) || subject.equals(SST)) {
            Intent in = new Intent(getContext(), SelectActivity.class);
            in.putExtra("Subject Name", subject);
            getActivity().startActivity(in);
            return;
        }

        // For Technical doubts, there will be pre-written question and answers which will be useful
        // TODO: Add Technical Activity: DONE

        Intent in = new Intent(getContext(), TechnicalDoubtsActivity.class);
        startActivity(in);

    }


}