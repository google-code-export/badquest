package world.tile;

import graphics.Camera;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

import world.Room;

public class Water extends Tile {
	
	double period = 2;
	double fluxPeriod = 2;
	double time = 0;
	double fluxTime = 0;
	
	public Water(int y, int x, Room owner){
		super(x, y, TileType.WATER, owner);
//		if((x+y)%2==1)
//			time = period/2;
		floor = false;
	}
	
	double a = 1.067;
	double b = -1.6;
	double c = 1.5333;
	double d = 0;
	public double getShift(){
		double x = time/period;
		return a * Math.pow(x, 3) + b * Math.pow(x, 2) + c * x + d;
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
		g.scale(cam.scale(), cam.scale());
		g.setClip(0, 0, Tile.SIZE, Tile.SIZE);
		
//		double shift = time/period * 2 * Tile.SIZE;
		double shift = getShift() * Tile.SIZE;
		
		g.setColor(new Color(30,100,200,255));
		g.fillRect(0, 0, Tile.SIZE+1, Tile.SIZE+1);
		
		g.setColor(new Color(20,128,255,255));
		
		g.setStroke(new BasicStroke((float)(Math.pow(Math.abs(fluxTime-fluxPeriod/2)/(fluxPeriod/2),2))+.5f));
		
//		g.drawArc((int)Math.round(0+shift), (int)Math.round(0), Tile.SIZE, Tile.SIZE, 270, 90);
//		g.drawArc((int)Math.round(-Tile.SIZE/2. + shift), (int)Math.round(-Tile.SIZE/2.), Tile.SIZE, Tile.SIZE, 270, 90);
//		
//		g.drawArc((int)Math.round(-Tile.SIZE/2. + shift), (int)Math.round(Tile.SIZE/2.), Tile.SIZE, Tile.SIZE, 90, 90);
//		g.drawArc((int)Math.round(-Tile.SIZE + shift), (int)Math.round(0), Tile.SIZE, Tile.SIZE, 90, 90);
//		
//		g.drawArc((int)Math.round(-2 * Tile.SIZE + shift), (int)Math.round(0), Tile.SIZE, Tile.SIZE, 270, 90);
//		g.drawArc((int)Math.round(-2.5 * Tile.SIZE + shift), (int)Math.round(-Tile.SIZE/2.), Tile.SIZE, Tile.SIZE, 270, 90);
		
		g.drawArc((int)Math.round(-Tile.SIZE*3/2.+shift), -Tile.SIZE/2, Tile.SIZE, Tile.SIZE, 270, 90);
		g.drawArc((int)Math.round(-Tile.SIZE/2.+shift), Tile.SIZE/2, Tile.SIZE, Tile.SIZE, 90, 90);
		
		g.drawArc((int)Math.round(-Tile.SIZE/2.+shift), -Tile.SIZE/2, Tile.SIZE, Tile.SIZE, 270, 90);
		g.drawArc((int)Math.round(Tile.SIZE/2.+shift), Tile.SIZE/2, Tile.SIZE, Tile.SIZE, 90, 90);
		
		g.setStroke(pStroke);
		g.setClip(null);
		g.setTransform(prev);
	}

}
