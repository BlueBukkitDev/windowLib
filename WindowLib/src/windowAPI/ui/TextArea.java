package windowAPI.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import windowAPI.ui.geometry.Transform;
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
			//if(editable) {
			//	renderBlinker();
			//}
		}
	}
	
	/*private void renderBlinker() {
		if (this.selected) {
			if (this.timer >= 2 * this.blinkSpeed)
				this.timer = 0;
			if (this.timer <= this.blinkSpeed) {
				g.setColor(Color.BLACK);
				g.fillRect(this.transform.getX() + g.getFontMetrics().stringWidth(this.displayText.substring(0, this.text1.length())) + this.indent - 1,
						this.transform.getY() + this.vertIndent, 2, this.height - this.vertIndent * 2);
			}
		}
		this.lineHeight = g.getFontMetrics().getHeight();
		this.timer++;
	}*/

	public void logInfo(String message, Font bold, Font plain) {
		addLine(new TextBit[] { new TextBit(Color.BLUE, bold, "INFO: ", null, null),
				new TextBit(Color.BLACK, plain, message, null, null) });
	}

	public void logError(String message, Font bold, Font plain) {
		addLine(new TextBit[] { new TextBit(Color.RED, bold, "ERROR: ", null, null),
				new TextBit(Color.RED, plain, message, null, null) });
	}

	public void logIncoming(String message, Font plain, Font italic, Font bold) {
		if (message.contains("§")) {/////////////////////// Guess it was �, idk
			TextBit[] bits = getBits(message, plain, italic, bold);
			if (this != null && bits != null)
				addLine(bits);
		} else if (this != null){
			addLine(new TextBit[] { new TextBit(Color.BLACK, plain, message, null, null) });
		}
	}

	private TextBit[] getBits(String message, Font plain, Font italic, Font bold) {
		String[] parts = message.split("§");//////////////////////////// Prolly also is �
		TextBit[] bits = new TextBit[parts.length];
		for (int i = 1; i < parts.length; i++) {
			TextBit bit;
			if (parts[i].startsWith("a")) {
				bit = new TextBit(new Color(0, 255, 0), plain, parts[i].substring(1), null, null);
			} else if (parts[i].startsWith("b")) {
				bit = new TextBit(Color.CYAN, plain, parts[i].substring(1), null, null);
			} else if (parts[i].startsWith("c")) {
				bit = new TextBit(Color.RED, plain, parts[i].substring(1), null, null);
			} else if (parts[i].startsWith("d")) {
				bit = new TextBit(Color.PINK, plain, parts[i].substring(1), null, null);
			} else if (parts[i].startsWith("e")) {
				bit = new TextBit(Color.YELLOW, plain, parts[i].substring(1), null, null);
			} else if (parts[i].startsWith("f")) {
				bit = new TextBit(Color.WHITE, plain, parts[i].substring(1), null, null);
			} else if (parts[i].startsWith("0")) {
				bit = new TextBit(Color.BLACK, plain, parts[i].substring(1), null, null);
			} else if (parts[i].startsWith("1")) {
				bit = new TextBit(Color.BLUE, plain, parts[i].substring(1), null, null);
			} else if (parts[i].startsWith("2")) {
				bit = new TextBit(new Color(0, 80, 0), plain, parts[i].substring(1), null, null);
			} else if (parts[i].startsWith("3")) {
				bit = new TextBit(new Color(0, 80, 80), plain, parts[i].substring(1), null, null);
			} else if (parts[i].startsWith("4")) {
				bit = new TextBit(new Color(80, 0, 0), plain, parts[i].substring(1), null, null);
			} else if (parts[i].startsWith("5")) {
				bit = new TextBit(new Color(80, 0, 80), plain, parts[i].substring(1), null, null);
			} else if (parts[i].startsWith("6")) {
				bit = new TextBit(new Color(80, 0, 80), plain, parts[i].substring(1), null, null);
			} else if (parts[i].startsWith("7")) {
				bit = new TextBit(Color.GRAY, plain, parts[i].substring(1), null, null);
			} else if (parts[i].startsWith("8")) {
				bit = new TextBit(Color.DARK_GRAY, plain, parts[i].substring(1), null, null);
			} else if (parts[i].startsWith("9")) {
				bit = new TextBit(new Color(80, 0, 200), plain, parts[i].substring(1), null, null);
			} else if (parts[i].startsWith("l")) {
				if (i > 1) {
					bit = new TextBit(bits[i - 1].getC(), bold, parts[i].substring(1), null, null);
				} else {
					bit = new TextBit(Color.BLACK, bold, parts[i].substring(1), null, null);
				}
			} else if (parts[i].startsWith("i")) {
				if (i > 1) {
					bit = new TextBit(bits[i - 1].getC(), italic, parts[i].substring(1), null, null);
				} else {
					bit = new TextBit(Color.BLACK, italic, parts[i].substring(1), null, null);
				}
			} else if (parts[i].startsWith("r")) {
				bit = new TextBit(Color.BLACK, plain, parts[i].substring(1), null, null);
			} else if (i > 1) {
				bit = new TextBit(bits[i - 1].getC(), bits[i - 1].getF(), parts[i], null, null);
			} else {
				bit = new TextBit(Color.BLACK, plain, "�" + parts[i], null, null);
			}
			bits[i] = bit;
		}
		return bits;
	}

	public void addLine(TextBit... bits) {
		BitLine line = new BitLine(new Transform(0, 0), g, this.width - this.cushion * 2);
		byte b;
		int i;
		TextBit[] arrayOfTextBit;
		for (i = (arrayOfTextBit = bits).length, b = 0; b < i;) {
			TextBit each = arrayOfTextBit[b];
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

	}
	
	@Override
	public void onMouseMove(Point p) {
		if (this.getBounds().contains(p)) {
			this.hovering = true;
		} else {
			this.hovering = false;
		}
	}

	public void run() {
	}

	@Override
	public boolean onClick(int button, Point p) {
		if (this.getBounds().contains(p)) {
			run();
			return true;
		}
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
	public void onType(KeyEvent paramKeyEvent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onKeyPressed(KeyEvent paramKeyEvent) {
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
