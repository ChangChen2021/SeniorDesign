package main.util.shape;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Represents a geometrical shape.
 * 
 * @author Manuela Kastner
 *
 */
public interface Shape {

	/**
	 * Get the type of shape
	 * 
	 * @return type of shape
	 */
	public ShapeEnum getType();

	/**
	 * Get all shape property enums (radius etc.)
	 * 
	 * @return all shape properties as enmums
	 */
	public ShapeProperty[] getProperties();

	/**
	 * Get all values of the shape poperties
	 * 
	 * @return all values of the shape properties
	 */
	public Object[] getValues();

	/**
	 * Checks if input is out of shape range or not
	 * 
	 * @param input
	 *            input at which the shape shall be evaluated at
	 * @return true if input is out of range, false otherwise
	 */
	public boolean outOfRange(Vector2D input);
}
