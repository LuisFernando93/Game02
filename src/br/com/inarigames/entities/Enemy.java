package br.com.inarigames.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

import br.com.inarigames.main.Game;
import br.com.inarigames.world.AStar;
import br.com.inarigames.world.Camera;
import br.com.inarigames.world.Node;
import br.com.inarigames.world.Vector2i;
import br.com.inarigames.world.World;

public class Enemy extends Entity {

	private int right_dir = 0, left_dir = 1, up_dir = 2, down_dir = 3;
	private BufferedImage spriteRight, spriteLeft, spriteUp, spriteDown;
	private int dir = Game.random.nextInt(4);
	private int maskx = 5, masky = 3, maskw = 23, maskh = 26;
	private int speed = 1;
	private int life = 1;
	private BufferedImage[] vulnerableEnemy, vulnerableEnemyWhite;
	private int frames = 0, maxFrames = 12, imageIndex = 0, maxIndex = 3; 
	private int blinkFrames = 0, maxBlinkFrames = 20;
	private boolean vulnerableWhite = false;
	
	private List<Node> path;
	
	public Enemy(double x, double y, int width, int height) {
		super(x, y, width, height);
		this.depth = 1;
		this.spriteRight = Entity.ENEMY_RIGHT_EN;
		this.spriteLeft = Entity.ENEMY_LEFT_EN;
		this.spriteUp = Entity.ENEMY_UP_EN;
		this.spriteDown = Entity.ENEMY_DOWN_EN;
		vulnerableEnemy = new BufferedImage[4];
		vulnerableEnemyWhite = new BufferedImage[4];
		
		for (int i = 0; i < 4; i++) {
			vulnerableEnemy[i] = Game.spritesheet.getSprite(6*32 + (32*i), 32, 32, 32);
			vulnerableEnemyWhite[i] = Game.spritesheet.getSprite(6*32 + (32*i), 64, 32, 32);
		}
		
	}

	public boolean isColliding(int xnext, int ynext) {
		Rectangle enemyCurrent = new Rectangle(xnext+maskx,ynext+masky,maskw,maskh);
		
		for (Enemy enemy : Game.enemies) {
			if (enemy == this) {
				continue;
			}
			Rectangle targetEnemy = new Rectangle(enemy.getX()+maskx,enemy.getY()+masky,maskw,maskh);
			if (enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isCollidingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX()+maskx,this.getY()+masky,maskw,maskh);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 32, 32);
		if (enemyCurrent.intersects(player)) {
			return true;
		}
		return false;
	}
	
	private void followPath(List<Node> path) {
		if(path != null) {
			if (path.size() > 0) {
				Vector2i target = path.get(path.size() - 1).getTile();
				if (this.x < target.getX() * World.TILE_SIZE && !isColliding(this.getX() + 1, this.getY()) && World.isFree(this.getX()+1, this.getY())) {
					dir = right_dir;
					this.x += speed;
				} else if (this.x > target.getX() * World.TILE_SIZE && !isColliding(this.getX() - 1, this.getY()) && World.isFree(this.getX()-1, this.getY())) {
					dir = left_dir;
					this.x -= speed;
				} else if (this.y < target.getY() * World.TILE_SIZE && !isColliding(this.getX(), this.getY()+1) && World.isFree(this.getX(), this.getY()+1)) {
					dir = down_dir;
					this.y += speed;
				} else if (this.y > target.getY() * World.TILE_SIZE && !isColliding(this.getX(), this.getY()-1) && World.isFree(this.getX(), this.getY()-1)) {
					dir = up_dir;
					this.y -= speed;
				}
				
				if(this.getX() == target.getX() * World.TILE_SIZE && this.getY() == target.getY() * World.TILE_SIZE) {
					path.remove(path.size() - 1);
				}
			}
		}
	}
	
	private void moveAStar() {
		if (path == null || path.size() == 0) {
			Vector2i start = new Vector2i(this.getX()/World.TILE_SIZE, this.getY()/World.TILE_SIZE);
			Vector2i end = new Vector2i(Game.player.getX()/World.TILE_SIZE, Game.player.getY()/World.TILE_SIZE);
			path = AStar.findPath(Game.world, start, end);
		}
		if (new Random().nextInt(100) < 90) {
			followPath(path);
		}
		if (new Random().nextInt(100) < 10) {
			Vector2i start = new Vector2i(this.getX()/32, this.getY()/32);
			Vector2i end = new Vector2i(Game.player.getX()/32, Game.player.getY()/32);
			path = AStar.findPath(Game.world, start, end);
		}
		
	}
	
	private void checkIfAttack() {
		if(isCollidingWithPlayer()) {
			if (Game.isAttackMode()) {
				this.life--;
			} else {
				//atacar
				System.out.println("atacar");
			}
			
		}
	}
	
	private void checkLife() {
		if (this.life == 0) {
			Game.toRemove.add(this);
		}
	}
	
	private void regularAnimation(Graphics graphics) {
		if (dir == right_dir) {
			graphics.drawImage(spriteRight, Camera.offsetX(this.getX()), Camera.offsetY(this.getY()), null);
		} else if (dir == left_dir) {
			graphics.drawImage(spriteLeft, Camera.offsetX(this.getX()), Camera.offsetY(this.getY()), null);
		} else if (dir == up_dir) {
			graphics.drawImage(spriteUp, Camera.offsetX(this.getX()), Camera.offsetY(this.getY()), null);
		} else if (dir == down_dir) {
			graphics.drawImage(spriteDown, Camera.offsetX(this.getX()), Camera.offsetY(this.getY()), null);
		}
	}
	
	private void vulnerableAnimation(Graphics graphics) {
		
		if (!vulnerableWhite) {
			graphics.drawImage(vulnerableEnemy[imageIndex], Camera.offsetX(this.getX()), Camera.offsetY(this.getY()), null);
		} else graphics.drawImage(vulnerableEnemyWhite[imageIndex], Camera.offsetX(this.getX()), Camera.offsetY(this.getY()), null);
		
		if (Game.isBlankState()) {
			blinkFrames++;
			if (blinkFrames == maxBlinkFrames) {
				blinkFrames = 0;
				if (vulnerableWhite == false) {
					vulnerableWhite = true;
				} else vulnerableWhite = false;
				
			}
		}
		frames++;
		if (frames == maxFrames) {
			frames = 0;
			imageIndex++;
			if (imageIndex == maxIndex) {
				imageIndex = 0;
			}
		}
		
	}
	
	public void update() {
		if (!Game.isAttackMode()) {
			moveAStar();
		}
		checkIfAttack();
		checkLife();
	}
	
	public void render(Graphics graphics) {
		
		if (!Game.isAttackMode()) {
			regularAnimation(graphics);
		} else {
			vulnerableAnimation(graphics);
		}
	}
	
}
