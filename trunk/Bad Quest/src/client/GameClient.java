package client;

import gameStates.DebugState;
import gameStates.GameStateManager;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class GameClient {
	final boolean spinLock = false;
	
	private static final int bufferCreateAttempts = 100;
	
	final long updateCapNanos = (long)(1e9/30);
	final float nanoSecondsPerSecond = 1e9f;
	
	public final static int frameWidth = 1200;
	public final static int frameHeight = 700;
	
	JFrame gameFrame;
	GraphicsConfiguration graphicsConfiguration;
	
	boolean done = false;
	
	public GameClient(){
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		
		graphicsConfiguration = ge.getDefaultScreenDevice().getDefaultConfiguration();
		
		gameFrame = new JFrame("Bad Quest", graphicsConfiguration);
		gameFrame.setSize(frameWidth, frameHeight);
//		gameFrame.setUndecorated(true);
//		gameFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
		gameFrame.setResizable(false);
		
		// The program should end when the window is closed
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		gameFrame.addKeyListener(new GameStateManager.GSM_KeyListener());
		gameFrame.addMouseListener(new GameStateManager.GSM_MouseListener());
		gameFrame.addMouseMotionListener(new GameStateManager.GSM_MouseMotionListener());
		
		GameStateManager.pushState(new DebugState());		
	}
	
	public void stopDraw()
	{
		done = true;
	}
	
	public void startDrawAndUpdate()
	{		
		 // Show our window
		gameFrame.setVisible(true);

		int lastFail = -1;
		
		 // Create a general double-buffering strategy
		for(int i = 1; i <= bufferCreateAttempts; i++){
			try
			{
				Thread.sleep(2);
				gameFrame.createBufferStrategy(2);
			} 
			catch (IllegalStateException e)
			{
				lastFail = i;
			}
			catch (InterruptedException e) 
			{
				throw new RuntimeException("Graphics Creation Thead interrupted.");
			}
		}
		
		if(lastFail == bufferCreateAttempts)
			throw new RuntimeException("Graphics failed to initialize after "
					+ bufferCreateAttempts + " attempts.");
		
		BufferStrategy strategy = gameFrame.getBufferStrategy();
		
		long currTimeNano, prevUpdateTimeNano = System.nanoTime();

		 // Main loop
		 while (!done) {
		     
			 if(!spinLock)
			 {
				 currTimeNano = System.nanoTime();
				 
				 long remMillis = (updateCapNanos - (currTimeNano - prevUpdateTimeNano))/1000000;
				  
				 if(remMillis > 10)
					 try{Thread.sleep(remMillis-5);} catch (Exception e) {}
			 }
			 
			 do
			 {
				 currTimeNano = System.nanoTime();
			 }
			 while(currTimeNano - prevUpdateTimeNano < updateCapNanos);
			 
			 float elapsedTime = (currTimeNano - prevUpdateTimeNano)/nanoSecondsPerSecond;
			 
			 prevUpdateTimeNano = currTimeNano;
			 
			 //Update here
			 GameStateManager.update(elapsedTime);
			 
		     // Render single frame
		     do {
		         // The following loop ensures that the contents of the drawing buffer
		         // are consistent in case the underlying surface was recreated
		         do {
		             // Get a new graphics context every time through the loop
		             // to make sure the strategy is validated
		             Graphics2D graphics = (Graphics2D)strategy.getDrawGraphics();
		     
		             // Render to graphics
		             graphics.setBackground(Color.BLACK);
		             graphics.clearRect(0, 0, gameFrame.getWidth(), gameFrame.getHeight());
		             
		             
		             GameStateManager.draw(graphics, elapsedTime);
		             
		             
		             // Dispose the graphics
		             graphics.dispose();

		             // Repeat the rendering if the drawing buffer contents 
		             // were restored
		         } while (strategy.contentsRestored());

		         // Display the buffer
		         strategy.show();

		         // Repeat the rendering if the drawing buffer was lost
		     } while (strategy.contentsLost());
		 }

		 // Dispose the window
		 gameFrame.setVisible(false);
		 gameFrame.dispose();
	}
	
	public static void main(String[] args){
		new GameClient().startDrawAndUpdate();
	}
}
