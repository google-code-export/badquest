package gameObjects;

import java.util.TreeMap;

/**
 * The object manager is to keep track of every game object generated for this session.
 * Each object will be assigned a unique ID, by which it may be referred to in queries.
 * @author Mike
 *
 */
public class ObjectManager {
	private static int OIDcounter = 1;
	private static TreeMap<Integer, DrawableObject> map = new TreeMap<Integer, DrawableObject>();
	
	public static int objectCount(){
		return map.size();
	}
	
	public static int register(DrawableObject obj, int ID){
		synchronized(map){
			if(map.containsKey(ID)){
				System.err.println("Error registering " + obj + ", OID currently in use.");
				return register(obj);
			}
			map.put(ID, obj);
		}
		return ID;
	}
	
	public static int register(DrawableObject obj){
		synchronized(map){
			while(map.containsKey(OIDcounter))
				OIDcounter++;
			map.put(OIDcounter, obj);
		}
		return OIDcounter++;
	}
	
	public static DrawableObject getObjectByID(int ID){
		synchronized(map){
			if(!map.containsKey(ID))
				System.err.println("Error retrieving object " + ID + ", ID is not registered to any object");
			return map.get(ID);
		}
	}
	
	/**
	 * Remove an object from the object map. This method should only be called once the object has been removed
	 * from all relevant lists and mappings.
	 * @param q The OID of the object.
	 * @return Returns true if the object was successfully remove, false otherwise.
	 */
	public static boolean removeObjectByID(int ID){
		synchronized(map){
			System.out.println("Removing object " + ID);
			return map.remove(ID) != null;
		}
	}
}
