package joerobot1;
import robocode.*;
import static java.lang.Math.*;

//import java.awt.Color;

// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

/**
 * JosephRobot1 - a robot by (your name here)
 */
public class JosephRobot1 extends AdvancedRobot
{

	double bearingTargetWrtGlobal = 0;
	double targetDistance = 0;
	double maxDistance = 1;
	
	double bearingGunWrtTarget;
	boolean targetSighted = false;
	double gunBearingThreshold = 5.0;

	double targetHeading = 0;
	double targetSpeed = 0;

	boolean collisionOccured = true;
	double collisionHeading = 0;
	
	double direction = 1;
	double targetDirection = 1;
	 
	/**
	 * run: JosephRobot1's default behavior
	 */
	public void run() {
		// Initialization of the robot should be put here
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setTurnRadarRight(Double.POSITIVE_INFINITY);

		double fieldHeight = getBattleFieldHeight();
		double fieldWidth = getBattleFieldWidth();
		maxDistance = sqrt(fieldHeight * fieldHeight  + fieldWidth * fieldWidth);
		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		targetHeading = 0;
		// setColors(Color.red,Color.blue,Color.green); // body,gun,radar

//		setAhead(5);
		// Robot main loop
		while(true) {
			// Replace the next 4 lines with any behavior you would like
			//ahead(50);

			setTurnRadarLeft(45);
			if (targetSighted && abs(bearingGunWrtTarget) < gunBearingThreshold) {		
				double firePower = 3 - targetDistance/maxDistance * 2.9;
				setFire(firePower);
			}
			//checkCollision(5);
			//double headingError = normalizeAngleErrorDegrees(targetHeading - getHeading());

			//double turnRate = 10 * Math.abs(headingError) / 180;
			//targetSpeed = 8 - 8 * Math.abs(headingError) / 180;


			checkCollision();
			
			if(direction > 0) {
				ahead(targetSpeed);
			} else {
				back(targetSpeed);
			}
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Replace the next line with any behavior you would like
		// turnLeft(90);
		// turnGunRight(90);
		double bearingTargetWrtBody = e.getBearing();
		double bearingGunWrtGlobal = getGunHeading();
		double heading = getHeading();
		
		bearingTargetWrtGlobal = bearingTargetWrtBody + heading;

		bearingGunWrtTarget = bearingTargetWrtGlobal - bearingGunWrtGlobal;

		bearingGunWrtTarget = ((bearingGunWrtTarget + 180) % 360) - 180;		

		double turretRotation = abs(bearingGunWrtTarget);
		if(bearingGunWrtTarget > 0) {
			setTurnGunRight(turretRotation);
		} else {
			setTurnGunLeft(turretRotation);
		}

		targetDistance = e.getDistance();
		targetSighted = true;

	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		//back(30);
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
	//	stop();
	//	targetHeading = normalizeAngleDegrees(getHeading() - 180);
	//	collisionHeading = getHeading();
	//	direction *= -1;
		// setTurnRight(90);
		//turnGunLeft(90);
	}
	
	public void checkCollision() {
	
		double x0 = getX();
		double y0 = getY();
		
		double theta = getHeadingRadians() * direction;
		double phi = Math.PI/2 - theta;

		boolean horizontal = cos(phi) == 0;
		boolean vertical = sin(phi) == 0;

		double m = 0;
		if(sin(phi) != 0) {
			m = Math.tan(phi);
		}

		double b = y0 - m*x0;		

		double W = getBattleFieldWidth();
		double H = getBattleFieldHeight();

		double R = Math.sqrt(W*W + H*H) / 2;
		
		double x2 = W/2 + R*sin(theta);
		double x1 = maxMinLimit(x2, W, 0);
		
		double y2 = H/2 + R*cos(theta);

		if(!horizontal && !vertical) {
			y2 = m*x2 + b;			
		}
		double y1 = maxMinLimit(y2, H, 0);
		
		if(!horizontal && !vertical) {
			x1 = (y1 - b) / m;			
		}
		
		double distance = Math.sqrt( Math.pow(x1-x0, 2) + Math.pow(y1-y0, 2));
		
		targetSpeed = 8*(Math.tanh(distance + 8) + 1);
		

		if(distance <= 4) {
			direction *= -1;
			targetSpeed = 8;
		}
	}
	
	public double maxMinLimit(double a, double max, double min) {
		return Math.min(Math.max(a, min), max);
	}	
	
	public double normalizeAngleDegrees(double angle) {
		return (angle + 360) % 360;
	}
	
	public double normalizeAngleErrorDegrees(double angle) {
		return (angle + 360) % 180;
	}
}

