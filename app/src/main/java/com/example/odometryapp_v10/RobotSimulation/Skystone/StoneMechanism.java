package com.example.odometryapp_v10.RobotSimulation.Skystone;//package internal.Skystone;
//
//import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.HardwareDevice;
//import com.qualcomm.robotcore.hardware.Servo;
//import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
//import org.firstinspires.ftc.teamcode.Robot.Structure.Accessory;
//import org.firstinspires.ftc.teamcode.Robot.Structure.FunctionReturn;
//
//import java.util.ArrayList;
//
//public class StoneMechanism extends internal.Structure.Accessory {
//
//    public int blockHeight = -1;
//
//    public enum MotorNames {
//        liftingMotor, collectionMotorLeft, collectionMotorRight
//    }
//
//    public enum ServoNames {
//        clawServo, rotationServo, flipServo, blockServo, floodGateLeft, floodGateRight, foundationServoLeft, foundationServoRight, capstoneDropper
//        //testServo,
//    }
//
//    private enum SensorNames {
//        verticalTOF
//    }
//
//    public enum VerticalModes {
//        moveUp, moveDown, stop
//    }
//
//    public enum FloodGateModes {
//        in, out
//    }
//
//    public enum CollectionModes {
//        in, out, stop
//    }
//
//    public enum ClawModes {
//        clawOpen, clawClosed, clawHalfway
//    }
//
//    public enum FlipModes {
//        in, out, quarter, threeFOURTH
//    }
//
//    public enum rotateModes {
//        IN, OUT
//    }
//
//    public enum BlockModes {
//        in, out
//    }
//
//    public enum FoundationModes {
//        grab, open
//    }
//
//    public enum CapstoneModes {
//        drop, in
//    }
//
//
//    public StoneMechanism(OpMode opMode) {
//
//        // Initialize OpMode Variables
//
//        this.opMode = opMode;
//        this.telemetry = opMode.telemetry;
//        this.hardwareMap = opMode.hardwareMap;
//
//        // Create empty ArrayLists for the DcMotors and Sensors
//
//        this.DcMotors = new ArrayList<DcMotor>();
//        this.Servos = new ArrayList<Servo>();
//        this.Sensors = new ArrayList<HardwareDevice>();
//
//        // Get motors from the hardwareMap using names defined in the Motors enum
//
//        for (MotorNames motorName : MotorNames.values()) {
//            DcMotor motor = this.hardwareMap.dcMotor.get(motorName.toString());
//            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//            this.DcMotors.add(motor);
//        }
//
//        this.Servos.add(this.hardwareMap.servo.get(ServoNames.clawServo.toString()));
//        this.Servos.add(this.hardwareMap.servo.get(ServoNames.flipServo.toString()));
//        this.Servos.add(this.hardwareMap.servo.get(ServoNames.rotationServo.toString()));
//        this.Servos.add(this.hardwareMap.servo.get(ServoNames.blockServo.toString()));
//        this.Servos.add(this.hardwareMap.servo.get(ServoNames.floodGateLeft.toString()));
//        this.Servos.add(this.hardwareMap.servo.get(ServoNames.floodGateRight.toString()));
//        this.Servos.add(this.hardwareMap.servo.get(ServoNames.foundationServoLeft.toString()));
//        this.Servos.add(this.hardwareMap.servo.get(ServoNames.foundationServoRight.toString()));
//        this.Servos.add(this.hardwareMap.servo.get(ServoNames.capstoneDropper.toString()));
//
//
//        //        this.Servos.add(this.hardwareMap.servo.getPoint(ServoNames.floodGateLeft.toString()));
////        this.Servos.add(this.hardwareMap.servo.getPoint(ServoNames.floodGateRight.toString()));
//        this.Sensors.add(this.hardwareMap.get(Rev2mDistanceSensor.class, SensorNames.verticalTOF.toString()));
//
//
//        addTelemetry("StoneMechanism", "Ready");
//    }
//
//    // Function to access DcMotor by name
//
//    public DcMotor getDcMotor(MotorNames motorName) {
//        return this.DcMotors.get(motorName.ordinal());
//    }
//
//    public Servo getServo(ServoNames servoName) {
//        return this.Servos.get(servoName.ordinal());
//    }
//
//    private void resetMotorEncoders(DcMotor motor) {
//        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//    }
//
//    private static final double singleBlockHeight = 5;
//    private static final double foundationHeightOffSet = 2;
//
//    int verticalStateConditions = 0;
//
//    public FunctionReturn moveVertical2(VerticalModes mode, double power, double blockHeight) {
//        double position = blockHeight * singleBlockHeight + foundationHeightOffSet;
//        power = Math.abs(power);
//        double currentDistance;
//        switch (mode) {
//            case moveUp:
//
//                currentDistance = getVerticalTOFDistance();
//                while (currentDistance < position) {
//                    getDcMotor(MotorNames.liftingMotor).setPower(power);
//                    currentDistance = getVerticalTOFDistance();
//                }
//                getDcMotor(MotorNames.liftingMotor).setPower(0.0);
//                return new FunctionReturn(true, null, null);
//
//            case moveDown:
//                currentDistance = getVerticalTOFDistance();
//                while (currentDistance > position) {
//                    getDcMotor(MotorNames.liftingMotor).setPower(-power);
//                    currentDistance = getVerticalTOFDistance();
//                }
//                getDcMotor(MotorNames.liftingMotor).setPower(0.0);
//                return new FunctionReturn(true, null, null);
//            case stop:
//                getDcMotor(MotorNames.liftingMotor).setPower(0.0);
//                return new FunctionReturn(true, null, null);
//        }
//        return new FunctionReturn(false, null, null);
//    }
//
//    public boolean moveVerticalPID(double power, int blockHeight) {
//        telemetry.setAutoClear(true);
//        telemetry.clearAll();
//        double position;
//        if(blockHeight >= 0) {
//            position = blockHeight * singleBlockHeight + foundationHeightOffSet;
//        } else {
//            position = 0;
//        }
//        power = Math.abs(power);
//
//        double currentDistance = getVerticalTOFDistance();
////        telemetry.addData("height", getVerticalTOFDistance());
////        telemetry.update();
//
////        if (position < currentDistance) {
////            //moveDown
////            if (currentDistance > position) {
////                getDcMotor(MotorNames.liftingMotor).setPower(-power);
////            } else {
////                getDcMotor(MotorNames.liftingMotor).setPower(0.0);
////                return true;
////            }
////        } else if (position > currentDistance) {
////            //moveUp
////            if (currentDistance < position) {
////                getDcMotor(MotorNames.liftingMotor).setPower(power);
////            } else {
////                getDcMotor(MotorNames.liftingMotor).setPower(0.0);
////                return true;
////            }
////        } else if(position == currentDistance){
////
////        }
//        double gain = (position - currentDistance)/5;
////        if(gain < 0){
////            power = -power + gain;
////        } else {
////            power += gain;
////        }
//
//        getDcMotor(MotorNames.liftingMotor).setPower(power * gain);
//
//        return false;
//    }
//
//    public void moveVerticalPIDClosedLoop(double power, double blockHeight){
//        double position;
//        if(blockHeight > 0) {
//            position = blockHeight * singleBlockHeight + foundationHeightOffSet;
//        } else if(blockHeight == -1){
//            position = 0.1;
//        } else {
//            position = 1;
//        }
//        telemetry.setAutoClear(true);
//        power = Math.abs(power);
//        double currentDistance = getVerticalTOFDistance();
//        while(currentDistance > position + 0.5 || currentDistance < position - 0.5 && ((LinearOpMode)this.opMode).opModeIsActive()) {
//            currentDistance = getVerticalTOFDistance();
//
//            double gain = (position - currentDistance) / 1.75;
//
//            getDcMotor(MotorNames.liftingMotor).setPower(power * gain);
////            telemetry.addData("height", getVerticalTOFDistance());
////            telemetry.update();
//        }
//        getDcMotor(MotorNames.liftingMotor).setPower(0.1);
//    }
//
//
//    public void incrementToNextStep(boolean up) {
//        double currentBlockHeight = (getVerticalTOFDistance() - foundationHeightOffSet) / singleBlockHeight;
//        if (up) {
//            blockHeight = (int) Math.floor(currentBlockHeight) + 1;
//        } else {
//            blockHeight = (int) Math.floor(currentBlockHeight);
//        }
//    }
//
//    public void moveVertical(VerticalModes mode, double power) {
//        switch (mode) {
//            case moveUp:
//                getDcMotor(MotorNames.liftingMotor).setPower(Math.abs(power));
//                break;
//            case moveDown:
//                getDcMotor(MotorNames.liftingMotor).setPower(-1 * Math.abs(power));
//                break;
//            case stop:
//                getDcMotor(MotorNames.liftingMotor).setPower(0);
//                break;
//        }
//    }
//
////    public FunctionReturn moveHorizontal(HorizontalModes mode, double power) {
////        switch (mode) {
////            case moveOut:
////                getDcMotor(MotorNames.horizontalMotor).setPower(Math.abs(power));
////                return new FunctionReturn(true, null, null);
////            case moveIn:
////                getDcMotor(MotorNames.horizontalMotor).setPower(-Math.abs(power));
////                return new FunctionReturn(true, null, null);
////            case stop:
////                getDcMotor(MotorNames.horizontalMotor).setPower(0.0);
////                return new FunctionReturn(true, null, null);
////        }
////        return new FunctionReturn(false, null, null);
////    }
//
//    public FunctionReturn moveClaw(ClawModes mode) {
//        switch (mode) {
//            case clawOpen:
//                getServo(ServoNames.clawServo).setPosition(0.5);
//                return new FunctionReturn(true, null, null);
//            case clawClosed:
//                getServo(ServoNames.clawServo).setPosition(1.0);
//                return new FunctionReturn(true, null, null);
//            case clawHalfway:
//                getServo(ServoNames.clawServo).setPosition(0.8);
//                return new FunctionReturn(true, null, null);
//        }
//        return new FunctionReturn(false, null, null);
//    }
//
//    public FunctionReturn collectBlocks(CollectionModes collectionMode, double collectionSpeed) {
//        switch (collectionMode) {
//            case out:
//                getDcMotor(MotorNames.collectionMotorLeft).setPower(collectionSpeed);
//                getDcMotor(MotorNames.collectionMotorRight).setPower(-collectionSpeed);
//                return new FunctionReturn(true, null, null);
//            case in:
//                getDcMotor(MotorNames.collectionMotorLeft).setPower(-collectionSpeed);
//                getDcMotor(MotorNames.collectionMotorRight).setPower(collectionSpeed);
//                return new FunctionReturn(true, null, null);
//            case stop:
//                getDcMotor(MotorNames.collectionMotorLeft).setPower(0);
//                getDcMotor(MotorNames.collectionMotorRight).setPower(0);
//                return new FunctionReturn(true, null, null);
//        }
//        return new FunctionReturn(false, null, null);
//    }
//
//
//    public FunctionReturn flipServo(FlipModes blockModes, double position) {
////        switch (blockModes) {
////            case in:
////                getServo(ServoNames.flipServo).setPosition(1.0);
////                return new FunctionReturn(true, null, null);
////            case out:
////                getServo(ServoNames.flipServo).setPosition(0.01);
////                return new FunctionReturn(true, null, null);
////            case quarter:
////                getServo(ServoNames.flipServo).setPosition(0.5);
////                return new FunctionReturn(true, null, null);
////            case threeFOURTH:
////                getServo(ServoNames.flipServo).setPosition(0.5);
////                return new FunctionReturn(true, null, null);
////        }
//        getServo(ServoNames.flipServo).setPosition(position);
////        telemetry.addData("position flip", position);*/
//        return new FunctionReturn(false, null, null);
//    }
//
//    public FunctionReturn rotateBlockServo(rotateModes flipModes, double position) {
////        switch (flipModes) {
////            case IN:
////                getServo(ServoNames.rotationServo).setPosition(1.0);
////                return new FunctionReturn(true, null, null);
////            case OUT:
////                getServo(ServoNames.rotationServo).setPosition(0.01);
////                return new FunctionReturn(true, null, null);
////        }
//        getServo(ServoNames.rotationServo).setPosition(position);
////        telemetry.addData("position rotation", position);
//        return new FunctionReturn(false, null, null);
//    }
//
//    public FunctionReturn moveFloodGates(FloodGateModes modes){
//        switch (modes){
//            case out:
//                getServo(ServoNames.floodGateLeft).setPosition(0.0);
//                getServo(ServoNames.floodGateRight).setPosition(1.0);
//                return new FunctionReturn(true, null, null);
//            case in:
//                getServo(ServoNames.floodGateLeft).setPosition(1.0);
//                getServo(ServoNames.floodGateRight).setPosition(0.0);
//                return new FunctionReturn(true, null, null);
//        }
//        return new FunctionReturn(false, null, null);
//    }
//
//    public FunctionReturn blockServo(BlockModes blockModes) {
//        switch (blockModes) {
//            case in:
//                getServo(ServoNames.blockServo).setPosition(0.91);
//                return new FunctionReturn(true, null, null);
//            case out:
//                getServo(ServoNames.blockServo).setPosition(0.28);
//                return new FunctionReturn(true, null, null);
//        }
//        return new FunctionReturn(false, null, null);
//    }
//
//    public FunctionReturn capstoneServo(CapstoneModes capstoneModes){
//        switch (capstoneModes){
//            case in:
//                getServo(ServoNames.capstoneDropper).setPosition(0.0);
//                return new FunctionReturn(true, null, null);
//            case drop:
//                getServo(ServoNames.capstoneDropper).setPosition(1.0);
//                return new FunctionReturn(true, null, null);
//        }
//        return new FunctionReturn(false, null, null);
//    }
//
//    public void blockStepperServo(double position){
//        getServo(ServoNames.blockServo).setPosition(position);
//    }
//
//    // Flip and rotate
//    public FunctionReturn foundationServo(FoundationModes foundationModes) {
//        switch (foundationModes) {
//            case grab:
//                moveFoundationServoRight(0.45);
//                moveFoundationServoLeft(0.6);
//                return new FunctionReturn(true, null, null);
//            case open:
//                moveFoundationServoRight(0.9);
//                moveFoundationServoLeft(0.1);
//                return new FunctionReturn(true, null, null);
//        }
//        return new FunctionReturn(false, null, null);
//    }
//
//
//    public enum FlipStates {
//        IN, OUT
//    }
//
//    int stateConditions = 0;
//    long currentTime = 0;
//    FlipStates currentFlipState = FlipStates.IN;
//
//    public boolean flipServoComplete(FlipStates targetFlipState) {
//        telemetry.setAutoClear(false);
//        if (currentFlipState != targetFlipState) {
//            switch (stateConditions) {
//                case 0:
//                    currentTime = System.currentTimeMillis();
//                    stateConditions++;
//                case 1:
//                    if (targetFlipState == FlipStates.IN) {
//                        getServo(ServoNames.rotationServo).setPosition(0.999);
//                        getServo(ServoNames.flipServo).setPosition(0.5);
//                        stateConditions++;
////                        telemetry.addData("IN: ", "1.0, 0.3");
//                    } else if (targetFlipState == FlipStates.OUT) {
//                        getServo(ServoNames.flipServo).setPosition(0.5);
//                        stateConditions++;
////                        telemetry.addData("OUT: ", "0.3 - flip");
//                    }
////                    telemetry.update();
//                    break;
//                case 2:
//                    if (targetFlipState == FlipStates.IN) {
//                        if (System.currentTimeMillis() < currentTime + 750) {
//                            // waiting
//                        } else {
//                            getServo(ServoNames.flipServo).setPosition(0.01);
//                            getServo(ServoNames.clawServo).setPosition(0.5);
//                            stateConditions = 0;
//                            currentFlipState = targetFlipState;
////                            telemetry.addData("IN: ", "");
//
//                            return true;
//                        }
//                    } else if (targetFlipState == FlipStates.OUT) {
//                        if (System.currentTimeMillis() < currentTime + 1000) {
//                            // waiting
//                        } else {
//                            getServo(ServoNames.rotationServo).setPosition(0.1);
//                            if (System.currentTimeMillis() < currentTime + 1400) {
//
//                            } else {
//                                getServo(ServoNames.flipServo).setPosition(0.999);
//                                stateConditions = 0;
//                                currentFlipState = targetFlipState;
//                                return true;
//                            }
//                        }
//                    }
////                    telemetry.update();
//                    break;
//            }
//        }
//        return false;
//    }
//
//    public void flipServoCompleteAuto(FlipStates targetFlipState){
////        if (currentFlipState != targetFlipState) {
//            if(targetFlipState == FlipStates.IN){
//                getServo(ServoNames.rotationServo).setPosition(0.999);
//                getServo(ServoNames.flipServo).setPosition(0.5);
//                pause(750);
//                getServo(ServoNames.flipServo).setPosition(0.01);
//                getServo(ServoNames.clawServo).setPosition(0.5);
//            } else if(targetFlipState == FlipStates.OUT) {
//                getServo(ServoNames.flipServo).setPosition(0.5);
//                pause(1000);
//                getServo(ServoNames.rotationServo).setPosition(0.1);
//                pause(400);
//                getServo(ServoNames.flipServo).setPosition(0.999);
//            }
////        }
//    }
//
//    public void dropBlock(int blockHeight){
//        moveVerticalPIDClosedLoop(0.65, blockHeight);
//        flipServoCompleteAuto(StoneMechanism.FlipStates.OUT);
//        pause(750);
//        moveClaw(StoneMechanism.ClawModes.clawOpen);
//        moveVerticalPIDClosedLoop(0.65, blockHeight+1);
//        flipServoCompleteAuto(StoneMechanism.FlipStates.IN);
//        moveVerticalPIDClosedLoop(0.4, -1);
//    }
//
//    public void incrementFlipServo(double position) {
//        getServo(ServoNames.flipServo).setPosition(position);
//    }
//
//    public void incrementRotateServo(double position) {
//        getServo(ServoNames.rotationServo).setPosition(position);
//    }
//
//    public void incrementGrabServo(double position) {
//        getServo(ServoNames.clawServo).setPosition(position);
//    }
//
//    public void stop() {
//        getDcMotor(MotorNames.collectionMotorLeft).setPower(0);
//        getDcMotor(MotorNames.collectionMotorRight).setPower(0);
//        getDcMotor(MotorNames.liftingMotor).setPower(0);
//    }
//
//    public double getVerticalTOFDistance() {
//        return ((Rev2mDistanceSensor) this.Sensors.get(SensorNames.verticalTOF.ordinal())).getDistance(DistanceUnit.INCH);
//    }
//
//    public void pause(double milliseconds) {
//        long startTime = System.currentTimeMillis();
//        while ((System.currentTimeMillis() < startTime + milliseconds) && ((LinearOpMode)this.opMode).opModeIsActive()) {
//            // waiting
//        }
//    }
//
//    public void moveFoundationServoLeft(double position){
//        getServo(ServoNames.foundationServoLeft).setPosition(position);
//    }
//
//    public void moveFoundationServoRight(double position){
//        getServo(ServoNames.foundationServoRight).setPosition(position);
//    }
//
////    public FunctionReturn moveBlockServo(FlipModes mode) {
////        switch (mode) {
////            case out:
////                getServo(ServoNames.blockServo).setPosition(1.0);
////                return new FunctionReturn(true, null, null);
////            case in:
////                getServo(ServoNames.blockServo).setPosition(0.0);
////                return new FunctionReturn(true, null, null);
////        }
////        return new FunctionReturn(false, null, null);
////    }
//
////    public void incrementServo(double position){
////        getServo(ServoNames.testServo).setPosition(position);
////    }
//
//}
