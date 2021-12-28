package com.example.airlinepacker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link checklist#newInstance} factory method to
 * create an instance of this fragment.
 */
public class checklist extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FINAL_ITEMS = "items";
    private static final String FINAL_VALUE = "value";
    private static final String FINAL_WEIGHT = "weight";

    // TODO: Rename and change types of parameters
    private ArrayList<String> mParam1;

    private ArrayList<String> finalItems;
    int valueResult;
    int weightResult;
    private CheckBox item;
    private TableLayout tL;
    private TableRow tR;
    private TextView tV;

    public checklist() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param finalItems Parameter 1.
     * @return A new instance of fragment checklist.
     */
    // TODO: Rename and change types and number of parameters
    public static checklist newInstance(ArrayList<String> finalItems) {
        checklist fragment = new checklist();
        Bundle args = new Bundle();
        args.putStringArrayList(FINAL_ITEMS, finalItems);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getStringArrayList(FINAL_ITEMS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.checklist, container, false);
        Bundle itemBundle = this.getArguments();
        if (itemBundle != null) {
            finalItems = (ArrayList<String>) itemBundle.getStringArrayList(FINAL_ITEMS);
            valueResult = (int) itemBundle.getInt(FINAL_VALUE);
            weightResult = (int) itemBundle.getInt(FINAL_WEIGHT);
        }
        tL = (TableLayout) view.findViewById(R.id.tableLayout);

        for (int i = 0; i < finalItems.size(); i++) {
            String str = finalItems.get(i);
            item = new CheckBox(getContext());
            item.setText(str);
            item.setGravity(Gravity.CENTER | Gravity.START);
            tR = new TableRow(getContext());
            tR.addView(item);
            tL.addView(tR);
        }

        tV = new TextView(getContext());
        tV.setText("Total value: " + valueResult);
        tV.setGravity(Gravity.CENTER | Gravity.START);
        tR = new TableRow(getContext());
        tR.addView(tV);
        tL.addView(tR);

        tV = new TextView(getContext());
        tV.setText("Total weight: " + weightResult);
        tV.setGravity(Gravity.CENTER | Gravity.START);
        tR = new TableRow(getContext());
        tR.addView(tV);
        tL.addView(tR);


        FloatingActionButton home = view.findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_checklist_to_home22);
            }
        });

        return view;
    }
}