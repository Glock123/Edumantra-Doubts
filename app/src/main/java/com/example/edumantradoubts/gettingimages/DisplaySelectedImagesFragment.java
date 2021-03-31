package com.example.edumantradoubts.gettingimages;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.edumantradoubts.R;
import com.example.edumantradoubts.adapters.DisplaySelectedImagesAdapter;

public class DisplaySelectedImagesFragment extends Fragment {

    public DisplaySelectedImagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_display_selected_images, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView mRecyclerView = view.findViewById(R.id.uploaded_images_recycler_view);
        DisplaySelectedImagesAdapter mAdapter = new DisplaySelectedImagesAdapter(getContext());

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter.notifyItemInserted(0);
//        mAdapter.notifyItemRangeChanged(0, size+1);

    }
}