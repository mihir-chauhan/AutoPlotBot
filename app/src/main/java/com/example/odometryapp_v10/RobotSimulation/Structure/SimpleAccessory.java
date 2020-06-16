package com.example.odometryapp_v10.RobotSimulation.Structure;//package internal.Structure;
//
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.HardwareDevice;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.hardware.Servo;
//
//import org.firstinspires.ftc.robotcore.external.Telemetry;
//
//import java.util.ArrayList;
//
//public class SimpleAccessory {
//
//    // OpMode Variables
//
//    public OpMode opMode;
//    public Telemetry telemetry;
//    public HardwareMap hardwareMap;
//
//    public boolean isTelemetryEnabled = false;
//
//    // Telemetry function to relay data from the external classes to the OpMode
//
//    public void addTelemetry(String caption, Object value) {
//        if (this.isTelemetryEnabled) {
//            this.telemetry.addData(caption, value);
//            this.telemetry.update();
//        }
//    }
//
//}