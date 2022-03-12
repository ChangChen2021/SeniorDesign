package main.fields;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Pair;

import main.util.Axis;
import main.util.FieldAndOperation;
import main.util.MatrixOperations;
import main.util.Method;
import main.util.Proportionality;
import main.util.shape.Shape;

/**
 * Represents a vector field.
 * 
 * @author Christian Weber
 */
public class VectorFieldLight {
	
	/**
	 * flag whether the input matrix is unchanged
	 */
	private boolean identityMatrixInput = true;
	
	/**
	 * flag whether the output matrix is unchanged
	 */
	private boolean identityMatrixOutput = true;

	/**
	 * zero point
	 */
	private final Vector2D ZERO = new Vector2D(0.0, 0.0);

	/**
	 * Operations to be applied on the input of the vector function
	 */
	private RealMatrix operationsOnInput = MatrixOperations.IDENTITY_MATRIX.copy();
	
	/**
	 * Operations to be applied on the output of the vector function
	 */
	private RealMatrix operationsOnOutput = MatrixOperations.IDENTITY_MATRIX.copy();
	
	/**
	 * the field Function describing the underlying basic field type and its properties
	 */
	private final Pair<FieldAndOperation, Object[]> fieldFunction;
	
	/**
	 * Fields to be in superposition to this field
	 */
	private List<VectorFieldLight> superposedFields = new LinkedList<VectorFieldLight>();
	
	/**
	 * Mask operations applied to this field. The matrix holds all inverted 
	 * operations applied to the field after the mask operation was applied
	 */
	private List<Pair<RealMatrix, Shape>> maskOperations = new LinkedList<>();
	
	/**
	 * Scale area operations applied to this field.
	 */
	private List<Pair<RealMatrix, Pair<Shape, Double>>> scaleAreaOperations = new LinkedList<>();
	
	/**
	 * Constructor to create a basic vector field with no operations applied yet.
	 * 
	 * @param fieldFunction 
	 * 					contains the type of field and its properties
	 */
	public VectorFieldLight(Pair<FieldAndOperation, Object[]> fieldFunction) {
		this.fieldFunction = fieldFunction;
		this.operationsOnInput = MatrixOperations.IDENTITY_MATRIX;
		this.operationsOnOutput = MatrixOperations.IDENTITY_MATRIX;
	}
	
	/**
	 * Constructor to create a vector field with given properties and operations:
	 * 
	 * @param fieldFunction	
	 * 					the underlying function of the basic vector field and its properties
	 * @param operationsOnInput 
	 * 					the matrix that holds information of all operations to be applied 
	 * 					on the input of the fieldFunction
	 * @param operationsOnOutput 
	 * 					the matrix that holds information of all operations to be applied 
	 * 					on the output of the fieldFunction
	 * @param nonlinearTrans
	 * 					an Array of non linear transformations such as superposed fields, masks and scale areas
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public VectorFieldLight(Pair<FieldAndOperation, Object[]> fieldFunction, RealMatrix operationsOnInput, 
			RealMatrix operationsOnOutput, List[] nonlinearTrans) {
		this.fieldFunction = fieldFunction;
		this.operationsOnInput = operationsOnInput;
		this.operationsOnOutput = operationsOnOutput;
		this.superposedFields = (List<VectorFieldLight>)nonlinearTrans[0];
		this.maskOperations = (List<Pair<RealMatrix, Shape>>)nonlinearTrans[1];
		this.scaleAreaOperations = (List<Pair<RealMatrix, Pair<Shape, Double>>>)nonlinearTrans[2];
		if(!operationsOnInput.equals(MatrixOperations.IDENTITY_MATRIX)) {
			this.identityMatrixInput = false;
		}
		if(!operationsOnOutput.equals(MatrixOperations.IDENTITY_MATRIX)) {
			this.identityMatrixOutput = false;
		}
	}
	
	/**
	 * Creates a deep copy of this VectorFieldLight
	 * 
	 * @return a deep copy of this
	 */
	@SuppressWarnings("rawtypes")
	public VectorFieldLight deepCopy() {
		List<VectorFieldLight> superpositionFieldsCopy = new LinkedList<>();
		List<Pair<RealMatrix, Shape>> maskOperationsCopy = new LinkedList<>();
		List<Pair<RealMatrix, Pair<Shape, Double>>> scaleAreaOperationsCopy = new LinkedList<>();
		List[] nonlinearTrans = new List[3];

		superposedFields.forEach(field -> superpositionFieldsCopy.add(field.deepCopy()));
		maskOperations.forEach(pair -> maskOperationsCopy.add(pair)); //pairs are immutable no need to copy
		scaleAreaOperations.forEach(pair -> scaleAreaOperationsCopy.add(pair));
		
		nonlinearTrans[0] = superpositionFieldsCopy;
		nonlinearTrans[1] = maskOperationsCopy;
		nonlinearTrans[2] = scaleAreaOperationsCopy;
		
		return new VectorFieldLight(fieldFunction, operationsOnInput.copy(), operationsOnOutput.copy(), nonlinearTrans);
	}

	/**
	 * Rotate field around specified point.
	 * 
	 * @param degree
	 *            rotate to this angle
	 * @param rotationPoint
	 *            point to rotate around
	 */
	public void rotate(double degree, Vector2D rotationPoint) {
		operationsOnOutput = MatrixOperations.rotate(degree).multiply(operationsOnOutput);
		operationsOnInput = operationsOnInput.multiply(
				MatrixOperations.invert(MatrixOperations.rotate(degree, rotationPoint))
				);
		superposedFields.forEach(field -> field.rotate(degree, rotationPoint));
		identityMatrixInput = false;
		identityMatrixOutput = false;
	}

