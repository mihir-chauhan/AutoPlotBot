package com.example.odometryapp_v10.RobotSimulation.Structure;

import java.util.ArrayList;

public class Path {

	ArrayList<Pose> waypoints;
	public double power;

	public Path(ArrayList<Pose> points, double power) {
		waypoints = points;
		this.power = power;
	}

	public int size() {
		return waypoints.size();
	}

	public Pose getPoint(int index) {
		return waypoints.get(index);
	}

}
