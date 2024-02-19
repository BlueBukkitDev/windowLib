package windowAPI.ui.geometry;

import java.util.Random;

/**
 *A geometry class to enable simplified manipulation of vectors in a 2D space. This one 
 *specifically deals with orientation, as denoted by the name. <br><br>
 *Direction is defined by a single value measured in degrees of a circle; if the degrees were 
 *to exceed 360, a simple modulus will bring them back to 0. To subtract a direction is to 
 *rotate it CCW, and to add to it is to rotate it CW. This can be used in conjunction with 
 *the SP and Transform classes to determine new directions based on existing points, or to 
 *determine new points based on directions and distances. 
 **/
public class Direction {
	
	private double angle;
	
	public Direction(double angle) {
		this.angle = angle;
	}
	
	/**
	 *Creates a new random direction between 0 and 360.
	 **/
	public static Direction newRandom() {
		return new Direction(new Random().nextDouble(360));
	}
	
	public double getAngle() {
		return angle;
	}
	
	/**
	*Mirrors the current angle around the specified Axis. <br>
	*Assuming angle = 15.0, if we reflect(Axis.VERTICAL), we get angle = 165.0.
	*However, if we reflect(Axis.HORIZONTAL), we get angle = 345.0.<br>
	*Future development will allow for an Axis to be set to a direction float instead; reflection 
	*will then occur about that axis, which will allow for reflections off tangential lines such 
	*as might be calculated against a curve. 
	**/
	public void reflect(Axis axis) {
		if(axis == Axis.VERTICAL) {
			if(angle < 180.0) {
				angle = 180.0-angle;
			}else if(angle > 180.0) {
				angle = 540.0-angle;
			}
		}else if(axis == Axis.HORIZONTAL) {
			angle = 360-angle;
		}
	}
	
	/** 
    Returns the absolute value of the real difference between two directions. 
   * If `angle1` is 300.0 and `angle2` is 15.0, the difference is 75.0. Because of this wrapping, difference returned cannot exceed 180.0. 
   */
	public static double difference(Direction angle1, Direction angle2) {
		return Math.abs(angle1.angle - angle2.angle)%180.0;
	}
	
	/**
    Rolls the `Direction` CCW by "amt" degrees. 
   */
	public void subtract(float amt) {
		angle = angle-amt % 360.0f;
		if(angle < 0) {
			angle += 360;
		}
	}
	
	/**
    Rolls the `Direction` CW by "amt" degrees
   */
	public void add(double amt) {
		angle = angle+amt % 360;
	}
	
	/**
	 *Returns whether this direction is closer to dir in a clockwise direction than a counter-clockwise 
	 *direction. If this.clockwiseOf(dir) and dir.clockwiseOf(this) both return false, then they are 
	 *exactly opposite each other. 
	 *
	 *@param dir - the direction to check against
	 *
	 *@return whether this is clockwise of dir
	 **/
	public boolean clockwiseOf(Direction dir) {
		if (angle > 180.0) {//self is left
            if (dir.angle > 180.0) {//dir is left
                if (angle < dir.angle) {//self is lower/ccw
                    return false;
                }else if (angle > dir.angle) {//self is higher/cw
                    return true;
                }
            }else if (dir.angle < 180.0) {//dir is right
                if (angle-dir.angle < 180.0) {//angle open to the bottom
                    return true;
                }else if (angle-dir.angle > 180.0) {//angle open to the top
                    return false;
                }
            }
        }else if (angle < 180.0) {//self is right
            if (dir.angle < 180.0) {//dir is right
                if (angle < dir.angle) {//self is higher/ccw
                    return false;
                }else if (angle > dir.angle) {//self is lower/cw
                    return true;
                }
            }else if (dir.angle > 180.0) {//dir is left
                if (dir.angle-angle < 180.0) {//angle open to the bottom
                    return false;
                }else if (dir.angle-angle > 180.0) {//angle open to the top
                    return true;
                }
            }
        }
        return false;
	}
}
