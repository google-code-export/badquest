package graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import gameObjects.Actor;
import gameObjects.DrawableObject;
import gameObjects.ObjectManager;
import util.SpringDampHelper;
import util.Vector;
import client.GameClient;

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
	
	private double shakeTime;
	private double elapsedShakeTime;
	private double timeToNextShake;
	private double shakesPerSecond = 20;
	private double shakeMagnitude;
	private boolean shaking = false;
	
	private DrawableObject follow = null;
	
	private final double DEFAULT_SCALE = 1;
	
	/**
	 * Returns a camera centered on position pos, with default scaling.
	 * @param pos
	 */
	public Camera(Vector pos){
		position = new Vector(pos);
		scale = DEFAULT_SCALE;
	}
	
	/**
	 * Returns a camera centered on position pos, with specified scaling.
	 * @param pos
	 * @param scale
	 */
	public Camera(Vector pos, double scale){
		position = new Vector(pos);
		this.scale = scale;
	}
	
	public Vector getPosition(){
		return position;
	}
	
	public void setPosition(Vector pos){
		position = new Vector(pos);
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
	
	public double getScale(){
		return scale;
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
		return getScale();
	}
	
	public double yScale(){
		return getScale();
	}
	
	public double scale(){
		return scale;
	}
	
	public Vector center(){
		return new Vector(center);
	}
	
	/**
	 * Returns a double array of length 4 containg the bounding box of the camera in world coordinates.
	 * @return The top left point of the box (x,y) is returned in {0,1}, the bottom right in {2,3}.
	 */
	public double[] getBoundingBox(){
		Vector top = screenToWorld(new Vector(0,0));
		Vector bot = screenToWorld(new Vector(GameClient.frameWidth, GameClient.frameHeight));
		return new double[]{top.x,top.y,bot.x,bot.y};
	}
	
	//TODO: Merge shake effects when possible, utilizing a queue.
	/**
	 * Shake the camera! If the camera is currently shaking, the active shake effect will be overridden only
	 * if the new magnitude exceeds the current one.
	 * 
	 * For reference, a magnitude of 10 produces a fairly violent effect, 
	 * whereas a magnitude of 2 is more subtle without being irrelevant.
	 * 
	 * @param mag The magnitude of the shake offset
	 * @param time	The duration of the shake effect
	 * @return
	 */
	public boolean shake(double mag, double time){
		if(!shaking || mag > shakeMagnitude){
			shaking = true;
			shakeMagnitude = mag;
			shakeTime = time;
			elapsedShakeTime = 0;
			timeToNextShake = 0;
			return true;
		}
		return false;
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
			velocity = velocity.add(velocity.scale(-dragPerSecond*elapsedSeconds));
		}
		
		Vector shakeOffset = new Vector(0,0);
		if(shaking){
			elapsedShakeTime += elapsedSeconds;
			timeToNextShake -= elapsedSeconds;
			
			if(timeToNextShake <= 0){ //Shake the camera!
				shakeOffset = new Vector(Math.random() * (2*Math.PI)).scale(shakeMagnitude);
				timeToNextShake = 1/shakesPerSecond;
			}
			
			if(elapsedShakeTime > shakeTime){
				shaking = false;
				shake(0,0);
				shaking = false;
			}
		}
		
		position = position.add(velocity.scale(elapsedSeconds)).add(shakeOffset);
	}
	
	public void draw(Graphics2D g, double elapsedSeconds){
		g.setColor(Color.WHITE);
		g.setStroke(new BasicStroke(2.f));
		g.drawLine(GameClient.frameWidth/2, GameClient.frameHeight/2-20, GameClient.frameWidth/2, GameClient.frameHeight/2+20);
		g.drawLine(GameClient.frameWidth/2-20, GameClient.frameHeight/2, GameClient.frameWidth/2+20, GameClient.frameHeight/2);
		
		g.drawRect(GameClient.frameWidth/2-150, 30, 300, 40);
		FontMetrics f = g.getFontMetrics();
		g.drawString(String.format("%.2f", 1/elapsedSeconds), GameClient.frameWidth-(int)(f.getStringBounds("Following", g).getWidth()+10), 45);
		g.drawString("Following", GameClient.frameWidth/2-(int)(f.getStringBounds("Following", g).getWidth()/2), 45);
		if(getFocus() != null){
			if(getFocus() instanceof Actor)
				g.drawString(((Actor)(getFocus())).getName(), GameClient.frameWidth/2-(int)(f.getStringBounds(((Actor)(getFocus())).getName(), g).getWidth()/2), 62);
			g.drawString(String.format("(%.4f, %.4f)", getFocus().getPosition().x, getFocus().getPosition().y), GameClient.frameWidth/2+5, GameClient.frameHeight/2-7);
			g.drawString(String.format("(%.4f, %.4f)", getFocus().getVelocity().x, getFocus().getVelocity().y), GameClient.frameWidth/2+5, GameClient.frameHeight/2+15);
			g.drawString(String.format("%.4f", scale()), GameClient.frameWidth/2-5, GameClient.frameHeight-30);
			g.drawString(String.format("%d", ObjectManager.objectCount()), GameClient.frameWidth/2-5, GameClient.frameHeight-45);
		}
	}
}
