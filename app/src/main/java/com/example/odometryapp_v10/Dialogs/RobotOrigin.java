package com.example.odometryapp_v10.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.example.odometryapp_v10.R;
import com.example.odometryapp_v10.RobotSimulation.Structure.Pose;

public class RobotOrigin extends AppCompatDialogFragment {
    private robotOriginListener listener;
    private View view;

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.setrobotorigin_dialog, null);
        final EditText x = view.findViewById(R.id.originX);
        final EditText y = view.findViewById(R.id.originY);
        final EditText heading = view.findViewById(R.id.originHeading);

        builder.setView(view).setTitle("Set Robot Origin").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setPositiveButton("done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.setRobotOrigin(new Pose(Double.parseDouble(x.getText().toString()), Double.parseDouble(y.getText().toString()), Double.parseDouble(heading.getText().toString())));
            }
        });

        return builder.create();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (robotOriginListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement pathListener");
        }
    }

    public interface robotOriginListener {
        void setRobotOrigin(Pose robotOrigin);
    }
}
