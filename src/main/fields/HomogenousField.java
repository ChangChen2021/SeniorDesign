package main.fields;

import java.util.UUID;

import main.util.FieldAndOperation;

/**
 * Represents a homogenous vector field
 * 
 * @author Manuela Kastner
 *
 */
public class HomogenousField extends VectorField {

	/**
	 * Constructor
	 * 
	 * @param value
	 *            value of the field
	 */
	public HomogenousField(double value) {
		super(FieldAndOperation.HOMOGENOUS, new Object[] { value });
	}

	/**
	 * Constructor
	 */
	public HomogenousField() {
		super(FieldAndOperation.HOMOGENOUS, new Object[] { 1.0 });
	}
	
	/**
	 * Constructor to generate Field with existing ID
	 */
	HomogenousField(UUID fieldID, double value) {
		super(fieldID, FieldAndOperation.HOMOGENOUS, new Object[] { 1.0 });
	}
}