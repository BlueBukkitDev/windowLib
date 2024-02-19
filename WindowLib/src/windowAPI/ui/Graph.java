package windowAPI.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Calendar;

import windowAPI.Animation;
import windowAPI.ui.geometry.SP;
import windowAPI.ui.geometry.Transform;
import windowAPI.ui.gfx.Pattern;

/**
 *@deprecated
 **/
public class Graph extends UIObject {
	private String title;

	private Color titleColor;

	private Font titleFont;

	private int x;

	private int y;

	private int width;

	private int height;

	private int cushion;

	private Color lineColor;

	//private double pointSpacing;

	private SP[] points;

	private Animation animation;

	private boolean hovering;

	//private Button[] buttons;

	private TextArea infoPanel;

	//private boolean infoIsViewed;

	//private Button currentDot;

	private Double[] values;

	public Graph(String id, Transform transform, TextArea infoPanel, UIObjectRegistry registry, String title, Color titleColor, Font titleFont, int width, int height, int cushion, Color lineColor, Double[] values, double peak, Pattern pattern) {
		super(id, transform, width, height, pattern);
		this.title = title;
		this.titleColor = titleColor;
		this.titleFont = titleFont;
		this.width = width;
		this.height = height;
		this.cushion = cushion;
		this.lineColor = lineColor;
		this.values = values;
		this.infoPanel = infoPanel;
		setupDefaultInfoPanel();
		//registry.registerObject(this.infoPanel);
		//this.pointSpacing = (width - cushion * 2) / (values.length - 1);
		//double xCoord = cushion;
		//this.points = new SP[values.length];
		//this.buttons = new Button[values.length];
		/*for (int i = 0; i < values.length; i++) {
			YamlConfiguration yaml;
			double yCoord = values[i].doubleValue() / peak * (height - cushion * 2);
			this.points[i] = new SimplePoint((int) xCoord, (int) yCoord);
			String name = String.valueOf(yaml.getString(String.valueOf(yaml.getKeys(false).toArray()[i]) + ".Title"))
					+ "�" + yaml.getString(String.valueOf(yaml.getKeys(false).toArray()[i]) + ".Date") + "�"
					+ yaml.getString(String.valueOf(yaml.getKeys(false).toArray()[i]) + ".Score");
			Button button = new Button(app, name, false, false, 0, (int) xCoord - 5 + x, y + height - cushion - (int) yCoord - 5, 10, 10) {
				public void runClick() {
					this.isSelected = true;
					Graph.this.infoIsViewed = true;
					Graph.this.currentDot = this;
					Graph.this.updateInfoPanel(this);
				}

				public void runOnMissedClick() {
					this.isSelected = false;
					if (Graph.this.currentDot == this) {
						Graph.this.infoIsViewed = false;
						Graph.this.currentDot = null;
					}
					if (!Graph.this.infoIsViewed)
						Graph.this.setupDefaultInfoPanel();
				}

				public void runOnHover() {
					if (!this.isSelected) {

					}
				}

				public void runOnStopHover() {
					if (!this.isSelected) {

					}
				}
			};
			this.buttons[i] = button;
			app.getUIRegistry().registerObject(button);
			xCoord += this.pointSpacing;
		}*/
	}

