package windowAPI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.TextField;

/**
 *Provides a standard JTextField with a piece of text (the prompt) pre-rendered to provide a clue to the user as to what should be input. 
 **/
public class PromptTextField extends TextField {

	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;
	
	private String prompt = "";
	private Color promptColor = new Color(200, 200, 200, 80);

	public PromptTextField() {
		
	}
	
	public PromptTextField(int columns) {
		super(columns);
	}
	
	public PromptTextField(String prompt) {
		this.prompt = prompt;
	}
	
	public PromptTextField(int columns, String prompt) {
		super(columns);
		this.prompt = prompt;
	}
	
	public void setPromptColor(Color color) {
		this.promptColor = color;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);

		if (getText().isEmpty()) {// && !(FocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == this)
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setFont(getFont().deriveFont(Font.ITALIC));
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			int fontHeight = g2.getFontMetrics(getFont()).getHeight();
			//int heightFromTop = this.getHeight()-fontHeight-g2.getFontMetrics(getFont()).getDescent();
			g2.setColor(promptColor);
			g2.drawString(prompt, 5, fontHeight);
			g2.dispose();
		}
	}
}