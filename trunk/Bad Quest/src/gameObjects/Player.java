package gameObjects;

import graphics.Camera;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.HashMap;

import util.Vector;
import client.KeyBindings;

public class Player extends Actor {	
	private EquipmentModule rightHand,head;
	
	private ArrayDeque<PlayerInput> input;
	private double speed = 225;
	private boolean lock = false;
	
	public Player(String name, int r, Vector position){
		super(name,r,position);
		color = new Color(125,190,209).darker();
		
		rightHand = new EquipmentModule(this, radius*1.5, Math.PI/2.5, Math.PI/6);
		rightHand.loadEquipment(new DebugSword());
		
		head = new EquipmentModule(this);
		head.loadEquipment(new DebugHelmet());
		
		input = new ArrayDeque<PlayerInput>();
	}
	
	public void setSpeed(double s){
		speed = s;
	}
	
	public void receiveInput(BitSet keys, BitSet clicks){
		input = KeyBindings.getInputList(keys, clicks);
	}
	
	@Override
	public void update(double elapsedSeconds){		
		//Add a block for player input
		Vector inputVel = new Vector(0,0);
		if(!lock){
			for(PlayerInput pid:input){
				switch(pid){
				case MOVE_UP:
					Vector.add(inputVel, new Vector(0,-speed));
					break;
				case MOVE_DOWN:
					Vector.add(inputVel, new Vector(0,speed));
					break;
				case MOVE_RIGHT:
					Vector.add(inputVel, new Vector(speed,0));
					break;
				case MOVE_LEFT:
					Vector.add(inputVel, new Vector(-speed,0));
					break;
				case JUMP:
					break;
				case USE1:
					rightHand.use();
					break;
				case USE2:
					break;
				case USE3:
					break;
				case USE4:
					break;
				default:
					System.err.println("Unrecognized player input: " + pid);
				}
			}
		}
		
		setInternalVelocity(inputVel);
		
		//Perform the Actor update
		super.update(elapsedSeconds);
		//Update equipment modules
		rightHand.update(elapsedSeconds);
		head.update(elapsedSeconds);
	}
	
	@Override
	public void drawBody(Graphics2D g, double elapsedSeconds, Camera cam) {
		AffineTransform prev = g.getTransform();
		Stroke pStroke = g.getStroke();
		
		g.translate(cam.xTranslatePosition(position.x), cam.yTranslatePosition(position.y));
		g.scale(cam.scale(), cam.scale());
		g.rotate(angle);
		
		g.setColor(Color.black);
		g.drawOval(-(int)radius, -(int)radius, 2*(int)radius, 2*(int)radius);

		super.drawBody(g, elapsedSeconds, cam);
		
		g.setStroke(pStroke);
		g.setTransform(prev);
		
		rightHand.drawBody(g, elapsedSeconds, cam);
		head.drawBody(g, elapsedSeconds, cam);
	}
	
	private static int enumCount = 0;
	public static enum PlayerInput{
		MOVE_UP,
		MOVE_RIGHT,
		MOVE_DOWN,
		MOVE_LEFT,
		JUMP,
		USE1,
		USE2,
		USE3,
		USE4;
		
		public final int val;
		private PlayerInput(){
			val = enumCount++;
		}
		
		private static HashMap<Integer, PlayerInput> map = new HashMap<Integer, PlayerInput>();
		static{
			for(PlayerInput pid:PlayerInput.values())
				map.put(pid.val,pid);
		}
		
		public static PlayerInput getInputFromID(int pid){
			return map.get(pid);
		}
	}
}
