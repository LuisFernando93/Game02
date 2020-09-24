package br.com.inarigames.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import br.com.inarigames.main.Game;

public class Tile {

	protected static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(0, 0, 32, 32);
	protected static BufferedImage TILE_WALL = Game.spritesheet.getSprite(32, 0, 32, 32);
	
	protected BufferedImage sprite;
	private int x, y;
	
	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void render(Graphics graphics) {
		graphics.drawImage(sprite, Camera.offsetX(this.x) , Camera.offsetY(this.y), null);
	}
}
