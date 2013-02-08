package world.tile;

import graphics.Camera;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.AffineTransform;
import java.util.ArrayDeque;

import util.Vector;
import world.Room;

public abstract class BubblingTile extends Tile {
	protected Color in;
	protected Color out;
	protected Color c1;
	protected Color c2;
	protected float[] fractions = {0f, 1f};
	
	protected int maxBubbleCount = 3;
	protected double bubbleChance = .33;
	protected double period = 4;
	protected double time = 0;
	
	protected ArrayDeque<Bubble> bubbles = new ArrayDeque<Bubble>();
	
	public BubblingTile(int y, int x, TileType TID, Room owner){
		super(x, y, TID, owner);
		floor = false;
		bubbles.add(new Bubble());
		initialize();
	}
	
	public Color mix(Color a, Color b){
		float[] v = a.getColorComponents(null);
		float[] u = b.getColorComponents(null);
		float[] w = new float[3];
		
		float p = (float)(2*Math.abs(time-period/2)/period);
		float q = 1-p;
		for(int i = 0; i < 3; i++)
			w[i] = v[i] * p + u[i] * q;
		return new Color(w[0], w[1], w[2]);
	}
	
	@Override
	public void update(double elapsedSeconds) {
		time = (time + elapsedSeconds)%period;
		
		for(Bubble bubble:bubbles)
			bubble.update(elapsedSeconds);
		
		ArrayDeque<Bubble> bubs = new ArrayDeque<Bubble>();
		while(!bubbles.isEmpty()){
			Bubble bubble = bubbles.remove();
			if(!bubble.isPopped())
				bubs.add(bubble);
		}
		bubbles = bubs;
		
		if(bubbles.size() < maxBubbleCount && Math.random() < bubbleChance * elapsedSeconds){
			Bubble candidate = new Bubble();
			for(Bubble bubble:bubbles)
				if(bubble.overlaps(candidate))
					return;
			bubbles.add(new Bubble());
		}
	}
	
	double scaledTile;
	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam) {
		AffineTransform prev = g.getTransform();
		
		g.translate(cam.xTranslatePosition(position.x), cam.yTranslatePosition(position.y));
//		g.scale(cam.scale(), cam.scale());
		scaledTile = Tile.SIZE*cam.scale();
		
		g.setClip(0, 0, (int)Math.ceil(scaledTile), (int)Math.ceil(scaledTile));
		
		RadialGradientPaint paint = new RadialGradientPaint((float)scaledTile/2f, (float)scaledTile/2f, (float)scaledTile * .666667f, fractions, new Color[]{mix(in,out), mix(out,in)});
		
		g.setPaint(paint);
		g.fillRect(0,0,(int)Math.round(scaledTile+1),(int)Math.round(scaledTile+1));
		
		for(Bubble bubble:bubbles)
			bubble.drawBody(g, elapsedSeconds, cam);
		
		g.setClip(null);
		g.setTransform(prev);
	}
	
	public abstract void initialize();
	
	private class Bubble{
		Vector position;
		double radius,growth;
		double sRadius,sGrowth;
		double timeToPop = Math.random()*2+1;
		boolean popping = false, popped = false;
		
		public Bubble(){
			double x = Math.random() * Tile.SIZE/2+Tile.SIZE/4;
			double y = Math.random() * Tile.SIZE/2+Tile.SIZE/4;
			position = new Vector(x,y);
			radius = Math.random() * Tile.SIZE/16. + Tile.SIZE/32.;
			growth = radius/timeToPop;
		}
		
		public boolean overlaps(Bubble b){
			return position.dis2(b.position) <= Math.pow(radius + b.radius,2);
		}
		
		public boolean isPopped(){
			return popped;
		}
		
		public void update(double elapsedSeconds) {
			timeToPop -= elapsedSeconds;
			
			if(!popping)
				radius += elapsedSeconds * growth;
			else
				sRadius += elapsedSeconds * sGrowth;
			
			if(timeToPop <= 0 && popping)
				popped = true;
			else if(timeToPop <= 0){
				popping = true;
				timeToPop = .25;
				sGrowth = (radius*1.25)/timeToPop;
			}
		}

		//Only called from the parent tile, current camera position and scaling are set.
		public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam) {
			AffineTransform prev = g.getTransform();
			
			g.setColor(c1);
			g.fillOval((int)((position.x-radius)*cam.scale()), (int)((position.y-radius)*cam.scale()), (int)(2*radius*cam.scale()), (int)(2*radius*cam.scale()));
			
			if(popping){
				g.setColor(c2);
				g.fillOval((int)((position.x-sRadius)*cam.scale()), (int)((position.y-sRadius)*cam.scale()), (int)(2*sRadius*cam.scale()), (int)(2*sRadius*cam.scale()));
			}
			
			g.setTransform(prev);
		}
	}
}
