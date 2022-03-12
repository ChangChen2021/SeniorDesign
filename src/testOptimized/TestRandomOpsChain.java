package testOptimized;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;
import java.util.Random;

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

public class TestRandomOpsChain {
	
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

	Random r = new Random();
	
	private int recursionDepth = 0;	//current depth of superpositioned fields
	private int maxDepth = 5;		//maximum depth of superpositioned fields
	
	@Before
	public void init() {
		VectorField.setExportPath("C:/Users/Christian/Dropbox/BA/BA Util/DebugInfo");
	}
	
    //The Operations are tested on all basic vector fields
	private static VectorField[] getBasicFields(){
		VectorField[] basicFields = new VectorField[4];
		basicFields[0] = new HomogenousField();
		basicFields[1] = new RadialField();
		basicFields[2] = new TangentialField();
		basicFields[3] = new BorderField();
		return basicFields;
	}
	
	private double random(int range) {
		return ((r.nextDouble()*2)-1)*range;
	}
	
	private void applyRandomOp(VectorField[] f, int op) {
		switch (op) {
		case 0:
			getRotatedFields(f, random(360));
			break;
		case 1:
			getRotatedFields(f, random(5), new Vector2D(random(5), random(5)));
			break;
		case 2:
			getShearedFields(f, random(5),  Axis.X);
			break;
		case 3:
			getShearedFields(f, random(5),  Axis.Y);
			break;
		case 4:
			getTranslatedFields(f, random(5), random(5));
			break;
		case 5:
			getScaledFields(f, random(2));
			break;
		case 6:
			getMaskedFields(f, new Rectangle(random(3), random(3), new Vector2D(random(3), random(3))));
			break;
		case 7:
			getMaskedFields(f, new Circle(random(3), new Vector2D(random(3), random(3))));
			break;
		case 8:
			getScaledAreaFields(f, new Rectangle(random(3), random(3), new Vector2D(random(3), random(3))), random(3));
			break;
		case 9:
			getScaledAreaFields(f, new Circle(random(3), new Vector2D(random(3), random(3))), random(3));
			break;
		case 10:
			recursionDepth++;
				VectorField[] forSuperPos = {getBasicFields()[r.nextInt(4)]};
				int opsOnSUField = r.nextInt(11);
				for (int i = 0; i < opsOnSUField; i++) {
					applyRandomOp(forSuperPos, r.nextInt(recursionDepth >= maxDepth ? 10 : 11)); //This excludes SU (10) to prevent infinite loops
				}
				getSuperpositionedFields(f, forSuperPos[0]);
				System.out.println(recursionDepth);
			recursionDepth--;
			break;

		default:
			System.err.println("operation input out of range " + op);
		}
	}
	
	private static void getRotatedFields(VectorField[] vecFields, double degree) {
		for (VectorField vectorField : vecFields) {
				vectorField.rotate(degree);
		}
	}
	
	private static void getRotatedFields(VectorField[] vecFields, double degree, Vector2D center) {
		for (VectorField vectorField : vecFields) {
				vectorField.rotate(degree, center);
		}
	}
	
	private static void getShearedFields(VectorField[] vecFields, double factor, Axis axis) {
		for (VectorField vectorField : vecFields) {
				vectorField.shear(factor, axis);
		}
	}
	
	private static void getScaledFields(VectorField[] vecFields, double factor) {
		for (VectorField vectorField : vecFields) {
				vectorField.scale(factor);
		}
	}
	
	private static void getTranslatedFields(VectorField[] vecFields, double x, double y) {
		for (VectorField vectorField : vecFields) {
				vectorField.translate(x, y);
		}
	}
	
	private static void getMaskedFields(VectorField[] vecFields, Shape shape) {
		for (VectorField vectorField : vecFields) {
				vectorField.mask(shape);
		}
	}
	
	private static void getScaledAreaFields(VectorField[] vecFields, Shape shape, double factor) {
		for (VectorField vectorField : vecFields) {
				vectorField.scaleArea(shape, factor);
		}
	}
	
	@SuppressWarnings("deprecation")
	private static void getSuperpositionedFields(VectorField[] vecFields, VectorField field) {
		for (VectorField vectorField : vecFields) {
				vectorField.superposition(field);
		}
	}
	
	private VectorField[] appllyRandomOps(VectorField[] fields, int amountOfOps) {
		int op;
		for (int i = 0; i < amountOfOps; i++) {
			op = r.nextInt(11);
			applyRandomOp(fields, op);
		}
		return fields;
	}
	/* ---------- Start of the actual tests --------------------
	 * All Operations are applied on input fields
	 * As this is a parameterized test, the methods run for 
	 * a selection of basic vector fields that each had a different 
	 * operation applied. Therefore all possible operation combinations 
	 * for two operations are tested
	 */
	
	@Test
	public void test5ops(){
		Assert.assertTrue(Equal.equal(appllyRandomOps(getBasicFields(), 5)));
	}
	
	@Test
	public void test10ops(){
		Assert.assertTrue(Equal.equal(appllyRandomOps(getBasicFields(), 10)));
	}
	
	@Test
	public void test15ops(){
		Assert.assertTrue(Equal.equal(appllyRandomOps(getBasicFields(), 15)));
	}
	
//	@Test
//	public void test20ops(){
//		Assert.assertTrue(equal(appllyRandomOps(getBasicFields(), 20)));
//	}
//	
//	@Test
//	public void test25ops(){
//		Assert.assertTrue(equal(appllyRandomOps(getBasicFields(), 25)));
//	}
//	
//	@Test
//	public void test40ops(){
//		Assert.assertTrue(equal(appllyRandomOps(getBasicFields(), 40)));
//	}
}
