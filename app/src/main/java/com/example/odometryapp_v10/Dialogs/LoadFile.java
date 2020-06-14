package com.example.odometryapp_v10.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.odometryapp_v10.Main.JSON;
import com.example.odometryapp_v10.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoadFile extends AppCompatDialogFragment {
    private loadProgramListener listener;
    private View view;

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.load_file_dialog, null);
        Spinner programSelectorSpinner = view.findViewById(R.id.loadFileSpinner);
        List<String> allProgramNames;

        builder.setView(view).setTitle("Load Program").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setPositiveButton("done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.loadProgram();
            }
        });

        File f = new File(Environment.getExternalStorageDirectory() + "/Innov8rz/");

        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File f, String name) {
                return name.endsWith(".txt");
            }
        };

        allProgramNames = Arrays.asList(f.list(filter));

        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Select A Program To Open");
        for(int i = 0; i < allProgramNames.size(); i++) {
            String programFileName = JSON.splitCamelCase(allProgramNames.get(i)).substring(0, 1).toUpperCase() + JSON.splitCamelCase(allProgramNames.get(i)).substring(1);
            String programFileNameWITHOUT_extension = programFileName.replace(".txt", "");
            spinnerArray.add(programFileNameWITHOUT_extension);
        }
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        programSelectorSpinner.setAdapter(spinnerAdapter);

        programSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (loadProgramListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement pathListener");
        }
    }

    public interface loadProgramListener {
        void loadProgram();
    }
}
