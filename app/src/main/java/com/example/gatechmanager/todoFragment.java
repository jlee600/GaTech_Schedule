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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link todoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class todoFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView listView;
    private EditText editTextText;
    private Button button;

    public todoFragment() {
        // Required empty public constructor
    }

    public static todoFragment newInstance(String param1, String param2) {
        todoFragment fragment = new todoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void setUpListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = getContext().getApplicationContext();
                Toast.makeText(context, "Removed from Todo list", Toast.LENGTH_LONG).show();
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void addItem(View v) {
        Context context = getContext().getApplicationContext();
        String itemText = editTextText.getText().toString();
        if (!itemText.isEmpty()) {
            itemsAdapter.add(itemText);
            editTextText.setText("");

            // Hide the keyboard
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        } else {
            Toast.makeText(context, "Please Enter Text...", Toast.LENGTH_LONG).show();
        }
    }

    private class CustomArrayAdapter extends ArrayAdapter<String> {
        public CustomArrayAdapter(Context context, List<String> objects) {
            super(context, R.layout.custom_list_item, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View finalConvertView;

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_item, parent, false);
            }

            finalConvertView = convertView;

            CheckBox checkBox = convertView.findViewById(R.id.checkBox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        finalConvertView.setVisibility(View.GONE);
                    } else {
                        finalConvertView.setVisibility(View.VISIBLE);
                    }
                }
            });

            TextView textViewDescription = convertView.findViewById(R.id.textViewDescription);
            textViewDescription.setText(getItem(position));

            // Additional TextViews for Attribute, Course, Date, Time, and Location
            TextView textViewAttribute = convertView.findViewById(R.id.textViewAttribute);
            TextView textViewCourse = convertView.findViewById(R.id.textViewCourse);
            TextView textViewDate = convertView.findViewById(R.id.textViewDate);
            TextView textViewTime = convertView.findViewById(R.id.textViewTime);
            TextView textViewLocation = convertView.findViewById(R.id.textViewLocation);

            // Set default values to N/A
            textViewAttribute.setText("Attribute: N/A");
            textViewCourse.setText("Course: N/A");
            textViewDate.setText("Date: N/A");
            textViewTime.setText("Time: N/A");
            textViewLocation.setText("Location: N/A");

            // Add click listener for editing
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEditDialog(position);
                }
            });

            return convertView;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        listView = view.findViewById(R.id.listView);
        button = view.findViewById(R.id.button);
        editTextText = view.findViewById(R.id.editTextText);

        items = new ArrayList<>();
        itemsAdapter = new CustomArrayAdapter(getContext(), items);
        listView.setAdapter(itemsAdapter);

        button.setOnClickListener(v -> addItem(v));

        setUpListViewListener();
        setUpListViewClickListener();

        return view;
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

        // Create a layout for the input dialog (you can use a custom layout if needed)
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        // Add EditTexts for Attribute, Course, Date, Time, and Location
        final EditText attributeEditText = new EditText(getContext());
        attributeEditText.setHint("Attribute");
        layout.addView(attributeEditText);

        final EditText courseEditText = new EditText(getContext());
        courseEditText.setHint("Course");
        layout.addView(courseEditText);

        final EditText dateEditText = new EditText(getContext());
        dateEditText.setHint("Date");
        layout.addView(dateEditText);

        final EditText timeEditText = new EditText(getContext());
        timeEditText.setHint("Time");
        layout.addView(timeEditText);

        final EditText locationEditText = new EditText(getContext());
        locationEditText.setHint("Location");
        layout.addView(locationEditText);

        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve values from EditTexts
                String attribute = attributeEditText.getText().toString();
                String course = courseEditText.getText().toString();
                String date = dateEditText.getText().toString();
                String time = timeEditText.getText().toString();
                String location = locationEditText.getText().toString();

                // Get the corresponding item view
                View convertView = listView.getChildAt(position);

                // Set values to TextViews
                TextView textViewAttribute = convertView.findViewById(R.id.textViewAttribute);
                TextView textViewCourse = convertView.findViewById(R.id.textViewCourse);
                TextView textViewDate = convertView.findViewById(R.id.textViewDate);
                TextView textViewTime = convertView.findViewById(R.id.textViewTime);
                TextView textViewLocation = convertView.findViewById(R.id.textViewLocation);

                textViewAttribute.setText("Attribute: " + attribute);
                textViewCourse.setText("Course: " + course);
                textViewDate.setText("Date: " + date);
                textViewTime.setText("Time: " + time);
                textViewLocation.setText("Location: " + location);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
