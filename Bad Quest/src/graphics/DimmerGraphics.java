package graphics;

import java.awt.Color;
import java.awt.Graphics2D;

public class DimmerGraphics extends CustomGraphics {
	private double level;
	
	/**
	 * Dims the whatever color is passed to this graphics context by the lightLevel
	 * @param g
	 * 			Backing graphics context.
	 * @param lightLevel
	 * 			Amount of light being cast on this object, clamped between 0 and 1.
	 */
	public DimmerGraphics(Graphics2D g, double lightLevel){
		super(g);
		level = Math.min(1,Math.max(0, lightLevel));
	}
	
	/**
	 * Set the light level
	 * @param level
	 */
	public void setLevel(double level){
		this.level = level;
	}
	
	/**
	 * Sets the current color to a color interpolated between the specified color and black,
	 * the weight of the specified color being the light level and the weight of black 1 minus this value.
	 */
	public void setColor(Color arg0) {
		float[] a = arg0.getRGBComponents(null);
		float[] b = Color.black.getRGBComponents(null);
		float[] c = new float[4];
		for(int i = 0; i < 4; i++)
			c[i] = (float)(a[i]*(level)+b[i]*(1-level));
	    g.setColor(new Color(c[0], c[1], c[2], a[3]));
	}
}
