package br.com.inarigames.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import br.com.inarigames.main.Game;
import br.com.inarigames.world.Camera;
import br.com.inarigames.world.World;

public class Player extends Entity {
	
	private int speed = 2;
	
	private int dir;
	private int right_dir = 1, left_dir = 2, up_dir = 3, down_dir = 4;
	private boolean right, left, up, down; 
	private boolean isOpening = true, moved = false;
	private BufferedImage[] rightPlayer, leftPlayer, upPlayer, downPlayer;
	private int frames = 0, maxFrames = 5, imageIndex = 0, maxIndex = 3; 

	public Player(double x, double y, int width, int height) {
		super(x, y, width, height);
		this.depth = 2;
		dir = right_dir;
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		upPlayer = new BufferedImage[4];
		downPlayer = new BufferedImage[4];
		for (int i = 0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(64 + (32*i), 0, 32, 32);
			leftPlayer[i] = Game.spritesheet.getSprite(64 + (32*i),32, 32, 32);
			upPlayer[i] = Game.spritesheet.getSprite(64 + (32*i), 64, 32, 32);
			downPlayer[i] = Game.spritesheet.getSprite(64 + (32*i), 96, 32, 32);
		}
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
			dir = right_dir;
			x+=speed;
			moved = true;
		}
		if(left && (x - speed >= 0) && World.isFree(this.getX()-speed, this.getY())) {
			dir = left_dir;
			x-=speed;
			moved = true;
		}
		if(up && (y - speed >= 0) && World.isFree(this.getX(), this.getY()-speed)) {
			dir = up_dir;
			y-=speed;
			moved = true;
		}
		if(down && (this.y + speed + this.height <= World.getHeight()*World.TILE_SIZE) && World.isFree(this.getX(), this.getY()+speed)) {
			dir = down_dir;
			y+=speed;
			moved = true;
		}
		
		if (isOpening) {
			if(moved) {
				frames++;
				if (frames == maxFrames) {
					frames = 0;
					imageIndex++;
					if (imageIndex == maxIndex) {
						imageIndex--;
						isOpening = false;
					}
				}
				moved = false;
			}
		} else {
			if(moved) {
				frames++;
				if (frames == maxFrames) {
					frames = 0;
					imageIndex--;
					if (imageIndex == 0) {
						isOpening = true;
					}
				}
				moved = false;
			}
		}
	}
	
	private void checkIfEatFruit() {
		for (Entity entity : Game.entities) {
			
			if (entity instanceof Fruit) {
				if (Entity.isColliding(entity, Game.player)) {
					//come morango
					Game.incrementFruitCount();
					Game.toRemove.add(entity);
					return;
				}
			}
			
			if (entity instanceof PowerUp) {
				if (Entity.isColliding(entity, Game.player)) {
					//come pitaya
					Game.toRemove.add(entity);
					return;
				}
			}
			
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
		checkIfEatFruit();
		updateCamera();
	}
	
	public void render(Graphics graphics) {
		if (dir == right_dir) {
			graphics.drawImage(rightPlayer[imageIndex], Camera.offsetX(this.getX()), Camera.offsetY(this.getY()), null);
		} else if (dir == left_dir) {
			graphics.drawImage(leftPlayer[imageIndex], Camera.offsetX(this.getX()), Camera.offsetY(this.getY()), null);
		} else if (dir == up_dir) {
			graphics.drawImage(upPlayer[imageIndex], Camera.offsetX(this.getX()), Camera.offsetY(this.getY()), null);
		} else if (dir == down_dir) {
			graphics.drawImage(downPlayer[imageIndex], Camera.offsetX(this.getX()), Camera.offsetY(this.getY()), null);
		}
	}
	
}
