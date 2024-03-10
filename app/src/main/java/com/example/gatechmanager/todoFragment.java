package com.example.gatechmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class todoFragment extends Fragment {
    public static final String[] VALID_ATTRIBUTES = {"Exam", "HW", "General"};
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<TodoItem> items;
    private ArrayList<TodoItem> originalItems;
    private ArrayAdapter<TodoItem> itemsAdapter;
    private ListView listView;
    private EditText editTextText;
    private Button button;

    public todoFragment() {
    }

    public class TodoItem {
        private String description;
        private String attribute;
        private String course;
        private String date;
        private String time;
        private String location;
        private boolean completed;

        public TodoItem(String description) {
            this.description = description;
            this.completed = false;
        }

        // Getter and setter methods for all fields
        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAttribute() {
            return attribute;
        }

        public void setAttribute(String attribute) {
            this.attribute = attribute;
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

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }
    }

    public static todoFragment newInstance(String param1, String param2) {
        todoFragment fragment = new todoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = new ArrayList<>();
        originalItems = new ArrayList<>(items);
        itemsAdapter = new CustomArrayAdapter(getContext(), items);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        listView = view.findViewById(R.id.listView);
        button = view.findViewById(R.id.button);
        editTextText = view.findViewById(R.id.editTextText);

        listView.setAdapter(itemsAdapter);
        button.setOnClickListener(v -> addItem(v));

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
            TodoItem newItem = new TodoItem(itemText);
            itemsAdapter.add(newItem);
            originalItems.add(newItem);
            editTextText.setText("");

            // Show the edit dialog for the newly added item
            showEditDialog(itemsAdapter.getCount() - 1);

            // Hide the keyboard
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        } else {
            Toast.makeText(context, "Please Enter Text...", Toast.LENGTH_LONG).show();
        }
    }

    private class CustomArrayAdapter extends ArrayAdapter<TodoItem> {
        private List<TodoItem> masterList;
        private Set<Integer> checkedPositions;

        public CustomArrayAdapter(Context context, List<TodoItem> objects) {
            super(context, R.layout.custom_list_item, objects);
            this.masterList = objects;
            this.checkedPositions = new HashSet<>();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_item, parent, false);
            }

            TodoItem currentItem = getItem(position);

            CheckBox checkBox = convertView.findViewById(R.id.checkBox);
            checkBox.setOnCheckedChangeListener(null); // Remove previous listener

            // Set checked state and listener
            checkBox.setChecked(currentItem.isCompleted());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    currentItem.setCompleted(isChecked);
                    notifyDataSetChanged();
                }
            });

            TextView textViewDescription = convertView.findViewById(R.id.textViewDescription);
            textViewDescription.setText(currentItem.getDescription());

            TextView textViewAttribute = convertView.findViewById(R.id.textViewAttribute);
            TextView textViewCourse = convertView.findViewById(R.id.textViewCourse);
            TextView textViewDate = convertView.findViewById(R.id.textViewDate);
            TextView textViewTime = convertView.findViewById(R.id.textViewTime);
            TextView textViewLocation = convertView.findViewById(R.id.textViewLocation);

            textViewAttribute.setText("Type: " + currentItem.getAttribute());
            textViewCourse.setText("Course: " + currentItem.getCourse());
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
            TodoItem removedItem = masterList.get(position);
            masterList.remove(position);
            notifyDataSetChanged();
            originalItems.remove(removedItem);
        }

        public void clearCheckedPositions() {
            checkedPositions.clear();
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

        final TodoItem currentItem = items.get(position);

        final EditText descriptionEditText = new EditText(getContext());
        descriptionEditText.setHint("Description");
        descriptionEditText.setText(currentItem.getDescription());
        layout.addView(descriptionEditText);

        final Spinner attributeSpinner = new Spinner(getContext());
        ArrayAdapter<String> attributeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, VALID_ATTRIBUTES);
        attributeSpinner.setAdapter(attributeAdapter);
        attributeSpinner.setSelection(attributeAdapter.getPosition(currentItem.getAttribute()));
        layout.addView(attributeSpinner);

        final EditText courseEditText = new EditText(getContext());
        courseEditText.setHint("Course");
        courseEditText.setText(currentItem.getCourse());
        layout.addView(courseEditText);

        final EditText dateEditText = new EditText(getContext());
        dateEditText.setHint("Date (MM/DD -> Jan/12)");
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
                currentItem.setDescription(descriptionEditText.getText().toString());
                currentItem.setAttribute(attributeSpinner.getSelectedItem().toString());
                currentItem.setCourse(courseEditText.getText().toString());
                currentItem.setDate(dateEditText.getText().toString());
                currentItem.setTime(timeEditText.getText().toString());
                currentItem.setLocation(locationEditText.getText().toString());

                itemsAdapter.notifyDataSetChanged();
            }
        });

        builder.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.todo_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int itemId = item.getItemId();

        if (itemId == R.id.menu_sort_by_course) {
            sortByCourse();
            return true;
        } else if (itemId == R.id.menu_sort_by_due_date) {
            sortByDueDate();
            return true;
        } else if (itemId == R.id.menu_sort_by_complete) {
            sortByCompletionStatus();
            return true;
        } else if (itemId == R.id.menu_sort_by_incomplete) {
            sortByIncompletionStatus();
            return true;
        } else if (itemId == R.id.menu_sort_by_exam) {
            sortByAttribute("Exam");
            return true;
        } else if (itemId == R.id.menu_sort_by_hw) {
            sortByAttribute("HW");
            return true;
        } else if (itemId == R.id.menu_sort_by_general) {
            sortByAttribute("General");
            return true;
        } else if (itemId == R.id.menu_reset) {
            resetList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void resetList() {
        // Clear any previous filters and display the entire list
        itemsAdapter.clear();
        itemsAdapter.addAll(new ArrayList<>(originalItems));
        itemsAdapter.notifyDataSetChanged();
    }

    private void sortByAttribute(String attribute) {
        List<TodoItem> filteredList = new ArrayList<>();

        for (TodoItem item : items) {
            if (item.getAttribute().equals(attribute)) {
                filteredList.add(item);
            }
        }

        // Update the adapter with the filtered list
        itemsAdapter.clear();
        itemsAdapter.addAll(filteredList);
        itemsAdapter.notifyDataSetChanged();
    }

    private void sortByCourse() {
        Collections.sort(items, new Comparator<TodoItem>() {
            @Override
            public int compare(TodoItem item1, TodoItem item2) {
                String course1 = item1.getCourse();
                String course2 = item2.getCourse();

                // Check for null or empty course names and treat them as "zzz" so they move down in alphabetical sort
                if ((course1 == null || course1.isEmpty()) && (course2 == null || course2.isEmpty())) {
                    return 0; // Both are effectively equal in sorting terms
                } else if (course1 == null || course1.isEmpty()) {
                    return 1; // Null/empty course names move down
                } else if (course2 == null || course2.isEmpty()) {
                    return -1; // Null/empty course names move down
                }

                // If neither course name is null/empty, proceed with normal string comparison
                return course1.compareToIgnoreCase(course2);
            }
        });
        itemsAdapter.notifyDataSetChanged();
    }

    private void sortByDueDate() {
        Collections.sort(items, new Comparator<TodoItem>() {
            @Override
            public int compare(TodoItem item1, TodoItem item2) {
                String date1 = item1.getDate();
                String date2 = item2.getDate();

                SimpleDateFormat format = new SimpleDateFormat("MMM/dd", Locale.US); // Make sure this matches your expected input format
                Date dateObj1, dateObj2;

                // Convert date1 to Date object, treat invalid date as "far future"
                try {
                    dateObj1 = (date1 != null && !date1.isEmpty()) ? format.parse(date1) : new Date(Long.MAX_VALUE);
                } catch (ParseException e) {
                    dateObj1 = new Date(Long.MAX_VALUE); // Set invalid dates to "far future"
                }

                // Convert date2 to Date object, treat invalid date as "far future"
                try {
                    dateObj2 = (date2 != null && !date2.isEmpty()) ? format.parse(date2) : new Date(Long.MAX_VALUE);
                } catch (ParseException e) {
                    dateObj2 = new Date(Long.MAX_VALUE); // Set invalid dates to "far future"
                }

                // Now compare the two Date objects
                return dateObj1.compareTo(dateObj2);
            }
        });
        itemsAdapter.notifyDataSetChanged();
    }

    private void sortByCompletionStatus() {
        List<TodoItem> completedList = new ArrayList<>();

        for (TodoItem item : items) {
            if (item.isCompleted()) {
                completedList.add(item);
            }
        }

        // Update the adapter with the sorted list
        itemsAdapter.clear();
        itemsAdapter.addAll(completedList);
        itemsAdapter.notifyDataSetChanged();
    }
    private void sortByIncompletionStatus() {
        List<TodoItem> incompleteList = new ArrayList<>();

        for (TodoItem item : items) {
            if (!item.isCompleted()) {
                incompleteList.add(item);
            }
        }

        // Update the adapter with the sorted list
        itemsAdapter.clear();
        itemsAdapter.addAll(incompleteList);
        itemsAdapter.notifyDataSetChanged();
    }
}

