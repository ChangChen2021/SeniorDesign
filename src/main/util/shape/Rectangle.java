package main.util.shape;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Represents a rectangle
 * 
 * @author Manuela Kastner
 *
 */
public class Rectangle implements Shape {
	private double size_x;
	private double size_y;
	private Vector2D center;

	/**
	 * Constructor
	 * 
	 * @param size_x
	 *            width of the rectangle
	 * @param size_y
	 *            height of the rectangle
	 * @param center
	 *            center of the rectangle
	 */
	public Rectangle(double size_x, double size_y, Vector2D center) {
		this.size_x = Math.abs(size_x);
		this.size_y = Math.abs(size_y);
		this.center = center;
	}

	/**
	 * Constructor
	 * 
	 * @param size_x
	 *            width of the rectangle
	 * @param size_y
	 *            height of the rectangle
	 */
	public Rectangle(double size_x, double size_y) {
		this.size_x = Math.abs(size_x);
		this.size_y = Math.abs(size_y);
		this.center = new Vector2D(0, 0);
	}

	@Override
	public ShapeEnum getType() {
		return ShapeEnum.RECTANGLE;
	}

	@Override
	public ShapeProperty[] getProperties() {
		return new ShapeProperty[] { ShapeProperty.SIZE_X, ShapeProperty.SIZE_Y, ShapeProperty.CENTER_X,
				ShapeProperty.CENTER_Y };
	}

	@Override
	public Object[] getValues() {
		return new Object[] { size_x, size_y, center.getX(), center.getY() };
	}

	@Override
	public boolean outOfRange(Vector2D input) {
		double center_x = center.getX();
		double center_y = center.getY();
		double input_x = input.getX();
		double input_y = input.getY();
		double halfSize_x = size_x / 2.0;
		double halfSize_y = size_y / 2.0;

		if (input_x > center_x + halfSize_x || input_x < center_x - halfSize_x || input_y > center_y + halfSize_y
				|| input_y < center_y - halfSize_y) {
			return true;
		}
		return false;
	}
}
