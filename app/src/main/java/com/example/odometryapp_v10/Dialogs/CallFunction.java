package com.example.odometryapp_v10.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.odometryapp_v10.JSON;
import com.example.odometryapp_v10.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CallFunction extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener {
    private callFunctionListener listener;
    private Spinner functionSelectorSpinner;
    private View view;
    final List<String> allFunctionNames = new ArrayList<>();
    ListView listView;
    String selectedFunctionName;
    CallFunctionListViewAdapter adapter;

    private enum ParameterTypes {
        String, Integer, Double, Boolean
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.call_function_dialog, null);
        initalizeCallFunctionsDialogContents(view);

        builder.setView(view).setTitle("Call Function").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setPositiveButton("done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    ArrayList<Object> functionInfo = new ArrayList<>();
                    JSONArray jsonArray = JSON.readJSONTextFile("functions", Environment.getExternalStorageDirectory() + "/Documents/").getJSONArray("function");
                    for (int x = 0; x < jsonArray.getJSONObject(returnPositionFromJSONArray(jsonArray, selectedFunctionName)).getJSONObject("parameters").length(); x++) {
                        functionInfo.add(adapter.getValuesFromView(x));
                    }
                    listener.callFunction(functionInfo);
                } catch (Exception e) {
                    listener.callFunction(null);
                }
            }
        });
        return builder.create();
    }

    private void initalizeCallFunctionsDialogContents(final View view) {
        listView = view.findViewById(R.id.callFunctionListView);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        String[] parameterTypes = new String[]{};
        String[] parameterNames = new String[]{};
        adapter = new CallFunctionListViewAdapter(this.getContext(), parameterTypes, parameterNames);
        listView.setAdapter(adapter);

        functionSelectorSpinner = view.findViewById(R.id.functionSelector);
        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Select A Function To Call");
        try {
            JSONArray jsonArray = JSON.readJSONTextFile("functions", Environment.getExternalStorageDirectory() + "/Documents/").getJSONArray("function");
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    allFunctionNames.add(jsonArray.getJSONObject(i).getString("functionName"));
                    String functionName = JSON.splitCamelCase(jsonArray.getJSONObject(i).getString("functionName"));
                    spinnerArray.add(functionName.substring(0, 1).toUpperCase() + functionName.substring(1).toLowerCase());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        functionSelectorSpinner.setAdapter(spinnerAdapter);
        functionSelectorSpinner.setOnItemSelectedListener(this);
    }

    private void updateListViewFromSelection(final ListView listView, final Context context) {
        String functionName = allFunctionNames.get(functionSelectorSpinner.getSelectedItemPosition() - 1);
        selectedFunctionName = functionName;
        try {
            JSONArray jsonArray = JSON.readJSONTextFile("functions", Environment.getExternalStorageDirectory() + "/Documents/").getJSONArray("function");
            if (jsonArray != null) {
                int i = returnPositionFromJSONArray(jsonArray, functionName);
                if (i != -1) {
                    ArrayList<String> parameterTypes = new ArrayList<>();
                    ArrayList<String> parameterNames = new ArrayList<>();
                    Iterator<String> keys = jsonArray.getJSONObject(i).getJSONObject("parameters").keys();

                    while (keys.hasNext()) {
                        String key = keys.next();
                        try {
                            parameterNames.add(key);
                            parameterTypes.add(jsonArray.getJSONObject(i).getJSONObject("parameters").get(key).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    //image_urls.toArray(new String[image_urls.size()]);
                    final String[] parameterNamesArray = parameterNames.toArray(new String[parameterNames.size()]);
                    final String[] parameterTypesArray = parameterTypes.toArray(new String[parameterTypes.size()]);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listView.setAdapter(new CallFunctionListViewAdapter(context, parameterTypesArray, parameterNamesArray));
                        }
                    });
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Integer returnPositionFromJSONArray(JSONArray jsonArray, String name) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                if (jsonArray.getJSONObject(i).getString("functionName").equals(name)) {
                    return i;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, final View view, int position, long id) {
        if (functionSelectorSpinner.getSelectedItemPosition() == 0) {
            final String[] parameterNamesArray = new String[]{};
            final String[] parameterTypesArray = new String[]{};
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listView.setAdapter(new CallFunctionListViewAdapter(view.getContext(), parameterTypesArray, parameterNamesArray));
                }
            });
        } else {
            updateListViewFromSelection(listView, view.getContext());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
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
        void callFunction(ArrayList<Object> functionInfo);
    }

    private class CallFunctionListViewAdapter extends ArrayAdapter<String> {
        private String[] parameterNames;

        public CallFunctionListViewAdapter(Context context, String[] typeOfParameters, String[] parameterNames) {
            super(context, -1, -1, typeOfParameters);
            this.parameterNames = parameterNames;
        }

        @Override
        public View getView(int position, View convertView, @Nullable ViewGroup parent) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            RelativeLayout listLayout = new RelativeLayout(this.getContext());
            listLayout.setLayoutParams(new AbsListView.LayoutParams(
                    AbsListView.LayoutParams.WRAP_CONTENT,
                    75));

            final EditText editText = new EditText(this.getContext());
            editText.setWidth(500);
            editText.setHint(parameterNames[position].substring(0, 1).toUpperCase() + parameterNames[position].substring(1));
            editText.setId(position);
            editText.setFocusableInTouchMode(true);

            Switch booleanSwitch = new Switch(this.getContext());
            booleanSwitch.setWidth(500);
            booleanSwitch.setText(parameterNames[position]);

            if (super.getItem(position) == ParameterTypes.String.toString()) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
            } else if (super.getItem(position) == ParameterTypes.Integer.toString()) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (super.getItem(position) == ParameterTypes.Double.toString()) {
                editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            } else if (super.getItem(position) == ParameterTypes.Boolean.toString()) {
                booleanSwitch.setChecked(false);
            }


            listLayout.addView(booleanSwitch);
            return listLayout;

        }

        public ArrayList<Object> getValuesFromView(int position) {
            ArrayList<Object> functionInfo = new ArrayList<>();
            EditText editText = null;
            Switch aSwitch = null;
            try {
                editText = view.findViewById(position);
            } catch (Exception e) {
                aSwitch = view.findViewById(position);
            }
            if (editText != null && !editText.getText().toString().isEmpty()) {
                functionInfo.add(editText.getText().toString());
            } else if (aSwitch != null) {
                functionInfo.add(aSwitch.isChecked());
            }
            return functionInfo;
        }
    }
}
