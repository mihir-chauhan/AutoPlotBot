package com.example.odometryapp_v10.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.odometryapp_v10.JSON;
import com.example.odometryapp_v10.R;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EditFunction extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener {
    private editFunctionListener listener;
    private Spinner editFunctionSelectorSpinner;
    private View view;
    final List<String> allFunctionNames = new ArrayList<>();
    ListView listView;
    String selectedFunctionName;

    private enum ParameterTypes {
        String, Integer, Double, Boolean
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.edit_function_dialog, null);

        builder.setView(view).setTitle("Edit Function").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setPositiveButton("done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return builder.create();
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, final View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (editFunctionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement pathListener");
        }
    }

    public interface editFunctionListener {
        void editFunction(ArrayList<Object> functionInfo);
    }
}
