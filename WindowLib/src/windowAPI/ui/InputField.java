package windowAPI.ui;


import windowAPI.ui.geometry.Transform;
import windowAPI.ui.gfx.Pattern;

public abstract class InputField extends UIObject {
	protected String id;

	protected boolean hovering = false;

	public InputField(String id, Transform transform, int width, int height, Pattern pattern) {
		super(id, transform, width, height, pattern);
		this.id = id;
		this.width = width;
		this.height = height;
	}
	
	public abstract void setPointerCursor();
	
	public abstract void setTypeCursor();
}
