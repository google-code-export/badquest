package world;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;

import util.DisjointSet;
import world.tile.Dirt;
import world.tile.Stone;
import world.tile.Tile;
import world.tile.Tile.TileType;
import world.tile.Wall;
import world.tile.Wire;

public class DebugRoomGenerator {
	static int minWallLength = 3;
	static double bendDist = .5;
	static double bendicity = .1;
	static double doorsPerWall = .7;
	public static Tile[][] makeRoom(Room caller, int R, int C, long seed){
		Random rand = new Random(seed);
		Tile[][] ret = new Tile[R][C];
		
		for(int i = 0; i < R; i++)
			for(int j = 0; j < C; j++)
				ret[i][j] = new Wall(j, i, caller);
		
		for(int i = 1; i < R-1; i++)
			for(int j = 1; j < C-1; j++)
				ret[i][j] = new Stone(j, i, caller);
		
		LinkedList<Integer> bends = new LinkedList<Integer>(); 
		
		int height = rand.nextInt(R-2*minWallLength)+minWallLength;
		double chance = -bendDist;
		for(int i = 1; i < C-1; i++){
			ret[height][i] = new Wall(i,height,caller);
			chance += bendicity;
			
			//We're bending!
			if(i < C-3 && rand.nextDouble() < chance){
				chance = -bendDist;
				
				boolean canBendUp = height >= 2*minWallLength;
				boolean canBendDown = height < R-2*minWallLength;
				
				if(canBendUp && canBendDown && rand.nextDouble() > .5 || canBendUp && !canBendDown){
					bends.add(height); bends.add(i);
					int to = rand.nextInt(height-2*minWallLength+1)+minWallLength;
					for(; height > to; height--)
						ret[height][i] = new Wall(i,height,caller);
					bends.add(height); bends.add(i);
				}else if(canBendDown){
					bends.add(height); bends.add(i);
					int to = rand.nextInt(R-height-2*minWallLength)+height+minWallLength;
					for(; height < to; height++)
						ret[height][i] = new Wall(i,height,caller);
					bends.add(height); bends.add(i);
				}
				
				ret[height][i] = new Dirt(i,height,caller);
			}
		}
		
		int[] dx = {0,1,0,-1};
		int[] dy = {1,0,-1,0};
		while(!bends.isEmpty()){
			int r = bends.remove();
			int c = bends.remove();
			
			ArrayList<Integer> dir = new ArrayList<Integer>();
			for(int k = 0; k < 4; k++){
				int nx = r+dx[k];
				int ny = c+dy[k];
				if(ret[nx][ny].TID == TileType.STONE)
					dir.add(k);
			}
			
			if(dir.size() == 0 || rand.nextDouble() < .1) continue;
			
			int z = dir.get(rand.nextInt(dir.size()));
			int x = r + dx[z];	
			int y = c + dy[z];
			
			boolean initialCheck = false;
			for(int k = -1; k <= 1; k++)
				initialCheck |= (ret[x+dx[(z+k+4)%4]][y+dy[(z+k+4)%4]].TID != TileType.STONE);
			
			while(!initialCheck){
				ret[x][y] = new Wall(x,y,caller);
				
				boolean flag = false;
				for(int k = -1; k <= 1; k++)
					flag |= (ret[x+dx[(z+k+4)%4]][y+dy[(z+k+4)%4]].TID != TileType.STONE);
				
				if(flag) break;
					
				x += dx[z];
				y += dy[z];
			}
		}
		
		/****************
		 * Label chambers
		 ****************/
		
		int chamberCount = 0; //The number of chambers in this room
		int[][] color = new int[R][C]; //Coloring used to distinguish separate chambers
		for(int[] c:color)
			for(int i = 0; i < c.length; i++)
				c[i] = -1; //Initialize colorings to -1
		
		for(int i = 0; i < R; i++)
			for(int j = 0; j < C; j++)
				if(ret[i][j].TID == TileType.STONE && color[i][j] == -1){
					ArrayDeque<Integer> q = new ArrayDeque<Integer>();
					
					q.add(i);
					q.add(j);
					
					color[i][j] = chamberCount;
					
					while(!q.isEmpty()){
						int cx = q.remove();
						int cy = q.remove();
						
						for(int k = 0; k < 4; k++){
							int nx = cx + dx[k];
							int ny = cy + dy[k];
							if(ret[nx][ny].TID == TileType.STONE && color[nx][ny] == -1){
								q.add(nx);
								q.add(ny);
								color[nx][ny] = chamberCount;
							}
						}
					}
					
					chamberCount++;
				}
		
		/****************
		 * Find walls
		 ****************/
		
		TreeMap<Edge, WallSet> map = new TreeMap<Edge, WallSet>();
		for(int i = 0; i < R; i++)
			for(int j = 0; j < C; j++)
				if(color[i][j] == -1)
					for(int k = 0; k < 2; k++){
						int nxl = i + dx[k];
						int nyl = j + dy[k];
						int nxr = i + dx[k+2];
						int nyr = j + dy[k+2];
						
						if(nxl < 0 || nxl >= R || nyl < 0 || nyl >= C || nxr < 0 || nxr >= R || nyr < 0 || nyr >= C)
							continue;
						
						int s = color[nxl][nyl];
						int t = color[nxr][nyr];
						
						if(s > t){
							int temp = s;
							s = t;
							t = temp;
						}
						
						if(s == -1 || t == -1 || s == t)
							continue;
						
						Edge edge = new Edge(s, t);
						if(!map.containsKey(edge))
							map.put(edge, new WallSet(s,t));
						
						map.get(edge).addWall(i, j);
					}
		
		/****************
		 * Run Random Spanning Tree (RST) to ensure connectivity
		 ****************/
		
		Edge[] edges = map.keySet().toArray(new Edge[0]);
		boolean[] usedEdge = new boolean[edges.length];
		DisjointSet djs = new DisjointSet(chamberCount);
		
		for(int i = 0; !djs.singleSet() && i < edges.length; i++){
			int j = rand.nextInt(edges.length - i);
			if(djs.find(edges[j].s) != djs.find(edges[j].t)){
				WallSet w = map.get(edges[j]);
				w.openDoor(ret, caller);
				djs.union(edges[j].s, edges[j].t);
				usedEdge[edges.length-1-i] = true;
			}
			
			Edge temp = edges[edges.length-1-i];
			edges[edges.length-1-i] = edges[j];
			edges[j] = temp;
		}
		
		/****************
		 * Open additional doors
		 ****************/
		
		int extraDoors = (int)Math.ceil(doorsPerWall*edges.length - chamberCount);
		for(int i = 0; i < extraDoors; i++){
			int j = rand.nextInt(edges.length);
			WallSet w = map.get(edges[j]);
			w.openDoor(ret, caller);
		}

		return ret;
	}
	
	public static class Edge implements Comparable<Edge>{
		int s,t;
		public Edge(int _s, int _t){
			s = _s;
			t = _t;
		}
		public int compareTo(Edge e){
			if(s == e.s)
				return t - e.t;
			return s - e.s;
		}
	}
	
	/**
	 * Denotes a set of Wall tiles between rooms A and B.
	 */
	public static class WallSet{
		TreeSet<Edge> set = new TreeSet<Edge>();
		int a, b; //The two rooms this wall spans
		
		public WallSet(int _a, int _b){
			a = Math.min(_a, _b);
			b = Math.max(_a, _b);
		}
		
		public void addWall(int x, int y){
			set.add(new Edge(x, y));
		}
		
		public void openDoor(Tile[][] map, Room caller){
			Edge[] wall = set.toArray(new Edge[0]);
			int n = wall.length;
			
			map[wall[n/2].s][wall[n/2].t] = new Wire(wall[n/2].s, wall[n/2].t, caller);
			if(n > 1) map[wall[n/2-1].s][wall[n/2-1].t] = new Wire(wall[n/2-1].s, wall[n/2-1].t, caller);
		}
	}
}
