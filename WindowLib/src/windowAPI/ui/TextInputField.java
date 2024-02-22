package windowAPI.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import windowAPI.ui.geometry.Transform;
import windowAPI.ui.gfx.BitFormat;
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
						this.toWriteTo.addLine(Arrays.asList(bits));
						setText("");
						prefix = "";
						suffix = "";
					}
				} else {
					this.toWriteTo.addLine(Arrays.asList(new TextBit(message, new BitFormat())));
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
			bit = new TextBit(parts[i].substring(1), new BitFormat(getColorFromChar(parts[i].charAt(0))));
			bits[i] = bit;
		}
		return bits;
	}
	
	private Color getColorFromChar(char c) {
		switch(c) {
		case 'a':return new Color(0, 255, 0);
		case 'b':return Color.CYAN;
		case 'c':return Color.RED;
		case 'd':return Color.PINK;
		case 'e':return Color.YELLOW;
		case 'f':return Color.WHITE;
		case 'g':return Color.BLACK;
		case '0':return Color.BLUE;
		case '1':return new Color(0, 80, 0);
		case '2':return new Color(0, 80, 80);
		case '3':return new Color(80, 0, 0);
		case '4':return new Color(80, 0, 80);
		case '5':return new Color(220, 220, 0);
		case '6':return Color.GRAY;
		case '7':return Color.DARK_GRAY;
		case '8':return new Color(80, 0, 200);
		default:return Color.BLACK;
		}
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
