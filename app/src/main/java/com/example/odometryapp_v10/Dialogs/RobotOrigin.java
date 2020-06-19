package com.example.odometryapp_v10.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatDialogFragment;
import com.example.odometryapp_v10.R;
import com.example.odometryapp_v10.RobotDimensions;
import com.example.odometryapp_v10.RobotSimulation.Structure.Pose;

public class RobotOrigin extends AppCompatDialogFragment {
    private robotOriginListener listener;
    private View view;

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.setrobotorigin_dialog, null);
        final EditText xValue = view.findViewById(R.id.originX);
        final EditText yValue = view.findViewById(R.id.originY);
        final EditText heading = view.findViewById(R.id.originHeading);
        final Spinner positionOnRobot = view.findViewById(R.id.robotOriginPositionsOnRobot);
        positionOnRobot.setSelection(4);

        builder.setView(view).setTitle("Set Robot Origin").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setPositiveButton("done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                double x = Double.parseDouble(xValue.getText().toString());
                double y = Double.parseDouble(yValue.getText().toString());
                switch (positionOnRobot.getSelectedItemPosition()) {
                    case 0:
                        x += RobotDimensions.halfRobotWidth;
                        y -= RobotDimensions.halfRobotLength;
                        break;
                    case 1:
                        y -= RobotDimensions.halfRobotLength;
                        break;
                    case 2:
                        x -= RobotDimensions.halfRobotWidth;
                        y -= RobotDimensions.halfRobotLength;
                        break;
                    case 3:
                        x += RobotDimensions.halfRobotWidth;
                        break;
                    case 4:
                        break;
                    case 5:
                        x -= RobotDimensions.halfRobotWidth;
                        break;
                    case 6:
                        x += RobotDimensions.halfRobotWidth;
                        y += RobotDimensions.halfRobotLength;
                        break;
                    case 7:
                        y += RobotDimensions.halfRobotLength;
                        break;
                    case 8:
                        x -= RobotDimensions.halfRobotWidth;
                        y += RobotDimensions.halfRobotLength;
                        break;
                }
                listener.setRobotOrigin(new Pose(x, y, Math.toRadians(Double.parseDouble(heading.getText().toString()))));
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
