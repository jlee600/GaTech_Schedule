package com.example.gatechmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class scheduleFragment extends Fragment {

    private EditText titleEditText, startTimeEditText, endTimeEditText, locationEditText;
    private Button addButton;
    private RecyclerView scheduleRecyclerView;
    private ScheduleAdapter scheduleAdapter;

    private List<ClassModel> classList = new ArrayList<>();

    public scheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        titleEditText = view.findViewById(R.id.titleEditText);
        startTimeEditText = view.findViewById(R.id.startTimeEditText);
        endTimeEditText = view.findViewById(R.id.endTimeEditText);
        locationEditText = view.findViewById(R.id.locationEditText);
        addButton = view.findViewById(R.id.addButton);
        scheduleRecyclerView = view.findViewById(R.id.scheduleRecyclerView);

        scheduleAdapter = new ScheduleAdapter(classList);
        scheduleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        scheduleRecyclerView.setAdapter(scheduleAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClass();
            }
        });

        return view;
    }

    private void addClass() {
        String title = titleEditText.getText().toString();
        String startTime = startTimeEditText.getText().toString();
        String endTime = endTimeEditText.getText().toString();
        String location = locationEditText.getText().toString();

        if (!title.isEmpty() && !startTime.isEmpty() && !endTime.isEmpty() && !location.isEmpty()) {
            ClassModel newClass = new ClassModel(title, startTime, endTime, location);
            classList.add(newClass);

            // Clear input fields after adding a class
            titleEditText.getText().clear();
            startTimeEditText.getText().clear();
            endTimeEditText.getText().clear();
            locationEditText.getText().clear();

            // Notify the adapter that the data set has changed
            scheduleAdapter.notifyDataSetChanged();
        }
    }

    public class ClassModel {
        private String title;
        private String startTime;
        private String endTime;
        private String location;

        public ClassModel(String title, String startTime, String endTime, String location) {
            this.title = title;
            this.startTime = startTime;
            this.endTime = endTime;
            this.location = location;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }

    public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

        private List<ClassModel> classList;

        public ScheduleAdapter(List<ClassModel> classList) {
            this.classList = classList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ClassModel classModel = classList.get(position);

            holder.titleTextView.setText(classModel.getTitle());
            holder.timeTextView.setText(String.format("%s - %s", classModel.getStartTime(), classModel.getEndTime()));
            holder.locationTextView.setText(classModel.getLocation());

            holder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Implement edit functionality here
                    ClassModel classModel = classList.get(position);
                    showEditDialog(v.getContext(), classModel);
                }
            });

            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    classList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return classList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView titleTextView;
            TextView timeTextView;
            TextView locationTextView;
            Button editButton;
            Button deleteButton;

            public ViewHolder(View itemView) {
                super(itemView);
                titleTextView = itemView.findViewById(R.id.titleTextView);
                timeTextView = itemView.findViewById(R.id.timeTextView);
                locationTextView = itemView.findViewById(R.id.locationTextView);
                editButton = itemView.findViewById(R.id.editButton);
                deleteButton = itemView.findViewById(R.id.deleteButton);
            }
        }

        private void showEditDialog(Context context, ClassModel classModel) {
            View dialogView = LayoutInflater.from(context).inflate(R.layout.edit_class_dialog, null);

            EditText editTitleEditText = dialogView.findViewById(R.id.editTitleEditText);
            EditText editStartTimeEditText = dialogView.findViewById(R.id.editStartTimeEditText);
            EditText editEndTimeEditText = dialogView.findViewById(R.id.editEndTimeEditText);
            EditText editLocationEditText = dialogView.findViewById(R.id.editLocationEditText);

            editTitleEditText.setText(classModel.getTitle());
            editStartTimeEditText.setText(classModel.getStartTime());
            editEndTimeEditText.setText(classModel.getEndTime());
            editLocationEditText.setText(classModel.getLocation());

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(dialogView)
                    .setTitle("Edit Class")
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String editedTitle = editTitleEditText.getText().toString();
                            String editedStartTime = editStartTimeEditText.getText().toString();
                            String editedEndTime = editEndTimeEditText.getText().toString();
                            String editedLocation = editLocationEditText.getText().toString();

                            classModel.setTitle(editedTitle);
                            classModel.setStartTime(editedStartTime);
                            classModel.setEndTime(editedEndTime);
                            classModel.setLocation(editedLocation);

                            notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("Cancel", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}






