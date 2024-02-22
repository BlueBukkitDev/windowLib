package windowAPI.ui;


import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import windowAPI.ui.geometry.Transform;

/**
 *x and y are by default 0, so that x and y are local coordinates and transform is coordinates relative to parent or screenspace, whichever is closest. 
 **/
public class BitLine {
	private List<TextBit> bits = new ArrayList<>();
	private List<TextBit> bufferedBits = new ArrayList<TextBit>();
	//private List<List<TextBit>> rows = new ArrayList<List<TextBit>>();

	private int x;
	private int y;
	private int index;
	private int width;
	private int height;
	int maxWidth;
	
	private Graphics g;
	private Transform transform;

	boolean maxWidthSet = false;
	private boolean wrap = false;

	public BitLine(Transform transform, Graphics g) {
		this.transform = transform;
		this.index = this.x;
		
		this.g = g;
	}
	
	public void setMaxWidth(int width) {
		maxWidth = width;
		maxWidthSet = true;
	}
	
	public void setWrap(boolean wrap) {
		this.wrap = wrap;
	}
	
	public int getSubWidth(int index) {
		int width = 0;
		if(index > bits.size()) {
			index = bits.size();
		}
		if(index < 0) {
			index = 0;
		}
		for(int i = 0; i < index; i++) {
			width += g.getFontMetrics(bits.get(i).getF()).stringWidth(bits.get(i).getS());
		}
		return width;
	}

	/**
	 *Returns true if successful, returns false if it could not add a bit. Reasons for this may 
	 *include a null parameter, a bit of 0 length, or the line's maximum width being exceeded. 
	 **/
	public boolean addBit(TextBit bit, int index) {
		if(bit == null) {
			return false;
		}
		width += g.getFontMetrics().stringWidth(bit.s);
		if(!canAddBit(bit)) {
			width -= g.getFontMetrics().stringWidth(bit.s);
			return false;
		}
		this.bits.add(index, bit);
		adjustHeight();
		return true;
	}
	
	private boolean canAddBit(TextBit bit) {
		if (bit.getS().length() <= 0) {
			return false;
		}
		if(!isMax()) {
			return true;
		}
		if(wrap) {
			return true;
		}
		return false;
	}
	
	private boolean isMax() {
		if(maxWidthSet && calculateWidth()+x >= maxWidth) {
			return true;
		}
		return false;
	}
	
	private void adjustHeight() {
		int i = 0;
		for (TextBit each : this.bits) {
			int h = each.getHeight(g);
			if (h > i)
				i = h;
		}
		if (i > this.height)
			this.height = i;
	}
	
	public List<TextBit> getBits() {
		return bits;
	}
	
	public void removeBit(int index) {
		bufferedBits.add(bits.get(index));
	}

	/**
	 *Renders each textbit sequentially, iterating to the right by the width of the bit.
	 **/
	public void render(Graphics g) {
		this.index = this.x;
		int line = 1;
		for (TextBit each : this.bits) {
			if(maxWidthSet && index > maxWidth) {
				line++;
				index = this.x;
			}
			g.setColor(each.getC());
			g.setFont(each.getF());
			g.drawString(each.getS(), this.index+transform.getX(), this.y+transform.getY()+(this.height*line));
			this.index += each.getWidth(g);
		}
	}
	
	public void update() {
		bits.removeAll(bufferedBits);
		bufferedBits.clear();
	}

	public int getX() {
		return this.x;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int calculateWidth() {
		int wide = 0;
		for (TextBit each : this.bits) {
			wide += each.getWidth(g);
		}
		return wide;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int y) {
		this.y = y;
	}

	/**
	 *Returns 0 until a textbit has been added.
	 **/
	public int getHeight() {
		if(this.height == 0) {
			return g.getFontMetrics(new Font("Helvetica", Font.PLAIN, 16)).getHeight();
		}
		return this.height;
	}
}
