package windowAPI.test;

public class Main {
	private static Engine engine;
	private static Window window;

	public static void main(String[] args) {
		window = new Window();
		engine = new Engine(window);
		engine.start();
	}
	
	public static Window getWindow() {
		return window;
	}
	
	public static Engine getEngine() {
		return engine;
	}
	
	public static void stop() {
		window.dispose();
		engine.stop();
	}
}
