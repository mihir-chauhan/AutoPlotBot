package com.example.odometryapp_v10.Main;

import java.util.ArrayList;

public class LoadFileReturnFormat {

    public String functionName, movementType;
    public ArrayList<FunctionReturnFormat> parameters;
    public boolean isDrivetrain;

    public LoadFileReturnFormat(String functionName, String movementType,  ArrayList<FunctionReturnFormat> parameters, boolean isDrivetrain) {
        this.functionName = functionName;
        this.parameters = parameters;
        this.isDrivetrain = isDrivetrain;
        this.movementType = movementType;
    }
}
