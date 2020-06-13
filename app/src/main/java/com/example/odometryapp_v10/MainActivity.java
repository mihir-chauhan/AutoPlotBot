package com.example.odometryapp_v10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.util.Function;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;

import com.example.odometryapp_v10.Dialogs.AddNewFunction;
import com.example.odometryapp_v10.Dialogs.CallFunction;
import com.example.odometryapp_v10.Dialogs.EditFunction;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AddNewFunction.addNewFunctionListener, CallFunction.callFunctionListener, EditFunction.editFunctionListener {
    com.github.sealstudios.fab.FloatingActionButton callFunction;
    com.github.sealstudios.fab.FloatingActionButton editFunction;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    final ArrayList<RecyclerViewItem> recyclerViewItemArrayList = new ArrayList<>();
    boolean doesHaveToCreateNewFile = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        checkForWritePermission();

        buildRecyclerView();

        com.github.sealstudios.fab.FloatingActionButton addNewFunction = findViewById(R.id.addNewFunction);
        addNewFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewFunction addNewFunction = new AddNewFunction();
                addNewFunction.setCancelable(false);
                addNewFunction.show(getSupportFragmentManager(), "addNewFunction");
            }
        });

        callFunction = findViewById(R.id.callFunction);
        callFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallFunction callFunction = new CallFunction();
                callFunction.setCancelable(false);
                callFunction.show(getSupportFragmentManager(), "callFunction");
            }
        });

        editFunction = findViewById(R.id.editFunction);
        editFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditFunction editFunction = new EditFunction();
                editFunction.setCancelable(false);
                editFunction.show(getSupportFragmentManager(), "editFunction");
            }
        });

        canRunFABThread = true;
        enableOrDisableFAButtons();
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

    private void buildRecyclerView() {
        recyclerView = findViewById(R.id.programFunctionsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerViewAdapter = new RecyclerViewAdapter(recyclerViewItemArrayList);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                CallFunction callFunction = new CallFunction();
//                callFunction.setCancelable(false);
//                callFunction.show(getSupportFragmentManager(), "callFunction");
//                int positionOfFunction;
//                try {
//                    positionOfFunction = JSON.returnPositionFromJSONArray(JSON.readJSONTextFile("functions", Environment.getExternalStorageDirectory() + "/Documents/").getJSONArray("function"), recyclerViewItemArrayList.get(position).getFunctionName()) + 1;
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    positionOfFunction = 0;
//                }
//                System.out.println("position: " + positionOfFunction);
//
//                while (!CallFunction.canSetSelectionOfFunctionSelector){
//
//                }
//
//                CallFunction.setUpFunctionEditing(position, listOfAllFunctionParameters.get(position));
//                CallFunction.populateComponentsForEditing(positionOfFunction);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                Collections.swap(recyclerViewItemArrayList, fromPosition, toPosition);
                recyclerViewAdapter.notifyItemMoved(fromPosition, toPosition);

                JSON.reorderFunctionsInProgram(fromPosition, toPosition, currentFileName, Environment.getExternalStorageDirectory() + "/Innov8rz/AutosavedFiles/");

                return true;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                final int deletedColumnPosition = viewHolder.getAdapterPosition();
                final RecyclerViewItem currentItem = recyclerViewItemArrayList.get(deletedColumnPosition);
                CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator);
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Function deleted", Snackbar.LENGTH_SHORT)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                recyclerViewItemArrayList.add(deletedColumnPosition, currentItem);
                                recyclerViewAdapter.notifyDataSetChanged();
                            }
                        });
                snackbar.show();
                recyclerViewItemArrayList.remove(deletedColumnPosition);
                recyclerViewAdapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void addToRecyclerView(String functionName, ArrayList<FunctionReturnFormat> functionParameters) {
        StringBuilder parametersOfFunction = new StringBuilder();

        if(functionParameters.size() >= 1) {
            for(int i = 0; i < functionParameters.size() - 1; i++) {
                parametersOfFunction.append(functionParameters.get(i).parameterName + ": " + functionParameters.get(i).parameterValue + ", ");
            }
            parametersOfFunction.append(functionParameters.get(functionParameters.size() - 1).parameterName + ": " + functionParameters.get(functionParameters.size() - 1).parameterValue);
        }

        recyclerViewItemArrayList.add(new RecyclerViewItem(functionName, parametersOfFunction.toString()));
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private boolean canRunFABThread;
    private Thread fabuttonThread;

    private void enableOrDisableFAButtons() {
        fabuttonThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (canRunFABThread) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (JSON.doesFileExist("functions", Environment.getExternalStorageDirectory() + "/Documents/")) {
                                try {
                                    if (JSON.readJSONTextFile("functions", Environment.getExternalStorageDirectory() + "/Documents/").getJSONArray("function").length() >= 1) {
                                        callFunction.setEnabled(true);
                                        editFunction.setEnabled(true);
                                    } else {
                                        callFunction.setEnabled(false);
                                        editFunction.setEnabled(false);
                                    }
                                } catch (Exception ignore) {
                                    callFunction.setEnabled(false);
                                    editFunction.setEnabled(false);
                                }
                            }
                        }
                    });
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        fabuttonThread.setName("Floating Action Bar Button Thread");
        fabuttonThread.setPriority(Thread.MIN_PRIORITY);
        fabuttonThread.start();
    }

    @Override
    protected void onDestroy() {
        canRunFABThread = false;
        fabuttonThread.interrupt();
        super.onDestroy();
    }

    @Override
    public void addNewFunction(String functionName, ArrayList<ArrayList<Object>> allParameters, String functionType) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("functionName", functionName);
            jsonObject.put("functionType", functionType);
            JSONObject paramObject = new JSONObject();
            for (int parameter = 0; parameter < allParameters.size(); parameter++) {
                paramObject.put(allParameters.get(parameter).get(0).toString(), allParameters.get(parameter).get(1).toString());
            }

            jsonObject.put("parameters", paramObject);

            JSON.appendJSONToTextFile("functions", Environment.getExternalStorageDirectory() + "/Documents/", jsonObject, null, JSON.JSONArchitecture.Function_Notation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ArrayList<ArrayList<FunctionReturnFormat>> listOfAllFunctionParameters = new ArrayList<>();
    String currentFileName;

    @Override
    public void callFunction(String functionName, ArrayList<FunctionReturnFormat> functionParameters, boolean isDrivetrainFunction, boolean isEditing, int positionOfEdit) {
        ArrayList<Object> nullValidation = new ArrayList<>();
        nullValidation.add(functionName);
        nullValidation.add(functionParameters);
        if(!isNull(nullValidation)) {
            String fileName = "program";
            if(!isEditing) {
                if(doesHaveToCreateNewFile) {
                    int fileNumber = 1;
                    if(JSON.doesFileExist("program", null)) {
                        boolean didFindNonexistentFile = false;
                        while(!didFindNonexistentFile) {
                            fileName = "program" + fileNumber;
                            if(!JSON.doesFileExist(fileName, null)) {
                                didFindNonexistentFile = true;
                            }
                            fileNumber++;
                        }
                    }
                    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss");
                    Date date = new Date();
                    fileName += "-" + formatter.format(date);
                    JSON.createFile(fileName, Environment.getExternalStorageDirectory() + "/Innov8rz/AutosavedFiles/");
                    doesHaveToCreateNewFile = false;
                    currentFileName = fileName;
                    listOfAllFunctionParameters.add(functionParameters);
                    addToRecyclerView(functionName, functionParameters);
                    JSON.addFunctionFromProgramToFile(fileName, functionName, functionParameters);
                } else {
                    listOfAllFunctionParameters.add(functionParameters);
                    addToRecyclerView(functionName, functionParameters);
                    JSON.addFunctionFromProgramToFile(currentFileName, functionName, functionParameters);
                }
            }
        }
    }

    public boolean isNull(ArrayList<Object> objects) {
        for(int i = 0; i < objects.size(); i++) {
            if(objects.get(i) == null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void editFunction(int originalFunctionPosition, String functionName, ArrayList<ArrayList<Object>> allParameters, String functionType) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("functionName", functionName);
            jsonObject.put("functionType", functionType);
            JSONObject paramObject = new JSONObject();
            for (int parameter = 0; parameter < allParameters.size(); parameter++) {
                paramObject.put(allParameters.get(parameter).get(0).toString(), allParameters.get(parameter).get(1).toString());
            }

            jsonObject.put("parameters", paramObject);

            JSON.replaceInJSONTextFile("functions", originalFunctionPosition, Environment.getExternalStorageDirectory() + "/Documents/", jsonObject, null, JSON.JSONArchitecture.Function_Notation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
