package test;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Before;
import org.junit.Test;

import main.fields.BorderField;
import main.fields.HomogenousField;
import main.fields.RadialField;
import main.fields.VectorField;
import main.util.Proportionality;
import main.util.shape.Circle;

public class Test_Problems {
	@Before
	public void init() {
		VectorField.setExportPath("./testResults/Test_Problems");
		VectorField.setSavingPath("./testResults/Test_Problems");

	}
	
	@Test
	public void superpositionRadialAndHom() {
		
		RadialField radial = new RadialField(1, 0.5, Proportionality.INVERSELY_EXPONENTIAL);
		radial.save("problemRadial");
		radial.export(new Vector2D(-2, 2),0.2, new Vector2D(-2, 2), 0.2, "problemRadial");
		
		HomogenousField hom = new HomogenousField(0.2);
		hom.save("problemHom");
		hom.export(new Vector2D(-2, 2),0.2, new Vector2D(-2, 2), 0.2, "problemHom");
		
		radial.superposition(hom);
		radial.save("problemSuperposition");
		radial.export(new Vector2D(-2, 2),0.2, new Vector2D(-2, 2), 0.2, "problemSuperposition");
		
		radial.scaleArea(new Circle(0.5), 0.0);
		radial.save("problemSuperpositionSolved");
		radial.export(new Vector2D(-2, 2),0.2, new Vector2D(-2, 2), 0.2, "problemSuperpositionSolved");
	}
	
	@Test
	public void testOverflow() {
		BorderField border = new BorderField(0.1, Proportionality.INVERSELY_LINEAR);
		border.rotate(180.0);
		border.save("testOverflow");
		border.export(new Vector2D(-2, -0.1),0.2, new Vector2D(-2, -0.1), 0.2, "testOverflow");
	}
}
