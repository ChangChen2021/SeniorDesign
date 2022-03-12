package testOptimized;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import main.fields.BorderField;
import main.fields.HomogenousField;
import main.fields.RadialField;
import main.fields.TangentialField;
import main.fields.VectorField;
import main.util.Axis;
import main.util.shape.Circle;
import main.util.shape.Rectangle;
import main.util.shape.Shape;

@RunWith(Parameterized.class)
public class TestAllPairCombinations {
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

	final double maxResolution = 0.000000000001;
	final double samplingSistnace = 0.5;
	final Vector2D xRange = new Vector2D(-10,10);
	final Vector2D yRange = new Vector2D(-10,10);

	
	@Before
	public void init() {
		VectorField.setExportPath("C:/Users/Christian/Dropbox/BA/BA Util/DebugInfo");
	}
	
	public boolean equal(VectorField[] fields) {
		for (VectorField field : fields) {
			for(double x = xRange.getX(); x < xRange.getY(); x += samplingSistnace) {
				for(double y = yRange.getX(); y < yRange.getY(); y += samplingSistnace) {
					double distance = field.getFunctionValue(x, y)
							.distance(field.getFunctionValueOptimized(x, y));
					if (distance > maxResolution) {
						
						//Check potential Border Conflict
						double distanceN = field.getFunctionValue(x, y)
								.distance(field.getFunctionValueOptimized(x + maxResolution, y));
						double distanceE = field.getFunctionValue(x, y)
								.distance(field.getFunctionValueOptimized(x, y + maxResolution));
						double distanceS = field.getFunctionValue(x, y)
								.distance(field.getFunctionValueOptimized(x - maxResolution, y));
						double distanceW = field.getFunctionValue(x, y)
								.distance(field.getFunctionValueOptimized(x, y - maxResolution));
						//diagonal
						double distanceNE = field.getFunctionValue(x, y)
								.distance(field.getFunctionValueOptimized(x + maxResolution, y + maxResolution));
						double distanceSE = field.getFunctionValue(x, y)
								.distance(field.getFunctionValueOptimized(x - maxResolution, y + maxResolution));
						double distanceSW = field.getFunctionValue(x, y)
								.distance(field.getFunctionValueOptimized(x - maxResolution, y - maxResolution));
						double distanceNW = field.getFunctionValue(x, y)
								.distance(field.getFunctionValueOptimized(x + maxResolution, y - maxResolution));
						
						
						if (distanceN < maxResolution || distanceE < maxResolution 
								|| distanceS < maxResolution || distanceW < maxResolution
								|| distanceNE < maxResolution || distanceSE < maxResolution
								|| distanceSW < maxResolution || distanceNW < maxResolution) {
							//Border conflict resolved
						} else {
							System.out.print(x + " " + y);
							System.out.print(field.getFunctionValue(x, y));
							System.out.println(field.getFunctionValueOptimized(x, y));
							double time = System.currentTimeMillis();
							field.export(new Vector2D(-5,5), samplingSistnace, new Vector2D(-5,5), samplingSistnace, Double.toString(time));
							field.exportUsingOpt(new Vector2D(-5,5), samplingSistnace, new Vector2D(-5,5), samplingSistnace, Double.toString(time) + "opt");
							return false;
						}
					}
				}
			}
		}	
		return true;
	}
	
	@Parameters
    public static Collection<Object[]> data() {
    	return Arrays.asList(new Object[][]{
        		{new OperationForPairCombo() {
					@Override
					public VectorField[] getFields() {
						return getRotatedFields(getBasicFields(), 34);
					}
				}}, 
        		{new OperationForPairCombo() {
					@Override
					public VectorField[] getFields() {
						return getRotatedFields(getBasicFields(), 25, new Vector2D(5, 2));
					}
				}}, 
        		{new OperationForPairCombo() {
					@Override
					public VectorField[] getFields() {
						return getShearedFields(getBasicFields(), 2,  Axis.X);
					}
				}}, 
        		{new OperationForPairCombo() {
					@Override
					public VectorField[] getFields() {
						return getShearedFields(getBasicFields(), -2,  Axis.Y);
					}
				}}, 
        		{new OperationForPairCombo() {
					@Override
					public VectorField[] getFields() {
						return getTranslatedFields(getBasicFields(), 2, 4);
					}
				}}, 
        		{new OperationForPairCombo() {
					@Override
					public VectorField[] getFields() {
						return getScaledFields(getBasicFields(), 0.7);
					}
				}}, 
        		{new OperationForPairCombo() {
					@Override
					public VectorField[] getFields() {
						return getMaskedFields(getBasicFields(), new Rectangle(2, 3, new Vector2D(0.55, 1.05)));
					}
				}}, 
        		{new OperationForPairCombo() {
					@Override
					public VectorField[] getFields() {
						return getMaskedFields(getBasicFields(), new Circle(2, new Vector2D(0.55, 1.05)));
					}
				}}, 
        		{new OperationForPairCombo() {
					@Override
					public VectorField[] getFields() {
						return getScaledAreaFields(getBasicFields(), new Rectangle(2, 3, new Vector2D(0.55, 1.05)), 3);
					}
				}}, 
        		{new OperationForPairCombo() {
					@Override
					public VectorField[] getFields() {
						return getScaledAreaFields(getBasicFields(), new Circle(3, new Vector2D(0.55, 1.05)), 0.5);
					}
				}}, 
        		{new OperationForPairCombo() {
					@Override
					public VectorField[] getFields() {
						return getSuperpositionedFields(getBasicFields(), getBasicFields()[2]);
					}
				}}, 
        });
    }
    
