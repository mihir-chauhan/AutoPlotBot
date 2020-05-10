package com.example.odometryapp_v10.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.example.odometryapp_v10.R;

public class AddNewDrivetrainFunction extends AppCompatDialogFragment {

    private AddNewDrivetrainFunction.addDrivetrainFunctionListener listener;
    ListView listView;
    int numberOfParameters = 0;
    View view;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.add_drivetrain_dialog, null);
        listView = view.findViewById(R.id.addnewDrivetrainFunctionListView);

        final CustomListViewAdapter adapter = new CustomListViewAdapter();

        listView.setAdapter(adapter);

        builder.setView(view).setTitle("Add Drivetrain Function").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.addDrivetrainFunction();
            }
        });

        Button addParameters = view.findViewById(R.id.addParam);

        addParameters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberOfParameters++;
                adapter.notifyDataSetChanged();

            }
        });

//        final Spinner parameterTypeSpinner = view.findViewById(R.id.parameterType);
//
//        parameterTypeSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (!parameterTypeSpinner.getSelectedItem().toString().equalsIgnoreCase("Parameter Type")) {
//
//                }
//            }
//        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (AddNewDrivetrainFunction.addDrivetrainFunctionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement pathListener");
        }
    }


    public interface addDrivetrainFunctionListener {
        void addDrivetrainFunction();
    }


    class CustomListViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return numberOfParameters;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.custom_listview_layout, null);
            return convertView;
        }
    }
}
