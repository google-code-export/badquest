package world.tile;

import gameObjects.Actor;
import gameObjects.DrawableObject;
import graphics.Camera;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayDeque;

import util.Vector;
import world.Room;

public class Smart extends Tile {
	Vector center;
	double brightness = 0;
	double radius = 100;
	public Smart(int y, int x, Room owner){
		super(x, y, TileType.SMART, owner);
		center = position.add(new Vector(Tile.SIZE/2,Tile.SIZE/2));
	}
	
	@Override
	public void update(double elapsedSeconds) {
		ArrayDeque<DrawableObject> near = owner.getEntitiesWithinCircle(center, radius);
		double min = radius*radius;
		for(DrawableObject d:near)
			if(d instanceof Actor)
				min = Math.min(min, d.getPosition().dis2(center));
		brightness = (radius*radius-min)/(radius*radius);
		brightness *= brightness; //A squared function makes everything smoother!
	}

	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam) {
		AffineTransform prev = g.getTransform();
		
		g.translate(cam.xTranslatePosition(position.x), cam.yTranslatePosition(position.y));
		g.scale(cam.scale(), cam.scale());
		g.setClip(0, 0, Tile.SIZE, Tile.SIZE);
		
		g.setColor(new Color(.5f,.5f,.5f,(float)brightness));
		g.fillRect(0, 0, Tile.SIZE+1, Tile.SIZE+1);
		
		g.setClip(null);
		
		g.setTransform(prev);
	}

}
