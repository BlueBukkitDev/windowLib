package windowAPI.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import windowAPI.test.Main;
import windowAPI.ui.geometry.SP;
import windowAPI.ui.geometry.Transform;
import windowAPI.ui.gfx.BitFormat;
import windowAPI.ui.gfx.Pattern;

public class TextArea extends UIObject {

	private String id;

	private int width;
	private int height;
	private int lastLineY = 0;
	private int blinkSpeed = 16;
	private int timer = 0; 
	private int cursorCol = 0;
	private int cursorRow = 0;
	private int paddingLeft;
	private int paddingRight;
	private int paddingTop;
	private int paddingBottom;

	private boolean isActive;
	private boolean hovering = false;
	private boolean editable;

	private Color blinkerColor = Color.LIGHT_GRAY;
	private Point clickedPos = new Point(0, 0);
	private List<BitLine> lines;
	private Graphics g;
	private Pattern pattern;

	public TextArea(String id, Transform transform, int width, int height, boolean editable, Pattern pattern) {
		super(id, transform, width, height, pattern);
		this.id = id;
		this.width = width;
		this.height = height;
		this.lines = new ArrayList<>();
		this.isActive = true;
		this.editable = editable;
		this.pattern = pattern;
		
		this.paddingLeft = 25;
		this.paddingRight = 15;
		this.paddingTop = 30;
		this.paddingBottom = 0;
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
			SP p = indexToPosition(cursorRow, cursorCol);
			if(p != null) {
				g.setColor(blinkerColor);
				g.fillRect(transform.getX()+paddingLeft+p.getX(), transform.getY()+p.getY()-20, 2, 20);
			}
		}
		this.timer++;
	}
	
	private int positionToRow(Point p) {
		int y = (int) p.getY();
		y -= transform.getY();
		y -= this.getY();
		y -= paddingTop;
		for(int i = 0; i < lines.size(); i++) {
			if(lines.get(i).getY() > y) {
				continue;
			}else {
				return i;
			}
		}
		return 0;//Not sure I should return 0. May cause unexpected behavior.
	}
	
	private int positionToColumn(Point p, int row) {
		int x = (int) p.getX();
		x -= transform.getX();
		x -= this.getX();
		x -= paddingLeft;
		List<TextBit> bits = lines.get(row).getBits();
		for(int i = 0; i < bits.size(); i++) {
			if(bits.get(i).x > x) {
				continue;
			}else {
				return i;
			}
		}
		return 0;//Not sure I should return 0. May cause unexpected behavior.
	}
	
	/**
	 *Produces a position to be used by the blinker, given row and column. 
	 **/
	private SP indexToPosition(int row, int col) {
		int x, y;
		BitLine line;
		if(lines.size() != 0) {
			if(row > lines.size()) {
				line = lines.get(lines.size());
			}else {
				line = lines.get(row);
			}
			y = line.getY();
		}else {
			return null;
		}
		
		x = line.getSubWidth(col);
		return new SP(x, y);
	}
	
	public void setBlinkerColor(Color c) {
		this.blinkerColor = c;
	}

	public void addLine(List<TextBit> bits) {
		BitLine line = new BitLine(new Transform(0, 0), g);
		byte b;
		int i;
		for (i = bits.size(), b = 0; b < i;) {
			TextBit each = bits.get(b);
			line.addBit(each, i);
			b++;
		}
		if(lastLineY == 0) {
			lastLineY = paddingTop+line.getY()+lines.size()*line.getHeight();//this is the first line being added, so we can use it as our base. 
		}
		line.setY(this.lastLineY);
		line.setX(this.transform.getX() + this.paddingLeft);
		this.lastLineY += line.getHeight();
		this.lines.add(line);
	}
	
	public void removeLine(int index) {
		lines.remove(index);
	}

	public void update() {
		if(hovering) {
			if(!editable) {
				return;
			}
			Main.getWindow().setCursorForText();
		}
		Main.getWindow().setCursorForNormal();
		
		if(selected) {
			cursorRow = positionToRow(clickedPos);
			cursorCol = positionToColumn(clickedPos, cursorRow);
		}
		
		for(BitLine each:lines) {
			each.update();
		}
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
			if(lines.size() == 0) {
				addLine(new ArrayList<TextBit>());
			}
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
		if(lines.size() == 0) {
			lines.add(new BitLine(transform, g));
			rationalizeCursor();
		}
		if(e.getKeyChar() == KeyEvent.VK_ENTER) {
			addLine(new ArrayList<TextBit>());
			cursorRow++;
			rationalizeCursor();
			return;
		}
		if(e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {//Need to do backspaces, verify newlines, and work on arrow keys for navigation. 
			//Also add padding, use index to insert text, and add bitformat settings to this area.///////////////////////////////////////////////////////////////////
			if(lines.get(cursorRow).getBits().size() == 0) {
				return;
			}
			lines.get(cursorRow).getBits().remove(cursorCol-1);
			rationalizeCursor();
			return;
		}
		
		lines.get(cursorRow).addBit(new TextBit(e.getKeyChar()+"", new BitFormat(Color.LIGHT_GRAY)), cursorCol);
		cursorCol++;
		/*if(lines.get(lines.size()).getWidth() >= width-50) {
			System.out.println("Too long");
		}else {
			lines.get(lines.size()).addBit(new TextBit(paramKeyEvent.getKeyChar()+"", new BitFormat()));
		}*/
	}
	
	private void rationalizeCursor() {
		if(cursorRow < 0) {
			cursorRow = 0;
		}else if(cursorRow > lines.size()) {
			cursorRow = lines.size();
		}
		
		if(cursorCol < 0) {
			cursorCol = 0;
		}else if(cursorCol > lines.get(cursorRow).getBits().size()) {
			cursorCol = lines.get(cursorRow).getBits().size();
		}
	}

	@Override
	public void onKeyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			cursorCol--;
			rationalizeCursor();
			return;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			cursorCol++;
			rationalizeCursor();
			return;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			cursorRow--;
			rationalizeCursor();
			return;
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			cursorRow++;
			rationalizeCursor();
			return;
		}
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
