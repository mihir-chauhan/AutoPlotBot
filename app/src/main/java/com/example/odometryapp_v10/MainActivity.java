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
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.odometryapp_v10.Dialogs.CallFunction;
import com.example.odometryapp_v10.Dialogs.LoadFile;
import com.example.odometryapp_v10.Dialogs.RobotOrigin;
import com.example.odometryapp_v10.Dialogs.SaveFile;
import com.example.odometryapp_v10.Main.CanvasRobotDrawer;
import com.example.odometryapp_v10.Main.Coordinate;
import com.example.odometryapp_v10.Main.FunctionReturnFormat;
import com.example.odometryapp_v10.Main.JSON;
import com.example.odometryapp_v10.Main.LoadFileReturnFormat;
import com.example.odometryapp_v10.Main.RecyclerViewAdapter;
import com.example.odometryapp_v10.Main.RecyclerViewItem;
import com.example.odometryapp_v10.RobotSimulation.MovementPose;
import com.example.odometryapp_v10.RobotSimulation.RobotSim;
import com.example.odometryapp_v10.RobotSimulation.Structure.Odometry;
import com.example.odometryapp_v10.RobotSimulation.Structure.Pose;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements CallFunction.callFunctionListener, SaveFile.saveProgramListener, LoadFile.loadProgramListener, RobotOrigin.robotOriginListener {
    com.github.sealstudios.fab.FloatingActionButton callFunction;
    com.github.sealstudios.fab.FloatingActionButton saveFunction;
    com.github.sealstudios.fab.FloatingActionButton loadFunction;
    TextView simulate;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    final ArrayList<RecyclerViewItem> recyclerViewItemArrayList = new ArrayList<>();
    public static final ArrayList<Coordinate> allCoordinates = new ArrayList<>();
    boolean doesHaveToCreateNewFile = true;
    boolean isEditingLoadedFile = false;
    private static CanvasRobotDrawer drawer;
    private ArrayList<MovementPose> robotSimulatorMovementCoordinates;
    RobotSim robotSim;
    boolean didSendRobotSimCommand = false;
    private Pose robotOrigin = new Pose(0, 0, Math.toRadians(90));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Toast.makeText(this, "Set a robot origin. Default origin: (0, 0, 90)", Toast.LENGTH_SHORT).show();

        robotSimulatorMovementCoordinates = new ArrayList<>();

        checkForWritePermission();

        buildRecyclerView();

        simulate = findViewById(R.id.simulate);

        drawer = new CanvasRobotDrawer(this, findViewById(android.R.id.content).getRootView(), MainActivity.this);

        setOrigin(new Pose(0, 0, Math.toRadians(90)), true);

        callFunction = findViewById(R.id.callFunction);
        callFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallFunction callFunction = new CallFunction();
                callFunction.setCancelable(false);
                callFunction.show(getSupportFragmentManager(), "callFunction");
            }
        });

        saveFunction = findViewById(R.id.saveFile);
        saveFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditingLoadedFile) {
                    SaveFile saveFile = new SaveFile();
                    saveFile.setCancelable(false);
                    saveFile.show(getSupportFragmentManager(), "saveFile");
                } else {
                    SaveFile saveFile = new SaveFile();
                    saveFile.setCancelable(false);
                    saveFile.show(getSupportFragmentManager(), "saveFile");
                }
            }
        });
        saveFunction.setEnabled(false);

        loadFunction = findViewById(R.id.uploadFile);
        loadFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadFile loadFile = new LoadFile();
                loadFile.setCancelable(false);
                loadFile.show(getSupportFragmentManager(), "loadFile");
            }
        });

        com.github.sealstudios.fab.FloatingActionButton newProgram = findViewById(R.id.newProgram);
        newProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewItemArrayList.clear();
                recyclerViewAdapter.notifyDataSetChanged();
                doesHaveToCreateNewFile = true;
                isEditingLoadedFile = false;
                String fileName = "program";
                SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss");
                Date date = new Date();
                fileName += "-" + formatter.format(date);
                JSON.createFile(fileName, Environment.getExternalStorageDirectory() + "/Innov8rz/AutosavedFiles/");
                doesHaveToCreateNewFile = false;
                currentFileName = fileName;
                currentFilePath = Environment.getExternalStorageDirectory() + "/Innov8rz/AutosavedFiles/";
                setOrigin(new Pose(0, 0, Math.toRadians(90)), true);
                robotSimulatorMovementCoordinates.clear();
                listOfAllFunctionParameters.clear();
            }
        });

        com.github.sealstudios.fab.FloatingActionButton setOrigin = findViewById(R.id.setOrigin);
        setOrigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RobotOrigin robotOrigin = new RobotOrigin();
                robotOrigin.setCancelable(false);
                robotOrigin.show(getSupportFragmentManager(), "robotOrigin");
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

    /**
     * Main Activity Layout Creation
     **/

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
                if (didSendRobotSimCommand) {
                    Toast.makeText(MainActivity.this, "Unable to open editor because simulation is currently running", Toast.LENGTH_SHORT).show();
                    return;
                }

                CallFunction callFunction = new CallFunction();
                callFunction.setCancelable(false);
                callFunction.show(getSupportFragmentManager(), "callFunction");
                int positionOfFunction;
                try {
                    positionOfFunction = JSON.returnFunctionPositionFromJSONArray(JSON.readJSONTextFile("functions", Environment.getExternalStorageDirectory() + "/Documents/").getJSONArray("function"), recyclerViewItemArrayList.get(position).getFunctionName()) + 1;
                } catch (JSONException e) {
                    e.printStackTrace();
                    positionOfFunction = 0;
                }