	/**
	 * Scale a specified area (shape).
	 * 
	 * @param shape
	 *            shape to scale
	 * @param factor
	 *            scaling factor
	 */
	public void scaleArea(Shape shape, double factor) {
		superposedFields.forEach(field -> field.scaleArea(shape, factor));
		scaleAreaOperations.add(new Pair<>(MatrixOperations.invert(operationsOnInput.copy()), new Pair<>(shape, factor)));
	}

	/**
	 * Shear by specified axis.
	 * 
	 * @param factor
	 *            shearing factor
	 * @param axis
	 *            axis to shear by
	 */
	public void shear(double factor, Axis axis) {
		operationsOnOutput = MatrixOperations.shear(factor, axis).multiply(operationsOnOutput);
		operationsOnInput = operationsOnInput.multiply(MatrixOperations.invert(
				MatrixOperations.shear(factor, axis)));
		superposedFields.forEach(field -> field.shear(factor, axis));
		identityMatrixInput = false;
		identityMatrixOutput = false;
	}

	/**
	 * Mask a specified shape.
	 * 
	 * @param shape
	 *            shape to mask
	 */
	public void mask(Shape shape) {
		maskOperations.add(new Pair<RealMatrix, Shape>(MatrixOperations.invert(operationsOnInput.copy()), shape));
		superposedFields.forEach(field -> field.mask(shape));
	}

	/**
	 * Translate by specified values.
	 * 
	 * @param alongXaxis
	 *            translation value along x axis
	 * @param alongYaxis
	 *            translation value along y axis
	 */
	public void translate(double alongXaxis, double alongYaxis) {
		operationsOnInput = operationsOnInput.multiply(
				MatrixOperations.invert(MatrixOperations.translate(alongXaxis, alongYaxis)));
		superposedFields.forEach(field -> field.translate(alongXaxis, alongYaxis));
		identityMatrixInput = false;
	}
	
	/**
	 * Scale by factor.
	 * 
	 * @param factor
	 *            scaling factor
	 */
	public void scale(double factor) {
		operationsOnOutput = MatrixOperations.scale(factor).multiply(operationsOnOutput);
		superposedFields.forEach(field -> field.scale(factor));
		identityMatrixOutput = false;
	}
	
	/**
	 * Superposition a field with another field.
	 * 
	 * @param field
	 *            field to superposition with
	 */
	public void superposition(VectorFieldLight field) {
		superposedFields.add(field.deepCopy());
	}
	
	/**
	 * Calculates the corresponding vector for a given coordinate using matrices for linear Operations
	 * 
	 * @param x coordinate
	 * @param y coordinate
	 * @return the vector for the given input coordinates
	 */
	public Vector2D getFunctionValue(double x, double y) {
		Vector2D input = new Vector2D(x, y);
		Vector2D output = ZERO;
		
		boolean masked = false;
		
		FieldAndOperation fieldType = fieldFunction.getFirst();
		Object[] parameters = fieldFunction.getSecond();
		
		// field parameters
		double value;
		double radius;
		Proportionality proportionality;
				
		Vector2D intermediateResult;
		//Apply inner Operations
		if(!identityMatrixInput) {
			intermediateResult =  MatrixOperations.applyMatrixToVector(operationsOnInput, input);
		} else {
			intermediateResult = input;
		}
		
		//apply masks
		if(!maskOperations.isEmpty()) {
			for (Pair<RealMatrix, Shape> pair : maskOperations) {
				Vector2D point = MatrixOperations.applyMatrixToVector(pair.getFirst(), intermediateResult);
				if(pair.getSecond().outOfRange(point)) {
					masked = true;
					break;
				}
				//we cannot return the zero vector just yet, 
				//as superpositions might lead to a non zero vector return
			}
		}
		
		if(!masked) {
			//calculate function of basic vector field
			switch (fieldType) {
			case ZERO:
				output = Method.zero();
				break;
			case HOMOGENOUS:
				value = (double) parameters[0];
	
				output = Method.homogenous(intermediateResult, value);
				break;
			case RADIAL:
				value = (double) parameters[0];
				radius = (double) parameters[1];
				proportionality = (Proportionality) parameters[2];
	
				output = Method.radial(intermediateResult, value, radius, proportionality);
				break;
			case TANGENTIAL:
				value = (double) parameters[0];
				radius = (double) parameters[1];
				proportionality = (Proportionality) parameters[2];
	
				output = Method.tangential(intermediateResult, value, radius, proportionality);
				break;
			case BORDER:
				value = (double) parameters[0];
				proportionality = (Proportionality) parameters[1];
	
				output = Method.border(intermediateResult, value, proportionality);
				break;
			default:
				System.err.println("FIELD OR OPERATION DOES NOT EXIST");
				break;
			}
			
			//appy output matrix
			if(!identityMatrixOutput) {
				output = MatrixOperations.applyMatrixToVector(operationsOnOutput, output);
			}
			
			//apply scale area	
			if(!scaleAreaOperations.isEmpty()) {
				double factor = 1.0;
				for(Pair<RealMatrix, Pair<Shape, Double>> p : scaleAreaOperations) {
					factor *= p.getSecond().getFirst().outOfRange(MatrixOperations.applyMatrixToVector(p.getFirst(), intermediateResult)) ? 1 : p.getSecond().getSecond();
				}
				output = output.scalarMultiply(factor);	
			}
		}		
		
		//apply superpositions
		if(!superposedFields.isEmpty()) {			
			for(VectorFieldLight f : superposedFields) {
				output = output.add(f.getFunctionValue(x, y));
			}
		}
		return output;
	}
	
}