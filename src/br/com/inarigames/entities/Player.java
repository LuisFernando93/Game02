package br.com.inarigames.entities;

import br.com.inarigames.main.Game;
import br.com.inarigames.world.Camera;
import br.com.inarigames.world.World;

public class Player extends Entity {
	
	private int speed = 2;
	
	private boolean right, left, up, down;

	public Player(double x, double y, int width, int height) {
		super(x, y, width, height);
		this.depth = 1;
		this.sprite = Entity.PLAYER_EN;
	}
	
	public void setRight(boolean right) {
		this.right = right;
	}
	
	public void setLeft(boolean left) {
		this.left = left;
	}
	
	public void setUp(boolean up) {
		this.up = up;
	}
	
	public void setDown(boolean down) {
		this.down = down;
	}
	
	private void movePlayer() {
		
		if(right && (this.x + speed + this.width <= World.getWidth()*World.TILE_SIZE) && World.isFree(this.getX()+speed, this.getY())) {
			x+=speed;
		}
		if(left && (x - speed >= 0) && World.isFree(this.getX()-speed, this.getY())) {
			x-=speed;
		}
		if(up && (y - speed >= 0) && World.isFree(this.getX(), this.getY()-speed)) {
			y-=speed;
		}
		if(down && (this.y + speed + this.height <= World.getHeight()*World.TILE_SIZE) && World.isFree(this.getX(), this.getY()+speed)) {
			y+=speed;
		}
		
	}
	
	private void updateCamera() {
		int cameraX = Camera.clamp(this.getX() - (Game.WIDTH)/2, 0, World.getWidth()*World.TILE_SIZE - Game.WIDTH);
		int cameraY = Camera.clamp(this.getY() - (Game.HEIGHT)/2, 0, World.getHeight()*World.TILE_SIZE - Game.HEIGHT);
		Camera.setX(cameraX);
		Camera.setY(cameraY);
	}
	
	public void update() {
		movePlayer();
		updateCamera();
	}
	
}
