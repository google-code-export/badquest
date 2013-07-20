package world;

import gameAI.Node;
import gameObjects.Actor;
import gameObjects.DrawableObject;
import gameObjects.Portal;
import graphics.Camera;
import graphics.DimmerGraphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

import util.Collision;
import util.Geometry;
import util.Vector;
import world.tile.Lava;
import world.tile.Stone;
import world.tile.Tile;
import world.tile.Tile.TileType;
import world.tile.Void;
import world.tile.Wall;

public class Room implements Comparable<Room>{
	private Tile[][] map;
	private ArrayList<Node>[] nodeGraph;
	private TreeMap<Integer, DrawableObject> entityMap;
	
	private Vector position;
	private double layer;
	
	public final int R,C;
	private final int RID;	
	/**
	 * Initialize a room with R row and C columns
	 * @param R
	 * @param C
	 */
	public Room(int R, int C, double d){
		RID = RoomManager.register(this);
		
		layer = d;
		this.R = R;
		this.C = C;
		position = new Vector(0,0);
		
		map = DebugRoomGenerator.makeRoom(this, R, C, 0);
//		for(int i = 0; i < R; i++)
//			for(int j = 0; j < C; j++)
//				map[i][j] = new Stone(i,j,this);
//		for(int i = 0; i < R; i++){
//			map[i][0] = new Wall(i,0,this);
//			map[i][C-1] = new Wall(i,C-1,this);
//		}
//		for(int i = 0; i < C; i++){
//			map[0][i] = new Wall(0,i,this);
//			map[R-1][i] = new Wall(R-1,i,this);
//		}
//		
//		map[7][5] = new Wall(7,5,this);
//		map[7][6] = new Wall(7,6,this);
//		map[7][7] = new Wall(7,7,this);
//		map[8][7] = new Wall(8,7,this);
//		map[9][7] = new Wall(9,7,this);
//		map[9][6] = new Wall(9,6,this);
		
		entityMap = new TreeMap<Integer, DrawableObject>();
		buildNodeGraph();
	}
	
	public Room(int selector, Vector v, double d){
		RID = RoomManager.register(this);
		position = new Vector(v);
		layer = d;
		
		R = DebugRoomMaker.prebuiltRows(selector);
		C = DebugRoomMaker.prebuiltCols(selector);
		map = DebugRoomMaker.selectPrebuilt(selector,this);
		
		entityMap = new TreeMap<Integer, DrawableObject>();
		buildNodeGraph();
	}
	
	public Room(boolean rect, int radius, Vector v, double d){
		RID = RoomManager.register(this);
		position = new Vector(v);
		layer = d;
		
		R = radius;
		C = radius;
		map = new Tile[R][C];
		
		for(int i = 0; i < R; i++)
			for(int j = 0; j < C; j++){
				if(Math.pow(i+.5-R/2.,2) + Math.pow(j+.5-C/2.,2) <= R*C/4.)
					map[i][j] = new Stone(i, j, this, new Color(0x3D0000));
				else
					map[i][j] = rect?new Wall(i,j,this,new Color(0x96613F)):new Void(i,j,this);
			}
		
		for(int i = 0; i < R; i++)
			for(int j = 0; j < C; j++){
				if(!rect && Math.pow(i+.5-R/2.,2) + Math.pow(j+.5-C/2.,2) <= R*C/16.)
					map[i][j] = new Lava(i, j, this);
			}
		
		boolean[][] visited = new boolean[R][C];
		int[] dx = {-1,0,1,0};
		int[] dy = {0,1,0,-1};
		LinkedList<Integer> q = new LinkedList<Integer>();
		for(int i = 0; i < R; i++){
			for(int j = 0; j < C; j++)
				if(!visited[i][j] && map[i][j].TID == TileType.VOID){
					q.add(i);
					q.add(j);
					visited[i][j] = true;
					while(!q.isEmpty()){
						int x = q.remove();
						int y = q.remove();
						for(int k = 0; k < 4; k++){
							int nx = x + dx[k];
							int ny = y + dy[k];
							if(nx < 0 || nx >= R || ny < 0 || ny >= C || visited[nx][ny])
								continue;
							if(map[nx][ny].TID == TileType.STONE){
								map[nx][ny] = new Wall(nx, ny, this,new Color(0x96613F));
								continue;
							}else if(map[nx][ny].TID == TileType.VOID){
								q.add(nx);
								q.add(ny);
								visited[nx][ny] = true;
							}
						}
					}
				}
			
			if(map[0][i].TID == TileType.STONE)
				map[0][i] = new Wall(0,i,this,new Color(0x96613F));
			if(map[i][0].TID == TileType.STONE)
				map[i][0] = new Wall(i,0,this,new Color(0x96613F));
			if(map[R-1][i].TID == TileType.STONE)
				map[R-1][i] = new Wall(R-1,i,this,new Color(0x96613F));
			if(map[i][C-1].TID == TileType.STONE)
				map[i][C-1] = new Wall(i,C-1,this,new Color(0x96613F));
		}
		
		entityMap = new TreeMap<Integer, DrawableObject>();
		buildNodeGraph();
	}
	
