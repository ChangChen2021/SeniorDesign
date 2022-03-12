package main.fields;

import java.util.UUID;

import main.util.FieldAndOperation;
import main.util.Proportionality;

/**
 * Represents a tangential vector field
 * 
 * @author Manuela Kastner
 *
 */
public class TangentialField extends VectorField {
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
	public TangentialField(double value, double radius, Proportionality proportionality) {
		super(FieldAndOperation.TANGENTIAL, new Object[] { value, radius, proportionality });
	}

	/**
	 * Constructor
	 */
	public TangentialField() {
		super(FieldAndOperation.TANGENTIAL, new Object[] { 1.0, 0.0, Proportionality.INVERSELY_EXPONENTIAL });
	}

	/**
	 * Constructor to generate Field with existing ID
	 */
	TangentialField(UUID fieldID, double value, double radius, Proportionality proportionality) {
		super(fieldID, FieldAndOperation.TANGENTIAL, new Object[] { value, radius, proportionality });
	}
	
}
