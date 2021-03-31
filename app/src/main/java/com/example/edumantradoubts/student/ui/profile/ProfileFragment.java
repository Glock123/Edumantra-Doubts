package com.example.edumantradoubts.student.ui.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.edumantradoubts.R;
import com.example.edumantradoubts.Structure.Student;
import com.example.edumantradoubts.Structure.Teacher;
import com.example.edumantradoubts.loginsignup.LoginActivity;
import com.example.edumantradoubts.sharedpreferences.SessionManager;
import com.google.api.Distribution;

import java.util.Objects;

import static com.example.edumantradoubts.loginsignup.SignUpTeacherActivity.TEACHERS;

public class ProfileFragment extends Fragment {

    private int pushed=0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView name = view.findViewById(R.id.profile_name);
        TextView userType = view.findViewById(R.id.profile_user_type);
        TextView emailId = view.findViewById(R.id.profile_email_id);
        TextView phoneNo = view.findViewById(R.id.profile_phone_no);
        TextView typeOfId = view.findViewById(R.id.profile_type_of_id); // = userType Id:
        TextView userId = view.findViewById(R.id.profile_user_id);
        LinearLayout aboutDeveloper = view.findViewById(R.id.profile_about_developer);
        ImageView arrow = view.findViewById(R.id.profile_arrow);
        TextView developerInfo = view.findViewById(R.id.profile_developer_info);
        LinearLayout logout = view.findViewById(R.id.profile_logout);

        SessionManager mManager = new SessionManager(getContext());

        if(mManager.getUserType().equals(TEACHERS)) {
            Teacher teacher = mManager.getTeacherDetails();
            name.setText(teacher.getName());
            userType.setText("Teacher");;
            emailId.setText(teacher.getEmailId());
            phoneNo.setText(teacher.getPhoneNo());
            typeOfId.setText("Teacher Id:");
            userId.setText(teacher.getTeacherId());
        }
        else {
            Student student = mManager.getStudentDetails();
            name.setText(student.getName());
            userType.setText("Student");;
            emailId.setText(student.getEmailId());
            phoneNo.setText(student.getPhoneno());
            typeOfId.setText("Student Id:");
            userId.setText(student.getStudentId());
        }

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        developerInfo.setLayoutParams(lp);

        aboutDeveloper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushed++;
                if((pushed%2)==1) {
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    developerInfo.setLayoutParams(lp);
                    arrow.setImageResource(R.drawable.ic_arrow_up);
                }
                else {
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                    developerInfo.setLayoutParams(lp);
                    arrow.setImageResource(R.drawable.ic_arrow_down);
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Want to log out?");
                alertDialog.setMessage("Have to login with valid credentials again");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mManager.removeSession();
                        Intent in = new Intent(getContext(), LoginActivity.class);
                        startActivity(in);
                        requireActivity().finish();
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }
        });

    }
}