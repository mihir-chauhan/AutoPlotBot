package com.example.odometryapp_v10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.odometryapp_v10.Dialogs.AddNewDrivetrainFunction;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddNewDrivetrainFunction.addDrivetrainFunctionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton addDrivetrainFunction = findViewById(R.id.addNewDrivetrainFunctionFloatingActionButton);
        addDrivetrainFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddDrivetrainFunctionDialog();
            }
        });
    }

    @Override
    public void addDrivetrainFunction(ArrayList<ArrayList<Object>> allParameters) {
        System.out.println(allParameters);
    }

    private void openAddDrivetrainFunctionDialog() {
        AddNewDrivetrainFunction addDrivetrainFunction = new AddNewDrivetrainFunction();
        addDrivetrainFunction.setCancelable(false);
        addDrivetrainFunction.show(getSupportFragmentManager(), "addDrivetrainFunctionDialog");
    }
}
