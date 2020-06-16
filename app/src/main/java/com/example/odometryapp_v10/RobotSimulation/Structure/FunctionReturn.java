package com.example.odometryapp_v10.RobotSimulation.Structure;

public class FunctionReturn {

    public boolean success;
    public Object data;
    public String errorMessage;

    public FunctionReturn(boolean success, Object data, String errorMessage) {
        this.success = success;
        this.data = data;
        this.errorMessage = errorMessage;
    }

}
