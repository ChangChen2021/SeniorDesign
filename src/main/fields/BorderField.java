package main.fields;

import java.util.UUID;

import main.util.FieldAndOperation;
import main.util.Proportionality;

/**
 * Represents a border vector field
 * 
 * @author Manuela Kastner
 *
 */
public class BorderField extends VectorField {

	/**
	 * Constructor
	 * 
	 * @param value
	 *            value of the field
	 * @param proportionality
	 *            proportionality of the value to the distance from the border
	 */
	public BorderField(double value, Proportionality proportionality) {
		super(FieldAndOperation.BORDER, new Object[] { value, proportionality });
	}

	/**
	 * Constructor
	 */
	public BorderField() {
		super(FieldAndOperation.BORDER, new Object[] { 1.0, Proportionality.INVERSELY_EXPONENTIAL });
	}
	
	/**
	 * Constructor to generate Field with existing ID
	 */
	BorderField(UUID fieldID, double value, Proportionality proportionality) {
		super(fieldID, FieldAndOperation.BORDER, new Object[] { 1.0, Proportionality.INVERSELY_EXPONENTIAL });
	}

}
