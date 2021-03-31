package com.example.edumantradoubts.teacher;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.edumantradoubts.R;
import com.example.edumantradoubts.questionsandanswers.ViewQuestionsFragment;


public class PendingFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pending, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = new Bundle();
        ViewQuestionsFragment frag = new ViewQuestionsFragment();
        bundle.putString("PENDING ONLY", "OK");
        frag.setArguments(bundle);
        ((AppCompatActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.pending, frag).commit();

    }
}