	@SuppressWarnings("unchecked")
	private void buildNodeGraph(){
		nodeGraph = new ArrayList[R*C];
		
		int[] dx = new int[]{-1,0,1,0};
		int[] dy = new int[]{0,1,0,-1};
		
		for(int i = 0; i < R; i++)
			for(int j = 0; j < C; j++){
				nodeGraph[i*C+j] = new ArrayList<Node>();
				for(int k = 0; k < 4; k++){
					int nx = i+dx[k];
					int ny = j+dy[k];
					if(nx < 0 || nx >= R || ny < 0 || ny >= C || map[nx][ny].obstructed() || !map[nx][ny].hasFloor())
						continue;
					nodeGraph[i*C+j].add(map[nx][ny].getNode());
				}
			}
	}
	
	/**
	 * Update the node graph at and around map[r][c]
	 * @param r
	 * @param c
	 */
	public void updateNodeGraph(int r, int c){
		int[] dx = new int[]{-1,0,1,0,0};
		int[] dy = new int[]{0,1,0,-1,0};
		
		for(int d = 0; d < dx.length; d++){
			int cx = r+dx[d];
			int cy = c+dy[d];
			
			if(cx < 0 || cx >= R || cy < 0 || cy >= C)
				continue;
			
			nodeGraph[cx*C+cy] = new ArrayList<Node>();
			
			for(int k = 0; k < 4; k++){
				int nx = cx+dx[k];
				int ny = cy+dy[k];
				if(nx < 0 || nx >= R || ny < 0 || ny >= C || map[nx][ny].obstructed() || !map[nx][ny].hasFloor())
					continue;
				nodeGraph[cx*C+cy].add(map[nx][ny].getNode());
			}
		}
	}
	
	/**
	 * Retrieve the tile at (r, c). Returns null if there is no such tile.
	 * @param r
	 * @param c
	 * @param t
	 */
	public Tile getTileAt(int r, int c){
		if(r < 0 || r >= R || c < 0 || c >= C)
			return null;
		return map[r][c];
	}
	
	/**
	 * Change the tile at (r, c) to t
	 * @param r
	 * @param c
	 * @param t
	 */
	public void updateTile(int r, int c, Tile t){
		if(r < 0 || r >= R || c < 0 || c >= C)
			return;
		map[r][c] = t;
		updateNodeGraph(r, c);
	}
	
	public int getRID(){
		return RID;
	}
	
	public Vector getPosition(){
		return new Vector(position);
	}
	
	public double getLayer(){
		return layer;
	}
	
	public double getDepth(double currentLayer){
		return layer - currentLayer;
	}
	
	public TreeMap<Integer, DrawableObject> getEntityMap(){
		return entityMap;
	}
	
	public ArrayList<Node>[] getNodeGraph(){
		return nodeGraph;
	}
	
	public void setPosition(Vector v){
		position = new Vector(v);
		for(int i = 0; i < R; i++)
			for(int j = 0; j < C; j++)
				map[i][j].updatePosition(i,j);
//		synchronized(entityMap){
//			for(Integer x:entityMap.keySet()){
//				DrawableObject d = entityMap.get(x);
//			}
//		}
		buildNodeGraph();
	}
	
	public void setLayer(double d){
		layer = d;
	}
	
	public ArrayDeque<Node> getAllNodesInTileRadius(Vector p, double r){
		ArrayDeque<Node> set = new ArrayDeque<Node>();
		for(Tile[] ti:map)
			for(Tile t:ti)
				if(!t.obstructed() && t.hasFloor() && t.getCenter().dis2(p) < r*r*Tile.SIZE*Tile.SIZE)
					set.add(t.getNode());
		if(set.isEmpty())
			set.add(getNearestNode(p));
		return set;
	}
	
