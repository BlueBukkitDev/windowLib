package windowAPI.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;

import windowAPI.Fonts;
import windowAPI.ui.geometry.Transform;
import windowAPI.ui.gfx.Pattern;

public abstract class TextInputField extends InputField {

	protected boolean writable;

	protected int vertIndent = 4;

	protected int indent = 6;

	protected int timer = 0;

	protected int blinkSpeed = 16;

	protected String text1 = "";

	protected String text2 = "";

	protected String displayText = "";
	
	protected String prefix = "";
	
	protected String suffix = "";

	protected String preview = "";

	protected char[] allowed = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
			'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', '0', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '-', '+', '=', '~', '`',
			',', '<', '.', '>', '/', '?', '\\', '|', ' ', '{', '}', '[', ']', ':', ';', '\'', '"', '§', '°' };

	protected Graphics g;

	protected Font font;

	protected int typeIndex = 0;

	protected boolean protectedDisplay = false;

	protected boolean showingClicked = false;

	protected boolean visible = true;

	private TextArea toWriteTo;

	private Pattern pattern;

	private int lineHeight = 0;

	public TextInputField(String id, Transform transform, int width, int height, String preview, String value, boolean writable, boolean protectedDisplay, TextArea toWriteTo, Pattern pattern) {
		super(id, transform, width, height, pattern);
		this.writable = writable;
		this.preview = preview;
		this.text1 = value;
		this.displayText = this.text1;
		this.protectedDisplay = protectedDisplay;
		this.toWriteTo = toWriteTo;
	}

	public void render(Graphics g) {
		if (this.visible) {
			pattern.draw(g, transform);
			if (this.text1.length() + this.text2.length() >= 1) {
				if (this.font == null) {
					g.setFont(new Font("Arial", Font.PLAIN, 16));
				} else {
					g.setFont(this.font);
				}
				g.setColor(Color.BLACK);
				if (this.protectedDisplay) {
					String replacement = "";
					for (int i = 0; i < this.displayText.length(); i++)
						replacement = String.valueOf(replacement) + "*";
					g.drawString(replacement, this.transform.getX() + this.indent - 1, this.transform.getY() + g.getFontMetrics().getHeight());
				} else {
					g.drawString(this.displayText, this.transform.getX() + this.indent - 1, this.transform.getY() + g.getFontMetrics().getHeight());
				}
			} else if (!isSelected()) {
				if (this.font == null) {
					g.setFont(new Font("Arial", Font.ITALIC, 14));
				} else {
					g.setFont(this.font);
				}
				g.setColor(Color.BLACK);
				g.drawString(this.preview, this.transform.getX() + this.indent - 1, this.transform.getY() + g.getFontMetrics().getHeight());
			}
			this.g = g;
			renderBlinker();
		}
	}
	
	private void renderBlinker() {
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
	}

	public String getText() {
		return String.valueOf(this.text1) + this.text2;
	}

	public void setText(String text) {
		this.text1 = text;
		this.text2 = "";
		this.displayText = String.valueOf(this.text1) + this.text2;
		this.typeIndex = this.displayText.length();
	}

	public void update() {
		runUpdate();
	}
	
	public abstract void runUpdate();

	public boolean onClick(int button, Point p) {
		if (this.visible) {
			if (this.getBounds().contains(p)) {
				this.timer = 0;
				if (this.writable) {
					this.selected = true;
					runClick();
				}
				int i = getIndex(p);
				if (i < this.displayText.length()) {
					this.text1 = this.displayText.substring(0, i);
					this.text2 = this.displayText.substring(i, this.displayText.length());
					this.typeIndex = i;
				} else if (i >= this.displayText.length()) {
					this.text1 = this.displayText;
					this.text2 = "";
					this.typeIndex = this.displayText.length();
				}
				return true;
			}
			this.selected = false;
		}
		return false;
	}

	public boolean onMouseDown(int button, Point p) {
		if (this.getBounds().contains(p)) {
			runMouseDown();
			this.showingClicked = true;
			return true;
		}
		return false;
	}

	public boolean onMouseUp(int button, Point p) {
		if (this.getBounds().contains(p)) {
			if (this.showingClicked) {
				this.showingClicked = false;
				runMouseUp();
				onClick(button, p);
				return true;
			}
			return false;
		}
		
		this.selected = false;
		return false;
	}

	public void onMouseMove(Point p) {
		if (this.visible)
			if (this.getBounds().contains(p)) {
				this.hovering = true;
				setTypeCursor();
			} else {
				this.hovering = false;
				setPointerCursor();
			}
	}

	public void onType(KeyEvent e) {
		if (this.visible) {
			if (selected) {
				if (this.g.getFontMetrics().stringWidth(String.valueOf(this.text1) + this.text2 + e.getKeyChar()) <= this.width - this.indent * 2) {// if the width is not too wide
					byte b;
					int i;
					char[] arrayOfChar;
					for (i = (arrayOfChar = this.allowed).length, b = 0; b < i;) {
						char each = arrayOfChar[b];
						if (each == e.getKeyChar()) {
							this.text1 = String.valueOf(this.text1) + e.getKeyChar();
							this.displayText = String.valueOf(this.text1) + this.text2;
							this.typeIndex++;
							break;
						}
						b++;
					}
				} else {
					this.setHeight(this.getHeight() + this.lineHeight);
					this.transform.getPosition().setY(this.transform.getY() - this.lineHeight);
				}
				if (e.getKeyChar() == '\b' && this.text1.length() > 0) {
					this.text1 = this.text1.substring(0, this.text1.length() - 1);
					this.displayText = String.valueOf(this.text1) + this.text2;
					this.typeIndex--;
				}
				if (this.protectedDisplay) {
					String replacement = "";
					for (int i = 0; i < this.displayText.length(); i++)
						replacement = String.valueOf(replacement) + "*";
					this.displayText = replacement;
				}
				onTypeExtra();
			}
		}
	}

	public void onTypeExtra() {
	}

	public void onKeyPressed(KeyEvent e) {
		if (this.visible) {
			if (selected) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {// 37
					if (this.typeIndex >= 1) {
						this.text1 = this.text1.substring(0, this.text1.length() - 1);
						this.text2 = this.displayText.substring(this.typeIndex - 1, this.displayText.length());
						this.typeIndex--;
						this.timer = 0;
					}
				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {// 39
					if (this.typeIndex < this.displayText.length()) {
						this.text1 = this.displayText.substring(0, this.text1.length() + 1);
						this.text2 = this.displayText.substring(this.typeIndex + 1, this.displayText.length());
						this.typeIndex++;
						this.timer = 0;
					}
				} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {// 10
					print();
				}
			}
		}
	}

	@Override
	public void onScroll(int amount) {

	}

	public TextInputField setFont(Font font) {
		this.font = font;
		return this;
	}

	public boolean onPrint() {
		return true;
	}

	public void print() {
		if(onPrint()) {
			if (this.toWriteTo != null) {
				String message = prefix+getText()+suffix;
				if (message.contains("§")) {
					TextBit[] bits = getBits(message);
					if (bits != null) {
						this.toWriteTo.addLine(bits);
						setText("");
						prefix = "";
						suffix = "";
					}
				} else {
					this.toWriteTo.addLine(new TextBit[] { new TextBit(Color.BLACK, Fonts.plain, message, null, null) });
					setText("");
					prefix = "";
					suffix = "";
				}
			}
		}
	}

	public void setToWriteTo(TextArea toWriteTo) {
		this.toWriteTo = toWriteTo;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible.booleanValue();
	}

	private TextBit[] getBits(String message) {
		String[] parts = message.split("§");
		TextBit[] bits = new TextBit[parts.length];
		for (int i = 1; i < parts.length; i++) {
			TextBit bit;
			String first = parts[i].split("")[0];
			switch(first) {
			case "a":bit = new TextBit(new Color(0, 255, 0), Fonts.plain, parts[i].substring(1), null, null);break;
			case "b":bit = new TextBit(Color.CYAN, Fonts.plain, parts[i].substring(1), null, null);break;
			case "c":bit = new TextBit(Color.RED, Fonts.plain, parts[i].substring(1), null, null);
			case "d":bit = new TextBit(Color.PINK, Fonts.plain, parts[i].substring(1), null, null);
			case "e":bit = new TextBit(Color.YELLOW, Fonts.plain, parts[i].substring(1), null, null);
			case "f":bit = new TextBit(Color.WHITE, Fonts.plain, parts[i].substring(1), null, null);
			case "0":bit = new TextBit(Color.BLACK, Fonts.plain, parts[i].substring(1), null, null);
			case "1":bit = new TextBit(Color.BLUE, Fonts.plain, parts[i].substring(1), null, null);
			case "2":bit = new TextBit(new Color(0, 80, 0), Fonts.plain, parts[i].substring(1), null, null);
			case "3":bit = new TextBit(new Color(0, 80, 80), Fonts.plain, parts[i].substring(1), null, null);
			case "4":bit = new TextBit(new Color(80, 0, 0), Fonts.plain, parts[i].substring(1), null, null);
			case "5":bit = new TextBit(new Color(80, 0, 80), Fonts.plain, parts[i].substring(1), null, null);
			case "6":bit = new TextBit(new Color(220, 220, 0), Fonts.plain, parts[i].substring(1), null, null);
			case "7":bit = new TextBit(Color.GRAY, Fonts.plain, parts[i].substring(1), null, null);
			case "8":bit = new TextBit(Color.DARK_GRAY, Fonts.plain, parts[i].substring(1), null, null);
			case "9":bit = new TextBit(new Color(80, 0, 200), Fonts.plain, parts[i].substring(1), null, null);
			case "l":{
						if (i > 1) {
							bit = new TextBit(bits[i - 1].getC(), Fonts.bold, parts[i].substring(1), null, null);
						} else {
							bit = new TextBit(Color.BLACK, Fonts.bold, parts[i].substring(1), null, null);
						}
					}
			case "i":{
						if (i > 1) {
							bit = new TextBit(bits[i - 1].getC(), Fonts.italic, parts[i].substring(1), null, null);
						} else {
							bit = new TextBit(Color.BLACK, Fonts.italic, parts[i].substring(1), null, null);
						}
					}
			case "r":bit = new TextBit(Color.BLACK, Fonts.plain, parts[i].substring(1), null, null);
			default:{
					if (i > 1) {
						bit = new TextBit(bits[i - 1].getC(), bits[i - 1].getF(), parts[i], null, null);
					} else {
						bit = new TextBit(Color.BLACK, Fonts.plain, "§" + parts[i], null, null);
					}
				}
			}
			bits[i] = bit;
		}
		return bits;
	}

	private int getIndex(Point p) {
		if (this.getBounds().contains(p))
			for (int i = 0; i < this.displayText.length(); i++) {
				if ((this.transform.getX() + this.indent + this.g.getFontMetrics().stringWidth(this.displayText.substring(0, i))) >= p
						.getX())
					return i;
				if ((this.transform.getX() + this.indent + this.g.getFontMetrics().stringWidth(this.displayText)) < p.getX())
					return this.displayText.length();
			}
		return 0;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}
}
