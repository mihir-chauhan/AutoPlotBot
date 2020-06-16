package com.example.odometryapp_v10.RobotSimulation.Structure;

public class Pose {
	public double x;
	public double y;
	public double heading;
	public String name;

	public Pose(double x, double y, double thetaRADIANS) {
		this.x = x;
		this.y = y;
		this.heading = thetaRADIANS % (2 * Math.PI);
	}

	public void setName(String name) {
		this.name = name;
	}

	public Coordinate toCoordinate() {
		return new Coordinate(this.x, this.y);
	}

	public Pose add(double deltaX, double deltaY) {
		this.x += deltaX;
		this.y += deltaY;
		return this;
	}

	public Pose add(Pose pose) {
		this.x += pose.x;
		this.y += pose.y;
		return this;
	}

	public Pose add(Pose pose, boolean addHeading) {
		this.x += pose.x;
		this.y += pose.y;
		if (addHeading) {
			this.heading += pose.heading;
		}
		return this;
	}

	public Pose subtract(double deltaX, double deltaY) {
		this.x -= deltaX;
		this.y -= deltaY;
		return this;
	}

	public Pose subtract(Pose pose) {
		this.x -= pose.x;
		this.y -= pose.y;
		return this;
	}

	public Pose subtract(Pose pose, boolean subtractHeading) {
		this.x -= pose.x;
		this.y -= pose.y;
		if (subtractHeading) {
			this.heading -= pose.heading;
		}
		return this;
	}

	public double distanceToPose(Pose pose) {
		return Math.hypot((pose.x - this.x), (pose.y - this.y));
	}

	public Pose findCloserPose(Pose pose1, Pose pose2) {
		if (distanceToPose(pose1) < distanceToPose(pose2)) {
			return pose1;
		} else {
			return pose2;
		}
	}
}