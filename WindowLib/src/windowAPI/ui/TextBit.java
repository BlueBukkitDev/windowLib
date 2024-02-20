package windowAPI.ui;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import windowAPI.ui.gfx.BitFormat;

public class TextBit {
	
	protected Color c;

	protected Font f;

	protected String s;

	protected int x;

	protected int y;

	protected TextBit[] bits;

	protected Runnable hover;

	protected Runnable click;

	public TextBit(String s, BitFormat format) {
		this.c = format.getColor();
		this.f = format.getFont();
		this.s = s;
		this.hover = format.getHover();
		this.click = format.getClick();
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public void onMouseHover() {
		if (this.hover != null)
			this.hover.run();
	}

	public void onMouseClick() {
		if (this.click != null)
			this.click.run();
	}

	public Color getC() {
		return this.c;
	}

	public void setC(Color c) {
		this.c = c;
	}

	public Font getF() {
		return this.f;
	}

	public void setF(Font f) {
		this.f = f;
	}

	public String getS() {
		return this.s;
	}

	public void setS(String s) {
		this.s = s;
	}

	public Runnable getHover() {
		return this.hover;
	}

	public void setHover(Runnable hover) {
		this.hover = hover;
	}

	public Runnable getClick() {
		return this.click;
	}

	public void setClick(Runnable click) {
		this.click = click;
	}
	
	public int getWidth(Graphics g) {
		g.setFont(f);
		return g.getFontMetrics().stringWidth(s);
	}

	public int getHeight(Graphics g) {
		g.setFont(this.f);
		return g.getFontMetrics().getHeight();
	}
}
