package windowAPI.ui;

import windowAPI.ui.geometry.Transform;
import windowAPI.ui.gfx.Pattern;

public abstract class NumberInputField extends TextInputField {
	public NumberInputField(String id, Transform transform, int width, int height, String preview, String value, boolean writable, boolean protectedDisplay, TextArea toWriteTo, Pattern pattern, char... added) {
		super(id, transform, width, height, preview, value, writable, protectedDisplay, toWriteTo, pattern);
		this.allowed = new char[10 + added.length];
		this.allowed[0] = '0';
		this.allowed[1] = '1';
		this.allowed[2] = '2';
		this.allowed[3] = '3';
		this.allowed[4] = '4';
		this.allowed[5] = '5';
		this.allowed[6] = '6';
		this.allowed[7] = '7';
		this.allowed[8] = '8';
		this.allowed[9] = '9';
		for (int i = 10; i < added.length + 10; i++)
			this.allowed[i] = added[i - 10];
	}
}
