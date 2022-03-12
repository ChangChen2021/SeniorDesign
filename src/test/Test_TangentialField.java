package test;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Before;
import org.junit.Test;

import main.fields.TangentialField;
import main.fields.VectorField;
import main.util.Proportionality;

/**
 * Test TangentialField with all possible proportionalities.
 * 
 * @author Manuela Kastner
 *
 */
public class Test_TangentialField {

	@Before
	public void init() {
		VectorField.setExportPath("./testResults/Test_TangentialField");
		VectorField.setSavingPath("./testResults/Test_TangentialField");
	}

	@Test
	public void test1() {
		TangentialField tangential = new TangentialField(2.0, 4.0, Proportionality.INVERSELY_EXPONENTIAL);
		tangential.save("tangentialInverselyExponential");
		tangential.export(new Vector2D(-10, 10),  1.0,new Vector2D(-10, 10), 1.0, "tangentialInverselyExponential");
	}

	@Test
	public void test2() {
		TangentialField tangential = new TangentialField(1, 1, Proportionality.LINEAR);
		tangential.save("tangentialLinear");
		tangential.export(new Vector2D(-4, 4), 0.4,new Vector2D(-4, 4), 0.4, "tangentialLinear");
	}

	@Test
	public void test3() {
		TangentialField tangential = new TangentialField(1, 1, Proportionality.INVERSELY_LINEAR);
		tangential.save("tangentialInverselyLinear");
		tangential.export(new Vector2D(-4, 4),0.4, new Vector2D(-4, 4), 0.4, "tangentialInverselyLinear");
	}

	@Test
	public void test4() {
		TangentialField tangential = new TangentialField(1, 1, Proportionality.QUADRATIC);
		tangential.save("tangentialQuadratic");
		tangential.export(new Vector2D(-4, 4), 0.4,new Vector2D(-4, 4), 0.4, "tangentialQuadratic");
	}

	@Test
	public void test5() {
		TangentialField tangential = new TangentialField(1, 1, Proportionality.INVERSELY_QUADRATIC);
		tangential.save("tangentialInverselyQuadratic");
		tangential.export(new Vector2D(-4, 4), 0.4,new Vector2D(-4, 4), 0.4, "tangentialInverselyQuadratic");
	}

	@Test
	public void test6() {
		TangentialField tangential = new TangentialField(1, 1, Proportionality.CUBIC);
		tangential.save("tangentialCubic");
		tangential.export(new Vector2D(-4, 4), 0.4,new Vector2D(-4, 4), 0.4, "tangentialCubic");
	}

	@Test
	public void test7() {
		TangentialField tangential = new TangentialField(1, 1, Proportionality.INVERSELY_CUBIC);
		tangential.save("tangentialInverselyCubic");
		tangential.export(new Vector2D(-4, 4),0.4, new Vector2D(-4, 4), 0.4, "tangentialInverselyCubic");
	}

	@Test
	public void test8() {
		TangentialField tangential = new TangentialField(1, 1, Proportionality.EXPONENTIAL);
		tangential.save("tangentialExponential");
		tangential.export(new Vector2D(-4, 4), 0.4,new Vector2D(-4, 4), 0.4, "tangentialExponential");
	}

	@Test
	public void test9() {
		TangentialField tangential = new TangentialField(1, 1, Proportionality.LOGARITHMIC);
		tangential.save("tangentialLogarithmic");
		tangential.export(new Vector2D(-4, 4), 0.4,new Vector2D(-4, 4), 0.4, "tangentialLogarithmic");
	}

	@Test
	public void test10() {
		TangentialField tangential = new TangentialField(1, 1, Proportionality.INVERSELY_LOGARITHMIC);
		tangential.save("tangentialInverselyLogarithmic");
		tangential.export(new Vector2D(-4, 4), 0.4,new Vector2D(-4, 4), 0.4, "tangentialInverselyLogarithmic");
	}
	
	@Test
	public void test11() {
		TangentialField tangential = new TangentialField(-1, 1, Proportionality.NONE);
		tangential.save("tangentialNone");
		tangential.export(new Vector2D(-2, 2), 0.5,new Vector2D(-2, 2), 0.5, "tangentialNone");
	}
	
	@Test
	public void test12() {
		TangentialField tangential = new TangentialField();
		tangential.save("tangentialDefault");
		tangential.export(new Vector2D(-2, 2), 0.5,new Vector2D(-2, 2), 0.5, "tangentialDefault");
	}
}
