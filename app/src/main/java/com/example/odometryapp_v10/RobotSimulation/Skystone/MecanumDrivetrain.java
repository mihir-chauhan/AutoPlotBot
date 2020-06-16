package com.example.odometryapp_v10.RobotSimulation.Skystone;

import internal.Structure.Odometry;
import internal.Structure.Path;
import internal.Structure.Pose;
import internal.Structure.PurePursuitController;
import main.BackCalculation;
import main.RobotSim;

public class MecanumDrivetrain {
	private static double previousDistance;
	public static final double odometryGainMultiplier = 2.5; // 3.5
	static int i = 0;

	public enum TankDirection {
		forward, backward
	}

	private static void resetOvershoot() {
		i = 0;
	}

	private static boolean overshootDistance(double currentDistance) {
		if (i == 0) {
			previousDistance = currentDistance;
			i++;
		}
		// input should be lower than previous distance
		if (currentDistance - previousDistance > 0.5) {// currentDistance > previousDistance) {
			i = 0;
			return true;
		} else {
			previousDistance = currentDistance;
			return false;
		}
	}

	static Odometry odometry = new Odometry(RobotSim.startingPosition);

	static Thread mecanumThread;

	public static void strafeToPosition(Pose targetCoordinate, double movementPower) {
		double movementHeading = targetCoordinate.heading;
		double r = movementPower;// maxPower
		double robotAngle, v1, v2, v3, v4;
		double targetHeading = movementHeading;
		double currAngle;
		double subtractNumberForHeading;

		resetOvershoot();
		Pose currentPose = odometry.getCurrentPose();
		long currentTime = System.currentTimeMillis();
		double dist = currentPose.distanceToPose(targetCoordinate);
		while ((currentPose.distanceToPose(targetCoordinate) > 10)
				&& !overshootDistance(currentPose.distanceToPose(targetCoordinate))) {
			currentPose = odometry.getCurrentPose();
			robotAngle = Math.atan2((targetCoordinate.y - odometry.getCurrentPose().y),
					(targetCoordinate.x - odometry.getCurrentPose().x)) + Math.PI / 4
					- odometry.getCurrentPose().heading;
			v1 = r * Math.cos(robotAngle);
			v2 = r * Math.sin(robotAngle);
			v3 = r * Math.sin(robotAngle);
			v4 = r * Math.cos(robotAngle);
			currAngle = odometry.getCurrentPose().heading % (2 * Math.PI);

			subtractNumberForHeading = (targetHeading - currAngle) / 4;
			BackCalculation.setFrontLeftPower(v1 - subtractNumberForHeading);
			BackCalculation.setFrontRightPower(v2 + subtractNumberForHeading);
			BackCalculation.setBackLeftPower(v3 - subtractNumberForHeading);
			BackCalculation.setBackRightPower(v4 + subtractNumberForHeading);
			currAngle = odometry.getCurrentPose().heading % (2 * Math.PI);
		}
		BackCalculation.setFrontLeftPower(0);
		BackCalculation.setFrontRightPower(0);
		BackCalculation.setBackLeftPower(0);
		BackCalculation.setBackRightPower(0);
	}

	public static void strafeTowardsPosition(Pose targetPose, double movementPower) {
		double angle1 = targetPose.heading;
		double movementHeading = angle1;
		double r = movementPower;// maxPower
		double robotAngle, v1, v2, v3, v4;
		double targetHeading = movementHeading;
		double currAngle;
		double subtractNumberForHeading;
		resetOvershoot();
		Pose currentPose = odometry.getCurrentPose();
		long currentTime = System.currentTimeMillis();
		double dist = currentPose.distanceToPose(targetPose);
		while ((currentPose.distanceToPose(targetPose) > 1
				&& !overshootDistance(currentPose.distanceToPose(targetPose)))) {
			currentPose = odometry.getCurrentPose();
			robotAngle = Math.atan2((targetPose.y - odometry.getCurrentPose().y),
					(targetPose.x - odometry.getCurrentPose().x)) + Math.PI / 4 - odometry.getCurrentPose().heading;
			v1 = r * Math.cos(robotAngle);
			v2 = r * Math.sin(robotAngle);
			v3 = r * Math.sin(robotAngle);
			v4 = r * Math.cos(robotAngle);
			currAngle = odometry.getCurrentPose().heading % (2 * Math.PI);
			subtractNumberForHeading = (targetHeading - currAngle) / 4;
			BackCalculation.setFrontLeftPower(v1 - subtractNumberForHeading);
			BackCalculation.setFrontRightPower(v2 + subtractNumberForHeading);
			BackCalculation.setBackLeftPower(v3 - subtractNumberForHeading);
			BackCalculation.setBackRightPower(v4 + subtractNumberForHeading);
			return;
		}
	}

