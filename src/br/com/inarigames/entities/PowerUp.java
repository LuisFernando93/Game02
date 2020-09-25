package br.com.inarigames.entities;

public class PowerUp extends Entity {

	public PowerUp(double x, double y, int width, int height) {
		super(x, y, width, height);
		this.sprite = Entity.POWERUP_DRAGONFRUIT_EN;
	}

}
