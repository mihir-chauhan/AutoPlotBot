package com.example.odometryapp_v10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ClipData;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.example.odometryapp_v10.Dialogs.AddNewFunction;
import com.example.odometryapp_v10.Dialogs.CallFunction;
import com.example.odometryapp_v10.Dialogs.EditFunction;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddNewFunction.addNewFunctionListener, CallFunction.callFunctionListener, EditFunction.editFunctionListener {
    com.github.sealstudios.fab.FloatingActionButton callFunction;
    com.github.sealstudios.fab.FloatingActionButton editFunction;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkForWritePermission();


        final ArrayList<RecyclerViewItem> recyclerViewItemArrayList = new ArrayList<>();
        recyclerViewItemArrayList.add(new RecyclerViewItem("Move To Position", "X: 12, Y: 25, Direction: true"));
        recyclerViewItemArrayList.add(new RecyclerViewItem("Strafe To Position", "X: 12, Y: 25, Direction: true"));
        recyclerViewItemArrayList.add(new RecyclerViewItem("Turn", "Heading: 90, Power: 10"));

        recyclerView = findViewById(R.id.programFunctionsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerViewAdapter = new RecyclerViewAdapter(recyclerViewItemArrayList);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                System.out.println("ksjdnlanf: " + position);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
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

    @Override
    public void callFunction(ArrayList<ArrayList<Object>> functionParameters) {

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
