package windowAPI.ui.gfx;

import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

import windowAPI.ui.geometry.Transform;

public class Pattern {
	
	private List<Shape> shapes;
	//private Transform transform;
	
	public Pattern() {
		shapes = new ArrayList<Shape>();
		//transform = new Transform(new SP(0, 0));
	}
	
	public void addShape(Shape shape) {
		shapes.add(shape);
	}
	
	/**
	 *Draws the described pattern with an origin point of Transform 'parent'.
	 *
	 *@param g - the Graphics object being used to draw
	 *@param parent - the Transform of the parent object
	 **/
	public void draw(Graphics g, Transform parent) {
		for(Shape each:shapes) {
			if(each.isPolygon()) {
				if(each.getFilled()) {
					g.setColor(each.getFillColor()); 
					g.fillPolygon((Polygon)each.getShape());
				}
				if(each.getOutlined()) {
					g.setColor(each.getOutlineColor());
					g.drawPolygon((Polygon)each.getShape());
				}
			}//else if(each.isRect()) {
				//g.fillRect();
			//}
		}
	}
}
