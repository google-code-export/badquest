package gameObjects;

import graphics.Camera;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

import util.Vector;
import world.Room;

public class DebugSword extends Equipment {
	private boolean animating;
	private double startAngle = 2*Math.PI/6;
	private double angleOffset = -4*Math.PI/6;
	
	private double animationTime = .15;
	private double time = 0;
	
	public DebugSword(){
		cycleTimer = .35;
	}
	
	@Override
	public void activate() {
		if(cooldown <= 0){
			//Create a damage volume
			Room room = host.getActor().getCurrentRoom();
			room.getRID();
			triggerCycleCooldown();
			animating = true;
			time = 0;
		}
	}
	
	//TODO: Less hacky, please! At least don't make the object move twice per update
	@Override
	public void update(double elapsedSeconds) {
		super.update(elapsedSeconds);
		
		if(time < animationTime){
			time += elapsedSeconds;
			animating &= time < animationTime;
		}
		
		if(animating){
			Actor actor = host.getActor();
			double actorAngle = actor.getAngle();
			Vector actorPos = actor.getPosition();
			
			double x = time/animationTime;
			double offset = angleOffset*x;
			
			setAngle(actorAngle + startAngle + offset);
			setPosition(actorPos.add(new Vector(actorAngle + startAngle + offset).scale(host.getRadius()+actor.getRadius()/2)));
		}
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
