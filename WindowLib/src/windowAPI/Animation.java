package windowAPI;


import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Animation {
	private BufferedImage[] images;

	private boolean isRunning;

	private BufferedImage frame;

	private boolean terminated;

	private int x;

	private int y;

	private int width;

	private int height;

	private int inc;

	private int counter;

	private int index;

	public Animation(int inc, int x, int y, int width, int height, BufferedImage... images) {
		this.images = images;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.inc = inc;
		this.counter = 0;
		this.index = 0;
		this.frame = images[this.index];
		this.terminated = false;
	}

	public BufferedImage[] getImages() {
		return this.images;
	}

	public BufferedImage getFrame(int index) {
		return this.images[index];
	}

	public void render(Graphics g) {
		if (this.isRunning)
			g.drawImage(this.frame, this.x, this.y, this.width, this.height, null);
	}

	public void update() {
		this.frame = this.images[this.index];
		this.counter++;
		if (this.counter > this.inc) {
			this.counter = 0;
			this.index++;
			if (this.index >= this.images.length)
				if (this.terminated) {
					cancel();
					onEnd();
				} else {
					this.index = 0;
				}
		}
	}

	public void run() {
		this.isRunning = true;
		this.terminated = false;
	}

	public void end() {
		this.terminated = true;
	}

	public void cancel() {
		this.isRunning = false;
	}

	public void onEnd() {
	}

	public int getX() {
		return this.x;
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

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public boolean isRunning() {
		return this.isRunning;
	}
}
