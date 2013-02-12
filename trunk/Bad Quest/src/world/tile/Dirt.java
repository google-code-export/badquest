package world.tile;

import graphics.Camera;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.ArrayDeque;
import java.util.Arrays;

import util.Vector;
import world.Room;

public class Dirt extends Tile {
	static Color[] rockColors = {new Color(0x674829), new Color(0x74542E), new Color(0x775B36), new Color(0x49321D)};
	Color dirt = new Color(0x563F29);
	
	ArrayDeque<RockSprite> rocks = new ArrayDeque<RockSprite>();
	double scaledTile;
	
	public Dirt(int y, int x, Room owner){
		super(x, y, TileType.DIRT, owner);
		
		//Generate a few rock sprites
		int count = (int)(Math.random() * 4)+2;
		for(int i = 0; i < count; i++){
			boolean added = false;
			for(int j = 0; j < 10 && !added; j++){
				RockSprite candidate = new RockSprite();
				boolean good = true;
				for(RockSprite rock:rocks)
					good &= !candidate.overlaps(rock);
				if(good){
					rocks.add(candidate);
					added = true;
				}
			}
		}
		
		//Shift the dirt color
		float[] rgb = dirt.getRGBColorComponents(null);
		double offset = (.2*Math.random())+.90;
		for(int i = 0; i < 3; i++)
			rgb[i] = (float)Math.min(1,(rgb[i] * offset));
		dirt = new Color(rgb[0],rgb[1],rgb[2]);
	}
	
	@Override
	public void update(double elapsedSeconds) {
		
	}

	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam) {
		AffineTransform prev = g.getTransform();
		
		g.translate(cam.xTranslatePosition(position.x), cam.yTranslatePosition(position.y));
//		g.scale(cam.scale(), cam.scale());
		scaledTile = Tile.SIZE*cam.scale();
		
		g.setClip(0, 0, (int)Math.ceil(scaledTile), (int)Math.ceil(scaledTile));
		
		g.setColor(dirt);
		g.fillRect(0, 0, (int)Math.ceil(scaledTile+1), (int)Math.ceil(scaledTile+1));
		
		for(RockSprite rock:rocks)
			rock.drawBody(g, elapsedSeconds, cam);
		
		g.setClip(null);
		g.setTransform(prev);
	}
	
	private class RockSprite{
		Color color;
		Vector position;
		Vector[] points;
		double radius;
		public RockSprite(){
			int n = (int)(Math.random()*3) + 5;
			points = new Vector[n];
			radius = Math.random() * Tile.SIZE/16.+Tile.SIZE/8.;
			position = new Vector(Math.random() * Tile.SIZE * 3./4. + Tile.SIZE/8., Math.random() * Tile.SIZE * 3./4. + Tile.SIZE/8.);
			
			double[] a = new double[n];
			double[] r = new double[n];
			for(int i = 0; i < n; i++){
				a[i] = (2*Math.PI * i)/n + ((Math.random()*(4*Math.PI * i)/n) - (2*Math.PI * i)/n)/8;
				r[i] = radius*(Math.random()*.2 + .8);
			}
			
			Arrays.sort(a);
			
//			for(int i = 0; i < n; i++)
//				points[i] = new Vector((2*Math.PI * i)/n + ((Math.random()*(4*Math.PI * i)/n) - (2*Math.PI * i)/n)/8).scaleTo(radius*(Math.random()*.2 + .8));
			for(int i = 0; i < n; i++)
				points[i] = new Vector(a[i]).scaleTo(r[i]);
			
			color = rockColors[(int)(Math.random()*rockColors.length)];
		}
		
		public boolean overlaps(RockSprite candidate){
			return position.dis2(candidate.position) <= Math.pow(radius + candidate.radius,2);
		}
		
		public Polygon buildPoly(double scale){
			int[] x = new int[points.length];
			int[] y = new int[points.length];
			for(int i = 0; i < points.length; i++){
				x[i] = (int)Math.round((points[i].x+position.x)*scale);
				y[i] = (int)Math.round((points[i].y+position.y)*scale);
			}
			return new Polygon(x, y, x.length);
		}
		
		@SuppressWarnings("unused")
		public GeneralPath buildPath(double scale){
			GeneralPath ret = new GeneralPath();
			ret.moveTo(points[0].add(position).scale(scale).x, points[0].add(position).scale(scale).y);
			for(int i = 0; i < points.length; i++){
				Vector a = points[i].add(position).scale(scale);
				Vector b = points[(i+1)%points.length].add(position).scale(scale);
				Vector mid = a.add(b).scale(.5);
				Vector c = mid.add(b.sub(a).norm().orthoCCW().scaleTo(radius/8*scale));
				ret.curveTo(a.x, a.y, b.x, b.y, c.x, c.y);
			}
			ret.closePath();
			return ret;
		}
		
		public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam){
			AffineTransform prev = g.getTransform();
			
			g.setColor(color);
			g.fillPolygon(buildPoly(cam.scale()));
//			g.setColor(Color.black);
//			g.draw(buildPath(cam.scale()));
			
			g.setTransform(prev);
		}
	}
}
