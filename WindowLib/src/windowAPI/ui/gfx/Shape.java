package windowAPI.ui.gfx;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

import windowAPI.ui.geometry.Direction;
import windowAPI.ui.geometry.SP;
import windowAPI.ui.geometry.Transform;

public class Shape {
	private Transform parent;
	private java.awt.Shape poly;
	private Color color;
	private int fill;
	private Color outline;
	
	public Shape(Transform parent, Color color, SP... points) {
		createPolygon(parent, color, points);
	}
	
	public Shape(Transform parent, Color color, SP topLeft, SP bottomRight, boolean ellipse) {
		this.parent = parent;
		if(!ellipse) {
			if(parent.getDirection().getAngle() != 0) {
				createPolygon(parent, color, topLeft, new SP(bottomRight.getX(), topLeft.getY()), bottomRight, new SP(topLeft.getX(), bottomRight.getY()));
			}
			poly = new Rectangle(topLeft.getX(), topLeft.getY(), bottomRight.getX()-topLeft.getX(), bottomRight.getY()-topLeft.getY());
		}else {
			poly = new Ellipse2D.Double(topLeft.getX(), topLeft.getY(), bottomRight.getX()-topLeft.getX(), bottomRight.getY()-topLeft.getY());
		}
		this.color = color;
	}
	
	private void createPolygon(Transform parent, Color color, SP... points) {
		this.parent = parent;
		poly = new Polygon();
		SP origin = new SP(0, 0);
		for(int i = 0; i < points.length; i++) {
			double distance = origin.distanceTo(points[i]);
			Direction dir = origin.directionTo(points[i]);
			dir.add(parent.getDirection().getAngle());
			SP target = origin.translate(dir, distance);
			((Polygon)poly).addPoint(
					target.getX()+parent.getX(), 
					target.getY()+parent.getY()
			);
		}
		this.color = color;
	}
	
	/**
	 *0 for outline only, 1 for filled only, 2 for filled with outline of other color. 
	 **/
	public void setStyle(int fill) {
		this.fill = fill;
	}
	
	public boolean getFilled() {
		return (fill == 1 || fill == 2 ? true : false);
	}
	
	public boolean getOutlined() {
		return (fill == 0 || fill == 2 ? true : false);
	}
	
	public boolean isPolygon() {
		return poly instanceof Polygon;
	}
	
	public boolean isRect() {
		return poly instanceof Rectangle;
	}
	
	public boolean isEllipse() {
		return poly instanceof Ellipse2D;
	}
	
	public Transform getTransform() {
		return parent;
	}
	
	/**
	 *@return an AWT Polygon
	 **/
	public java.awt.Shape getShape() {
		return poly;
	}
	
	public Color getFillColor() {
		return color;
	}
	
	public Color getOutlineColor() {
		return outline;
	}
	
	public void setOutlineColor(Color color) {
		this.outline = color;
	}
}
