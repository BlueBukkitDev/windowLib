package windowAPI.ui.geometry;

import java.awt.Frame;

/**
 *A Simple Point for denoting a position in a lightweight fashion. Comes with helper classes to determine 
 *distances and directions, and perform translations. 
 **/
public class SP {
	private int x;

	private int y;
	
	public static SP Q0(Frame frame) {
		return new SP(0, 0);
	}
	public static SP Q1(Frame frame) {
		return new SP(frame.getWidth(), 0);
	}
	public static SP Q2(Frame frame) {
		return new SP(frame.getWidth(), frame.getHeight());
	}
	public static SP Q3(Frame frame) {
		return new SP(0, frame.getHeight());
	}

	public SP(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return this.x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 *Calculates the distance from this point to the target in pixel units. Considers the direction 
	 *between the two during calculation, providing a normalized result. 
	 **/
	public double distanceTo(SP target) {
		double a = x-target.getX();
		double b = y-target.getY();
		return Math.sqrt((a*a) + (b*b));
	}
	
	/**
	 *Takes the starting position of this SP, and calculates a new SP of given distance in given 
	 *direction. 
	 *
	 *@param dir - The direction to translate the SP
	 *@param distance - The distance to translate the SP
	 *
	 *@return A new SP at the calculated position
	 **/
	public SP translate(Direction dir, double distance) {
	    double radians = Math.toRadians(dir.getAngle());
	    double deltaX = Math.cos(radians) * distance;
	    double deltaY = Math.sin(radians) * distance;

	    double newX = this.x - deltaX;
	    double newY = this.y + deltaY;  // Adjusted to add deltaY for the y-coordinate

	    return new SP((int) newX, (int) newY);
	}
	
	/**
	 *Takes the starting position of this SP, and calculates a new SP of given distance in given 
	 *direction. 
	 *
	 *@param dir - The direction to translate the SP
	 *@param distance - The distance to translate the SP
	 *
	 *@return A new SP at the calculated position
	 **/
	public SP translate(double dir, double distance) {
	    double radians = Math.toRadians(dir);
	    double deltaX = Math.cos(radians) * distance;
	    double deltaY = Math.sin(radians) * distance;

	    double newX = this.x - deltaX;
	    double newY = this.y + deltaY;  // Adjusted to add deltaY for the y-coordinate

	    return new SP((int) newX, (int) newY);
	}
	
	/**
	 *Calculates the direction from this to target. 
	 *
	 *@param target - The target SP
	 *
	 *@return The float direction
	 **/
	public Direction directionTo(SP target) {//cos(theta) = adj/hyp, so //acos(adj/hyp) = theta
	    if (target.x > x) {//right
	        if (target.y < y) {//top
	            return new Direction(Math.toDegrees(Math.acos((y-target.y)/distanceTo(target))));//top right
	        }//bottom
	        return new Direction(90.0+Math.toDegrees(Math.acos((target.x-x)/distanceTo(target))));//bottom right
	    }//left
	    if (target.y > y) {//bottom
	        return new Direction(180.0+Math.toDegrees(Math.acos((target.y-y)/distanceTo(target))));//bottom left
	    }//top
	    return new Direction(270.0+Math.toDegrees(Math.acos((x-target.x)/distanceTo(target))));//top left
	}
}
