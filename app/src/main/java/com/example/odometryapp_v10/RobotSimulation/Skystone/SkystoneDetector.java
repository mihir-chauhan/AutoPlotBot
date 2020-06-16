package com.example.odometryapp_v10.RobotSimulation.Skystone;//package internal.Skystone;
//
////import org.firstinspires.ftc.teamcode.Autonomous.AutoTemplateUDP;
//import org.firstinspires.ftc.teamcode.Robot.Structure.SimpleAccessory;
//
//public class SkystoneDetector extends SimpleAccessory {
//
//    SmartColorSensor colorSensorLeft, colorSensorRight;
//
//    private int numOfRetries = 3; // if any of the color sensors doesn't detect a valid color
//    private int fallbackBlockPosition = 2; // if the detection consistently fails
//
//    /**
//     *  left block = 1
//     *  center block = 2
//     *  right block = 3
//     *  no value = 0 -- should not happen
//     *  unknown = -1 -- should not happen
//     */
//
//    public SkystoneDetector(String leftSensorDeviceName, String rightSensorDeviceName) {
//        colorSensorLeft = new SmartColorSensor(SkystoneRobot.opMode, leftSensorDeviceName);
//        colorSensorRight = new SmartColorSensor(SkystoneRobot.opMode, rightSensorDeviceName);
//    }
//
//    public int findBlock(double milliseconds) {
////        SmartColorSensor.Color leftColor = SmartColorSensor.Color.UNKNOWN;
////        SmartColorSensor.Color rightColor = SmartColorSensor.Color.UNKNOWN;
////
////        for (int i = 0; i < numOfRetries; i++) {
////            leftColor = SmartColorSensor.Color.UNKNOWN;
////            rightColor = SmartColorSensor.Color.UNKNOWN;
////            while ((leftColor == SmartColorSensor.Color.UNKNOWN || rightColor == SmartColorSensor.Color.UNKNOWN) && SkystoneRobot.opModeIsActive()) {
////                leftColor = colorSensorLeft.normalizedYellowBlack(milliseconds);
////                rightColor = colorSensorRight.normalizedYellowBlack(milliseconds);
////                AutoTemplateUDP.autoClient.sendData("leftColor: " + leftColor + " rightColor: " + rightColor);
////            }
////            AutoTemplateUDP.autoClient.sendData("FINALleftColor: " + leftColor + " FINALrightColor: " + rightColor);
////            if (leftColor != SmartColorSensor.Color.NO_RESULT && rightColor != SmartColorSensor.Color.NO_RESULT) {
////                break;
////            }
////        }
////        return aggregateColorValues(leftColor, rightColor);
//        int blockPosition = 0;
//        for (int i = 0; i < numOfRetries; i++) {
//            blockPosition = findBlockPosition(milliseconds);
//            if (blockPosition != -1) {
//                break;
//            } else {
////                AutoTemplateUDP.autoClient.sendData("blockposition: " + blockPosition);
//            }
//        }
//        if (blockPosition != -1) {
//            return blockPosition;
//        } else {
//            return fallbackBlockPosition;
//        }
//    }
//
//    public int findBlockPosition(double milliSec) {//}, Pose finalPos) {
////        telemetry.setAutoClear(true);
//
////        if(!colorSensorLeft.rangeCheck() || !colorSensorRight.rangeCheck()){
////            drivetrain.strafeToPosition(finalPos, 0.4);
////        }
//
//        int leftAndRight = 0;
//
//        colorSensorRight.clearColorArrays();
//        colorSensorLeft.clearColorArrays();
////        while(opModeIsActive()) {
//        double currentTime = System.currentTimeMillis();
//        int i = 0;
//        while (System.currentTimeMillis() < currentTime + milliSec && SkystoneRobot.opModeIsActive()) {
////                telemetry.addData("finding", null);
////                telemetry.update();
//            colorSensorLeft.addToArray(false);
//            colorSensorRight.addToArray(false);
//            i++;
//        }
//        SmartColorSensor.Color leftColor = colorSensorLeft.addToArray(true);
//        SmartColorSensor.Color rightColor = colorSensorRight.addToArray(true);
////        telemetry.addData("leftColor", leftColor);
////        telemetry.addData("rightColor", rightColor);
////        telemetry.addData("ran loop: ", i);
////        telemetry.update();
////        }
//        /**
//         * left block = 1
//         * center block = 2
//         * right block = 3
//         * no value = 0
//         * unknown = -1
//         */
//
//        //leftandright
//        if (leftColor != SmartColorSensor.Color.UNKNOWN && rightColor != SmartColorSensor.Color.UNKNOWN) {
//            switch (leftColor) {
//                case YELLOW:
//                    switch (rightColor) {
//                        case YELLOW:
//                            leftAndRight = 1;
//                            break;
//                        case BLACK:
//                            leftAndRight = 2;
//                            break;
//                    }
//                    break;
//                case BLACK:
//                    switch (rightColor) {
//                        case YELLOW:
//                            leftAndRight = 3;
//                            break;
//                        case BLACK:
//                            leftAndRight = -1;
//                            break;
//                    }
//                    break;
//            }
//        } else {
//            leftAndRight = -1;
//        }
//
//
//        int finalBlockPosition = 0;
//        if (leftAndRight != -1) {
//            finalBlockPosition = leftAndRight;
//        } else {
//            finalBlockPosition = fallbackBlockPosition;
//        }
////        telemetry.addData("leftAndRight", leftAndRight);
////        telemetry.addData("finalBlockPosition", finalBlockPosition);
////        telemetry.update();
////        saveToStorage("colors.txt", "LR: " + leftAndRight + ", FBP: " + finalBlockPosition);
////        AutoTemplateUDP.autoClient.sendData("LR: " + leftAndRight + ", FBP: " + finalBlockPosition);
//        return finalBlockPosition;
//    }
//
//    private int aggregateColorValues(SmartColorSensor.Color leftColor, SmartColorSensor.Color rightColor) {
//        if (leftColor != SmartColorSensor.Color.NO_RESULT && rightColor != SmartColorSensor.Color.NO_RESULT) {
//            switch (leftColor) {
//                case YELLOW:
//                    switch (rightColor) {
//                        case YELLOW:
//                            return 3;
//                        case BLACK:
//                            return 1;
//                    }
//                case BLACK:
//                    switch (rightColor) {
//                        case YELLOW:
//                            return 2;
//                        case BLACK:
//                            return fallbackBlockPosition; // fallback to center due to failed detection
//                    }
//            }
//        }
//        return fallbackBlockPosition; // fallback to center due to failed detection
//    }
//}
