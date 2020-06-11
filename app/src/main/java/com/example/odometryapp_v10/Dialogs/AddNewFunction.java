package com.example.odometryapp_v10.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.odometryapp_v10.R;

import java.util.ArrayList;

public class AddNewFunction extends AppCompatDialogFragment {
    private addNewFunctionListener listener;
    EditText functionName;
    ListView listView;
    int numberOfParameters = 0;
    View view;
    boolean canRemoveFromSavedParametersArrayList = false;

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.add_function_dialog, null);
        listView = view.findViewById(R.id.addNewFunctionListView);
        functionName = view.findViewById(R.id.addNewFunctionName);
        final ArrayList<ArrayList<Object>> allParameters = new ArrayList<>();

        final CustomListViewAdapter adapter = new CustomListViewAdapter();

        listView.setAdapter(adapter);


        builder.setView(view).setTitle("Add New Function").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (numberOfParameters >= 1) {
                    for (int position = 0; position < numberOfParameters; position++) {
                        allParameters.add(adapter.getInfoFromView(adapter.getViewByPosition(position, listView)));
                    }
                }
                //TODO: add validator for spinners and parameter names to make sure they are not empty
                if (!functionName.getText().toString().isEmpty()) {
                    listener.addNewFunction(functionName.getText().toString(), allParameters);
                } else {
                    Toast.makeText(view.getContext(), "Unable to add new function", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button addParameters = view.findViewById(R.id.addParameters);

        addParameters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberOfParameters >= 1) {
                    savedParameters.clear();
                    for(int parameters = 0; parameters < numberOfParameters; parameters++) {
                        savedParameters.add(adapter.getParameterNamesFromView(adapter.getViewByPosition(parameters, listView)));
                    }
                }
                System.out.println("SAVED_PARAMS: " + savedParameters);
                numberOfParameters++;
                canRemoveFromSavedParametersArrayList = false;
                adapter.notifyDataSetChanged();
            }
        });

        Button removeParameters = view.findViewById(R.id.removeParameters);

        removeParameters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canRemoveFromSavedParametersArrayList) {
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
        void addNewFunction(String functionName, ArrayList<ArrayList<Object>> allParameters);
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
                convertView = getLayoutInflater().inflate(R.layout.custom_listview_layout, null);
                holder = new ViewHolder();
                holder.editText = convertView.findViewById(R.id.parameterInfo);
                holder.spinner = convertView.findViewById(R.id.parameterType);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            try {
                holder.editText.setText(savedParameters.get(position));
            } catch (Exception e) {
                holder.editText.setText("");
                System.out.println("unable to get for: " + position);
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
