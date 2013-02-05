package world.tile;

import graphics.Camera;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

import world.Room;

public class Water extends Tile {
	
	double period = 12;
	double fluxPeriod = 6;
	double time = 0;
	double fluxTime = 0;
	
	public Water(int y, int x, Room owner){
		super(x, y, TileType.WATER, owner);
		floor = false;
	}
	
	@Override
	public void update(double elapsedSeconds) {
		time = (time + elapsedSeconds)%period;
		fluxTime = (fluxTime + elapsedSeconds)%fluxPeriod;
	}
	
	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam) {
		AffineTransform prev = g.getTransform();
		Stroke pStroke = g.getStroke();
		
		g.translate(cam.xTranslatePosition(position.x), cam.yTranslatePosition(position.y));
//		g.scale(cam.scale(), cam.scale());
		int scaledTile = (int)Math.ceil(Tile.SIZE * cam.scale());
		g.setClip(0, 0, scaledTile, scaledTile);

		double shift = (Math.pow(Math.cos(time/period * 2 * Math.PI),1) + 1)/2 * scaledTile;
		
		g.setColor(new Color(30,100,200,255));
		g.fillRect(0, 0, scaledTile+1, scaledTile+1);
		
		g.setColor(new Color(20,128,255,255));
		
		g.setStroke(new BasicStroke((float)(((Math.pow(Math.abs(fluxTime-fluxPeriod/2)/(fluxPeriod/2),2))+.5f)*cam.scale())));
		
//		g.drawArc((int)Math.round(0+shift), (int)Math.round(0), scaledTile, scaledTile, 270, 90);
//		g.drawArc((int)Math.round(-scaledTile/2. + shift), (int)Math.round(-scaledTile/2.), scaledTile, scaledTile, 270, 90);
//		
//		g.drawArc((int)Math.round(-scaledTile/2. + shift), (int)Math.round(scaledTile/2.), scaledTile, scaledTile, 90, 90);
//		g.drawArc((int)Math.round(-scaledTile + shift), (int)Math.round(0), scaledTile, scaledTile, 90, 90);
//		
//		g.drawArc((int)Math.round(-2 * scaledTile + shift), (int)Math.round(0), scaledTile, scaledTile, 270, 90);
//		g.drawArc((int)Math.round(-2.5 * scaledTile + shift), (int)Math.round(-scaledTile/2.), scaledTile, scaledTile, 270, 90);
		
		g.drawArc((int)Math.round(-scaledTile*3/2.+shift), -(int)Math.round(scaledTile/2.), scaledTile, scaledTile, 270, 90);
		g.drawArc((int)Math.round(-scaledTile/2.+shift), (int)Math.round(scaledTile/2.), scaledTile, scaledTile, 90, 90);
		
		g.drawArc((int)Math.round(-scaledTile/2.+shift), -(int)Math.round(scaledTile/2.), scaledTile, scaledTile, 270, 90);
		g.drawArc((int)Math.round(scaledTile/2.+shift), (int)Math.round(scaledTile/2.), scaledTile, scaledTile, 90, 90);
		
		g.setStroke(pStroke);
		g.setClip(null);
		g.setTransform(prev);
	}

}
