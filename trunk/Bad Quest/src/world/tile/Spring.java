package world.tile;

import java.awt.Color;

import world.Room;

public class Spring extends BubblingTile {
	public Spring(int y, int x, Room owner){
		super(y, x, TileType.SPRING, owner);
	}
	@Override
	public void initialize() {
		in = new Color(0x0B445B);
		out = new Color(0x298CCD);
		c1 = new Color(0x94C9FC);
		c2 = new Color(0x218DF8);
		maxBubbleCount = 4;
		bubbleChance = .5;
	}
}
