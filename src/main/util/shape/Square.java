package main.util.shape;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Represents a square
 * 
 * @author Manuela Kastner
 *
 */
public class Square extends Rectangle {

	/**
	 * Constructor
	 * 
	 * @param size
	 *            length of the square
	 * @param center
	 *            center of the square
	 */
	public Square(double size, Vector2D center) {
		super(size, size, center);
	}

	/**
	 * Constructor
	 * 
	 * @param size
	 *            length of the square
	 */
	public Square(double size) {
		super(size, size);
	}

	@Override
	public ShapeEnum getType() {
		return ShapeEnum.SQUARE;
	}

}