	public ArrayDeque<Node> getNodesInTileRadius(Vector p, double r){
		ArrayDeque<Node> set = new ArrayDeque<Node>();
		for(Tile[] ti:map)
			for(Tile t:ti)
				if(!t.obstructed() && t.hasFloor() && t.getCenter().dis2(p) < r*r*Tile.SIZE*Tile.SIZE && isPathClear(t.getCenter(), p))
					set.add(t.getNode());
		if(set.isEmpty())
			set.add(getNearestNode(p));
		return set;
	}
	
	public ArrayDeque<Node> getNodesInTileRing(Vector p, double interiorRadius, double exteriorRadius){
		ArrayDeque<Node> set = new ArrayDeque<Node>();
		for(Tile[] ti:map)
			for(Tile t:ti)
				if(!t.obstructed() && t.hasFloor() && t.getCenter().dis2(p) < exteriorRadius*exteriorRadius*Tile.SIZE*Tile.SIZE && t.getCenter().dis2(p) > interiorRadius*interiorRadius*Tile.SIZE*Tile.SIZE && isPathClear(t.getCenter(), p))
					set.add(t.getNode());
		if(set.isEmpty())
			set.add(getNearestNode(p));
		return set;
	}
	
	public Node getNearestNode(Vector p){
		Node closest = null;
		for(Tile[] ti:map)
			for(Tile t:ti)
				if(!t.obstructed() && t.hasFloor() && (closest == null || t.getCenter().dis2(p) < closest.getPosition().dis2(p)))
					closest = t.getNode();
		return closest;
	}
	
	public void addEntity(DrawableObject obj){
		obj.setCurrentRoom(this);
		synchronized(entityMap){
			entityMap.put(obj.getOID(),obj);
		}
	}
	
	/**
	 * Add a drawable object to this Room at the specified room coordinates.
	 * @param obj
	 * @param v Coordinates relative to the top-left corner of the room.
	 */
	public void addEntityAt(DrawableObject obj, Vector v){
		obj.setPosition(getPosition().add(v));
		addEntity(obj);
	}
	
	/**
	 * Add a drawable object to this Room at the specified tile
	 * @param obj
	 * @param r The row coordinate, between 0 and R-1, inclusive
	 * @param c The tile coordinate, between 0 and C-1, inclusive
	 * @throws Exception 
	 */
	public void addEntityAt(DrawableObject obj, int r, int c){
		if(r < 0 || r >= R || c < 0 || c >= C){
			System.err.println("Adding " + obj + " to " + this + " at invalid coordinates: ("+r+", "+c+") when bounded by ("+R+", "+C+")");
			return;
		}
		addEntityAt(obj,map[r][c].getCenter().sub(getPosition()));
	}
	
	public void removeEntity(int OID){
		synchronized(entityMap){
			entityMap.remove(OID);
		}
	}
	
	public ArrayDeque<DrawableObject> getEntityList(){
		ArrayDeque<DrawableObject> entityList = new ArrayDeque<DrawableObject>();
		synchronized(entityMap){
			for(Integer x:entityMap.keySet())
				entityList.add(entityMap.get(x));
		}
		return entityList;
	}
	
	//TODO: Consolidate into getEntitiesWithinCircle
	public boolean actorWithinCircle(Vector p, double r){
		boolean flag = false;
		synchronized(entityMap){
			for(Integer x:entityMap.keySet())
				if(entityMap.get(x) instanceof Actor && entityMap.get(x).getPosition().dis2(p) <= r*r)
					flag = true;
		}
		return flag;
	}
	
	/**
	 * Retrieve a list of entities within the specified circle
	 * @param p the center of the circle
	 * @param r the radius of the circle
	 */
	public ArrayDeque<DrawableObject> getEntitiesWithinCircle(Vector p, double r){
		ArrayDeque<DrawableObject> ret = new ArrayDeque<DrawableObject>();
		synchronized(entityMap){
			for(Integer x:entityMap.keySet())
				if(entityMap.get(x).getPosition().dis2(p) <= r*r)
					ret.add(entityMap.get(x));
		}
		return ret;
	}
	
