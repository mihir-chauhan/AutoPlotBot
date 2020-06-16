ackage com.example.odometryapp_v10.RobotSimulation;////package org.firstinspires.ftc.teamcode.Autonomous;
////
////import android.os.Environment;
////import com.google.gson.JsonArray;
////import com.google.gson.JsonObject;
////import com.google.gson.JsonParser;
////import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
////import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
////import com.qualcomm.robotcore.util.ElapsedTime;
////
////import org.firstinspires.ftc.teamcode.Robot.Skystone.MecanumDrivetrain;
////import org.firstinspires.ftc.teamcode.Robot.Skystone.SkystoneDetector;
////import org.firstinspires.ftc.teamcode.Robot.Skystone.SkystoneRobot;
////import org.firstinspires.ftc.teamcode.Robot.Skystone.StoneMechanism;
////import org.firstinspires.ftc.teamcode.Robot.Structure.AutoClient;
////import org.firstinspires.ftc.teamcode.Robot.Structure.Coordinate;
////import org.firstinspires.ftc.teamcode.Robot.Structure.Pose;
////import org.firstinspires.ftc.teamcode.Robot.Structure.Hardware;
////import org.firstinspires.ftc.teamcode.Robot.Structure.PursuitCoordinate;
////import org.firstinspires.ftc.teamcode.Robot.Structure.SimpleAccessory;
////
////import java.io.File;
////import java.io.FileOutputStream;
////import java.io.FileReader;
////import java.util.ArrayList;
////
////@Autonomous(name = "AutoTemplate", group = "Autonomous")
////public class AutoTemplateUDP extends LinearOpMode {
////
////    private ElapsedTime runtime = new ElapsedTime();
////
////    SkystoneRobot robot;
////    MecanumDrivetrain drivetrain;
////    StoneMechanism stoneMechanism;
////    SkystoneDetector skystoneDetector;
////
////    Pose startingPosition = new Pose(0, 0, (Math.PI / 2)); // new Pose(32.875, 8.375, Math.toRadians(180));
////    ArrayList<PursuitCoordinate> allMovementCoordinates = new ArrayList<>();
////    ArrayList<Pose> purePursuitPoses = new ArrayList<>();
////
////    String program;
////    public static AutoClient autoClient;
////
////    Pose targetPose = new Pose(0, 0, (Math.PI / 2));
////
////    private static ArrayList<ArrayList<Object>> funcParams = new ArrayList<>();
////
////    public void printOdometryPosition() {
//////        telemetry.addData("X", drivetrain.odometry.getCurrentPose().x);
//////        telemetry.addData("Y", drivetrain.odometry.getCurrentPose().y);
//////        telemetry.addData("Heading", drivetrain.odometry.getCurrentPose().heading);
//////        telemetry.update();
////    }
////
////    public void waitForNextInstruction() {
////        while(opModeIsActive() && !gamepad1.a) {
////            printOdometryPosition();
////        }
////    }
////
////    public void runOpMode() {
////        // Setup telemetry settings
////        telemetry.setAutoClear(true);
////        telemetry.addData("Status", "Initialized");
////        telemetry.update();
////
////        funcParams.clear();
////
////        autoClient = new AutoClient("192.168.49.171", 11039);
////        autoClient.connectToServer();
////        autoClient.sendData("AUTO CLIENT INITIALIZED AND CONNECTED");
////
//////        try {
//////            program = autoClient.requestFile("auto.txt");
//////            autoClient.sendData("AUTO CLIENT RECEIVED FILE -- READY TO PARSE AND RUN");
//////        } catch (Exception e) {
//////            e.printStackTrace();
//////        }
//////        jsonParseString(program);
////
////        // Initialize Robot
////
////        initRobot(true, startingPosition);
////
////        purePursuitPoses.add(new Pose(0, 0, (Math.PI / 2)));
////        purePursuitPoses.add(new Pose(12, 12, (Math.PI / 2)));
////        purePursuitPoses.add(new Pose(0, 24, (Math.PI / 2)));
////        purePursuitPoses.add(new Pose(12, 36, (Math.PI / 2)));
////
//////        purePursuitPoses.add(new Pose(0, 0, (Math.PI / 2)));
//////        purePursuitPoses.add(new Pose(0, 36, (Math.PI / 2)));
//////        purePursuitPoses.add(new Coordinate(0, 24));
//////        purePursuitPoses.add(new Coordinate(12, 36));
////
////        // Autonomous Routine
////        telemetry.clearAll();
////        waitForStart();
////        runtime.reset();
////
////        double movementPower = 0.3;
////
////        startPurePursuitCalculation();
////        while (opModeIsActive()) {
////           drivetrain.strafeToPosition(targetPose, 0.4);
////        }
////
////        drivetrain.stop();
////
//////        purePursuit(0.3);
////
//////        runAllFuncs();
////
////        Hardware.getInstance().stopBackgroundUpdates();
////        requestOpModeStop();
////
//////        waitForNextInstruction();
//////        drivetrain.rampTankToPosition(new Pose(drivetrain.odometry.getCurrentPose().x, 12, 90), 0.45, MecanumDrivetrain.TankDirection.forward);
//////        waitForNextInstruction();
//////        drivetrain.strafeToPosition(new Pose(12, drivetrain.odometry.getCurrentPose().y, 90), 0.6);
//////        waitForNextInstruction();
//////        drivetrain.rampTankToPosition(new Pose(drivetrain.odometry.getCurrentPose().x, 24, 90), 0.45, MecanumDrivetrain.TankDirection.forward);
//////        waitForNextInstruction();
//////        drivetrain.strafeToPosition(new Pose(0, drivetrain.odometry.getCurrentPose().y, 90), 0.6);
//////        waitForNextInstruction();
//////        drivetrain.rampTankToPosition(new Pose(drivetrain.odometry.getCurrentPose().x, 36, 90), 0.45, MecanumDrivetrain.TankDirection.forward);
//////        waitForNextInstruction();
//////
//////        drivetrain.rampTankToPosition(new Pose(drivetrain.odometry.getCurrentPose().x, 24, 90), 0.45, MecanumDrivetrain.TankDirection.backward);
//////        waitForNextInstruction();
//////        drivetrain.strafeToPosition(new Pose(12, drivetrain.odometry.getCurrentPose().y, 90), 0.6);
//////        waitForNextInstruction();
//////        drivetrain.rampTankToPosition(new Pose(drivetrain.odometry.getCurrentPose().x, 12, 90), 0.45, MecanumDrivetrain.TankDirection.backward);
//////        waitForNextInstruction();
//////        drivetrain.strafeToPosition(new Pose(0, drivetrain.odometry.getCurrentPose().y, 90), 0.6);
//////        waitForNextInstruction();
//////        drivetrain.rampTankToPosition(new Pose(drivetrain.odometry.getCurrentPose().x, 0, 90), 0.45, MecanumDrivetrain.TankDirection.backward);
////
////
//////        drivetrain.strafeToPosition(new Pose(32.875, 38.375, 180), 0.6);
//////        robot.pause(500);
//////        drivetrain.rampTankToPosition(new Pose(24.875, 38.375, 180), 0.45, MecanumDrivetrain.TankDirection.forward);
////
//////        int blockPosition = robot.findAndCollect(1500);
//////        waitForNextInstruction();
//////        if (blockPosition == 1) {
//////            stoneMechanism.blockServo(StoneMechanism.BlockModes.out);
//////        } else if (blockPosition == 2) {
//////            drivetrain.rampTankToPosition(new Pose(40.875, 38.375, 180), 0.45, MecanumDrivetrain.TankDirection.backward);
//////            stoneMechanism.blockServo(StoneMechanism.BlockModes.out);
//////        } else if (blockPosition == 3) {
//////            drivetrain.rampTankToPosition(new Pose(48.875, 38.375, 180), 0.45, MecanumDrivetrain.TankDirection.backward);
//////            stoneMechanism.blockServo(StoneMechanism.BlockModes.out);
//////        }
////
//////        int numberOfElements = drivetrain.mecanumDriveOutputData.size();
//////        int currentElementIndex = 0;
//////        String fileContents = "";
//////        telemetry.setAutoClear(true);
//////
//////        while (opModeIsActive()) {
//////            if (currentElementIndex < numberOfElements) {
//////                MecanumData entry = drivetrain.mecanumDriveOutputData.getPoint(currentElementIndex);
//////
//////                telemetry.addData("#", numberOfElements);
//////                telemetry.addData("curr", currentElementIndex);
//////                telemetry.addData("data", entry);
//////                telemetry.update();
//////                fileContents = fileContents.concat(entry.currentAngle + "," + entry.desiredAngle + "," + entry.distance + "," + entry.gain + "," + entry.x + "," + entry.y + "," + entry.heading + "\n");
//////                currentElementIndex++;
//////            } else {
//////                saveToStorage("MecanumDataREDLEFTAUTO.csv", fileContents);
//////                requestOpModeStop();
//////            }
//////        }
//////        while(opModeIsActive()){
//////            // waiting
//////        }
//////        requestOpModeStop();
////
//////        while(gamepad1.a != true && opModeIsActive()){
//////            drivetrain.odometry.getCurrentPose();
//////        }
////
//////        int numberOfElements;
//////        int currentElementIndex = 0;
//////        String fileContents = "";
//////
//////        while (opModeIsActive()) {
//////            numberOfElements = drivetrain.odometry.outputData.size();
//////
//////            if (currentElementIndex < numberOfElements) {
//////                OdometryData entry = drivetrain.odometry.outputData.getPoint(currentElementIndex);
//////
//////                telemetry.addData("#", numberOfElements);
//////                telemetry.addData("curr", currentElementIndex);
//////                telemetry.addData("data", entry);
//////                telemetry.update();
//////
//////                fileContents = fileContents.concat(entry.startingPose.x + "," + entry.startingPose.y + "," + entry.startingPose.heading + "," + entry.left_raw + "," + entry.right_raw + "," + entry.center_raw + "," + entry.delta_left + "," + entry.delta_right + "," + entry.delta_center + "," + entry.arc_length_center + "," + entry.phi + "," + entry.delta_x + "," + entry.delta_y + "," + entry.arc_length_strafe + "," + entry.strafe + "," + entry.strafe_x + "," + entry.strafe_y + "," + entry.endingPose.x + "," + entry.endingPose.y + "," + entry.endingPose.heading + "\n");
//////                currentElementIndex++;
//////            } else {
//////                saveToStorage("OdometryData.csv", fileContents);
//////                pause(5000);
//////
//////                requestOpModeStop();
//////            }
//////        }
////    }
////
////    private Thread purePursuitThread = new Thread();
////
////    public static final double lookaheadRadiusInches = 8;
////
////    public void startPurePursuitCalculation() {
////        purePursuitThread = new Thread(new Runnable() {
////            @Override
////            public void run() {
////                int currentPoseIndex = 1;
////                while (opModeIsActive()) {
////
////                    // assign values for previousTarget, current, and target coordinates
////                    Pose previousTargetPose = purePursuitPoses.getPoint(currentPoseIndex - 1);
////                    Pose currentPose = drivetrain.odometry.getCurrentPose();
////                    Pose nextTargetPose = purePursuitPoses.getPoint(currentPoseIndex);
////
////                    // if we are closer than lookahead distance, then check next coordinate
////                    if (distanceToPos(currentPose, nextTargetPose) < lookaheadRadiusInches) {
////                        if (currentPoseIndex < purePursuitPoses.size() - 1) {
////                            currentPoseIndex++;
////                            previousTargetPose = purePursuitPoses.getPoint(currentPoseIndex - 1);
////                            currentPose = drivetrain.odometry.getCurrentPose();
////                            nextTargetPose = purePursuitPoses.getPoint(currentPoseIndex);
////                        } else {
////                            drivetrain.stop();
////                            break;
////                        }
////                    }
////
////                    // switch reference frame to current coordinate being the origin (relative to current position)
////                    previousTargetPose = new Pose(previousTargetPose.x - currentPose.x, previousTargetPose.y - currentPose.y, previousTargetPose.heading);
////                    nextTargetPose = new Pose(nextTargetPose.x - currentPose.x, nextTargetPose.y - currentPose.y, nextTargetPose.heading);
////
////                    Pose pose1 = currentPose;
////                    Pose pose2 = currentPose;
////
////                    // lines and circles
////                    if ((nextTargetPose.x - previousTargetPose.x) == 0) {
////                        double x = nextTargetPose.x;
////                        double forwardY = lookaheadRadiusInches;
////                        double backwardY = -lookaheadRadiusInches;
////
////                        pose1 = new Pose(x + currentPose.x, forwardY + currentPose.y, nextTargetPose.heading);
////                        pose1 = new Pose(x + currentPose.x, backwardY - currentPose.y, nextTargetPose.heading);
////
////                    } else {
////                        double m = (nextTargetPose.y - previousTargetPose.y) / (nextTargetPose.x - previousTargetPose.x);
////                        double b = nextTargetPose.y - (m * nextTargetPose.x);
////                        double leftX = (Math.sqrt((-1 * Math.pow(b, 2)) + (Math.pow(m, 2) * Math.pow(lookaheadRadiusInches, 2)) + Math.pow(lookaheadRadiusInches, 2)) - (b * m)) / (Math.pow(m, 2) + 1);
////                        double rightX = -1 * ((Math.sqrt((-1 * Math.pow(b, 2)) + (Math.pow(m, 2) * Math.pow(lookaheadRadiusInches, 2)) + Math.pow(lookaheadRadiusInches, 2)) - (b * m)) / (Math.pow(m, 2) + 1));
////                        double leftY = (m * leftX) + b;
////                        double rightY = (m * rightX) + b;
////
////                        // coordinates and motion
////                        pose1 = new Pose(leftX + currentPose.x, leftY + currentPose.y, nextTargetPose.heading);
////                        pose2 = new Pose(rightX + currentPose.x, rightY + currentPose.y, nextTargetPose.heading);
////                        nextTargetPose = new Pose(nextTargetPose.x + currentPose.x, nextTargetPose.y + currentPose.y, nextTargetPose.heading);
////                        targetPose = findCloserCoordinate(pose1, pose2, nextTargetPose);
////                    }
////
////                    autoClient.sendData(currentPose.x + ", " + currentPose.y + ", " + currentPose.heading + ", " + pose1.x + ", " + pose1.y + ", "  + pose1.heading + ", " + pose2.x + ", " + pose2.y + ", " + pose2.heading + ", " + targetPose.x + ", " + targetPose.y + ", " + targetPose.heading);
////
////                    long time = System.currentTimeMillis();
////                    while (opModeIsActive() && System.currentTimeMillis() < time + 250) {
////                        // waiting
////                    }
////                }
////            }
////        });
////        purePursuitThread.setPriority(7);
////        purePursuitThread.setName("Pure Pursuit Background Thread");
////        purePursuitThread.start();
////    }
////
//////    public void purePursuit(double movementPower) {
//////
//////            // movement
//////
//////    }
////
////    public Double angleToTravel(Coordinate currentCoordinate, Coordinate endCoordinate) {
////        double opposite = endCoordinate.y - currentCoordinate.y;
////        double adjacent = endCoordinate.x - currentCoordinate.x;
////        return (Math.atan2(opposite, adjacent) + Math.PI * 2) % (Math.PI * 2);
////    }
////
////    public double distanceToPos(Pose currentPose, Pose endPose) {
////        return Math.hypot((endPose.x - currentPose.x), (endPose.y - currentPose.y));
////    }
////
////    public Pose findCloserCoordinate(Pose pose1, Pose pose2, Pose targetPose) {
////        if (distanceToPos(pose1, targetPose) < distanceToPos(pose2, targetPose)) {
////            return pose1;
////        } else {
////            return pose2;
////        }
////    }
////
////    public void pause(double milliseconds) {
////        long startTime = System.currentTimeMillis();
////        while ((System.currentTimeMillis() < startTime + milliseconds) && (opModeIsActive())) {
////            // waiting
////        }
////    }
////
////    public void initRobot(boolean telemetryEnabled, Pose startingPose) {
////        // Create the robot object
////        robot = new SkystoneRobot(this, true, startingPose);
////        drivetrain = (MecanumDrivetrain) robot.Drivetrain;
////        drivetrain.odometry.reset();
////
////        Hardware.getInstance().setHardwareMap(hardwareMap);
////        Hardware.getInstance().setRobot(robot);
////        Hardware.getInstance().startBackgroundUpdates();
////
////        stoneMechanism = (StoneMechanism) robot.Accessories.getPoint(SkystoneRobot.AccessoryNames.StoneMechanism.ordinal());
////        skystoneDetector = (SkystoneDetector) robot.Accessories.getPoint(SkystoneRobot.AccessoryNames.SkystoneDetector.ordinal());
////
////        if (telemetryEnabled) {
////            robot.Drivetrain.isTelemetryEnabled = true;
////            for (SimpleAccessory accessory : robot.Accessories) {
////                accessory.isTelemetryEnabled = true;
////            }
////        }
////    }
////
////
////    public void jsonParseFile(String fileName) {
////        try {
////            File programsDirectory = new File(fileName);
////            FileReader json = new FileReader(programsDirectory);
////            JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
////            JsonArray arr = jsonObject.getAsJsonArray("program");
////
////            for (int i = 0; i < arr.size(); i++) {
////                ArrayList<Object> temporaryStorage = new ArrayList<>();
////                temporaryStorage.clear();
////                String name = arr.getPoint(i).getAsJsonObject().getPoint("name").getAsString();
////                switch (name) {
////                    case "moveToPosition":
////                        temporaryStorage.add("moveToPosition");
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("x").getAsDouble());
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("y").getAsDouble());
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("movementPower").getAsDouble());
////                        allMovementCoordinates.add(new PursuitCoordinate((double) temporaryStorage.getPoint(1), (double) temporaryStorage.getPoint(2), false));
////                        funcParams.add(temporaryStorage);
////                        break;
////                    case "changeHeading":
////                        temporaryStorage.add("changeHeading");
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("degree").getAsDouble());
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("power").getAsDouble());
////                        funcParams.add(temporaryStorage);
////                        break;
////                    case "pause":
////                        temporaryStorage.add("pause");
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("pauseTime").getAsDouble());
////                        funcParams.add(temporaryStorage);
////                        telemetry.update();
////                        break;
////                    case "origin":
////                        double x = (arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("x").getAsDouble());
////                        double y = (arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("y").getAsDouble());
////                        double theta = (arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("theta").getAsDouble());
////                        startingPosition = new Pose(x, y, Math.toRadians(theta));
////                        break;
////                    case "setPosition":
////                        temporaryStorage.add("setPosition");
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("positionName").getAsString());
////                        funcParams.add(temporaryStorage);
////                        break;
////                    case "goToPosition":
////                        temporaryStorage.add("goToPosition");
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("positionName").getAsString());
////                        funcParams.add(temporaryStorage);
////                        break;
////                    case "vuforiaDetection":
////                        temporaryStorage.add("vuforiaDetection");
////                        funcParams.add(temporaryStorage);
////                        break;
////                    case "stopAtColor":
////                        temporaryStorage.add("stopAtColor");
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("color").getAsString());
////                        funcParams.add(temporaryStorage);
////                        break;
////                    case "collection":
////                        temporaryStorage.add("collection");
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("start").getAsString());
////                        funcParams.add(temporaryStorage);
////                        break;
////                    case "strafeToPosition":
////                        temporaryStorage.add("strafeToPosition");
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("x").getAsDouble());
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("y").getAsDouble());
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("movementPower").getAsDouble());
////                        allMovementCoordinates.add(new PursuitCoordinate((double) temporaryStorage.getPoint(1), (double) temporaryStorage.getPoint(2), true));
////                        funcParams.add(temporaryStorage);
////                        break;
////                    case "findAndCollect":
////                        temporaryStorage.add("findAndCollect");
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("detectTime").getAsDouble());
////                        funcParams.add(temporaryStorage);
////                        break;
////                    case "findAndCollect2":
////                        temporaryStorage.add("findAndCollect2");
////                        funcParams.add(temporaryStorage);
////                        break;
////                    default:
////                        temporaryStorage.clear();
////                        break;
////                }
////            }
////        } catch (Exception e) {
////            telemetry.addData("Error accessing program directory", e);
////            telemetry.update();
////        }
////
////        telemetry.addData("Waiting for start", "please begin auto");
////        telemetry.update();
////    }
////
////    public void jsonParseString(String text) {
////        try {
////            JsonObject jsonObject = new JsonParser().parse(text).getAsJsonObject();
////            JsonArray arr = jsonObject.getAsJsonArray("program");
////
////            for (int i = 0; i < arr.size(); i++) {
////                ArrayList<Object> temporaryStorage = new ArrayList<>();
////                temporaryStorage.clear();
////                String name = arr.getPoint(i).getAsJsonObject().getPoint("name").getAsString();
////                switch (name) {
////                    case "moveToPosition":
////                        temporaryStorage.add("moveToPosition");
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("x").getAsDouble());
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("y").getAsDouble());
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("movementPower").getAsDouble());
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("backwards").getAsBoolean());
////                        allMovementCoordinates.add(new PursuitCoordinate((double) temporaryStorage.getPoint(1), (double) temporaryStorage.getPoint(2), false));
////                        funcParams.add(temporaryStorage);
////                        break;
////                    case "changeHeading":
////                        temporaryStorage.add("changeHeading");
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("degree").getAsDouble());
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("power").getAsDouble());
////                        funcParams.add(temporaryStorage);
////                        break;
////                    case "pause":
////                        temporaryStorage.add("pause");
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("pauseTime").getAsDouble());
////                        funcParams.add(temporaryStorage);
////                        telemetry.update();
////                        break;
////                    case "origin":
////                        double x = (arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("x").getAsDouble());
////                        double y = (arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("y").getAsDouble());
////                        double theta = (arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("theta").getAsDouble());
////                        startingPosition = new Pose(x, y, Math.toRadians(theta));
////                        break;
////                    case "setPosition":
////                        temporaryStorage.add("setPosition");
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("positionName").getAsString());
////                        funcParams.add(temporaryStorage);
////                        break;
////                    case "goToPosition":
////                        temporaryStorage.add("goToPosition");
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("positionName").getAsString());
////                        funcParams.add(temporaryStorage);
////                        break;
////                    case "foundationServo":
////                        temporaryStorage.add("foundationServo");
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("position").getAsString());
////                        funcParams.add(temporaryStorage);
////                        break;
////                    case "stopAtColor":
////                        temporaryStorage.add("stopAtColor");
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("color").getAsString());
////                        funcParams.add(temporaryStorage);
////                        break;
////                    case "collection":
////                        temporaryStorage.add("collection");
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("start").getAsString());
////                        funcParams.add(temporaryStorage);
////                        break;
////                    case "strafeToPosition":
////                        temporaryStorage.add("strafeToPosition");
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("x").getAsDouble());
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("y").getAsDouble());
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("movementPower").getAsDouble());
////                        allMovementCoordinates.add(new PursuitCoordinate((double) temporaryStorage.getPoint(1), (double) temporaryStorage.getPoint(2), true));
////                        funcParams.add(temporaryStorage);
////                        break;
////                    case "findAndCollect":
////                        temporaryStorage.add("findAndCollect");
////                        temporaryStorage.add(arr.getPoint(i).getAsJsonObject().getPoint("params").getAsJsonObject().getPoint("detectTime").getAsDouble());
////                        funcParams.add(temporaryStorage);
////                        break;
////                    case "findAndCollect2":
////                        temporaryStorage.add("findAndCollect2");
////                        funcParams.add(temporaryStorage);
////                        break;
////                    case "dropBlock":
////                        temporaryStorage.add("dropBlock");
////                        funcParams.add(temporaryStorage);
////                        break;
////                    default:
////                        temporaryStorage.clear();
////                        break;
////                }
////            }
////        } catch (Exception e) {
////            telemetry.addData("Error accessing program directory", e);
////            telemetry.update();
////        }
////
////        telemetry.addData("Waiting for start", "please begin auto");
////        telemetry.update();
////    }
////
////    public ArrayList<String> getProgramNames() {
////        ArrayList<String> programNamesArray = new ArrayList<>();
////        try {
////
////            File programsDirectory = new File("/sdcard/Innov8rz/programs");
////            File[] programNames = programsDirectory.listFiles();
////
////            for (int x = 0; x < programNames.length; x++) {
////                programNamesArray.add(programNames[x].toString());
////            }
//////            for (int b = 0; b < programNames.length; b++) {
//////
//////                FileReader json = new FileReader(programNames[b]);
//////                JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
//////                JsonArray arr = jsonObject.getAsJsonArray("program");
//////                ArrayList<String> namesOfFuncs = new ArrayList<>();
//////
//////
//////                for (int i = 0; i < arr.size(); i++) {
//////                    String name = arr.getPoint(i).getAsJsonObject().getPoint("name").getAsString();
//////                    namesOfFuncs.add(name);
//////                }
//////                int i;
//////                int error = 0;
//////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////
////        return programNamesArray;
////    }
////
////    public String selectProgram() {
////        ArrayList<String> programNames = new ArrayList<>();
////        for (int i = 0; i < getProgramNames().size(); i++) {
////            telemetry.addData("ProgramFile Name", getProgramNames().getPoint(i));
////            telemetry.update();
////            programNames.add(getProgramNames().getPoint(i));
////        }
////        int currentProgramIndex = 0;
////        while (!gamepad1.a) { //&& opModeIsActive()
////            if (gamepad1.dpad_up == true) {
////                while (gamepad1.dpad_up == true) {
////                }//wait to release
////                if (currentProgramIndex < programNames.size() - 1) {
////                    currentProgramIndex++;
////                    telemetry.clear();
////                    for (int i = 0; i < programNames.size(); i++) {
////                        telemetry.addData("ProgramFile Name", programNames.getPoint(i));
////                        telemetry.update();
////                    }
////                    telemetry.addData("Current Program Selected", programNames.getPoint(currentProgramIndex));
////                }
////            } else if (gamepad1.dpad_down == true) {
////                while (gamepad1.dpad_down == true) {
////                }//wait to release
////                if (currentProgramIndex > 0) {
////                    currentProgramIndex--;
////                    telemetry.clear();
////                    for (int i = 0; i < programNames.size(); i++) {
////                        telemetry.addData("ProgramFile Name", programNames.getPoint(i));
////                        telemetry.update();
////                    }
////                    telemetry.addData("Current Program Selected", programNames.getPoint(currentProgramIndex));
////                }
////            }
////            telemetry.update();
////        }
////        telemetry.addData("Finished with selecting program", "going to parsing");
////        telemetry.update();
////        return programNames.getPoint(currentProgramIndex);
////    }
////
////    public void runAllFuncs() {
////        for (int i = 0; i < funcParams.size(); i++) {
////            drivetrain.odometry.getCurrentPose();
////            switch (funcParams.getPoint(i).getPoint(0).toString()) {
////                case "moveToPosition":
////                    if ((boolean) funcParams.getPoint(i).getPoint(4)) {
////                        drivetrain.rampTankToPosition(new Pose((double) funcParams.getPoint(i).getPoint(1), (double) funcParams.getPoint(i).getPoint(2), Math.toRadians(0)), (double) funcParams.getPoint(i).getPoint(3), MecanumDrivetrain.TankDirection.backward);
////                    } else {
////                        drivetrain.rampTankToPosition(new Pose((double) funcParams.getPoint(i).getPoint(1), (double) funcParams.getPoint(i).getPoint(2), Math.toRadians(0)), (double) funcParams.getPoint(i).getPoint(3), MecanumDrivetrain.TankDirection.forward);
////                    }
////                    break;
////                case "changeHeading":
////                    drivetrain.turnToHeading(Math.toRadians((double) funcParams.getPoint(i).getPoint(1)), (double) funcParams.getPoint(i).getPoint(2));
////                    break;
////                case "pause":
////                    pause((double) funcParams.getPoint(i).getPoint(1));
////                    break;
////                case "setPosition":
////                    // code
////                    break;
////                case "goToPosition":
//////                    drivetrain.odometry.getCoordinate(funcParams.getPoint(i).getPoint(1).toString());
////                    break;
////                case "strafeToPosition":
////                    drivetrain.strafeToPosition(new Pose((double) funcParams.getPoint(i).getPoint(1), (double) funcParams.getPoint(i).getPoint(2), Math.toRadians(0)), (double) funcParams.getPoint(i).getPoint(3));
////                    break;
////                case "findAndCollect":
////                    robot.findAndCollect((double) funcParams.getPoint(i).getPoint(1));
////                    break;
////                case "findAndCollect2":
//////                    collectSecondBlock(new Pose(0, 0, 0));
////                    break;
////                case "foundationServo":
////                    if (funcParams.getPoint(i).getPoint(1).toString().contains("Open")) {
////                        stoneMechanism.foundationServo(StoneMechanism.FoundationModes.open);
////                    } else if (funcParams.getPoint(i).getPoint(1).toString().contains("Close")) {
////                        stoneMechanism.foundationServo(StoneMechanism.FoundationModes.grab);
////                    }
////                    break;
////                case "dropBlock":
////                    stoneMechanism.blockServo(StoneMechanism.BlockModes.in);
////                    break;
////                default:
////                    telemetry.addData("runAllFuncs", "Function doesn't exist");
////                    break;
////            }
////        }
////    }
////
////    public void saveToStorage(String fileName, String fileContents) {
////        File file = new File(Environment.getExternalStorageDirectory(), fileName);
////        try {
////            FileOutputStream fos = new FileOutputStream(file);
////            fos.write(fileContents.getBytes());
////            fos.flush();
////            fos.close();
////        } catch (Exception e) {
////
////        }
////    }
////}
////
//
//package org.firstinspires.ftc.teamcode.Autonomous;
//
//import android.os.Environment;
//
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//import org.firstinspires.ftc.teamcode.Robot.Skystone.MecanumDrivetrain;
//import org.firstinspires.ftc.teamcode.Robot.Skystone.SkystoneDetector;
//import org.firstinspires.ftc.teamcode.Robot.Skystone.SkystoneRobot;
//import org.firstinspires.ftc.teamcode.Robot.Skystone.StoneMechanism;
//import org.firstinspires.ftc.teamcode.Robot.Structure.AutoClient;
//import org.firstinspires.ftc.teamcode.Robot.Structure.Coordinate;
//import org.firstinspires.ftc.teamcode.Robot.Structure.Hardware;
//import org.firstinspires.ftc.teamcode.Robot.Structure.Path;
//import org.firstinspires.ftc.teamcode.Robot.Structure.Pose;
//import org.firstinspires.ftc.teamcode.Robot.Structure.PursuitCoordinate;
//import org.firstinspires.ftc.teamcode.Robot.Structure.SimpleAccessory;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.util.ArrayList;
//
//@Autonomous(name = "PurePursuitTest", group = "Autonomous")
//public class NewAutoTemplate extends LinearOpMode {
//
//    private ElapsedTime runtime = new ElapsedTime();
//
//    SkystoneRobot robot;
//    MecanumDrivetrain drivetrain;
//    StoneMechanism stoneMechanism;
//    SkystoneDetector skystoneDetector;
//
//    Pose startingPosition = new Pose(32.813,8.875, Math.toRadians(180)); // new Pose(32.875, 8.375, Math.toRadians(180));
//    ArrayList<PursuitCoordinate> allMovementCoordinates = new ArrayList<>();
//    ArrayList<Coordinate> purePursuitCoordinates = new ArrayList<>();
//
//    String program;
//    public static AutoClient autoClient;
//
//    Coordinate targetCoordinate = new Coordinate(0, 0);
//
//    private static ArrayList<ArrayList<Object>> funcParams = new ArrayList<>();
//
//    public void printOdometryPosition() {
////        telemetry.addData("X", drivetrain.odometry.getCurrentPose().x);
////        telemetry.addData("Y", drivetrain.odometry.getCurrentPose().y);
////        telemetry.addData("Heading", drivetrain.odometry.getCurrentPose().heading);
////        telemetry.update();
//    }
//
//    public void waitForNextInstruction() {
//        while(opModeIsActive() && !gamepad1.a) {
//            printOdometryPosition();
//        }
//    }
//
//    public void runOpMode() {
//        // Setup telemetry settings
//        telemetry.setAutoClear(true);
//        telemetry.addData("Status", "Initialized");
//        telemetry.update();
//
//        funcParams.clear();
//
//        autoClient = new AutoClient("192.168.49.171", 11039);
//        autoClient.connectToServer();
//        autoClient.sendData("AUTO CLIENT INITIALIZED AND CONNECTED");
//
////        try {
////            program = autoClient.requestFile("auto.txt");
////            autoClient.sendData("AUTO CLIENT RECEIVED FILE -- READY TO PARSE AND RUN");
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        jsonParseString(program);
//
//        // Initialize Robot
//
//        initRobot(true, startingPosition);
//
////        purePursuitCoordinates.add(new Coordinate(0, 0));
////        purePursuitCoordinates.add(new Coordinate(12, 12));
////        purePursuitCoordinates.add(new Coordinate(0, 24));
////        purePursuitCoordinates.add(new Coordinate(12, 36));
//
////        purePursuitCoordinates.add(new Coordinate(0, 0));
////        purePursuitCoordinates.add(new Coordinate(0, 36));
////        purePursuitCoordinates.add(new Coordinate(0, 24));
////        purePursuitCoordinates.add(new Coordinate(12, 36));
//
//        // Autonomous Routine
//        telemetry.clearAll();
//        waitForStart();
//        runtime.reset();
//
////        ArrayList<Pose> poses1 = new ArrayList<Pose>();
////        poses1.add(new Pose(32.813,8.875, Math.toRadians(180)));
////        poses1.add(new Pose(48,36, Math.toRadians(180)));
////        poses1.add(new Pose(48,48, Math.toRadians(180)));
////        poses1.add(new Pose(44,48, Math.toRadians(180)));
////        drivetrain.executePath(new Path(poses1, 0.8));
////
////        ArrayList<Pose> poses2 = new ArrayList<Pose>();
////        poses2.add(new Pose(44,48, Math.toRadians(180)));
////        poses2.add(new Pose(40,33, Math.toRadians(180)));
////        poses2.add(new Pose(72,27, Math.toRadians(180)));
////        poses2.add(new Pose(120,27, Math.toRadians(270)));
////        drivetrain.executePath(new Path(poses2, 0.8));
////
////        ArrayList<Pose> poses3 = new ArrayList<Pose>();
////        poses3.add(new Pose(120,27, Math.toRadians(270)));
////        poses3.add(new Pose(120,40.75, Math.toRadians(270)));
////        poses3.add(new Pose(108,24, Math.toRadians(180)));
////        poses3.add(new Pose(115,24, Math.toRadians(180)));
////        poses3.add(new Pose(113.5,26, Math.toRadians(180)));
////        drivetrain.executePath(new Path(poses3, 0.8));
//
//
//        ArrayList<Pose> poses1 = new ArrayList<Pose>();
//        poses1.add(new Pose(32.813,8.875, Math.toRadians(180)));
//        poses1.add(new Pose(48,36, Math.toRadians(180)));
////        poses1.add(new Pose(48,48, Math.toRadians(180)));
////        poses1.add(new Pose(44,48, Math.toRadians(180)));
//        poses1.add(new Pose(40,33, Math.toRadians(180)));
//        poses1.add(new Pose(72,27, Math.toRadians(180)));
//        poses1.add(new Pose(120,27, Math.toRadians(270)));
////        poses1.add(new Pose(120,40.75, Math.toRadians(270)));
////        poses1.add(new Pose(108,24, Math.toRadians(180)));
////        poses1.add(new Pose(115,24, Math.toRadians(180)));
////        poses1.add(new Pose(113.5,26, Math.toRadians(180)));
//        drivetrain.executePath(new Path(poses1, 0.8));
//
//
//
//
//
////        double movementPower = 0.3;
////
////        startPurePursuitCalculation();
////        while (opModeIsActive()) {
////            double desiredHeading = angleToTravel(drivetrain.odometry.getCurrentCoordinate(), targetCoordinate);
////            double error = desiredHeading - drivetrain.odometry.getCurrentPose().heading;
////            double gain = Math.atan2(Math.sin(error), Math.cos(error)) * 2.5;
////
////            drivetrain.setLeftDrivetrainPower(drivetrain.checkPower(movementPower - gain, 0.4));
////            drivetrain.setRightDrivetrainPower(drivetrain.checkPower(movementPower + gain, 0.4));
////        }
//
////        purePursuit(0.3);
//
////        runAllFuncs();
//
//        Hardware.getInstance().stopBackgroundUpdates();
//        requestOpModeStop();
//
////        waitForNextInstruction();
////        drivetrain.rampTankToPosition(new Pose(drivetrain.odometry.getCurrentPose().x, 12, 90), 0.45, MecanumDrivetrain.TankDirection.forward);
////        waitForNextInstruction();
////        drivetrain.strafeToPosition(new Pose(12, drivetrain.odometry.getCurrentPose().y, 90), 0.6);
////        waitForNextInstruction();
////        drivetrain.rampTankToPosition(new Pose(drivetrain.odometry.getCurrentPose().x, 24, 90), 0.45, MecanumDrivetrain.TankDirection.forward);
////        waitForNextInstruction();
////        drivetrain.strafeToPosition(new Pose(0, drivetrain.odometry.getCurrentPose().y, 90), 0.6);
////        waitForNextInstruction();
////        drivetrain.rampTankToPosition(new Pose(drivetrain.odometry.getCurrentPose().x, 36, 90), 0.45, MecanumDrivetrain.TankDirection.forward);
////        waitForNextInstruction();
////
////        drivetrain.rampTankToPosition(new Pose(drivetrain.odometry.getCurrentPose().x, 24, 90), 0.45, MecanumDrivetrain.TankDirection.backward);
////        waitForNextInstruction();
////        drivetrain.strafeToPosition(new Pose(12, drivetrain.odometry.getCurrentPose().y, 90), 0.6);
////        waitForNextInstruction();
////        drivetrain.rampTankToPosition(new Pose(drivetrain.odometry.getCurrentPose().x, 12, 90), 0.45, MecanumDrivetrain.TankDirection.backward);
////        waitForNextInstruction();
////        drivetrain.strafeToPosition(new Pose(0, drivetrain.odometry.getCurrentPose().y, 90), 0.6);
////        waitForNextInstruction();
////        drivetrain.rampTankToPosition(new Pose(drivetrain.odometry.getCurrentPose().x, 0, 90), 0.45, MecanumDrivetrain.TankDirection.backward);
//
//
////        drivetrain.strafeToPosition(new Pose(32.875, 38.375, 180), 0.6);
////        robot.pause(500);
////        drivetrain.rampTankToPosition(new Pose(24.875, 38.375, 180), 0.45, MecanumDrivetrain.TankDirection.forward);
//
////        int blockPosition = robot.findAndCollect(1500);
////        waitForNextInstruction();
////        if (blockPosition == 1) {
////            stoneMechanism.blockServo(StoneMechanism.BlockModes.out);
////        } else if (blockPosition == 2) {
////            drivetrain.rampTankToPosition(new Pose(40.875, 38.375, 180), 0.45, MecanumDrivetrain.TankDirection.backward);
////            stoneMechanism.blockServo(StoneMechanism.BlockModes.out);
////        } else if (blockPosition == 3) {
////            drivetrain.rampTankToPosition(new Pose(48.875, 38.375, 180), 0.45, MecanumDrivetrain.TankDirection.backward);
////            stoneMechanism.blockServo(StoneMechanism.BlockModes.out);
////        }
//
////        int numberOfElements = drivetrain.mecanumDriveOutputData.size();
////        int currentElementIndex = 0;
////        String fileContents = "";
////        telemetry.setAutoClear(true);
////
////        while (opModeIsActive()) {
////            if (currentElementIndex < numberOfElements) {
////                MecanumData entry = drivetrain.mecanumDriveOutputData.getPoint(currentElementIndex);
////
////                telemetry.addData("#", numberOfElements);
////                telemetry.addData("curr", currentElementIndex);
////                telemetry.addData("data", entry);
////                telemetry.update();
////                fileContents = fileContents.concat(entry.currentAngle + "," + entry.desiredAngle + "," + entry.distance + "," + entry.gain + "," + entry.x + "," + entry.y + "," + entry.heading + "\n");
////                currentElementIndex++;
////            } else {
////                saveToStorage("MecanumDataREDLEFTAUTO.csv", fileContents);
////                requestOpModeStop();
////            }
////        }
////        while(opModeIsActive()){
////            // waiting
////        }
////        requestOpModeStop();
//
////        while(gamepad1.a != true && opModeIsActive()){
////            drivetrain.odometry.getCurrentPose();
////        }
//
////        int numberOfElements;
////        int currentElementIndex = 0;
////        String fileContents = "";
////
////        while (opModeIsActive()) {
////            numberOfElements = drivetrain.odometry.outputData.size();
////
////            if (currentElementIndex < numberOfElements) {
////                OdometryData entry = drivetrain.odometry.outputData.getPoint(currentElementIndex);
////
////                telemetry.addData("#", numberOfElements);
////                telemetry.addData("curr", currentElementIndex);
////                telemetry.addData("data", entry);
////                telemetry.update();
////
////                fileContents = fileContents.concat(entry.startingPose.x + "," + entry.startingPose.y + "," + entry.startingPose.heading + "," + entry.left_raw + "," + entry.right_raw + "," + entry.center_raw + "," + entry.delta_left + "," + entry.delta_right + "," + entry.delta_center + "," + entry.arc_length_center + "," + entry.phi + "," + entry.delta_x + "," + entry.delta_y + "," + entry.arc_length_strafe + "," + entry.strafe + "," + entry.strafe_x + "," + entry.strafe_y + "," + entry.endingPose.x + "," + entry.endingPose.y + "," + entry.endingPose.heading + "\n");
////                currentElementIndex++;
////            } else {
////                saveToStorage("OdometryData.csv", fileContents);
////                pause(5000);
////
////                requestOpModeStop();
////            }
////        }
//    }
//
//    private Thread purePursuitThread = new Thread();
//
//    public static final double lookaheadRadiusInches = 8;
//
//    public void startPurePursuitCalculation() {
//        purePursuitThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                int currentCoordinateIndex = 1;
//                while (opModeIsActive()) {
//
//                    // assign values for previousTarget, current, and target coordinates
//                    Coordinate previousTargetCoordinate = purePursuitCoordinates.get(currentCoordinateIndex - 1);
//                    Pose currentPose = drivetrain.odometry.getCurrentPose();
//                    Coordinate currentCoordinate = new Coordinate(currentPose.x, currentPose.y);
//                    Coordinate nextTargetCoordinate = purePursuitCoordinates.get(currentCoordinateIndex);
//
//                    // if we are closer than lookahead distance, then check next coordinate
//                    if (distanceToPos(currentCoordinate, nextTargetCoordinate) < lookaheadRadiusInches) {
//                        if (currentCoordinateIndex < purePursuitCoordinates.size() - 2) { // 1
//                            currentCoordinateIndex++;
//                            previousTargetCoordinate = purePursuitCoordinates.get(currentCoordinateIndex - 1);
//                            currentPose = drivetrain.odometry.getCurrentPose();
//                            currentCoordinate = new Coordinate(currentPose.x, currentPose.y);
//                            nextTargetCoordinate = purePursuitCoordinates.get(currentCoordinateIndex);
//                        } else {
//                            if (distanceToPos(currentCoordinate, nextTargetCoordinate) < 2) {
//                                drivetrain.stop();
//                                break;
//                            }
//                        }
//                    }
//
//                    // switch reference frame to current coordinate being the origin (relative to current position)
//                    previousTargetCoordinate = new Coordinate(previousTargetCoordinate.x - currentCoordinate.x, previousTargetCoordinate.y - currentCoordinate.y);
//                    nextTargetCoordinate = new Coordinate(nextTargetCoordinate.x - currentCoordinate.x, nextTargetCoordinate.y - currentCoordinate.y);
//
//                    Coordinate coordinate1 = currentCoordinate;
//                    Coordinate coordinate2 = currentCoordinate;
//
//                    // lines and circles
//                    if ((nextTargetCoordinate.x - previousTargetCoordinate.x) == 0) {
//                        double x = nextTargetCoordinate.x;
//                        double forwardY = lookaheadRadiusInches;
//                        double backwardY = -lookaheadRadiusInches;
//                    } else {
//                        double m = (nextTargetCoordinate.y - previousTargetCoordinate.y) / (nextTargetCoordinate.x - previousTargetCoordinate.x);
//                        double b = nextTargetCoordinate.y - (m * nextTargetCoordinate.x);
//                        double leftX = (Math.sqrt((-1 * Math.pow(b, 2)) + (Math.pow(m, 2) * Math.pow(lookaheadRadiusInches, 2)) + Math.pow(lookaheadRadiusInches, 2)) - (b * m)) / (Math.pow(m, 2) + 1);
//                        double rightX = -1 * ((Math.sqrt((-1 * Math.pow(b, 2)) + (Math.pow(m, 2) * Math.pow(lookaheadRadiusInches, 2)) + Math.pow(lookaheadRadiusInches, 2)) - (b * m)) / (Math.pow(m, 2) + 1));
//                        double leftY = (m * leftX) + b;
//                        double rightY = (m * rightX) + b;
//
//                        // coordinates and motion
//                        coordinate1 = new Coordinate(leftX + currentCoordinate.x, leftY + currentCoordinate.y);
//                        coordinate2 = new Coordinate(rightX + currentCoordinate.x, rightY + currentCoordinate.y);
//                        nextTargetCoordinate = new Coordinate(nextTargetCoordinate.x + currentCoordinate.x, nextTargetCoordinate.y + currentCoordinate.y);
//                        targetCoordinate = findCloserCoordinate(coordinate1, coordinate2, nextTargetCoordinate);
//                    }
//
//                    autoClient.sendData(currentCoordinate.x + ", " + currentCoordinate.y + ", " + coordinate1.x + ", " + coordinate1.y + ", " + coordinate2.x + ", " + coordinate2.y + ", " + targetCoordinate.x + ", " + targetCoordinate.y);
//
//                    long time = System.currentTimeMillis();
//                    while (opModeIsActive() && System.currentTimeMillis() < time + 250) {
//                        // waiting
//                    }
//                }
//            }
//        });
//        purePursuitThread.setPriority(7);
//        purePursuitThread.setName("Pure Pursuit Background Thread");
//        purePursuitThread.start();
//    }
//
////    public void purePursuit(double movementPower) {
////
////            // movement
////
////    }
//
//    public Double angleToTravel(Coordinate currentCoordinate, Coordinate endCoordinate) {
//        double opposite = endCoordinate.y - currentCoordinate.y;
//        double adjacent = endCoordinate.x - currentCoordinate.x;
//        return (Math.atan2(opposite, adjacent) + Math.PI * 2) % (Math.PI * 2);
//    }
//
//    public double distanceToPos(Coordinate currentPosition, Coordinate endPosition) {
//        return Math.hypot((endPosition.x - currentPosition.x), (endPosition.y - currentPosition.y));
//    }
//
//    public Coordinate findCloserCoordinate(Coordinate coordinate1, Coordinate coordinate2, Coordinate targetCoordinate) {
//        if (distanceToPos(coordinate1, targetCoordinate) < distanceToPos(coordinate2, targetCoordinate)) {
//            return coordinate1;
//        } else {
//            return coordinate2;
//        }
//    }
//
//    public void pause(double milliseconds) {
//        long startTime = System.currentTimeMillis();
//        while ((System.currentTimeMillis() < startTime + milliseconds) && (opModeIsActive())) {
//            // waiting
//        }
//    }
//
//    public void initRobot(boolean telemetryEnabled, Pose startingPose) {
//        // Create the robot object
//        robot = new SkystoneRobot(this, true, startingPose);
//        drivetrain = (MecanumDrivetrain) robot.Drivetrain;
//        drivetrain.odometry.reset();
//
//        Hardware.getInstance().setHardwareMap(hardwareMap);
//        Hardware.getInstance().setRobot(robot);
//        Hardware.getInstance().startBackgroundUpdates();
//
//        stoneMechanism = (StoneMechanism) robot.Accessories.get(SkystoneRobot.AccessoryNames.StoneMechanism.ordinal());
//        skystoneDetector = (SkystoneDetector) robot.Accessories.get(SkystoneRobot.AccessoryNames.SkystoneDetector.ordinal());
//
//        if (telemetryEnabled) {
//            robot.Drivetrain.isTelemetryEnabled = true;
//            for (SimpleAccessory accessory : robot.Accessories) {
//                accessory.isTelemetryEnabled = true;
//            }
//        }
//    }
//
//
//    public void jsonParseFile(String fileName) {
//        try {
//            File programsDirectory = new File(fileName);
//            FileReader json = new FileReader(programsDirectory);
//            JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
//            JsonArray arr = jsonObject.getAsJsonArray("program");
//
//            for (int i = 0; i < arr.size(); i++) {
//                ArrayList<Object> temporaryStorage = new ArrayList<>();
//                temporaryStorage.clear();
//                String name = arr.get(i).getAsJsonObject().get("name").getAsString();
//                switch (name) {
//                    case "moveToPosition":
//                        temporaryStorage.add("moveToPosition");
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("x").getAsDouble());
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("y").getAsDouble());
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("movementPower").getAsDouble());
//                        allMovementCoordinates.add(new PursuitCoordinate((double) temporaryStorage.get(1), (double) temporaryStorage.get(2), false));
//                        funcParams.add(temporaryStorage);
//                        break;
//                    case "changeHeading":
//                        temporaryStorage.add("changeHeading");
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("degree").getAsDouble());
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("power").getAsDouble());
//                        funcParams.add(temporaryStorage);
//                        break;
//                    case "pause":
//                        temporaryStorage.add("pause");
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("pauseTime").getAsDouble());
//                        funcParams.add(temporaryStorage);
//                        telemetry.update();
//                        break;
//                    case "origin":
//                        double x = (arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("x").getAsDouble());
//                        double y = (arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("y").getAsDouble());
//                        double theta = (arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("theta").getAsDouble());
//                        startingPosition = new Pose(x, y, Math.toRadians(theta));
//                        break;
//                    case "setPosition":
//                        temporaryStorage.add("setPosition");
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("positionName").getAsString());
//                        funcParams.add(temporaryStorage);
//                        break;
//                    case "goToPosition":
//                        temporaryStorage.add("goToPosition");
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("positionName").getAsString());
//                        funcParams.add(temporaryStorage);
//                        break;
//                    case "vuforiaDetection":
//                        temporaryStorage.add("vuforiaDetection");
//                        funcParams.add(temporaryStorage);
//                        break;
//                    case "stopAtColor":
//                        temporaryStorage.add("stopAtColor");
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("color").getAsString());
//                        funcParams.add(temporaryStorage);
//                        break;
//                    case "collection":
//                        temporaryStorage.add("collection");
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("start").getAsString());
//                        funcParams.add(temporaryStorage);
//                        break;
//                    case "strafeToPosition":
//                        temporaryStorage.add("strafeToPosition");
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("x").getAsDouble());
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("y").getAsDouble());
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("movementPower").getAsDouble());
//                        allMovementCoordinates.add(new PursuitCoordinate((double) temporaryStorage.get(1), (double) temporaryStorage.get(2), true));
//                        funcParams.add(temporaryStorage);
//                        break;
//                    case "findAndCollect":
//                        temporaryStorage.add("findAndCollect");
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("detectTime").getAsDouble());
//                        funcParams.add(temporaryStorage);
//                        break;
//                    case "findAndCollect2":
//                        temporaryStorage.add("findAndCollect2");
//                        funcParams.add(temporaryStorage);
//                        break;
//                    default:
//                        temporaryStorage.clear();
//                        break;
//                }
//            }
//        } catch (Exception e) {
//            telemetry.addData("Error accessing program directory", e);
//            telemetry.update();
//        }
//
//        telemetry.addData("Waiting for start", "please begin auto");
//        telemetry.update();
//    }
//
//    public void jsonParseString(String text) {
//        try {
//            JsonObject jsonObject = new JsonParser().parse(text).getAsJsonObject();
//            JsonArray arr = jsonObject.getAsJsonArray("program");
//
//            for (int i = 0; i < arr.size(); i++) {
//                ArrayList<Object> temporaryStorage = new ArrayList<>();
//                temporaryStorage.clear();
//                String name = arr.get(i).getAsJsonObject().get("name").getAsString();
//                switch (name) {
//                    case "moveToPosition":
//                        temporaryStorage.add("moveToPosition");
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("x").getAsDouble());
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("y").getAsDouble());
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("movementPower").getAsDouble());
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("backwards").getAsBoolean());
//                        allMovementCoordinates.add(new PursuitCoordinate((double) temporaryStorage.get(1), (double) temporaryStorage.get(2), false));
//                        funcParams.add(temporaryStorage);
//                        break;
//                    case "changeHeading":
//                        temporaryStorage.add("changeHeading");
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("degree").getAsDouble());
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("power").getAsDouble());
//                        funcParams.add(temporaryStorage);
//                        break;
//                    case "pause":
//                        temporaryStorage.add("pause");
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("pauseTime").getAsDouble());
//                        funcParams.add(temporaryStorage);
//                        telemetry.update();
//                        break;
//                    case "origin":
//                        double x = (arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("x").getAsDouble());
//                        double y = (arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("y").getAsDouble());
//                        double theta = (arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("theta").getAsDouble());
//                        startingPosition = new Pose(x, y, Math.toRadians(theta));
//                        break;
//                    case "setPosition":
//                        temporaryStorage.add("setPosition");
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("positionName").getAsString());
//                        funcParams.add(temporaryStorage);
//                        break;
//                    case "goToPosition":
//                        temporaryStorage.add("goToPosition");
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("positionName").getAsString());
//                        funcParams.add(temporaryStorage);
//                        break;
//                    case "foundationServo":
//                        temporaryStorage.add("foundationServo");
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("position").getAsString());
//                        funcParams.add(temporaryStorage);
//                        break;
//                    case "stopAtColor":
//                        temporaryStorage.add("stopAtColor");
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("color").getAsString());
//                        funcParams.add(temporaryStorage);
//                        break;
//                    case "collection":
//                        temporaryStorage.add("collection");
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("start").getAsString());
//                        funcParams.add(temporaryStorage);
//                        break;
//                    case "strafeToPosition":
//                        temporaryStorage.add("strafeToPosition");
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("x").getAsDouble());
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("y").getAsDouble());
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("movementPower").getAsDouble());
//                        allMovementCoordinates.add(new PursuitCoordinate((double) temporaryStorage.get(1), (double) temporaryStorage.get(2), true));
//                        funcParams.add(temporaryStorage);
//                        break;
//                    case "findAndCollect":
//                        temporaryStorage.add("findAndCollect");
//                        temporaryStorage.add(arr.get(i).getAsJsonObject().get("params").getAsJsonObject().get("detectTime").getAsDouble());
//                        funcParams.add(temporaryStorage);
//                        break;
//                    case "findAndCollect2":
//                        temporaryStorage.add("findAndCollect2");
//                        funcParams.add(temporaryStorage);
//                        break;
//                    case "dropBlock":
//                        temporaryStorage.add("dropBlock");
//                        funcParams.add(temporaryStorage);
//                        break;
//                    default:
//                        temporaryStorage.clear();
//                        break;
//                }
//            }
//        } catch (Exception e) {
//            telemetry.addData("Error accessing program directory", e);
//            telemetry.update();
//        }
//
//        telemetry.addData("Waiting for start", "please begin auto");
//        telemetry.update();
//    }
//
//    public ArrayList<String> getProgramNames() {
//        ArrayList<String> programNamesArray = new ArrayList<>();
//        try {
//
//            File programsDirectory = new File("/sdcard/Innov8rz/programs");
//            File[] programNames = programsDirectory.listFiles();
//
//            for (int x = 0; x < programNames.length; x++) {
//                programNamesArray.add(programNames[x].toString());
//            }
////            for (int b = 0; b < programNames.length; b++) {
////
////                FileReader json = new FileReader(programNames[b]);
////                JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
////                JsonArray arr = jsonObject.getAsJsonArray("program");
////                ArrayList<String> namesOfFuncs = new ArrayList<>();
////
////
////                for (int i = 0; i < arr.size(); i++) {
////                    String name = arr.getPoint(i).getAsJsonObject().getPoint("name").getAsString();
////                    namesOfFuncs.add(name);
////                }
////                int i;
////                int error = 0;
////            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return programNamesArray;
//    }
//
//    public String selectProgram() {
//        ArrayList<String> programNames = new ArrayList<>();
//        for (int i = 0; i < getProgramNames().size(); i++) {
//            telemetry.addData("ProgramFile Name", getProgramNames().get(i));
//            telemetry.update();
//            programNames.add(getProgramNames().get(i));
//        }
//        int currentProgramIndex = 0;
//        while (!gamepad1.a) { //&& opModeIsActive()
//            if (gamepad1.dpad_up == true) {
//                while (gamepad1.dpad_up == true) {
//                }//wait to release
//                if (currentProgramIndex < programNames.size() - 1) {
//                    currentProgramIndex++;
//                    telemetry.clear();
//                    for (int i = 0; i < programNames.size(); i++) {
//                        telemetry.addData("ProgramFile Name", programNames.get(i));
//                        telemetry.update();
//                    }
//                    telemetry.addData("Current Program Selected", programNames.get(currentProgramIndex));
//                }
//            } else if (gamepad1.dpad_down == true) {
//                while (gamepad1.dpad_down == true) {
//                }//wait to release
//                if (currentProgramIndex > 0) {
//                    currentProgramIndex--;
//                    telemetry.clear();
//                    for (int i = 0; i < programNames.size(); i++) {
//                        telemetry.addData("ProgramFile Name", programNames.get(i));
//                        telemetry.update();
//                    }
//                    telemetry.addData("Current Program Selected", programNames.get(currentProgramIndex));
//                }
//            }
//            telemetry.update();
//        }
//        telemetry.addData("Finished with selecting program", "going to parsing");
//        telemetry.update();
//        return programNames.get(currentProgramIndex);
//    }
//
//    public void runAllFuncs() {
//        for (int i = 0; i < funcParams.size(); i++) {
//            drivetrain.odometry.getCurrentPose();
//            switch (funcParams.get(i).get(0).toString()) {
//                case "moveToPosition":
//                    if ((boolean) funcParams.get(i).get(4)) {
//                        drivetrain.rampTankToPosition(new Pose((double) funcParams.get(i).get(1), (double) funcParams.get(i).get(2), Math.toRadians(0)), (double) funcParams.get(i).get(3), MecanumDrivetrain.TankDirection.backward);
//                    } else {
//                        drivetrain.rampTankToPosition(new Pose((double) funcParams.get(i).get(1), (double) funcParams.get(i).get(2), Math.toRadians(0)), (double) funcParams.get(i).get(3), MecanumDrivetrain.TankDirection.forward);
//                    }
//                    break;
//                case "changeHeading":
//                    drivetrain.turnToHeading(Math.toRadians((double) funcParams.get(i).get(1)), (double) funcParams.get(i).get(2));
//                    break;
//                case "pause":
//                    pause((double) funcParams.get(i).get(1));
//                    break;
//                case "setPosition":
//                    // code
//                    break;
//                case "goToPosition":
////                    drivetrain.odometry.getCoordinate(funcParams.getPoint(i).getPoint(1).toString());
//                    break;
//                case "strafeToPosition":
//                    drivetrain.strafeToPosition(new Pose((double) funcParams.get(i).get(1), (double) funcParams.get(i).get(2), Math.toRadians(0)), (double) funcParams.get(i).get(3));
//                    break;
//                case "findAndCollect":
//                    robot.findAndCollect((double) funcParams.get(i).get(1));
//                    break;
//                case "findAndCollect2":
////                    collectSecondBlock(new Pose(0, 0, 0));
//                    break;
//                case "foundationServo":
//                    if (funcParams.get(i).get(1).toString().contains("Open")) {
//                        stoneMechanism.foundationServo(StoneMechanism.FoundationModes.open);
//                    } else if (funcParams.get(i).get(1).toString().contains("Close")) {
//                        stoneMechanism.foundationServo(StoneMechanism.FoundationModes.grab);
//                    }
//                    break;
//                case "dropBlock":
//                    stoneMechanism.blockServo(StoneMechanism.BlockModes.in);
//                    break;
//                default:
//                    telemetry.addData("runAllFuncs", "Function doesn't exist");
//                    break;
//            }
//        }
//    }
//
//    public void saveToStorage(String fileName, String fileContents) {
//        File file = new File(Environment.getExternalStorageDirectory(), fileName);
//        try {
//            FileOutputStream fos = new FileOutputStream(file);
//            fos.write(fileContents.getBytes());
//            fos.flush();
//            fos.close();
//        } catch (Exception e) {
//
//        }
//    }
//}