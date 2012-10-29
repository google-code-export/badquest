package util;

import java.util.ArrayDeque;
import java.util.Arrays;

public class Geometry {
	static double EPS = 1e-9;
	/**
	 * Returns the convex hull in counterclockwise order
	 * Notes: Sorts p, pass a copy if this is an issue. If consecutive points
	 *		  on the hull are collinear, will include all of them 
	 * @param p The set of points
	 * @return The set of points on the hull of p.
	 */
	public static Vector[] hull(Vector[] p){
		int n = p.length;
		Vector[] b = new Vector[n];
		Vector[] u = new Vector[n];
		Arrays.sort(p);
		int bi = 1,ui = 1;
		b[0] = p[0];
		b[1] = p[1];
		//Compute lower component
		for(int i = 2; i < n; i++){
			while(bi >= 1){
				Vector bot = b[bi].sub(b[bi-1]);
				Vector top = p[i].sub(b[bi]);
				if(top.cross(bot) <= EPS)
					break;
				bi--;
			}
			bi++;
			b[bi] = p[i];
		}
		u[0] = p[n-1];
		u[1] = p[n-2];
		ui = 1;
		//Compute upper component
		for(int i = n-3; i >= 0; i--){
			while(ui >= 1){
				Vector bot = u[ui].sub(u[ui-1]);
				Vector top = p[i].sub(u[ui]);
				if(top.cross(bot) <= EPS)
					break;
				ui--;
			}
			ui++;
			u[ui] = p[i]; 
		}
		//Stitch together top and bottom components
		int unique = ui + bi;
		Vector[] hull = new Vector[unique];	
		for(int i = 0; i <= bi; i++)
			hull[i] = b[i];
		for(int j = 1; j < ui; j++)
			hull[bi+j] = u[j];
		return hull;
	}
	
	/**
	 * Clips the polygon specified in p to the half-plane AB
	 * If a point b lies on the left of AB (AB cross b > 0), it will be included in the clipping
	 * Note: The polygon returned will not be simple, and may include zero-area paths or repetitions of the same vertex
	 * @param A A point on line AB 
	 * @param B A point on line AB
	 * @param p The polygon to be clipped
	 * @return The clipped polygon. Note that the result may include some degenerate faces, in which case running hull should alleviate this.
	 */
	public static Vector[] clip(Vector A, Vector B, Vector[] p){
		ArrayDeque<Vector> ret = new ArrayDeque<Vector>();
		int n = p.length;
		Vector C = B.sub(A);
		
		for(int i = 0; i < n; i++){
			Vector a = p[i];
			Vector b = p[(i+1)%n];
			
			Vector hit = llIntersect(a,b,A,B);
				
			if(C.cross(a.sub(A)) > -EPS && C.cross(b.sub(A)) > -EPS){
				ret.add(b);
			}else if(C.cross(a.sub(A)) < -EPS && C.cross(b.sub(A)) > -EPS){
				ret.add(hit);
				ret.add(b);
			}else if(C.cross(a.sub(A)) > -EPS && C.cross(b.sub(A)) < -EPS){
				ret.add(hit);
			}
		}

		return ret.toArray(new Vector[ret.size()]);
	}
	
	/**
	 * @param p The polygon whose area is to be computed
	 * @return The area of the polygon.
	 */
	public static double area(Vector[] p){
		int n = p.length;
		double a = 0;
		for(int i = 0; i < n; i++)
			a += p[i].cross(p[(i+1)%n]);
		return Math.abs(a/2);
	}
	
	/**
	 * Coincident case listed, handling omitted (problem-specific)
	 * Returns: List of intersecting points or an empty list if none
	 * @param a The center of circle A.
	 * @param R The radius of circle A.
	 * @param b The center of circle B.
	 * @param r The radius of circle B.
	 * @return The set of points the two circles have in common. If the circles are coincident, the empty set will be returned.
	 */
	public static ArrayDeque<Vector> ccIntersect(Vector a, double R, Vector b, double r){
		ArrayDeque<Vector> ret = new ArrayDeque<Vector>();
		if(Math.abs(a.sub(b).mag()-(R+r)) < EPS){ //Circles intersect at one Vector
			ret.add(a.sub(b).norm().scale(r).add(b));
			return ret;
		}
		if(a.sub(b).mag() < EPS && Math.abs(R-r)<EPS) //Infinitely many solutions
			return ret;
		if(a.sub(b).mag() > R+r || a.sub(b).mag() < Math.abs(R-r)) //No intersection
			return ret;
		
		double d = a.sub(b).mag();
		double A = (R*R - r*r + d*d)/(2*d);
		double H = Math.sqrt(R*R-A*A);
		
		Vector top = a.add(b.sub(a).norm().scale(A)).add(b.sub(a).norm().orthoCCW().scale(H));
		Vector bot = a.add(b.sub(a).norm().scale(A)).add(b.sub(a).norm().orthoCW().scale(H));
		ret.add(top);
		ret.add(bot);
		
		return ret;
	}
	
