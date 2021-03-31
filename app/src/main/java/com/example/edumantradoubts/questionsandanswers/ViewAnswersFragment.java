package com.example.edumantradoubts.questionsandanswers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edumantradoubts.R;
import com.example.edumantradoubts.adapters.ViewAnswersAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewAnswersFragment extends Fragment {

    public static ArrayList<String> ansPathsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_answers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        String[] ansPaths = bundle.getStringArray("ANSWERS PATH");
        if(ansPaths==null) {
            Toast.makeText(getContext(), "Cannot load Answers", Toast.LENGTH_LONG).show();
            return;
        }

        TextView labelForNoAnswers = view.findViewById(R.id.label_for_no_answers);

        if(ansPaths.length>0) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
            labelForNoAnswers.setLayoutParams(lp);
        }

        ansPathsList = new ArrayList<String>(Arrays.asList(ansPaths));

        RecyclerView mRecyclerView = view.findViewById(R.id.rv_view_answers);
        ViewAnswersAdapter mAdapter = new ViewAnswersAdapter(ansPathsList, getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }
}