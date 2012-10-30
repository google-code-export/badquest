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
	
	public static int register(DrawableObject obj, int ID){
		if(map.containsKey(ID)){
			System.err.println("Error registering " + obj + ", OID currently in use.");
			return register(obj);
		}
		map.put(ID, obj);
		return ID;
	}
	
	public static int register(DrawableObject obj){
		while(map.containsKey(OIDcounter))
			OIDcounter++;
		map.put(OIDcounter, obj);
		return OIDcounter++;
	}
	
	public static DrawableObject getObjectByID(int q){
		if(!map.containsKey(q))
			System.err.println("Error retrieving object " + q + ", ID is not registered to any object");
		return map.get(q);
	}
	
	/**
	 * Remove an object from the object map. This method should only be called once the object has been removed
	 * from all relevant lists and mappings.
	 * @param q The OID of the object.
	 * @return Returns true if the object was successfully remove, false otherwise.
	 */
	public static boolean removeObjectByID(int q){
		return map.remove(q) != null;
	}
	
	//Object enum?
/*	public static ObjectType getObjectType(int q){
		if(!map.containsKey(q)){
			System.err.println("Error referencing object " + q + ", ID is not registered to any object");
			return null;
		}
		instanceof the rest
	}
*/
}
