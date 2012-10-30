package gameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import util.Vector;
import client.Camera;

public class Player extends Actor {
	private double visorHeight = radius + radius*.5, 
	               visorBase = 1.3*radius, 
	               jointWidth = .3 * radius, 
	               jointHeight = .7 * radius;
	
	private int[] vx;
	private int[] vy;
	public Player(String name, int r, Vector position){
		super(name,r,position);
		color = new Color(125,190,209).darker();
//		solid = false;
		initVisor();
	}
	
	private void initVisor(){
		vx = new int[]{(int)Math.round(visorHeight), (int)Math.round(radius*.1), (int)Math.round(radius*.1)};
		vy = new int[]{0, (int)Math.round(visorBase), (int)Math.round(-visorBase)};
	}
	
	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam) {
		AffineTransform prev = g.getTransform();
		
//		Polygon visor = new Polygon(vx, vy, vx.length);
//		
//		g.setColor(color);
		g.translate(cam.xTranslatePosition(position.x), cam.yTranslatePosition(position.y));
		g.scale(cam.scale(), cam.scale());
//		g.rotate(angle);
		
		g.setColor(Color.black);
		g.drawOval(-(int)radius, -(int)radius, 2*(int)radius, 2*(int)radius);
		
//		g.setClip(0, (int)-(radius+1), (int)visorHeight+1, (int)(2*(radius+1)));
//		g.fillPolygon(visor);
		g.setTransform(prev);
//		g.setClip(null);
		
		super.drawBody(g, elapsedSeconds, cam);
//		
//		prev = g.getTransform();
//		
//		g.setColor(color.darker());
//		g.translate(cam.xTranslatePosition(position.x), cam.yTranslatePosition(position.y));
//		g.scale(cam.scale(), cam.scale());
//		g.rotate(angle);
//		
//		g.fillRect((int)(-jointHeight/2), -(int)(radius+jointWidth-1), (int)jointHeight, (int)jointWidth);
//		g.fillRect((int)(-jointHeight/2), (int)(radius-1), (int)jointHeight, (int)jointWidth);
		
		g.setTransform(prev);
	}
	
	@Override
	public void update(double elapsedSeconds) {
		super.update(elapsedSeconds);
	}
}
