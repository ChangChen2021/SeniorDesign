package test;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Before;
import org.junit.Test;

import main.fields.BorderField;
import main.fields.VectorField;
import main.util.Proportionality;

/**
 * Test border field
 * 
 * @author Manuela Kastner
 *
 */
public class Test_BorderField {

	@Before
	public void init() {
		VectorField.setExportPath("./testResults/Test_BorderField");
		VectorField.setSavingPath("./testResults/Test_BorderField");
	}

	@Test
	public void test1() {
		BorderField border = new BorderField(2.0, Proportionality.LINEAR);

		border.save("borderLinear");
		border.export(new Vector2D(-10, 10), 1.0, new Vector2D(-10, 10), 1.0, "borderLinear");

		border.rotate(90.0);
		border.save("borderLinearRotate");
		border.export(new Vector2D(-10, 10), 1.0, new Vector2D(-10, 10), 1.0, "borderLinearRotate");
	}

	@Test
	public void test2() {
		BorderField border = new BorderField();

		border.save("borderDefault");
		border.export(new Vector2D(0, 4), 0.5, new Vector2D(-2, 2), 0.5, "borderDefault");
	}

	@Test
	public void test3() {
		BorderField border = new BorderField(1.0, Proportionality.NONE);

		border.save("border");
		border.export(new Vector2D(0, 4), 0.5, new Vector2D(-2, 2), 0.5, "border");
	}

	@Test
	public void test4() {
		BorderField border = new BorderField(-1.0, Proportionality.INVERSELY_EXPONENTIAL);

		border.save("borderNegativeValue");
		border.export(new Vector2D(0.5, 4), 0.5, new Vector2D(-2, 2), 0.5, "borderNegativeValue");
	}

	@Test
	public void test5() {
		BorderField border = new BorderField(1.0, Proportionality.INVERSELY_EXPONENTIAL);
		border.rotate(180, new Vector2D(4, 0));

		border.save("borderRotate");
		border.export(new Vector2D(0.5, 4), 0.5, new Vector2D(-2, 2), 0.5, "borderRotate");
	}
	
	// all other proportionalities
	@Test
	public void test6() {
		BorderField border = new BorderField(1.0, Proportionality.INVERSELY_LINEAR);

		border.save("borderInverselyLinear");
		border.export(new Vector2D(-10, 10), 1.0, new Vector2D(-10, 10), 1.0, "borderInverselyLinear");
	}
	@Test
	public void test7() {
		BorderField border = new BorderField(1.0, Proportionality.QUADRATIC);

		border.save("borderQuadratic");
		border.export(new Vector2D(-10, 10), 1.0, new Vector2D(-10, 10), 1.0, "borderQuadratic");
	}
	@Test
	public void test8() {
		BorderField border = new BorderField(1.0, Proportionality.INVERSELY_QUADRATIC);

		border.save("borderInverselyQuadratic");
		border.export(new Vector2D(-10, 10), 1.0, new Vector2D(-10, 10), 1.0, "borderInverselyQuadratic");
	}
	@Test
	public void test9() {
		BorderField border = new BorderField(1.0, Proportionality.CUBIC);

		border.save("borderCubic");
		border.export(new Vector2D(-10, 10), 1.0, new Vector2D(-10, 10), 1.0, "borderCubic");
	}
	@Test
	public void test10() {
		BorderField border = new BorderField(1.0, Proportionality.INVERSELY_CUBIC);

		border.save("borderInverselyCubic");
		border.export(new Vector2D(-10, 10), 1.0, new Vector2D(-10, 10), 1.0, "borderInverselyCubic");
	}
	@Test
	public void test11() {
		BorderField border = new BorderField(1.0, Proportionality.EXPONENTIAL);

		border.save("borderExponential");
		border.export(new Vector2D(-10, 10), 1.0, new Vector2D(-10, 10), 1.0, "borderExponential");
	}
	@Test
	public void test12() {
		BorderField border = new BorderField(1.0, Proportionality.LOGARITHMIC);

		border.save("borderLogarithmic");
		border.export(new Vector2D(-10, 10), 1.0, new Vector2D(-10, 10), 1.0, "borderLogarithmic");
	}
	@Test
	public void test13() {
		BorderField border = new BorderField(1.0, Proportionality.INVERSELY_LOGARITHMIC);

		border.save("borderInverselyLogarithmic");
		border.export(new Vector2D(2, 10), 1.0, new Vector2D(-10, 10), 1.0, "borderInverselyLogarithmic");
	}
	
	
}
