package windowAPI.test;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import windowAPI.ui.UIObject;

public class MouseManager implements MouseListener, MouseMotionListener, MouseWheelListener {

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for(UIObject each:Main.getWindow().getRegistry().getObjects()) {
			each.onMouseMove(e.getPoint());
		}
		Main.getEngine().update();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		for(UIObject each:Main.getWindow().getRegistry().getObjects()) {
			each.onClick(e.getButton(), e.getPoint());
		}
		Main.getEngine().update();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}
}
