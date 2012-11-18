package client;

import gameObjects.Player;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.HashMap;

public class KeyBindings implements Serializable{
	private static final long serialVersionUID = 1L;
	private static String fileName = "keybindings.bad";
	private static KeyBindings currentKeyBindings;
	
	private HashMap<Integer, Player.PlayerInput> keyMap;
	private HashMap<Integer, Player.PlayerInput> mouseMap;
	static{
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try{
			fis = new FileInputStream(new File(fileName));
			in = new ObjectInputStream(fis);
			currentKeyBindings = (KeyBindings)in.readObject();
		}catch(FileNotFoundException e){
			currentKeyBindings = new KeyBindings();
			updateFile();
		}catch(IOException e){
			currentKeyBindings = new KeyBindings();
			updateFile();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	
	private KeyBindings(){
		keyMap = new HashMap<Integer, Player.PlayerInput>();
		
		keyMap.put(KeyEvent.VK_W, Player.PlayerInput.MOVE_UP);
		keyMap.put(KeyEvent.VK_D, Player.PlayerInput.MOVE_RIGHT);
		keyMap.put(KeyEvent.VK_S, Player.PlayerInput.MOVE_DOWN);
		keyMap.put(KeyEvent.VK_A, Player.PlayerInput.MOVE_LEFT);
		keyMap.put(KeyEvent.VK_SPACE, Player.PlayerInput.JUMP);
		
		mouseMap = new HashMap<Integer, Player.PlayerInput>();
		
		mouseMap.put(MouseEvent.BUTTON1, Player.PlayerInput.USE1);
		mouseMap.put(MouseEvent.BUTTON2, Player.PlayerInput.USE2);
	}

	public static ArrayDeque<Player.PlayerInput> getInputList(BitSet keys, BitSet clicks){
		ArrayDeque<Player.PlayerInput> ret = new ArrayDeque<Player.PlayerInput>();
		
		HashMap<Integer, Player.PlayerInput> map = currentKeyBindings.keyMap;
		for(Integer key:map.keySet())
			if(keys.get(key))
				ret.add(map.get(key));
		
		map = currentKeyBindings.mouseMap;
		for(Integer key:map.keySet())
			if(clicks.get(key))
				ret.add(map.get(key));
		
		return ret;
	}
	
	public static void updateFile(){
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try{
			fos  = new FileOutputStream(new File(fileName));
			out = new ObjectOutputStream(fos);
			out.writeObject(currentKeyBindings);
		}catch(FileNotFoundException e){
			currentKeyBindings = new KeyBindings();		
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}