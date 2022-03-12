package test;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import main.fields.BorderField;
import main.fields.HomogenousField;
import main.fields.RadialField;
import main.fields.TangentialField;
import main.fields.VectorField;
import main.util.Proportionality;
import main.util.shape.Rectangle;

public class Test_Segelumgebung {
	public static void main(String[] args) {
		VectorField.setSavingPath("./testResults/Test_Segelumgebung");
		VectorField.setExportPath("./testResults/Test_Segelumgebung");

		double gridWidth = 0.5;

		HomogenousField hom = new HomogenousField(0);
		BorderField b = new BorderField(10.0, Proportionality.INVERSELY_EXPONENTIAL);
		b.translate(-11, 0);
		hom.superposition(b);

		b.rotate(90);

		hom.superposition(b);
		b.rotate(90);
		hom.superposition(b);
		b.rotate(90);
		hom.superposition(b);

		hom.export(new Vector2D(-10, 10), gridWidth, new Vector2D(-10, 10), gridWidth, "border");

		RadialField rad = new RadialField(10.0, 1.0, Proportionality.INVERSELY_EXPONENTIAL);
		hom.superposition(rad);
		hom.export(new Vector2D(-10, 10), gridWidth, new Vector2D(-10, 10), gridWidth, "isle");

		TangentialField tan = new TangentialField(3.0, 1.0, Proportionality.INVERSELY_EXPONENTIAL);
		hom.superposition(tan);
		hom.export(new Vector2D(-10, 10), gridWidth, new Vector2D(-10, 10), gridWidth, "isle2");

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
		hom2.export(new Vector2D(-10, 10), gridWidth, new Vector2D(-10, 10), gridWidth, "course");

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

		hom2.export(new Vector2D(-10, 10), gridWidth, new Vector2D(-10, 10), gridWidth, "course2");

		hom.superposition(hom2);
		hom.export(new Vector2D(-10, 10), gridWidth, new Vector2D(-10, 10), gridWidth, "done");

		hom.save("segelumgebung");

	}
}
