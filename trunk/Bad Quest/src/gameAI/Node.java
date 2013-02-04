package gameAI;

import java.util.TreeSet;

import util.Vector;

public class Node{
	private Vector p;
	public final int n;
	public TreeSet<Integer> traffic;
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
	public String toString(){
		return p.toString();
	}
}
