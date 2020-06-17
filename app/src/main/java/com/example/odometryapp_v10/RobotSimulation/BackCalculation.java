package com.example.odometryapp_v10.RobotSimulation;

public class BackCalculation {
	static double angle;
	static double movementPower;
	static double rotationPower;

	boolean negate;
	static Orientation orientation_left = Orientation.FORWARD;
	static Orientation orientation_center = Orientation.SIDEWAYS;
	static Orientation orientation_right = Orientation.FORWARD;

	public enum Orientation {
		FORWARD, SIDEWAYS
	}

	public static final double d = 14.5625;
	public static long timeStampCenter = 0;
	public static long timeStampRight = 0;
	public static long timeStampLeft = 0;
	static double RPM = 340;
	static double mecanum_radius = 2;
	static double encoder_radius = 38 / 25.4 / 2;
	static double distance_mecanumToCenter = 17.25 / 2;
	static double circumferenceOfRobot = distance_mecanumToCenter * 2 * Math.PI;

	public static final double encoderWheelDiameterInches = 38 / 25.4;
	public static final double ticksPerRev = 1000;
	public static double circumferenceOfEncoderWheel = Math.PI * encoderWheelDiameterInches;
	public static double circumferenceOfMecanumWheel = Math.PI * mecanum_radius * 2;
	public static double inchesPerTick = circumferenceOfEncoderWheel / ticksPerRev;

	static double currentTicks = 0;

	public static double fL;
	public static double fR;
	public static double bL;
	public static double bR;

	private static double deltaTicks_components(Orientation orientation, long ts) {
		double deltaTime = System.currentTimeMillis() - ts;
		if (orientation == Orientation.FORWARD) {
			double verticalComponentVelocity = verticalVector_overall_POWER * RPM * (circumferenceOfMecanumWheel)
					/ 60000;
			double verticalDistance = verticalComponentVelocity * (deltaTime);
			return (verticalDistance / (circumferenceOfEncoderWheel)) * ticksPerRev; // verticalTicks
		} else {
			double horizontalComponentVelocity = horizontalVector_overall_POWER * RPM * (circumferenceOfMecanumWheel)
					/ 60000;
			double horizontalDistance = horizontalComponentVelocity * (deltaTime);
			return (horizontalDistance / (circumferenceOfEncoderWheel)) * ticksPerRev; // horizontalTicks
		}

	}

	private static double deltaTicks_rotationalComponent(long ts) {
		double deltaTime = System.currentTimeMillis() - ts;
		double rotVelocity = rotationPower * RPM / 60000;
		double angularVelocity = rotVelocity / distance_mecanumToCenter;
		double deltaArcLengthOfEncoder = angularVelocity * (d / 2) * deltaTime;
		double rotationDistance = deltaArcLengthOfEncoder * (1 / circumferenceOfEncoderWheel) * (ticksPerRev);
		return rotationDistance;
	}

	public static double deltaTick_LeftEncoder() {
		backCalculate(fL, fR, bL, bR);
		double totalDeltaInches = (deltaTicks_components(orientation_left, timeStampLeft)
				+ (negate(deltaTicks_rotationalComponent(timeStampLeft)))) * inchesPerTick;
		timeStampLeft = System.currentTimeMillis();
		return totalDeltaInches;
	}

	public static double deltaTick_CenterEncoder() {
		backCalculate(BackCalculation.fL, BackCalculation.fR, BackCalculation.bL, BackCalculation.bR);
		double totalDeltaInches = (deltaTicks_components(orientation_center, timeStampCenter)) * inchesPerTick;
		timeStampCenter = System.currentTimeMillis();
		return totalDeltaInches;
	}

	public static double deltaTick_RightEncoder() {
		backCalculate(BackCalculation.fL, BackCalculation.fR, BackCalculation.bL, BackCalculation.bR);
		double totalDeltaInches = (deltaTicks_components(orientation_right, timeStampRight)
				+ deltaTicks_rotationalComponent(timeStampRight)) * inchesPerTick;
		timeStampRight = System.currentTimeMillis();
		return totalDeltaInches;
	}

	static double horizontalVector_overall_POWER, verticalVector_overall_POWER;

	public static void backCalculate(double fL, double fR, double bL, double bR) {
		horizontalVector_overall_POWER = -fR + fL - bL + bR;
		verticalVector_overall_POWER = fR + fL + bR + bL;
		int a = 7;
		int b = 7;

		int[][] firstMatrix = { { (int) (fR * 10) }, { (int) (fL * 10) }, { (int) (bL * 10) }, { (int) (bR * 10) } };
		int[][] secondMatrix = { { (int) mecanum_radius } };
		int[][] product = BackCalculation.multiplyMatrices(firstMatrix, secondMatrix, 4, 1, 1);

		int[][] first2Matrix = { { -1, 1, -1, 1 }, { 1, 1, 1, 1 }, { a + b, -(a + b), -(a + b), a + b } };
		int[][] secondProduct = BackCalculation.multiplyMatrices(first2Matrix, product, 3, 4, 1);
		double rotPower = (secondProduct[2][0]) / mecanum_radius;

		rotationPower = rotPower / 10;
	}

	public double encodersToInches(double encoderValue) {
		return inchesPerTick * encoderValue;
	}

	public static void setFrontLeftPower(double fLPower) {
		fL = fLPower;
	}

	public static void setBackLeftPower(double bLPower) {
		bL = bLPower;
	}

	public static void setFrontRightPower(double fRPower) {
		fR = fRPower;
	}

	public static void setBackRightPower(double bRPower) {
		bR = bRPower;
	}

	public static void setLeftDrivetrainPower(double power) {
		bL = power;
		fL = power;
	}

	public static void setRightDrivetrainPower(double power) {
		bR = power;
		fR = power;
	}

	public static void setDrivetrainPower(double power) {
		bL = power;
		fL = power;
		bR = power;
		fR = power;
	}

	public static double negate(double value) {
		return (value * -1);
	}

	public static int[][] multiplyMatrices(int[][] firstMatrix, int[][] secondMatrix, int r1, int c1, int c2) {
		int[][] product = new int[r1][c2];
		for (int i = 0; i < r1; i++) {
			for (int j = 0; j < c2; j++) {
				for (int k = 0; k < c1; k++) {
					product[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
				}
			}
		}
		return product;
	}

	public static void displayProduct(int[][] product) {
		System.out.println("Product of two matrices is: ");
		for (int[] row : product) {
			for (int column : row) {
				System.out.print(column + "    ");
			}
			System.out.println();
		}
	}
}
