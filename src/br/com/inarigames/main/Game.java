package br.com.inarigames.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import br.com.inarigames.entities.Enemy;
import br.com.inarigames.entities.Entity;
import br.com.inarigames.entities.Player;
import br.com.inarigames.graphics.Spritesheet;
import br.com.inarigames.world.World;

public class Game extends Canvas implements Runnable, KeyListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private Thread thread;
	private boolean isRunning = false;
	private double amountOfUpdates = 60.0;
	public static Random random;
	
	public static final int WIDTH = 480;
	public static final int HEIGHT = 640;
	public static final int SCALE = 1;
	
	private BufferedImage image;
	
	public static World world;
	
	public static Player player;
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<Entity> toRemove = new ArrayList<Entity>();
	public static Spritesheet spritesheet =  new Spritesheet("/spritesheet.png");
	
	private static String gameState = "NORMAL";
	
	private static int level = 1;
	private static int fruitCountTotal = 0;
	private static int fruitCount = 0;

	public Game() {
		
		addKeyListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		
		//initializing objects
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		player = new Player(0, 0, 32, 32);
		entities.add(player);
		random = new Random();
		world = new World("/level" + level + ".png");
	}
	
	public static void incrementFruitCountTotal() {
		fruitCountTotal++;
	}
	
	public static void incrementFruitCount() {
		fruitCount++;
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	public void initFrame() {
		
		frame = new JFrame("Game #2");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			System.out.println("erro na thread");
			e.printStackTrace();
		}
	}
	
	private void update() {
		switch (Game.gameState) {
		case "NORMAL":
			for (Entity entity : entities) {
				entity.update();
			}
			entities.removeAll(toRemove);
			enemies.removeAll(toRemove);
			toRemove.clear();
			break;

		default:
			throw new IllegalArgumentException("Unexpected value: " + Game.gameState);
		}
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics graphics = image.getGraphics();
		graphics.setColor(new Color(0,0,0));
		graphics.fillRect(0, 0, WIDTH, HEIGHT);
		
		world.render(graphics);
		Collections.sort(entities, Entity.entitySorter);
		for (Entity entity : entities) {
			entity.render(graphics);
		}
		for (Entity entity : entities) {
			entity.render(graphics);
		}
		
		graphics.dispose();
		graphics = bs.getDrawGraphics();
		graphics.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		graphics.setColor(Color.white);
		graphics.setFont(new Font("arial", Font.BOLD, 18));
		String score = "Morangos: " + fruitCount + "/" + fruitCountTotal;
		graphics.drawString(score, 30, 30);
		bs.show();
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double ns = 1000000000 / amountOfUpdates;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime)/ns;
			lastTime = now;
			if (delta >= 1) {
				update();
				render();
				frames++;
				delta--;
			}
			
			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS = " + frames);
				frames = 0;
				timer = System.currentTimeMillis();
			}
		}
		stop();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (Game.gameState) {
		case "NORMAL":
			if (e.getKeyCode() == KeyEvent.VK_UP ||
					e.getKeyCode() == KeyEvent.VK_W) {
				player.setUp(true);
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN ||
					e.getKeyCode() == KeyEvent.VK_S) {
				player.setDown(true);
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT ||
					e.getKeyCode() == KeyEvent.VK_D) {
				player.setRight(true);
			}
			if (e.getKeyCode() == KeyEvent.VK_LEFT ||
					e.getKeyCode() == KeyEvent.VK_A) {
				player.setLeft(true);
			}
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (Game.gameState) {
		case "NORMAL":
			if (e.getKeyCode() == KeyEvent.VK_UP ||
					e.getKeyCode() == KeyEvent.VK_W) {
				player.setUp(false);
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN ||
					e.getKeyCode() == KeyEvent.VK_S) {
				player.setDown(false);
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT ||
					e.getKeyCode() == KeyEvent.VK_D) {
				player.setRight(false);
			}
			if (e.getKeyCode() == KeyEvent.VK_LEFT ||
					e.getKeyCode() == KeyEvent.VK_A) {
				player.setLeft(false);
			}
			break;
		}
	}
	

}
