package com.example.odometryapp_v10.RobotSimulation;

import internal.Structure.Pose;

public class Print {
	public static String getPoseString(String caption, Pose pose) {
		return (caption + ": " + pose.x + ", " + pose.y + " , " + pose.heading);
	}

	public static String getObjectString(String caption, Object[] objects) {
		return (caption + ": " + objects.toString());
	}
}
