package com.example.odometryapp_v10.RobotSimulation.Structure;

import internal.Skystone.MecanumDrivetrain;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import org.firstinspires.ftc.robotcore.external.Telemetry;
//import org.firstinspires.ftc.teamcode.Robot.Skystone.MecanumDrivetrain;
import main.BackCalculation;
import main.RobotSim;

public class Odometry {

	public static Pose startPosition, currentPosition = new Pose(0, 0, 0);

	static final int left_calibration_encoders = 2450; // 2283; // 2493 // 2440
	static final int right_calibrarion_encoders = 2450; // 632; // 2406 // 2466
	static final int center_calibration_encoders = -13; // -13; // 27.4 // -171

	public static final double wheelDiameterInches = 38 / 25.4;
	public static final double ticksPerRev = 1000;
	public static double circumferenceOfWheel = Math.PI * wheelDiameterInches;
	public static double inchesPerTick = circumferenceOfWheel / ticksPerRev;

	static final double left_calibration_inches = left_calibration_encoders * inchesPerTick;
	static final double right_calibration_inches = right_calibrarion_encoders * inchesPerTick;
	static final double center_calibration_inches = center_calibration_encoders * inchesPerTick;

	static final double d = (2 / Math.PI) * (left_calibration_inches + right_calibration_inches);
	static final double centerInchesPerRadian = center_calibration_inches / (Math.PI / 2);

	public Odometry(Pose startingPose) {
		this.startPosition = startingPose;
		currentPosition = new Pose(0.0, 0.0, startingPose.heading);
	}

	public void reset() {
		currentPosition = new Pose(0.0, 0.0, startPosition.heading);
	}

	public static synchronized Pose getCurrentPose() {
		return new Pose(currentPosition.x + startPosition.x, currentPosition.y + startPosition.y,
				currentPosition.heading);
	}

	public static synchronized Coordinate getCurrentCoordinate() {
		return new Coordinate(currentPosition.x + startPosition.x, currentPosition.y + startPosition.y);
	}

	public synchronized void updatePosition() {
		currentPosition = calculateCurrentPosition(currentPosition, BackCalculation.deltaTick_LeftEncoder(),
				BackCalculation.deltaTick_RightEncoder(), BackCalculation.deltaTick_CenterEncoder());
	}

	public Pose calculateCurrentPosition(Pose previousPosition, double dl, double dr, double dc) {
		double arc_len_center = (dl + dr) / 2;
		double phi = (dr - dl) / d;
		double currentHeading = previousPosition.heading + phi;

		double delta_x = (arc_len_center * Math.cos(currentHeading));
		double delta_y = (arc_len_center * Math.sin(currentHeading));

		double arc_len_strafe = phi * centerInchesPerRadian;
		double strafe = dc - arc_len_strafe;
		double strafe_x = (strafe * Math.sin(currentHeading));
		double strafe_y = -(strafe * Math.cos(currentHeading));

		return new Pose((previousPosition.x + delta_x + strafe_x), (previousPosition.y + delta_y + strafe_y),
				currentHeading);
	}

	Thread backgroundPositionThread;

	Pose updatedPosition;
	boolean runThread = true;

	public void startBackgroundPositionUpdates() {
		backgroundPositionThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (runThread) {
					updatePosition();
					updatedPosition = getCurrentPose();
					RobotSim.setPosition(updatedPosition.x, updatedPosition.y);
					RobotSim.setHeading(updatedPosition.heading);
				}
			}
		});
		backgroundPositionThread.setPriority(Thread.MAX_PRIORITY);
		backgroundPositionThread.setName("Odometry Background Position Thread");
		backgroundPositionThread.start();
	}

	public void stopBackgroundPositionUpdates() {
		runThread = false;
		backgroundPositionThread.interrupt();
	}

	public void sleep(long ms) {
		long currentTime = System.currentTimeMillis();
		while (currentTime + ms > System.currentTimeMillis()) {
		}
	}

	public Double angleToTravel(Pose endCoordinate, MecanumDrivetrain.TankDirection direction) {
		double opposite = 0, adjacent = 0, angle = 0, inverseAngle = 0.0;
		opposite = endCoordinate.y - getCurrentPose().y;
		adjacent = endCoordinate.x - getCurrentPose().x;
		angle = (Math.atan2(opposite, adjacent) + Math.PI * 2) % (Math.PI * 2);
		inverseAngle = (angle + Math.PI) % (2 * Math.PI);

		if (direction == MecanumDrivetrain.TankDirection.backward) {
			return inverseAngle;
		} else {
			return angle;
		}

	}

}