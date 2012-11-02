package gameObjects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

import util.Vector;
import client.Camera;

public class DebugSword extends Equipment {
	
	public DebugSword(Vector pos){
		setPosition(pos);
	}
	
	@Override
	public void use() {
		
	}
	
	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam) {
		AffineTransform prev = g.getTransform();
		Stroke pStroke = g.getStroke();
		
		g.translate(cam.xTranslatePosition(position.x), cam.yTranslatePosition(position.y));
		g.rotate(angle);
		g.scale(cam.scale(), cam.scale());
		
		BasicStroke s = new BasicStroke(1.5f);
		g.setStroke(s);
		g.setColor(Color.gray);
		g.drawLine(-4, 0, 14, 0);
		g.setColor(Color.DARK_GRAY);
		g.drawLine(0, -3, 0, 3);
		g.setColor(Color.yellow.darker().darker());
		g.fillOval(-6,-1,2,2);
		
		g.setStroke(pStroke);
		g.setTransform(prev);
	}
}