	/**
	 * Retrieve a list of entities intersecting the specified circle
	 * @param p the center of the circle
	 * @param r the radius of the circle
	 */
	public ArrayDeque<DrawableObject> getEntitiesIntersectingCircle(Vector p, double r){
		ArrayDeque<DrawableObject> ret = new ArrayDeque<DrawableObject>();
		synchronized(entityMap){
			for(Integer x:entityMap.keySet())
				if(entityMap.get(x).getPosition().dis2(p) <= Math.pow(entityMap.get(x).getRadius() + r, 2))
					ret.add(entityMap.get(x));
		}
		return ret;
	}
	
	/**
	 * Retrieve a list of entities intersecting or contained within a circular arc
	 * @param center The center point of the arc
	 * @param start The starting point of the arc (the arc will be treated as having radius |start-center|)
	 * @param a The angular extension of the arc in the counter-clockwise direction
	 * @return
	 */
	public ArrayDeque<DrawableObject> getEntitiesIntersectingArc(Vector center, Vector start, double a){
		ArrayDeque<DrawableObject> ret = new ArrayDeque<DrawableObject>();
		
		double r = start.sub(center).mag();
		
		double startAngle = Math.toDegrees(start.sub(center).ang());
		double angularExtent = Math.toDegrees(a);
		
		Arc2D.Double arcShape = new Arc2D.Double(center.x-r, center.y-r, 2*r, 2*r, 360-startAngle, -angularExtent, Arc2D.PIE);
		Area area = new Area(arcShape);
		
		ArrayDeque<DrawableObject> candidates = getEntitiesIntersectingCircle(center, r);
		for(DrawableObject obj : candidates){
			Vector pos = obj.getPosition();
			double R = obj.getRadius();
			Ellipse2D.Double test = new Ellipse2D.Double(pos.x-R, pos.y-R, 2*R, 2*R);
			Area testArea = new Area(test);
			
			testArea.intersect(area);
			if(!testArea.isEmpty())
				ret.add(obj);
		}
		
		return ret;
	}
	
	/**
	 * Retrieve a list of entities intersecting or contained within a circular arc
	 * @param center The center point of the arc
	 * @param start The starting point of the arc (the arc will be treated as having radius |start-center|)
	 * @param a The angular extension of the arc in the counter-clockwise direction
	 * @return
	 */
	public ArrayDeque<DrawableObject> getVisibleEntitiesIntersectingArc(Vector center, Vector start, double a){
		ArrayDeque<DrawableObject> ret = new ArrayDeque<DrawableObject>();
		
		double r = start.sub(center).mag();
		
		double startAngle = Math.toDegrees(start.sub(center).ang());
		double angularExtent = Math.toDegrees(a);
		
		Arc2D.Double arcShape = new Arc2D.Double(center.x-r, center.y-r, 2*r, 2*r, 360-startAngle, -angularExtent, Arc2D.PIE);
		Area area = new Area(arcShape);
		
		ArrayDeque<DrawableObject> candidates = getEntitiesIntersectingCircle(center, r);
		for(DrawableObject obj : candidates){
			Vector pos = obj.getPosition();
			double R = obj.getRadius();
			Ellipse2D.Double test = new Ellipse2D.Double(pos.x-R, pos.y-R, 2*R, 2*R);
			Area testArea = new Area(test);
			
			testArea.intersect(area);
			if(!testArea.isEmpty() && isPathClear(center,pos))
				ret.add(obj);
		}
		
		return ret;
	}
	
	/**
	 * Given two points within the room, determine whether the path can be traversed
	 * @param a
	 * @param b
	 * @return true if nothing blocks the path, such as void, wall, and water tiles
	 */
	public boolean isPathClear(Vector a, Vector b){
		return isPathClear(a,b,0);
	}
	
	/**
	 * Given two points within the room and a path radius, determine whether the path can be traversed
	 * @param a
	 * @param b
	 * @param r The radius of the path
	 * @return true if nothing blocks the path, such as void, wall, and water tiles
	 */
	public boolean isPathClear(Vector a, Vector b, double r){
		for(Tile[] row:map)
			for(Tile t:row)
				if(t.intersectsSegment(a, b, r))
					return false;
		
		//Intersect along the room's bounding box
		int[] dx = {0,1,1,0};
		int[] dy = {0,0,1,1};
		for(int i = 0; i < 4; i++)
			if(Geometry.segmentIntersect(a, b, position.add(new Vector(dx[i]*Tile.SIZE*C, dy[i]*Tile.SIZE*R)), position.add(new Vector(dx[(i+1)%4]*Tile.SIZE*C, dy[(i+1)%4]*Tile.SIZE*R))))
				return false;
		
		return true;
	}
	
