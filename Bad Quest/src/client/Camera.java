package client;

import gameObjects.DrawableObject;
import util.SpringDampHelper;
import util.Vector;

/**
 * Camera!
 */
public class Camera {
	private int viewHeight = GameClient.frameHeight;
	private int viewWidth = GameClient.frameWidth;
	private double scale;
	private double dragPerSecond = .9;
	private SpringDampHelper spring = new SpringDampHelper(1.2,.08,1.7);
	
	private Vector position = new Vector(0,0); //Position in the game world
	private Vector velocity = new Vector(0,0);
	private Vector center = new Vector(viewWidth/2, viewHeight/2); //Center of view-box
	
	private DrawableObject follow = null;
	
	private final double DEFAULT_SCALE = 1;
	
	/**
	 * Returns a camera centered on position pos, with default scaling.
	 * @param pos
	 */
	public Camera(Vector pos){
		position.setTo(pos);
		scale = DEFAULT_SCALE;
	}
	
	/**
	 * Returns a camera centered on position pos, with specified scaling.
	 * @param pos
	 * @param scale
	 */
	public Camera(Vector pos, double scale){
		position.setTo(pos);
		this.scale = scale;
	}
	
	public Vector getPosition(){
		return position;
	}
	
	public void setPosition(Vector v){
		position.setTo(v);
	}
	
	public void follow(DrawableObject follow){
		this.follow = follow;
	}
	
	/**
	 * Returns a reference to the follow object.
	 * @return
	 */
	public DrawableObject getFocus(){
		return follow;
	}
	
	public void setScale(double s){
		scale = s;
	}
	
	/**
	 * Translates cx from world coordinates to camera coordinates
	 * @param cx
	 * @return
	 */
	public double xTranslatePosition(double cx){
		return scale * (cx - position.x) + center.x; 
	}
	
	/**
	 * Translates cy from world coordinates to camera coordinates
	 * @param cx
	 * @return
	 */
	public double yTranslatePosition(double cy){
		return scale * (cy - position.y) + center.y; 
	}
	
	public double xScale(){
		return scale;
	}
	
	public double yScale(){
		return scale;
	}
	
	public double scale(){
		return scale;
	}
	
	public Vector center(){
		return new Vector(center);
	}
	
	/**
	 * Convert a pair of world coordinates to screen coordinates
	 * @param v, the world coordinate pair
	 * @return The screen coordinates of v
	 */
	public Vector worldToScreen(Vector v){
		return v.sub(position).scale(scale).add(center);
	}
	
	/**
	 * Convert a pair of screen coordinates to world coordinates
	 * @param v, the screen coordinate pair
	 * @return The world coordinates of v
	 */
	public Vector screenToWorld(Vector v){
		return v.sub(center).scale(1/scale).add(position);
	}
	
	public void update(double elapsedSeconds){
		spring.setVelocity(velocity);
		if(follow != null){
			velocity = spring.getVelocity(follow.getPosition(), position);
		}else{
			Vector.add(velocity, velocity.scale(-dragPerSecond*elapsedSeconds));
		}
		position = position.add(velocity.scale(elapsedSeconds));
	}
}