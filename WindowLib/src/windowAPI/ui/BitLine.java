package windowAPI.ui;


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

	private int x;

	private int y;

	private int index;
	
	private Graphics g;
	
	private Transform transform;

	private int width;

	private int height;

	public BitLine(Transform transform, Graphics g) {
		this.transform = transform;
		this.index = this.x;
		
		this.g = g;
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

	public void addBit(TextBit bit) {
		if (bit != null && bit.getS().length() > 0) {
			this.bits.add(bit);
			int i = 0;
			for (TextBit each : this.bits) {
				int h = each.getHeight(g);
				if (h > i)
					i = h;
			}
			if (i > this.height)
				this.height = i;
			for(TextBit each:bits) {
				width += g.getFontMetrics().stringWidth(each.s);
			}
		}
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
		for (TextBit each : this.bits) {
			g.setColor(each.getC());
			g.setFont(each.getF());
			g.drawString(each.getS(), this.index+transform.getX(), this.y+transform.getY()+this.height);
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

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getHeight() {
		return this.height;
	}
}
