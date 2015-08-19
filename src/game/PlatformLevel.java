package game;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import engine.components.ColliderComponent;
import engine.components.CollisionComponent;
import engine.components.SpriteComponent;
import engine.core.entity.Entity;
import engine.parsing.json.JSON;
import engine.parsing.json.JSONValue;
import engine.rendering.ArrayBitmap;
import engine.rendering.Color;
import engine.rendering.IRenderDevice;
import engine.rendering.SpriteSheet;
import engine.space.ISpatialStructure;
import engine.util.factory.SpriteSheetFactory;

public class PlatformLevel {
	private ISpatialStructure<Entity> structure;
	private SpriteSheetFactory sprites;
	private Entity player;
	private JSON entitySpec;
	
	public PlatformLevel(ISpatialStructure<Entity> structure, SpriteSheetFactory sprites,
			JSON entitySpec) {
		this.structure = structure;
		this.sprites = sprites;
		this.player = null;
		this.entitySpec = entitySpec;
	}
	
	public Entity getPlayer() {
		return player;
	}
	
	public void loadLevel(String fileName) throws IOException
	{
		SpriteSheet level = sprites.get(fileName, 1, 1, 0, IRenderDevice.FILTER_NEAREST);
		ArrayBitmap levelData = level.getSheet().getPixels();
		
		for(int layer = 0; layer < level.getNumSprites(); layer++) {
			for(int j = 0; j < level.getSpriteHeight(); j++) {
				for(int i = 0; i < level.getSpriteWidth(); i++) {
					int x = level.getStartX(layer) + i;
					int y = level.getStartY(layer) + j;
					int pixel = levelData.get(x, y);
					
					int r = Color.getARGBComponent(pixel, 1);
					int g = Color.getARGBComponent(pixel, 2);
					int b = Color.getARGBComponent(pixel, 3);
					int a = Color.getARGBComponent(pixel, 0);
					loadEntity(i, j, r, g, b, a);
				}
			}
		}
	}
	
	private void loadEntity(int x, int y, int r, int g, int b, int a) throws IOException
	{
		double entityWidth = 0.1;
		double entityHeight = 0.133333333333333;
		
		if(b == 0) {
			return;
		}
		
		Entity e = new Entity(structure, entityWidth * x, -entityHeight * y, 0);
		if(g >= 128) {
			new ColliderComponent(e);
			new CollisionComponent(e);
		}
		
		JSONValue entityDescription = entitySpec.get().asObject().get("" + b);
		if(entityDescription == null) {
			return;
		}
		
		if(b == 255) {
			player = e;
		}
		
		Iterator<Entry<String, JSONValue>> it = entityDescription.asObject().entrySet().iterator();
		while(it.hasNext()) {
			Entry<String, JSONValue> entry = it.next();
			Map<String, JSONValue> parameters = entry.getValue().asObject();
			
			switch(entry.getKey()) {
			case "sprite":
				new SpriteComponent(e, entityWidth, entityHeight, sprites.get(
						parameters.get("fileName").asString(), 1, 1, 0,
						IRenderDevice.FILTER_LINEAR), 0, Color.WHITE);
				break;
			}
		}
	}
}