	public void collideWithSolids(DrawableObject obj, double elapsedSeconds){
		synchronized(entityMap){
			Collision.collideObjectWithObjects(obj, getEntityList(), elapsedSeconds);
			Collision.collideObjectWithRoom(map, obj, elapsedSeconds);
		}
	}
	
	public void clean(){
		ArrayDeque<Integer> remove = new ArrayDeque<Integer>();
		synchronized(entityMap){
			for(Integer e:entityMap.keySet())
				if(entityMap.get(e).isDead())
					remove.add(e);
		}
		
		for(Integer e:remove)
			entityMap.get(e).delete();
	}
	
	public void updateAll(double elapsedSeconds){
		for(Tile[] row:map)
			for(Tile t:row)
				t.update(elapsedSeconds);
		
		Portal transfer = null;
		
		synchronized(entityMap){			
			for(Integer oid:entityMap.keySet()){
				entityMap.get(oid).update(elapsedSeconds);
				if(entityMap.get(oid) instanceof Portal){
					Portal p = (Portal)entityMap.get(oid);
					if(transfer == null && p.canTransfer())
						transfer = p;
				}
			}
		}
		
		if(transfer != null)
			transfer.transfer();
	}
	
	public void drawAll(Graphics2D g, double elapsedSeconds, Camera cam){
		
		double L = getDepth(RoomManager.getCurrentRoom().getLayer());
		
		if(L < 0)
			return;
		
		if(getRID() != RoomManager.getCurrentRoom().getRID())
			g = new DimmerGraphics(g,1/(2+L*L));
		
		double prevScale = cam.getScale();
		cam.setScale(1/(1+L*.75) * cam.getScale());
//		cam = new Camera(cam.getPosition(), 1/(1 + L*.75)*cam.scale());
		
		//Get bounding box
		double[] bound = cam.getBoundingBox();
		//Compute draw bounds
		int left = (int)Math.floor((bound[0] - position.x)/Tile.SIZE);
		int top = (int)Math.floor((bound[1] - position.y)/Tile.SIZE);
		int right = (int)Math.floor((bound[2] - position.x)/Tile.SIZE);
		int bot = (int)Math.floor((bound[3] - position.y)/Tile.SIZE);
		
		//Only draw visible tiles
		for(int i = Math.max(top,0); i <= Math.min(bot,R-1); i++)
			for(int j = Math.max(left,0); j <= Math.min(right,C-1); j++)
				map[i][j].drawBody(g, elapsedSeconds, cam);
		
//		g.setColor(Color.cyan);
//		for(int i = Math.max(top,0); i <= Math.min(bot,R-1); i++)
//			for(int j = Math.max(left,0); j <= Math.min(right,C-1); j++){
//				Node node = map[i][j].getNode();
//				for(Node x:nodeGraph[node.n])
//					g.drawLine((int)cam.xTranslatePosition(node.getPosition().x), (int)cam.yTranslatePosition(node.getPosition().y), (int)cam.xTranslatePosition((x.getPosition().x+node.getPosition().x)/2), (int)cam.yTranslatePosition((x.getPosition().y+node.getPosition().y)/2));
//			}
//		
//		g.setColor(Color.cyan.darker());
//		for(int i = Math.max(top,0); i <= Math.min(bot,R-1); i++)
//			for(int j = Math.max(left,0); j <= Math.min(right,C-1); j++){
//				Node node = map[i][j].getNode();
//				g.fillRect((int)cam.xTranslatePosition(node.getPosition().x)-5, (int)cam.yTranslatePosition(node.getPosition().y)-5, 10, 10);
//			}
		
		synchronized(entityMap){
			for(Integer e:entityMap.keySet())
				entityMap.get(e).drawBody(g, elapsedSeconds, cam);
		}
		
		cam.setScale(prevScale);
	}
	
	public int compareTo(Room r){
		return (int)Math.signum(r.getLayer()-layer);
	}
	
	public String toString(){
		return "Room " + RID;
	}
}
