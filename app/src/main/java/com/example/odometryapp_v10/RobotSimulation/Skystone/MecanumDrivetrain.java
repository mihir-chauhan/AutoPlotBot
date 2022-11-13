package com.example.odometryapp_v10.RobotSimulation.Skystone;

import com.example.odometryapp_v10.RobotSimulation.BackCalculation;
import com.example.odometryapp_v10.RobotSimulation.MovementPose;
import com.example.odometryapp_v10.RobotSimulation.RobotSim;
import com.example.odometryapp_v10.RobotSimulation.Structure.Odometry;
import com.example.odometryapp_v10.RobotSimulation.Structure.Path;
import com.example.odometryapp_v10.RobotSimulation.Structure.Pose;
import com.example.odometryapp_v10.RobotSimulation.Structure.PurePursuitController;

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

            if (Math.abs(gain) < 0.01) {
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
        System.out.println("ENRERING"  +Math.toDegrees(Math.abs(targetHeading - currAngle)));
        while (Math.abs(targetHeading - currAngle) > 0.0175) {
            System.out.println(Math.toDegrees(Math.abs(targetHeading - currAngle)));
            final double dampeningConst = 0.5;
            if (targetHeading - currAngle > 0) {
                BackCalculation.setFrontLeftPower(-inputPower * (Math.abs(targetHeading - currAngle) + dampeningConst));
                BackCalculation.setFrontRightPower(inputPower * (Math.abs(targetHeading - currAngle) + dampeningConst));
                BackCalculation.setBackLeftPower(-inputPower * (Math.abs(targetHeading - currAngle) + dampeningConst));
                BackCalculation.setBackRightPower(inputPower * (Math.abs(targetHeading - currAngle) + dampeningConst));
            } else if (targetHeading - currAngle < 0) {
                BackCalculation.setFrontLeftPower(inputPower * (Math.abs(targetHeading - currAngle) + dampeningConst));
                BackCalculation.setFrontRightPower(-inputPower * (Math.abs(targetHeading - currAngle) + dampeningConst));
                BackCalculation.setBackLeftPower(inputPower * (Math.abs(targetHeading - currAngle) + dampeningConst));
                BackCalculation.setBackRightPower(-inputPower * (Math.abs(targetHeading - currAngle) + dampeningConst));
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

    public static void startBackgroundPositionUpdates(final ArrayList<MovementPose> targetCoordinates, final double movementPower, final Pose startingPosition) {
        mecanumThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Started Movement.");
                int numberOfPurePursuitPositions = 0;
                ArrayList<Pose> positions = new ArrayList<>();
                for (int i = 0; i < targetCoordinates.size() + 1; i++) {
                    try {
                        if (targetCoordinates.get(i).movementType.equals(MovementPose.MovementType.strafe)) {
                            System.out.println("S");
                            if (numberOfPurePursuitPositions >= 1) {
                                positions.add(0, odometry.getCurrentPose());
                                positions.add(positions.size() - 1, positions.get(positions.size() - 1));
                                executePath(new Path(positions, 0.1));
                            }
                            numberOfPurePursuitPositions = 0;
                            positions.clear();
                            strafeToPosition(targetCoordinates.get(i).position, movementPower);
                        } else if (targetCoordinates.get(i).movementType.equals(MovementPose.MovementType.moveForward)) {
                            System.out.println("MF");
                            if (numberOfPurePursuitPositions >= 1) {
                                positions.add(0, odometry.getCurrentPose());
                                positions.add(positions.size() - 1, positions.get(positions.size() - 1));
                                executePath(new Path(positions, 0.1));
                            }
                            numberOfPurePursuitPositions = 0;
                            positions.clear();

                            rampTankToPosition(targetCoordinates.get(i).position, movementPower, TankDirection.forward);
                        } else if (targetCoordinates.get(i).movementType.equals(MovementPose.MovementType.moveBackward)) {
                            System.out.println("MB");
                            if (numberOfPurePursuitPositions >= 1) {
                                positions.add(0, odometry.getCurrentPose());
                                positions.add(positions.size() - 1, positions.get(positions.size() - 1));
                                executePath(new Path(positions, 0.1));
                            }
                            numberOfPurePursuitPositions = 0;
                            positions.clear();

                            rampTankToPosition(targetCoordinates.get(i).position, movementPower, TankDirection.backward);
                        } else if (targetCoordinates.get(i).movementType.equals(MovementPose.MovementType.PurePursuitTankForward) || targetCoordinates.get(i).movementType.equals(MovementPose.MovementType.PurePursuitTankBackward) || targetCoordinates.get(i).movementType.equals(MovementPose.MovementType.PurePursuitStrafe)) {
                            //TODO: Add Tank Forward, Tank Backward, and Strafe Versions of Pure Pursuit
                            System.out.println("PP");
                            numberOfPurePursuitPositions++;
                            positions.add(targetCoordinates.get(i).position);
                        } else if (targetCoordinates.get(i).movementType.equals(MovementPose.MovementType.turn)) {
                            System.out.println("T");
                            if (numberOfPurePursuitPositions >= 1) {
                                positions.add(0, odometry.getCurrentPose());
                                positions.add(positions.size() - 1, positions.get(positions.size() - 1));
                                executePath(new Path(positions, 0.1));
                            }
                            numberOfPurePursuitPositions = 0;
                            positions.clear();
                            turnToHeading(targetCoordinates.get(i).position.heading, movementPower);
                        } else {
                            System.out.println("?");
                        }
                    } catch (Exception ignore) {
                        if (numberOfPurePursuitPositions >= 1) {
                            if (numberOfPurePursuitPositions >= 1) {
                                positions.add(0, odometry.getCurrentPose());
                                positions.add(positions.size() - 1, positions.get(positions.size() - 1));
                                executePath(new Path(positions, 0.1));
                            }
                        }
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
        double gain = Math.atan2(Math.sin(error), Math.cos(error));
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