	public static void rampTankToPosition(Pose targetPose, double movementPower, TankDirection direction,
			boolean stop) {
		double angleToTravel = odometry.angleToTravel(targetPose, direction);
//		turnToHeading(angleToTravel, movementPower);
		tankToPosition(targetPose, movementPower, direction, stop);
//		turnToHeading(angleToTravel, movementPower);
		if (stop) {
			BackCalculation.setFrontLeftPower(0);
			BackCalculation.setFrontRightPower(0);
			BackCalculation.setBackLeftPower(0);
			BackCalculation.setBackRightPower(0);
		}
	}

	public static void tankToPosition(Pose targetPose, double movementPower, TankDirection direction, boolean stop) {
		Pose currentPosition = odometry.getCurrentPose();
		double distance = currentPosition.distanceToPose(targetPose);
		double currAngle = odometry.getCurrentPose().heading;

		double deltaX = targetPose.x - odometry.getCurrentPose().x;
		double deltaY = targetPose.y - odometry.getCurrentPose().y;
		double ratio = (deltaX) / (deltaY);

		double ppX;
		double ppY;

		if (deltaX < 0 && deltaY < 0) { // 3rd quadrant
			ppX = targetPose.x - ratio * 10;
			ppY = targetPose.y - 10;
		} else if (deltaX > 0 && deltaY < 0) { // 4th quadrant
			ppX = targetPose.x - ratio * 10;
			ppY = targetPose.y - 10;
		} else if (deltaX < 0 && deltaY > 0) { // 2nd quadrant
			ppX = targetPose.x + ratio * 10;
			ppY = targetPose.y + 10;
		} else if (deltaX > 0 && deltaY > 0) { // 1st quadrant
			ppX = targetPose.x + ratio * 10;
			ppY = targetPose.y + 10;
		} else if (deltaX == 0) {
			ppX = targetPose.x;
			if (targetPose.y > odometry.getCurrentPose().y) {
				ppY = targetPose.y + 10;
			} else {
				ppY = targetPose.y - 10;
			}
		} else if (deltaY == 0) {
			ppY = targetPose.y;
			if (targetPose.x > odometry.getCurrentPose().x) {
				ppX = targetPose.x + 10;
			} else {
				ppX = targetPose.y - 10;
			}
		} else {
			return;
		}
		double powerToMove = (direction == TankDirection.backward) ? -1 * Math.abs(movementPower)
				: Math.abs(movementPower);
		double desiredHeading;
		double gain = 0;
		double error;

		BackCalculation.setFrontLeftPower(movementPower);
		BackCalculation.setFrontRightPower(movementPower);
		BackCalculation.setBackLeftPower(movementPower);
		BackCalculation.setBackRightPower(movementPower);

		if (stop) {
			return;
		}

		resetOvershoot();

		long currentTime = System.currentTimeMillis();
		while ((currentPosition.distanceToPose(targetPose) > 0.5
				&& !overshootDistance(currentPosition.distanceToPose(targetPose)))) {
			currentPosition = odometry.getCurrentPose();

			distance = currentPosition.distanceToPose(targetPose);
			desiredHeading = odometry.angleToTravel(new Pose(ppX, ppY, targetPose.heading), direction);
			currAngle = odometry.getCurrentPose().heading;

			error = desiredHeading - currAngle;
			gain = Math.atan2(Math.sin(error), Math.cos(error)) * odometryGainMultiplier;
			BackCalculation.setFrontLeftPower(checkPower(powerToMove - gain, 0.6));
			BackCalculation.setFrontRightPower(checkPower(powerToMove + gain, 0.6));
			BackCalculation.setBackLeftPower(checkPower(powerToMove - gain, 0.6));
			BackCalculation.setBackRightPower(checkPower(powerToMove + gain, 0.6));
			return;
		}
	}

	public static void turnToHeading(double targetHeading, double inputPower) {
		double currAngle = odometry.getCurrentPose().heading % (2 * Math.PI);
		if (targetHeading - currAngle > Math.toRadians(180)) {
			targetHeading -= Math.toRadians(360);
		}
		while (Math.abs(targetHeading - currAngle) > 0.0175) {
			if (targetHeading - currAngle > 0) {
				BackCalculation.setFrontLeftPower(-inputPower);
				BackCalculation.setFrontRightPower(inputPower);
				BackCalculation.setBackLeftPower(-inputPower);
				BackCalculation.setBackRightPower(inputPower);
			} else if (targetHeading - currAngle < 0) {
				BackCalculation.setFrontLeftPower(inputPower);
				BackCalculation.setFrontRightPower(-inputPower);
				BackCalculation.setBackLeftPower(inputPower);
				BackCalculation.setBackRightPower(-inputPower);
			}
			currAngle = odometry.getCurrentPose().heading % (2 * Math.PI);
			return;
		}

		BackCalculation.setFrontLeftPower(0);
		BackCalculation.setFrontRightPower(0);
		BackCalculation.setBackLeftPower(0);
		BackCalculation.setBackRightPower(0);
	}

