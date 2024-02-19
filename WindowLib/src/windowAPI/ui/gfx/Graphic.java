package windowAPI.ui.gfx;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;

import windowAPI.ui.UIObject;
import windowAPI.ui.geometry.Transform;

public class Graphic extends UIObject {
	
	private Pattern pattern;
	
	/**
	 * Takes in an id (can be anything, but should be unique so as to be identifiable by name) and a 
	 * Pattern to be drawn. Will not respond to clicks, scrolls, key presses, hovers, etc. Strictly for display. 
	 * 
	 *@param id - the string identification of this graphic
	 *@param pattern - the Pattern to be drawn
	 **/
	public Graphic(String id, Transform transform, Pattern pattern) {
		super(id, transform, 1, 1, pattern);
		this.id = id;
		this.pattern = pattern;
	}

	@Override
	public void render(Graphics g) {
		pattern.draw(g, transform);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScroll(int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onClick(int paramInt, Point paramPoint) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onType(KeyEvent paramKeyEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyPressed(KeyEvent paramKeyEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMouseMove(Point paramPoint) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMouseDown(int paramInt, Point paramPoint) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onMouseUp(int paramInt, Point paramPoint) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	@Override
	public void onKeyReleased(KeyEvent paramKeyEvent) {
		// TODO Auto-generated method stub
		
	}
	
}
