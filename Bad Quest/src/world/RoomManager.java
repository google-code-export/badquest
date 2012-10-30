package world;

import gameObjects.DrawableObject;
import gameObjects.Player;

import java.util.HashMap;

public class RoomManager {
	private static HashMap<Integer, Room> rmap = new HashMap<Integer, Room>();
	private static int RIDcounter = 0;
	private static int curRID = -1;
	
	//When changing rooms, pass a set of OIDs to transfer. Each object should include its children's OI
	
//	public static Room loadRoom(int id){
//		if(curRID == -1)
//			return null;
//		
//		Player transfer = null;
//		for(DrawableObject d:rmap.get(curRID).getEntityList())
//			if(d instanceof Player)
//				transfer = (Player)d;
//		
//		
//	}
	
	public static Room getRoomFromID(int id){
		return rmap.get(id);
	}
	
	public static void addRoom(Room room){
		room.setRID(RIDcounter++);
		rmap.put(room.getRID(), room);
	}
}
