 package com.example.airlinepacker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link enter_items#newInstance} factory method to
 * create an instance of this fragment.
 */
public class enter_items extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String AIRLINE = "airline";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;

    private HashMap<String, KeyValuePair<Integer, Integer>> items;
    private ArrayList<String> finalItems;
    private RadioGroup rG;
    private RadioButton item;

    int capacity;
    int valueResult;
    int weightResult;
    ArrayList<Integer> itemWeight;
    ArrayList<Integer> itemValue;
    ArrayList<String> itemNames;

    private String txtFileName;
    private File txtFile;
    private File storageDir;
    private FileOutputStream itemsFileOutputStream;
    private FileInputStream itemsFileInputStream;
    private String str;

    Bundle bundle;

    public enter_items() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param weight Parameter 1.
     * @return A new instance of fragment enter_items.
     */
    // TODO: Rename and change types and number of parameters
    public static enter_items newInstance(int weight) {
        enter_items fragment = new enter_items();
        Bundle args = new Bundle();
        args.putInt(AIRLINE, weight);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(AIRLINE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.enter_items, container, false);
        Bundle airlineBundle = this.getArguments();
        if (airlineBundle != null) {
            capacity = airlineBundle.getInt(AIRLINE);
        }
        bundle = new Bundle();
        rG = (RadioGroup) view.findViewById(R.id.radioGroup);
        items = new HashMap<>();
        finalItems = new ArrayList<>();
        itemWeight = new ArrayList<>();
        itemValue = new ArrayList<>();
        itemNames = new ArrayList<>();
        storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        txtFileName = "ITEM_DATA" + ".txt";
        txtFile = new File(storageDir, txtFileName);
        try {
            itemsFileOutputStream = new FileOutputStream(txtFile, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            itemsFileInputStream = new FileInputStream(txtFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            loadFile(storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        FloatingActionButton home = view.findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_enter_items2_to_home22);
            }
        });

        /*
        Add new user data
         */
        FloatingActionButton add = view.findViewById(R.id.addButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Enter a new item");
                LinearLayout inputs = new LinearLayout(getContext());
                inputs.setOrientation(LinearLayout.VERTICAL);
                EditText itemInput = new EditText(getContext());
                itemInput.setInputType(InputType.TYPE_CLASS_TEXT);
                itemInput.setHint("Enter item name...");
                inputs.addView(itemInput);
                EditText weightInput = new EditText(getContext());
                weightInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                weightInput.setHint("Enter weight (lb)...");
                inputs.addView(weightInput);
                EditText valueInput = new EditText(getContext());
                valueInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                valueInput.setHint("Enter value ($)...");
                inputs.addView(valueInput);
                builder.setView(inputs);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        items.put(itemInput.getText().toString(),
                                new KeyValuePair<>(Integer.parseInt(weightInput.getText().toString()),
                                Integer.parseInt(valueInput.getText().toString())));
                        itemNames.add(itemInput.getText().toString());
                        itemWeight.add(Integer.parseInt(weightInput.getText().toString()));
                        itemValue.add(Integer.parseInt(valueInput.getText().toString()));
                        str = itemInput.getText().toString() + ": " + "Weight - " +
                                weightInput.getText().toString() + ", Value - " + valueInput.getText().toString();
                        item = new RadioButton(getContext());
                        item.setText(str);
                        item.setGravity(Gravity.CENTER | Gravity.START);
                        rG.addView(item);
                        try {
                            saveText(items);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        FloatingActionButton delete = view.findViewById(R.id.removeLineButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Choose an item.",
                        Toast.LENGTH_SHORT).show();
                rG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup rG, int checkedId)
                    {
                        RadioButton checkedRadioButton = (RadioButton) rG.findViewById(checkedId);
                        String removeItem = (String) checkedRadioButton.getText();
                        String[] arrOfItem = removeItem.split(":");
                        items.remove(arrOfItem[0]);
                        itemWeight.remove(itemNames.indexOf(arrOfItem[0]));
                        itemValue.remove(itemNames.indexOf(arrOfItem[0]));
                        itemNames.remove(itemNames.indexOf(arrOfItem[0]));
                        try {
                            saveText(items);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        rG.removeViewAt(rG.indexOfChild(checkedRadioButton));
                    }
                });
            }
        });

        /*
        Calculate the final results and send them to the next fragment.
         */
        FloatingActionButton next = view.findViewById(R.id.continueButton3);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                knapsack();
                bundle.putStringArrayList("items", finalItems);
                bundle.putInt("value", valueResult);
                bundle.putInt("weight", weightResult);
                Navigation.findNavController(view).navigate(R.id.action_enter_items2_to_checklist, bundle);
            }
        });

        return view;
    }

    /*
    Saves the user-inputted data so it
    can be reloaded the next time the
    application runs.
     */
    public void saveText(HashMap<String, KeyValuePair<Integer, Integer>> savedItems) throws IOException {
        FileWriter fW = new FileWriter(txtFile, false);
        PrintWriter pW = new PrintWriter(fW, false);
        pW.flush();
        pW.close();
        fW.close();
        ObjectOutputStream itemsObjectOutputStream = new ObjectOutputStream(itemsFileOutputStream);
        itemsObjectOutputStream.writeObject(savedItems);
    }

    /*
    Loads the saved data
    from storage.
     */
    public void loadFile(File storageDir) throws IOException, ClassNotFoundException {
        // Retrieve all .txt files in "Documents" folder of the app's storage
        String path = storageDir.toString() + "/ITEM_DATA.txt";
        File data = new File(path);
        assert data != null;
        File fileEvents = new File(String.valueOf(data));
        if (fileEvents.length() > 0) {
            ObjectInputStream airlineObjectInputStream = new ObjectInputStream(itemsFileInputStream);
            items = (HashMap<String, KeyValuePair<Integer, Integer>>) airlineObjectInputStream.readObject();
            if (items != null) {
                items.forEach((key, value) -> {
                    item = new RadioButton(getContext());
                    String line = key + ": " + value;
                    item.setText(line);
                    item.setGravity(Gravity.CENTER | Gravity.START);
                    rG.addView(item);
                    itemWeight.add(value.getKey());
                    itemValue.add(value.getValue());
                    itemNames.add(key);
                });
            }
        }
    }

    /*
    Calculate the most optimal list of items,
    maximizing the size and value of the total items.
     */
    public void knapsack() {
        int[][] dp = new int[items.size()][capacity + 1];

        // Populate the first column
        for (int item = 0; item < items.size(); item++) {
            dp[item][0] = 0;
        }

        // If the first item is within capacity, populate first row
        for(int cap = 0; cap <= capacity; cap++) {
            if (itemWeight.get(0) <= cap) {
                dp[0][cap] = itemValue.get(0);
            }
        }

        for (int cap = 1; cap <= capacity; cap++) {
            for (int item = 0; item < items.size(); item++) {
                int value1 = 0;
                int value2;

                // Avoid an out of index exception
                if (item - 1 >= 0) {

                    // Check if the current item weight is within the current capacity
                    if (itemWeight.get(item) <= cap) {

                        // "value1" equals the sum of the item value
                        // and the value of the previous row's value
                        value1 = itemValue.get(item) + dp[item - 1][cap - itemWeight.get(item)];
                    }
                    value2 = dp[item - 1][cap];

                    // The greater value is put in the array
                    dp[item][cap] = Math.max(value1, value2);
                }
            }
        }

        // Find the final total value
        valueResult = dp[items.size() - 1][capacity];

        int x = items.size() - 1;
        int y = capacity;

        // Save the list of items needed to be packed
        while (x >= 0  && y >= 0) {
            if (x - 1 >= 0) {
                int currentValue = dp[x][y];
                int previousValue = dp[x - 1][y];

                if (currentValue > previousValue) {
                    finalItems.add(itemNames.get(x));
                    y -= itemWeight.get(x);
                    weightResult += itemWeight.get(x);
                }
            } else {
                if (y - itemWeight.get(x) == 0) {
                    finalItems.add(itemNames.get(x));
                    weightResult += itemWeight.get(x);
                }
            }
            x--;
        }
    }
}