package windowAPI.ui;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class TextBit {
	
	protected Color c;

	protected Font f;

	protected String s;

	protected int x;

	protected int y;

	protected TextBit[] bits;

	protected Runnable hover;

	protected Runnable click;

	protected boolean isContainer;

	public TextBit(Color c, Font f, String s, Runnable hover, Runnable click) {
		this.c = c;
		this.f = f;
		this.s = s;
		this.hover = hover;
		this.click = click;
		this.isContainer = false;
	}
	
	public int getWidth(Graphics g) {
		int w = 0;
		if(isContainer) {
			for(TextBit each:bits) {
				w += each.getWidth(g);
			}
		}
		w += g.getFontMetrics().stringWidth(s);
		return w;
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

	public TextBit(TextBit... bits) {
		this.bits = bits;
		this.isContainer = true;
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

	public TextBit[] getBits() {
		List<TextBit> bitsList = new ArrayList<>();
		byte b;
		int i;
		TextBit[] arrayOfTextBit;
		for (i = (arrayOfTextBit = this.bits).length, b = 0; b < i;) {
			TextBit each = arrayOfTextBit[b];
			byte b1;
			int j;
			TextBit[] arrayOfTextBit1;
			for (j = (arrayOfTextBit1 = each.getBits()).length, b1 = 0; b1 < j;) {
				TextBit every = arrayOfTextBit1[b1];
				bitsList.add(every);
				b1++;
			}
			b++;
		}
		return (TextBit[]) bitsList.toArray();
	}

	public boolean isContainer() {
		return this.isContainer;
	}

	public int getHeight(Graphics g) {
		g.setFont(this.f);
		return g.getFontMetrics().getHeight();
	}
}
