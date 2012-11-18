package gameObjects.equipment;

import graphics.Camera;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;


public class DebugHelmet extends Equipment {
	private double[] vx,vy,cx,cy;
	private Color one,two;
	
	public DebugHelmet(){
		radius = 10;
		one = new Color(2,99,193);
		two = new Color(0,51,102);
		init();
	}
	
	private void init(){
		cy = new double[]{radius + 15/200. * radius, radius + 15/200. * radius, radius - 15/200. * radius, radius - 15/200. * radius};
		cx = new double[]{-55/200. * radius, 55/200. * radius, 95/200. * radius, -95/200. * radius};
		
		vx = new double[]{cx[1], radius * .8, radius * 1.3, radius * .8, cx[1]};
		vy = new double[]{cy[1], cy[1] * .7, 0, -cy[1] * .7, -cy[1]};
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
		g.setStroke(new BasicStroke(.3f));
		
		g.setColor(two);
		fillPoly(vx, vy, g);
		
		g.setClip(new Polygon(roundOff(vx), roundOff(vy), vx.length));
		g.setColor(one);
		g.setStroke(new BasicStroke(.7f));
		for(double y = -radius; y <= radius; y += (2*radius/10))
			g.drawLine(0, (int)Math.round(y), (int)Math.round(2*radius), (int)Math.round(y));
		g.setStroke(new BasicStroke(.3f));
		g.setClip(null);
		
		g.setColor(Color.black);
		drawPoly(vx, vy, g);
		
		g.setColor(one);
		g.fillOval(-(int)radius, -(int)radius, 2*(int)radius, 2*(int)radius);
		
		g.setColor(two);
		g.setClip(new Ellipse2D.Double(-radius-.1,-radius-.1,2*radius+.2,2*radius+.2));
		g.fillRect(-(int)radius, -(int)Math.round(radius/6.), (int)(2*radius), (int)Math.round(radius/3.+1));
		g.setColor(Color.black);
		g.drawRect(-(int)radius, -(int)Math.round(radius/6.), (int)(2*radius), (int)Math.round(radius/3.+1));
		g.setClip(null);
		
		g.setColor(Color.black);
		g.drawOval(-(int)radius, -(int)radius, 2*(int)radius, 2*(int)radius);
		
		g.setColor(two);
		fillPoly(cx, cy, g);
		g.rotate(Math.PI);
		fillPoly(cx, cy, g);
		g.rotate(Math.PI);
		
		g.setColor(Color.black);
		drawPoly(cx, cy, g);
		g.rotate(Math.PI);
		drawPoly(cx, cy, g);
		g.rotate(Math.PI);
		
		g.setStroke(pStroke);
		g.setTransform(prev);
	}
	
}