	private void setupDefaultInfoPanel() {
		int complete = 0; 
		int total = 0;
		String firstDate = "";
		this.infoPanel.clear();
		/*
		 * if (this.subject == GraphSubject.MATH) { complete =
		 * Config.getMathStats().getKeys(false).size(); total =
		 * Config.getConfig().getInt("Math Lessons"); firstDate =
		 * Config.getConfig().getString("Math Graduation Date"); } else if (this.subject
		 * == GraphSubject.GRAMMAR) { complete =
		 * Config.getGrammarStats().getKeys(false).size(); total =
		 * Config.getConfig().getInt("Grammar Lessons"); firstDate =
		 * Config.getConfig().getString("Grammar Graduation Date"); } else if
		 * (this.subject == GraphSubject.LANGUAGE) { complete =
		 * Config.getLanguageStats().getKeys(false).size(); total =
		 * Config.getConfig().getInt("Language Lessons"); firstDate =
		 * Config.getConfig().getString("Language Graduation Date"); } else { complete =
		 * Config.getSpellingStats().getKeys(false).size(); total =
		 * Config.getConfig().getInt("Spelling Lessons"); firstDate =
		 * Config.getConfig().getString("Spelling Graduation Date"); }
		 */
		int avgScore = 0, highestScore = 0, lowestScore = 100;
		if (this.values.length > 0) {
			for (int j = 0; j < this.values.length; j++) {
				avgScore = (int) (avgScore + this.values[j].doubleValue());
				if (this.values[j].doubleValue() > highestScore) {
					highestScore = 0;
					highestScore = (int) (highestScore + this.values[j].doubleValue());
				}
				if (this.values[j].doubleValue() < lowestScore) {
					lowestScore = 0;
					lowestScore = (int) (lowestScore + this.values[j].doubleValue());
				}
			}
			avgScore /= this.values.length;
		} else {
			lowestScore = 0;
		}
		int month = Calendar.getInstance().get(2) + 1;
		int day = Calendar.getInstance().get(5);
		int year = Calendar.getInstance().get(1);
		int dayOfWeek = Calendar.getInstance().get(7);
		int remainingLessons = total - complete;
		for (int i = remainingLessons; i > 0; i--) {
			if (dayOfWeek == 7 || dayOfWeek == 1)
				i++;
			day++;
			dayOfWeek++;
			if (dayOfWeek > 7)
				dayOfWeek = 1;
			if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10) {
				if (day > 31) {
					month++;
					day = 1;
				}
			} else if (month == 4 || month == 6 || month == 9 || month == 11) {
				if (day > 30) {
					month++;
					day = 1;
				}
			} else if (month == 2) {
				if (year % 4 == 0) {
					if (day > 29) {
						month++;
						day = 1;
					}
				} else if (day > 28) {
					month++;
					day = 1;
				}
			} else if (month == 12 && day > 31) {
				month++;
				if (month > 12) {
					year++;
					month = 1;
				}
				day = 1;
			}
		}
		String date = String.valueOf(month) + "/" + day + "/" + year;
		String status = "On Schedule!";
		if (firstDate != null) {
			int firstMonth = Integer.parseInt(firstDate.split("/")[0]);
			int firstDay = Integer.parseInt(firstDate.split("/")[1]);
			int firstYear = Integer.parseInt(firstDate.split("/")[2]);
			if (firstYear > year) {
				status = "Ahead!";
			} else if (firstYear == year) {
				if (firstMonth > month) {
					status = "Ahead!";
				} else if (firstMonth == month) {
					if (firstDay > day) {
						status = "Ahead!";
					} else if (firstDay == day) {
						status = "On Schedule!";
					} else {
						status = "Behind!";
					}
				} else {
					status = "Behind!";
				}
			} else {
				status = "Behind!";
			}
		}
		if (total > 0) {
			this.infoPanel
					.addLine(
							new TextBit[] {
									new TextBit(new Color(255, 174, 0), new Font("Helvetica", 0, 18),
											"Lessons Complete: " + complete + "/" + total + " ("
													+ (int) Math.floor(complete / total * 100.0D) + "%)",
											null, null) });
		} else {
			this.infoPanel.addLine(new TextBit[] { new TextBit(new Color(255, 174, 0), new Font("Helvetica", 0, 18),
					"Lessons Complete: " + complete + "/" + total + " (100%)", null, null) });
		}
		this.infoPanel.addLine(new TextBit[] { new TextBit(new Color(255, 174, 0), new Font("Helvetica", 0, 18),
				"Average Score: " + avgScore + "%", null, null) });
		this.infoPanel.addLine(new TextBit[] { new TextBit(new Color(255, 174, 0), new Font("Helvetica", 0, 18),
				"Highest Score: " + highestScore + "%", null, null) });
		this.infoPanel.addLine(new TextBit[] { new TextBit(new Color(255, 174, 0), new Font("Helvetica", 0, 18),
				"Lowest Score: " + lowestScore + "%", null, null) });
		this.infoPanel.addLine(new TextBit[] { new TextBit(new Color(255, 174, 0), new Font("Helvetica", 0, 18),
				"Original Graduation Date: ", null, null) });
		this.infoPanel.addLine(new TextBit[] { new TextBit(new Color(255, 174, 0), new Font("Helvetica", 0, 18),
				"Estimated Graduation Date: " + date, null, null) });
		if (status.equalsIgnoreCase("Behind!")) {
			this.infoPanel.addLine(new TextBit[] {
					new TextBit(new Color(220, 0, 0), new Font("Helvetica", 0, 18), status, null, null) });
		} else if (status.equalsIgnoreCase("Ahead!")) {
			this.infoPanel.addLine(new TextBit[] {
					new TextBit(new Color(0, 220, 0), new Font("Helvetica", 0, 18), status, null, null) });
		} else if (status.equalsIgnoreCase("On Schedule!")) {
			this.infoPanel.addLine(new TextBit[] {
					new TextBit(new Color(255, 174, 0), new Font("Helvetica", 0, 18), status, null, null) });
		}
	}

	/*private void updateInfoPanel(Button button) {
		this.infoPanel.clear();
		String[] parts = button.getId().split("�");
		this.infoPanel.addLine(new TextBit[] {
				new TextBit(app, Color.ORANGE, new Font("Helvetica", 0, 18), "Title: " + parts[0], null, null) });
		this.infoPanel.addLine(new TextBit[] {
				new TextBit(app, Color.ORANGE, new Font("Helvetica", 0, 18), "Date: " + parts[1], null, null) });
		this.infoPanel.addLine(new TextBit[] {
				new TextBit(app, Color.ORANGE, new Font("Helvetica", 0, 18), "Score: " + parts[2] + "%", null, null) });
	}*/

	public void render(Graphics g) {
		animate();
		this.animation.render(g);
		for (int i = 0; i < this.points.length - 1; i++) {
			g.setColor(this.lineColor);
			g.drawLine(this.points[i].getX() + this.x, this.y + this.height - this.cushion - this.points[i].getY(),
					this.points[i + 1].getX() + this.x,
					this.y + this.height - this.cushion - this.points[i + 1].getY());
		}
		g.setColor(this.titleColor);
		g.setFont(this.titleFont);
		int wordWidth = g.getFontMetrics().stringWidth(this.title);
		g.drawString(this.title, this.x + this.width / 2 - wordWidth / 2, this.y);
	}

	public boolean onClick(int button, Point p) {
		if (this.getBounds().contains(p)) {
			runClick();
			return true;
		}
		return false;
	}

	public void onType(KeyEvent e) {
	}

	public void onKeyPressed(KeyEvent e) {
	}

	public void onMouseMove(Point p) {
		if (this.getBounds().contains(p)) {
			if (!this.hovering) {
				this.hovering = true;
				runOnHover();
			}
		} else if (this.hovering) {
			this.hovering = false;
			runOnStopHover();
		}
	}
	
	@Override
	public void onScroll(int amount) {
		
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public void update() {
		animate();
	}

	private void animate() {
		this.animation.run();
	}

	public boolean onMouseDown(int button, Point p) {
		return false;
	}

	public boolean onMouseUp(int button, Point p) {
		return false;
	}

	@Override
	public void onKeyReleased(KeyEvent paramKeyEvent) {
		// TODO Auto-generated method stub
		
	}
}
