package com.example.mylogin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageButton mathFragmentButton = view.findViewById(R.id.mathButton);
        ImageButton historyFragmentButton = view.findViewById(R.id.historyButton);

        mathFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Replace the current fragment with MathGroupFragment
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new MathGroupFragment());
                //transaction.addToBackStack(null); // Optional, for back navigation
                transaction.commit();
            }
        });

        historyFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Replace the current fragment with HistoryGroupFragment
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new HistoryGroupFragment());
                //transaction.addToBackStack(null); // Optional, for back navigation
                transaction.commit();
            }
        });

        return view;
    }
}
