package windowAPI.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;

import windowAPI.test.Main;
import windowAPI.ui.geometry.Transform;
import windowAPI.ui.gfx.BitFormat;
import windowAPI.ui.gfx.Pattern;

public class TextArea extends UIObject {

	private String id;

	private int width;
	private int height;
	private int lastLineY = 0;
	private int cushion;
	private int timer = 0; 
	private int blinkSpeed = 16;

	private boolean isActive;
	private boolean hovering = false;
	private boolean editable;

	private List<BitLine> lines;
	private Graphics g;
	private Pattern pattern;
	
	private int cursorCol = 0, cursorRow = 0;

	public TextArea(String id, Transform transform, int width, int height, int cushion, boolean editable, Pattern pattern) {
		super(id, transform, width, height, pattern);
		this.id = id;
		this.width = width;
		this.height = height;
		this.cushion = cushion;
		this.lines = new ArrayList<>();
		this.isActive = true;
		this.editable = editable;
		this.pattern = pattern;
	}
	
	public void setGraphics(Graphics g) {
		this.g = g;
	}

	public void render(Graphics g) {
		if (this.isActive) {
			this.g = g;
			this.pattern.draw(g, transform);
			try {
				for (BitLine each : this.lines) {
					each.render(g);
				}
			} catch (ConcurrentModificationException e) {
				e.printStackTrace();
			}
			if(selected && editable) {
				renderBlinker();
			}
		}
	}
	
	private void renderBlinker() {
		if (this.timer >= 2 * this.blinkSpeed)
			this.timer = 0;
		if (this.timer <= this.blinkSpeed) {
			g.setColor(Color.LIGHT_GRAY);
			//g.fillRect(this.transform.getX() + g.getFontMetrics().stringWidth(this.displayText.substring(0, this.text1.length())) + this.indent - 1, this.transform.getY() + this.vertIndent, 2, this.height - this.vertIndent * 2);
			g.fillRect(transform.getX()+5, transform.getY()+5, 2, 20);
		}
		//this.lineHeight = g.getFontMetrics().getHeight();
		this.timer++;
	}

	public void logInfo(String message, Font bold, Font plain) {
		addLine(Arrays.asList(new TextBit("INFO: ", new BitFormat(Color.BLUE, Font.BOLD)), new TextBit(message, new BitFormat())));
	}

	public void logError(String message, Font bold, Font plain) {
		addLine(Arrays.asList(new TextBit("ERROR: ", new BitFormat(Color.RED, Font.BOLD)), new TextBit(message, new BitFormat())));
	}

	public void logIncoming(String message, Font plain, Font italic, Font bold) {
		if (message.contains("§")) {
			List<TextBit> bits = getBits(message, plain, italic, bold);
			if (this != null && bits != null)
				addLine(bits);
		} else if (this != null){
			addLine(Arrays.asList(new TextBit(message, new BitFormat())));
		}
	}

	private List<TextBit> getBits(String message, Font plain, Font italic, Font bold) {
		List<TextBit> bits = new ArrayList<TextBit>();
		for(BitLine each:lines) {
			bits.addAll(each.getBits());
		}
		return bits;
	}

	public void addLine(List<TextBit> bits) {
		BitLine line = new BitLine(new Transform(0, 0), g, this.width - this.cushion * 2);
		byte b;
		int i;
		for (i = bits.size(), b = 0; b < i;) {
			TextBit each = bits.get(b);
			line.addBit(each);
			b++;
		}
		if(lastLineY == 0) {
			lastLineY = cushion+line.getHeight();
		}
		line.setY(this.lastLineY);
		line.setX(this.transform.getX() + this.cushion);
		this.lastLineY += line.getHeight();
		this.lines.add(line);
	}

	public void update() {
		if(hovering) {
			if(!editable) {
				return;
			}
			Main.getWindow().setCursorForText();
		}
		Main.getWindow().setCursorForNormal();
	}
	
	@Override
	public void onMouseMove(Point p) {
		if (this.getBounds().contains(p)) {
			this.hovering = true;
			if(editable) Main.getWindow().setCursorForText();
		} else {
			this.hovering = false;
			if(editable) Main.getWindow().setCursorForNormal();
		}
	}

	public void run() {
		
	}

	@Override
	public boolean onClick(int button, Point p) {
		if (this.getBounds().contains(p)) {
			this.selected = true;
			run();
			return true;
		}
		this.selected = false;
		return false;
	}
	
	@Override
	public void onScroll(int amount) {
		
	}
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
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
	public boolean isHovering() {
		return this.hovering;
	}
	public void setHovering(boolean hovering) {
		this.hovering = hovering;
	}
	public List<BitLine> getLines() {
		return this.lines;
	}
	public Graphics getGraphics() {
		return this.g;
	}
	public void deactivate() {
		this.isActive = false;
	}

	public void clear() {
		try {
			this.lines.clear();
			this.lastLineY = 0;
		} catch (ConcurrentModificationException e) {
			System.out.println("Caught a CCME!");
		}
	}

	@Override
	public void onType(KeyEvent e) {
		if(e.getKeyChar() == KeyEvent.VK_ENTER) {
			lines.add(new BitLine(transform, g, width-50));
			return;
		}
		if(e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {//Need to do backspaces, verify newlines, and work on arrow keys for navigation. 
			//Also add padding, use index to insert text, and add bitformat settings to this area.///////////////////////////////////////////////////////////////////
			
		}
		if(lines.size() == 0) {
			lines.add(new BitLine(transform, g, width-50));
			lines.get(0).addBit(new TextBit(e.getKeyChar()+"", new BitFormat(Color.LIGHT_GRAY)));
		}else {
			lines.get(0).addBit(new TextBit(e.getKeyChar()+"", new BitFormat(Color.LIGHT_GRAY)));
		}
		/*if(lines.get(lines.size()).getWidth() >= width-50) {
			System.out.println("Too long");
		}else {
			lines.get(lines.size()).addBit(new TextBit(paramKeyEvent.getKeyChar()+"", new BitFormat()));
		}*/
	}

	@Override
	public void onKeyPressed(KeyEvent paramKeyEvent) {

	}

	@Override
	public boolean onMouseDown(int paramInt, Point paramPoint) {
		return false;
	}

	@Override
	public boolean onMouseUp(int paramInt, Point paramPoint) {
		return false;
	}
	
	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	@Override
	public void onKeyReleased(KeyEvent paramKeyEvent) {
		
	}
}
