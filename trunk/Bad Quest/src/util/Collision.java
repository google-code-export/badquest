package util;

import gameObjects.DrawableObject;

import java.util.ArrayDeque;

import world.Tile;

public class Collision {
	private static int[] dx = new int[]{1,0,0,1};
	private static int[] dy = new int[]{0,0,1,1};
	private static int[] cardinal = new int[]{0,-1,0,1};
	
	private static final double extensionEPS = 1e-3;
	private static final int MAX_COLLISIONS = 10;
	
	public static Pair objectTileCollision(DrawableObject obj, Tile t, double elapsedSeconds){
		Vector nextPos = obj.getPosition().add(obj.getVelocity().scale(elapsedSeconds));
		double min = 1;
		Vector hit = null;
		
		//Decompose the tile to 4 segments and 4 circles, all points obj.radius-EPS away from the tile
		//Intersect the 4 line segments
		for(int i = 0; i < 4; i++){
			Vector shift = new Vector(cardinal[i]*(obj.getRadius()), cardinal[(i+1)%4]*(obj.getRadius()));
			Vector a = t.getPosition().add(new Vector(dx[i]*Tile.SIZE, dy[i]*Tile.SIZE)).add(shift);
			Vector b = t.getPosition().add(new Vector(dx[(i+1)%4]*Tile.SIZE, dy[(i+1)%4]*Tile.SIZE)).add(shift);
			
			Vector intersect = Geometry.llIntersect(a, b, obj.getPosition(), nextPos);
			if(intersect != null){
				double scale = Geometry.findScale(obj.getPosition(), nextPos, intersect);
				double tileScale = Geometry.findScale(a,b,intersect);
				if(tileScale >= 0 && tileScale <= 1 && scale < min && scale >= 0){
					min = scale;
					hit = intersect.sub(shift);
				}
			}
		}
		
		//Intersect the 4 circles
		double angle = 2*Math.PI;
		for(int i = 0; i < 4; i++){
			//Center of the circle, a vertex on the tile
			Vector a = t.getPosition().add(new Vector(dx[i]*Tile.SIZE, dy[i]*Tile.SIZE));
			double radius = obj.getRadius();
			ArrayDeque<Vector> intersect = Geometry.clIntersect(a, radius, obj.getPosition(), nextPos);
			if(intersect.size() == 0)
				continue;
			for(Vector v:intersect){
				double scale = Geometry.findScale(obj.getPosition(), nextPos, v);
				if(scale < min && scale >= 0){
					min = scale;
					hit = a;
				}
			}
			angle -= Math.PI/2;
		}
		
		//If minimum scale on displacement vector is 1, there is no collision
		if(hit == null)
			return new Pair(null,1);
		//Otherwise, stop the object in the appropriate dimension
		//Replace this with an object-specific reaction when available! (splat, bounce, teleport)
		return new Pair(hit,min);
	}
	
	public static void collideObjectWithRoom(Tile[][] map, DrawableObject obj, double elapsedSeconds){
		boolean changed = true;
		int counter = 0;
		while(changed){
			if(counter >= MAX_COLLISIONS){
				obj.setVelocity(new Vector(0,0));
				break;
			}
			
			Pair best = new Pair(null,1);
			
			double r = obj.getRadius();
			double x = 2*Tile.SIZE;
			double D = obj.getVelocity().scale(elapsedSeconds).mag();
			double check = Math.pow(r+D+x,2);
			
			for(int i = 0; i < map.length; i++)
				for(int j = 0; j < map[i].length; j++){
					Vector center = map[i][j].getCenter();
					if(!map[i][j].isSolid() || center.sub(obj.getPosition()).mag2() > check)
						continue;
					Pair ret = objectTileCollision(obj, map[i][j], elapsedSeconds);
					if(ret.min < best.min)
						best = ret;
				}
			
			if(best.hit == null)
				changed = false;
			else{
				Vector nextPos = obj.getPosition().add(obj.getVelocity().scale(elapsedSeconds));
				Vector stop = nextPos.sub(obj.getPosition()).scale(Math.max(0,best.min-extensionEPS)).add(obj.getPosition());
				Vector sub = obj.getVelocity().project(best.hit.sub(stop));
				
				obj.setPosition(stop);
				obj.setVelocity(obj.getVelocity().sub(sub));
//				obj.setVelocity(new Vector(0,0));
			}
			counter++;
		}
	}
	
	private static class Pair{
		Vector hit;
		double min;
		public Pair(Vector _hit, double _min){
			if(_hit != null)
				hit = new Vector(_hit);
			min = _min;
		}
	}
}