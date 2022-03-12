package test;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Before;
import org.junit.Test;

import main.fields.HomogenousField;
import main.fields.VectorField;

/**
 * Test homogenous field
 * 
 * @author Manuela Kastner
 *
 */
public class Test_HomogenousField {

	double delta = 0.00000001;

	@Before
	public void init() {
		VectorField.setExportPath("./testResults/Test_HomogenousField");
		VectorField.setSavingPath("./testResults/Test_HomogenousField");
	}

	@Test
	public void test1() {
		HomogenousField hom = new HomogenousField();

		Vector2D expected = hom.getFunctionValue(-1.2, -2.5);
		Vector2D actual = new Vector2D(0, 1);

		assertEquals(expected.getX(), actual.getX(), delta);
		assertEquals(expected.getY(), actual.getY(), delta);

		hom.save("homogenousDefault");
		hom.export(new Vector2D(-2, 2), 0.5, new Vector2D(-2, 2), 1.0, "homogenousDefault");
	}

	@Test
	public void test2() {
		HomogenousField hom = new HomogenousField(-3.2);
		Vector2D expected = hom.getFunctionValue(-1.2, -2.5);
		Vector2D actual = new Vector2D(0, -3.2);

		assertEquals(expected.getX(), actual.getX(), delta);
		assertEquals(expected.getY(), actual.getY(), delta);

		hom.save("homogenous");
		hom.export(new Vector2D(-2, 2), 0.5, new Vector2D(-2, 2), 1.0, "homogenous");
	}
	
	@Test
	public void test3() {
		HomogenousField hom = new HomogenousField(2.2);
		Vector2D expected = hom.getFunctionValue(-1.2, -2.5);
		Vector2D actual = new Vector2D(0, 2.2);

		assertEquals(expected.getX(), actual.getX(), delta);
		assertEquals(expected.getY(), actual.getY(), delta);

		hom.save("homogenous");
		hom.export(new Vector2D(-2, 2), 0.5, new Vector2D(-2, 2), 1.0, "homogenous");
	}
}
