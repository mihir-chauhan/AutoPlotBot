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
import android.widget.AdapterView;
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
import org.json.JSONObject;

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
    CustomListViewAdapter adapter = new CustomListViewAdapter();
    int numberOfParameters = 1;
    ArrayList<ArrayList<Object>> parametersOfFunction = new ArrayList<>();
    EditText editFunctionName;
    final ArrayList<ArrayList<Object>> allParameters = new ArrayList<>();
    boolean canRemoveFromSavedParametersArrayList = false;
    boolean canReferToOriginalFunction = true;
    int originalFunctionPosition;
    String originalFunctionType;

    private enum ParameterTypes {
        String, Integer, Double, Boolean
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.edit_function_dialog, null);
        listView = view.findViewById(R.id.editFunctionListView);
        listView.setAdapter(adapter);
        initializeDialogComponents();

        builder.setView(view).setTitle("Edit Function").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setNeutralButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                JSON.removeFromJSONTextFile("functions", Environment.getExternalStorageDirectory() + "/Documents/", originalFunctionPosition);
            }
        }).setPositiveButton("done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (numberOfParameters >= 1) {
                    for (int position = 0; position < numberOfParameters; position++) {
                        if(adapter.getInfoFromView(adapter.getViewByPosition(position, listView)).get(0).toString().equals("") ||
                                adapter.getInfoFromView(adapter.getViewByPosition(position, listView)).get(1).toString().equals("Parameter Type")) {
                            Toast.makeText(getContext(), "One or more parameter fields are blank", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        allParameters.add(adapter.getInfoFromView(adapter.getViewByPosition(position, listView)));
                    }
                }
                //TODO: add validator for spinners and parameter names to make sure they are not empty
                if (!editFunctionName.getText().toString().isEmpty()) {
                    if(editFunctionSelectorSpinner.getSelectedItemPosition() == 0) {
                        Toast.makeText(getContext(), "Function name is undefined", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    listener.editFunction(originalFunctionPosition, editFunctionName.getText().toString(), allParameters, originalFunctionType);
                } else {
                    Toast.makeText(getContext(), "Function name is undefined", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        Button addParameters = view.findViewById(R.id.addEditParameters);
        addParameters.setEnabled(false);

        addParameters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberOfParameters >= 1) {
                    savedParameters.clear();
                    for(int parameters = 0; parameters < numberOfParameters; parameters++) {
                        savedParameters.add(adapter.getParameterNamesFromView(adapter.getViewByPosition(parameters, listView)));
                    }
                }
                numberOfParameters++;
                canRemoveFromSavedParametersArrayList = false;
                adapter.notifyDataSetChanged();
            }
        });

        Button removeParameters = view.findViewById(R.id.removeEditParameters);
        removeParameters.setEnabled(false);

        removeParameters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canRemoveFromSavedParametersArrayList) {
                    try {
                        savedParameters.remove(savedParameters.size() - 1);
                    } catch (Exception e) {
                        savedParameters.clear();
                    }
                }

                if (numberOfParameters > 0) {
                    numberOfParameters--;
                    canRemoveFromSavedParametersArrayList = true;
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(view.getContext(), "Unable to remove parameters", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return builder.create();

    }

    private void initializeDialogComponents() {
        editFunctionName = view.findViewById(R.id.editFunctionName);
        editFunctionSelectorSpinner = view.findViewById(R.id.editFunctionSelector);
        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Select A Function To Edit");
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
        editFunctionSelectorSpinner.setAdapter(spinnerAdapter);
        editFunctionSelectorSpinner.setOnItemSelectedListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position >= 1) {
            canReferToOriginalFunction = true;
            String functionName = allFunctionNames.get(editFunctionSelectorSpinner.getSelectedItemPosition() - 1);
            selectedFunctionName = functionName;
            try {
                JSONArray jsonArray = JSON.readJSONTextFile("functions", Environment.getExternalStorageDirectory() + "/Documents/").getJSONArray("function");
                if (jsonArray != null) {
                    int i = returnPositionFromJSONArray(jsonArray, functionName);
                    if (i != -1) {
                        originalFunctionPosition = i;
                        editFunctionName.setText(jsonArray.getJSONObject(i).getString("functionName"));
                        editFunctionName.setEnabled(true);
                        Button addParameter = this.view.findViewById(R.id.addEditParameters);
                        addParameter.setEnabled(true);
                        Button removeParameter = this.view.findViewById(R.id.removeEditParameters);
                        removeParameter.setEnabled(true);
                        parametersOfFunction.clear();
                        originalFunctionType = jsonArray.getJSONObject(i).getString("functionType");

                        Iterator<String> keys = jsonArray.getJSONObject(i).getJSONObject("parameters").keys();

                        while (keys.hasNext()) {
                            String key = keys.next();
                            try {
                                ArrayList<Object> parameters = new ArrayList<>();
                                parameters.add(key);
                                parameters.add(jsonArray.getJSONObject(i).getJSONObject("parameters").get(key).toString());
                                parametersOfFunction.add(parameters);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        numberOfParameters = parametersOfFunction.size();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            editFunctionName.setEnabled(false);
            numberOfParameters = 0;
        }
        adapter.notifyDataSetChanged();
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
        void editFunction(int originalFunctionPosition, String functionName, ArrayList<ArrayList<Object>> allParameters, String functionType);
    }

    ArrayList<String> savedParameters = new ArrayList<>();

    class CustomListViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return numberOfParameters;
        }

        @Override
        public Object getItem(int position) {
            if (numberOfParameters >= 1) {
                ArrayList<Object> parameter = new ArrayList<>();
                EditText parameterName = view.findViewById(R.id.parameterInfo);
                parameter.add(parameterName.getText().toString());
                Spinner parameterType = view.findViewById(R.id.parameterType);
                parameter.add(parameterType.getSelectedItem());
                return parameter;
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.add_function_custom_listview_layout, null);
                holder = new ViewHolder();
                holder.editText = convertView.findViewById(R.id.parameterInfo);
                holder.spinner = convertView.findViewById(R.id.parameterType);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            try {
                if(savedParameters.size() == 0 && canReferToOriginalFunction) {
                    holder.editText.setText(parametersOfFunction.get(position).get(0).toString());
                    if (parametersOfFunction.get(position).get(1).toString().equals(ParameterTypes.String.toString())) {
                        holder.spinner.setSelection(1);
                    } else if (parametersOfFunction.get(position).get(1).toString().equals(ParameterTypes.Integer.toString())) {
                        holder.spinner.setSelection(2);
                    } else if (parametersOfFunction.get(position).get(1).toString().equals(ParameterTypes.Double.toString())) {
                        holder.spinner.setSelection(3);
                    } else if (parametersOfFunction.get(position).get(1).toString().equals(ParameterTypes.Boolean.toString())) {
                        holder.spinner.setSelection(4);
                    }
                } else {
                    canReferToOriginalFunction = false;
                    holder.editText.setText(savedParameters.get(position));
                }
            } catch (Exception e) {
//                e.printStackTrace();
                holder.editText.setText("");
            }

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

        public String getParameterNamesFromView(View view) {
            return ((EditText) view.findViewById(R.id.parameterInfo)).getText().toString();
        }
    }

    public static class ViewHolder {
        EditText editText;
        Spinner spinner;
    }
}
