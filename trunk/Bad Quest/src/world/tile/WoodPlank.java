package world.tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

import util.Vector;
import world.Room;

import client.Camera;

public class WoodPlank extends Tile {
	private Color fill = new Color(117,101,53);
	private Color outline = fill.darker();
	public WoodPlank(int y, int x, Room owner){
		super(new Vector(x*Tile.SIZE,y*Tile.SIZE), TileType.WOOD_PLANK, owner);
	}
	
	@Override
	public void update(double elapsedSeconds) {
		
	}
	
	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam) {
		AffineTransform prev = g.getTransform();
		Stroke pStroke = g.getStroke();
		
		g.translate(cam.xTranslatePosition(position.x), cam.yTranslatePosition(position.y));
		g.scale(cam.scale(), cam.scale());
		g.setClip(0, 0, Tile.SIZE, Tile.SIZE);
		
		g.setColor(fill);
		g.fillRect(0,0,Tile.SIZE+1,Tile.SIZE+1);
		
		g.setColor(outline);
		for(int i = 0; i < 5; i++)
			g.drawLine(0, i*Tile.SIZE/4, Tile.SIZE, i*Tile.SIZE/4);
		
		for(int i = 0; i < 2; i++){
			g.drawLine(Tile.SIZE/2, (2*i+1)*Tile.SIZE/4, Tile.SIZE/2, (2*i+2)*Tile.SIZE/4);
			g.drawLine(Tile.SIZE, (2*i)*Tile.SIZE/4, Tile.SIZE, (2*i+1)*Tile.SIZE/4);
			g.drawLine(0, (2*i)*Tile.SIZE/4, 0, (2*i+1)*Tile.SIZE/4);
//			g.drawLine(Tile.SIZE/4, (2*i+1)*Tile.SIZE/4, Tile.SIZE/4, (2*i+2)*Tile.SIZE/4);
//			g.drawLine(3*Tile.SIZE/4, (2*i)*Tile.SIZE/4, 3*Tile.SIZE/4, (2*i+1)*Tile.SIZE/4);
		}
		
		g.setClip(null);
		g.setStroke(pStroke);
		g.setTransform(prev);
	}
}
