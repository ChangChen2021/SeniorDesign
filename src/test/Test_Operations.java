package test;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Before;
import org.junit.Test;

import main.fields.HomogenousField;
import main.fields.VectorField;
import main.util.Axis;
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
public class Test_Operations {
	HomogenousField hom = new HomogenousField(1.0);
	double input_x = 0.0;
	double input_y = 0.0;

	Vector2D expected;
	Vector2D actual;

	double delta = 0.00000001;

	@Before
	public void init() {
		VectorField.setExportPath("./testResults/Test_Operations");
		VectorField.setSavingPath("./testResults/Test_Operations");
		hom.rotate(45);
		hom.save("originalField");
		hom.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.5, "originalField");
	}

	@Test
	public void testRotateAroundOrigin() {
		hom.mask(new Square(2.0));
		hom.rotate(45.0);

		hom.save("testRotateAroundOrigin");
		hom.export(new Vector2D(-4.0, 4.0), 0.2, new Vector2D(-4.0, 4.0), 0.2, "testRotateAroundOrigin");
	}

	@Test
	public void testRotateAroungPoint() {
		// hom.mask(new Square(2.0, new Vector2D(-1.2,1.5)));
		hom.mask(new Square(2.0));
		hom.rotate(45.0, new Vector2D(-1.5, 1.5));

		hom.save("testRotateAroundPoint");
		hom.export(new Vector2D(-4.0, 4.0), 0.2, new Vector2D(-4.0, 4.0), 0.2, "testRotateAroundPoint");
	}

	@Test
	public void testScale() {
		hom.mask(new Square(2.0, new Vector2D(-1.0, 1.0)));
		hom.scale(2.0);

		hom.save("testScale");
		hom.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.5, "testScale");
	}

	@Test
	public void testSuperposition() {
		HomogenousField hom2 = new HomogenousField(2.0);
		hom2.mask(new Ellipse(2.6, 1.6, new Vector2D(-1.0, 1.0)));
		hom2.rotate(45, new Vector2D(-1.0, 1.0));
		hom2.export(new Vector2D(-4.0, 4.0), 0.2, new Vector2D(-4.0, 4.0), 0.2, "testSuperposition1");
		hom.mask(new Ellipse(2.6, 1.6, new Vector2D(1.5, 1.0)));
		hom.rotate(-45, new Vector2D(1.5, 1.0));
		hom.export(new Vector2D(-4.0, 4.0), 0.2, new Vector2D(-4.0, 4.0), 0.2, "testSuperposition2");
		hom.superposition(hom2);

		hom.save("testSuperposition");
		hom.export(new Vector2D(-4.0, 4.0), 0.2, new Vector2D(-4.0, 4.0), 0.2, "testSuperposition");
	}
	
	@Test
	public void testSuperpositionWithSameField() {
		hom.superposition(hom);
		hom.save("testSuperpositionWithSameField");
		hom.export(new Vector2D(-4.0, 4.0), 0.2, new Vector2D(-4.0, 4.0), 0.2, "testSuperpositionWithSameField");
	}

	@Test
	public void testMaskCircleInCentre() {
		hom.mask(new Circle(2.0));
		hom.save("testMaskCircleInCentre");
		hom.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.2, "testMaskCircleInCentre");
	}
	
	@Test
	public void testMaskCircle() {
		hom.mask(new Circle(2.0, new Vector2D(1.2,-1.2)));
		hom.save("testMaskCircle");
		hom.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.2, "testMaskCircle");
	}
	
	@Test
	public void testMaskCircleNegativeRadius() {
		hom.mask(new Circle(-2.0));
		hom.save("testMaskCircleNegativeRadius");
		hom.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.2, "testMaskCircleNegativeRadius");
	}

	@Test
	public void testMaskEllipseInCentre() {
		hom.mask(new Ellipse(3.0, 2.0));
		hom.save("testMaskEllipseInCentre");
		hom.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.2, "testMaskEllipseInCentre");
	}
	
	@Test
	public void testMaskEllipse() {
		hom.mask(new Ellipse(3.0, 2.0, new Vector2D(1.2,-1.2)));
		hom.save("testMaskEllipse");
		hom.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.2, "testMaskEllipse");
	}
	
	@Test
	public void testMaskEllipseNegativeRadius() {
		hom.mask(new Ellipse(-3.0, -2.0));
		hom.save("testMaskEllipseNegativeRadius");
		hom.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.2, "testMaskEllipseNegativeRadius");
	}

	@Test
	public void testMaskRectangleInCentre() {
		hom.mask(new Rectangle(3.0, 2.0));
		hom.save("testMaskRectangleInCentre");
		hom.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.5, "testMaskRectangleInCentre");
	}
	
	@Test
	public void testMaskRectangle() {
		hom.mask(new Rectangle(3.0, 2.0, new Vector2D(1.2,-1.2)));
		hom.save("testMaskRectangle");
		hom.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.5, "testMaskRectangle");
	}
	
	@Test
	public void testMaskRectangleNegativeSize() {
		hom.mask(new Rectangle(-3.0, -2.0));
		hom.save("testMaskRectangleNegativeSize");
		hom.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.5, "testMaskRectangleNegativeSize");
	}

	@Test
	public void testMaskSquareInCentre() {
		hom.mask(new Square(2.0));
		hom.save("testMaskSquareInCentre");
		hom.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.5, "testMaskSquareInCentre");
	}
	
	@Test
	public void testMaskSquare() {
		hom.mask(new Square(2.0, new Vector2D(1.2,-1.2)));
		hom.save("testMaskSquare");
		hom.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.5, "testMaskSquare");
	}
	
	@Test
	public void testMaskSquareNegativeSize() {
		hom.mask(new Square(-2.0));
		hom.save("testMaskSquareNegativeSize");
		hom.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.5, "testMaskSquareNegativeSize");
	}

	@Test
	public void testTranslateMaskedSquare() {
		hom.mask(new Square(2.0));
		hom.translate(1.2, 1.3);

		hom.save("testTranslateMaskedSquare");
		hom.export(new Vector2D(-4.0, 4.0), 0.2, new Vector2D(-4.0, 4.0), 0.2, "testTranslateMaskedSquare");
	}

	@Test
	public void testTranslateMaskedSquare2() {
		hom.mask(new Square(2.0));
		hom.translate(-1.2, -1.3);

		hom.save("testTranslateMaskedSquare2");
		hom.export(new Vector2D(-4.0, 4.0), 0.2, new Vector2D(-4.0, 4.0), 0.2, "testTranslateMaskedSquare2");
	}

	@Test
	public void testShearMaskedSquare() {
		hom.mask(new Square(2.0, new Vector2D(0, -1.0)));
		hom.shear(0.6);

		hom.save("testShearMaskedSquare");
		hom.export(new Vector2D(-4.0, 4.0), 0.2, new Vector2D(-4.0, 4.0), 0.2, "testShearMaskedSquare");
	}

	@Test
	public void testRotateTranslatedMaskedSquareAroundPoint() {
		hom.mask(new Square(2.0));
		hom.translate(1, 1);
		hom.rotate(-45.0, new Vector2D(1, 1));

		hom.save("testRotateTranslatedMaskedSquareAroundPoint");
		hom.export(new Vector2D(-4.0, 4.0), 0.2, new Vector2D(-4.0, 4.0), 0.2,
				"testRotateTranslatedMaskedSquareAroundPoint");
	}

	@Test
	public void testMaskVerticalTunnelInCentre() {
		hom.mask(new VerticalTunnel(2.0));

		hom.save("testMaskVerticalTunnelInCentre");
		hom.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.5, "testMaskVerticalTunnelInCentre");
	}
	
	@Test
	public void testMaskVerticalTunnel() {
		hom.mask(new VerticalTunnel(2.0, 1.2));

		hom.save("testMaskVerticalTunnel");
		hom.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.5, "testMaskVerticalTunnel");
	}
	
	@Test
	public void testMaskVerticalTunnelNegativeWidth() {
		hom.mask(new VerticalTunnel(-2.0));

		hom.save("testMaskVerticalTunnelNegativeWidth");
		hom.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.5, "testMaskVerticalTunnelNegativeWidth");
	}

	@Test
	public void testScaleSquare() {
		hom.scaleArea(new Square(3), 2);

		hom.save("testScaleSquare");
		hom.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.5, "testScaleSquare");
	}

	@Test
	public void testScaleSquare2() {
		hom.scaleArea(new Square(3, new Vector2D(-1.2, -0.7)), -2);

		hom.save("testScaleSquare2");
		hom.export(new Vector2D(-4.0, 4.0), 0.5, new Vector2D(-4.0, 4.0), 0.5, "testScaleSquare2");
	}

	@Test
	public void testShearX() {
		hom.mask(new Square(2.0));
		hom.shear(2.0, Axis.X);

		hom.save("testShearX");
		hom.export(new Vector2D(-4.0, 4.0), 0.2, new Vector2D(-4.0, 4.0), 0.2, "testShearX");
	}

	@Test
	public void testRotateShearY() {
		hom.mask(new Square(2.0));
		
		hom.shear(1.0, Axis.Y);

		hom.save("testShearY");
		hom.export(new Vector2D(-4.0, 4.0), 0.2, new Vector2D(-4.0, 4.0), 0.2, "testShearY");
	}
	
	@Test
	public void testSenselessGrid() {
		hom.save("testSenselessGrid");
		hom.export(new Vector2D(4.0, -4.0), 0.2, new Vector2D(4.0, -4.0), 0.2, "testSenselessGrid");
	}
	
//	@Test
//	public void testExportGridWidthZero() {
//		hom.export(new Vector2D(-4.0, 4.0), 0.0, new Vector2D(-4.0, 4.0), 0.0, "testExportGridWidthZero");
//	}

}
