// scheduleFragment.java

package com.example.gatechmanager;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class scheduleFragment extends Fragment {

    private EditText titleEditText, timeEditText, locationEditText;
    private Button addButton, displayButton;
    private TextView scheduleTextView;

    private List<ClassModel> classList = new ArrayList<>();

    public scheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        titleEditText = view.findViewById(R.id.titleEditText);
        timeEditText = view.findViewById(R.id.timeEditText);
        locationEditText = view.findViewById(R.id.locationEditText);
        addButton = view.findViewById(R.id.addButton);
        displayButton = view.findViewById(R.id.displayButton);
        scheduleTextView = view.findViewById(R.id.scheduleTextView);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClass();
            }
        });

        displayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayClasses();
            }
        });

        return view;
    }

    private void addClass() {
        String title = titleEditText.getText().toString();
        String time = timeEditText.getText().toString();
        String location = locationEditText.getText().toString();

        if (!title.isEmpty() && !time.isEmpty() && !location.isEmpty()) {
            ClassModel newClass = new ClassModel(title, time, location);
            classList.add(newClass);

            // Clear input fields after adding a class
            titleEditText.getText().clear();
            timeEditText.getText().clear();
            locationEditText.getText().clear();
        }
    }

    private void displayClasses() {
        StringBuilder scheduleText = new StringBuilder();
        for (ClassModel classModel : classList) {
            scheduleText.append("Title: ").append(classModel.getTitle())
                    .append(", Time: ").append(classModel.getTime())
                    .append(", Location: ").append(classModel.getLocation())
                    .append("\n");
        }
        scheduleTextView.setText(scheduleText.toString());
    }

    // ClassModel is included here in the same file
    public class ClassModel {
        private String title;
        private String time;
        private String location;

        public ClassModel(String title, String time, String location) {
            this.title = title;
            this.time = time;
            this.location = location;
        }

        public String getTitle() {
            return title;
        }

        public String getTime() {
            return time;
        }

        public String getLocation() {
            return location;
        }
    }
}