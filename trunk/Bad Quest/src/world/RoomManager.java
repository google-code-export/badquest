package world;

import gameObjects.DrawableObject;
import gameObjects.ObjectManager;

import java.util.ArrayDeque;
import java.util.TreeMap;

public class RoomManager {
	private static TreeMap<Integer, Room> rmap = new TreeMap<Integer, Room>();
	private static int RIDcounter = 0;
	private static int curRID = -1;
	
	public static Room setRoom(int ID){
		if(getRoomFromID(ID) == null)
			throw new AssertionError();
		curRID = ID;
		return getRoomFromID(curRID);
	}
	
	public static Room changeRoom(ArrayDeque<Integer> transfer, int ID){
		if(getRoomFromID(ID) == null){
			System.err.println("Error referencing room " + ID + ", no room found with this ID");
			return null;
		}
		
		ArrayDeque<Integer> add = new ArrayDeque<Integer>();
		if(curRID > -1){
			TreeMap<Integer, DrawableObject> entityMap = getRoomFromID(curRID).getEntityMap();
			synchronized(entityMap){
				for(Integer OID:transfer)
					if(entityMap.containsKey(OID)){
						add.add(OID);
						entityMap.remove(OID);
					}
			}
		}
		
		Room next = getRoomFromID(ID);
		for(Integer OID:add)
			next.addEntity(ObjectManager.getObjectByID(OID));
		
		System.out.println("Transferring from " + curRID + " to " + ID);
		
		curRID = ID;
		return next;
	}
	
	public static Room getRoomFromID(int ID){
		Room ret = null;
		synchronized(rmap){
			ret = rmap.get(ID);
		}
		return ret;
	}
	
	public static int register(Room room, int ID){
		synchronized(rmap){
			if(rmap.containsKey(ID)){
				System.err.println("Error registering room " + ID + ", RID currently in use.");
				return register(room);
			}
			rmap.put(ID, room);
		}
		return ID;
	}
	
	public static int register(Room room){
		synchronized(rmap){
			while(rmap.containsKey(RIDcounter))
				RIDcounter++;
			rmap.put(RIDcounter, room);
		}
		return RIDcounter++;
	}
}
