package com.example.edumantradoubts.dispimagesanswers;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edumantradoubts.R;
import com.example.edumantradoubts.adapters.ImagesFromStorageAdapter;

public class ImagesFromStorageFragment extends Fragment {

    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_images_from_storage, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        assert bundle != null;
        String[] paths = bundle.getStringArray("IMAGE PATH");
        if(paths==null) {
            Toast.makeText(getContext(), "Cannot load question images", Toast.LENGTH_LONG).show();
            return;
        }

        ((TextView) view.findViewById(R.id.num_images)).setText(paths.length + " Images attached");

        // Have to create recycler view to view images...
        mRecyclerView = view.findViewById(R.id.rv_images_from_storage);
        ImagesFromStorageAdapter mAdapter = new ImagesFromStorageAdapter(paths, getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}