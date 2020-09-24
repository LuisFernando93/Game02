package br.com.inarigames.world;

public class WallTile extends Tile {

	public WallTile(int x, int y) {
		super(x, y);
		this.sprite = Tile.TILE_WALL;
	}

}
