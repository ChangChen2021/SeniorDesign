package testOptimized;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.AfterClass;
import org.junit.Assert;
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

public class TestNewSuperpositionFunction {
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
		    
		    @SuppressWarnings("deprecation")
			@Test
			public void testOldVsNewHomTan() {
				HomogenousField hom = new HomogenousField();
				TangentialField tan = new TangentialField();
				
				VectorField newSup = VectorField.superposition(hom, tan);
				hom.superposition(tan);
				Assert.assertTrue(Equal.equalFields(newSup, hom));
			}
			
			@SuppressWarnings("deprecation")
			@Test
			public void testOldVsNewMultiple() {
				HomogenousField hom = new HomogenousField();
				TangentialField tan = new TangentialField();
				RadialField rad = new RadialField();
				
				VectorField sup1 = VectorField.superposition(hom, tan);
				VectorField sup2 = VectorField.superposition(sup1, rad);
				hom.superposition(tan);
				hom.superposition(rad);
				Assert.assertTrue(Equal.equalFields(sup2, hom));
			}
			
			@Test
			public void testNewFieldsUnchanged() {
				HomogenousField hom1 = new HomogenousField();
				TangentialField tan1 = new TangentialField();
				
				HomogenousField hom2 = new HomogenousField();
				TangentialField tan2 = new TangentialField();
				
				VectorField sup1 = VectorField.superposition(hom1, tan1);
				VectorField sup2 = VectorField.superposition(hom2, tan2);
				
				hom1.rotate(90);
				tan1.shear(2);

				Assert.assertTrue(Equal.equalFields(sup1, sup2));
			}
			
			@SuppressWarnings("deprecation")
			@Test
			public void testNewFieldsOperations() {
				HomogenousField hom1 = new HomogenousField();
				TangentialField tan1 = new TangentialField();
				
				HomogenousField hom2 = new HomogenousField();
				TangentialField tan2 = new TangentialField();
				
				hom1.rotate(90);
				tan1.shear(2);
				
				hom2.rotate(90);
				tan2.shear(2);
				
				VectorField sup1 = VectorField.superposition(hom1, tan1);
				hom2.superposition(tan2);
				
				sup1.rotate(90, new Vector2D(2,1));
				sup1.shear(2, Axis.Y);
				
				hom2.rotate(90, new Vector2D(2,1));
				hom2.shear(2, Axis.Y);
				Assert.assertTrue(Equal.equalFields(sup1, hom2));
			}
			
			@SuppressWarnings("deprecation")
			@Test
			public void testOldVsNewTreeDepth2() {
				HomogenousField hom = new HomogenousField();
				TangentialField tan = new TangentialField();
				
				BorderField bor = new BorderField();
				RadialField rad = new RadialField();
				
				VectorField sup1 = VectorField.superposition(hom, tan);
				VectorField sup2 = VectorField.superposition(bor, rad);
				VectorField sup3 = VectorField.superposition(sup1, sup2);
				
				hom.superposition(tan);
				hom.superposition(rad);
				hom.superposition(bor);

				Assert.assertTrue(Equal.equalFields(sup3, hom));
			}
			
			@Test
			public void testMoreThanTwoFields() {
				HomogenousField hom = new HomogenousField();
				TangentialField tan = new TangentialField();
				RadialField rad = new RadialField();
				BorderField bor = new BorderField();
				
				VectorField sup = VectorField.superposition(hom, tan, rad, bor);
				VectorField supBin = VectorField.superposition(
						VectorField.superposition(hom, tan),
						VectorField.superposition(rad, bor));

				Assert.assertTrue(Equal.equalFields(sup, supBin));
			}
			
			
}
