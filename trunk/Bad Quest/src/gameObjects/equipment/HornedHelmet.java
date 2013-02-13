package gameObjects.equipment;

import graphics.Camera;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

public class HornedHelmet extends Equipment {
	Color dark = new Color(0x0F1207);
	Color light = new Color(0x2B3715);
	Color horns = new Color(0x4B5A25);
	
	double[][] left,right;
	
	public HornedHelmet(){
		radius = 10;
		
		if(Math.random() < 1./3.)
			left = makeBrokenHorn();
		else
			left = makeHorn();
		
		if(Math.random() < 1./3.)
			right = makeBrokenHorn();
		else
			right = makeHorn();
		
		flipOverX(left[1]);
	}
	
	public void flipOverX(double[] y){
		for(int i = 0; i < y.length; i++)
			y[i] *= -1;
	}
	
	/**
	 * Returns the vertices of a horn {x,y}
	 * @return
	 */
	private double[][] makeHorn(){
		return new double[][]{{1,5,11,16,10,3,-3,-3,-2},
				              {3,3,5,5,9,11,9,6,4}};
	}
	
	/**
	 * Returns the vertices of a broken horn {x,y}
	 * @return
	 */
	private double[][] makeBrokenHorn(){
		return new double[][]{{1,5,11,11,9,7,7,3,-3,-3,-2},
				              {3,3,5,6,8,8,10,11,9,6,4}};
	}
	
	@Override
	public void activate() {
		
	}
	
	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam) {
		AffineTransform prev = g.getTransform();
		Stroke pStroke = g.getStroke();
		
		g.translate(cam.xTranslatePosition(position.x), cam.yTranslatePosition(position.y));
		g.scale(cam.scale(), cam.scale());
		g.rotate(angle);
		
		g.setColor(dark);
		g.fillOval(-(int)Math.round(radius), -(int)Math.round(radius), 2*(int)Math.round(radius), 2*(int)Math.round(radius));
		
		g.setColor(light);
		g.setClip(new Ellipse2D.Double(-radius-.1,-radius-.1,2*radius+.2,2*radius+.2));
		
		double bigRadius = 2*radius;
		g.fillOval(-(int)bigRadius, -(int)(bigRadius+2.2*radius), 2*(int)bigRadius, 2*(int)bigRadius);
		g.fillOval(-(int)bigRadius, (int)(-bigRadius+2.2*radius), 2*(int)bigRadius, 2*(int)bigRadius);
		
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(.3f));
		g.drawOval(-(int)bigRadius, -(int)(bigRadius+2.2*radius), 2*(int)bigRadius, 2*(int)bigRadius);
		g.drawOval(-(int)bigRadius, (int)(-bigRadius+2.2*radius), 2*(int)bigRadius, 2*(int)bigRadius);
		
		g.setClip(null);
		
		g.setColor(Color.black);
		g.drawOval(-(int)Math.round(radius), -(int)Math.round(radius), 2*(int)Math.round(radius), 2*(int)Math.round(radius));
		
		g.setColor(horns);
		fillPoly(left[0], left[1], g);
		fillPoly(right[0], right[1], g);
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(.3f));
		drawPoly(left[0], left[1], g);
		drawPoly(right[0], right[1], g);
		
		g.setStroke(pStroke);
		g.setTransform(prev);
	}
}
