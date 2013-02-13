package gameObjects.equipment;

import gameObjects.Actor;
import gameObjects.DrawableObject;
import gameObjects.interfaces.Damageable;
import graphics.Camera;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.util.ArrayDeque;

import util.Vector;
import world.Room;

public class DebugSword extends Equipment {
	private boolean animating;
	private double startAngle = 2*Math.PI/6;
	private double angleOffset = -4*Math.PI/6;
	
	private double animationTime = .15;
	private double time = 0;
	
	private int damage = 5;
	
//	private Polygon hilt;
//	private Polygon blade;
	
	public DebugSword(){
		cycleTimer = .35;
		name = "Debug Sword";
	}
	
	@Override
	public void activate() {
		if(cooldown <= 0){
			//Create a damage volume
			Actor actor = host.getActor();
			Room room = actor.getCurrentRoom();
			
			Vector center = actor.getPosition();
			Vector start = center.add(new Vector(actor.getAngle() + startAngle).scaleTo(actor.getRadius()+20));
			
//			ArrayDeque<DrawableObject> obj = room.getEntitiesWithinCircle(actor.getPosition().add(new Vector(actor.getAngle()).scale(host.getRadius())), 20);
			ArrayDeque<DrawableObject> obj = room.getEntitiesIntersectingArc(center,start,angleOffset);
			
			for(DrawableObject d:obj)
				if(d instanceof Damageable){
					Damageable ref = (Damageable)d;
					if(ref.isDamageable(actor.getFaction()))
						ref.applyDamage(damage);
				}
			
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
		
//		Actor actor = host.getActor();
//		Vector center = actor.getPosition();
//		int rad = (int)(actor.getRadius()+20);
//		Vector start = new Vector(actor.getAngle() + startAngle).scaleTo(actor.getRadius()+20);
//		Vector end = start.rot(angleOffset);
//		
//		g.translate(cam.xTranslatePosition(center.x), cam.yTranslatePosition(center.y));
//		g.rotate(angle);
//		g.scale(cam.scale(), cam.scale());
//		g.setColor(Color.white);
//		g.drawLine(0, 0, (int)start.x, (int)start.y);
//		g.drawLine(0, 0, (int)end.x, (int)end.y);
//		g.drawArc(-rad, -rad, 2*rad, 2*rad, 360-(int)Math.toDegrees(actor.getAngle()+startAngle), (int)Math.toDegrees(-angleOffset));
//		g.drawOval(-20, -20, 40, 40);
//		
//		g.setTransform(prev);
		
		g.translate(cam.xTranslatePosition(position.x), cam.yTranslatePosition(position.y));
		g.rotate(angle);
		g.scale(cam.scale(), cam.scale());
		
		BasicStroke s = new BasicStroke(1.5f);
		g.setStroke(s);
		
		g.setColor(Color.black);
//		g.setColor(Color.gray);
		g.drawLine(-4, 0, 14, 0);
		
//		g.setColor(Color.DARK_GRAY);
		g.drawLine(0, -3, 0, 3);
		
		g.setStroke(new BasicStroke(1.3f));
		g.setColor(Color.gray);
		g.drawLine(-4, 0, 14, 0);
		
		g.setColor(Color.DARK_GRAY);
		g.drawLine(0, -3, 0, 3);
		
		g.setColor(Color.yellow.darker().darker());
		g.fillOval(-6,-1,2,2);
		
		g.setStroke(new BasicStroke(.2f));
		g.setColor(Color.black);
		g.drawOval(-6,-1,2,2);
		
		g.setStroke(pStroke);
		g.setTransform(prev);
	}
}
