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
			//turnRadarLeft(45);
			if (targetSighted && abs(bearingGunWrtTarget) < gunBearingThreshold) {		
				double firePower = 3 - targetDistance/maxDistance * 2.9;
				setFire(firePower);
			}
			checkCollision(5);
			double headingError = normalizeAngleErrorDegrees(targetHeading - getHeading());

			double turnRate = 10 * Math.abs(headingError) / 180;
			targetSpeed = 8 - 8 * Math.abs(headingError) / 180;

			if(headingError > 0) {
				setTurnRight(turnRate);
			} else {
				setTurnLeft(turnRate);
			} 
			
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
		stop();
		targetHeading = normalizeAngleDegrees(getHeading() - 180);
		collisionHeading = getHeading();
		direction *= -1;
		// setTurnRight(90);
		//turnGunLeft(90);
	}
	
	public void checkCollision(int timeSteps) {
		double speed = getVelocity(); //pixels per turn
		double heading = getHeadingRadians(); //radians
		double xSpeed = speed * Math.cos(heading);
		double ySpeed = speed * Math.sin(heading);
		
		double headingCorrection = 0;		
		double newSpeed = 5;
		double turnSpeed = 5;

		double predictedPositionX = xSpeed * timeSteps + getX();
		double predictedPositionY = ySpeed * timeSteps + getY();

		double fieldHeight = getBattleFieldHeight();
		double fieldWidth = getBattleFieldWidth();

		double xCorrection = 0;		
		if (predictedPositionX >= fieldWidth) {
			xCorrection = 1; 
		} else if (predictedPositionX <= 0) {
			xCorrection = -1;
		}
		
		double yCorrection = 0;	
		if (predictedPositionY >= fieldHeight) {
			yCorrection = 1; 
		} else if (predictedPositionY <= 0) {
			yCorrection = -1;
		}
		
		if(yCorrection != 0 || xCorrection != 0) {
			targetHeading = getHeading() + xCorrection * yCorrection * 90;
			targetHeading = normalizeAngleDegrees(targetHeading);
			direction *= -1;
		}
		
	}	
	
	public double normalizeAngleDegrees(double angle) {
		return (angle + 360) % 360;
	}
	
	public double normalizeAngleErrorDegrees(double angle) {
		return (angle + 360) % 180;
	}
}

