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
	/**
	 * run: JosephRobot1's default behavior
	 */
	public void run() {
		// Initialization of the robot should be put here
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setTurnRadarRight(Double.POSITIVE_INFINITY);
		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		// setColors(Color.red,Color.blue,Color.green); // body,gun,radar

		// Robot main loop
		while(true) {
			// Replace the next 4 lines with any behavior you would like
			ahead(50);
			//turnRadarLeft(45);
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

		double bearingGunWrtTarget = bearingTargetWrtGlobal - bearingGunWrtGlobal;

		bearingGunWrtTarget = ((bearingGunWrtTarget + 180) % 360) - 180;		

		double turretRotation = abs(bearingGunWrtTarget);
		if(bearingGunWrtTarget > 0) {
			setTurnGunRight(turretRotation);
		} else {
			setTurnGunLeft(turretRotation);
		}

		fire(3);
//		ahead(50);
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
		back(20);
		setTurnRight(90);
		//turnGunLeft(90);
	}	
}
