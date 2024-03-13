package com.example.gatechmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.inputmethod.InputMethodManager;
import java.util.ArrayList;
import java.util.List;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class scheduleFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<scheduleFragment.ScheduleItem> items;
    private ArrayList<scheduleFragment.ScheduleItem> originalItems;
    private ArrayAdapter<scheduleFragment.ScheduleItem> itemsAdapter;
    private ListView listView;

    public scheduleFragment() {
        // Required empty public constructor
    }

    public class ScheduleItem {
        private String description;
        private String course;
        private String date;
        private String time;
        private String location;
        private String professor;

        public ScheduleItem(String description) {
            this.description = description;
        }

        // Getter and setter methods for all fields
        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCourse() {
            return course;
        }

        public void setCourse(String course) {
            this.course = course;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getLocation() {
            return location;
        }
        public void setLocation(String location) {
            this.location = location;
        }
        public String getProfessor() {
            return professor;
        }
        public void setProfessor(String professor) {
            this.professor = professor;
        }
    }

    public static scheduleFragment newInstance(String param1, String param2) {
        scheduleFragment fragment = new scheduleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = new ArrayList<>();
        originalItems = new ArrayList<>(items);
        itemsAdapter = new CustomArrayAdapter(getContext(), items);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        items = new ArrayList<>();
        itemsAdapter = new CustomArrayAdapter(getContext(), items);
        listView = view.findViewById(R.id.listView);
        listView.setAdapter(itemsAdapter);

        FloatingActionButton addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> addItem(v));

        setUpListViewListener();
        setUpListViewClickListener();
        setHasOptionsMenu(true);

        return view;
    }

    private void setUpListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = getContext().getApplicationContext();
                Toast.makeText(context, "Removed from Schedule", Toast.LENGTH_LONG).show();
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void addItem(View v) {
        Context context = getContext().getApplicationContext();
        showEditDialog(-1);

        // Hide the keyboard
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private class CustomArrayAdapter extends ArrayAdapter<scheduleFragment.ScheduleItem> {
        private List<scheduleFragment.ScheduleItem> masterList;

        public CustomArrayAdapter(Context context, List<scheduleFragment.ScheduleItem> objects) {
            super(context, R.layout.custom_schedule_item, objects);
            this.masterList = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_schedule_item, parent, false);
            }

            scheduleFragment.ScheduleItem currentItem = getItem(position);

            TextView textViewDescription = convertView.findViewById(R.id.textViewDescription);
            textViewDescription.setText(currentItem.getDescription());

            TextView textViewCourse = convertView.findViewById(R.id.textViewCourse);
            TextView textViewDate = convertView.findViewById(R.id.textViewDate);
            TextView textViewTime = convertView.findViewById(R.id.textViewTime);
            TextView textViewLocation = convertView.findViewById(R.id.textViewLocation);
            TextView textViewProfessor = convertView.findViewById(R.id.textViewProfessor);

            textViewProfessor.setText("Professor: " + currentItem.getProfessor());
            textViewCourse.setText("Section: " + currentItem.getCourse());
            textViewDate.setText("Date: " + currentItem.getDate());
            textViewTime.setText("Time: " + currentItem.getTime());
            textViewLocation.setText("Location: " + currentItem.getLocation());

            // Edit and Remove buttons
            ImageButton btnEdit = convertView.findViewById(R.id.btnEdit);
            ImageButton btnRemove = convertView.findViewById(R.id.btnRemove);
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEditDialog(position);
                }
            });
            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(position);
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            return convertView;
        }

        private void removeItem(int position) {
            scheduleFragment.ScheduleItem removedItem = masterList.get(position);
            masterList.remove(position);
            notifyDataSetChanged();
            originalItems.remove(removedItem);
        }
    }

    private void setUpListViewClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showEditDialog(position);
            }
        });
    }

    private void showEditDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter Details");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final scheduleFragment.ScheduleItem currentItem;
        if (position == -1) {
            currentItem = new ScheduleItem("");
        } else {
            currentItem = items.get(position);
        }

        final EditText descriptionEditText = new EditText(getContext());
        descriptionEditText.setHint("Course Name");
        descriptionEditText.setText(currentItem.getDescription());
        layout.addView(descriptionEditText);

        final EditText profEditText = new EditText(getContext());
        profEditText.setHint("Professor");
        profEditText.setText(currentItem.getProfessor());
        layout.addView(profEditText);

        final EditText courseEditText = new EditText(getContext());
        courseEditText.setHint("Section");
        courseEditText.setText(currentItem.getCourse());
        layout.addView(courseEditText);

        final EditText dateEditText = new EditText(getContext());
        dateEditText.setHint("Date (MW / TR)");
        dateEditText.setText(currentItem.getDate());
        layout.addView(dateEditText);

        final EditText timeEditText = new EditText(getContext());
        timeEditText.setHint("Time");
        timeEditText.setText(currentItem.getTime());
        layout.addView(timeEditText);

        final EditText locationEditText = new EditText(getContext());
        locationEditText.setHint("Location");
        locationEditText.setText(currentItem.getLocation());
        layout.addView(locationEditText);

        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Context context = getContext().getApplicationContext();
                String regex = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$";
                String regexProfessor = "^[A-Za-z]+$";
                String regexDate = "^[MTWRF]+$";
                boolean isReady = true;
                if (profEditText.getText().toString().isEmpty() || courseEditText.getText().toString().isEmpty()|| dateEditText.getText().toString().isEmpty()
                        || timeEditText.getText().toString().isEmpty() || locationEditText.getText().toString().isEmpty()) {
                    Toast.makeText(context, "All entries must be non-null", Toast.LENGTH_LONG).show();
                    showEditDialog(position);
                    isReady = false;
                } else {
                    currentItem.setDescription(descriptionEditText.getText().toString());
                    if (!profEditText.getText().toString().matches(regexProfessor)) {
                        Toast.makeText(context, "Please enter a valid professor name (Pedro)", Toast.LENGTH_LONG).show();
                        isReady = false;
                        showEditDialog(position);
                    } else {
                        currentItem.setProfessor(profEditText.getText().toString());
                    }
                    currentItem.setCourse(courseEditText.getText().toString());
                    if (!dateEditText.getText().toString().matches(regexDate)) {
                        Toast.makeText(context, "Please enter a valid day (MTWRF)", Toast.LENGTH_LONG).show();
                        isReady = false;
                        showEditDialog(position);
                    } else {
                        currentItem.setDate(dateEditText.getText().toString());
                    }
                    currentItem.setLocation(locationEditText.getText().toString());

                    if (!timeEditText.getText().toString().matches(regex)) {
                        Toast.makeText(context, "Please enter a valid time (HH:MM)", Toast.LENGTH_LONG).show();
                        isReady = false;
                        showEditDialog(position);
                    } else {
                        currentItem.setTime(timeEditText.getText().toString());
                    }
                }
                if (position == -1 && isReady) {
                    itemsAdapter.add(currentItem);
                }

                itemsAdapter.notifyDataSetChanged();
            }
        });
        builder.show();
    }
}

