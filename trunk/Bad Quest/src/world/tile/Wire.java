package world.tile;

import graphics.Camera;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

import world.Room;

public class Wire extends Tile {
	public Wire(int y, int x, Room owner){
		super(x, y, TileType.WIRE, owner);
//		color = new Color(0x452116);
		color = Color.DARK_GRAY;
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
		
		g.setColor(color);
		g.setStroke(new BasicStroke(2f));
		g.drawLine(0, 0, Tile.SIZE, Tile.SIZE);
		g.drawLine(0,Tile.SIZE,Tile.SIZE,0);
		g.drawLine(0, Tile.SIZE/2, Tile.SIZE/2, 0);
		g.drawLine(Tile.SIZE/2,Tile.SIZE,Tile.SIZE,Tile.SIZE/2);
		g.drawLine(0, Tile.SIZE/2, Tile.SIZE/2, Tile.SIZE);
		g.drawLine(Tile.SIZE/2,0,Tile.SIZE,Tile.SIZE/2);
		
		g.setColor(color.brighter());
		g.setStroke(new BasicStroke(2/3.f));
		g.drawLine(0, 0, Tile.SIZE, Tile.SIZE);
		g.drawLine(0,Tile.SIZE,Tile.SIZE,0);
		g.drawLine(0, Tile.SIZE/2, Tile.SIZE/2, 0);
		g.drawLine(Tile.SIZE/2,Tile.SIZE,Tile.SIZE,Tile.SIZE/2);
		g.drawLine(0, Tile.SIZE/2, Tile.SIZE/2, Tile.SIZE);
		g.drawLine(Tile.SIZE/2,0,Tile.SIZE,Tile.SIZE/2);
		
		g.setClip(null);
		g.setStroke(pStroke);
		g.setTransform(prev);
	}
}
