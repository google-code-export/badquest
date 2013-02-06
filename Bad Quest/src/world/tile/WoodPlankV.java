package world.tile;

import graphics.Camera;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

import world.Room;


public class WoodPlankV extends Tile {
	private Color[] full = {new Color(0x5D2F17), new Color(0x7E502F)};
	private Color[] half = {new Color(0x5A3316), new Color(0x7C4A25)};
	private Color outline = new Color(0x3E2813);
	public WoodPlankV(int y, int x, Room owner){
		super(x, y, TileType.WOOD_PLANK_V, owner);
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
		g.translate(Tile.SIZE/2, Tile.SIZE/2);
		g.rotate(Math.PI/2);
		g.translate(-Tile.SIZE/2, -Tile.SIZE/2);
		g.setClip(0, 0, Tile.SIZE, Tile.SIZE+1);
		
		g.setColor(full[(x+y)%2]);
		g.fillRect(0,0,Tile.SIZE+1,Tile.SIZE/4+1);
		
		g.setColor(full[(x+y+1)%2]);
		g.fillRect(0,Tile.SIZE/2,Tile.SIZE+1,Tile.SIZE/4+1);
		
		g.setColor(half[(x+y)%2]);
		g.fillRect(0, Tile.SIZE/4, Tile.SIZE/2, Tile.SIZE/4);
		g.fillRect(Tile.SIZE/2, 3*Tile.SIZE/4, Tile.SIZE/2+1, Tile.SIZE/4);
		
		g.setColor(half[(x+y+1)%2]);
		g.fillRect(Tile.SIZE/2, Tile.SIZE/4, Tile.SIZE/2+1, Tile.SIZE/4);
		g.fillRect(0, 3*Tile.SIZE/4, Tile.SIZE/2, Tile.SIZE/4);
		
		g.setColor(outline);
		for(int i = 0; i < 5; i++)
			g.drawLine(0, i*Tile.SIZE/4, Tile.SIZE, i*Tile.SIZE/4);
		
		for(int i = 0; i < 2; i++){
			g.drawLine(Tile.SIZE/2, (2*i+1)*Tile.SIZE/4, Tile.SIZE/2, (2*i+2)*Tile.SIZE/4);
			g.drawLine(Tile.SIZE, (2*i)*Tile.SIZE/4, Tile.SIZE, (2*i+1)*Tile.SIZE/4);
			g.drawLine(0, (2*i)*Tile.SIZE/4, 0, (2*i+1)*Tile.SIZE/4);
		}
		
		g.setClip(null);
		g.setStroke(pStroke);
		g.setTransform(prev);
	}
}
