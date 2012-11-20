package gameObjects.interfaces;

public interface Damageable {
	public int getMaxHealth();
	public int getCurrentHealth();
	public Faction getFaction();
	public boolean isDamageable(Faction f);
	public void applyDamage(int amount);
	
	public static enum Faction{
		FRIENDLY,
		NEUTRAL,
		HOSTILE;
	}
}
