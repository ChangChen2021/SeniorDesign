package test;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Before;
import org.junit.Test;

import main.fields.BorderField;
import main.fields.HomogenousField;
import main.fields.VectorField;
import main.util.Proportionality;
import main.util.shape.VerticalTunnel;

/**
 * Test course corridor
 * 
 * @author Manuela Kastner
 *
 */
public class Test_CourseCorridor {
	@Before
	public void init() {
		VectorField.setExportPath("./testResults/Test_CourseCorridor");
		VectorField.setSavingPath("./testResults/Test_CourseCorridor");
	}

	@Test
	public void test() {

		BorderField border1 = new BorderField(1, Proportionality.INVERSELY_QUADRATIC);
		border1.translate(-5, 0);
		border1.save("border1");
		border1.export(new Vector2D(-2, 2), 0.2, new Vector2D(-2, 2), 0.2, "border1");

		BorderField border2 = new BorderField(1, Proportionality.INVERSELY_QUADRATIC);
		border2.rotate(180.0);
		border2.translate(5, 0.0);
		border1.save("border2");
		border2.export(new Vector2D(-2, 2), 0.2, new Vector2D(-2, 2), 0.2, "border2");

		HomogenousField course = new HomogenousField(0.05);
		course.mask(new VerticalTunnel(2));
		border1.save("course");
		course.export(new Vector2D(-2, 2), 0.2, new Vector2D(-2, 2), 0.2, "course");

		border1.superposition(border2);
		border1.superposition(course);
		border1.export(new Vector2D(-2, 2), 0.2, new Vector2D(-2, 2), 0.2, "courseCorridor");

		border1.save("courseCorridor");

	}
}
