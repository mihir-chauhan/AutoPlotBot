package com.example.odometryapp_v10.RobotSimulation.Skystone;//package internal.Skystone;
//
//import java.util.ArrayList;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import org.firstinspires.ftc.robotcore.external.Telemetry;
////import org.firstinspires.ftc.teamcode.Autonomous.AutoTemplateUDP;
//import org.firstinspires.ftc.teamcode.Robot.Structure.Pose;
//import org.firstinspires.ftc.teamcode.Robot.Structure.Robot;
//import org.firstinspires.ftc.teamcode.Robot.Structure.SimpleAccessory;
//
//public class SkystoneRobot extends Robot {
//
//    public static final double robotWidth = 16.75;
//    public static final double robotLength = 17.75;
//
//    static OpMode opMode;
//    Telemetry telemetry;
//
//    public static boolean opModeIsActive() {
//        return ((LinearOpMode) opMode).opModeIsActive();
//    }
//
//    public enum AccessoryNames {
//        StoneMechanism, SkystoneDetector
//    }
//
//    public SkystoneRobot(OpMode opMode, boolean autonomous, Pose startingPose) {
//        this.opMode = opMode;
//        this.telemetry = opMode.telemetry;
//
//        this.Drivetrain = new MecanumDrivetrain(opMode, startingPose);
//        this.Accessories = new ArrayList<SimpleAccessory>();
//        this.Accessories.add(new StoneMechanism(opMode));
//        if (autonomous) {
//            this.Accessories.add(new SkystoneDetector("colorSensorLeft", "colorSensorRight"));
//        }
//    }
//
//    public void pause(double milliSec){
//        double currentTime = System.currentTimeMillis();
//        while(System.currentTimeMillis() < currentTime + milliSec && ((LinearOpMode)this.opMode).opModeIsActive()) {
//            // waiting
//        }
//    }
//
//    public void findAndCollect(double milliseconds) {
//        int blockPosition = ((SkystoneDetector) Accessories.get(AccessoryNames.SkystoneDetector.ordinal())).findBlock(milliseconds);
////        AutoTemplateUDP.autoClient.sendData("blockPosition: " + blockPosition);
//        double currentY = ((MecanumDrivetrain) Drivetrain).odometry.getCurrentPose().y;
//
//        /**
//         * TODO: double check distance to move for each block (ramptopos)
//         */
//        if(currentY < 72) {
//            //red alliance
//            if (blockPosition == 1) {
//                ((MecanumDrivetrain) Drivetrain).rampTankToPosition(new Pose(36.875, 38.375, 180), 0.3, MecanumDrivetrain.TankDirection.forward);
//                ((StoneMechanism) Accessories.get(AccessoryNames.StoneMechanism.ordinal())).blockServo(StoneMechanism.BlockModes.out);
//                pause(1000);
//            } else if (blockPosition == 2) {
////            ((MecanumDrivetrain) Drivetrain).rampTankToPosition(new Pose(38.875, 38.375, 180), 0.3, MecanumDrivetrain.TankDirection.backward);
//                ((StoneMechanism) Accessories.get(AccessoryNames.StoneMechanism.ordinal())).blockServo(StoneMechanism.BlockModes.out);
//                pause(1000);
//            } else if (blockPosition == 3) {
//                ((MecanumDrivetrain) Drivetrain).rampTankToPosition(new Pose(46.375, 38.375, 180), 0.3, MecanumDrivetrain.TankDirection.backward);
//                ((StoneMechanism) Accessories.get(AccessoryNames.StoneMechanism.ordinal())).blockServo(StoneMechanism.BlockModes.out);
//                pause(1000);
//            }
//            ((MecanumDrivetrain) Drivetrain).strafeToPosition(new Pose(((MecanumDrivetrain) Drivetrain).odometry.getCurrentPose().x, 24, 180), 0.55);
//        } else {
//            //blue alliance
//            if (blockPosition == 1) {
//                ((MecanumDrivetrain) Drivetrain).rampTankToPosition(new Pose(46.375, 38.375, 180), 0.3, MecanumDrivetrain.TankDirection.forward);
//                ((StoneMechanism) Accessories.get(AccessoryNames.StoneMechanism.ordinal())).blockServo(StoneMechanism.BlockModes.out);
//                pause(1000);
//            } else if (blockPosition == 2) {
////            ((MecanumDrivetrain) Drivetrain).rampTankToPosition(new Pose(38.875, 38.375, 180), 0.3, MecanumDrivetrain.TankDirection.backward);
//                ((StoneMechanism) Accessories.get(AccessoryNames.StoneMechanism.ordinal())).blockServo(StoneMechanism.BlockModes.out);
//                pause(1000);
//            } else if (blockPosition == 3) {
//                ((MecanumDrivetrain) Drivetrain).rampTankToPosition(new Pose(36.875, 38.375, 180), 0.3, MecanumDrivetrain.TankDirection.backward);
//                ((StoneMechanism) Accessories.get(AccessoryNames.StoneMechanism.ordinal())).blockServo(StoneMechanism.BlockModes.out);
//                pause(1000);
//            }
//            ((MecanumDrivetrain) Drivetrain).strafeToPosition(new Pose(((MecanumDrivetrain) Drivetrain).odometry.getCurrentPose().x, 120, 180), 0.55);
//        }
//    }
//
//
//}