	/**
	 * @param a A point on line AB.
 	 * @param b A point on line AB, not equivalent to B.
	 * @param p The point to be tested.
	 * @return The distance from p to line AB.
	 */
	public static double plDist(Vector a, Vector b, Vector p){
		Vector c = p.sub(a);
		Vector d = b.sub(a);
		return Math.abs(c.cross(d))/d.mag();
	}
	
	/**
	 * Finds the value of s in the equation P = A + (B-A)*s. Assumes P lies on line AB.
	 * @param a Point A on line AB.
	 * @param b Point B on line AB.
	 * @param p The point to solve for, assumed to be on line AB.
	 * @return The scaling of p.
	 */
	public static double findScale(Vector a, Vector b, Vector p){
		Vector c = b.sub(a);
		Vector d = p.sub(a);
		return Math.signum(c.dot(d)) * d.mag()/c.mag();
	}
	
	/**
     * @param a The start of segment AB.
 	 * @param b The end of segment AB.
	 * @param p The point to be tested.
	 * @return The distance from p to segment AB.
	 */
	public static double plsDist(Vector a, Vector b, Vector p){
		Vector c = p.sub(a);
		Vector d = b.sub(a);
		if(c.dot(d) < 0)
			return p.dis(a);
		if(p.sub(b).dot(d.scale(-1)) < 0)
			return p.dis(b);
		return Math.abs(c.cross(d))/d.mag();
	}
	
	/**
	 * Returns the intersection of two lines. Note that AB and CD should be of non-zero length.
	 * Notes: Detects coincidence, handling left for a per-problem basis
	 * @param a A point on line AB.
	 * @param b A point on line AB.
	 * @param c A point on line CD.
	 * @param d A point on line CD.
	 * @return Null if the lines are parallel, the point of intersection otherwise.
	 */
	public static Vector llIntersect(Vector a, Vector b, Vector c, Vector d){
		Vector e = d.sub(c), f = a.sub(c), g = b.sub(a);
		double AB = e.cross(f);
//		double CD = g.cross(f);
		double D = g.cross(e);
		
		//if AB == CD == D == 0, then the lines are coincident
		//if D == 0, the lines are parallel
		if(Math.abs(D) < EPS)
			return null;
		
		return a.add(g.scale(AB/D));
	}
	
	/**
	 * @param a A on segment AB
	 * @param b B on segment AB
	 * @param c C on segment CD
	 * @param d D on segment CD
	 * @return True if the two segments share at least one common point, false otherwise.
	 */
	public static boolean segmentIntersect(Vector a, Vector b, Vector c, Vector d){
		Vector e = b.sub(a);
		Vector f = d.sub(c);
		if(Math.abs(c.sub(a).cross(e)) < EPS && Math.abs(d.sub(a).cross(e)) < EPS)
			return (c.sub(a).dot(e) > -EPS && c.sub(b).dot(a.sub(b)) > -EPS) || (d.sub(a).dot(e) > -EPS && d.sub(b).dot(a.sub(b)) > -EPS) ||
			       (a.sub(c).dot(f) > -EPS && a.sub(d).dot(c.sub(d)) > -EPS) || (b.sub(c).dot(f) > -EPS && b.sub(d).dot(c.sub(d)) > -EPS);
		return Math.signum(b.sub(c).cross(f)) != Math.signum(a.sub(c).cross(f)) && Math.signum(c.sub(a).cross(e)) !=  Math.signum(d.sub(a).cross(e));
	}
	
	/**
	 * Finds the two points at which line AB passes through the circle centered at C.
	 * @param c Center of circle C.
	 * @param r Radius of circle C.
	 * @param a A point on line AB.
	 * @param b A point on line AB.
	 * @return List of intersecting points or an empty list if none.
	 */
	public static ArrayDeque<Vector> clIntersect(Vector c, double r, Vector a, Vector b){
		ArrayDeque<Vector> ret = new ArrayDeque<Vector>();
		Vector ba = b.sub(a);
		
		double d = plDist(a,b,c);
		double h = Math.sqrt(r*r-d*d);
		if(d > r+EPS) return ret;
		
		Vector mid = (Math.signum(ba.cross(c.sub(a)))>0?ba.orthoCW():ba.orthoCCW()).norm().scale(d).add(c);
		Vector left = mid.add(ba.norm().scale(-h));
		Vector right = mid.add(ba.norm().scale(h));
		
		ret.add(left);
		if(d < r-EPS) ret.add(right);
		return ret;
	}
}
