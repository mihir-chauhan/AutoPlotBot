package com.example.odometryapp_v10.RobotSimulation.Structure;//package internal.Structure;
//
//import java.util.ArrayList;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.hardware.HardwareDevice;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import org.firstinspires.ftc.robotcore.external.Telemetry;
//
//public class Drivetrain {
//
//    // OpMode Variables
//
//    public OpMode opMode;
//    public Telemetry telemetry;
//    public HardwareMap hardwareMap;
//
//    public boolean isTelemetryEnabled = false;
//
//    // Motor and Sensor ArrayLists
//
//    public ArrayList<DcMotor> DcMotors;
//    public ArrayList<HardwareDevice> Sensors;
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
