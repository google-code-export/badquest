package world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import util.Vector;
import client.Camera;

public class Stone extends Tile{
	public Stone(int x, int y){
		super(new Vector(x*Tile.SIZE,y*Tile.SIZE), TileType.STONE);
		color = new Color(33,45,52);
	}
	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam){
		AffineTransform prev = g.getTransform();
		
		g.translate(cam.xTranslatePosition(position.x), cam.yTranslatePosition(position.y));
		g.scale(cam.scale(), cam.scale());
		
		g.setClip(0, 0, Tile.SIZE, Tile.SIZE);
		
		g.setColor(color.darker());
		g.fillRect(0, 0, Tile.SIZE, Tile.SIZE);
		g.setColor(color.brighter());
		g.fillRect(0,0,Tile.SIZE-1,Tile.SIZE-1);
		g.setColor(color);
		g.fillRect(1, 1, Tile.SIZE-2, Tile.SIZE-2);
		
//		g.setColor(color.brighter().brighter());
//		g.drawLine(0, Tile.SIZE/2, Tile.SIZE/2, 0);
//		g.drawLine(Tile.SIZE, Tile.SIZE/2, Tile.SIZE/2, Tile.SIZE);
		
		g.setClip(null);
		g.setTransform(prev);
	}
}
