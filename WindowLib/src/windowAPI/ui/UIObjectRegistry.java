package windowAPI.ui;


import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class UIObjectRegistry {
	private List<UIObject> registeredObjects = new ArrayList<UIObject>();
	private List<UIObject> bufferedObjects = new ArrayList<UIObject>();
	private UIObject selected;
	
	public void registerObject(UIObject object) {
		registeredObjects.add(object);
	}
	
	public void unregisterObject(UIObject object) {
		bufferedObjects.add(object);
	}
	
	public boolean isRegistered(UIObject object) {
		if(registeredObjects.contains(object)) {
			return true;
		}
		return false;
	}
	
	public void render(Graphics g) {
		for(UIObject each:registeredObjects) {
			each.render(g);
		}
	}
	
	public void update() {
		if(bufferedObjects.size() <= 0) {
			return;
		}
		registeredObjects.removeAll(bufferedObjects);
		bufferedObjects.clear();
		for(UIObject each:registeredObjects) {
			each.update();
		}
	}
	
	public List<UIObject> getObjects(){
		return registeredObjects;
	}
	
	public void setFocusedObject(UIObject object) {
		selected = object;
	}
	
	public UIObject getFocusedObject() {
		return selected;
	}
}
