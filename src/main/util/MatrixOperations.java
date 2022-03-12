package main.util;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * Supplies matrices for selected linear Transformations
 * 
 * @author Christian
 *
 */
public class MatrixOperations {
	
	/**
	 * The Identity Matrix
	 */
	public static final RealMatrix IDENTITY_MATRIX = new Array2DRowRealMatrix(new double[][] {
        { 1.0, 0.0, 0.0 },
        { 0.0, 1.0, 0.0 },
        { 0.0, 0.0, 1.0 }
    });

	/**
	 * Rotation matrix to rotate around any given point. The result matrix combines the operations
	 * of translation - rotation - translation 
	 * 
	 * @param angleInDegree angle to rotate
	 * @param rotationPoint pivot point for rotation
	 * @return the matrix that rotates a vector by given angle around the given point
	 */
	public static RealMatrix rotate(double angleInDegree, Vector2D rotationPoint) {
		RealMatrix rotation = rotate(angleInDegree);
		RealMatrix translation = translate(rotationPoint.getX(), rotationPoint.getY());
		return translation.multiply(rotation).multiply(invert(translation));
	}
	
	/**
	 * Rotation matrix to rotate around center coordinate
	 * 
	 * @param angleInDegree rotate to this angle
	 * @return matrix to rotate a vector by given angle
	 */
	public static RealMatrix rotate(double angleInDegree) {
		final double angleInRad = Math.toRadians(Math.floorMod((long) -angleInDegree, (long) 360.0));
		return new Array2DRowRealMatrix(new double[][] {
		        { Math.cos(angleInRad), -Math.sin(angleInRad), 0.0 },
		        { Math.sin(angleInRad), Math.cos(angleInRad), 0.0 },
		        { 0.0, 0.0, 1.0 }
		    });
	}
	
	/**
	 * Standard matrix for shearing
	 * 
	 * @param factor to shear by
	 * @param axis to shear along
	 * @return matrix to shear a vector by given factor along given axis
	 */
	public static RealMatrix shear(double factor, Axis axis) {
		RealMatrix output = null;
		switch (axis) {
		case X:
			output = new Array2DRowRealMatrix(new double[][] {
		        { 1.0, factor, 0.0 },
		        { 0.0, 1.0, 0.0 },
		        { 0.0, 0.0, 1.0 }
		    });
			break;
		case Y:
			output = new Array2DRowRealMatrix(new double[][] {
		        { 1.0,  0.0, 0.0 },
		        { factor, 1.0, 0.0 },
		        { 0.0, 0.0, 1.0 }
		    });
			break;
		default:
			System.err.println("AXIS DOES NOT EXIST");
			break;
		}
		return output;
	}
	
	/**
	 * Standard matrix for translation
	 * 
	 * @param alongXaxis translation along x axis
	 * @param alongYaxis translation along y axis
	 * @return matrix to translate a vector by given ofset
	 */
	public static RealMatrix translate(double alongXaxis, double alongYaxis) {
		return new Array2DRowRealMatrix(new double[][] {
	        { 1.0, 0.0, alongXaxis },
	        { 0.0, 1.0, alongYaxis },
	        { 0.0, 0.0, 1.0 }
	    });
	}
	
	/**
	 * Standard matrix to scale vectors
	 * 
	 * @param scaleFaftor to scale by
	 * @return matrix to scale vectors by given factor
	 */
	public static RealMatrix scale(double scaleFaftor) {
		return new Array2DRowRealMatrix(new double[][] {
	        { scaleFaftor, 0.0, 0.0 },
	        { 0.0, scaleFaftor, 0.0 },
	        { 0.0, 0.0, 1.0 }
	    });
	}
	
	/**
	 * Inverts a matrix 
	 * 
	 * @param toInvert matrix to be inverted
	 * @return inverted matrix
	 */
	public static RealMatrix invert(RealMatrix toInvert) {
		DecompositionSolver solver = new LUDecomposition(toInvert).getSolver();
		return solver.solve(IDENTITY_MATRIX);
	}
	
	/**
	 * Converts 2D vector to 3D vector using homogeneous coordinates in order
	 * to multiply a 3D matrix with the 3D vector.
	 * trims the resulting vector back to 2D to return it
	 * 
	 * @param matrix 3D matrix to be applied to 2D vector
	 * @param vec 2D vector to be multiplied by the 3D matrix
	 * @return result of matrix times vector as 2D vector  
	 */
	public static Vector2D applyMatrixToVector(RealMatrix matrix, Vector2D vec){
		RealMatrix hommogeneKoordinates = new Array2DRowRealMatrix( new double[] {vec.getX(), vec.getY(), 1} );
		RealMatrix result = matrix.multiply(hommogeneKoordinates);
		return new Vector2D(result.getEntry(0,0), result.getEntry(1,0));
	}
	
}
