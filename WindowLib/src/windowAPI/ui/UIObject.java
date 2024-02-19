package windowAPI.ui;


import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import windowAPI.ui.geometry.Transform;
import windowAPI.ui.gfx.Pattern;

public abstract class UIObject {
	protected boolean selected = false;

	protected boolean hovering = false;

	protected String id;
	
	protected Transform transform;

	protected int width;

	protected int height;
	
	protected Pattern pattern;
	
	protected UIObject(String id, Transform transform, int width, int height, Pattern pattern) {
		this.id = id;
		this.transform = transform;
		this.width = width;
		this.height = height;
		this.pattern = pattern;
	}
	
	protected Transform getTransform() {
		return transform;
	}

	protected void runClick() {}

	protected void runMouseDown() {}

	protected void runMouseUp() {}

	protected void runOnHover() {}

	protected void runOnStopHover() {}

	public abstract void render(Graphics paramGraphics);

	public abstract void update();
	
	public abstract void onScroll(int amount);

	public abstract boolean onClick(int paramInt, Point paramPoint);

	public abstract void onType(KeyEvent paramKeyEvent);

	public abstract void onKeyPressed(KeyEvent paramKeyEvent);
	
	public abstract void onKeyReleased(KeyEvent paramKeyEvent);

	public abstract void onMouseMove(Point paramPoint);

	public abstract boolean onMouseDown(int paramInt, Point paramPoint);

	public abstract boolean onMouseUp(int paramInt, Point paramPoint);

	public Rectangle getBounds() {
		return new Rectangle(transform.getX(), transform.getY(), width, height);
	}

	public int getX() {
		return this.transform.getX();
	}

	public int getY() {
		return this.transform.getY();
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setHovering(boolean hovering) {
		this.hovering = hovering;
		if (hovering = false) {
			runOnStopHover();
		} else if (hovering = true) {
			runOnHover();
		}
	}

	public void stopHovering() {
		this.hovering = false;
		runOnStopHover();
	}

	protected boolean isHovering() {
		return this.hovering;
	}

	public boolean isSelected() {
		return this.selected;
	}
}
