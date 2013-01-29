package gameStates;

import graphics.Camera;
import graphics.DimmerGraphics;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;

import util.Vector;
import world.Room;
import world.RoomManager;
import world.tile.Tile;
import client.GameClient;

public class TitleState extends State {
	
	BitSet keys; //Set of active keyboard presses
	BitSet clicks; //Set of active mouse clicks
	
	double scale = 2.868;
	double layerSpacing = .75;
	double drawLayers = 4;
	
	ArrayList<Room> background = new ArrayList<Room>();
	Camera cam = new Camera(new Vector(0,0));
	double camVel = 200;
	
	AudioClip title = Applet.newAudioClip(this.getClass().getClassLoader().getResource("resources/audio/dankasheck.wav"));
	
	public TitleState(){
		keys = new BitSet();
		clicks = new BitSet();
		
		title.loop();
		
		for(int i = 0; i < 30; i++){
			while(true){
				Room add = new Room((int)(Math.random()*50+10), (int)(Math.random()*50+10), (int)(Math.random()*4));
				add.setPosition(new Vector(Math.random()*3000-1500, Math.random()*3000-1500));
				
				System.out.println(i + " " + add.getPosition() + " " + add.R + " " + add.C);
				
				boolean flag = false;
				Rectangle2D.Double bounds = new Rectangle2D.Double(add.getPosition().x, add.getPosition().y, add.C*Tile.SIZE, add.R*Tile.SIZE);
				for(int j = 0; j < i; j++){
					Room a = background.get(j);
					if(a.getLayer() != add.getLayer())
						continue;
					Rectangle2D.Double check = new Rectangle2D.Double(a.getPosition().x, a.getPosition().y, a.C*Tile.SIZE, a.R*Tile.SIZE);
					flag |= check.intersects(bounds);
				}
				
				if(flag)
					RoomManager.removeRoomByID(add.getRID());
				else{
					background.add(add);
					break;
				}
			}
		}
	}
	
	@Override
	protected void draw(Graphics2D g, double elapsedSeconds) {
		AffineTransform prev = g.getTransform();
		Stroke pStroke = g.getStroke();
		
		ArrayList<Room> roomList = RoomManager.getRoomList();
		Collections.sort(roomList);
		
		//Draw non-focus rooms
		DimmerGraphics backgroundGraphics = new DimmerGraphics(g,1);
		
		//Draw non-focus rooms
		for(Room r:roomList){						
			backgroundGraphics.setLevel(1/(1+r.getLayer()*r.getLayer()));
			Camera backCam = new Camera(cam.getPosition(), 1/(1 + r.getLayer()*layerSpacing)*cam.scale());
			r.drawAll(backgroundGraphics, elapsedSeconds, backCam);
		}
		
		backgroundGraphics.dispose();
		
		g.setColor(Color.white);
		Font pFont = g.getFont();
		g.setFont(new Font("Arial", Font.PLAIN, 72));
		g.drawString("Bad Quest", GameClient.frameWidth/2, GameClient.frameHeight/2);
		
		g.setFont(new Font("Arial", Font.PLAIN, 36));
		g.drawString("Press any key to continue", GameClient.frameWidth/2+5, GameClient.frameHeight/2+72);
		
		g.setFont(pFont);
		g.setStroke(pStroke);
		g.setTransform(prev);
	}

	@Override
	protected void update(double elapsedSeconds) {
		cam.setPosition(cam.getPosition().add(new Vector(camVel*elapsedSeconds,0)));
		if(!keys.isEmpty()){
			for(Room x:background)
				RoomManager.removeRoomByID(x.getRID());
			title.stop();
			title = null;
			GameStateManager.pushState(new DebugState());
		}
		
		ArrayList<Integer> remove = new ArrayList<Integer>();
		for(int i = 0; i < background.size(); i++){
			Room x = background.get(i);
			Camera backCam = new Camera(cam.getPosition(), 1/(1 + x.getLayer()*layerSpacing)*cam.scale());
			Vector tl = backCam.screenToWorld(new Vector(-100,-100));
			Vector br = backCam.screenToWorld(new Vector(GameClient.frameWidth+500,GameClient.frameHeight+500));
			Rectangle2D.Double camera = new Rectangle2D.Double(tl.x,tl.y,br.x,br.y);
			Rectangle2D.Double box = new Rectangle2D.Double(x.getPosition().x, x.getPosition().y, x.C*Tile.SIZE, x.R*Tile.SIZE);
			if(box.getMaxX() < camera.getMinX())
				remove.add(i);
		}
		
		int curry = 0;
		for(Integer x:remove){
			RoomManager.removeRoomByID(background.get(x-curry).getRID());
			background.remove(x-curry++);
		}
		
		if(background.size() <= 30)
			for(int i = 0; i < 10; i++){
				while(true){
					Room add = new Room((int)(Math.random()*50+10), (int)(Math.random()*50+10), (int)(Math.random()*4));
					add.setPosition(new Vector(Math.random()*500+2*GameClient.frameWidth, Math.random()*3000-1500).add(cam.getPosition()));
					
					boolean flag = false;
					Rectangle2D.Double bounds = new Rectangle2D.Double(add.getPosition().x, add.getPosition().y, add.C*Tile.SIZE, add.R*Tile.SIZE);
					for(int j = 0; j < background.size(); j++){
						Room a = background.get(j);
						if(a.getLayer() != add.getLayer())
							continue;
						Rectangle2D.Double check = new Rectangle2D.Double(a.getPosition().x, a.getPosition().y, a.C*Tile.SIZE, a.R*Tile.SIZE);
						flag |= check.intersects(bounds);
					}
					
					if(flag)
						RoomManager.removeRoomByID(add.getRID());
					else{
						background.add(add);
						break;
					}
				}
			}
	}
	
	@Override
	protected void keyPressed(KeyEvent e) {
		keys.set(e.getKeyCode());
		super.keyPressed(e);
	}
	
	@Override
	protected void keyReleased(KeyEvent e) {
		keys.clear(e.getKeyCode());
		super.keyReleased(e);
	}
}
