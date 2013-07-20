package gameObjects;

import gameObjects.interfaces.Ambulatory;
import gameObjects.interfaces.Damageable;
import graphics.Camera;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import util.Vector;

public class Actor extends DrawableObject implements Damageable, Ambulatory{
	protected Color color = Color.ORANGE;
	
	private Vector lookAt = null;
	
	private final int maxHealth = 100;
	private int currentHealth = maxHealth;
	private Faction faction = Faction.HOSTILE;
	
	public Actor(){
		
	}
	
	public Actor(String name){
		this.name = name;
	}
	
	public Actor(String name, int r){
		this.name = name;
		radius = r;
		faction = Faction.NEUTRAL;
	}
	
	public Actor(String name, int r, Vector position){
		this.name = name;
		radius = r;
		setPosition(position);
	}
	
	public void setFaction(Faction f){
		faction = f;
	}
	
	public Vector getLookAt(){
		return lookAt;
	}
	
	public void setLookAt(Vector look){
		if(look == null)
			lookAt = null;
		else
			lookAt = new Vector(look);
	}
	
	//Health stuff
	
	@Override
	public void applyDamage(int amount) {
		System.out.println(this + " taking " + amount + " damage!");
		if(faction == Faction.NEUTRAL){
			System.out.println(this + " turning hostile!");
			faction = Faction.HOSTILE;
		}
		currentHealth -= amount;
	}
	
	@Override
	public int getCurrentHealth() {
		return currentHealth;
	}
	
	@Override
	public Faction getFaction() {
		return faction;
	}
	
	@Override
	public int getMaxHealth() {
		return maxHealth;
	}
	
	@Override
	public boolean isDamageable(Faction f) {
		return f == Faction.NEUTRAL || f != faction;
	}
	
	@Override
	public double getMaxSpeed(){
		return 75;
	}

	@Override
	public void update(double elapsedSeconds){
		if(lookAt != null && lookAt.sub(position).mag2() > 1e-3)
			angle = lookAt.sub(position).ang();
		if(currentHealth <= 0)
			kill();
		move(elapsedSeconds);
	}

	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam){
		AffineTransform prev = g.getTransform();
		
		AffineTransform next = new AffineTransform();
		next.translate(cam.xTranslatePosition(position.x), cam.yTranslatePosition(position.y));
		next.scale(cam.xScale(), cam.yScale());
		
		g.setTransform(next);
		g.setColor(color);
		int drawRadius = (int)radius;
		g.fillOval(-drawRadius,-drawRadius,2*drawRadius,2*drawRadius);
		
		g.setTransform(prev);
	}
}
