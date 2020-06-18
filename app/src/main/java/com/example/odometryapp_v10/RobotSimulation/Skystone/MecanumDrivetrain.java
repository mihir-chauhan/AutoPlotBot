package com.example.odometryapp_v10.RobotSimulation.Skystone;

import com.example.odometryapp_v10.RobotSimulation.BackCalculation;
import com.example.odometryapp_v10.RobotSimulation.MovementPose;
import com.example.odometryapp_v10.RobotSimulation.RobotSim;
import com.example.odometryapp_v10.RobotSimulation.Structure.Odometry;
import com.example.odometryapp_v10.RobotSimulation.Structure.Pose;

import java.util.ArrayList;

public class MecanumDrivetrain {
    private static double previousDistance;
    public static final double odometryGainMultiplier = 1.5; // 3.5
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
        if (currentDistance - previousDistance > 0) {// currentDistance > previousDistance) {
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
        while ((currentPose.distanceToPose(targetCoordinate) > 0.5)
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

            subtractNumberForHeading = (targetHeading - currAngle) / 10;
            if (Math.abs(subtractNumberForHeading) < 0.001) {
                subtractNumberForHeading = 0;
            }
            BackCalculation.setFrontLeftPower(v1 - subtractNumberForHeading);
            BackCalculation.setFrontRightPower(v2 + subtractNumberForHeading);
            BackCalculation.setBackLeftPower(v3 - subtractNumberForHeading);
            BackCalculation.setBackRightPower(v4 + subtractNumberForHeading);
        }
        BackCalculation.setFrontLeftPower(0);
        BackCalculation.setFrontRightPower(0);
        BackCalculation.setBackLeftPower(0);
        BackCalculation.setBackRightPower(0);
    }

    public static void rampTankToPosition(Pose targetPose, double movementPower, TankDirection direction) {
        double angleToTravel = odometry.angleToTravel(targetPose, direction);
        System.out.println("Angle to travel to: " + "(" + targetPose.x + ", " + targetPose.y + ")" + " : " + angleToTravel);
		turnToHeading(angleToTravel, movementPower);
        tankToPosition(targetPose, movementPower, direction);
        BackCalculation.setFrontLeftPower(0);
        BackCalculation.setFrontRightPower(0);
        BackCalculation.setBackLeftPower(0);
        BackCalculation.setBackRightPower(0);
    }

    private static final double startPowerForRamp = 0.4;
    private static final double rampInchConstant = 3;

    public static void tankToPosition(Pose targetPose, double movementPower, TankDirection direction) {
        Pose currentPosition = odometry.getCurrentPose();

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

        BackCalculation.setDrivetrainPower(powerToMove);

        resetOvershoot();

        while (currentPosition.distanceToPose(targetPose) > 0.5 && !overshootDistance(currentPosition.distanceToPose(targetPose))) {
            currentPosition = odometry.getCurrentPose();

            desiredHeading = odometry.angleToTravel(new Pose(ppX, ppY, targetPose.heading), direction);

            error = desiredHeading - currentPosition.heading;
            gain = Math.atan2(Math.sin(error), Math.cos(error)) * odometryGainMultiplier;

            if(Math.abs(gain) < 0.01) {
                gain = 0;
            }
            BackCalculation.setLeftDrivetrainPower(checkPower(powerToMove - gain, 0.6));
            BackCalculation.setRightDrivetrainPower(checkPower(powerToMove + gain, 0.6));
        }
    }

    public static void turnToHeading(double targetHeading, double inputPower) {
        double currAngle = odometry.getCurrentPose().heading % (2 * Math.PI);
        if (targetHeading - currAngle > Math.toRadians(180)) {
            targetHeading -= Math.toRadians(360);
        }
        while (Math.abs(targetHeading - currAngle) > 0.0175) {
            if (targetHeading - currAngle > Math.toRadians(2)) {
                BackCalculation.setFrontLeftPower(-inputPower);
                BackCalculation.setFrontRightPower(inputPower);
                BackCalculation.setBackLeftPower(-inputPower);
                BackCalculation.setBackRightPower(inputPower);
            } else if (targetHeading - currAngle < Math.toRadians(2)) {
                BackCalculation.setFrontLeftPower(inputPower);
                BackCalculation.setFrontRightPower(-inputPower);
                BackCalculation.setBackLeftPower(inputPower);
                BackCalculation.setBackRightPower(-inputPower);
            } else {
                break;
            }
            currAngle = odometry.getCurrentPose().heading % (2 * Math.PI);
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

    public static void startBackgroundPositionUpdates(final ArrayList<MovementPose> targetCoordinates, final double movementPower) {
        mecanumThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Started Movement.");
                for (int i = 0; i < targetCoordinates.size(); i++) {
                    if (targetCoordinates.get(i).movementType.equals(MovementPose.MovementType.strafe)) {
                        strafeToPosition(targetCoordinates.get(i).position, movementPower);
                    } else if (targetCoordinates.get(i).movementType.equals(MovementPose.MovementType.moveForward)) {
                        rampTankToPosition(targetCoordinates.get(i).position, movementPower, TankDirection.forward);
                    } else if (targetCoordinates.get(i).movementType.equals(MovementPose.MovementType.moveBackward)) {
                        rampTankToPosition(targetCoordinates.get(i).position, movementPower, TankDirection.backward);
                    } else {
                        System.out.println("?");
                    }
                }
                System.out.println("Done Moving.");
                odometry.stopBackgroundPositionUpdates();
                mecanumThread.interrupt();
            }
        });
        mecanumThread.setPriority(Thread.NORM_PRIORITY);
        mecanumThread.setName("Odometry Background Position Thread");
        mecanumThread.start();
    }
}
