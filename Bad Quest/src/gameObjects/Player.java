package gameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

import util.Vector;
import client.Camera;

public class Player extends Actor {	
	private EquipmentModule rightHand,head;
	
	public Player(String name, int r, Vector position){
		super(name,r,position);
		color = new Color(125,190,209).darker();
		rightHand = new EquipmentModule(new Vector(Math.PI/2.5).scale(radius*1.5).add(position));
		rightHand.loadEquipment(new DebugSword(new Vector(-Math.PI/2.5).scale(radius*1.5).add(position)));
		head = new EquipmentModule(position, true);
		head.loadEquipment(new DebugHelmet());
	}

	public void drawPoly(double[] x, double[] y, Graphics2D g){
		int[] px = new int[x.length];
		int[] py = new int[y.length];
		for(int i = 0; i < x.length; i++){
			px[i] = (int)Math.round(x[i]);
			py[i] = (int)Math.round(y[i]);
		}
		
		g.drawPolygon(px, py, x.length);
	}
	
	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam) {
		AffineTransform prev = g.getTransform();
		Stroke pStroke = g.getStroke();
		
		g.translate(cam.xTranslatePosition(position.x), cam.yTranslatePosition(position.y));
		g.scale(cam.scale(), cam.scale());
		g.rotate(angle);
		
		g.setColor(Color.black);
		g.drawOval(-(int)radius, -(int)radius, 2*(int)radius, 2*(int)radius);

		super.drawBody(g, elapsedSeconds, cam);
		
		g.setStroke(pStroke);
		g.setTransform(prev);
		
		rightHand.drawBody(g, elapsedSeconds, cam);
		head.drawBody(g, elapsedSeconds, cam);
	}
	
	@Override
	public void update(double elapsedSeconds){
		if(velocity.mag2() > 0)
			angle = velocity.ang();
		
		super.update(elapsedSeconds);
		
		rightHand.setPosition(new Vector(Math.PI/2.5).scale(radius*1.5).rot(angle).add(position));
		rightHand.move(elapsedSeconds);
		rightHand.setAngle(angle + Math.PI/6);
		
		head.setPosition(position);
		head.setAngle(angle);
		head.move(elapsedSeconds);
	}
}
