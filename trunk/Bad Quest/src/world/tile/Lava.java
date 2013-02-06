package world.tile;

import java.awt.Color;

import world.Room;

public class Lava extends BubblingTile {
	public Lava(int y, int x, Room owner){
		super(y, x, TileType.LAVA, owner);
	}
	@Override
	public void initialize() {
		in = new Color(0xBB1111);
		out = new Color(0xFF4D00);
		c1 = new Color(0xFF9900);
		c2 = new Color(0xFF6600);
	}
}
