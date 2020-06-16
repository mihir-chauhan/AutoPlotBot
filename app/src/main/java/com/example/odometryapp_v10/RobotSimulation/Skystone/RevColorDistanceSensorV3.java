package com.example.odometryapp_v10.RobotSimulation.Skystone;//package internal.Skystone;
//
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.hardware.ColorSensor;
//import com.qualcomm.robotcore.hardware.DistanceSensor;
//import com.qualcomm.robotcore.hardware.I2cAddr;
//
//import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
//
//public class RevColorDistanceSensorV3 implements DistanceSensor, ColorSensor {
//
//    ColorSensor colorSensor;
//    DistanceSensor distanceSensor;
//
//    public RevColorDistanceSensorV3(OpMode opMode, String deviceName){
//        colorSensor = opMode.hardwareMap.get(ColorSensor.class, deviceName);
//        distanceSensor = opMode.hardwareMap.get(DistanceSensor.class, deviceName);
//    }
//
//    // Distance Sensor Implementation
//    @Override
//    public double getDistance(DistanceUnit unit) {
//        return distanceSensor.getDistance(unit);
//    }
//
//    @Override
//    public Manufacturer getManufacturer() {
//        return distanceSensor.getManufacturer();
//    }
//
//    @Override
//    public String getDeviceName() {
//        return distanceSensor.getDeviceName();
//    }
//
//    @Override
//    public String getConnectionInfo() {
//        return distanceSensor.getConnectionInfo();
//    }
//
//    @Override
//    public int getVersion() {
//        return distanceSensor.getVersion();
//    }
//
//    @Override
//    public void resetDeviceConfigurationForOpMode() {
//        distanceSensor.resetDeviceConfigurationForOpMode();
//    }
//
//    @Override
//    public void close() {
//        distanceSensor.close();
//    }
//
//    // Color Sensor Implementation
//    @Override
//    public int red() {
//        return colorSensor.red();
//    }
//
//    @Override
//    public int green() {
//        return colorSensor.green();
//    }
//
//    @Override
//    public int blue() {
//        return colorSensor.blue();
//    }
//
//    @Override
//    public int alpha() {
//        return colorSensor.alpha();
//    }
//
//    @Override
//    public int argb() {
//        return colorSensor.argb();
//    }
//
//    @Override
//    public void enableLed(boolean enable) {
//        colorSensor.enableLed(enable);
//    }
//
//    @Override
//    public void setI2cAddress(I2cAddr newAddress) {
//        colorSensor.setI2cAddress(newAddress);
//    }
//
//    @Override
//    public I2cAddr getI2cAddress() {
//        return colorSensor.getI2cAddress();
//    }
//}
