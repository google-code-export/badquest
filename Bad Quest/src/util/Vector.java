package util;

public class Vector {
	public double x,y;
	/**
	 * Returns a copy of the given Vector.
	 * @param v
	 */
	public Vector(Vector v){
		setTo(v);
	}
	/**
	 * Returns a Vector with the given dimensions.
	 * @param x
	 * @param y
	 */
	public Vector(double x, double y){
		this.x = x;
		this.y = y;
	}
	/**
	 * Returns a Vector with the given angle and magnitude 1.
	 * @param angle
	 */
	public Vector(double angle){
		x = Math.cos(angle);
		y = Math.sin(angle);
	}
	
	//Static methods
	/**
	 * Add vector b to vector a, store in a
	 * @param a
	 * @param b
	 */
	public static void add(Vector a, Vector b){
		a.x = a.x+b.x;
		a.y = a.y+b.y;
	}
	
	//Object stuff
	public void setTo(Vector v){
		x = v.x;
		y = v.y;
	}
	public int compareTo(Vector p){
		if(p.x == x)
			return (int)Math.signum(y-p.y);
		return (int)Math.signum(x-p.x);	
	}
	public String toString(){
		return "("+x+", "+y+")";
	}
	
	//Vector operations
	public Vector sub(Vector p){
		return new Vector(x-p.x, y-p.y);
	}
	public Vector add(Vector p){
		return new Vector(x+p.x, y+p.y);
	}
	public Vector scale(double s){
		return new Vector(x*s, y*s);
	}
	public Vector rot(double t){ //Rotate t radians counter-clockwise
		double c = Math.cos(t);
		double s = Math.sin(t);	
		return new Vector(x*c-y*s, x*s+y*c);
	}
	public Vector orthoCCW(){ //Rotate 90 degrees counter-clockwise
		return new Vector(-y, x);
	}
	public Vector orthoCW(){ //Rotate 90 degrees clockwise
		return new Vector(y, -x);
	}
	public Vector norm(){ //Return a vector with same angle and magnitude 1
		return new Vector(x/mag(), y/mag());
	}
	public Vector project(Vector p){ //Return projection of this onto p
		return p.scale(dot(p)/p.dot(p));
	}
	public double dot(Vector p){
		return x*p.x + y*p.y;
	}
	public double cross(Vector p){
		return x*p.y-y*p.x;
	}
	public double mag2(){
		return dot(this);
	}
	public double mag(){
		return Math.sqrt(mag2());
	}
	public double dis(Vector p){
		return sub(p).mag();
	}
	public double ang(){
		return (Math.atan2(y,x) + 2*Math.PI)%(2*Math.PI);
	}
	public double angBetween(Vector p){ //Returns inner angle between two vectors
		double d = Math.abs(ang()-p.ang());
		return Math.min(d, 2*Math.PI-d);
	}
}
