package com.example.odometryapp_v10.Main;

import java.util.ArrayList;

public class LoadFileReturnFormat {

    public String functionName;
    public ArrayList<FunctionReturnFormat> parameters;

    public LoadFileReturnFormat(String functionName, ArrayList<FunctionReturnFormat> parameters) {
        this.functionName = functionName;
        this.parameters = parameters;
    }
}
