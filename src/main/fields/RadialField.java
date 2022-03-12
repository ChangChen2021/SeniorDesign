package main.fields;

import java.util.UUID;

import main.util.FieldAndOperation;
import main.util.Proportionality;

/**
 * Represents a radial vector field
 * 
 * @author Manuela Kastner
 *
 */
public class RadialField extends VectorField {

	/**
	 * Constructor
	 * 
	 * @param value
	 *            value of the field
	 * @param radius
	 *            radius of the obstacle
	 * @param proportionality
	 *            proportionality of the value to the distance from the obstacle
	 */
	public RadialField(double value, double radius, Proportionality proportionality) {
		super(FieldAndOperation.RADIAL, new Object[] { value, radius, proportionality });
	}

	/**
	 * Constructor
	 */
	public RadialField() {
		super(FieldAndOperation.RADIAL, new Object[] { 1.0, 0.0, Proportionality.INVERSELY_EXPONENTIAL });
	}
	
	/**
	 * Constructor to generate Field with existing ID
	 */
	RadialField(UUID fieldID, double value, double radius, Proportionality proportionality) {
		super(fieldID, FieldAndOperation.RADIAL, new Object[] { value, radius, proportionality });
	}

}