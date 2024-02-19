package windowAPI.test;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import windowAPI.ui.TextArea;
import windowAPI.ui.UIObjectRegistry;
import windowAPI.ui.geometry.SP;
import windowAPI.ui.geometry.Transform;
import windowAPI.ui.gfx.Graphic;
import windowAPI.ui.gfx.Pattern;
import windowAPI.ui.gfx.Shape;

public class Window extends Frame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Canvas canvas;
	private UIObjectRegistry registry;
	
	public boolean isSmall = false;
	
	public Window() {
		canvas = new Canvas();
		this.add(canvas);
		registry = new UIObjectRegistry();
		
		this.setTitle("Test");
		this.setUndecorated(false);
		this.setPreferredSize(new Dimension(900, 600));
		this.setResizable(true);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Main.stop();
			}
		});
		
		this.pack();
		//this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		canvas.createBufferStrategy(2);
		setupDisplay(canvas.getBufferStrategy().getDrawGraphics());
	}
	
	private void setupDisplay(Graphics g) {
		Transform contentsT = new Transform(300, 10);//300 10
		Dimension d = new Dimension(1600, 1000);
		TextArea contents = new TextArea("contents", contentsT, d.width, d.height, 20, true, contentsPattern(contentsT, d.width, d.height)) {
			@Override
			public void run() {
				this.selected = true;
				registry.setFocusedObject(this);
			}
		};
		contents.setGraphics(g);
	}
	
	private Pattern contentsPattern(Transform t, int width, int height) {
		Pattern p = new Pattern();
		Color outlineC = new Color(180, 180, 0);
		Color fillC = new Color(30, 30, 32);
		Shape panel = new Shape(t, fillC, new SP(0, 0), new SP(height, 0), new SP(height, width), new SP(0, width));
		panel.setOutlineColor(outlineC);
		panel.setStyle(2);//0 for outline only, 1 for filled only, 2 for filled with outline of other color. 
		p.addShape(panel);
		return p;
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	public UIObjectRegistry getRegistry() {
		return registry;
	}
}
