package com.example.odometryapp_v10.RobotSimulation;


import com.example.odometryapp_v10.RobotSimulation.Structure.Pose;

public class MovementPose {

	public enum MovementType {
		move, strafe
	}

	public Pose position;
	public MovementType movementType;

	MovementPose(Pose position, MovementType movementType) {
		this.position = position;
		this.movementType = movementType;
	}
}
