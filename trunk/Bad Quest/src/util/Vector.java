package util;

public class Vector {
	/**
	 * Vector with elements (0, 0).
	 */
	public static final Vector ZERO = new Vector(0,0);
	
	public double x,y;
	
	//Constructors
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
	
	/**
	 * Subtract vector b from vector a, store in a
	 * @param a
	 * @param b
	 */
	public static void sub(Vector a, Vector b){
		a.x = a.x-b.x;
		a.y = a.y-b.y;
	}
	
	//Object stuff
	/**
	 * Sets this vector's elements equivalent to those in v.
	 * @param v
	 */
	public void setTo(Vector v){
		x = v.x;
		y = v.y;
	}
	/**
	 * Sorts by x, then by y. If either coordinate is within 1e-9 of its counterpart in p, the coordinate is deemed identical.
	 * @param p
	 * @return
	 */
	public int compareTo(Vector p){
		if(Math.abs(x-p.x) < Geometry.EPS){
			if(Math.abs(y-p.y) < Geometry.EPS)
				return 0;
			return (int)Math.signum(y-p.y);
		}
		return (int)Math.signum(x-p.x);	
	}
	public String toString(){
		return "("+x+", "+y+")";
	}
	
	//Vector operations
	/**
	 * Returns the vector equivalent to (this - p).
	 * @param p
	 * @return
	 */
	public Vector sub(Vector p){
		return new Vector(x-p.x, y-p.y);
	}
	/**
	 * Returns the vector equivalent to (this + p).
	 * @param p
	 * @return
	 */
	public Vector add(Vector p){
		return new Vector(x+p.x, y+p.y);
	}
	/**
	 * Returns the vector equivalent to (s*this).
	 * @param p
	 * @return
	 */
	public Vector scale(double s){
		return new Vector(x*s, y*s);
	}
	/**
	 * Scale this vector to the specified length, maintaining direction.
	 * @param s
	 * @return
	 */
	public Vector scaleTo(double s){
		return norm().scale(s);
	}
	/**
	 * Rotate this vector t radians counter-clockwise.
	 * @param t The angle to be rotated, in radians.
	 * @return
	 */
	public Vector rot(double t){
		double c = Math.cos(t);
		double s = Math.sin(t);	
		return new Vector(x*c-y*s, x*s+y*c);
	}
	/**
	 * Returns a vector orthogonal to this one in the counter-clockwise direction, of the same magnitude.
	 * @return
	 */
	public Vector orthoCCW(){ //Rotate 90 degrees counter-clockwise
		return new Vector(-y, x);
	}
	/**
	 * Returns a vector orthogonal to this one in the clockwise direction, of the same magnitude.
	 * @return
	 */
	public Vector orthoCW(){ //Rotate 90 degrees clockwise
		return new Vector(y, -x);
	}
	/**
	 * Returns a unit vector in the same direction as this.
	 * @return
	 */
	public Vector norm(){ //Return a vector with same angle and magnitude 1
		return new Vector(x/mag(), y/mag());
	}
	/**
	 * Returns the orthogonal projection of this onto the vector p.
	 * @return
	 */
	public Vector project(Vector p){
		return p.scale(dot(p)/p.dot(p));
	}
	/**
	 * Returns the scalar product between this and p.
	 * @param p
	 * @return
	 */
	public double dot(Vector p){
		return x*p.x + y*p.y;
	}
	/**
	 * Returns the cross product between this and p. If the sign on the result is positive, "this" is making a right turn into p. If the sign is negative, the opposite is true. A sign of zero indicates the two vectors describe the same direction.
	 * @param p
	 * @return
	 */
	public double cross(Vector p){
		return x*p.y-y*p.x;
	}
	/**
	 * Returns the magnitude of this vector, squared.
	 * @return
	 */
	public double mag2(){
		return dot(this);
	}
	/**
	 * Returns the magnitude of this vector.
	 * @return
	 */
	public double mag(){
		return Math.sqrt(mag2());
	}
	/**
	 * Returns the distance between this point and p, squared.
	 * @param p
	 * @return
	 */
	public double dis2(Vector p){
		return sub(p).mag2();
	}
	/**
	 * Returns the distance between this point and p.
	 * @param p
	 * @return
	 */
	public double dis(Vector p){
		return sub(p).mag();
	}
	/**
	 * Returns the direction of this vector, described by an angle counter-clockwise from the positive x axis.
	 * @return A value between 0, inclusive, and 2*Pi, exclusive
	 */
	public double ang(){
		double a = Math.atan2(y,x);
		if(a < 0)
			a += 2*Math.PI;
		return a;
	}
	/**
	 * Returns the smaller of the two angles between the two vectors.
	 * @param p
	 * @return A value between 0 and Pi, inclusive.
	 */
	public double angBetween(Vector p){
		double d = Math.abs(ang()-p.ang());
		return Math.min(d, 2*Math.PI-d);
	}
}
