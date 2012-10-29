package gameStates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import client.GameClient;

public class EditorOverlayState extends State {

	@Override
	protected void draw(Graphics2D g, double elapsedSeconds) {
		AffineTransform prev = g.getTransform();
		
		g.setColor(Color.darkGray);
		g.fillRect(4*GameClient.frameWidth/5, 0, GameClient.frameWidth - 4*GameClient.frameWidth/5, GameClient.frameHeight);
		
		g.setTransform(prev);
	}

	@Override
	protected void update(double elapsedSeconds) {
		// TODO Auto-generated method stub

	}

}
