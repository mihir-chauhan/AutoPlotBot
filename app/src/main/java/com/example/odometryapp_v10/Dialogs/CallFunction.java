package com.example.odometryapp_v10.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.odometryapp_v10.JSON;
import com.example.odometryapp_v10.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class CallFunction extends AppCompatDialogFragment {
    private callFunctionListener listener;
    private Spinner functionSelectorSpinner;
    private View view;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.call_function_dialog, null);
        functionSelectorSpinner = view.findViewById(R.id.functionSelector);
        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Select A Function To Call");
        try {
            JSONArray jsonArray = JSON.readJSONTextFile("functions", Environment.getExternalStorageDirectory() + "/Documents/").getJSONArray("function");
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    spinnerArray.add(jsonArray.getJSONObject(i).getString("functionName"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        functionSelectorSpinner.setTag("Select A Function To Call");
        functionSelectorSpinner.setAdapter(spinnerAdapter);

//        final CustomListViewAdapter adapter = new CustomListViewAdapter();

        builder.setView(view).setTitle("Call Function").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (callFunctionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement pathListener");
        }
    }


    public interface callFunctionListener {
        void callFunction();
    }


    class CustomListViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
//            if(numberOfParameters >= 1) {
//                ArrayList<Object> parameter = new ArrayList<>();
//                EditText parameterName = view.findViewById(R.id.parameterInfo);
//                parameter.add(parameterName.getText().toString());
//                Spinner parameterType = view.findViewById(R.id.parameterType);
//                parameter.add(parameterType.getSelectedItem());
//                return parameter;
//            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            getItem(position);
            convertView = getLayoutInflater().inflate(R.layout.custom_listview_layout, null);
            return convertView;
        }

        public View getViewByPosition(int pos, ListView listView) {
            final int firstListItemPosition = listView.getFirstVisiblePosition();
            final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

            if (pos < firstListItemPosition || pos > lastListItemPosition) {
                return listView.getAdapter().getView(pos, null, listView);
            } else {
                final int childIndex = pos - firstListItemPosition;
                return listView.getChildAt(childIndex);
            }
        }

        public ArrayList<Object> getInfoFromView(View view) {
            ArrayList<Object> parameterArray = new ArrayList<>();
            EditText parameterName = view.findViewById(R.id.parameterInfo);
            parameterArray.add(parameterName.getText().toString());
            Spinner parameterType = view.findViewById(R.id.parameterType);
            parameterArray.add(parameterType.getSelectedItem().toString());
            return parameterArray;
        }
    }
}
