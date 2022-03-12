package test;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Before;
import org.junit.Test;

import main.fields.HomogenousField;
import main.fields.RadialField;
import main.fields.VectorField;
import main.util.Axis;
import main.util.Proportionality;
import main.util.shape.Circle;
import main.util.shape.Ellipse;
import main.util.shape.Rectangle;
import main.util.shape.Square;
import main.util.shape.VerticalTunnel;

/**
 * Test all operations on a homogenous field (used for chapter 6)
 * 
 * @author Manuela Kastner
 *
 */
public class Test_Operations_Radial {
	RadialField radial = new RadialField(1.0, 1.0, Proportionality.NONE);
	double input_x = 0.0;
	double input_y = 0.0;

	Vector2D expected;
	Vector2D actual;

	double delta = 0.00000001;

	@Before
	public void init() {
		VectorField.setExportPath("./testResults/Test_Operations_Radial");
		VectorField.setSavingPath("./testResults/Test_Operations_Radial");
		radial.rotate(45);
		radial.save("originalField");
		radial.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.5, "originalField");
	}

	@Test
	public void testRotateAroundOrigin() {
		radial.mask(new Square(2.0));
		radial.rotate(45.0);

		radial.save("testRotateAroundOrigin");
		radial.export(new Vector2D(-4.0, 4.0), 0.2, new Vector2D(-4.0, 4.0), 0.2, "testRotateAroundOrigin");
	}

	@Test
	public void testRotateAroungPoint() {
		// hom.mask(new Square(2.0, new Vector2D(-1.2,1.5)));
		radial.mask(new Square(2.0));
		radial.rotate(45.0, new Vector2D(-1.5, 1.5));

		radial.save("testRotateAroundPoint");
		radial.export(new Vector2D(-4.0, 4.0), 0.2, new Vector2D(-4.0, 4.0), 0.2, "testRotateAroundPoint");
	}

	@Test
	public void testScale() {
		radial.mask(new Square(2.0, new Vector2D(-1.0, 1.0)));
		radial.scale(2.0);

		radial.save("testScale");
		radial.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.5, "testScale");
	}

	@Test
	public void testSuperposition() {
		HomogenousField hom2 = new HomogenousField(2.0);
		hom2.mask(new Ellipse(2.6, 1.6, new Vector2D(-1.0, 1.0)));
		hom2.rotate(45, new Vector2D(-1.0, 1.0));
		hom2.export(new Vector2D(-4.0, 4.0), 0.2, new Vector2D(-4.0, 4.0), 0.2, "testSuperposition1");
		radial.mask(new Ellipse(2.6, 1.6, new Vector2D(1.5, 1.0)));
		radial.rotate(-45, new Vector2D(1.5, 1.0));
		radial.export(new Vector2D(-4.0, 4.0), 0.2, new Vector2D(-4.0, 4.0), 0.2, "testSuperposition2");
		radial.superposition(hom2);

		radial.save("testSuperposition");
		radial.export(new Vector2D(-4.0, 4.0), 0.2, new Vector2D(-4.0, 4.0), 0.2, "testSuperposition");
	}

	@Test
	public void testMaskCircle() {
		radial.mask(new Circle(2.0));
		radial.save("testMaskCircle");
		radial.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.2, "testMaskCircle");
	}
	
	@Test
	public void testMaskCircleNegativeRadius() {
		radial.mask(new Circle(-2.0));
		radial.save("testMaskCircleNegativeRadius");
		radial.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.2, "testMaskCircleNegativeRadius");
	}

	@Test
	public void testMaskEllipse() {
		radial.mask(new Ellipse(3.0, 2.0));
		radial.save("testMaskEllipse");
		radial.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.2, "testMaskEllipse");
	}
	
	@Test
	public void testMaskEllipseNegativeRadius() {
		radial.mask(new Ellipse(-3.0, -2.0));
		radial.save("testMaskEllipseNegativeRadius");
		radial.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.2, "testMaskEllipseNegativeRadius");
	}

	@Test
	public void testMaskRectangle() {
		radial.mask(new Rectangle(3.0, 2.0));
		radial.save("testMaskRectangle");
		radial.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.5, "testMaskRectangle");
	}
	
	@Test
	public void testMaskRectangleNegativeSize() {
		radial.mask(new Rectangle(-3.0, -2.0));
		radial.save("testMaskRectangleNegativeSize");
		radial.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.5, "testMaskRectangleNegativeSize");
	}

	@Test
	public void testMaskSquare() {
		radial.mask(new Square(2.0));
		radial.save("testMaskSquare");
		radial.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.5, "testMaskSquare");
	}
	
	@Test
	public void testMaskSquareNegativeSize() {
		radial.mask(new Square(-2.0));
		radial.save("testMaskSquareNegativeSize");
		radial.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.5, "testMaskSquareNegativeSize");
	}

	@Test
	public void testTranslateMaskedSquare() {
		radial.mask(new Square(2.0));
		radial.translate(1.2, 1.3);

		radial.save("testTranslateMaskedSquare");
		radial.export(new Vector2D(-4.0, 4.0), 0.2, new Vector2D(-4.0, 4.0), 0.2, "testTranslateMaskedSquare");
	}

	@Test
	public void testTranslateMaskedSquare2() {
		radial.mask(new Square(2.0));
		radial.translate(-1.2, -1.3);

		radial.save("testTranslateMaskedSquare2");
		radial.export(new Vector2D(-4.0, 4.0), 0.2, new Vector2D(-4.0, 4.0), 0.2, "testTranslateMaskedSquare2");
	}

	@Test
	public void testShearMaskedSquare() {
		radial.mask(new Square(2.0, new Vector2D(0, -1.0)));
		radial.shear(0.6);

		radial.save("testShearMaskedSquare");
		radial.export(new Vector2D(-4.0, 4.0), 0.2, new Vector2D(-4.0, 4.0), 0.2, "testShearMaskedSquare");
	}

	@Test
	public void testRotateTranslatedMaskedSquareAroundPoint() {
		radial.mask(new Square(2.0));
		radial.translate(1, 1);
		radial.rotate(-45.0, new Vector2D(1, 1));

		radial.save("testRotateTranslatedMaskedSquareAroundPoint");
		radial.export(new Vector2D(-4.0, 4.0), 0.2, new Vector2D(-4.0, 4.0), 0.2,
				"testRotateTranslatedMaskedSquareAroundPoint");
	}

	@Test
	public void testMaskVerticalTunnel() {
		radial.mask(new VerticalTunnel(2.0));

		radial.save("testMaskVerticalTunnel");
		radial.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.5, "testMaskVerticalTunnel");
	}
	
	@Test
	public void testMaskVerticalTunnelNegativeWidth() {
		radial.mask(new VerticalTunnel(-2.0));

		radial.save("testMaskVerticalTunnelNegativeWidth");
		radial.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.5, "testMaskVerticalTunnelNegativeWidth");
	}

	@Test
	public void testScaleSquare() {
		radial.scaleArea(new Square(3), 2);

		radial.save("testScaleSquare");
		radial.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.5, "testScaleSquare");
	}

	@Test
	public void testScaleSquare2() {
		radial.scaleArea(new Square(3, new Vector2D(-1.2, -0.7)), -2);

		radial.save("testScaleSquare2");
		radial.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.5, "testScaleSquare2");
	}

	@Test
	public void testShearX() {
		radial.mask(new Square(2.0));
		radial.shear(2.0, Axis.X);

		radial.save("testShearX");
		radial.export(new Vector2D(-4.0, 4.0), 0.2, new Vector2D(-4.0, 4.0), 0.2, "testShearX");
	}

	@Test
	public void testRotateShearY() {
		radial.mask(new Square(2.0));
		
		radial.shear(1.0, Axis.Y);

		radial.save("testShearY");
		radial.export(new Vector2D(-4.0, 4.0), 0.2, new Vector2D(-4.0, 4.0), 0.2, "testShearY");
	}

}
