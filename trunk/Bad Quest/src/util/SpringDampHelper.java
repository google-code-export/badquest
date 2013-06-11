package util;

public class SpringDampHelper {
	private double springStiffness = 0.9, 
			   springDampening = .08, 
			   assumedMass = 1;
	
	private Vector velocity = new Vector(0,0);
	
	/**
	 * @param b The base of the spring
	 */
	public SpringDampHelper(){
	}
	
	public SpringDampHelper(double stiffness, double dampening, double mass){
		springStiffness = stiffness;
		springDampening = dampening;
		assumedMass = mass;
	}
	
	public void setVelocity(Vector v){
		velocity = new Vector(v);
	}
	
	public Vector getVelocity(Vector base, Vector position){
		Vector d = base.sub(position);
		Vector f1 = d.scale(springStiffness*assumedMass);
		Vector f2 = velocity.scale(-springDampening*assumedMass);
		velocity = f1.add(f2);
		return velocity;
	}
}
