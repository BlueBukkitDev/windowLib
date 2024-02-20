package windowAPI.test;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import windowAPI.ui.UIObject;

public class KeyManager implements KeyListener {

	@Override
	public void keyTyped(KeyEvent e) {
		for(UIObject each:Main.getWindow().getRegistry().getObjects()) {
			each.onType(e);
		}
		Main.getEngine().update();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			Main.stop();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

}
