package com.example.odometryapp_v10.RobotSimulation.Skystone;//package internal.Skystone;
//
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
//import org.firstinspires.ftc.teamcode.Robot.Structure.Accessory;
//import java.util.ArrayList;
//
//public class SmartColorSensor extends Accessory {
//
//    RevColorDistanceSensorV3 colorDistanceSensor;
//
//    public enum Color {
//        RED, BLUE, YELLOW, BLACK, UNKNOWN, NO_RESULT
//    }
//
//    private long sampleStartTime = 0;
//    private long sampleStopTime = 0;
//
//    private double r, g, b;
//    private double ALPHA_BASE = 100;
//
//    public ArrayList<Double> normalizedRedValues = new ArrayList<>();
//    public ArrayList<Double> normalizedGreenValues = new ArrayList<>();
//    public ArrayList<Double> normalizedBlueValues = new ArrayList<>();
//
//    public SmartColorSensor(OpMode opMode, String deviceName) {
//        this.opMode = opMode;
//        this.telemetry = opMode.telemetry;
//        this.hardwareMap = opMode.hardwareMap;
//
//        this.colorDistanceSensor = new RevColorDistanceSensorV3(opMode, deviceName);
//        addTelemetry("SmartColorSensor " + deviceName, "Ready");
//    }
//
//    public void clearColorArrays(){
//        normalizedRedValues.clear();
//        normalizedGreenValues.clear();
//        normalizedBlueValues.clear();
//    }
//
//    public Color normalizedRedBlue() {
//        r = colorDistanceSensor.red();
//        g = colorDistanceSensor.green();
//        b = colorDistanceSensor.blue();
//        ALPHA_BASE = 100;
//
////        telemetry.addData("red", r);
////        telemetry.addData("blue", g);
////        telemetry.addData("green", b);
//
//        // Either of the color sensors detects value greater than 10 in proximity to object
//        if (r > 10 || b > 10 || g > 10) {
//            double f = ALPHA_BASE / colorDistanceSensor.alpha();
//            double nR = f * r;
//            double nB = f * b;
//            double nG = f * g;
//
////            telemetry.addData("red_left", nR);
////            telemetry.addData("blue_left", nG);
////            telemetry.addData("green_left",  nB);
////            telemetry.addData("alpha", colorDistanceSensor.alpha());
//
//            if ((nR > 60) && (nB < 40) && (nG < 40)) {
//                return Color.RED;
//            } else if ((nR < 40) && (nB > 45) && (nG < 40)) {
//                return Color.BLUE;
//            } else {
//                return Color.NO_RESULT;
//            }
//        }
//        return Color.UNKNOWN;
//    }
//
//    public Color normalizedYellowBlack() {
//        r = colorDistanceSensor.red();
//        g = colorDistanceSensor.green();
//        b = colorDistanceSensor.blue();
//        ALPHA_BASE = 100;
//
////        telemetry.addData("red", r);
////        telemetry.addData("blue", g);
////        telemetry.addData("green", b);
////        telemetry.addData("alpha", colorDistanceSensor.alpha());
//
//        // Either of the color sensors detects value greater than 10 in proximity to object
//        if (r > 10 || b > 10 || g > 10) {
//            double f = ALPHA_BASE / colorDistanceSensor.alpha();
//            double nR = f * r;
//            double nB = f * b;
//            double nG = f * g;
//
////            telemetry.addData("red_left", nR);
////            telemetry.addData("blue_left", nB);
////            telemetry.addData("green_left", nG);
//
//            double value = -nB+nR;
////            telemetry.addData("value_left", value);
//
//            if (value > 20) {
//                return Color.YELLOW;
//            } else if (value < 5) {
//                return Color.BLACK;
//            } else {
//                return Color.NO_RESULT;
//            }
//        }
//        return Color.UNKNOWN;
//    }
//
//    public Color normalizedYellowBlack(double milliseconds) {
//        if (sampleStartTime == 0 && sampleStopTime == 0) {
//            clearColorArrays();
//            sampleStartTime = System.currentTimeMillis();
//            sampleStopTime = sampleStartTime + (long) milliseconds;
//            return addToArray(false);
//        } else if (System.currentTimeMillis() < sampleStopTime) {
//            return addToArray(false);
//        } else {
//            sampleStartTime = 0;
//            sampleStopTime = 0;
//            return addToArray(true);
//        }
//    }
//
//    public double average(ArrayList<Double> arrayList) {
//        double total = 0;
//        for (int i = 0; i < arrayList.size(); i++) {
//            total += arrayList.get(i);
//        }
//        return total / arrayList.size();
//    }
//
//    public Color addToArray(boolean average) {
//        if (!average) {
//            r = colorDistanceSensor.red();
//            g = colorDistanceSensor.green();
//            b = colorDistanceSensor.blue();
//            ALPHA_BASE = 100;
//
//            double f = ALPHA_BASE / colorDistanceSensor.alpha();
//            double nR = f * r;
//            double nB = f * b;
//            double nG = f * g;
//
//            normalizedRedValues.add(nR);
//            normalizedGreenValues.add(nG);
//            normalizedBlueValues.add(nB);
//
//            return Color.UNKNOWN;
//        } else {
//            double nR_average = average(normalizedRedValues);
//            double nG_average = average(normalizedGreenValues);
//            double nB_average = average(normalizedBlueValues);
//            double value = -nB_average + nR_average;
//            if (value >= 20) {
//                return Color.YELLOW;
//            } else if (value < 5) {
//                return Color.BLACK;
//            } else {
//                return Color.NO_RESULT;
//            }
//        }
//    }
//
//    public void printRGBOutputValues() {
//        double r = colorDistanceSensor.red();
//        double b = colorDistanceSensor.blue();
//        double g = colorDistanceSensor.green();
//        double ALPHA_BASE = 100;
//
//        // Either of the color sensors detects value greater than 10 in proximity to object
//        if (r > 10 || b > 10 || g > 10) {
//            double f = ALPHA_BASE / colorDistanceSensor.alpha();
//            double nR = f * r;
//            double nB = f * b;
//            double nG = f * g;
//
////            telemetry.addData("Red", nR);
////            telemetry.addData("Blue", nB);
////            telemetry.addData("Green", nG);
////            telemetry.addData("Alpha", colorDistanceSensor.alpha());
////            telemetry.update();
//        }
//    }
//
//    public double getDistance(DistanceUnit unit){
//        return colorDistanceSensor.getDistance(unit);
//    }
//
//    public boolean rangeCheck() {
//        double rangeConstant = 30;
//        return (colorDistanceSensor.getDistance(DistanceUnit.MM) < rangeConstant);
//    }
//}
