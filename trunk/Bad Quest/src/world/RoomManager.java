package world;

import gameObjects.DrawableObject;
import gameObjects.ObjectManager;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

public class RoomManager {
	private static TreeMap<Integer, Room> rmap = new TreeMap<Integer, Room>();
	private static int RIDcounter = 1;
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
		}else if(ID == curRID){
			return rmap.get(ID);
		}
		
		ArrayDeque<Integer> add = new ArrayDeque<Integer>();
		if(curRID > -1){
			TreeMap<Integer, DrawableObject> entityMap = getRoomFromID(curRID).getEntityMap();
			synchronized(entityMap){
				for(Integer OID:transfer)
					if(entityMap.containsKey(OID))
						add.add(OID);
			}
		}
		
		Room next = getRoomFromID(ID);
		for(Integer OID:add)
			ObjectManager.getObjectByID(OID).changeCurrentRoom(next);
		
		System.out.println("Transferring from " + rmap.get(curRID) + " to " + rmap.get(ID));
		
		curRID = ID;
		return next;
	}
	
	public static ArrayList<Room> getRoomList(){
		ArrayList<Room> ret = new ArrayList<Room>();
		synchronized(rmap){
			for(Integer e:rmap.keySet())
				ret.add(rmap.get(e));
		}
		Collections.sort(ret);
		return ret;
	}
	
	public static Room getRoomFromID(int ID){
		synchronized(rmap){
			return rmap.get(ID);
		}
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
