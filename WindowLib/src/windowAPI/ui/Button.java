package windowAPI.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;

import windowAPI.ui.geometry.Transform;
import windowAPI.ui.gfx.Pattern;

public abstract class Button extends UIObject {

	private int tooltipTimer = 0;

	private boolean showTooltip = false;

	private boolean useTooltip = false;

	protected boolean showingClicked = false;

	protected boolean isSelected;

	private boolean showID;

	private int fontSize;

	private Color color = Color.BLACK;
	
	private Pattern pattern;

	public Button(String id, Transform transform, boolean showID, boolean useTooltip, int fontSize, int width, int height, Pattern pattern) {
		super(id, transform, width, height, pattern);
		this.showID = showID;
		this.fontSize = fontSize;
		this.useTooltip = useTooltip;
	}

	public void render(Graphics g) {
		pattern.draw(g, transform);
		if (this.showID) {
			g.setFont(new Font("Helvetica", 1, this.fontSize));
			g.setColor(this.color);
			g.drawString(this.id, this.transform.getX() + this.width / 2 - g.getFontMetrics().stringWidth(this.id) / 2,
					(int) ((this.transform.getY() + this.height / 2) + g.getFontMetrics().getHeight() / 3.5D));
		}
		if (this.showTooltip && this.useTooltip) {
			g.setFont(new Font("Helvetica", 0, 12));
			g.setColor(Color.GRAY);
			g.drawString(this.id, this.transform.getX(), (int) (this.transform.getY() + g.getFontMetrics().getHeight() * 1.5D));
		}
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void update() {
		if (this.hovering) {
			this.tooltipTimer++;
		} else if (this.showingClicked) {
			this.showingClicked = false;
		}
		if (this.tooltipTimer >= 50)
			this.showTooltip = true;
		runOnUpdate();
	}

	public void runOnUpdate() {
	}

	public void onMouseMove(Point p) {
		if (this.getBounds().contains(p)) {
			if (!this.hovering) {
				this.hovering = true;
				runOnHover();
			}
		} else if (this.hovering) {
			this.hovering = false;
			this.showTooltip = false;
			this.tooltipTimer = 0;
			runOnStopHover();
		}
	}

	public boolean onClick(int button, Point p) {
		if (this.getBounds().contains(p)) {
			runClick();
			return true;
		}
		return false;
	}

	public abstract void runOnMissedClick();

	public boolean onMouseDown(int button, Point p) {
		if (this.getBounds().contains(p)) {
			runMouseDown();
			this.showingClicked = true;
			return true;
		}
		return false;
	}

	public boolean onMouseUp(int button, Point p) {
		if (!this.getBounds().contains(p)) {
			runOnMissedClick();
		}
		if (this.showingClicked) {
			this.showingClicked = false;
			if (this.getBounds().contains(p)) {
				runMouseUp();
				onClick(button, p);
				return true;
			}
			return false;
		}
		return false;
	}

	public void onType(KeyEvent e) {
	}

	public void onKeyPressed(KeyEvent e) {
	}
	
	public void onScroll(int amount) {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}
}
