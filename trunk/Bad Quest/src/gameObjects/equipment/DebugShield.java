package gameObjects.equipment;

import graphics.Camera;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

import util.Vector;

public class DebugShield extends Equipment {
	private Polygon body;
	private Color color = Color.gray;
	
	private boolean up = false;
	
	public DebugShield(){		
		int[] x = new int[]{0,-1,-2,0,1,2,2,1,0,-2,-1,0};
		int[] y = new int[]{-2,-6,-8,-8,-6,-2,2,6,8,8,6,2};
		body = new Polygon(x,y,x.length);
		name = "Debug Shield";
	}
	
	@Override
	public void activate() {
		up = true;
	}
	
	@Override
	public void update(double elapsedSeconds) {
		super.update(elapsedSeconds);
		
		if(up){
			setAngle(host.getActor().getAngle());
			setPosition(host.getActor().getPosition().add(new Vector(host.getActor().getAngle()).scale(host.getRadius())));
			up = false;
		}
	}
	
	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam) {
		AffineTransform prev = g.getTransform();
		Stroke pStroke = g.getStroke();
		
		g.translate(cam.xTranslatePosition(position.x), cam.yTranslatePosition(position.y));
		g.rotate(angle);
		g.scale(cam.scale(), cam.scale());
		
		g.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.setColor(Color.black);
		g.drawLine(-2,-2,0,-4);
		g.drawLine(-2,-2,-2,2);
		g.drawLine(-2,2,0,4);
		
		g.setColor(color);
		g.fillPolygon(body);
		
		g.setStroke(new BasicStroke(.25f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.setColor(Color.black);
		g.drawPolygon(body);

		g.setStroke(pStroke);
		g.setTransform(prev);
	}
}
