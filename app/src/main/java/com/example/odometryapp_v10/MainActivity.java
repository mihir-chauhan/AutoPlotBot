package com.example.odometryapp_v10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.example.odometryapp_v10.Dialogs.AddNewFunction;
import com.example.odometryapp_v10.Dialogs.CallFunction;
import com.example.odometryapp_v10.Dialogs.EditFunction;
import com.github.clans.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddNewFunction.addNewFunctionListener, CallFunction.callFunctionListener, EditFunction.editFunctionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkForWritePermission();

        FloatingActionButton addNewFunction = findViewById(R.id.addNewFunction);
        addNewFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewFunction addNewFunction = new AddNewFunction();
                addNewFunction.setCancelable(false);
                addNewFunction.show(getSupportFragmentManager(), "addNewFunction");
            }
        });

        FloatingActionButton callFunction = findViewById(R.id.callFunction);
        callFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallFunction callFunction = new CallFunction();
                callFunction.setCancelable(false);
                callFunction.show(getSupportFragmentManager(), "callFunction");
            }
        });

        FloatingActionButton editFunction = findViewById(R.id.editFunction);
        editFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditFunction editFunction = new EditFunction();
                editFunction.setCancelable(false);
                editFunction.show(getSupportFragmentManager(), "editFunction");
            }
        });
    }


    private void checkForWritePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 11039);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // Permission has already been granted
            }
        }

    }

    @Override
    public void addNewFunction(String functionName, ArrayList<ArrayList<Object>> allParameters, String functionType) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("functionName", functionName);
            jsonObject.put("functionType", functionType);
            JSONObject paramObject = new JSONObject();
            for(int parameter = 0; parameter < allParameters.size(); parameter++) {
                paramObject.put(allParameters.get(parameter).get(0).toString(), allParameters.get(parameter).get(1).toString());
            }

            jsonObject.put("parameters", paramObject);

            JSON.appendJSONToTextFile("functions", Environment.getExternalStorageDirectory() + "/Documents/", jsonObject, null, JSON.JSONArchitecture.Function_Notation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callFunction() {

    }

    @Override
    public void editFunction(ArrayList<Object> functionInfo) {


    }
}
