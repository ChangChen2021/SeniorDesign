package main.util.shape;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Represents a circle
 * 
 * @author Manuela Kastner
 *
 */
public class Circle extends Ellipse {

	/**
	 * Constructor
	 * 
	 * @param radius
	 *            radius of the circle
	 * @param center
	 *            center of the circle
	 */
	public Circle(double radius, Vector2D center) {
		super(radius, radius, center);
	}

	/**
	 * Constructor
	 * 
	 * @param radius
	 *            radius of the circle
	 */
	public Circle(double radius) {
		super(radius, radius);
	}

	@Override
	public ShapeEnum getType() {
		return ShapeEnum.CIRCLE;
	}
}