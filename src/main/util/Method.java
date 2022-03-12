package main.util;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import main.util.shape.Shape;

/**
 * Contains the methods for all necessary calculations (except for translation
 * and superposition) like fields, operations, proportionality etc.
 * 
 * @author Manuela Kastner
 *
 */
public class Method {

	/**
	 * a zero vector
	 */
	private static Vector2D ZERO = new Vector2D(0.0, 0.0);

	// fields----------------------------------------------------------------------------------------------------------

	/**
	 * Calculates the function value for a zero field.
	 * 
	 * @return always zero vector
	 */
	public static Vector2D zero() {
		return new Vector2D(0.0, 0.0);
	}
	/**
	 * Calculates the function value for a homogenous field.
	 * 
	 * @param input
	 *            position at which the function value shall be calculated
	 * @param value
	 *            the value of the field
	 * @return calculated function value
	 */
	public static Vector2D homogenous(Vector2D input, double value) {
		return new Vector2D(0.0, value);
	}

	/**
	 * Calculates the function value for a radial field.
	 * 
	 * @param input
	 *            position at which the function value shall be calculated
	 * @param value
	 *            the value of the field
	 * @param radius
	 *            radius of the obstacle (buoy, isle,..)
	 * @param proportionality
	 *            proportionality of the value to the distance to the obstacle
	 * @return calculated function value
	 */
	public static Vector2D radial(Vector2D input, double value, double radius, Proportionality proportionality) {
		radius = Math.abs(radius);
		Vector2D output = null;
		if (input.getNorm() <= radius) {
			output = ZERO;
		} else {
			output = input.normalize().scalarMultiply(value)
					.scalarMultiply(calculateProportionality(input, radius, proportionality, ZERO));
		}
		return output;
	}

	/**
	 * Calculates the function value for a tangential field.
	 * 
	 * @param input
	 *            position at which the function value shall be calculated
	 * @param value
	 *            the value of the field
	 * @param radius
	 *            radius of the obstacle (buoy, isle,..)
	 * @param proportionality
	 *            proportionality of the value to the distance to the obstacle
	 * @return calculated function value
	 */
	public static Vector2D tangential(Vector2D input, double value, double radius, Proportionality proportionality) {
		radius = Math.abs(radius);
		Vector2D output;
		Vector2D intermediateResult = null;
		if (input.getNorm() <= radius) {
			output = ZERO;
		} else {
			intermediateResult = new Vector2D(input.getY(), -input.getX());
			output = intermediateResult.normalize().scalarMultiply(value)
					.scalarMultiply(calculateProportionality(input, radius, proportionality, ZERO));
		}

		return output;
	}

	/**
	 * Calculates the function value for a border field.
	 * 
	 * @param input
	 *            position at which the function value shall be calculated
	 * @param value
	 *            the value of the field
	 * @param proportionality
	 *            proportionality of the value to the distance to the border
	 * @return calculated function value
	 */
	public static Vector2D border(Vector2D input, double value, Proportionality proportionality) {
		Vector2D output = null;
		if (input.getX() <= 0.0) {
			output = ZERO;
		} else {
			output = new Vector2D(value, 0.0).scalarMultiply(
					calculateProportionality(input, 0.0, proportionality, new Vector2D(0.0, input.getY())));
		}
		return output;
	}

	// operations----------------------------------------------------------------------------------------------------------

	/**
	 * translation and superposition not implemented here. can be found in
	 * VectorField directly in getFunctionValue()
	 */

	/**
	 * Calculates the function value of a field after a rotation.
	 * 
	 * @param input
	 *            position at which the function value shall be calculated
	 * @param angleInDegree
	 *            angle in degree around the field shall be rotated
	 * @param rotationPoint
	 *            point around the field shall be rotated
	 * @return calculated value
	 */
	public static Vector2D rotate(Vector2D input, double angleInDegree, Vector2D rotationPoint) {
		final double angleInRad = Math.toRadians(Math.floorMod((long) -angleInDegree, (long) 360.0));
		return new Vector2D(
				Math.cos(angleInRad) * (input.getX() - rotationPoint.getX())
						- Math.sin(angleInRad) * (input.getY() - rotationPoint.getY()) + rotationPoint.getX(),
				Math.sin(angleInRad) * (input.getX() - rotationPoint.getX())
						+ Math.cos(angleInRad) * (input.getY() - rotationPoint.getY()) + rotationPoint.getY());

	}

