package com.example.airlinepacker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link choose_airline#newInstance} factory method to
 * create an instance of this fragment.
 */
public class choose_airline extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RadioButton airline;
    RadioGroup rG;

    private HashMap<String, Integer> airlineWeights;

    private String txtFileName;
    private File txtFile;
    private File storageDir;
    private String str;
    private FileOutputStream airlineFileOutputStream;
    private FileInputStream airlineFileInputStream;

    Bundle bundle;
    int weight;

    public choose_airline() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment choose_airline.
     */
    // TODO: Rename and change types and number of parameters
    public static choose_airline newInstance(String param1, String param2) {
        choose_airline fragment = new choose_airline();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.choose_airline, container, false);
        rG = (RadioGroup) view.findViewById(R.id.radioGroup);
        bundle = new Bundle();
        airlineWeights = new HashMap<>();
        storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        txtFileName = "AIRLINE_DATA" + ".txt";
        txtFile = new File(storageDir, txtFileName);
        try {
            airlineFileOutputStream = new FileOutputStream(txtFile, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            airlineFileInputStream = new FileInputStream(txtFile);
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
                Navigation.findNavController(view).navigate(R.id.action_choose_airline_to_home23);
            }
        });

        rG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup rG, int checkedId)
            {
                bundle.clear();
                RadioButton checkedRadioButton = (RadioButton) rG.findViewById(checkedId);
                String airline = (String) checkedRadioButton.getText();
                String[] airlineName = airline.split(":");
                weight = airlineWeights.get(airlineName[0]);
                bundle.putInt("airline", weight);
            }
        });

        FloatingActionButton next = view.findViewById(R.id.continueButton3);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_choose_airline_to_enter_items2, bundle);

            }
        });

        Button enterAirline = view.findViewById(R.id.enterAirline);
        enterAirline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Enter a new airline");
                LinearLayout inputs = new LinearLayout(getContext());
                inputs.setOrientation(LinearLayout.VERTICAL);
                EditText titleInput = new EditText(getContext());
                titleInput.setInputType(InputType.TYPE_CLASS_TEXT);
                titleInput.setHint("Enter airline...");
                inputs.addView(titleInput);
                EditText numInput = new EditText(getContext());
                numInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                numInput.setHint("Enter weight...");
                inputs.addView(numInput);
                builder.setView(inputs);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        airlineWeights.put(titleInput.getText().toString(), Integer.parseInt(numInput.getText().toString()));
                        str = titleInput.getText().toString() + ": " + numInput.getText().toString();
                        airline = new RadioButton(getContext());
                        airline.setText(str);
                        airline.setGravity(Gravity.CENTER | Gravity.START);
                        rG.addView(airline);
                        try {
                            saveText();
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
        return view;
    }

    /*
    Saves the user-inputted data so it
    can be reloaded the next time the
    application runs.
     */
    public void saveText() throws IOException {
        FileWriter fW = new FileWriter(txtFile, false);
        PrintWriter pW = new PrintWriter(fW, false);
        pW.flush();
        pW.close();
        fW.close();
        ObjectOutputStream airlineObjectOutputStream = new ObjectOutputStream(airlineFileOutputStream);
        airlineObjectOutputStream.writeObject(airlineWeights);
    }


    /*
    Loads the saved data
    from storage.
     */
    public void loadFile(File storageDir) throws IOException, ClassNotFoundException {
        // Retrieve all .txt files in "Documents" folder of the app's storage
        String path = storageDir.toString() + "/AIRLINE_DATA.txt";
        File data = new File(path);
        assert data != null;
        File fileEvents = new File(String.valueOf(data));
        if (fileEvents.length() > 0) {
            ObjectInputStream airlineObjectInputStream = new ObjectInputStream(airlineFileInputStream);
            airlineWeights = (HashMap<String, Integer>) airlineObjectInputStream.readObject();
            if (airlineWeights != null) {
                airlineWeights.forEach((key, value) -> {
                    airline = new RadioButton(getContext());
                    String line = key + ": " + value;
                    airline.setText(line);
                    airline.setGravity(Gravity.CENTER | Gravity.START);
                    rG.addView(airline);
                });
            }
        }
    }
}