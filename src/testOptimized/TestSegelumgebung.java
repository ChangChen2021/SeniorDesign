package testOptimized;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.AfterClass;
import org.junit.Assert;
import main.fields.BorderField;
import main.fields.HomogenousField;
import main.fields.RadialField;
import main.fields.TangentialField;
import main.fields.VectorField;
import main.util.Proportionality;
import main.util.shape.Circle;
import main.util.shape.Rectangle;
@SuppressWarnings("deprecation")
public class TestSegelumgebung {
	
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
	    
	HomogenousField hom;
	HomogenousField homS2;
	
	
	
	@Before
	public void TestUmgebung() {
		VectorField.setExportPath("C:/Users/Christian/Dropbox/BA/BA Util/Pictures");
		
		hom = new HomogenousField(0);
		BorderField b = new BorderField(10.0, Proportionality.INVERSELY_EXPONENTIAL);
		b.translate(-11, 0);
		hom.superposition(b);

		b.rotate(90);

		hom.superposition(b);
		b.rotate(90);
		hom.superposition(b);
		b.rotate(90);
		hom.superposition(b);

		RadialField rad = new RadialField(10.0, 1.0, Proportionality.INVERSELY_EXPONENTIAL);
		hom.superposition(rad);

		TangentialField tan = new TangentialField(3.0, 1.0, Proportionality.INVERSELY_EXPONENTIAL);
		hom.superposition(tan);

		HomogenousField hom2 = new HomogenousField(0);
		HomogenousField hom3 = new HomogenousField(2.0);
		hom3.mask(new Rectangle(4, 8));
		hom3.translate(-6, 0);
		
		hom2.superposition(hom3);
		hom3.rotate(90);
		hom2.superposition(hom3);
		hom3.rotate(90);
		hom2.superposition(hom3);
		hom3.rotate(90);
		hom2.superposition(hom3);
		
		HomogenousField hom4 = new HomogenousField(2.0);
		hom4.mask(new Rectangle(4, 5));
		hom4.rotate(45);
		hom4.translate(-5, 5);
		hom2.superposition(hom4);

		hom4.rotate(90);
		hom2.superposition(hom4);

		hom4.rotate(90);
		hom2.superposition(hom4);

		hom4.rotate(90);
		hom2.superposition(hom4);

		hom.superposition(hom2);
		
	}
	
	@Before
	public void initSegelumgebung2(){
		homS2 = new HomogenousField(1.0);
		BorderField b = new BorderField(10.0, Proportionality.INVERSELY_EXPONENTIAL);
		b.translate(-11, 0);
		homS2.superposition(b);
		b.rotate(180);
		homS2.superposition(b);
		
		RadialField rad = new RadialField(10.0,1.0,Proportionality.INVERSELY_EXPONENTIAL);
		rad.translate(0, -5);
		homS2.superposition(rad);
		rad.translate(0, 10);
		homS2.superposition(rad); 
		homS2.scaleArea(new Circle(1.0, new Vector2D(0,-5)), 0);
		homS2.scaleArea(new Circle(1.0, new Vector2D(0,5)), 0);
		
		HomogenousField hom2 = new HomogenousField(0);
		HomogenousField hom3 = new HomogenousField(0.5);
		hom3.mask(new Rectangle(4,5, new Vector2D(6,-7.5)));
		hom2.superposition(hom3);
		hom3.translate(-12, 15);
		hom2.superposition(hom3);
		
		HomogenousField hom4 = new HomogenousField(0.5);
		hom4.mask(new Rectangle(4,10, new Vector2D(6,0)));
		hom4.shear(-1.2);
		hom4.translate(-5.5, 0);
		hom2.superposition(hom4);
		
		homS2.superposition(hom2);
	}
	
	@Test
	public void testEqualityS1(){
		//Field dimetions : X -10 - 10, Y -10 - 10
		double reselution = 0.20079;
		for(double x = -10; x <= 10; x += reselution) {
			for(double y = -10; y <= 10; y += reselution) {
				if(hom.getFunctionValueOptimized(x, y).distance((hom.getFunctionValue(x, y))) 
						>= 0.000001) {
					System.out.println(x + "" + y + hom.getFunctionValueOptimized(x, y) + "\t " + hom.getFunctionValue(x, y) );
				}
				Assert.assertTrue(hom.getFunctionValueOptimized(x, y).distance((hom.getFunctionValue(x, y))) 
						<= 0.000001);
				
			}
		}
	}
	
	@Test
	public void testEqualityS2(){
		//Field dimetions : X -10 - 10, Y -10 - 10
		double reselution = 0.20079;
		for(double x = -10; x <= 10; x += reselution) {
			for(double y = -10; y <= 10; y += reselution) {
				if(homS2.getFunctionValueOptimized(x, y).distance((homS2.getFunctionValue(x, y))) 
						>= 0.000001) {
					System.out.println(x + "" + y + homS2.getFunctionValueOptimized(x, y) + "\t " + homS2.getFunctionValue(x, y) );
				}
				Assert.assertTrue(homS2.getFunctionValueOptimized(x, y).distance((homS2.getFunctionValue(x, y))) 
						<= 0.000001);
				
			}
		}
	}
}
