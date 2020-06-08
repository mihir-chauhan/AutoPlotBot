package com.example.odometryapp_v10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.example.odometryapp_v10.Dialogs.AddNewFunction;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddNewFunction.addNewFunctionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton addNewFunction = findViewById(R.id.addNewFunctionFloatingActionButton);
        addNewFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewFunction addNewFunction = new AddNewFunction();
                addNewFunction.setCancelable(false);
                addNewFunction.show(getSupportFragmentManager(), "addNewFunctionDialog");
            }
        });
    }

    @Override
    public void addNewFunction(String functionName, ArrayList<ArrayList<Object>> allParameters) {
        System.out.println(allParameters);
        JSON.writeJSONToTextFile(Environment.getExternalStorageDirectory() + "/Innov8rz/", fileName);
    }
}
