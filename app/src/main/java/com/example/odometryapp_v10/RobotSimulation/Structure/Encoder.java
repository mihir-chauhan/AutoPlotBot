package com.example.odometryapp_v10.RobotSimulation.Structure;//package internal.Structure;
//
//import com.qualcomm.robotcore.hardware.DcMotor;
//import org.firstinspires.ftc.teamcode.Robot.Skystone.MecanumDrivetrain;
//
//public class Encoder {
//
//    public static final double d = 14.5625; // distance between left and right wheels
//    public static final double ds = 3.65625; // (d / 2) - 3.625 which is distance from right wheel to strafe wheel
//
//    DcMotor dcMotor;
//    boolean negate;
//
//    public static final double wheelDiameterInches = 38 / 25.4;
//    public static final double ticksPerRev = 1000;
//    public static double circumferenceOfWheel = Math.PI * wheelDiameterInches;
//    public static double inchesPerTick = circumferenceOfWheel / ticksPerRev;
//
//    double previousEncoderPosition = 0;
//    double deltaEncoderCounts = 0;
//    double deltaInches = 0;
//
//    public Encoder(DcMotor dcMotor, boolean negate) {
//        this.dcMotor = dcMotor;
//        this.negate = negate;
//    }
//
//    public double getCurrentDeltaInches() {
//        deltaEncoderCounts = (getCurrentPosition() - previousEncoderPosition);
//        deltaInches = encodersToInches(deltaEncoderCounts);
//        previousEncoderPosition = getCurrentPosition();
//        return negate ? negate(deltaInches) : deltaInches;
//    }
//
//    public double getCurrentPosition() {
//        return Hardware.getInstance().getCurrentPosition(dcMotor);
////        return dcMotor.getCurrentPose();
//    }
//
//    public double negate(double value) {
//        return (value * -1);
//    }
//
//    public double encodersToInches(double encoderValue) {
//        return inchesPerTick * encoderValue;
//    }
//
//    public void reset() {
//        previousEncoderPosition = 0;
//        deltaEncoderCounts = 0;
//        deltaInches = 0;
//        dcMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        dcMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//    }
//}
