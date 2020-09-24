package br.com.inarigames.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import br.com.inarigames.main.Game;
import br.com.inarigames.world.Camera;

public class Entity {

	protected static BufferedImage PLAYER_EN = Game.spritesheet.getSprite(2*32, 0, 32, 32);
	
	protected double x, y;
	protected BufferedImage sprite;
	protected int depth, width, height;
	
	public Entity(double x, double y, int width, int height) {
		this.depth = 0;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public int getX() {
		return (int)this.x;
	}
	
	public int getY() {
		return (int)this.y;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getDepth() {
		return this.depth;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public static boolean isColliding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX(), e1.getY(), e1.width, e1.height);
		Rectangle e2Mask = new Rectangle(e2.getX(), e2.getY(), e2.width, e2.height);
		if (e1Mask.intersects(e2Mask)) {
			return true;
		}
		return false;
	}
	
	public void update() {
		
	}
	
	public void render(Graphics graphics) {
		graphics.drawImage(sprite, Camera.offsetX(this.getX()), Camera.offsetY(this.getY()), null);
	}
}
