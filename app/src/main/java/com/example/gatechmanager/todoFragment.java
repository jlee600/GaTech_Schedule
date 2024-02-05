package com.example.gatechmanager;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link todoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class todoFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment todoFragment.
     */
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

    //TODO attributes: exam, assignment, general
    //TODO delete, edit button
    //TODO checklist box: if checked, then deleted
    //TODO separate todo list for the exam and the assignment
    //TODO sorting options
    private class CustomArrayAdapter extends ArrayAdapter<String> {
        public CustomArrayAdapter(Context context, List<String> objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Declare convertView as final
            final View finalConvertView;

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_item, parent, false);
            }
            // Initialize finalConvertView with convertView
            finalConvertView = convertView;

            CheckBox checkBox = convertView.findViewById(R.id.checkBox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // Handle checkbox checked
                        // For example, you can hide the item
                        finalConvertView.setVisibility(View.GONE);
                    } else {
                        // Handle checkbox unchecked
                        finalConvertView.setVisibility(View.VISIBLE);
                    }
                }
            });
            TextView textView = convertView.findViewById(R.id.textView);
            textView.setText(getItem(position));

            return convertView;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        listView = view.findViewById(R.id.listView);
        button = view.findViewById(R.id.button);
        editTextText = view.findViewById(R.id.editTextText);

        // Initialize listView and set its custom adapter
        items = new ArrayList<>();
        itemsAdapter = new CustomArrayAdapter(getContext(), items);  // Initialize itemsAdapter
        listView.setAdapter(itemsAdapter);

        // Initialize button and set its click listener
        button.setOnClickListener(v -> addItem(v));

        // Set up the ListView listener
        setUpListViewListener();

        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}