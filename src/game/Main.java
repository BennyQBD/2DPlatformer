package game;

import java.io.IOException;
import java.text.ParseException;

import engine.core.CoreEngine;
import engine.rendering.IDisplay;
import engine.rendering.opengl.OpenGLDisplay;
import engine.util.Debug;

public class Main {
	public static void main(String[] args) throws IOException, ParseException {
		Debug.init(true, true);
		IDisplay display = new OpenGLDisplay(640, 480, "My Display");
		CoreEngine engine = new CoreEngine(display, new PlatformScene(
				display.getInput(), display.getRenderDevice(),
				display.getAudioDevice()), 60.0);
		engine.start();
		display.dispose();
	}
}
