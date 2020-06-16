package com.example.odometryapp_v10.RobotSimulation.Structure;//package internal.Structure;
//
//import com.qualcomm.hardware.lynx.LynxAnalogInputController;
//import com.qualcomm.hardware.lynx.LynxController;
//import com.qualcomm.hardware.lynx.LynxDigitalChannelController;
//import com.qualcomm.hardware.lynx.LynxModule;
//import com.qualcomm.robotcore.hardware.AnalogInput;
//import com.qualcomm.robotcore.hardware.AnalogInputController;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
//import com.qualcomm.robotcore.hardware.DigitalChannel;
//import com.qualcomm.robotcore.hardware.DigitalChannelController;
//import com.qualcomm.robotcore.hardware.DigitalChannelImpl;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
//
//import org.firstinspires.ftc.robotcore.external.Telemetry;
//import org.firstinspires.ftc.robotcore.external.navigation.Rotation;
//import org.firstinspires.ftc.teamcode.Robot.Skystone.MecanumDrivetrain;
//import org.firstinspires.ftc.teamcode.Robot.Skystone.SkystoneRobot;
//import org.openftc.revextensions2.ExpansionHubEx;
//import org.openftc.revextensions2.RevBulkData;
//
//import java.lang.reflect.Field;
//
//public class Hardware {
//
//    private Thread thread = new Thread();
//
//    private SkystoneRobot robot;
//
//    private static Hardware hardwareInstance = null;
//
//    private HardwareMap hardwareMap;
//    private Telemetry telemetry;
//
//    private ExpansionHubEx expansionHub1, expansionHub2;
//    private RevBulkData bulkDataExHub1, bulkDataExHub2;
//
//    private Hardware() {
//
//    }
//
//    public static Hardware getInstance() {
//        if (hardwareInstance == null) {
//            hardwareInstance = new Hardware();
//        }
//        return hardwareInstance;
//    }
//
//    boolean stopThread = false;
//
//    public void setBusSpeedI2C() {
//        expansionHub1.setAllI2cBusSpeeds(ExpansionHubEx.I2cBusSpeed.FAST_400K);
//        expansionHub2.setAllI2cBusSpeeds(ExpansionHubEx.I2cBusSpeed.FAST_400K);
//    }
//
//    public void resumeBackgroundUpdates(){
//        startBackgroundUpdates();
//    }
//
//    public void startBackgroundUpdates() {
//        stopThread = false;
////        AutoTemplateUDP.autoClient.sendData("starting/resuming data");
//        thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
////                AutoTemplateUDP.autoClient.sendData("into run function of thread");
//                while (!stopThread) {
//                    RevBulkData tempBulkData1 = expansionHub1.getBulkInputData();
//                    RevBulkData tempBulkData2 = expansionHub2.getBulkInputData();
//                    if (tempBulkData1 != null) {
//                        bulkDataExHub1 = tempBulkData1;
//                    }
//                    if (tempBulkData2 != null) {
//                        bulkDataExHub2 = tempBulkData2;
//                    }
//                    if (robot != null) {
//                        ((MecanumDrivetrain) robot.Drivetrain).odometry.updatePosition();
//                    }
//                }
//            }
//        });
//        thread.setPriority(7);
//        thread.setName("Bulk Data Background Thread");
//        thread.start();
//    }
//
//    public void stopBackgroundUpdates(){
//        stopThread = true;
//        thread.interrupt();
//    }
//
//    public void setHardwareMap(HardwareMap hardwareMap) {
//        this.hardwareMap = hardwareMap;
//        expansionHub1 = hardwareMap.get(ExpansionHubEx.class, "Expansion Hub 2");
//        expansionHub2 = hardwareMap.get(ExpansionHubEx.class, "Expansion Hub 10");
//    }
//
//    public void setTelemetry(Telemetry telemetry) {
//        this.telemetry = telemetry;
//    }
//
//    public void setRobot(SkystoneRobot robot) {
//        this.robot = robot;
//    }
//
//    private LynxModule getLynxFromController(LynxController controller) {
//        Field moduleField;
//        try {
//            moduleField = LynxController.class.getDeclaredField("module");
//            moduleField.setAccessible(true);
//            return (LynxModule) moduleField.get(controller);
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    public Direction getDirection(DcMotor dcMotor) {
//        MotorConfigurationType motorType = dcMotor.getMotorType();
//        DcMotorSimple.Direction direction = dcMotor.getDirection();
//        if (motorType.getOrientation() == Rotation.CCW) {
//            return direction.inverted();
//        } else {
//            return direction;
//        }
//    }
//
//    public int getCurrentPosition(DcMotor dcMotor) {
//        if (expansionHub1.getStandardModule().getModuleAddress() == getLynxFromController((LynxController) dcMotor.getController()).getModuleAddress()) {
//            return bulkDataExHub1.getMotorCurrentPosition(dcMotor);
//        } else if (expansionHub2.getStandardModule().getModuleAddress() == getLynxFromController((LynxController) dcMotor.getController()).getModuleAddress()) {
//            return bulkDataExHub2.getMotorCurrentPosition(dcMotor);
//        }
//        return Integer.MAX_VALUE;
//    }
//
//    public int getMotorVelocity(DcMotor dcMotor) {
//        if (expansionHub1.getStandardModule().getModuleAddress() == getLynxFromController((LynxController) dcMotor.getController()).getModuleAddress()) {
//            return bulkDataExHub1.getMotorVelocity(dcMotor);
//        } else if (expansionHub2.getStandardModule().getModuleAddress() == getLynxFromController((LynxController) dcMotor.getController()).getModuleAddress()) {
//            return bulkDataExHub2.getMotorVelocity(dcMotor);
//        }
//        return Integer.MAX_VALUE;
//    }
//
//    public boolean isMotorAtTargetPosition(DcMotor dcMotor) {
//        if (expansionHub1.getStandardModule().getModuleAddress() == getLynxFromController((LynxController) dcMotor.getController()).getModuleAddress()) {
//            return bulkDataExHub1.isMotorAtTargetPosition(dcMotor);
//        } else if (expansionHub2.getStandardModule().getModuleAddress() == getLynxFromController((LynxController) dcMotor.getController()).getModuleAddress()) {
//            return bulkDataExHub2.isMotorAtTargetPosition(dcMotor);
//        }
//        return false;
//    }
//
//    public int getAnalogInputValue(AnalogInput input) {
//        AnalogInputController controller = null;
//        int port = -1;
//
//        try {
//            Field controllerField = AnalogInput.class.getDeclaredField("controller");
//            controllerField.setAccessible(true);
//            controller = (AnalogInputController) controllerField.get(input);
//
//            Field channelField = AnalogInput.class.getDeclaredField("channel");
//            channelField.setAccessible(true);
//            port = (int) channelField.get(input);
//        }
//        catch (Exception e) {
//            return Integer.MAX_VALUE;
//        }
//
//        if (controller instanceof LynxAnalogInputController) {
//            if (expansionHub1.getStandardModule().getModuleAddress() == getLynxFromController((LynxController) controller).getModuleAddress()) {
//                return bulkDataExHub1.getAnalogInputValue(input);
//            } else if (expansionHub2.getStandardModule().getModuleAddress() == getLynxFromController((LynxController) controller).getModuleAddress()) {
//                return bulkDataExHub2.getAnalogInputValue(input);
//            }
//        }
//        return Integer.MAX_VALUE;
//    }
//
//    public boolean getDigitalInputState(DigitalChannel digitalChannel) {
//        DigitalChannelController controller = null;
//        int port = -1;
//
//        try {
//            Field controllerField = DigitalChannelImpl.class.getDeclaredField("controller");
//            controllerField.setAccessible(true);
//            controller = (DigitalChannelController) controllerField.get(digitalChannel);
//
//            Field channelField = DigitalChannelImpl.class.getDeclaredField("channel");
//            channelField.setAccessible(true);
//            port = (int) channelField.get(digitalChannel);
//        } catch (Exception e) {
//            return false;
//        }
//
//        if (controller instanceof LynxDigitalChannelController) {
//            if (expansionHub1.getStandardModule().getModuleAddress() == getLynxFromController((LynxController) controller).getModuleAddress()) {
//                return bulkDataExHub1.getDigitalInputState(digitalChannel);
//            } else if (expansionHub2.getStandardModule().getModuleAddress() == getLynxFromController((LynxController) controller).getModuleAddress()) {
//                return bulkDataExHub2.getDigitalInputState(digitalChannel);
//            }
//        }
//        return false;
//    }
//
//
//
//}
