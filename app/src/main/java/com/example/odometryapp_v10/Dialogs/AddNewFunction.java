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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.odometryapp_v10.R;

import java.util.ArrayList;

public class AddNewFunction extends AppCompatDialogFragment {
    private addNewFunctionListener listener;
    EditText functionName;
    ListView listView;
    int numberOfParameters = 1;
    View view;
    ArrayList<ArrayList<Object>> allParameters = new ArrayList<>();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.add_function_dialog, null);
        listView = view.findViewById(R.id.addNewFunctionListView);
        functionName = view.findViewById(R.id.addNewFunctionName);

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
                numberOfParameters++;
                EditText editText = adapter.getViewByPosition(numberOfParameters-1, listView).findViewById(R.id.parameterInfo);
                editText.setHint("jnf;af;djfjeq");
                adapter.notifyDataSetChanged();
            }
        });

        Button removeParameters = view.findViewById(R.id.removeParameters);

        removeParameters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberOfParameters > 0) {
                    numberOfParameters--;
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


    class CustomListViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return numberOfParameters;
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
