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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.odometryapp_v10.Main.FunctionReturnFormat;
import com.example.odometryapp_v10.Main.JSON;
import com.example.odometryapp_v10.R;
import com.example.odometryapp_v10.RobotDimensions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class CallFunction extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener {
    private callFunctionListener listener;
    ListView listView;
    int numberOfParameters = 0;
    View view;
    static Spinner functionSelectorSpinner;
    ArrayList<String> allFunctionNames = new ArrayList<>();
    CustomListViewAdapter adapter = new CustomListViewAdapter();
    public boolean canSetSelectionOfFunctionSelector = false;
    public static boolean isEditingFunction = false;
    private MovementType movementType;
    private Spinner positionOnRobot;
    private enum ParameterTypes {
        String, Integer, Double, Boolean
    }

    private enum MovementType {
        TankForward, TankBackward, Strafe, PurePursuit, None
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        canSetSelectionOfFunctionSelector = false;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.call_function_dialog, null);
        listView = view.findViewById(R.id.callFunctionListView);
        listView.setAdapter(adapter);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        initializeDialogComponents();

        canSetSelectionOfFunctionSelector = true;

        builder.setView(view).setTitle("Call Function").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (functionSelectorSpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), "No function is being called", Toast.LENGTH_SHORT).show();
                    return;
                }
                ArrayList<FunctionReturnFormat> parametersArray = new ArrayList<>();
                for (int parameters = 0; parameters < numberOfParameters; parameters++) {
                    String functionName = parameterNames.get(parameters);
                    Object functionParameter;
                    if (parameterTypes.get(parameters).equals("Boolean")) {
                        functionParameter = (adapter.getParameterNamesFromView(adapter.getViewByPosition(parameters, listView), true));
                    } else {
                        if (adapter.getParameterNamesFromView(adapter.getViewByPosition(parameters, listView), false).equals("")) {
                            Toast.makeText(getContext(), "One or more fields are blank", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        functionParameter = (adapter.getParameterNamesFromView(adapter.getViewByPosition(parameters, listView), false));
                    }

                    parametersArray.add(new FunctionReturnFormat(functionName, functionParameter));
                }
                //returns array of format: [["parameterName", "parameterValue"], ["...", "..."], ["...", "..."], ...]
                if(isSelectedFunctionADrivetrainFunction) {
                    switch (positionOnRobot.getSelectedItemPosition()) {
                        case 0:
                            if (parametersArray.get(0).parameterValue instanceof Integer) {
                                parametersArray.get(0).parameterValue = (double) ((Integer) parametersArray.get(0).parameterValue).intValue() + RobotDimensions.halfRobotWidth;
                            } else {
                                parametersArray.get(0).parameterValue = (double) parametersArray.get(0).parameterValue + RobotDimensions.halfRobotWidth;
                            }
                            if (parametersArray.get(1).parameterValue instanceof Integer) {
                                parametersArray.get(1).parameterValue = (double) ((Integer) parametersArray.get(1).parameterValue).intValue() - RobotDimensions.halfRobotWidth;
                            } else {
                                parametersArray.get(1).parameterValue = (double) parametersArray.get(1).parameterValue - RobotDimensions.halfRobotWidth;
                            }
                            break;
                        case 1:
                            if (parametersArray.get(1).parameterValue instanceof Integer) {
                                parametersArray.get(1).parameterValue = (double) ((Integer) parametersArray.get(1).parameterValue).intValue() - RobotDimensions.halfRobotWidth;
                            } else {
                                parametersArray.get(1).parameterValue = (double) parametersArray.get(1).parameterValue - RobotDimensions.halfRobotWidth;
                            }
                            break;
                        case 2:
                            if (parametersArray.get(0).parameterValue instanceof Integer) {
                                parametersArray.get(0).parameterValue = (double) ((Integer) parametersArray.get(0).parameterValue).intValue() - RobotDimensions.halfRobotWidth;
                            } else {
                                parametersArray.get(0).parameterValue = (double) parametersArray.get(0).parameterValue - RobotDimensions.halfRobotWidth;
                            }
                            if (parametersArray.get(1).parameterValue instanceof Integer) {
                                parametersArray.get(1).parameterValue = (double) ((Integer) parametersArray.get(1).parameterValue).intValue() - RobotDimensions.halfRobotWidth;
                            } else {
                                parametersArray.get(1).parameterValue = (double) parametersArray.get(1).parameterValue - RobotDimensions.halfRobotWidth;
                            }
                            break;
                        case 3:
                            if (parametersArray.get(0).parameterValue instanceof Integer) {
                                parametersArray.get(0).parameterValue = (double) ((Integer) parametersArray.get(0).parameterValue).intValue() + RobotDimensions.halfRobotWidth;
                            } else {
                                parametersArray.get(0).parameterValue = (double) parametersArray.get(0).parameterValue + RobotDimensions.halfRobotWidth;
                            }
                            break;
                        case 4:
                            break;
                        case 5:
                            if (parametersArray.get(0).parameterValue instanceof Integer) {
                                parametersArray.get(0).parameterValue = (double) ((Integer) parametersArray.get(0).parameterValue).intValue() - RobotDimensions.halfRobotWidth;
                            } else {
                                parametersArray.get(0).parameterValue = (double) parametersArray.get(0).parameterValue - RobotDimensions.halfRobotWidth;
                            }
                            break;
                        case 6:
                            if (parametersArray.get(0).parameterValue instanceof Integer) {
                                parametersArray.get(0).parameterValue = (double) ((Integer) parametersArray.get(0).parameterValue).intValue() + RobotDimensions.halfRobotWidth;
                            } else {
                                parametersArray.get(0).parameterValue = (double) parametersArray.get(0).parameterValue + RobotDimensions.halfRobotWidth;
                            }
                            if (parametersArray.get(1).parameterValue instanceof Integer) {
                                parametersArray.get(1).parameterValue = (double) ((Integer) parametersArray.get(1).parameterValue).intValue() + RobotDimensions.halfRobotWidth;
                            } else {
                                parametersArray.get(1).parameterValue = (double) parametersArray.get(1).parameterValue + RobotDimensions.halfRobotWidth;
                            }
                            break;
                        case 7:
                            if (parametersArray.get(1).parameterValue instanceof Integer) {
                                parametersArray.get(1).parameterValue = (double) ((Integer) parametersArray.get(1).parameterValue).intValue() + RobotDimensions.halfRobotWidth;
                            } else {
                                parametersArray.get(1).parameterValue = (double) parametersArray.get(1).parameterValue + RobotDimensions.halfRobotWidth;
                            }
                            break;
                        case 8:
                            if (parametersArray.get(0).parameterValue instanceof Integer) {
                                parametersArray.get(0).parameterValue = (double) ((Integer) parametersArray.get(0).parameterValue).intValue() - RobotDimensions.halfRobotWidth;
                            } else {
                                parametersArray.get(0).parameterValue = (double) parametersArray.get(0).parameterValue - RobotDimensions.halfRobotWidth;
                            }
                            if (parametersArray.get(1).parameterValue instanceof Integer) {
                                parametersArray.get(1).parameterValue = (double) ((Integer) parametersArray.get(1).parameterValue).intValue() + RobotDimensions.halfRobotWidth;
                            } else {
                                parametersArray.get(1).parameterValue = (double) parametersArray.get(1).parameterValue + RobotDimensions.halfRobotWidth;
                            }
                            break;
                    }
                }
                listener.callFunction(selectedFunctionName, parametersArray, isSelectedFunctionADrivetrainFunction, isEditingFunction, positionToEDIT, movementType.toString());
                isEditingFunction = false;
            }
        });
        return builder.create();
    }

    public static void populateComponentsForEditing(final int functionSelectorSpinnerPosition) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                functionSelectorSpinner.setSelection(functionSelectorSpinnerPosition);
            }
        }, 100);
    }


    private void initializeDialogComponents() {
        functionSelectorSpinner = view.findViewById(R.id.functionSelector);
        positionOnRobot = view.findViewById(R.id.callFunctionPositionOnRobot);
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

    private static ArrayList<FunctionReturnFormat> parametersIF_EDITING = new ArrayList<>();
    private static int positionToEDIT;

    public static void setUpFunctionEditing(int editingPosition, ArrayList<FunctionReturnFormat> parameters) {
        isEditingFunction = true;
        positionToEDIT = editingPosition;
        parametersIF_EDITING = parameters;
        CallFunction callFunction = new CallFunction();
        callFunction.adapter.notifyDataSetChanged();
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
        void callFunction(String functionName, ArrayList<FunctionReturnFormat> functionParameters, boolean isDrivetrainFunction, boolean isEditing, int funtionPosition, String movementType);
    }

    String selectedFunctionName;
    ArrayList<String> parameterNames = new ArrayList<>();
    ArrayList<String> parameterTypes = new ArrayList<>();
    boolean isSelectedFunctionADrivetrainFunction;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position >= 1) {
            String functionName = allFunctionNames.get(functionSelectorSpinner.getSelectedItemPosition() - 1);
            selectedFunctionName = functionName;
            try {
                JSONArray jsonArray = JSON.readJSONTextFile("functions", Environment.getExternalStorageDirectory() + "/Documents/").getJSONArray("function");
                if (jsonArray != null) {
                    int i = returnPositionFromJSONArray(jsonArray, functionName);
                    isSelectedFunctionADrivetrainFunction = jsonArray.getJSONObject(i).getString("functionType").equals("Drivetrain");
                    if(isSelectedFunctionADrivetrainFunction) {
                        positionOnRobot.setVisibility(View.VISIBLE);
                        positionOnRobot.setSelection(4);
                    } else {
                        positionOnRobot.setVisibility(View.GONE);
                    }
                    try {
                        if (jsonArray.getJSONObject(i).getString("movementType").equals("Tank Forward")) {
                            movementType = MovementType.TankForward;
                        } else if (jsonArray.getJSONObject(i).getString("movementType").equals("Tank Backward")) {
                            movementType = MovementType.TankBackward;
                        } else if (jsonArray.getJSONObject(i).getString("movementType").equals("Strafe")) {
                            movementType = MovementType.Strafe;
                        } else if (jsonArray.getJSONObject(i).getString("movementType").equals("Pure Pursuit")) {
                            movementType = MovementType.PurePursuit;
                        }
                    } catch (Exception ignore) {
                        movementType = MovementType.None;
                    }
                    if (i != -1) {
                        parameterTypes.clear();
                        parameterNames.clear();
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
                        numberOfParameters = parameterNames.size();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
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

    class CustomListViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return numberOfParameters;
        }

        @Override
        public Object getItem(int position) {
            if (numberOfParameters >= 1) {
                EditText parameterName = view.findViewById(R.id.callFunctionParameterInput);
                return parameterName.getText();
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
                convertView = getLayoutInflater().inflate(R.layout.call_function_custom_listview_layout, null);
                holder = new ViewHolder();
                holder.editText = convertView.findViewById(R.id.callFunctionParameterInput);
                holder.booleanSwitch = convertView.findViewById(R.id.booleanSwitch);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

            holder.editText.setVisibility(View.INVISIBLE);
            holder.booleanSwitch.setVisibility(View.INVISIBLE);

            if (parameterTypes.get(position).equals(ParameterTypes.Boolean.toString())) {
                holder.booleanSwitch.setVisibility(View.VISIBLE);
                holder.booleanSwitch.setText(parameterNames.get(position).substring(0, 1).toUpperCase() + parameterNames.get(position).substring(1));
            } else {
                holder.editText.setVisibility(View.VISIBLE);
                holder.editText.setHint(parameterNames.get(position).substring(0, 1).toUpperCase() + parameterNames.get(position).substring(1));
                if (parameterTypes.get(position).equals(ParameterTypes.String.toString())) {
                    holder.editText.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
                } else if (parameterTypes.get(position).equals(ParameterTypes.Double.toString())) {
                    holder.editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                } else if (parameterTypes.get(position).equals(ParameterTypes.Integer.toString())) {
                    holder.editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                }

            }

            if (isEditingFunction) {
                if (parametersIF_EDITING.get(position).parameterValue.toString().equals("true") || parametersIF_EDITING.get(position).parameterValue.toString().equals("false")) {
                    if (parametersIF_EDITING.get(position).parameterValue.toString().equals("true")) {
                        holder.booleanSwitch.setChecked(true);
                    } else {
                        holder.booleanSwitch.setChecked(false);
                    }
                } else {
                    holder.editText.setText(parametersIF_EDITING.get(position).parameterValue.toString());
                }
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

        public String getInfoFromView(View view) {
            EditText parameterName = view.findViewById(R.id.callFunctionParameterInput);
            return parameterName.getText().toString();
        }

        public Object getParameterNamesFromView(View view, boolean isBoolean) {

            if (!isBoolean) {
                try {
                    Scanner scanner = new Scanner(((EditText) view.findViewById(R.id.callFunctionParameterInput)).getText().toString());
                    if (scanner.hasNextInt()) {
                        return Integer.parseInt(((EditText) view.findViewById(R.id.callFunctionParameterInput)).getText().toString());
                    } else if (scanner.hasNextDouble()) {
                        return Double.parseDouble(((EditText) view.findViewById(R.id.callFunctionParameterInput)).getText().toString());
                    } else {
                        return ((EditText) view.findViewById(R.id.callFunctionParameterInput)).getText().toString();
                    }
                } catch (Exception e) {
                    return ((EditText) view.findViewById(R.id.callFunctionParameterInput)).getText().toString();
                }
            } else {
                return ((Switch) view.findViewById(R.id.booleanSwitch)).isChecked();
            }
        }
    }

    public static class ViewHolder {
        EditText editText;
        Switch booleanSwitch;
    }
}
