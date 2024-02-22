package windowAPI.ui.gfx;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

public class BitFormat {
	private Color color = Color.BLACK;
	private Font font = new Font(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()[0], Font.PLAIN, 14);
	private Runnable hover;
	private Runnable click;
	
	public BitFormat(Color color, Font font, Runnable hover, Runnable click) {
		this.color = color;
		this.font = font;
		this.hover = hover;
		this.click = click;
	}
	
	public BitFormat(Color color, int fontStyle) {
		this.color = color;
		this.font = font.deriveFont(fontStyle);
	}
	
	public BitFormat() {}
	
	/**
	 *@param the AWT Color used in this format
	 **/
	public BitFormat(Color color) {
		this.color = color;
	}
	
	/**
	 *@param the FontStyle used in this format
	 **/
	public BitFormat(int style) {
		this.font = this.font.deriveFont(style);
	}
	
	/**
	 *@param the AWT Font size used in this format
	 **/
	public BitFormat(float size) {
		this.font = this.font.deriveFont(size);
	}
	
	/**
	 *@param the AWT Font family used in this format
	 **/
	public BitFormat(String fontFamily) {
		this.font = new Font(fontFamily, this.font.getStyle(), this.font.getSize());
	}
	
	public void setHover(Runnable hover) {
		this.hover = hover;
	}
	
	public void setClick(Runnable click) {
		this.click = click;
	}
	
	public void setColor(Color c) {
		this.color = c;
	}
	
	public void setStyle(int style) {
		this.font = this.font.deriveFont(style);
	}
	
	public Color getColor() {
		return color;
	}
	
	public Font getFont() {
		return font;
	}
	
	public Runnable getHover() {
		return hover;
	}
	
	public Runnable getClick() {
		return click;
	}
}
