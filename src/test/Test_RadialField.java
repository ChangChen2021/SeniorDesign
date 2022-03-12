package test;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Before;
import org.junit.Test;

import main.fields.RadialField;
import main.fields.VectorField;
import main.util.Proportionality;

/**
 * Test RadialField with all possible proportionalities.
 * 
 * @author Manuela Kastner
 *
 */
public class Test_RadialField {

	@Before
	public void init() {
		VectorField.setExportPath("./testResults/Test_RadialField");
		VectorField.setSavingPath("./testResults/Test_RadialField");
	}

	@Test
	public void test1() {

		RadialField radial = new RadialField(2.0, 4.0, Proportionality.INVERSELY_EXPONENTIAL);

		radial.translate(0, 5);
		radial.translate(1, -4);

		radial.save("radialTranslateInverselyExponential");
		radial.export(new Vector2D(-10, 10), 1.0, new Vector2D(-10, 10), 1.0, "radialTranslateInverselyExponential");
	}

	@Test
	public void test2() {
		RadialField radial = new RadialField(1, 1, Proportionality.LINEAR);
		radial.save("radialLinear");
		radial.export(new Vector2D(-4, 4), 0.4,new Vector2D(-4, 4), 0.4, "radialLinear");
	}

	@Test
	public void test3() {
		RadialField radial = new RadialField(1, 1, Proportionality.INVERSELY_LINEAR);
		radial.save("radialInverselyLinear");
		radial.export(new Vector2D(-4, 4), 0.4,new Vector2D(-4, 4), 0.4, "radialInverselyLinear");
	}

	@Test
	public void test4() {
		RadialField radial = new RadialField(1, 1, Proportionality.QUADRATIC);
		radial.save("radialQuadratic");
		radial.export(new Vector2D(-4, 4), 0.4,new Vector2D(-4, 4), 0.4, "radialQuadratic");
	}

	@Test
	public void test5() {
		RadialField radial = new RadialField(1, 1, Proportionality.INVERSELY_QUADRATIC);
		radial.save("radialInverselyQuadratic");
		radial.export(new Vector2D(-4, 4), 0.4,new Vector2D(-4, 4), 0.4, "radialInverselyQuadratic");
	}

	@Test
	public void test6() {
		RadialField radial = new RadialField(1, 1, Proportionality.CUBIC);
		radial.save("radialCubic");
		radial.export(new Vector2D(-4, 4), 0.4,new Vector2D(-4, 4), 0.4, "radialCubic");
	}

	@Test
	public void test7() {
		RadialField radial = new RadialField(1, 1, Proportionality.INVERSELY_CUBIC);
		radial.save("radialInverselyCubic");
		radial.export(new Vector2D(-4, 4), 0.4,new Vector2D(-4, 4), 0.4, "radialInverselyCubic");
	}

	@Test
	public void test8() {
		RadialField radial = new RadialField(1, 1, Proportionality.EXPONENTIAL);
		radial.save("radialExponential");
		radial.export(new Vector2D(-4, 4), 0.4,new Vector2D(-4, 4), 0.4, "radialExponential");
	}

	@Test
	public void test9() {
		RadialField radial = new RadialField(1, 1, Proportionality.LOGARITHMIC);
		radial.save("radialLogarithmic");
		radial.export(new Vector2D(-4, 4), 0.4,new Vector2D(-4, 4), 0.4, "radialLogarithmic");
	}

	@Test
	public void test10() {
		RadialField radial = new RadialField(1, 1, Proportionality.INVERSELY_LOGARITHMIC);
		radial.save("radialInverselyLogarithmic");
		radial.export(new Vector2D(-4, 4),0.4, new Vector2D(-4, 4), 0.4, "radialInverselyLogarithmic");
	}
	
	@Test
	public void test11() {
		RadialField radial = new RadialField(1, 1, Proportionality.NONE);
		radial.save("radialNone");
		radial.export(new Vector2D(-2, 2),0.5, new Vector2D(-2, 2), 0.5, "radialNone");
	}
	
	@Test
	public void test12() {
		RadialField radial = new RadialField();
		radial.save("radialDefault");
		radial.export(new Vector2D(-2, 2),0.5, new Vector2D(-2, 2), 0.5, "radialDefault");
	}
	
	@Test
	public void test13() {
		RadialField radial = new RadialField(-1, 1, Proportionality.NONE);
		radial.save("radialNegativeValue");
		radial.export(new Vector2D(-2, 2),0.5, new Vector2D(-2, 2), 0.5, "radialNegativeValue");
	}
	
	@Test
	public void test14() {
		RadialField radial = new RadialField(1, -1, Proportionality.NONE);
		radial.save("radialNegativeRadius");
		radial.export(new Vector2D(-2, 2),0.5, new Vector2D(-2, 2), 0.5, "radialNegativeRadius");
	}
	
	

}
