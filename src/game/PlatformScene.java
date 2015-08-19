package game;

import java.io.IOException;
import java.text.ParseException;

import engine.audio.IAudioDevice;
import engine.core.Scene;
import engine.core.entity.Entity;
import engine.input.IInput;
import engine.parsing.json.JSON;
import engine.rendering.Color;
import engine.rendering.IRenderContext;
import engine.rendering.IRenderDevice;
import engine.space.AABB;
import engine.space.QuadTree;
import engine.util.factory.SpriteSheetFactory;
import engine.util.factory.TextureFactory;

public class PlatformScene extends Scene {
	private Entity player;
	public PlatformScene(IInput input, IRenderDevice device,
			IAudioDevice audioDevice) throws IOException, ParseException {
		super(new QuadTree<Entity>(new AABB(-1, -1, 1, 1), 8));
		SpriteSheetFactory sprites = new SpriteSheetFactory(
				new TextureFactory(device, "./res/"));
		PlatformLevel level = new PlatformLevel(getStructure(), sprites,
				new JSON("./res/entities.json"));
		level.loadLevel("testLevel.png");
		player = level.getPlayer();
	}

	@Override
	public boolean update(double delta) {
		super.updateRange(delta, new AABB(-2, -2, 2, 2));
		return false;
	}

	@Override
	public void render(IRenderContext target) {
		target.clear(Color.BLACK);
		super.renderRange(target, player.getX(), player.getY());
	}
}
