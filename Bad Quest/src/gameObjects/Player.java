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
	
	private EquipmentModule rightHand;
	
	public Player(String name, int r, Vector position){
		super(name,r,position);
		color = new Color(125,190,209).darker();
		rightHand = new EquipmentModule(new Vector(Math.PI/2.5).scale(radius*1.5).add(position));
		rightHand.loadEquipment(new DebugSword(new Vector(-Math.PI/2.5).scale(radius*1.5).add(position)));
		initVisor();
	}
	
	private void initVisor(){
		vx = new int[]{(int)Math.round(visorHeight), (int)Math.round(radius*.1), (int)Math.round(radius*.1)};
		vy = new int[]{0, (int)Math.round(visorBase), (int)Math.round(-visorBase)};
	}
	
	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam) {
		AffineTransform prev = g.getTransform();
		
		g.translate(cam.xTranslatePosition(position.x), cam.yTranslatePosition(position.y));
		g.scale(cam.scale(), cam.scale());
		g.rotate(angle);
		
		g.setColor(Color.black);
		g.drawOval(-(int)radius, -(int)radius, 2*(int)radius, 2*(int)radius);
		
		super.drawBody(g, elapsedSeconds, cam);
		
		g.setColor(Color.black);
		g.drawLine(0, 0, (int)radius, 0);
		
		g.setTransform(prev);
		
		rightHand.drawBody(g, elapsedSeconds, cam);
	}
	
	@Override
	public void update(double elapsedSeconds){
		if(velocity.mag2() > 0)
			angle = velocity.ang();
		rightHand.setPosition(new Vector(Math.PI/2.5).scale(radius*1.5).rot(angle).add(position));
		rightHand.move(elapsedSeconds);
		rightHand.setAngle(angle + Math.PI/6);
		super.update(elapsedSeconds);
	}
}