	/**
	 * Calculates the function value of a border after a scale.
	 * 
	 * @param input
	 *            position at which the function value shall be calculated
	 * @param factor
	 *            factor the field shall be scaled with
	 * @return calculated value
	 */
	public static Vector2D scale(Vector2D input, double factor) {
		return new Vector2D(input.getX() * factor, input.getY() * factor);
	}

	/**
	 * Calculates the function value of a field after a shear.
	 * 
	 * @param input
	 *            position at which the function value shall be calculated
	 * @param factor
	 *            factor the field shall be sheared with
	 * @param axis
	 *            axis along the field shall be sheared
	 * @return calculated value
	 */
	public static Vector2D shear(Vector2D input, double factor, Axis axis) {
		Vector2D output = null;
		switch (axis) {
		case X:
			output = new Vector2D(input.getX() + factor * input.getY(), input.getY());
			break;
		case Y:
			output = new Vector2D(input.getX(), factor * input.getX() + input.getY());
			break;
		default:
			System.err.println("AXIS DOES NOT EXIST");
			break;
		}
		return output;
	}

	/**
	 * Calculates the function value of a field after it has been masked.
	 * 
	 * @param input
	 *            output from the operations before this operation
	 * @param initialInput
	 *            position at which the function value shall be calculated
	 * @param shape
	 *            masking shape
	 * @return calculated value
	 */
	public static Vector2D mask(Vector2D input, Vector2D initialInput, Shape shape) {
		Vector2D output;
		if (shape.outOfRange(initialInput)) {
			output = ZERO;
		} else {
			output = input;
		}
		return output;
	}

	/**
	 * Calculates the function value of a field after a part of it has been scaled.
	 * 
	 * @param input
	 *            output from the operations before this operation
	 * @param initialInput
	 *            position at which the function value shall be calculated
	 * @param shape
	 *            shape to scale
	 * @param factor
	 *            factor to scale with
	 * @return calculated value
	 */
	public static Vector2D scaleArea(Vector2D input, Vector2D initialInput, Shape shape, double factor) {
		Vector2D output;
		if (shape.outOfRange(initialInput)) {
			output = input;
		} else {
			output = input.scalarMultiply(factor);
		}
		return output;
	}

	// proportionality----------------------------------------------------------------------------------------------------------

	/**
	 * Calculates the proportionality factor of an input given proportionality of
	 * the value to the distance to the border
	 * 
	 * @param input
	 *            position at which the function value shall be calculated
	 * @param proportionality
	 *            proportionality of the value to the distance to the border
	 * @return calculated factor to multiply the function value with
	 */
	private static double calculateProportionality(Vector2D input, double offset, Proportionality proportionality,
			Vector2D distanceFrom) {
		double factor = 0.0;
		double dist = input.distance(distanceFrom);

		switch (proportionality) {
		case CUBIC:
			factor = Math.pow(dist + offset, 3);
			break;
		case EXPONENTIAL:
			factor = Math.exp(dist + offset);
			break;
		case INVERSELY_CUBIC:
			factor = 1.0 / Math.pow(dist + offset, 3);
			break;
		case INVERSELY_EXPONENTIAL:
			factor = 1.0 / Math.exp(dist + offset);
			break;
		case INVERSELY_LINEAR:
			factor = 1.0 / (dist + offset);
			break;
		case INVERSELY_LOGARITHMIC:
			factor = 1.0 / Math.log(dist + offset);
			break;
		case INVERSELY_QUADRATIC:
			factor = 1.0 / Math.pow(dist, 2);
			break;
		case LINEAR:
			factor = dist + offset;
			break;
		case LOGARITHMIC:
			factor = Math.log(dist + offset);
			break;
		case NONE:
			factor = 1.0;
			break;
		case QUADRATIC:
			factor = Math.pow((input.distance(ZERO) + offset), 2);
			break;
		default:
			System.err.println("PROPORTIONALITY DOES NOT EXIST");
			break;
		}
		return factor;
	}
}