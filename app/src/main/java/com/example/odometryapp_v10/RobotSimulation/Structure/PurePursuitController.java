package com.example.odometryapp_v10.RobotSimulation.Structure;

public class PurePursuitController {

	final double lookaheadRadiusInches = 10.0;

	Path path;
	int currentPoseIndex;

	public void setPath(Path path) {
		this.path = path;
		currentPoseIndex = 1;
	}

	public void clearPath() {
		this.path = null;
		currentPoseIndex = 1;
	}

	public boolean targetIsFinalPose() {
		return currentPoseIndex == (path.size() - 1);
	}

	public Pose getNextPose(Pose currentPose) {
		Pose targetPose;

		// assign values for previousTarget, current, and target pose
		Pose previousTargetPose = path.getPoint(currentPoseIndex - 1);
		Pose nextTargetPose = path.getPoint(currentPoseIndex);

		// if we are closer than lookahead distance, then check next coordinate
		if (currentPose.distanceToPose(nextTargetPose) < lookaheadRadiusInches) {
			if (currentPoseIndex < path.size() - 1) { // 1
				currentPoseIndex++;
				previousTargetPose = path.getPoint(currentPoseIndex - 1);
				nextTargetPose = path.getPoint(currentPoseIndex);
			} else {
				if (currentPose.distanceToPose(nextTargetPose) < 2) {
					return null;
				}
			}
		}

		// switch reference frame to current pose being the origin (relative to current
		// pose)
		previousTargetPose.subtract(currentPose);
		nextTargetPose.subtract(currentPose);

		Pose pose1, pose2;

		// lines and circles
		if (nextTargetPose.x == previousTargetPose.x) {
			double x = nextTargetPose.x;
			double forwardY = lookaheadRadiusInches;
			double backwardY = -lookaheadRadiusInches;

			pose1 = new Pose(x, forwardY, nextTargetPose.heading).add(currentPose);
			pose2 = new Pose(x, backwardY, nextTargetPose.heading).add(currentPose);
			previousTargetPose.add(currentPose);
			nextTargetPose.add(currentPose);
			targetPose = nextTargetPose.findCloserPose(pose1, pose2);
			targetPose.heading = getIntermediateAngle(previousTargetPose, targetPose, nextTargetPose);
		} else {
			double m = (nextTargetPose.y - previousTargetPose.y) / (nextTargetPose.x - previousTargetPose.x);
			double b = nextTargetPose.y - (m * nextTargetPose.x);

			double leftX = (Math.sqrt((-1 * Math.pow(b, 2)) + (Math.pow(m, 2) * Math.pow(lookaheadRadiusInches, 2))
					+ Math.pow(lookaheadRadiusInches, 2)) - (b * m)) / (Math.pow(m, 2) + 1);
			double rightX = -1
					* ((Math.sqrt((-1 * Math.pow(b, 2)) + (Math.pow(m, 2) * Math.pow(lookaheadRadiusInches, 2))
							+ Math.pow(lookaheadRadiusInches, 2)) - (b * m)) / (Math.pow(m, 2) + 1));
			double leftY = (m * leftX) + b;
			double rightY = (m * rightX) + b;

			// coordinates and motion
			pose1 = new Pose(leftX, leftY, nextTargetPose.heading).add(currentPose);
			pose2 = new Pose(rightX, rightY, nextTargetPose.heading).add(currentPose);
			previousTargetPose.add(currentPose);
			nextTargetPose.add(currentPose);
			targetPose = nextTargetPose.findCloserPose(pose1, pose2);
			targetPose.heading = getIntermediateAngle(previousTargetPose, targetPose, nextTargetPose);
		}
		return targetPose;
	}

	private double getHeadingForTargetCoordinate(Pose currentPose, Pose targetPose) {
		return Math.atan2(targetPose.x - currentPose.x, targetPose.y - currentPose.y);
	}

	private double getIntermediateAngle(Pose previousTargetPose, Pose intermediatePose, Pose currentTargetPose) {
		double totalDistance = previousTargetPose.distanceToPose(currentTargetPose) * 0.75;
		double partialDistance = previousTargetPose.distanceToPose(intermediatePose);
		double ratio = partialDistance / totalDistance;
		if (ratio > 1) {
			ratio = 1;
		}
		double totalDeltaAngle = currentTargetPose.heading - previousTargetPose.heading;
		double partialDeltaAngle = ratio * totalDeltaAngle;
		double intermediateTargetAngle = previousTargetPose.heading + partialDeltaAngle;
		return intermediateTargetAngle;

	}

}