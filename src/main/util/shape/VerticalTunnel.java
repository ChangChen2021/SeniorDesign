package main.util.shape;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Represents a vertical tunnel (to shape a sailing course)
 * 
 * @author Manuela Kastner
 *
 */
public class VerticalTunnel implements Shape {

	private double width;
	private double center_x;

	/**
	 * Constructor
	 * 
	 * @param width
	 *            width of the tunnel
	 * @param center_x
	 *            center of the tunnel
	 */
	public VerticalTunnel(double width, double center_x) {
		this.width = Math.abs(width);
		this.center_x = center_x;
	}

	/**
	 * Constructor
	 * 
	 * @param width
	 *            width of the tunnel
	 */
	public VerticalTunnel(double width) {
		this.width = Math.abs(width);
		center_x = 0.0;
	}

	@Override
	public ShapeEnum getType() {
		return ShapeEnum.VERTICAL_TUNNEL;
	}

	@Override
	public ShapeProperty[] getProperties() {

		return new ShapeProperty[] { ShapeProperty.WIDTH, ShapeProperty.CENTER_X };
	}

	@Override
	public Object[] getValues() {
		return new Object[] { width, center_x };
	}

	@Override
	public boolean outOfRange(Vector2D input) {
		double input_x = input.getX();
		double halfWidth = width / 2.0;

		if (input_x > center_x + halfWidth || input_x < center_x - halfWidth) {
			return true;
		}
		return false;
	}

}
