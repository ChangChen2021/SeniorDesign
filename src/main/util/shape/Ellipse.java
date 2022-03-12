package main.util.shape;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Represents an ellipse
 * 
 * @author Manuela Kastner
 *
 */
public class Ellipse implements Shape {

	double radius_x;
	double radius_y;
	Vector2D center;

	/**
	 * Constructor
	 * 
	 * @param radius_x
	 *            first radius of the ellipse
	 * @param radius_y
	 *            second radius of the ellipse
	 * @param center
	 *            center of the ellipse
	 */
	public Ellipse(double radius_x, double radius_y, Vector2D center) {
		this.radius_x = Math.abs(radius_x);
		this.radius_y = Math.abs(radius_y);
		this.center = center;
	}

	/**
	 * Constructor
	 * 
	 * @param radius_x
	 *            first radius of the ellipse
	 * @param radius_y
	 *            second radius of the ellipse
	 */
	public Ellipse(double radius_x, double radius_y) {
		this.radius_x = Math.abs(radius_x);
		this.radius_y = Math.abs(radius_y);
		this.center = new Vector2D(0, 0);
	}

	@Override
	public ShapeEnum getType() {
		return ShapeEnum.ELLIPSE;
	}

	@Override
	public ShapeProperty[] getProperties() {
		return new ShapeProperty[] { ShapeProperty.RADIUS_X, ShapeProperty.RADIUS_Y, ShapeProperty.CENTER_X,
				ShapeProperty.CENTER_Y };
	}

	@Override
	public Object[] getValues() {
		return new Object[] { radius_x, radius_y, center.getX(), center.getY() };
	}

	@Override
	public boolean outOfRange(Vector2D input) {
		double center_x = center.getX();
		double center_y = center.getY();
		double input_x = input.getX();
		double input_y = input.getY();

		if (Math.pow(input_x - center_x, 2) / Math.pow(radius_x, 2)
				+ Math.pow(input_y - center_y, 2) / Math.pow(radius_y, 2) > 1) {
			return true;
		}
		return false;
	}

}