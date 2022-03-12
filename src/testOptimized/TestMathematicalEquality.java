package testOptimized;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import main.fields.BorderField;
import main.fields.HomogenousField;
import main.fields.RadialField;
import main.fields.TangentialField;
import main.fields.VectorField;
import main.util.Axis;
import main.util.shape.Circle;
import main.util.shape.Rectangle;
import main.util.shape.Shape;

public class TestMathematicalEquality {
	//---------For log file only -----------------------------------------------------------------
		private static StringBuilder log = new StringBuilder();
		@AfterClass
	    public static void afterClass() throws IOException {
	        PrintWriter logFile = new PrintWriter("testResults/Test_OptimizedImplementation/" 
	        		+ MethodHandles.lookup().lookupClass().getSimpleName() +".txt", "UTF-8");
	        logFile.write(log.toString());
	        logFile.close();
	    }
		@Rule
	    public TestWatcher watchman = new TestWatcher() {

	        @Override
	        protected void failed(Throwable e, Description description) {
	            if (description != null) {
	        		log.append(description);
	            }
	            if (e != null) {
	                log.append(' ');
	                log.append(e);
	            }
	            log.append(" FAIL\n");
	        }

	        @Override
	        protected void succeeded(Description description) {
	            if (description != null) {
	                log.append(description);
	            }
	            log.append(" OK\n");
	        }
	    };
	  //---------End of logging -----------------------------------------------------------------
	
	final double samplingSistnace = 0.5;
	final Vector2D xRange = new Vector2D(-10,10);
	final Vector2D yRange = new Vector2D(-10,10);

	
	@Before
	public void init() {
		VectorField.setExportPath("C:/Users/Christian/Dropbox/BA/BA Util/Pictures");
	}
	public boolean equal(VectorField[] fields) {
		for (VectorField field : fields) {
			for(double x = xRange.getX(); x < xRange.getY(); x += samplingSistnace) {
				for(double y = yRange.getX(); y < yRange.getY(); y += samplingSistnace) {
					double distance = field.getFunctionValue(x, y)
							.distance(field.getFunctionValueOptimized(x, y));
					if (distance > 0.000000000001) {
						System.out.print(x + " " + y);
						System.out.print(field.getFunctionValue(x, y));
						System.out.println(field.getFunctionValueOptimized(x, y));
						field.export(new Vector2D(-5,5), samplingSistnace, new Vector2D(-5,5), samplingSistnace, "original");
						field.exportUsingOpt(new Vector2D(-5,5), samplingSistnace, new Vector2D(-5,5), samplingSistnace, "optimized");
						return false;
					}
				}
			}
		}	
		return true;
	}
	
	private VectorField[] getBasicFields(){
		VectorField[] basicFields = new VectorField[4];
		basicFields[0] = new HomogenousField();
		basicFields[1] = new RadialField();
		basicFields[2] = new TangentialField();
		basicFields[3] = new BorderField();
		return basicFields;
	}
	
	private VectorField[] getRotatedFields(double degree) {
		VectorField[] vecFields = getBasicFields();
		for (VectorField vectorField : vecFields) {
				vectorField.rotate(degree);
		}
		return vecFields;
	}
	
	private VectorField[] getRotatedFields(double degree, Vector2D center) {
		VectorField[] vecFields = getBasicFields();
		for (VectorField vectorField : vecFields) {
				vectorField.rotate(degree, center);
		}
		return vecFields;
	}
	
	private VectorField[] getShearedFields(double factor, Axis axis) {
		VectorField[] vecFields = getBasicFields();
		for (VectorField vectorField : vecFields) {
				vectorField.shear(factor, axis);
		}
		return vecFields;
	}
	
	private VectorField[] getScaledFields(double factor) {
		VectorField[] vecFields = getBasicFields();
		for (VectorField vectorField : vecFields) {
				vectorField.scale(factor);
		}
		return vecFields;
	}
	
	private VectorField[] getTranslatedFields(double x, double y) {
		VectorField[] vecFields = getBasicFields();
		for (VectorField vectorField : vecFields) {
				vectorField.translate(x, y);
		}
		return vecFields;
	}
	
