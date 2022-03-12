package main.fields;

import java.util.UUID;

import main.util.FieldAndOperation;

public class SuperposedField extends VectorField {

	/**
	 * Creates new vector field with all values being zero
	 */
	public SuperposedField() {
		super(FieldAndOperation.ZERO, new Object[] {});
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor to generate Field with existing ID
	 */
	SuperposedField(UUID fieldID) {
		super(fieldID, FieldAndOperation.ZERO, new Object[] {});
	}
}
