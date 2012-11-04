package world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import util.Vector;
import client.Camera;

public class Water extends Tile {
	
	double period = 2;
	double time = 0;
	
	public Water(int y, int x, Room owner){
		super(new Vector(x*Tile.SIZE,y*Tile.SIZE), TileType.STONE, owner);
	}
	
	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam) {
		AffineTransform prev = g.getTransform();
		
		g.translate(cam.xTranslatePosition(position.x), cam.yTranslatePosition(position.y));
		g.scale(cam.scale(), cam.scale());
		g.setClip(0, 0, Tile.SIZE, Tile.SIZE);
		
		time = (time + elapsedSeconds)%period;
		double shift = time/period * Tile.SIZE;
		
		g.setColor(new Color(30,100,200,255));
		g.fillRect(0, 0, Tile.SIZE+1, Tile.SIZE+1);
		
		g.setColor(new Color(20,128,255,255));
		
		g.drawArc((int)Math.round(-Tile.SIZE*3/2.+shift), -Tile.SIZE/2, Tile.SIZE, Tile.SIZE, 270, 90);
		g.drawArc((int)Math.round(-Tile.SIZE/2.+shift), Tile.SIZE/2, Tile.SIZE, Tile.SIZE, 90, 90);
		
		g.drawArc((int)Math.round(-Tile.SIZE/2.+shift), -Tile.SIZE/2, Tile.SIZE, Tile.SIZE, 270, 90);
		g.drawArc((int)Math.round(Tile.SIZE/2.+shift), Tile.SIZE/2, Tile.SIZE, Tile.SIZE, 90, 90);
		
		g.setClip(null);
		
		
		g.setTransform(prev);
	}

}