    VectorField[] vecFields;
    final OperationForPairCombo ofpc;
    
    public TestAllPairCombinations(OperationForPairCombo ofpc) {
    	this.ofpc = ofpc;
    }
    
    //------The following are all static help methods required for calculating the parameters for this test:-------
    
	private static VectorField[] getBasicFields(){
		VectorField[] basicFields = new VectorField[4];
		basicFields[0] = new HomogenousField();
		basicFields[1] = new RadialField();
		basicFields[2] = new TangentialField();
		basicFields[3] = new BorderField();
		return basicFields;
	}
	
	private static VectorField[] getRotatedFields(VectorField[] vecFields, double degree) {
		for (VectorField vectorField : vecFields) {
				vectorField.rotate(degree);
		}
		return vecFields;
	}
	
	private static VectorField[] getRotatedFields(VectorField[] vecFields, double degree, Vector2D center) {
		for (VectorField vectorField : vecFields) {
				vectorField.rotate(degree, center);
		}
		return vecFields;
	}
	
	private static VectorField[] getShearedFields(VectorField[] vecFields, double factor, Axis axis) {
		for (VectorField vectorField : vecFields) {
				vectorField.shear(factor, axis);
		}
		return vecFields;
	}
	
	private static VectorField[] getScaledFields(VectorField[] vecFields, double factor) {
		for (VectorField vectorField : vecFields) {
				vectorField.scale(factor);
		}
		return vecFields;
	}
	
	private static VectorField[] getTranslatedFields(VectorField[] vecFields, double x, double y) {
		for (VectorField vectorField : vecFields) {
				vectorField.translate(x, y);
		}
		return vecFields;
	}
	
	private static VectorField[] getMaskedFields(VectorField[] vecFields, Shape shape) {
		for (VectorField vectorField : vecFields) {
				vectorField.mask(shape);
		}
		return vecFields;
	}
	
	private static VectorField[] getScaledAreaFields(VectorField[] vecFields, Shape shape, double factor) {
		for (VectorField vectorField : vecFields) {
				vectorField.scaleArea(shape, factor);
		}
		return vecFields;
	}
	
	@SuppressWarnings("deprecation")
	private static VectorField[] getSuperpositionedFields(VectorField[] vecFields, VectorField field) {
		for (VectorField vectorField : vecFields) {
				vectorField.superposition(field);
		}
		return vecFields;
	}
	
	
	
	/* ---------- Start of the actual tests --------------------
	 * All Operations are applied on input fields
	 * As this is a parameterized test, the methods run for 
	 * a selection of basic vector fields that each had a different 
	 * operation applied. Therefore all possible operation combinations 
	 * for two operations are tested
	 */
	
	@Test
	public void testXRot(){
		Assert.assertTrue(equal(getRotatedFields(ofpc.getFields(), 20)));
	}
	
	@Test
	public void testXRotPoint(){
		Assert.assertTrue(equal(getRotatedFields(ofpc.getFields(), 20, new Vector2D(1, 3))));
	}
	
	@Test
	public void testXSchX(){
		Assert.assertTrue(equal(getShearedFields(ofpc.getFields(), 2, Axis.X)));
	}
	
	@Test
	public void testXSchY(){
		Assert.assertTrue(equal(getShearedFields(ofpc.getFields(), 2, Axis.Y)));
	}
	
	@Test
	public void testXScale(){
		Assert.assertTrue(equal(getScaledFields(ofpc.getFields(), 2)));
	}
	
	@Test
	public void testXTrans(){
		Assert.assertTrue(equal(getTranslatedFields(ofpc.getFields(), 2, 4)));
	}
	
	@Test
	public void testXMaskRect(){
		Assert.assertTrue(equal(getMaskedFields(ofpc.getFields(), new Rectangle(2, 3, new Vector2D(1,1)))));
	}
	
	@Test
	public void testXMaskCircle(){
		Assert.assertTrue(equal(getMaskedFields(ofpc.getFields(), new Circle(2, new Vector2D(2,1)))));
	}
	
	@Test
	public void testXScaleAreaRect(){
		Assert.assertTrue(equal(getScaledAreaFields(ofpc.getFields(), new Rectangle(2, 3, new Vector2D(1,1)), 0.4)));
	}
	
	@Test
	public void testXScaleAreaCircle(){
		Assert.assertTrue(equal(getScaledAreaFields(ofpc.getFields(), new Circle(3, new Vector2D(0,-1)), 4)));
	}
	
	@Test
	public void testXSuperpostion(){
		Assert.assertTrue(equal(getSuperpositionedFields((ofpc.getFields()), getBasicFields()[1])));
	}

}