	public static Double checkPower(double powerToMove, double maxPower) {
		if (powerToMove > maxPower) {
			return maxPower;
		} else if (powerToMove < -maxPower) {
			return -maxPower;
		} else {
			return powerToMove;
		}
	}

//	static RobotSim rs;

	public static void startBackgroundPositionUpdates(Path path, double movementPower) {
//		rs = new RobotSim();
		mecanumThread = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Started Movement.");
				executePath(path);
				System.out.println("Done Moving.");
				mecanumThread.interrupt();
			}
		});
		mecanumThread.setPriority(Thread.MAX_PRIORITY);
		mecanumThread.setName("Odometry Background Position Thread");
		mecanumThread.start();
	}

	static PurePursuitController purePursuitController = new PurePursuitController();

	public static void executePath(Path path) {
		purePursuitController.clearPath();
		purePursuitController.setPath(path);
		Pose targetPose = purePursuitController.getNextPose(odometry.getCurrentPose());
		while (targetPose != null) {
			if (purePursuitController.targetIsFinalPose()) {
				calculatePowerFromPosition(odometry.getCurrentPose(), targetPose, path.power, true);
				break;
			} else {
				calculatePowerFromPosition(odometry.getCurrentPose(), targetPose, path.power, false);
			}
			targetPose = purePursuitController.getNextPose(odometry.getCurrentPose());
			sleep(10);
		}
	}

	public static double closestAngle(double angle1, double angle2, double currentAngle) {
		currentAngle += (Math.PI * 2);
		currentAngle %= (Math.PI * 2);

		double delta_angle1 = (Math.abs(currentAngle - angle1)); // + Math.PI) % (Math.PI);
		double delta_angle2 = (Math.abs(currentAngle - angle2)); // + Math.PI) % (Math.PI);

		if (delta_angle1 > Math.PI) {
			delta_angle1 = 2 * Math.PI - delta_angle1;
		}
		if (delta_angle2 > Math.PI) {
			delta_angle2 = 2 * Math.PI - delta_angle2;
		}

		if (delta_angle1 < delta_angle2) {
			return angle1;
		}
		return angle2;
	}

	public static void calculatePowerFromPosition(Pose currentPosition, Pose targetPose, double powerToMove,
			boolean stop) {
		if (stop) {
			BackCalculation.setFrontLeftPower(0.0);
			BackCalculation.setFrontRightPower(0.0);
			BackCalculation.setBackLeftPower(0.0);
			BackCalculation.setBackRightPower(0.0);
			return;
		}
		double distance = currentPosition.distanceToPose(targetPose);
		double currAngle = odometry.getCurrentPose().heading;
		double angle1 = odometry.angleToTravel(new Pose(targetPose.x, targetPose.y, targetPose.heading),
				TankDirection.forward) % (Math.PI * 2);
		double angle2 = odometry.angleToTravel(new Pose(targetPose.x, targetPose.y, targetPose.heading),
				TankDirection.backward) % (Math.PI * 2);
		double desiredHeading = closestAngle(angle1, angle2, currAngle);
		System.out.println("angle1: " + angle1 + ", angle2: " + angle2 + ", currentAngle: " + currAngle
				+ ", angleToTravel: " + desiredHeading);

		if (desiredHeading == angle2) {
			powerToMove *= -1;
		}
		double error = desiredHeading - currAngle;
		double gain = Math.atan2(Math.sin(error), Math.cos(error)) * odometryGainMultiplier;
		BackCalculation.setFrontLeftPower(checkPower(powerToMove - gain, 0.6));
		BackCalculation.setFrontRightPower(checkPower(powerToMove + gain, 0.6));
		BackCalculation.setBackLeftPower(checkPower(powerToMove - gain, 0.6));
		BackCalculation.setBackRightPower(checkPower(powerToMove + gain, 0.6));
		return;
	}

	public static void sleep(long ms) {
		long currentTime = System.currentTimeMillis();
		while (currentTime + ms > System.currentTimeMillis()) {
		}
	}
}
