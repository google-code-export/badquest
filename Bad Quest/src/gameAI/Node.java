package gameAI;

import util.Vector;

public class Node{
	private Vector p;
	public final int n;
	public Node(Vector v, int _n){
		p = new Vector(v);
		n = _n;
	}
	public Vector getPosition(){
		return new Vector(p);
	}
	public double dis2(Node node){
		return p.dis2(node.getPosition());
	}
	public double dis(Node node){
		return p.dis(node.getPosition());
	}
}
