package windowAPI;

import java.awt.Font;

public class Fonts {
	public static Font plain;
	public static Font italic;
	public static Font bold;
	
	public Fonts() {
		plain = new Font("Helvetica", Font.PLAIN, 14);
		bold = new Font("Helvetica", Font.BOLD, 14);
		italic = new Font("Helvetica", Font.ITALIC, 14);
	}
}