//                while (!callFunction.canSetSelectionOfFunctionSelector){
//                    System.out.println("callF: " + callFunction.canSetSelectionOfFunctionSelector);
//                }

                CallFunction.setUpFunctionEditing(position, listOfAllFunctionParameters.get(position), robotSimulatorMovementCoordinates.get(position).movementType.toString());
                System.out.println("asdf: " + robotSimulatorMovementCoordinates.get(position).movementType.toString());
                CallFunction.populateComponentsForEditing(positionOfFunction);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                if (didSendRobotSimCommand) {
                    recyclerViewAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Unable to reorder because simulation is currently running", Toast.LENGTH_SHORT).show();
                    return false;
                }

                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                Collections.swap(listOfAllFunctionParameters, toPosition, fromPosition);


                if (recyclerViewItemArrayList.get(fromPosition).getFunctionParameters().contains("x") && recyclerViewItemArrayList.get(toPosition).getFunctionParameters().contains("x")) {
                    if (recyclerViewItemArrayList.get(fromPosition).getFunctionParameters().contains("y") && recyclerViewItemArrayList.get(toPosition).getFunctionParameters().contains("y")) {
                        int robotPointsOffSet = 0;
                        for (int i = 0; i < fromPosition; i++) {
                            if (!recyclerViewItemArrayList.get(i).getFunctionParameters().contains("x") && !recyclerViewItemArrayList.get(i).getFunctionParameters().contains("y")) {
                                robotPointsOffSet++;
                            }
                        }
                        System.out.println("Points offset is: " + robotPointsOffSet);
                        Collections.swap(allCoordinates, fromPosition - robotPointsOffSet + 1, toPosition - robotPointsOffSet + 1);
                        drawer.drawPointAt(allCoordinates);
                        Collections.swap(robotSimulatorMovementCoordinates, fromPosition - robotPointsOffSet, toPosition - robotPointsOffSet);
                    }
                }

                Collections.swap(recyclerViewItemArrayList, fromPosition, toPosition);
                recyclerViewAdapter.notifyItemMoved(fromPosition, toPosition);

                if (currentFilePath.contains("AutosavedFiles")) {
                    JSON.reorderFunctionsInProgram(fromPosition + 1, toPosition + 1, currentFileName, Environment.getExternalStorageDirectory() + "/Innov8rz/AutosavedFiles/");
                } else {
                    JSON.reorderFunctionsInProgram(fromPosition + 1, toPosition + 1, currentFileName, Environment.getExternalStorageDirectory() + "/Innov8rz/");
                }
                return true;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                if (didSendRobotSimCommand) {
                    recyclerViewAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Unable to delete because simulation is currently running", Toast.LENGTH_SHORT).show();
                    return;
                }
                final int deletedRowPosition = viewHolder.getAdapterPosition();
                final RecyclerViewItem currentItem = recyclerViewItemArrayList.get(deletedRowPosition);
                MovementPose currentMP = null;
                Coordinate currentC = null;
                try {
                    currentMP = robotSimulatorMovementCoordinates.get(deletedRowPosition);
                    currentC = allCoordinates.get(deletedRowPosition + 1);
                } catch (Exception ignore) {
                }

                final ArrayList<FunctionReturnFormat> currentParameterArray = listOfAllFunctionParameters.get(deletedRowPosition);

                final MovementPose movementPose = currentMP;
                final Coordinate currentCoordinate = currentC;

                JSONArray jsonArray = null;
                try {
                    jsonArray = JSON.readJSONTextFile(currentFileName, currentFilePath).getJSONArray("program");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final JSONArray currentJSONArray = jsonArray;
                CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator);
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Function deleted", Snackbar.LENGTH_SHORT)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                recyclerViewItemArrayList.add(deletedRowPosition, currentItem);
                                recyclerViewAdapter.notifyDataSetChanged();
                                listOfAllFunctionParameters.add(deletedRowPosition, currentParameterArray);
                                if (currentCoordinate != null) {
                                    allCoordinates.add(deletedRowPosition + 1, currentCoordinate);
                                    drawer.drawPointAt(allCoordinates);
                                }
                                if (movementPose != null) {
                                    robotSimulatorMovementCoordinates.add(deletedRowPosition, movementPose);
                                }
                                JSON.writeJSONToTextFile(currentFileName, currentFilePath, currentJSONArray, JSON.JSONArchitecture.DefaultRobotController_Notation);
                            }
                        });
                snackbar.show();

                if (recyclerViewItemArrayList.get(deletedRowPosition).getFunctionParameters().contains("x:")) {
                    if (recyclerViewItemArrayList.get(deletedRowPosition).getFunctionParameters().contains("y:")) {
                        robotSimulatorMovementCoordinates.remove(deletedRowPosition);
                        allCoordinates.remove(deletedRowPosition + 1);
                    }
                }
                listOfAllFunctionParameters.remove(deletedRowPosition);
                drawer.drawPointAt(allCoordinates);
                recyclerViewItemArrayList.remove(deletedRowPosition);
                recyclerViewAdapter.notifyDataSetChanged();
                if (jsonArray != null) {
                    JSON.removeFromJSONTextFile(currentFileName, currentFilePath, deletedRowPosition + 1, JSON.JSONArchitecture.DefaultRobotController_Notation);
                } else {
                    throw new NullPointerException("Unable to find program file onDeletion (onSwiped) at Line: 224, Main Activity");
                }
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void changeRecyclerViewLayout() {
        RecyclerViewAdapter.isSecondaryRecyclerView_View = !RecyclerViewAdapter.isSecondaryRecyclerView_View;
        buildRecyclerView();
    }

    private void addToRecyclerView(String functionName, ArrayList<FunctionReturnFormat> functionParameters) {
        StringBuilder parametersOfFunction = new StringBuilder();

        if (functionParameters.size() >= 1) {
            for (int i = 0; i < functionParameters.size() - 1; i++) {
                parametersOfFunction.append(functionParameters.get(i).parameterName + ": " + functionParameters.get(i).parameterValue + ", ");
            }
            parametersOfFunction.append(functionParameters.get(functionParameters.size() - 1).parameterName + ": " + functionParameters.get(functionParameters.size() - 1).parameterValue);
        }

        recyclerViewItemArrayList.add(new RecyclerViewItem(functionName, parametersOfFunction.toString()));
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void setRecyclerView(String functionName, ArrayList<FunctionReturnFormat> functionParameters, int position) {
        StringBuilder parametersOfFunction = new StringBuilder();

        if (functionParameters.size() >= 1) {
            for (int i = 0; i < functionParameters.size() - 1; i++) {
                parametersOfFunction.append(functionParameters.get(i).parameterName + ": " + functionParameters.get(i).parameterValue + ", ");
            }
            parametersOfFunction.append(functionParameters.get(functionParameters.size() - 1).parameterName + ": " + functionParameters.get(functionParameters.size() - 1).parameterValue);
        }

        recyclerViewItemArrayList.set(position, new RecyclerViewItem(functionName, parametersOfFunction.toString()));
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
                            if (!didSendRobotSimCommand) {
                                com.github.sealstudios.fab.FloatingActionButton robotOrigin = findViewById(R.id.setOrigin);
                                com.github.sealstudios.fab.FloatingActionButton callFunction = findViewById(R.id.callFunction);
                                com.github.sealstudios.fab.FloatingActionButton saveProgram = findViewById(R.id.saveFile);
                                com.github.sealstudios.fab.FloatingActionButton loadProgram = findViewById(R.id.uploadFile);
                                com.github.sealstudios.fab.FloatingActionButton newProgram = findViewById(R.id.newProgram);

                                robotOrigin.setEnabled(!didSendRobotSimCommand);
                                callFunction.setEnabled(!didSendRobotSimCommand);
                                saveProgram.setEnabled(!didSendRobotSimCommand);
                                loadProgram.setEnabled(!didSendRobotSimCommand);
                                newProgram.setEnabled(!didSendRobotSimCommand);

                                if (JSON.doesFileExist("functions", Environment.getExternalStorageDirectory() + "/Documents/")) {
                                    try {
                                        if (JSON.readJSONTextFile("functions", Environment.getExternalStorageDirectory() + "/Documents/").getJSONArray("function").length() >= 1) {
                                            callFunction.setEnabled(true);
                                        } else {
                                            callFunction.setEnabled(false);
                                        }
                                    } catch (Exception ignore) {
                                        callFunction.setEnabled(false);
                                    }
                                } else {
                                    callFunction.setEnabled(false);
                                }
                                if (recyclerViewItemArrayList.size() >= 1) {
                                    saveFunction.setEnabled(true);
                                } else {
                                    saveFunction.setEnabled(false);
                                }

                                File f = new File(Environment.getExternalStorageDirectory() + "/Innov8rz/");

                                FilenameFilter filter = new FilenameFilter() {
                                    @Override
                                    public boolean accept(File f, String name) {
                                        return name.endsWith(".txt");
                                    }
                                };

                                if (f.list(filter).length >= 1) {
                                    loadFunction.setEnabled(true);
                                } else {
                                    loadFunction.setEnabled(false);
                                }


                                if (!didSendRobotSimCommand) {
                                    drawer.drawPointAt(allCoordinates);
                                }
                            } else {
                                com.github.sealstudios.fab.FloatingActionButton robotOrigin = findViewById(R.id.setOrigin);
                                com.github.sealstudios.fab.FloatingActionButton callFunction = findViewById(R.id.callFunction);
                                com.github.sealstudios.fab.FloatingActionButton saveProgram = findViewById(R.id.saveFile);
                                com.github.sealstudios.fab.FloatingActionButton loadProgram = findViewById(R.id.uploadFile);
                                com.github.sealstudios.fab.FloatingActionButton newProgram = findViewById(R.id.newProgram);

                                robotOrigin.setEnabled(!didSendRobotSimCommand);
                                callFunction.setEnabled(!didSendRobotSimCommand);
                                saveProgram.setEnabled(!didSendRobotSimCommand);
                                loadProgram.setEnabled(!didSendRobotSimCommand);
                                newProgram.setEnabled(!didSendRobotSimCommand);
                            }
                            MainActivity.super.invalidateOptionsMenu();
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

    /**
     * Menu Item Creation and OnClickListeners
     **/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.simulate:
                robotSim = new RobotSim(getApplicationContext(), findViewById(android.R.id.content).getRootView(), robotOrigin, MainActivity.this, allCoordinates);
                robotSim.startMovement(robotSimulatorMovementCoordinates);
                didSendRobotSimCommand = true;
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (robotSimulatorMovementCoordinates.size() >= 1) {
            if (Odometry.runThread && didSendRobotSimCommand) {
                return false;
            } else {
                didSendRobotSimCommand = false;
                return true;
            }
        } else {
            return false;
        }

    }

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }

    /**
     * Callback functions from Dialogs
     **/

    @Override
    public void setRobotOrigin(Pose robotOrigin) {
        if (previousX == 0 && previousY == 0) {
            previousX = robotOrigin.x;
            previousY = robotOrigin.y;
        }
        setOrigin(robotOrigin, false);
    }

    private void setOrigin(Pose robotOrigin, boolean isNewFile) {
        this.robotOrigin = robotOrigin;
        if (isNewFile) {
            allCoordinates.clear();
        } else {
            allCoordinates.remove(0);
        }
        allCoordinates.add(0, new Coordinate(robotOrigin.x, robotOrigin.y));
        drawer.drawPointAt(allCoordinates);
        if (doesHaveToCreateNewFile) {
            String fileName = "program";
            allCoordinates.clear();
            allCoordinates.add(new Coordinate(robotOrigin.x, robotOrigin.y));
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss");
            Date date = new Date();
            fileName += "-" + formatter.format(date);
            JSON.createFile(fileName, Environment.getExternalStorageDirectory() + "/Innov8rz/AutosavedFiles/");
            doesHaveToCreateNewFile = false;
            currentFileName = fileName;
            currentFilePath = Environment.getExternalStorageDirectory() + "/Innov8rz/AutosavedFiles/";
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("x", robotOrigin.x);
                jsonObject.put("y", robotOrigin.y);
                jsonObject.put("heading", Math.toDegrees(robotOrigin.heading));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSON.appendJSONToTextFile(currentFileName, currentFilePath, jsonObject, null, JSON.JSONArchitecture.DefaultRobotController_Notation);
        } else {
            try {
                JSONArray jsonArray = JSON.readJSONTextFile(currentFileName, currentFilePath).getJSONArray("program");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("x", robotOrigin.x);
                jsonObject.put("y", robotOrigin.y);
                jsonObject.put("heading", Math.toDegrees(robotOrigin.heading));
                jsonArray.put(0, jsonObject);
                JSON.writeJSONToTextFile(currentFileName, currentFilePath, jsonArray, JSON.JSONArchitecture.DefaultRobotController_Notation);
            } catch (Exception ignore) {
                try {
                    JSONObject fileObject = new JSONObject();
                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("x", robotOrigin.x);
                    jsonObject.put("y", robotOrigin.y);
                    jsonObject.put("heading", Math.toDegrees(robotOrigin.heading));
                    jsonArray.put(jsonObject);
                    fileObject.put("program", jsonArray);
                    JSON.writeJSONToTextFile(currentFileName, currentFilePath, fileObject);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    ArrayList<ArrayList<FunctionReturnFormat>> listOfAllFunctionParameters = new ArrayList<>();
    String currentFileName;
    String currentFilePath;
    double previousX = 0, previousY = 0;

    @Override
    public void callFunction(String functionName, ArrayList<FunctionReturnFormat> functionParameters, boolean isDrivetrainFunction, boolean isEditing, int positionOfEdit, String movementType) {
        ArrayList<Object> nullValidation = new ArrayList<>();
        nullValidation.add(functionName);
        nullValidation.add(functionParameters);
        if (!isNull(nullValidation)) {
            String fileName = "program";
            if (!isEditing) {
                if (doesHaveToCreateNewFile) {
                    allCoordinates.clear();
                    allCoordinates.add(new Coordinate(robotOrigin.x, robotOrigin.y));
                    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss");
                    Date date = new Date();
                    fileName += "-" + formatter.format(date);
                    JSON.createFile(fileName, Environment.getExternalStorageDirectory() + "/Innov8rz/AutosavedFiles/");
                    doesHaveToCreateNewFile = false;
                    currentFileName = fileName;
                    currentFilePath = Environment.getExternalStorageDirectory() + "/Innov8rz/AutosavedFiles/";
                    listOfAllFunctionParameters.add(functionParameters);
                    addToRecyclerView(functionName, functionParameters);
                    JSON.addFunctionFromProgramToFile(fileName, functionName, movementType, functionParameters, null);
                } else {
                    listOfAllFunctionParameters.add(functionParameters);
                    addToRecyclerView(functionName, functionParameters);
                    if (currentFileName.contains("program")) {
                        JSON.addFunctionFromProgramToFile(currentFileName, functionName, movementType, functionParameters, null);
                    } else {
                        JSON.addFunctionFromProgramToFile(currentFileName, functionName, movementType, functionParameters, Environment.getExternalStorageDirectory() + "/Innov8rz/");
                    }
                }
                if (isDrivetrainFunction) {
                    double x = 0, y = 0, theta = 0;
                    for (int i = 0; i < functionParameters.size(); i++) {
                        if (functionParameters.get(i).parameterName.equals("x")) {
                            if (functionParameters.get(i).parameterValue instanceof Integer) {
                                x = (double) ((Integer) functionParameters.get(i).parameterValue).intValue();
                            } else {
                                x = (double) functionParameters.get(i).parameterValue;
                            }
                        } else if (functionParameters.get(i).parameterName.equals("y")) {
                            if (functionParameters.get(i).parameterValue instanceof Integer) {
                                y = (double) ((Integer) functionParameters.get(i).parameterValue).intValue();
                            } else {
                                y = (double) functionParameters.get(i).parameterValue;
                            }
                        } else if (functionParameters.get(i).parameterName.equals("heading")) {
                            if (functionParameters.get(i).parameterValue instanceof Integer) {
                                theta = (double) ((Integer) functionParameters.get(i).parameterValue).intValue();
                            } else {
                                theta = (double) functionParameters.get(i).parameterValue;
                            }
                        }
                    }
                    if (movementType.equals("Strafe")) {
                        robotSimulatorMovementCoordinates.add(new MovementPose(new Pose(x, y, Math.toRadians(theta)), MovementPose.MovementType.strafe));
                        previousX = x;
                        previousY = y;
                        allCoordinates.add(new Coordinate(x, y));
                        drawer.drawPointAt(allCoordinates);
                    } else if (movementType.equals("TankForward")) {
                        robotSimulatorMovementCoordinates.add(new MovementPose(new Pose(x, y, Math.toRadians(0)), MovementPose.MovementType.moveForward));
                        previousX = x;
                        previousY = y;
                        allCoordinates.add(new Coordinate(x, y));
                        drawer.drawPointAt(allCoordinates);
                    } else if (movementType.equals("TankBackward")) {
                        robotSimulatorMovementCoordinates.add(new MovementPose(new Pose(x, y, Math.toRadians(0)), MovementPose.MovementType.moveBackward));
                        previousX = x;
                        previousY = y;
                        allCoordinates.add(new Coordinate(x, y));
                        drawer.drawPointAt(allCoordinates);
                    } else if (movementType.equals("PurePursuitTankForward")) {
                        robotSimulatorMovementCoordinates.add(new MovementPose(new Pose(x, y, Math.toRadians(0)), MovementPose.MovementType.PurePursuitTankForward));
                        previousX = x;
                        previousY = y;
                        allCoordinates.add(new Coordinate(x, y));
                        drawer.drawPointAt(allCoordinates);
                    } else if (movementType.equals("PurePursuitTankBackward")) {
                        robotSimulatorMovementCoordinates.add(new MovementPose(new Pose(x, y, Math.toRadians(0)), MovementPose.MovementType.PurePursuitTankBackward));
                        previousX = x;
                        previousY = y;
                        allCoordinates.add(new Coordinate(x, y));
                        drawer.drawPointAt(allCoordinates);
                    } else if (movementType.equals("PurePursuitStrafe")) {
                        robotSimulatorMovementCoordinates.add(new MovementPose(new Pose(x, y, Math.toRadians(0)), MovementPose.MovementType.PurePursuitStrafe));
                        previousX = x;
                        previousY = y;
                        allCoordinates.add(new Coordinate(x, y));
                        drawer.drawPointAt(allCoordinates);
                    } else if (movementType.equals("Turn")) {
                        robotSimulatorMovementCoordinates.add(new MovementPose(new Pose(previousX, previousY, Math.toRadians(theta)), MovementPose.MovementType.turn));
                    }
                }
            } else {
                listOfAllFunctionParameters.set(positionOfEdit, functionParameters);
                setRecyclerView(functionName, functionParameters, positionOfEdit);
                try {
                    JSONArray filteredJSONArray = new JSONArray();
                    JSONArray jsonArray = JSON.readJSONTextFile(currentFileName, currentFilePath).getJSONArray("program");
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            //Excluding the item at position
//                            if (i != positionOfEdit + 1) {
                            filteredJSONArray.put(jsonArray.getJSONObject(i));
//                            }
                        }
                    }
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("functionName", functionName);
                    if (!movementType.equals("None")) {
                        jsonObject.put("movementType", movementType);
                    }
                    JSONObject parameterObject = new JSONObject();

                    for (int i = 0; i < functionParameters.size(); i++) {
                        parameterObject.put(functionParameters.get(i).parameterName, functionParameters.get(i).parameterValue);
                    }

                    jsonObject.put("parameters", parameterObject);
                    filteredJSONArray.put(positionOfEdit + 1, jsonObject);

                    JSON.writeJSONToTextFile(currentFileName, currentFilePath, filteredJSONArray, JSON.JSONArchitecture.DefaultRobotController_Notation);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (isDrivetrainFunction) {
                    double x = 0, y = 0, theta = 0;
                    for (int i = 0; i < functionParameters.size(); i++) {
                        if (functionParameters.get(i).parameterName.equals("x")) {
                            if (functionParameters.get(i).parameterValue instanceof Integer) {
                                x = (double) ((Integer) functionParameters.get(i).parameterValue).intValue();
                            } else {
                                x = (double) functionParameters.get(i).parameterValue;
                            }
                        } else if (functionParameters.get(i).parameterName.equals("y")) {
                            if (functionParameters.get(i).parameterValue instanceof Integer) {
                                y = (double) ((Integer) functionParameters.get(i).parameterValue).intValue();
                            } else {
                                y = (double) functionParameters.get(i).parameterValue;
                            }
                        } else if (functionParameters.get(i).parameterName.equals("heading")) {
                            if (functionParameters.get(i).parameterValue instanceof Integer) {
                                theta = (double) ((Integer) functionParameters.get(i).parameterValue).intValue();
                            } else {
                                theta = (double) functionParameters.get(i).parameterValue;
                            }
                        }
                    }
                    if (movementType.equals("Strafe")) {
                        robotSimulatorMovementCoordinates.set(positionOfEdit, new MovementPose(new Pose(x, y, Math.toRadians(theta)), MovementPose.MovementType.strafe));
                        allCoordinates.set(positionOfEdit + 1, new Coordinate(x, y));
                        drawer.drawPointAt(allCoordinates);
                    } else if (movementType.equals("TankForward")) {
                        robotSimulatorMovementCoordinates.set(positionOfEdit, new MovementPose(new Pose(x, y, Math.toRadians(0)), MovementPose.MovementType.moveForward));
                        allCoordinates.set(positionOfEdit + 1, new Coordinate(x, y));
                        drawer.drawPointAt(allCoordinates);
                    } else if (movementType.equals("TankBackward")) {
                        robotSimulatorMovementCoordinates.set(positionOfEdit, new MovementPose(new Pose(x, y, Math.toRadians(0)), MovementPose.MovementType.moveBackward));
                        allCoordinates.set(positionOfEdit + 1, new Coordinate(x, y));
                        drawer.drawPointAt(allCoordinates);
                    } else if (movementType.equals("PurePursuitTankForward")) {
                        robotSimulatorMovementCoordinates.set(positionOfEdit, new MovementPose(new Pose(x, y, Math.toRadians(0)), MovementPose.MovementType.PurePursuitTankForward));
                        allCoordinates.set(positionOfEdit + 1, new Coordinate(x, y));
                        drawer.drawPointAt(allCoordinates);
                    } else if (movementType.equals("PurePursuitTankBackward")) {
                        robotSimulatorMovementCoordinates.set(positionOfEdit, new MovementPose(new Pose(x, y, Math.toRadians(0)), MovementPose.MovementType.PurePursuitTankBackward));
                        allCoordinates.set(positionOfEdit + 1, new Coordinate(x, y));
                        drawer.drawPointAt(allCoordinates);
                    } else if (movementType.equals("PurePursuitStrafe")) {
                        robotSimulatorMovementCoordinates.set(positionOfEdit, new MovementPose(new Pose(x, y, Math.toRadians(0)), MovementPose.MovementType.PurePursuitStrafe));
                        allCoordinates.set(positionOfEdit + 1, new Coordinate(x, y));
                        drawer.drawPointAt(allCoordinates);
                    } else if (movementType.equals("Turn")) {
                        robotSimulatorMovementCoordinates.add(new MovementPose(new Pose(previousX, previousY, Math.toRadians(theta)), MovementPose.MovementType.turn));
                    }

                }
            }
        }
    }

    public boolean isNull(ArrayList<Object> objects) {
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) == null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void saveProgram(String fileName) {
        if (isEditingLoadedFile) {
            Toast.makeText(getApplicationContext(), "Since you are editing a loaded file, a duplicate will be made", Toast.LENGTH_LONG).show();
            try {
                JSONObject jsonObject = JSON.readJSONTextFile(currentFileName, currentFilePath);
                JSONArray jsonArray = jsonObject.getJSONArray("program");
                JSON.writeJSONToTextFile(fileName, Environment.getExternalStorageDirectory() + "/Innov8rz/", jsonArray, JSON.JSONArchitecture.DefaultRobotController_Notation);
            } catch (Exception e) {
                e.printStackTrace();
            }
            currentFileName = fileName.replace(".txt", "");
            currentFilePath = Environment.getExternalStorageDirectory() + "/Innov8rz/";
            isEditingLoadedFile = false;
        } else {
            File currentFile = new File(currentFilePath + currentFileName + ".txt");
            File newFileName = new File(Environment.getExternalStorageDirectory() + "/Innov8rz/" + fileName);
            if (!fileName.contains(".txt")) {
                newFileName = new File(Environment.getExternalStorageDirectory() + "/Innov8rz/" + fileName + ".txt");
            }

            if (currentFile.renameTo(newFileName)) {
                currentFileName = fileName.replace(".txt", "");
                currentFilePath = Environment.getExternalStorageDirectory() + "/Innov8rz/";
            } else {
                System.out.println("Failed in renaming file");
            }
        }
    }

    @Override
    public void loadProgram(String fileName, ArrayList<LoadFileReturnFormat> fileFunctions, Pose robotOrigin) {
        robotSimulatorMovementCoordinates.clear();
        recyclerViewItemArrayList.clear();
        recyclerViewAdapter.notifyDataSetChanged();
        isEditingLoadedFile = true;
        currentFileName = fileName;
        currentFilePath = Environment.getExternalStorageDirectory() + "/Innov8rz/";
        doesHaveToCreateNewFile = false;
        setOrigin(robotOrigin, true);

        if (fileFunctions.size() >= 1) {
            for (int i = 0; i < fileFunctions.size(); i++) {
                ArrayList<FunctionReturnFormat> functionParameters = new ArrayList<>();
                for (int x = 0; x < fileFunctions.get(i).parameters.size(); x++) {
                    functionParameters.add(new FunctionReturnFormat(fileFunctions.get(i).parameters.get(x).parameterName, fileFunctions.get(i).parameters.get(x).parameterValue));
                }
                addToRecyclerView(fileFunctions.get(i).functionName, fileFunctions.get(i).parameters);
                if (fileFunctions.get(i).isDrivetrain) {
                    double x = 0, y = 0, heading = 0;
                    for (int a = 0; a < fileFunctions.get(i).parameters.size(); a++) {
                        if (fileFunctions.get(i).parameters.get(a).parameterName.equals("x")) {
                            if (fileFunctions.get(i).parameters.get(a).parameterValue instanceof Integer) {
                                x = (double) ((Integer) fileFunctions.get(i).parameters.get(a).parameterValue).intValue();
                            } else {
                                x = (double) fileFunctions.get(i).parameters.get(a).parameterValue;
                            }
                        } else if (fileFunctions.get(i).parameters.get(a).parameterName.equals("y")) {
                            if (fileFunctions.get(i).parameters.get(a).parameterValue instanceof Integer) {
                                y = (double) ((Integer) fileFunctions.get(i).parameters.get(a).parameterValue).intValue();
                            } else {
                                y = (double) fileFunctions.get(i).parameters.get(a).parameterValue;
                            }
                        } else if (fileFunctions.get(i).parameters.get(a).parameterName.equals("heading")) {
                            if (fileFunctions.get(i).parameters.get(a).parameterValue instanceof Integer) {
                                heading = (double) ((Integer) fileFunctions.get(i).parameters.get(a).parameterValue).intValue();
                            } else {
                                heading = (double) fileFunctions.get(i).parameters.get(a).parameterValue;
                            }
                        }
                    }
                    allCoordinates.add(new Coordinate(x, y));
                    drawer.drawPointAt(allCoordinates);
                    if (!fileFunctions.get(i).movementType.equals("None")) {
                        if (fileFunctions.get(i).movementType.equals("Strafe")) {
                            robotSimulatorMovementCoordinates.add(new MovementPose(new Pose(x, y, Math.toRadians(heading)), MovementPose.MovementType.strafe));
                        } else if (fileFunctions.get(i).movementType.equals("TankForward")) {
                            robotSimulatorMovementCoordinates.add(new MovementPose(new Pose(x, y, Math.toRadians(0)), MovementPose.MovementType.moveForward));
                        } else if (fileFunctions.get(i).movementType.equals("TankBackward")) {
                            robotSimulatorMovementCoordinates.add(new MovementPose(new Pose(x, y, Math.toRadians(0)), MovementPose.MovementType.moveBackward));
                        } else if (fileFunctions.get(i).movementType.equals("PurePursuitTankForward")) {
                            robotSimulatorMovementCoordinates.add(new MovementPose(new Pose(x, y, Math.toRadians(0)), MovementPose.MovementType.PurePursuitTankForward));
                        } else if (fileFunctions.get(i).movementType.equals("PurePursuitTankBackward")) {
                            robotSimulatorMovementCoordinates.add(new MovementPose(new Pose(x, y, Math.toRadians(0)), MovementPose.MovementType.PurePursuitTankBackward));
                        } else if (fileFunctions.get(i).movementType.equals("PurePursuitStrafe")) {
                            robotSimulatorMovementCoordinates.add(new MovementPose(new Pose(x, y, Math.toRadians(0)), MovementPose.MovementType.PurePursuitStrafe));
                        }
                    }
                }
                listOfAllFunctionParameters.add(functionParameters);
            }
        } else {
            Toast.makeText(this, "Selected file is empty", Toast.LENGTH_SHORT).show();
        }
    }
}