	private VectorField[] getMaskedFields(Shape shape) {
		VectorField[] vecFields = getBasicFields();
		for (VectorField vectorField : vecFields) {
				vectorField.mask(shape);
		}
		return vecFields;
	}
	
	private VectorField[] getScaledAreaFields(Shape shape, double factor) {
		VectorField[] vecFields = getBasicFields();
		for (VectorField vectorField : vecFields) {
				vectorField.scaleArea(shape, factor);
		}
		return vecFields;
	}
	
	@SuppressWarnings("deprecation")
	private VectorField[] getSuperpositionedFields(VectorField field) {
		VectorField[] vecFields = getBasicFields();
		for (VectorField vectorField : vecFields) {
				vectorField.superposition(field);
		}
		return vecFields;
	}
	
	//---------Test-individual-operations----------------------
	@Test
	public void testPosRotation(){
		VectorField[] vecFields = getRotatedFields(35);
		Assert.assertTrue(equal(vecFields));
	}
	
	@Test
	public void testNegRotation(){
		VectorField[] vecFields = getRotatedFields(-20);
		Assert.assertTrue(equal(vecFields));
	}
	
	@Test
	public void testPosRotationWithPoint(){
		VectorField[] vecFields = getRotatedFields(25, new Vector2D(5, 2));
		Assert.assertTrue(equal(vecFields));
	}
	
	@Test
	public void testNegRotationWithPoint(){
		VectorField[] vecFields = getRotatedFields(-25, new Vector2D(5, 2));
		Assert.assertTrue(equal(vecFields));
	}
	
	
	@Test
	public void testPosShearX(){
		VectorField[] vecFields = getShearedFields(2,  Axis.X);
		Assert.assertTrue(equal(vecFields));
	}
	
	@Test
	public void testNegShearX(){
		VectorField[] vecFields = getShearedFields(-2,  Axis.X);
		Assert.assertTrue(equal(vecFields));
	}
	
	@Test
	public void testPosShearY(){
		VectorField[] vecFields = getShearedFields(0.7,  Axis.Y);
		Assert.assertTrue(equal(vecFields));
	}
	
	@Test
	public void testNegShearY(){
		VectorField[] vecFields = getShearedFields(-0.7,  Axis.Y);
		Assert.assertTrue(equal(vecFields));
	}
	
	@Test
	public void testPosTranslate(){
		VectorField[] vecFields = getTranslatedFields(2, 4);
		Assert.assertTrue(equal(vecFields));
	}
	
	@Test
	public void testNegTranslate(){
		VectorField[] vecFields = getTranslatedFields(-2, -4);
		Assert.assertTrue(equal(vecFields));
	}
	
	@Test
	public void testScale(){
		VectorField[] vecFields = getScaledFields(0.7);
		Assert.assertTrue(equal(vecFields));
	}
	
	@Test
	public void testMaskRectagle(){
		VectorField[] vecFields = getMaskedFields(new Rectangle(2, 3, new Vector2D(0.55, 1.05)));
		Assert.assertTrue(equal(vecFields));
	}
	
	@Test
	public void testMaskCircle(){
		VectorField[] vecFields = getMaskedFields(new Circle(2, 
						new Vector2D(0.55, 1.05)));
		Assert.assertTrue(equal(vecFields));
	}
	
	@Test
	public void testScaleAreaRect(){
		VectorField[] vecFields = getScaledAreaFields(new Rectangle(2, 3, 
						new Vector2D(0.55, 1.05)), 3);
		Assert.assertTrue(equal(vecFields));
	}
	
	@Test
	public void testScaleAreaCircle(){
		VectorField[] vecFields = getScaledAreaFields(new Circle(3, 
						new Vector2D(0.55, 1.05)), 0.5);
		Assert.assertTrue(equal(vecFields));
	}
	
	@Test
	public void testSuperposition(){
		VectorField[] vecFields = getBasicFields();
		for (VectorField vectorField : vecFields) {
			getSuperpositionedFields(vectorField);
		}
		Assert.assertTrue(equal(vecFields));
	}
	
}

