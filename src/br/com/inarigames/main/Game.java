package br.com.inarigames.main;

import javax.swing.JFrame;

public class Game implements Runnable {
	
	private static JFrame frame;
	private Thread thread;
	private boolean isRunning = false;

	public Game() {
		
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

	@Override
	public void run() {
		
	}
	

}
