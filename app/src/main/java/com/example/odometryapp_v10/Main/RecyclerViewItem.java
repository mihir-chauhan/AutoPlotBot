package com.example.odometryapp_v10.Main;

public class RecyclerViewItem {
    private String functionName;
    private String functionParameters;

    public RecyclerViewItem(String functionName, String functionParameters) {
        this.functionName = functionName;
        this.functionParameters = functionParameters;
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getFunctionParameters() {
        return functionParameters;
    }
}
