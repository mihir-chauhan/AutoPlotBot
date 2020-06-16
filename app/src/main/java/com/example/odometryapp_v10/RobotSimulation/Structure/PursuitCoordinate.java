package com.example.odometryapp_v10.RobotSimulation.Structure;
public class PursuitCoordinate {
    public double x;
    public double y;
    public boolean isStrafing;

    public PursuitCoordinate(double x, double y, boolean isStrafing) {
        this.x = x;
        this.y = y;
        this.isStrafing = isStrafing;
    }
}