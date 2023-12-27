package com.softitbd.diuquestionbank.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.button.MaterialButton;
import com.softitbd.diuquestionbank.PreferencesUtils;
import com.softitbd.diuquestionbank.R;
import com.softitbd.diuquestionbank.Utils;


public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Find the MaterialButton by its ID
        Button btnExamQuestion = view.findViewById(R.id.btnExamQuestion);
        Button btnELibrary = view.findViewById(R.id.btnELibrary);
        Button btnMySaves = view.findViewById(R.id.btnMySaves);

        // Set the click listener programmatically
        btnExamQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onExamQuestionButtonClick(v);
            }
        });
        btnELibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onELibraryButtonClick(v);
            }
        });
        btnMySaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMySavesButtonClick(v);
            }
        });

        return view;
    }
    public void onExamQuestionButtonClick(View view) {
        Fragment examQuestionFragment = new ExamQuestionFragment();
        replaceFragment(examQuestionFragment);
    }
    public void onELibraryButtonClick(View view) {
        Fragment eLibraryFragment = new ELibraryFragment();
        replaceFragment(eLibraryFragment);
    }
    public void onMySavesButtonClick(View view) {
        if (PreferencesUtils.getUserStatus(getContext()).equals("active")){
            Fragment mySavesFragment = new MySavesFragment();
            replaceFragment(mySavesFragment);
        }else {
            Utils.showSubscriptionDialog(getContext(),"Subscribe to access our premium features", new Runnable() {
                @Override
                public void run() {
                    // Call your subscription function here
                    Utils.Subscribe(getContext(),2);
                }
            });
        }

    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}