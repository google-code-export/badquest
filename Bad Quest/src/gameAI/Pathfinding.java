package gameAI;

import gameObjects.DrawableObject;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

import util.Vector;

public class Pathfinding {
	public static boolean isPathClear(DrawableObject cur, Vector target){
		return cur.getCurrentRoom().isPathClear(cur.getPosition(), target, cur.getRadius());
	}
	
	public static ArrayDeque<Node> routeTo(Vector c, ArrayDeque<Node> startList, ArrayDeque<Node> endList, ArrayList<Node>[] g){
		ArrayDeque<Node> ret = new ArrayDeque<Node>();
		PriorityQueue<Pair> q = new PriorityQueue<Pair>();
		
		boolean[] end = new boolean[g.length];
		double[] dist = new double[g.length];
		Arrays.fill(dist, 1e17);
		Node[] prev = new Node[g.length];
		
		Vector avg = Vector.ZERO;
		for(Node e:endList){
			avg = avg.add(e.getPosition());
			end[e.n] = true;
		}
		
		avg.scale(1./endList.size());
		
		for(Node s:startList){
			double d = c.dis(s.getPosition());
			double h = s.getPosition().dis(avg);
			q.add(new Pair(s, d, h));
			dist[s.n] = d+h;
		}
		
		Node landed = null;
		
		//A*
		while(!q.isEmpty()){
			Pair cur = q.remove();
			Node pos = cur.node;
			double d = cur.d;
			
			if(dist[pos.n] < cur.w())
				continue;
			
			if(end[pos.n]){
				landed = pos;
				break;
			}
			
			for(Node x:g[pos.n]){
				double nd = d + x.dis(pos);
				double nh = x.getPosition().dis(avg);
				if(nd + nh < dist[x.n] || (nd + nh == dist[x.n] && Math.random() > .5)){
					dist[x.n] = nd + nh;
					q.add(new Pair(x, nd, nh));
					prev[x.n] = pos; 
				}
			}
		}
		
		if(landed == null)
			return ret;
		
		//Build node path
		ret.add(landed);
		int p = landed.n;
		while(prev[p] != null){
			ret.addFirst(prev[p]);
			p = prev[p].n;
		}
		
		//Return node path
		return ret;
	}
	
	public static ArrayDeque<Node> routeTo(Vector c, ArrayDeque<Node> start, Node end, ArrayList<Node>[] g){
		ArrayDeque<Node> ret = new ArrayDeque<Node>();
		PriorityQueue<Pair> q = new PriorityQueue<Pair>();
		
		double[] dist = new double[g.length];
		Arrays.fill(dist, 1e17);
		Node[] prev = new Node[g.length];
		
		for(Node s:start){
			double d = c.dis(s.getPosition());
			double h = s.dis(end);
			q.add(new Pair(s, d, h));
			dist[s.n] = d+h;
		}
		
		//A*
		while(!q.isEmpty()){
			Pair cur = q.remove();
			Node pos = cur.node;
			double d = cur.d;
			
			if(dist[pos.n] < cur.w())
				continue;
			
			if(pos.n == end.n)
				break;
			
			for(Node x:g[pos.n]){
				double nd = d + x.dis(pos);
				double nh = x.dis(end);
				if(nd + nh < dist[x.n] || (nd + nh == dist[x.n] && Math.random() > .5)){
					dist[x.n] = nd + nh;
					q.add(new Pair(x, nd, nh));
					prev[x.n] = pos; 
				}
			}
		}
		
		if(dist[end.n] == 1e17)
			return ret;
		
		//Build node path
		ret.add(end);
		int p = end.n;
		while(prev[p] != null){
			ret.addFirst(prev[p]);
			p = prev[p].n;
		}
		
		//Return node path
		return ret;
	}
	
	public static class Pair implements Comparable<Pair>{
		Node node;
		double d,h;
		public Pair(Node _node, double _d, double _h){
			node = _node;
			d = _d;
			h = _h;
		}
		public double w(){
			return d+h;
		}
		public int compareTo(Pair p){
			return (int)Math.signum(w() - p.w());
		}
	}
}
