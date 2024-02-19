package windowAPI.ui.geometry;

/**
 *For now, a class to hold a location only, so that relative coordinates can be used. Later, this 
 *will also use the direction and rotation features found in the Geometry crate (rust) by BlueBukkitDev.
 **/
public class Transform {
	
	private SP origin;
	private Anchor anchor;
	private Direction dir;
	
	/**
	 *Provides a parent location origin for any Shape. <br>
	 *A Transform has an origin point of its own, dictated by passed SP and optionally an anchor. <br>
	 *If an anchor is supplied, then that will dictate the geometric functions applied later. For 
	 *instance, if an anchor of TOP_LEFT is used, then all math will be as expected, where an increase 
	 *of X or Y will move the transform right or down, respectively. If an anchor of BOTTOM_RIGHT is 
	 *used, then an increase of X or Y will move the transform left or up, respectively.  
	 *
	 *@param origin - the SimplePoint location of the transform's origin
	 **/
	public Transform(int x, int y) {
		this.origin = new SP(x, y);
		this.anchor = Anchor.TOP_LEFT;
		this.dir = new Direction(0);
	}
	
	/**
	 * @param origin
	 * @param anchor
	 */
	public Transform(int x, int y, Anchor anchor) {
		this.origin = new SP(x, y);
		this.anchor = anchor;
	}
	
	/**
	 * @param origin
	 * @param anchor
	 * @param dir
	 */
	public Transform(int x, int y, Anchor anchor, Direction dir) {
		this.origin = new SP(x, y);
		this.anchor = anchor;
		this.dir = dir;
	}
	
	public Transform clone() {
		return new Transform(origin.getX(), origin.getY());
	}
	
	/**
	 * @return
	 */
	public Anchor getAnchor() {
		return anchor;
	}
	
	/**
	 * @param anchor
	 */
	public void setAnchor(Anchor anchor) {
		this.anchor = anchor;
	}
	
	public SP getPosition() {
		return origin;
	}
	
	/**
	 * @return
	 */
	public Direction getDirection() {
		return dir;
	}
	
	/**
	 * @param dir
	 */
	public void setDirection(Direction dir) {
		this.dir = dir;
	}
	
	/**
	 * @param degrees
	 */
	public void rotateCW(float degrees) {
		dir.subtract(degrees);
	}
	
	/**
	 * @param degrees
	 */
	public void rotateCCW(float degrees) {
		dir.add(degrees);
	}
	
	/**
	 * @return
	 */
	public int getX() {
		return origin.getX();
	}
	
	/**
	 * @return
	 */
	public int getY() {
		return origin.getY();
	}
}
