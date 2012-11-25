package gameAI;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

public class Pathfinding {
	public static ArrayDeque<Node> routeTo(Node start, Node end, ArrayList<Node>[] g){
		ArrayDeque<Node> ret = new ArrayDeque<Node>();
		PriorityQueue<Pair> q = new PriorityQueue<Pair>();
		
		double[] dist = new double[g.length];
		Arrays.fill(dist, 1e17);
		Node[] prev = new Node[g.length];
		
		q.add(new Pair(start, 0, start.dis(end)));
		dist[start.n] = q.peek().w();
		
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
				if(nd + nh < dist[x.n]){
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
