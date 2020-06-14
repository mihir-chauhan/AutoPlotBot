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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.odometryapp_v10.Main.JSON;
import com.example.odometryapp_v10.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddNewFunction extends AppCompatDialogFragment {
    private addNewFunctionListener listener;
    EditText functionName;
    ListView listView;
    int numberOfParameters = 0;
    View view;
    boolean drivetrainFunctionProtocol = false;
    boolean canRemoveFromSavedParametersArrayList = false;

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.add_function_dialog, null);
        listView = view.findViewById(R.id.addNewFunctionListView);
        functionName = view.findViewById(R.id.addNewFunctionName);
        final ArrayList<ArrayList<Object>> allParameters = new ArrayList<>();
        final Spinner functionTypeSelector = view.findViewById(R.id.functionTypeSelector);

        final CustomListViewAdapter adapter = new CustomListViewAdapter();

        listView.setAdapter(adapter);

        functionTypeSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    numberOfParameters = 2;
                    drivetrainFunctionProtocol = true;
                    adapter.notifyDataSetChanged();
                } else {
                    numberOfParameters = 0;
                    drivetrainFunctionProtocol = false;
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        builder.setView(view).setTitle("Add New Function").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                JSONObject jsonObject = JSON.readJSONTextFile("functions", Environment.getExternalStorageDirectory() + "/Documents/");
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("function");
                    ArrayList<String> functionNames = new ArrayList<>();
                    for (int x = 0; x < jsonArray.length(); x++) {
                        functionNames.add(jsonArray.getJSONObject(x).getString("functionName"));
                    }
                    for(int y = 0; y < functionNames.size(); y++) {
                        if(functionNames.get(y).equals(functionName.getText().toString())) {
                            Toast.makeText(getContext(), "Function with the same name is already defined", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                } catch (Exception ignore) {

                }
                if (numberOfParameters >= 1) {
                    for (int position = 0; position < numberOfParameters; position++) {
                        if (adapter.getInfoFromView(adapter.getViewByPosition(position, listView)).get(0).toString().equals("") ||
                                adapter.getInfoFromView(adapter.getViewByPosition(position, listView)).get(1).toString().equals("Parameter Type")) {
                            Toast.makeText(getContext(), "One or more parameter fields are blank", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        allParameters.add(adapter.getInfoFromView(adapter.getViewByPosition(position, listView)));
                    }
                }
                if (!functionName.getText().toString().isEmpty()) {
                    if (functionTypeSelector.getSelectedItemPosition() == 0) {
                        Toast.makeText(getContext(), "Function type is undefined", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    listener.addNewFunction(functionName.getText().toString(), allParameters, functionTypeSelector.getSelectedItem().toString());
                } else {
                    Toast.makeText(getContext(), "Function name is undefined", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        Button addParameters = view.findViewById(R.id.addParameters);

        addParameters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberOfParameters >= 1) {
                    savedParameters.clear();
                    for (int parameters = 0; parameters < numberOfParameters; parameters++) {
                        savedParameters.add(adapter.getParameterNamesFromView(adapter.getViewByPosition(parameters, listView)));
                    }
                }
                numberOfParameters++;
                canRemoveFromSavedParametersArrayList = false;
                adapter.notifyDataSetChanged();
            }
        });

        Button removeParameters = view.findViewById(R.id.removeParameters);

        removeParameters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canRemoveFromSavedParametersArrayList) {
                    savedParameters.remove(savedParameters.size() - 1);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (addNewFunctionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement pathListener");
        }
    }


    public interface addNewFunctionListener {
        void addNewFunction(String functionName, ArrayList<ArrayList<Object>> allParameters, String functionType);
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

            if (drivetrainFunctionProtocol) {
                if (position == 0) {
                    holder.editText.setText("x");
                    holder.spinner.setSelection(3);
                    holder.editText.setEnabled(false);
                    holder.spinner.setEnabled(false);
                } else if (position == 1) {
                    holder.editText.setText("y");
                    holder.spinner.setSelection(3);
                    holder.editText.setEnabled(false);
                    holder.spinner.setEnabled(false);
                } else {
                    holder.editText.setEnabled(true);
                    holder.spinner.setEnabled(true);
                    if(position == savedParameters.size()) {
                        holder.spinner.setSelection(0);
                    }
                    try {
                        holder.editText.setText(savedParameters.get(position));
                    } catch (Exception e) {
                        holder.editText.setText("");
                    }
                }
                return convertView;
            }

            try {
                holder.editText.setText(savedParameters.get(position));
            } catch (Exception e) {
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
